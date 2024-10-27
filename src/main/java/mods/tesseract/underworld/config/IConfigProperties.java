package mods.tesseract.underworld.config;

import java.lang.reflect.Field;
import java.util.HashMap;

public interface IConfigProperties {
    void load(Field f, Class<?> c, String n, String v);

    static void defaultLoad(Field f, Class<?> c, String n, String v) {
        try {
            if (c == byte.class)
                f.set(null, Byte.valueOf(v));
            else if (c == short.class)
                f.set(null, Short.valueOf(v));
            else if (c == int.class)
                f.set(null, Integer.valueOf(v));
            else if (c == long.class)
                f.set(null, Long.valueOf(v));
            else if (c == float.class)
                f.set(null, Float.valueOf(v));
            else if (c == double.class)
                f.set(null, Double.valueOf(v));
            else if (c == boolean.class)
                f.set(null, Boolean.valueOf(v));
            else if (c == char.class)
                f.set(null, v.toCharArray()[0]);
            else if (c == String.class)
                f.set(null, v);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    static void loadProperties(String ct, Class<? extends IConfigProperties> cls) {
        Field[] fields = cls.getDeclaredFields();
        HashMap<String, Field> map = new HashMap<>();
        IConfigProperties i;
        try {
            i = cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        for (Field field : fields)
            map.put(field.getName(), field);
        for (String line : ct.split("\n")) {
            String t = line.trim();
            if (!t.isEmpty() && t.charAt(0) != '#') {
                String[] b = t.split("=", -1);
                String k = b[0].trim();
                String v = b[1].trim();
                Field f;
                if ((f = map.get(k)) != null) {
                    i.load(f, f.getType(), f.getName(), v);
                }
            }
        }
    }

    static String toProperties(Class<? extends IConfigProperties> cls) {
        Field[] fields = cls.getDeclaredFields();
        StringBuilder ct = new StringBuilder();
        try {
            for (Field field : fields) {
                if (ct.length() != 0)
                    ct.append("\n");
                if (field.isAnnotationPresent(Comment.class))
                    ct.append("#").append(field.getAnnotation(Comment.class).value()).append("\n");
                ct.append(field.getName()).append("=").append(field.get(null));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return ct.toString();
    }
}
