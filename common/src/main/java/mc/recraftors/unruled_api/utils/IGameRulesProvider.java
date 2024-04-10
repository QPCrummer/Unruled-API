package mc.recraftors.unruled_api.utils;

import mc.recraftors.unruled_api.rules.*;
import net.minecraft.command.EntitySelector;
import net.minecraft.world.GameRules;

/**
 * Utility interface for floating gamerules handling.
 */
public interface IGameRulesProvider {
    default float unruled_getFloat(GameRules.Key<FloatRule> key) {
        return 0;
    }

    default long unruled_getLong(GameRules.Key<LongRule> key) {
        return 0;
    }

    default double unruled_getDouble(GameRules.Key<DoubleRule> key) {
        return 0;
    }

    default <T extends Enum<T>> T unruled_getEnum(GameRules.Key<EnumRule<T>> key) {
        return null;
    }

    default String unruled_getString(GameRules.Key<StringRule> key) {
        return null;
    }

    default EntitySelector unruled_getEntitySelector(GameRules.Key<EntitySelectorRule> key) {
        return null;
    }
}
