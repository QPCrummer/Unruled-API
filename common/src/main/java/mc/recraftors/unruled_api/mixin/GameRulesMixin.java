package mc.recraftors.unruled_api.mixin;

import mc.recraftors.unruled_api.rules.*;
import mc.recraftors.unruled_api.utils.IGameRulesProvider;
import net.minecraft.command.EntitySelector;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameRules.class)
public abstract class GameRulesMixin implements IGameRulesProvider {
    @Shadow public abstract <T extends GameRules.Rule<T>> T get(GameRules.Key<T> key);

    @Override
    public float unruled_getFloat(GameRules.Key<FloatRule> key) {
        return this.get(key).get();
    }

    @Override
    public long unruled_getLong(GameRules.Key<LongRule> key) {
        return this.get(key).get();
    }

    @Override
    public double unruled_getDouble(GameRules.Key<DoubleRule> key) {
        return this.get(key).get();
    }

    @Override
    public <T extends Enum<T>> T unruled_getEnum(GameRules.Key<EnumRule<T>> key) {
        return this.get(key).get();
    }

    @Override
    public String unruled_getString(GameRules.Key<StringRule> key) {
        return this.get(key).get();
    }

    @Override
    public EntitySelector unruled_getEntitySelector(GameRules.Key<EntitySelectorRule> key) {
        return this.get(key).get();
    }
}
