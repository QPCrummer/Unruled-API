package mc.recraftors.unruled_api.rules;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import mc.recraftors.unruled_api.utils.EncapsulatedException;
import mc.recraftors.unruled_api.utils.IGameRulesVisitor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class StringRule extends GameRules.Rule<StringRule> {
    public static final Dynamic2CommandExceptionType SIZE_TOO_LONG = new Dynamic2CommandExceptionType((a, b) -> new LiteralMessage("Input must be at most " + a + " long, found " + b));

    private final int maxLength;
    private String value;

    public StringRule(GameRules.Type<StringRule> type, int maxLength, String initialValue) {
        super(type);
        if (maxLength <= 0) throw new UnsupportedOperationException("String rule size cannot be negative or null");
        else if (maxLength > 128) throw new UnsupportedOperationException("String rule size cannot be greater than 128. Use a Text rule for that.");
        this.maxLength = maxLength;
        if (initialValue.length() > this.getMaxLength()) throw new UnsupportedOperationException("Default value cannot breach max length");
        this.value = initialValue;
    }

    public static GameRules.Type<StringRule> create(int maxLength, String initialValue, BiConsumer<MinecraftServer, StringRule> changeCallback) {
        return new GameRules.Type<>(StringArgumentType::string, type -> new StringRule(type, maxLength, initialValue), changeCallback,
                (consumer, key, cType) -> ((IGameRulesVisitor)consumer).unruled_visitString(key, cType));
    }

    public static GameRules.Type<StringRule> create(int maxLength, String initialValue) {
        return create(maxLength, initialValue, (server, stringRule) -> {});
    }

    public static GameRules.Type<StringRule> createText(int maxLength, String initialValue, BiConsumer<MinecraftServer, StringRule> changeCallback) {
        return new GameRules.Type<>(StringArgumentType::string, type -> new TextRule(type, maxLength, initialValue), changeCallback,
                (consumer, key, cType) -> ((IGameRulesVisitor)consumer).unruled_visitText(key, cType));
    }

    public static GameRules.Type<StringRule> createText(int maxLength, String initialValue) {
        return createText(maxLength, initialValue, (server, stringRule) -> {});
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
        if (breaksMaxLength(input)) throw new UnsupportedOperationException();
        this.value = input;
        this.changed(server);
    }

    public boolean validate(String input) {
        if (breaksMaxLength(input)) return false;
        this.value = input;
        return true;
    }

    @Override
    protected void setFromArgument(CommandContext<ServerCommandSource> context, String name) {
        String input = StringArgumentType.getString(context, name);
        if (breaksMaxLength(input)) throw new EncapsulatedException("too long", SIZE_TOO_LONG.create(this.getMaxLength(), input.length()));
        this.value = input;
    }

    @Override
    protected void deserialize(String value) {
        if (breaksMaxLength(value)) value = value.substring(0, this.getMaxLength());
        this.value = value;
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
        return new StringRule(this.type, this.getMaxLength(), this.get());
    }

    @Override
    public void setValue(StringRule rule, @Nullable MinecraftServer server) {
        String input = rule.get();
        if (breaksMaxLength(input)) input = input.substring(0, this.getMaxLength());
        this.set(input, server);
        this.changed(server);
    }
}
