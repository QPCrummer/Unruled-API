package mc.recraftors.unruled_api.mixin;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import mc.recraftors.unruled_api.utils.EnumArgSupplier;
import mc.recraftors.unruled_api.rules.EnumRule;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(GameRules.Type.class)
public abstract class GameRulesTypeMixin {
    @Shadow @Final private Supplier<ArgumentType<?>> argumentType;

    @Inject(method = "argument", at = @At("RETURN"))
    private void onArgumentHeadSpecialArgHandler(
            String name, CallbackInfoReturnable<RequiredArgumentBuilder<ServerCommandSource, ?>> cir) {
        if (this.argumentType instanceof EnumArgSupplier<?> supplier) {
            cir.getReturnValue().suggests((context, builder) -> CommandSource.suggestMatching(EnumRule.getEnumNames(supplier.unruled_getTargetClass()), builder));
        }
    }
}
