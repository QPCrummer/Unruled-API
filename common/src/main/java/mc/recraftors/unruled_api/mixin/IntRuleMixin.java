package mc.recraftors.unruled_api.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.mojang.brigadier.context.CommandContext;
import mc.recraftors.unruled_api.utils.GameruleAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    @WrapOperation(method = "setValue(Lnet/minecraft/world/GameRules$IntRule;Lnet/minecraft/server/MinecraftServer;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/world/GameRules$IntRule;value:I", opcode = Opcodes.GETFIELD))
    private int setValueLoadFieldWrapper(GameRules.IntRule instance, Operation<Integer> original) {
        int i = original.call(instance);
        if (this.unruled_getValidator().validate(i)) return i;
        Optional<Integer> o = this.unruled_getAdapter().adapt(i);
        if (o.isPresent() && this.unruled_getValidator().validate(o.get())) return o.get();
        return this.value;
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

    @SuppressWarnings("unchecked")
    @Inject(method = "copy()Lnet/minecraft/world/GameRules$IntRule;", at = @At("RETURN"))
    private void copyReturnInjector(CallbackInfoReturnable<GameRules.IntRule> cir) {
        ((GameruleAccessor<Integer>) cir.getReturnValue()).unruled_setValidator(this.unruled_getValidator());
        ((GameruleAccessor<Integer>) cir.getReturnValue()).unruled_setAdapter(this.unruled_getAdapter());
    }
}
