package cn.tesseract.underworld.config;

import cn.tesseract.mycelium.config.Comment;
import cn.tesseract.mycelium.config.Config;
import cn.tesseract.mycelium.config.IConfigProperties;

import java.io.File;

public class ConfigOreEntry extends Config implements IConfigProperties {
    @Comment("矿物词典，当找不到对应的方块时将选取该矿物词典中第一个方块来生成")
    public String oreDict;
    @Comment("是否禁用")
    public boolean disable;

    @Comment("要生成的方块")
    public String block;

    @Comment("要生成的方块的元数据值")
    public int blockMeta;

    @Comment("矿脉最大尺寸")
    public int veinSize;

    @Comment("生成的最低高度")
    public int minY;

    @Comment("生成的最高高度")
    public int maxY;

    @Comment("要替换的方块，一般为石头")
    public String blockToReplace;

    @Comment("一个区块尝试生成的次数，越大生成得越多")
    public int frequency;
    @Comment("是否平均分布，否则75%的矿脉会生成在最低高度到最高高度之间的下半部分")
    public boolean uniformDistribution;
    @Comment("矿脉尺寸是否随深度而增加")
    public boolean sizeIncreasesWithDepth;

    public ConfigOreEntry(String file, String[] data) {
        super(file, "");
        this.oreDict = data[0];
        this.disable = Boolean.parseBoolean(data[1]);
        this.block = data[2];
        this.blockMeta = Integer.parseInt(data[3]);
        this.veinSize = Integer.parseInt(data[4]);
        this.minY = Integer.parseInt(data[5]);
        this.maxY = Integer.parseInt(data[6]);
        this.blockToReplace = data[7];
        this.frequency = Integer.parseInt(data[8]);
        this.uniformDistribution = Boolean.parseBoolean(data[9]);
        this.sizeIncreasesWithDepth = Boolean.parseBoolean(data[10]);
    }

    public ConfigOreEntry(File file, String defaultConfig) {
        super(file, defaultConfig);
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
