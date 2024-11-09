package mc.recraftors.unruled_api.rules;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mc.recraftors.unruled_api.utils.*;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.EntitySelectorReader;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

public class EntitySelectorRule extends GameRules.Rule<EntitySelectorRule> implements GameruleAccessor<EntitySelector> {
    private EntitySelector value;
    private String str;
    private IGameruleValidator<EntitySelector> validator;
    private IGameruleAdapter<EntitySelector> adapter;

    public EntitySelectorRule(GameRules.Type<EntitySelectorRule> type, String initialValue, IGameruleValidator<EntitySelector> validator, IGameruleAdapter<EntitySelector> adapter) {
        super(type);
        Objects.requireNonNull(initialValue);
        Objects.requireNonNull(validator);
        Objects.requireNonNull(adapter);
        this.str = initialValue;
        this.value = parseStr(initialValue);
        this.validator = validator;
        this.adapter = adapter;
    }

    public EntitySelectorRule(GameRules.Type<EntitySelectorRule> type, String initialValue) {
        this(type, initialValue, IGameruleValidator::alwaysTrue, Optional::of);
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
            EntitySelector e = parseStr(input);
            if (this.validator.validate(e)) {
                this.value = parseStr(input);
                this.str = input;
                return true;
            }
            Optional<EntitySelector> o = this.adapter.adapt(e);
            if (o.isPresent() && this.validator.validate(o.get())) {
                this.value = o.get();
                this.str = input; // we assume the adapter able to get the same result from the same input
                return true;
            }
            return false;
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
        String s = context.getArgument(name, String.class);
        EntitySelector e = context.getArgument(name, EntitySelector.class);
        if (this.validator.validate(e)) {
            this.value = e;
            this.str = s;
            return;
        }
        Optional<EntitySelector> o = this.adapter.adapt(e);
        if (o.isPresent() && this.validator.validate(o.get())) {
            this.value = o.get();
            this.str = s;
            return;
        }
        throw new IllegalArgumentException("Unsupported value for input "+s);
    }

    @Override
    protected void deserialize(String value) {
        EntitySelector e = parseStr(value);
        boolean b = this.validator.validate(e);
        if (!b) {
            Optional<EntitySelector> o = this.adapter.adapt(e);
            if (o.isPresent()) {
                e = o.get();
                b = this.validator.validate(e);
            }
        }
        if (b) {
            this.str = value;
            this.value = e;
        }
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
        return new EntitySelectorRule(this.type, this.str, this.validator, this.adapter);
    }

    @Override
    public void setValue(EntitySelectorRule rule, @Nullable MinecraftServer server) {
        EntitySelector e = rule.get();
        if (this.validator.validate(e)){
            this.value = e;
            this.str = rule.serialize();
            return;
        }
        Optional<EntitySelector> o = this.adapter.adapt(e);
        if (o.isEmpty() || !this.validator.validate(o.get())) return;
        this.str = rule.serialize();
        this.value = o.get();
    }

    @Override
    public IGameruleValidator<EntitySelector> unruled_getValidator() {
        return this.validator;
    }

    @Override
    public void unruled_setValidator(IGameruleValidator<EntitySelector> validator) {
        this.validator = Objects.requireNonNull(validator);
    }

    @Override
    public IGameruleAdapter<EntitySelector> unruled_getAdapter() {
        return this.adapter;
    }

    @Override
    public void unruled_setAdapter(IGameruleAdapter<EntitySelector> adapter) {
        this.adapter = Objects.requireNonNull(adapter);
    }
}
