package xyz.migoo.mise.extender;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.mise.exception.ExtenderException;
import xyz.migoo.mise.report.MiSeLog;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static xyz.migoo.mise.config.Platform.EXTENDS_CLASS;

/**
 * @author xiaomi
 */
public class ExtenderHelper {


    static final Pattern FUNC_PATTERN = Pattern.compile("^__(\\w+)\\((.*)\\)");

    static final Pattern PARAM_PATTERN = Pattern.compile("(\\$\\{(\\w+)})+");

    private static Map<String, Method> methods = null;

    static {
        try {
            methods = MethodHelper.loadFunction(EXTENDS_CLASS);
        } catch (ExtenderException e) {
            MiSeLog.log("load extends function exception. ", e);
        }
    }

    private ExtenderHelper() {
    }

    public static void bind(Object use, JSONObject vars) {
        if (use == null || vars == null) {
            return;
        }
        if (use instanceof JSONObject) {
            bind((JSONObject) use, vars);
        }
        if (use instanceof JSONArray) {
            bind((JSONArray) use, vars);
        }
    }

    private static void bind(JSONObject use, JSONObject vars) {
        for (String key : vars.keySet()) {
            if (FUNC_PATTERN.matcher(vars.getString(key)).find()) {
                continue;
            }
            bind(key, vars, use);
        }
    }

    private static void bind(JSONArray use, JSONObject vars) {
        for (String key : vars.keySet()) {
            if (FUNC_PATTERN.matcher(vars.getString(key)).find()) {
                continue;
            }
            bind(key, vars, use);
        }
    }

    private static void bind(String varsKey, JSONObject vars, JSONObject use) {
        String regex = "${" + varsKey + "}";
        for (String uKey : use.keySet()) {
            Object object = use.get(uKey);
            if (object instanceof String) {
                String v = (String) object;
                if (v.equals(regex)) {
                    use.put(uKey, vars.getString(varsKey));
                    MiSeLog.log("bind variable: {} -> {}", v, use.get(uKey));
                }else if (v.contains(regex)) {
                    use.put(uKey, v.replace(regex, vars.getString(varsKey)));
                    MiSeLog.log("bind variable: {} -> {}", v, use.get(uKey));
                }
                continue;
            }
            if (object instanceof JSONObject) {
                bind((JSONObject) object, vars);
            }
            if (object instanceof JSONArray) {
                bind((JSONArray) object, vars);
            }
        }
    }

    private static void bind(String varsKey, JSONObject vars, JSONArray use) {
        String regex = "${" + varsKey + "}";
        for (int i = 0; i < use.size(); i++) {
            Object object = use.get(i);
            if (object instanceof String) {
                String v = (String) object;
                if (v.equals(regex)) {
                    use.remove(i);
                    use.add(i, vars.getString(varsKey));
                    MiSeLog.log("bind variable: {} -> {}", v, use.get(i));
                } else if (v.contains(regex)) {
                    use.remove(i);
                    use.add(i, v.replace(regex, vars.getString(varsKey)));
                    MiSeLog.log("bind variable: {} -> {}", v, use.get(i));
                }
            }
            if (object instanceof JSONObject) {
                bind((JSONObject) object, vars);
            }
            if (object instanceof JSONArray) {
                bind((JSONArray) object, vars);
            }
        }
    }

    /**
     * 执行 validate.expect 指定的方法，将值put到 validate 对象中
     *
     * @param use 使用方法变量的对象
     */
    private static void evalVariables(JSONObject use, JSONObject variables) throws ExtenderException {
        if (use == null) {
            return;
        }
        for (String key : use.keySet()) {
            String value = use.getString(key);
            if (value == null || "".equals(value.trim())) {
                return;
            }
            Matcher func = FUNC_PATTERN.matcher(value);
            if (func.find()) {
                Object result = MethodHelper.invoke(methods, func.group(1), func.group(2), variables);
                use.put(key, result);
            }
        }
    }

    public static void bindAndEval(JSONObject use, JSONObject variables) throws ExtenderException {
        if (use == null) {
            return;
        }
        bind(use, variables);
        evalVariables(use, variables);
        bind(use, variables);
    }

    public static void hook(String object, JSONObject variables) throws ExtenderException {
        if (object == null) {
            return;
        }
        Matcher func = FUNC_PATTERN.matcher(object);
        if (func.find()) {
            MethodHelper.invoke(methods, func.group(1), func.group(2), variables);
        }
    }
}
