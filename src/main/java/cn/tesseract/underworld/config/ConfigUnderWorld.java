package cn.tesseract.underworld.config;

import cn.tesseract.mycelium.config.Comment;
import cn.tesseract.mycelium.config.Config;
import cn.tesseract.mycelium.config.IConfigProperties;

public class ConfigUnderWorld extends Config implements IConfigProperties {

    @Comment("该数值越大生成的矿物越多，1为MITE的矿物生成倍率")
    public static float ore_mutiplier = 4;

    public ConfigUnderWorld() {
        super("underworld.properties", "");
    }

    @Override
    public Config read() {
        loadProperties(readFile());
        return save(toProperties());
    }

    @Override
    public Config save(String s) {
        saveFile(s);
        return this;
    }
}
