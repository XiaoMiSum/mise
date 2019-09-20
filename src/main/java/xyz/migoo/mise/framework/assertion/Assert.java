package xyz.migoo.mise.framework.assertion;

/**
 * @author xiaomi
 * @date 2019/9/19 23:26
 */
public class Assert {

    public static void assertThat(AbstractAssertion assertion) throws AssertionFailure, ExecuteError{
        boolean result;
        try {
            result = assertion.assertThat();
        } catch (Exception e) {
            throw new ExecuteError("assert error.", e);
        }
        if (!result){
            String msg = "Value expected to be '%s', but found '%s' \n" +
                    "Assertion class is '%s'";
            throw new AssertionFailure(String.format(msg, assertion.getExpected(), assertion.getActual(),
                    assertion.getClass().getSimpleName()));
        }
    }
}
