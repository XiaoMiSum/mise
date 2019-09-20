package xyz.migoo.mise.framework;

import xyz.migoo.mise.framework.assertion.AssertionFailure;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author xiaomi
 * @date 2019/8/15 11:06
 */
public class TestFailure {

    private AbstractTest fFailedTest;
    private Throwable fThrownException;

    TestFailure(AbstractTest fFailedTest, Throwable fThrownException){
        this.fFailedTest = fFailedTest;
        this.fThrownException = fThrownException;
    }
    /**
     * Gets the failed test.
     */
    public AbstractTest failedTest() {
        return fFailedTest;
    }
    /**
     * Gets the thrown exception.
     */
    public Throwable thrownException() {
        return fThrownException;
    }
    /**
     * Returns a short description of the failure.
     */
    @Override
    public String toString() {
        return fFailedTest + ": " + fThrownException.getMessage();
    }

    public String trace() {
        StringWriter stringWriter= new StringWriter();
        PrintWriter writer= new PrintWriter(stringWriter);
        fThrownException.printStackTrace(writer);
        StringBuffer buffer= stringWriter.getBuffer();
        return buffer.toString();
    }
    public String exceptionMessage() {
        return fThrownException.getMessage();
    }
    public boolean isFailure() {
        return thrownException() instanceof AssertionFailure;
    }
}