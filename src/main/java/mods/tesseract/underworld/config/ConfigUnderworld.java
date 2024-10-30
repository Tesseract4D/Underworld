package mods.tesseract.underworld.config;

import java.lang.reflect.Field;

public class ConfigUnderworld implements IConfigProperties {
    @Comment("该数值越大生成的矿物越多，1为MITE的矿物生成倍率")
    public static float ore_mutiplier = 4;
    @Comment("削弱骷髅的血量为5点")
    public static boolean nerf_skeletons = false;

    @Override
    public void load(Field f, Class<?> c, String n, String v) {
        IConfigProperties.defaultLoad(f, c, n, v);
    }
}
