package xyz.migoo.mise.framework.assertion;

/**
 * @author xiaomi
 * @date 2019/8/15 14:06
 */
public class AssertionFactory {
    private static final String PACKAGE = "xyz.migoo.mise.framework.assertion.%s";

    public static AbstractAssertion getAssert(String clz) throws Exception{
        String clazz = String.format(PACKAGE, clz);
        return (AbstractAssertion)Class.forName(clazz).newInstance();
    }
}