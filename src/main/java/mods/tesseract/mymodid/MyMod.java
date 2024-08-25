package mods.tesseract.mymodid;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

@Mod(modid = "mymodid", acceptedMinecraftVersions = "[1.7.10]")
public class MyMod {
    public static String greeting;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        syncConfig(e.getSuggestedConfigurationFile());
    }

    public static void syncConfig(File f) {
        Configuration configuration = new Configuration(f);
        greeting = configuration.getString("greeting", Configuration.CATEGORY_GENERAL, "Hello World", "How shall I greet?");
        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
