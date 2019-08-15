package xyz.migoo.mise.framework.assertion;

/**
 * @author yacheng.xiao
 * @date 2019/8/15 14:06
 */
public class AssertionFactory {

    public static AbstractAssertion getAssert(String clz) throws Exception{
        String clazz = getRealClass(clz);
        return (AbstractAssertion)Class.forName(clazz).newInstance();
    }

    private static String getRealClass(String clz){
        String pack = "xyz.migoo.mise.framework.assertion.s%";

        return null;
    }
}