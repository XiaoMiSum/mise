package xyz.migoo.mise.framework;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.mise.exception.ExtenderException;
import xyz.migoo.mise.extender.ExtenderHelper;
import xyz.migoo.mise.framework.selenium.DriverBuilder;
import xyz.migoo.mise.framework.selenium.MiSe;
import xyz.migoo.mise.report.MiSeLog;

import java.util.Vector;

/**
 * @author xiaomi
 * @date 2019/8/15 10:12
 */
public class TestSuite extends AbstractTest {

    private Vector<TestCase> fTests;
    private MiSe miSe;

    public TestSuite(JSONObject suite) {
        super(suite.getString("name"));
        this.fTests = new Vector<>(10);
        this.initSuite(suite.getJSONObject("config"));
        JSONArray cases = suite.getJSONArray("case");
        for (int i = 0; i < cases.size(); i++) {
            this.addTest(new TestCase(cases.getJSONObject(i)));
        }
    }

    public TestSuite browser(String browser, JSONObject config){
        DriverBuilder builder = MiSe.builder().browser(browser);
        if (config != null) {
            builder.bin(config.getString("bin")).driverBin(config.getString("driver"));
        }
        miSe = builder.build();
        return this;
    }

    @Override
    public int countTestCases() {
        int count= 0;
        for (Test each : fTests) {
            count += each.countTestCases();
        }
        return count;
    }

    @Override
    public void run(TestResult result) {
        MiSeLog.log("===================================================================");
        MiSeLog.log("test suite begin: {}", this.getName());
        try {
            // bind variable to variables (globals -> variables)
            ExtenderHelper.bindAndEval(variables, variables);
            this.setUp("suite setup");
            fTests.forEach(test -> {
                test.addVariables(variables);
                test.setMiSe(miSe).run(result);
            });
        } catch (Exception e){
            MiSeLog.log("test run error: {}", e);
        } finally {
            this.teardown("suite teardown");
            MiSeLog.log("test suite end: {}", this.getName());
        }

    }

    private void addTest(TestCase test){
        this.fTests.add(test);
    }

    private void initSuite(JSONObject config) {
        // 1. add config.variables to variables;
        super.addVariables(config.getJSONObject("variables"));
        // 2. add config.setup to setUp
        super.addSetUp(config.getJSONArray("setup"));
        // 3. add config.teardown to teardown
        super.addTeardown(config.getJSONArray("teardown"));
    }

    @Override
    public void setUp(String type) throws ExtenderException {
        this.miSe.implicitlyWait(60).pageLoadTimeout(60).maximize();
        super.setUp(type);
    }

    @Override
    public void teardown(String type){
        super.teardown(type);
        this.miSe.close();
        this.miSe.quit();
    }
}