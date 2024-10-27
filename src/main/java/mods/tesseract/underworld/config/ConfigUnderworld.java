package mods.tesseract.underworld.config;

import mods.tesseract.underworld.util.FileUtils;
import net.minecraft.launchwrapper.Launch;

import java.io.File;
import java.io.IOException;

public class ConfigUnderworld {
    public static final String configDir = "config" + File.separator + "underworld" + File.separator;

    static {
        new File(Launch.minecraftHome, configDir).mkdirs();
    }

    public final File file;
    public final String defaultConfig;
    public String config;

    public ConfigUnderworld(String file, String defaultConfig) {
        this.file = new File(Launch.minecraftHome, configDir + file);
        this.defaultConfig = this.config = defaultConfig;
    }

    public void init() {
        if (file.exists())
            try {
                config = FileUtils.readFile(file);
            } catch (IOException ignored) {
            }
        else
            reset();
    }

    public void reset() {
        try {
            file.createNewFile();
            FileUtils.writeFile(file, defaultConfig);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
