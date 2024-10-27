package mods.tesseract.underworld.config;

import net.minecraft.launchwrapper.Launch;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Config {
    public static final String configDir = "config" + File.separator + "underworld" + File.separator;

    static {
        new File(Launch.minecraftHome, configDir).mkdirs();
    }

    public final File file;
    public final String defaultConfig;
    public String config;

    public Config(String file, String defaultConfig) {
        this.file = new File(Launch.minecraftHome, configDir + file);
        this.defaultConfig = this.config = defaultConfig;
    }

    public void read() {
        if (file.exists())
            try {
                config = FileUtils.readFileToString(file);
            } catch (IOException ignored) {
            }
        else
            reset();
    }

    public void save() {
        try {
            file.createNewFile();
            FileUtils.writeStringToFile(file, config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reset() {
        try {
            file.createNewFile();
            FileUtils.writeStringToFile(file, defaultConfig);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
