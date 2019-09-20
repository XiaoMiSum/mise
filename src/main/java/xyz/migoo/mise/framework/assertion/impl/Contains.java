package xyz.migoo.mise.framework.assertion.impl;

import xyz.migoo.mise.framework.assertion.AbstractAssertion;
import xyz.migoo.mise.utils.StringUtil;

/**
 * @author xiaomi
 * @date 2019-08-13 22:17
 */
public class Contains extends AbstractAssertion {

    public Contains(){
        super();
    }

    public Contains(Object actual, Object expected){
        super(actual, expected);
    }

    @Override
    public boolean assertThat() {
        return ((String) actual).contains(StringUtil.valueOf(expected));
    }
}
