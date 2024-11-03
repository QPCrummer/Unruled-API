package mc.recraftors.unruled_api.rules;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import mc.recraftors.unruled_api.utils.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public class EnumRule <T extends Enum<T>> extends GameRules.Rule<EnumRule<T>> implements GameruleAccessor<T> {
    private final Class<T> tClass;
    private T value;
    private IGameruleValidator<T> validator;
    private IGameruleAdapter<T> adapter;

    public EnumRule(GameRules.Type<EnumRule<T>> type, Class<T> targetClass, T initialValue, IGameruleValidator<T> validator, IGameruleAdapter<T> adapter) {
        super(type);
        Objects.requireNonNull(targetClass);
        Objects.requireNonNull(initialValue);
        Objects.requireNonNull(validator);
        Objects.requireNonNull(adapter);
        this.tClass = targetClass;
        this.value = initialValue;
        this.validator = validator;
        this.adapter = adapter;
    }

    public EnumRule(GameRules.Type<EnumRule<T>> type, Class<T> targetClass, T initialValue) {
        this(type, targetClass, initialValue, IGameruleValidator::alwaysTrue, Optional::of);
    }

    public static <T extends Enum<T>> GameRules.Type<EnumRule<T>> create(
            Class<T> targetClass, T initialValue, BiConsumer<MinecraftServer, EnumRule<T>> changeCallback) {
        return new GameRules.Type<>((EnumArgSupplier<T>) () -> targetClass, type -> new EnumRule<>(type, targetClass, initialValue),
                changeCallback, ((consumer, key, cType) -> ((IGameRulesVisitor)consumer).unruled_visitEnum(key, cType)));
    }

    public static <T extends Enum<T>> GameRules.Type<EnumRule<T>> create(Class<T> targetClass, T initialValue) {
        return create(targetClass, initialValue, ((server, enumRule) -> {}));
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
        return parse(input).flatMap(val -> {
            set(val);
            return Optional.of(0);
        }).isEmpty();
    }

    public T get() {
        return this.value;
    }

    private void set(T t) {
        this.value = t;
    }

    public void set(T value, MinecraftServer server) {
        set(value);
        this.changed(server);
    }

    public T[] values() {
        return tClass.getEnumConstants();
    }

    @Override
    protected void setFromArgument(CommandContext<ServerCommandSource> context, String name) {
        String s = StringArgumentType.getString(context, name);
        parse(s).ifPresent(this::set);
    }

    @Override
    protected void deserialize(String value) {
        this.value = parse(value).orElse(null);
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
        return new EnumRule<>(this.type, this.tClass, this.value);
    }

    @Override
    public void setValue(EnumRule<T> rule, @Nullable MinecraftServer server) {
        this.value = rule.get();
        this.changed(server);
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
        this.validator = Objects.requireNonNull(validator);
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
