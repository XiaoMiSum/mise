package xyz.migoo.mise.framework.assertion.impl;

import xyz.migoo.mise.framework.assertion.AbstractAssertion;
import xyz.migoo.mise.framework.assertion.AssertionFailure;
import xyz.migoo.mise.framework.assertion.ExecuteError;

import java.math.BigDecimal;

/**
 * @author xiaomi
 * @date 2019-08-13 22:17
 */
public class DoseNotEquals extends AbstractAssertion {

    @Override
    public boolean assertThat() throws Exception {
        return !new Equals(actual, expected).assertThat();
    }
}
