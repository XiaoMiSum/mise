package xyz.migoo.mise.framework.assertion;

import xyz.migoo.mise.framework.assertion.impl.Contains;
import xyz.migoo.mise.framework.assertion.impl.DoseNotContains;
import xyz.migoo.mise.framework.assertion.impl.DoseNotEquals;
import xyz.migoo.mise.framework.assertion.impl.Equals;
import xyz.migoo.mise.utils.StringUtil;

import static xyz.migoo.mise.config.Platform.*;

/**
 * @author xiaomi
 * @date 2019/8/15 14:06
 */
public class AssertionFactory {

    public static AbstractAssertion getAssertion(String searchChar) throws Exception {
        if (FUNCTION_EQUALS.contains(searchChar)) {
            return new Equals();
        }
        if (FUNCTION_NOT_EQUALS.contains(searchChar)) {
            return new DoseNotEquals();
        }
        if (FUNCTION_CONTAINS.contains(searchChar)) {
            return new Contains();
        }
        if (FUNCTION_NOT_CONTAINS.contains(searchChar)) {
            return new DoseNotContains();
        }
        if (!StringUtil.isEmpty(searchChar)){
            return (AbstractAssertion) Class.forName(searchChar).newInstance();
        }
        throw new ExecuteError(String.format("assert method '%s' not found", searchChar));
    }
}