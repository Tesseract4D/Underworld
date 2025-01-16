package cn.tesseract.underworld.config;

import cn.tesseract.mycelium.config.Comment;
import cn.tesseract.mycelium.config.ConfigProperties;

public class ConfigUnderWorld extends ConfigProperties {

    @Comment("该数值越大生成的矿物越多，1为MITE的矿物生成倍率")
    public float ore_multiplier = 1.5f;
    @Comment("将地下世界基岩层和基岩山替换为别的方块")
    public String bedrock_layer_block = "minecraft:bedrock";

    public ConfigUnderWorld() {
        super("underworld");
    }
}
