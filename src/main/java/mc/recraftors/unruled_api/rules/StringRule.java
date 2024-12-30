package mc.recraftors.unruled_api.rules;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import mc.recraftors.unruled_api.UnruledApi;
import mc.recraftors.unruled_api.mixin.GameRuleTypeInvoker;
import mc.recraftors.unruled_api.utils.*;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public class StringRule extends GameRules.Rule<StringRule> implements GameruleAccessor<String> {
    public static final Dynamic2CommandExceptionType SIZE_TOO_LONG = new Dynamic2CommandExceptionType((a, b) -> new LiteralMessage("Input must be at most " + a + " long, found " + b));

    private final int maxLength;
    private String value;
    private IGameruleValidator<String> validator;
    private IGameruleAdapter<String> adapter;

    public StringRule(GameRules.Type<StringRule> type, int maxLength, String initialValue, IGameruleValidator<String> validator, IGameruleAdapter<String> adapter) {
        super(type);
        Objects.requireNonNull(initialValue);
        Objects.requireNonNull(validator);
        Objects.requireNonNull(adapter);
        if (maxLength <= 0) throw new UnsupportedOperationException("String rule size cannot be negative or null");
        else if (maxLength > 128) throw new UnsupportedOperationException("String rule size cannot be greater than 128. Use a Text rule for that.");
        this.maxLength = maxLength;
        if (initialValue.length() > this.getMaxLength()) throw new UnsupportedOperationException("Default value cannot breach max length");
        this.value = initialValue;
        this.validator = validator;
        this.adapter = adapter;
    }

    public StringRule(GameRules.Type<StringRule> type, int maxLength, String initialValue) {
        this(type, maxLength, initialValue, IGameruleValidator::alwaysTrue, Optional::of);
    }

    public static GameRules.Type<StringRule> create(
            int maxLength, String initialValue, BiConsumer<MinecraftServer, StringRule> changeCallback,
            IGameruleValidator<String> validator, IGameruleAdapter<String> adapter
    ) {
        return GameRuleTypeInvoker.invokeInit(StringArgumentType::string, type -> new StringRule(type, maxLength, initialValue, validator, adapter), changeCallback,
                (consumer, key, cType) -> ((IGameRulesVisitor)consumer).unruled_visitString(key, cType), FeatureSet.empty());
    }

    public static GameRules.Type<StringRule> create(int maxLength, String initialValue, BiConsumer<MinecraftServer, StringRule> changeCallback) {
        return GameRuleTypeInvoker.invokeInit(StringArgumentType::string, type -> new StringRule(type, maxLength, initialValue),
                changeCallback, (consumer, key, cType) -> ((IGameRulesVisitor)consumer).unruled_visitString(key, cType), FeatureSet.empty());
    }

    public static GameRules.Type<StringRule> create(
            int maxLength, String initialValue, IGameruleValidator<String> validator, IGameruleAdapter<String> adapter
    ) {
        return create(maxLength, initialValue, UnruledApi.empty(), validator, adapter);
    }

    public static GameRules.Type<StringRule> create(int maxLength, String initialValue) {
        return create(maxLength, initialValue, UnruledApi.empty());
    }

    public static GameRules.Type<StringRule> createText(
            int maxLength, String initialValue, BiConsumer<MinecraftServer, StringRule> changeCallback,
            IGameruleValidator<String> validator, IGameruleAdapter<String> adapter
    ) {
        return GameRuleTypeInvoker.invokeInit(StringArgumentType::string, type -> new TextRule(type, maxLength, initialValue, validator, adapter),
                changeCallback, (consumer, key, cType) -> ((IGameRulesVisitor)consumer).unruled_visitText(key, cType), FeatureSet.empty());
    }

    public static GameRules.Type<StringRule> createText(
            int maxLength, String initialValue, BiConsumer<MinecraftServer, StringRule> changeCallback
    ) {
        return GameRuleTypeInvoker.invokeInit(StringArgumentType::string, type -> new TextRule(type, maxLength, initialValue), changeCallback,
                (consumer, key, cType) -> ((IGameRulesVisitor)consumer).unruled_visitText(key, cType), FeatureSet.empty());
    }

    public static GameRules.Type<StringRule> createText(
            int maxLength, String initialValue, IGameruleValidator<String> validator, IGameruleAdapter<String> adapter
    ) {
        return createText(maxLength, initialValue, UnruledApi.empty(), validator, adapter);
    }

    public static GameRules.Type<StringRule> createText(int maxLength, String initialValue) {
        return createText(maxLength, initialValue, UnruledApi.empty());
    }

    public String get() {
        return this.value;
    }

    public int getMaxLength() {
        return maxLength;
    }

    protected boolean breaksMaxLength(String input) {
        return input.length() > this.getMaxLength();
    }

    public void set(String input, MinecraftServer server) throws UnsupportedOperationException {
        this.bump(input, server, true);
    }

    private void bump(String s, MinecraftServer server, boolean thr) {
        if (breaksMaxLength(s)) {
            if (thr) throw new UnsupportedOperationException();
            s = s.substring(0, this.getMaxLength());
        }
        boolean b = this.validator.validate(s);
        if (!b) {
            Optional<String> o = this.adapter.adapt(s);
            if (o.isPresent() && this.validator.validate(o.get())) {
                s = o.get();
                b = true;
            }
        }
        if (b) {
            this.value = s;
            this.changed(server);
        }
    }

    private boolean set(String s, boolean thr, boolean trc) {
        if (this.breaksMaxLength(s)) {
            if (thr) {
                throw new EncapsulatedException("too long", SIZE_TOO_LONG.create(this.getMaxLength(), s.length()));
            }
            if (trc) {
                s = s.substring(0, this.getMaxLength());
            } else {
                return false;
            }
        }
        if (this.validator.validate(s)) {
            this.value = s;
            return true;
        }
        Optional<String> o = this.adapter.adapt(s);
        if (o.isEmpty() || !this.validator.validate(o.get())) return false;
        this.value = o.get();
        return true;
    }

    public boolean validate(String input) {
        return this.set(input, false, false);
    }

    @Override
    protected void setFromArgument(CommandContext<ServerCommandSource> context, String name) {
        String input = StringArgumentType.getString(context, name);
        this.set(input, true, false);
    }

    @Override
    protected void deserialize(String value) {
        this.set(value, false, true);
    }

    @Override
    public String serialize() {
        return this.get();
    }

    @Override
    public int getCommandResult() {
        return this.get().length();
    }

    @Override
    protected StringRule getThis() {
        return this;
    }

    @Override
    protected StringRule copy() {
        return new StringRule(this.type, this.getMaxLength(), this.get(), this.validator, this.adapter);
    }

    @Override
    public void setValue(StringRule rule, @Nullable MinecraftServer server) {
        this.bump(rule.get(), server, false);
    }

    @Override
    public IGameruleValidator<String> unruled_getValidator() {
        return this.validator;
    }

    @Override
    public void unruled_setValidator(IGameruleValidator<String> validator) {
        this.validator = Objects.requireNonNull(validator);
    }

    @Override
    public IGameruleAdapter<String> unruled_getAdapter() {
        return this.adapter;
    }

    @Override
    public void unruled_setAdapter(IGameruleAdapter<String> adapter) {
        this.adapter = Objects.requireNonNull(adapter);
    }
}
