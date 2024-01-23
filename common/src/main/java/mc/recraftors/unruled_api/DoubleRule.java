package mc.recraftors.unruled_api;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import mc.recraftors.unruled_api.utils.IGameRulesVisitor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public class DoubleRule extends GameRules.Rule<DoubleRule> {
    private double value;

    public DoubleRule(GameRules.Type<DoubleRule> type, double initialValue) {
        super(type);
        this.value = initialValue;
    }

    public static GameRules.Type<DoubleRule> create(double initialValue, BiConsumer<MinecraftServer, DoubleRule> changeCallback) {
        return new GameRules.Type<>(DoubleArgumentType::doubleArg, type -> new DoubleRule(type, initialValue), changeCallback,
                (consumer, key, cType) -> ((IGameRulesVisitor)consumer).unruled_visitDouble(key, cType));
    }

    public static GameRules.Type<DoubleRule> create(double initialValue) {
        return create(initialValue, (server, doubleRule) -> {});
    }

    public double get() {
        return this.value;
    }

    public void set(double value, MinecraftServer server) {
        this.value = value;
        this.changed(server);
    }

    public boolean validate(String input) {
        try {
            this.value = Double.parseDouble(input);
            return true;
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
        this.value = DoubleArgumentType.getDouble(context, name);
    }

    @Override
    protected void deserialize(String value) {
        this.value = Double.parseDouble(value);
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
        return new DoubleRule(this.type, this.value);
    }

    @Override
    public void setValue(DoubleRule rule, @Nullable MinecraftServer server) {
        this.value = rule.get();
        this.changed(server);
    }
}
