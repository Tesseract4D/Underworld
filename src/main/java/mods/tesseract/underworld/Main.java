package mods.tesseract.underworld;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mods.tesseract.underworld.world.WorldProviderUnderworld;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.DimensionManager;
import net.tclproject.mysteriumlib.asm.common.CustomLoadingPlugin;
import net.tclproject.mysteriumlib.asm.common.FirstClassTransformer;

@Mod(modid = "underworld", acceptedMinecraftVersions = "[1.7.10]")
public class Main extends CustomLoadingPlugin {
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
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
