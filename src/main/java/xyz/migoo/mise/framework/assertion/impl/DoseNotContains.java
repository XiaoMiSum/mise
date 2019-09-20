package xyz.migoo.mise.framework.assertion.impl;

import xyz.migoo.mise.framework.assertion.AbstractAssertion;

/**
 * @author xiaomi
 * @date 2019-08-13 22:17
 */
public class DoseNotContains extends AbstractAssertion {

    @Override
    public boolean assertThat() {
        return !new Contains(actual, expected).assertThat();
    }
}
