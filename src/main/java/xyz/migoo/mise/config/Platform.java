package xyz.migoo.mise.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.mise.loader.reader.ReaderException;
import xyz.migoo.mise.loader.reader.YamlReader;

/**
 * @author xiaomi
 * @date 2019-08-17 21:09
 */
public class Platform {


    private Platform() {
    }

    private static final JSONObject PROPERTIES = new JSONObject();

    private static final String STRING = "extends.class";

    private static final String IGNORE = "ignore.directory";

    static {
        String[] properties = new String[]{"application.yml", "migoo.yml"};
        for (String file : properties) {
            try {
                YamlReader reader = new YamlReader("classpath://" + file);
                if (reader.isNull()) {
                    continue;
                }
                ((JSONObject)reader.read()).forEach((key, value) -> {
                    if (STRING.equals(key) || IGNORE.equals(key)) {
                        if (PROPERTIES.getJSONArray(key) != null) {
                            ((JSONArray)value).addAll(PROPERTIES.getJSONArray(key));
                        }
                    }
                    PROPERTIES.put(key, value);
                });
            } catch (ReaderException e) {
                e.printStackTrace();
            }
        }
    }

    public static final JSONArray FUNCTION_EQUALS = PROPERTIES.getJSONArray("function.equals");

    public static final JSONArray FUNCTION_NOT_EQUALS = PROPERTIES.getJSONArray("function.notEquals");


    public static final JSONArray FUNCTION_CONTAINS = PROPERTIES.getJSONArray("function.contains");

    public static final JSONArray FUNCTION_NOT_CONTAINS = PROPERTIES.getJSONArray("function.doesNotContains");

    public static final boolean MAIL_SEND = Boolean.parseBoolean(PROPERTIES.getString("mail.send").trim());

    public static final String MAIL_IMAP_HOST = PROPERTIES.getString("mail.imap.host").trim();

    public static final String MAIL_SEND_FROM = PROPERTIES.getString("mail.send.from").trim();

    public static final String MAIL_SEND_PASS = PROPERTIES.getString("mail.send.password").trim();

    public static final JSONArray IGNORE_DIRECTORY = PROPERTIES.getJSONArray("ignore.directory");

    public static final Object[] MAIL_SEND_TO_LIST = PROPERTIES.getJSONArray("mail.send.toList").toArray();

    public static final Object[] EXTENDS_CLASS = PROPERTIES.getJSONArray("extends.class") == null ?
        null : PROPERTIES.getJSONArray("extends.class").toArray();
}
