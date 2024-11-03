package mc.recraftors.unruled_api.rules;

import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.context.CommandContext;
import mc.recraftors.unruled_api.UnruledApi;
import mc.recraftors.unruled_api.utils.GameruleAccessor;
import mc.recraftors.unruled_api.utils.IGameRulesVisitor;
import mc.recraftors.unruled_api.utils.IGameruleAdapter;
import mc.recraftors.unruled_api.utils.IGameruleValidator;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public class LongRule extends GameRules.Rule<LongRule> implements GameruleAccessor<Long> {
    private long value;
    private IGameruleValidator<Long> validator;
    private IGameruleAdapter<Long> adapter;

    public LongRule(GameRules.Type<LongRule> type, long initialValue, IGameruleValidator<Long> validator, IGameruleAdapter<Long> adapter) {
        super(type);
        Objects.requireNonNull(validator);
        Objects.requireNonNull(adapter);
        this.value = initialValue;
        this.validator = validator;
        this.adapter = adapter;
    }

    public LongRule(GameRules.Type<LongRule> type, long initialValue) {
        this(type, initialValue, IGameruleValidator::alwaysTrue, Optional::of);
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

    @Override
    public IGameruleValidator<Long> unruled_getValidator() {
        return this.validator;
    }

    @Override
    public void unruled_setValidator(IGameruleValidator<Long> validator) {
        this.validator = Objects.requireNonNull(validator);
    }

    @Override
    public IGameruleAdapter<Long> unruled_getAdapter() {
        return this.adapter;
    }

    @Override
    public void unruled_setAdapter(IGameruleAdapter<Long> adapter) {
        this.adapter = Objects.requireNonNull(adapter);
    }
}
