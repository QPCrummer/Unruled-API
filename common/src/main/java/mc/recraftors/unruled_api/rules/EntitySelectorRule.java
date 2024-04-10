package mc.recraftors.unruled_api.rules;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mc.recraftors.unruled_api.utils.EncapsulatedException;
import mc.recraftors.unruled_api.utils.IGameRulesVisitor;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.EntitySelectorReader;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class EntitySelectorRule extends GameRules.Rule<EntitySelectorRule> {
    private EntitySelector value;
    private String str;

    public EntitySelectorRule(GameRules.Type<EntitySelectorRule> type, String initialValue) {
        super(type);
        this.str = initialValue;
        this.value = parseStr(initialValue);
    }

    public static GameRules.Type<EntitySelectorRule> create(String initialValue, BiConsumer<MinecraftServer, EntitySelectorRule> changeCallback) {
        return new GameRules.Type<>(EntityArgumentType::entities, type -> new EntitySelectorRule(type, initialValue), changeCallback,
                ((consumer, key, cType) -> ((IGameRulesVisitor)consumer).unruled_visitEntitySelector(key, cType)));
    }

    public static GameRules.Type<EntitySelectorRule> create(String initialValue) {
        return create(initialValue, (server, entitySelectorRule) -> {});
    }

    public EntitySelector get() {
        return this.value;
    }

    public boolean validate(String input) {
        input = input.trim();
        try {
            this.value = parseStr(input);
            this.str = input;
            return true;
        } catch (EncapsulatedException e) {
            return false;
        }
    }

    private static EntitySelector parseStr(String s) throws EncapsulatedException {
        try {
            return new EntitySelectorReader(new StringReader(s)).read();
        } catch (CommandSyntaxException e) {
            throw new EncapsulatedException(e);
        }
    }

    @Override
    protected void setFromArgument(CommandContext<ServerCommandSource> context, String name) {
        this.value = context.getArgument(name, EntitySelector.class);
    }

    @Override
    protected void deserialize(String value) {
        this.str = value;
        this.value = parseStr(value);
    }

    @Override
    public String serialize() {
        return this.str;
    }

    @Override
    public int getCommandResult() {
        return this.str.length();
    }

    @Override
    protected EntitySelectorRule getThis() {
        return this;
    }

    @Override
    protected EntitySelectorRule copy() {
        return new EntitySelectorRule(this.type, this.str);
    }

    @Override
    public void setValue(EntitySelectorRule rule, @Nullable MinecraftServer server) {
        this.str = rule.serialize();
        this.value = parseStr(rule.serialize());
    }
}
