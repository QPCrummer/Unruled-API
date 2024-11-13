package mc.recraftors.unruled_api.mixin;

import mc.recraftors.unruled_api.UnruledApi;
import mc.recraftors.unruled_api.impl.OddIntRuleValidatorAdapter;
import mc.recraftors.unruled_api.impl.RoundingBehaviour;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(GameRules.class)
public abstract class TestRulesMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void testRulesInjector(CallbackInfo ci) {
        UnruledApi.registerLong("test.test1", GameRules.Category.PLAYER, 0,
                i -> ((i & 1) == 0 && i >= 0 && i < 64), i -> Optional.of(i > 62 ? 62 : i < 0 ? 0 : i + 1)
        );
        UnruledApi.registerInt("test.test2", GameRules.Category.CHAT, 1,
                new OddIntRuleValidatorAdapter(RoundingBehaviour.CEILING)
        );
    }
}
