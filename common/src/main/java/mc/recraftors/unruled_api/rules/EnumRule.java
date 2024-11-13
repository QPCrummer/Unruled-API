package mc.recraftors.unruled_api.rules;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import mc.recraftors.unruled_api.UnruledApi;
import mc.recraftors.unruled_api.utils.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public class EnumRule <T extends Enum<T>> extends GameRules.Rule<EnumRule<T>> implements GameruleAccessor<T> {
    private final Class<T> tClass;
    private T value;
    private IGameruleValidator<T> validator;
    private IGameruleAdapter<T> adapter;

    private static <U extends Enum<U>> void testValidator(Class<U> c, IGameruleValidator<U> v) {
        int i = 0;
        for (U u : c.getEnumConstants()) {
            if (v.validate(u)) i++;
        }
        if (i == 0) throw new UnsupportedOperationException("Validator needs to validate at least one enum entry");
    }

    public EnumRule(GameRules.Type<EnumRule<T>> type, Class<T> targetClass, T initialValue, IGameruleValidator<T> validator, IGameruleAdapter<T> adapter) {
        super(type);
        Objects.requireNonNull(targetClass);
        Objects.requireNonNull(initialValue);
        Objects.requireNonNull(validator);
        Objects.requireNonNull(adapter);
        testValidator(targetClass, validator);
        this.tClass = targetClass;
        this.value = initialValue;
        this.validator = validator;
        this.adapter = adapter;
    }

    public EnumRule(GameRules.Type<EnumRule<T>> type, Class<T> targetClass, T initialValue) {
        this(type, targetClass, initialValue, IGameruleValidator::alwaysTrue, Optional::of);
    }

    public static <T extends Enum<T>> GameRules.Type<EnumRule<T>> create(
            Class<T> targetClass, T initialValue, BiConsumer<MinecraftServer, EnumRule<T>> changeCallback,
            IGameruleValidator<T> validator, IGameruleAdapter<T> adapter) {
        return new GameRules.Type<>((EnumArgSupplier<T>) () -> targetClass,
                type -> new EnumRule<>(type, targetClass, initialValue, validator, adapter),
                changeCallback, ((consumer, key, cType) -> ((IGameRulesVisitor)consumer).unruled_visitEnum(key, cType)));
    }

    public static <T extends Enum<T>> GameRules.Type<EnumRule<T>> create(
            Class<T> targetClass, T initialValue, BiConsumer<MinecraftServer, EnumRule<T>> changeCallback) {
        return create(targetClass, initialValue, changeCallback, IGameruleValidator::alwaysTrue, Optional::of);
    }

    public static <T extends Enum<T>> GameRules.Type<EnumRule<T>> create(Class<T> targetClass, T initialValue,
            IGameruleValidator<T> validator, IGameruleAdapter<T> adapter) {
        return create(targetClass, initialValue, UnruledApi.empty(), validator, adapter);
    }

    public static <T extends Enum<T>> GameRules.Type<EnumRule<T>> create(Class<T> targetClass, T initialValue) {
        return create(targetClass, initialValue, UnruledApi.empty(), IGameruleValidator::alwaysTrue, Optional::of);
    }

    public Optional<T> parse(String input) {
        Objects.requireNonNull(input);
        try {
            return Optional.of(Enum.valueOf(tClass, input));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public boolean validate(String input) {
        return setFromStr(input);
    }

    private boolean setFromStr(String s) {
        Optional<T> o = parse(s);
        if (o.isEmpty()) return false;
        if (this.validator.validate(o.get())) {
            this.set(o.get());
            return true;
        }
        o = this.adapter.adapt(o.get());
        if (o.isPresent() && this.validator.validate(o.get())) {
            this.set(o.get());
            return true;
        }
        return false;
    }

    public T get() {
        return this.value;
    }

    private void set(T t) {
        this.value = t;
    }

    public void set(T value, MinecraftServer server) {
        this.bump(value, server);
    }

    private void bump(T value, MinecraftServer server) {
        if (value == null) return;
        boolean b = false;
        if (this.validator.validate(value)) {
            b = true;
        } else {
            Optional<T> o = this.adapter.adapt(value);
            if (o.isPresent() && this.validator.validate(o.get())) {
                value = o.get();
                b = true;
            }
        }
        if (b) {
            set(value);
            this.changed(server);
        }
    }

    public T[] values() {
        List<T> l = Arrays.stream(tClass.getEnumConstants()).filter(this.validator::validate).toList();
        T[] a = l.toArray(this.tClass.getEnumConstants());
        int i = 0;
        for (T t : a) {
            if (t == null) {
                break;
            }
            i++;
        }
        if (i == 0) {
            a[0] = this.value;
            i = 1;
        }
        a = Arrays.copyOf(a, i);
        return a;
    }

    @Override
    protected void setFromArgument(CommandContext<ServerCommandSource> context, String name) {
        String s = StringArgumentType.getString(context, name);
        setFromStr(s);
    }

    @Override
    protected void deserialize(String value) {
        setFromStr(value);
    }

    @Override
    public String serialize() {
        return this.value.name();
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public int getCommandResult() {
        return Collections.singletonList(this.tClass).indexOf(value) + 1;
    }

    @Override
    protected EnumRule<T> getThis() {
        return this;
    }

    @Override
    protected EnumRule<T> copy() {
        return new EnumRule<>(this.type, this.tClass, this.value, this.validator, this.adapter);
    }

    @Override
    public void setValue(EnumRule<T> rule, @Nullable MinecraftServer server) {
        T v = rule.get();
        this.bump(v, server);
    }

    public static <U extends Enum<U>> Iterable<String> getEnumNames(Class<U> target) {
        return Arrays.stream(target.getEnumConstants()).map(Enum::name).toList();
    }

    @Override
    public IGameruleValidator<T> unruled_getValidator() {
        return this.validator;
    }

    @Override
    public void unruled_setValidator(IGameruleValidator<T> validator) {
        Objects.requireNonNull(validator);
        testValidator(this.tClass, validator);
        this.validator = validator;
    }

    @Override
    public IGameruleAdapter<T> unruled_getAdapter() {
        return this.adapter;
    }

    @Override
    public void unruled_setAdapter(IGameruleAdapter<T> adapter) {
        this.adapter = Objects.requireNonNull(adapter);
    }
}
