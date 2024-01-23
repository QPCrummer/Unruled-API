package mc.recraftors.unruled_api.mixin;

import mc.recraftors.unruled_api.utils.IGameRulesVisitor;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GameRules.Visitor.class)
public interface GameRulesVisitorMixin extends IGameRulesVisitor {
}
