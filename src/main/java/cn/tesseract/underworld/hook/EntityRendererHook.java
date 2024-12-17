package cn.tesseract.underworld.hook;

import cn.tesseract.mycelium.asm.Hook;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.player.EntityPlayer;

public class EntityRendererHook {
    @Hook(createMethod = true)
    public static float getFogNightVisionBrightness(EntityRenderer c, EntityPlayer player, float d) {
        return player.worldObj.provider.dimensionId == -2 ? 0 : c.getNightVisionBrightness(player, d);
    }
}
