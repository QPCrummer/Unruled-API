package mc.recraftors.unruled_api.rules;

import com.mojang.brigadier.arguments.DoubleArgumentType;
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
public class DoubleRule extends GameRules.Rule<DoubleRule> implements GameruleAccessor<Double> {
    private double value;
    private IGameruleValidator<Double> validator;
    private IGameruleAdapter<Double> adapter;

    public DoubleRule(GameRules.Type<DoubleRule> type, double initialValue, IGameruleValidator<Double> validator, IGameruleAdapter<Double> adapter) {
        super(type);
        Objects.requireNonNull(validator);
        Objects.requireNonNull(adapter);
        this.value = initialValue;
        this.validator = validator;
        this.adapter = adapter;
    }

    public DoubleRule(GameRules.Type<DoubleRule> type, double initialValue) {
        this(type, initialValue, IGameruleValidator::alwaysTrue, Optional::of);
    }

    public static GameRules.Type<DoubleRule> create(double initialValue, BiConsumer<MinecraftServer, DoubleRule> changeCallback,
                                                    IGameruleValidator<Double> validator, IGameruleAdapter<Double> adapter) {
        return GameRuleTypeInvoker.invokeInit(DoubleArgumentType::doubleArg, type -> new DoubleRule(type, initialValue, validator, adapter), changeCallback,
                (consumer, key, cType) -> ((IGameRulesVisitor) consumer).unruled_visitDouble(key, cType), FeatureSet.empty());
    }

    public static GameRules.Type<DoubleRule> create(double initialValue, BiConsumer<MinecraftServer, DoubleRule> changeCallback) {
        return create(initialValue, changeCallback, IGameruleValidator::alwaysTrue, Optional::of);
    }

    public static GameRules.Type<DoubleRule> create(double initialValue, IGameruleValidator<Double> validator, IGameruleAdapter<Double> adapter) {
        return create(initialValue, UnruledApi.empty(), validator, adapter);
    }

    public static GameRules.Type<DoubleRule> create(double initialValue) {
        return create(initialValue, UnruledApi.empty(), IGameruleValidator::alwaysTrue, Optional::of);
    }

    public double get() {
        return this.value;
    }

    public void set(double value, MinecraftServer server) {
        if (this.validator.validate(value)) {
            this.value = value;
            this.changed(server);
            return;
        }
        Optional<Double> o = this.adapter.adapt(value);
        if (o.isPresent() && this.validator.validate(o.get())) {
            this.value = o.get();
            this.changed(server);
        }
    }

    public boolean validate(String input) {
        try {
            double d = Double.parseDouble(input);
            if (this.validator.validate(d)) {
                this.value = Double.parseDouble(input);
                return true;
            }
            Optional<Double> o = this.adapter.adapt(d);
            if (o.isPresent() && this.validator.validate(o.get())) {
                this.value = o.get();
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static double parseDouble(String input) {
        if (!input.isEmpty()) {
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                UnruledApi.LOGGER.warn("Failed to parse double {}", input);
            }
        }
        return 0;
    }

    @Override
    protected void setFromArgument(CommandContext<ServerCommandSource> context, String name) {
        double d = DoubleArgumentType.getDouble(context, name);
        boolean b = this.validator.validate(d);
        if (b) {
            this.value = d;
            return;
        }
        Optional<Double> o = this.adapter.adapt(d);
        if (o.isPresent()) {
            b = this.validator.validate(o.get());
            if (b) {
                this.value = o.get();
                return;
            }
        }
        throw new IllegalArgumentException("Invalid value "+d);
    }

    @Override
    protected void deserialize(String value) {
        double d = Double.parseDouble(value);
        boolean b = this.validator.validate(d);
        if (b) {
            this.value = d;
            return;
        }
        Optional<Double> o = this.adapter.adapt(d);
        if (o.isPresent()) {
            b = this.validator.validate(o.get());
            if (b) {
                this.value = o.get();
            }
        }
    }

    @Override
    public String serialize() {
        return Double.toString(this.value);
    }

    @Override
    public int getCommandResult() {
        return (int) this.value;
    }

    @Override
    protected DoubleRule getThis() {
        return this;
    }

    @Override
    protected DoubleRule copy() {
        return new DoubleRule(this.type, this.value, this.validator, this.adapter);
    }

    @Override
    public void setValue(DoubleRule rule, @Nullable MinecraftServer server) {
        double d = rule.get();
        boolean b = this.validator.validate(d);
        if (!b) {
            Optional<Double> o = this.adapter.adapt(d);
            if (o.isPresent() && this.validator.validate(o.get())) {
                d = o.get();
                b = true;
            }
        }
        if (b) {
            this.value = d;
            this.changed(server);
        }
    }

    @Override
    public IGameruleValidator<Double> unruled_getValidator() {
        return this.validator;
    }

    @Override
    public void unruled_setValidator(IGameruleValidator<Double> validator) {
        this.validator = Objects.requireNonNull(validator);
    }

    @Override
    public IGameruleAdapter<Double> unruled_getAdapter() {
        return this.adapter;
    }

    @Override
    public void unruled_setAdapter(IGameruleAdapter<Double> adapter) {
        this.adapter = Objects.requireNonNull(adapter);
    }
}
