package mc.recraftors.unruled_api.rules;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import mc.recraftors.unruled_api.UnruledApi;
import mc.recraftors.unruled_api.mixin.GameRuleTypeInvoker;
import mc.recraftors.unruled_api.utils.GameruleAccessor;
import mc.recraftors.unruled_api.utils.IGameRulesVisitor;
import mc.recraftors.unruled_api.utils.IGameruleAdapter;
import mc.recraftors.unruled_api.utils.IGameruleValidator;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public class FloatRule extends GameRules.Rule<FloatRule> implements GameruleAccessor<Float> {
    private float value;
    private IGameruleValidator<Float> validator;
    private IGameruleAdapter<Float> adapter;

    public FloatRule(GameRules.Type<FloatRule> type, float initialValue, IGameruleValidator<Float> validator, IGameruleAdapter<Float> adapter) {
        super(type);
        Objects.requireNonNull(validator);
        Objects.requireNonNull(adapter);
        this.value = initialValue;
        this.validator = validator;
        this.adapter = adapter;
    }

    public FloatRule(GameRules.Type<FloatRule> type, float initialValue) {
        this(type, initialValue, IGameruleValidator::alwaysTrue, Optional::of);
    }

    public static GameRules.Type<FloatRule> create(float initialValue, BiConsumer<MinecraftServer, FloatRule> changeCallback, IGameruleValidator<Float> validator, IGameruleAdapter<Float> adapter) {
        return GameRuleTypeInvoker.invokeInit(FloatArgumentType::floatArg, type -> new FloatRule(type, initialValue, validator, adapter), changeCallback,
                (consumer, key, cType) -> ((IGameRulesVisitor)consumer).unruled_visitFloat(key, cType), FeatureSet.empty());
    }

    public static GameRules.Type<FloatRule> create(float initialValue, BiConsumer<MinecraftServer, FloatRule> changeCallback) {
        return create(initialValue, changeCallback, IGameruleValidator::alwaysTrue, Optional::of);
    }

    public static GameRules.Type<FloatRule> create(float initialValue, IGameruleValidator<Float> validator, IGameruleAdapter<Float> adapter) {
        return create(initialValue, UnruledApi.empty(), validator, adapter);
    }

    public static GameRules.Type<FloatRule> create(float initialValue) {
        return create(initialValue, UnruledApi.empty());
    }

    public float get() {
        return this.value;
    }

    public void set(float value, MinecraftServer server) {
        this.bump(value, server);
    }

    private void bump(float value, MinecraftServer server) {
        boolean b = false;
        if (this.validator.validate(value)) b = true;
        else {
            Optional<Float> o = this.adapter.adapt(value);
            if (o.isPresent() && this.validator.validate(o.get())) {
                value = o.get();
                b = true;
            }
        }
        if (b) {
            this.value = value;
            this.changed(server);
        }
    }

    public boolean validate(String input) {
        try {
            float f = Float.parseFloat(input);
            return this.set(f);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static float parseFloat(String input) {
        if (!input.isEmpty()) {
            try {
                return Float.parseFloat(input);
            } catch (NumberFormatException e) {
                UnruledApi.LOGGER.warn("Failed to parse float {}", input);
            }
        }
        return 0;
    }

    private boolean set(float f) {
        if (this.validator.validate(f)) {
            this.value = f;
            return true;
        }
        Optional<Float> o = this.adapter.adapt(f);
        if (o.isEmpty() || !this.validator.validate(o.get())) return false;
        this.value = o.get();
        return true;
    }

    @Override
    protected void setFromArgument(CommandContext<ServerCommandSource> context, String name) {
        float f = FloatArgumentType.getFloat(context, name);
        this.set(f);
    }

    @Override
    protected void deserialize(String value) {
        float f = FloatRule.parseFloat(value);
        this.set(f);
    }

    @Override
    public String serialize() {
        return Float.toString(this.value);
    }

    @Override
    public int getCommandResult() {
        return (int) this.value;
    }

    @Override
    protected FloatRule getThis() {
        return this;
    }

    @Override
    protected FloatRule copy() {
        return new FloatRule(this.type, this.value);
    }

    @Override
    public void setValue(FloatRule rule, @Nullable MinecraftServer server) {
        this.bump(rule.get(), server);
    }

    @Override
    public IGameruleValidator<Float> unruled_getValidator() {
        return this.validator;
    }

    @Override
    public void unruled_setValidator(IGameruleValidator<Float> validator) {
        this.validator = Objects.requireNonNull(validator);
    }

    @Override
    public IGameruleAdapter<Float> unruled_getAdapter() {
        return this.adapter;
    }

    @Override
    public void unruled_setAdapter(IGameruleAdapter<Float> adapter) {
        this.adapter = Objects.requireNonNull(adapter);
    }
}
