package xyz.migoo.mise.extender;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.mise.exception.ExtenderException;
import xyz.migoo.mise.report.MiSeLog;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaomi
 */
public class MethodHelper {

    private static final String SEPARATOR = ",";
    private static final Pattern REGEX_LONG = Pattern.compile("^[-\\+]?[0-9]+$");
    private static final Pattern REGEX_FLOAT = Pattern.compile("^[-\\+]?[0-9]+\\.[0-9]+$");

    /**
     * 从指定 扩展类中 获取扩展函数
     */
    static Map<String, Method> loadFunction(Object[] classes) throws ExtenderException {
        if (classes == null){
            return new HashMap<>(0);
        }
        try {
            Map<String, Method> map = new HashMap<>(100);
            for (Object clazz : classes) {
                Class clz = Class.forName((String) clazz);
                for (; clz != null && clz != Object.class; clz = clz.getSuperclass()){
                    Method[] methods = clz.getDeclaredMethods();
                    for (Method method : methods) {
                        String key = String.format("%s(%s)", method.getName(), method.getParameterCount());
                        map.put(key, method);
                    }
                }
            }
            return map;
        }catch (Exception e){
            throw new ExtenderException(e.getMessage());
        }
    }

    /**
     * 生成 参数数组
     *
     * @param params 参数字符串，处理时使用,分割
     * @param variables 变量，参数可能使用变量，需要从此对象取出
     */
    private static Object[] loadParameter(String params, JSONObject variables) throws RuntimeException {
        if (params == null || "".equals(params.trim())) {
            return null;
        }
        String[] strParams = params.split(SEPARATOR);
        Object[] parameters = new Object[strParams.length];
        for (int i = 0; i < strParams.length; i++) {
            parseParameter(parameters, strParams[i], i);
            if (parameters[i] == null){
                parseParameter(parameters, strParams[i], i, variables);
            }
        }
        return parameters;
    }

    private static void parseParameter(Object[] parameters, String parameter, int index){
        if (REGEX_LONG.matcher(parameter).find()){
            parameters[index] = Long.valueOf(parameter);
        } else if (REGEX_FLOAT.matcher(parameter).find()){
            parameters[index] = Double.valueOf(parameter);
        } else if ("true".equalsIgnoreCase(parameter) || "false".equalsIgnoreCase(parameter)){
            parameters[index] = Boolean.valueOf(parameter);
        }
    }

    private static void parseParameter(Object[] parameters, String parameter, int index, JSONObject variables){
        Matcher param = ExtenderHelper.PARAM_PATTERN.matcher(parameter);
        if (param.find()) {
            if (variables != null && !variables.isEmpty()){
                Object object = variables.get(parameter.substring(2, parameter.length() -1));
                if (ExtenderHelper.FUNC_PATTERN.matcher(String.valueOf(object)).find()
                        || ExtenderHelper.PARAM_PATTERN.matcher(String.valueOf(object)).find()){
                    throw new RuntimeException(String.format("%s need eval!", param.group(1)));
                }
                parameters[index] = object;
                return;
            }
        }
        parameters[index] = parameter;
    }

    /**
     * 获取 Method 对象
     *
     * @param methods 保存了 Method 对象的 Map
     * @param name method 名称
     * @param params 参数字符串，处理时使用,分割
     */
    private static Method method(Map<String, Method> methods, String name, Object[] params){
        if (params == null){
            return methods.get(String.format("%s(0)", name));
        }else {
            return methods.get(String.format("%s(%s)", name, params.length));
        }
    }

    /**
     * 执行 Method
     *
     * @param methods 保存了 Method 对象的 Map
     * @param name method 名称
     * @param parameter 参数数组
     */
    static Object invoke(Map<String, Method> methods, String name, String parameter, JSONObject variables)
            throws ExtenderException {
        Object result;
        try {
            Object[] parameters = loadParameter(parameter, variables);
            result = method(methods, name, parameters).invoke(null, parameters);
            MiSeLog.log(String.format("invoke success, method [%s] -> parameter [%s] -> return [%s]", name, parameter, result));
        } catch (NullPointerException e){
            throw new ExtenderException(String.format("method '%s(%s)' not found !", name, parameter));
        } catch (Exception e) {
            throw new ExtenderException(String.format("invoke error, method name '%s', parameter '%s'", name, parameter), e);
        }
        return result;
    }


}
