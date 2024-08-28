package mods.tesseract.underworld;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mods.tesseract.underworld.biomes.BiomeGenUnderworld;
import mods.tesseract.underworld.world.WorldProviderUnderworld;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraftforge.common.DimensionManager;
import net.tclproject.mysteriumlib.asm.common.CustomLoadingPlugin;
import net.tclproject.mysteriumlib.asm.common.FirstClassTransformer;
import net.tclproject.mysteriumlib.asm.core.MetaReader;
import net.tclproject.mysteriumlib.asm.core.MiscUtils;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

@Mod(modid = "underworld", acceptedMinecraftVersions = "[1.7.10]")
public class Main extends CustomLoadingPlugin {
    public static int underworld_y_offset = 120;
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        try {
            //System.out.println("&" + new MetaReader().getLocalVariables(EntityRenderer.class.getDeclaredMethod("setupFog", int.class, float.class)));
            //System.out.println("&" + MiscUtils.getMemberInfo(GL11.class.getDeclaredMethod("glFogi", int.class, int.class)));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        BiomeGenUnderworld.biome = (new BiomeGenUnderworld(26)).setColor(16711680).setBiomeName("Underworld").setDisableRain().setTemperatureRainfall(1.0F, 0.0F);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        DimensionManager.registerProviderType(-2, WorldProviderUnderworld.class, false);
        DimensionManager.registerDimension(-2, -2);
    }
    public String[] getASMTransformerClass() {
        return new String[]{FirstClassTransformer.class.getName()};
    }

    public void registerFixes() {
        registerClassWithFixes("mods.tesseract.underworld.fix.FixesUnderworld");
        registerImplementation("net.minecraft.world.World","mods.tesseract.underworld.fix.IWorld");
    }
}
