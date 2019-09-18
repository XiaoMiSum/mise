package xyz.migoo.mise.loader;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.mise.loader.reader.ReaderException;
import xyz.migoo.mise.report.MiSeLog;
import xyz.migoo.mise.loader.reader.ReaderFactory;

import static xyz.migoo.mise.config.Platform.IGNORE_DIRECTORY;

/**
 * @author xiaomi
 * @date 2019-08-17 18:10
 */
public class TestLoader {

    public static JSONObject load(String object) throws ReaderException {
        MiSeLog.log("load and parse object to json begin");
        MiSeLog.log("content: {}", object);
        JSONObject json;
        try {
            json = JSONObject.parseObject(object);
        } catch (JSONException e) {
            json = (JSONObject) ReaderFactory.getReader(object).read();
        }
        MiSeLog.log("load end");
        return json;
    }
}
