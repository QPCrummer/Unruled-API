package mc.recraftors.unruled_api.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.brigadier.context.CommandContext;
import mc.recraftors.unruled_api.utils.EncapsulatedException;
import net.minecraft.server.command.GameRuleCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRuleCommand.class)
public abstract class GameRuleCommandMixin {
    @WrapOperation(
            method = "executeSet",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/GameRules$Rule;set(Lcom/mojang/brigadier/context/CommandContext;Ljava/lang/String;)V"
            )
    )
    private static void executeSetGameRuleSetWrapper(
            GameRules.Rule<?> instance, CommandContext<ServerCommandSource> context, String name,
            Operation<Void> original
    ) throws Exception {
        try {
            original.call(instance, context, name);
        } catch (EncapsulatedException ex) {
            throw ex.exception;
        }
    }
}
