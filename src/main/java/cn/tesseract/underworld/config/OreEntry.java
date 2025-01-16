package cn.tesseract.underworld.config;

import cn.tesseract.mycelium.config.Comment;
import cn.tesseract.mycelium.config.ConfigProperties;

import java.io.File;

public class OreEntry{
    //矿物词典，当找不到对应的方块时将选取该矿物词典中第一个方块来生成
    public String oreDict;
    //是否禁用
    public boolean disable;

    //要生成的方块
    public String block;

    //要生成的方块的元数据值
    public int blockMeta;

    //矿脉最大尺寸
    public int veinSize;

    //生成的最低高度
    public int minY;

    //生成的最高高度
    public int maxY;

    //要替换的方块，一般为石头
    public String blockToReplace;

    //一个区块尝试生成的次数，越大生成得越多
    public int frequency;
    //是否平均分布，否则75%的矿脉会生成在最低高度到最高高度之间的下半部分
    public boolean uniformDistribution;
    //矿脉尺寸是否随深度而增加
    public boolean sizeIncreasesWithDepth;
}
