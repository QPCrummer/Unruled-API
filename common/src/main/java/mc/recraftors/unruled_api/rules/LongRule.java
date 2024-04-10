package mc.recraftors.unruled_api.rules;

import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.context.CommandContext;
import mc.recraftors.unruled_api.UnruledApi;
import mc.recraftors.unruled_api.utils.IGameRulesVisitor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public class LongRule extends GameRules.Rule<LongRule> {
    private long value;

    public LongRule(GameRules.Type<LongRule> type, long initialValue) {
        super(type);
        this.value = initialValue;
    }

    public static GameRules.Type<LongRule> create(long initialValue, BiConsumer<MinecraftServer, LongRule> changeCallback) {
        return new GameRules.Type<>(LongArgumentType::longArg, type -> new LongRule(type, initialValue), changeCallback,
                (consumer, key, cType) -> ((IGameRulesVisitor)consumer).unruled_visitLong(key, cType));
    }

    public static GameRules.Type<LongRule> create(long initialValue) {
        return create(initialValue, (server, longRule) -> {});
    }

    public long get() {
        return this.value;
    }

    public void set(long value, MinecraftServer server) {
        this.value = value;
        this.changed(server);
    }

    public boolean validate(String input) {
        try {
            this.value = Long.parseLong(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static long parseLong(String input) {
        if (!input.isEmpty()) {
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                UnruledApi.LOGGER.warn("Failed to parse long {}", input);
            }
        }
        return 0;
    }

    @Override
    protected void setFromArgument(CommandContext<ServerCommandSource> context, String name) {
        this.value = LongArgumentType.getLong(context, name);
    }

    @Override
    protected void deserialize(String value) {
        this.value = LongRule.parseLong(value);
    }

    @Override
    public String serialize() {
        return Long.toString(this.value);
    }

    @Override
    public int getCommandResult() {
        return (int) this.value;
    }

    @Override
    protected LongRule getThis() {
        return this;
    }

    @Override
    protected LongRule copy() {
        return new LongRule(this.type, this.value);
    }

    @Override
    public void setValue(LongRule rule, @Nullable MinecraftServer server) {
        this.value = rule.get();
        this.changed(server);
    }
}
