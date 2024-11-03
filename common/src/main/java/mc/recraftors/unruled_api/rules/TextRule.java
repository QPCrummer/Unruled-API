package mc.recraftors.unruled_api.rules;

import mc.recraftors.unruled_api.utils.IGameruleAdapter;
import mc.recraftors.unruled_api.utils.IGameruleValidator;
import net.minecraft.world.GameRules;

import java.util.Objects;
import java.util.Optional;

/**
 * Technically functional text gamerule for rules beyond 128 characters long.
 * <p>
 * Warning: world creation menu not yet implemented (will appear as a useless button)
 */
public class TextRule extends StringRule {
    private final int maxTextLength;
    public TextRule(GameRules.Type<StringRule> type, int maxLength, String initialValue, IGameruleValidator<String> validator, IGameruleAdapter<String> adapter) {
        super(type, 1, "", validator, adapter);
        Objects.requireNonNull(initialValue);
        this.maxTextLength = maxLength;
        this.validate(initialValue);
    }
    public TextRule(GameRules.Type<StringRule> type, int maxLength, String initialValue) {
        this(type, maxLength, initialValue, IGameruleValidator::alwaysTrue, Optional::of);
    }

    @Override
    public int getMaxLength() {
        return this.maxTextLength;
    }

    @Override
    protected StringRule copy() {
        return new TextRule(this.type, getMaxLength(), this.get());
    }
}
