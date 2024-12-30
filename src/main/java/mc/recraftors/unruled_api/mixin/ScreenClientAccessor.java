package mc.recraftors.unruled_api.mixin;

import mc.recraftors.unruled_api.utils.ClientProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(Screen.class)
public abstract class ScreenClientAccessor implements ClientProvider {

    @Shadow @Nullable protected MinecraftClient client;

    @Override
    public MinecraftClient unruled_getClient() {
        return this.client;
    }
}
