package xyz.migoo.mise.framework.assertion.impl;

import xyz.migoo.mise.framework.assertion.AbstractAssertion;

import java.math.BigDecimal;

/**
 * @author xiaomi
 * @date 2019-08-13 22:17
 */
public class Equals extends AbstractAssertion {

    public Equals(){
        super();
    }

    public Equals(Object actual, Object expected){
        super(actual, expected);
    }

    @Override
    public boolean assertThat() throws Exception{
        String str1 = objectToString(actual);
        String str2 = objectToString(expected);
        if (actual instanceof Number || expected instanceof Number){
            str1 = "null".equals(str1) ? "0" : str1;
            str2 = "null".equals(str2) ? "0" : str2;
            return new BigDecimal(str1).compareTo(new BigDecimal(str2)) == 0;
        }
        return str1.equalsIgnoreCase(str2);
    }
}
