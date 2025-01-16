package cn.tesseract.underworld.config;

import cn.tesseract.mycelium.config.ConfigJSON;

import java.io.File;

public class ConfigOreEntries extends ConfigJSON<OreEntries> {
    public ConfigOreEntries(File file, Class<OreEntries> clazz) {
        super(file, clazz);
    }

    public ConfigOreEntries(String name, Class<OreEntries> clazz) {
        super(name, clazz);
    }
}
