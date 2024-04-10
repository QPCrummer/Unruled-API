package mc.recraftors.unruled_api.utils;

import mc.recraftors.unruled_api.rules.*;
import net.minecraft.world.GameRules;

public interface IGameRulesVisitor {
    default void unruled_visitFloat(GameRules.Key<FloatRule> key, GameRules.Type<FloatRule> type) {}

    default void unruled_visitLong(GameRules.Key<LongRule> key, GameRules.Type<LongRule> type) {}

    default void unruled_visitDouble(GameRules.Key<DoubleRule> key, GameRules.Type<DoubleRule> type) {}

    default <T extends Enum<T>> void unruled_visitEnum(GameRules.Key<EnumRule<T>> key, GameRules.Type<EnumRule<T>> type) {}

    default void unruled_visitString(GameRules.Key<StringRule> key, GameRules.Type<StringRule> type) {}

    default void unruled_visitText(GameRules.Key<StringRule> key, GameRules.Type<StringRule> type) {}

    default void unruled_visitEntitySelector(GameRules.Key<EntitySelectorRule> key, GameRules.Type<EntitySelectorRule> type) {}
}
