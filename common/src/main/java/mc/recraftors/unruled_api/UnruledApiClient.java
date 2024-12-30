package mc.recraftors.unruled_api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class UnruledApiClient {
    public static final Identifier SCROLLBAR_VERTICAL = guiTexture("scrollbar_vertical");
    public static final Identifier SCROLLBAR_HORIZONTAL = guiTexture("scrollbar_horizontal");

    public static Identifier guiTexture(String fileName) {
        return Identifier.of(UnruledApi.MOD_ID, "textures/gui/" + UnruledApi.MOD_ID + "/" + fileName + ".png");
    }
}
