package xyz.migoo.mise.framework;

/**
 * @author xiaomi
 * @date 2019/8/15 11:06
 */
public class TestFailure {

    private AbstractTest test;
    private Throwable throwable;

    TestFailure(AbstractTest test, Throwable throwable){
        this.test = test;
        this.throwable = throwable;
    }

}