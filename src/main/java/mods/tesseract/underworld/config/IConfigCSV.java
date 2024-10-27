package mods.tesseract.underworld.config;

import java.util.ArrayList;
import java.util.Arrays;

public interface IConfigCSV {
    String[] getTypes();

    String toCSV();

    IConfigCSV parseCSV(String[] csv);

    static ArrayList<IConfigCSV> parseCSV(String csv, Class<? extends IConfigCSV> cls) throws IllegalArgumentException{
        ArrayList<IConfigCSV> list = new ArrayList<>();
        String[] l = split2(csv, '\n');
        if (l.length < 2)
            return list;
        try {
            String[] n = cls.newInstance().getTypes();
            StringBuilder b = new StringBuilder();
            for (String s : n)
                b.append(",").append(s);
            b.delete(0, 1);
            if (!l[0].contentEquals(b))
                throw new IllegalArgumentException("不匹配的类型！应为：" + b + " 实为：" + l[0]);
            for (int i = 1; i < l.length; i++) {
                String t = l[i];
                String[] s = split(t, ',');
                if (s.length != n.length)
                    throw new IllegalArgumentException("不匹配的值数！应为：" + n.length + " 实为：" + s.length + " " + Arrays.toString(s));
                IConfigCSV c = cls.newInstance();
                c.parseCSV(s);
                list.add(c);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    static String toCSV(IConfigCSV[] csv, Class<? extends IConfigCSV> cls) {
        if (csv.length == 0)
            return "";
        StringBuilder str = new StringBuilder();
        String[] n = csv[0].getTypes();
        StringBuilder b = new StringBuilder();
        for (String s : n)
            b.append(",").append(s);
        b.delete(0, 1);
        str.append(b);
        for (IConfigCSV c : csv)
            str.append("\n").append(c.toCSV());
        return str.toString();
    }

    static String[] split(String str, char c) {
        ArrayList<String> strs = new ArrayList<>();
        int l = str.length();
        int j = 0;
        for (int i = 0; i <= l; i++)
            if (i == l || str.charAt(i) == c) {
                strs.add(str.substring(j, i));
                j = i + 1;
            }
        return strs.toArray(new String[0]);
    }

    static String[] split2(String str, char c) {
        ArrayList<String> strs = new ArrayList<>();
        int l = str.length();
        int j = 0;
        for (int i = 0; i <= l; i++)
            if (i == l || str.charAt(i) == c) {
                String t = str.substring(j, i).trim();
                j = i + 1;
                if (t.isEmpty() || t.charAt(0) == '#')
                    continue;
                strs.add(t);
            }
        return strs.toArray(new String[0]);
    }
}
