package mc.recraftors.unruled_api.rules;

import net.minecraft.world.GameRules;

import java.util.Objects;

/**
 * Technically functional text gamerule for rules beyond 128 characters long.
 * <p>
 * Warning: world creation menu not yet implemented (will appear as a useless button)
 */
public class TextRule extends StringRule {
    private final int maxTextLength;
    public TextRule(GameRules.Type<StringRule> type, int maxLength, String initialValue) {
        super(type, 1, "");
        Objects.requireNonNull(initialValue);
        this.maxTextLength = maxLength;
        this.validate(initialValue);
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
