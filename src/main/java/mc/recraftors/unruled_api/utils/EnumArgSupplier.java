package mc.recraftors.unruled_api.utils;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import java.util.function.Supplier;

public interface EnumArgSupplier <T extends Enum<T>> extends Supplier<ArgumentType<?>> {
    @Override
    default ArgumentType<String> get() {
        return StringArgumentType.string();
    }

    Class<T> unruled_getTargetClass();
}
