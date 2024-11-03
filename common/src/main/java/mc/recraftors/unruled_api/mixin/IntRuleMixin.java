package mc.recraftors.unruled_api.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.mojang.brigadier.context.CommandContext;
import mc.recraftors.unruled_api.utils.GameruleAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(GameRules.IntRule.class)
public abstract class IntRuleMixin implements GameruleAccessor<Integer> {
    @Shadow private int value;

    @SuppressWarnings("DuplicatedCode")
    @WrapOperation(method = "setFromArgument", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/arguments/IntegerArgumentType;getInteger(Lcom/mojang/brigadier/context/CommandContext;Ljava/lang/String;)I"))
    private int setFromArgumentParseIntWrapper(CommandContext<?> context, String name, Operation<Integer> original) {
        int i = original.call(context, name);
        boolean b = this.unruled_getValidator().validate(i);
        if (b) return i;
        Optional<Integer> o = this.unruled_getAdapter().adapt(i);
        if (o.isPresent()) {
            b = unruled_getValidator().validate(o.get());
            if (b) return o.get();
        }
        throw new IllegalArgumentException("Invalid rule value "+i);
    }

    @SuppressWarnings("DuplicatedCode")
    @WrapOperation(method = "deserialize", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules$IntRule;parseInt(Ljava/lang/String;)I"))
    private int deserializeParseIntWrapper(String input, Operation<Integer> original) {
        int i = original.call(input);
        boolean b = this.unruled_getValidator().validate(i);
        if (b) return i;
        Optional<Integer> o = this.unruled_getAdapter().adapt(i);
        if (o.isPresent()) {
            b = unruled_getValidator().validate(o.get());
            if (b) return o.get();
        }
        return this.value;
    }

    @WrapOperation(method = "validate", at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I"))
    private int validateParseIntWrapper(String s, Operation<Integer> original) {
        int i = original.call(s);
        boolean b = this.unruled_getValidator().validate(i);
        if (b) return i;
        throw new NumberFormatException();
    }

    @Inject(method = "set", at = @At("HEAD"), cancellable = true)
    private void setValidatorAdapter(int value, MinecraftServer server, CallbackInfo ci, @Local(argsOnly = true, ordinal = 0) LocalIntRef v) {
        boolean b = this.unruled_getValidator().validate(value);
        if (b) return;
        Optional<Integer> o = this.unruled_getAdapter().adapt(value);
        if (o.isPresent()) {
            b = this.unruled_getValidator().validate(o.get());
            if (b) v.set(o.get());
        } else {
            ci.cancel();
        }
    }
}
