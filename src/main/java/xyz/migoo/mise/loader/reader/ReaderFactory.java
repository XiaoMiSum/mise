package xyz.migoo.mise.loader.reader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaomi
 */
public class ReaderFactory {

    public final static String JSON_SUFFIX = ".json";
    public final static String YAML_SUFFIX = ".yml";
    public final static String PROS_SUFFIX = ".properties";
    public static final String XLS_SUFFIX = ".xls";
    public static final String XLSX_SUFFIX = ".xlsx";
    private static final List<String> LIST = new ArrayList<>();

    public static Reader getReader(String path) throws ReaderException {
        if (YAML_SUFFIX.equals(suffix(path))) {
            return new YamlReader(path);
        }
        throw new ReaderException("file reader error");
    }

    private static String suffix(String file){
        String suffix = file.substring(file.lastIndexOf("."));
        if (LIST.contains(suffix)){
            return suffix;
        }
        return null;
    }

    static{
        Field[] fields = ReaderFactory.class.getDeclaredFields();
        for (Field field:fields){
            try {
                if ("LIST".equals(field.getName())){
                    continue;
                }
                LIST.add(field.get(ReaderFactory.class).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
