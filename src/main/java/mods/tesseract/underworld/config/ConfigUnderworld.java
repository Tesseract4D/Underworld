package mods.tesseract.underworld.config;

import java.lang.reflect.Field;

public class ConfigUnderworld implements IConfigProperties {
    @Comment("该数值越大生成的矿物越多")
    public static float ore_mutiplier = 1;

    @Override
    public void load(Field f, Class<?> c, String n, String v) {
        IConfigProperties.defaultLoad(f, c, n, v);
    }
}
