package xyz.migoo.mise.framework.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.mise.framework.selenium.MiSe;
import xyz.migoo.mise.log.MiSeLog;

import java.util.Vector;

/**
 * @author yacheng.xiao
 * @date 2019/8/15 10:12
 */
public class TestSuite extends AbstractTest {

    private Vector<AbstractTest> fTests;
    private MiSe miSe;

    public TestSuite(JSONObject suite, String browser) {
        super(suite.getString("name"));
        this.fTests = new Vector<>(10);
        this.addSuiteInfo(suite.getJSONObject("config"));
        miSe = this.initMiSe(browser, suite.getJSONObject("browser").getJSONObject("browser"));
        JSONArray cases = suite.getJSONArray("case");
        for (int i = 0; i < cases.size(); i++) {
            this.addTest(new TestCase(cases.getJSONObject(i)).setMiSe(miSe));
        }
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
    public void run(TestResult result, JSONObject globals) {
        MiSeLog.log("===================================================================");
        MiSeLog.log("test suite begin: {}", this.getName());
        try {
            // bind variable to variables (globals -> variables)
            // todo BindVariable.bind(globals, super.variables, true);
            this.setUp("suite setup");
            fTests.forEach(test -> test.run(result, super.variables));
            this.teardown("suite teardown");
        } catch (Exception e){
            MiSeLog.log("test run error: {}", e);
        } finally {
            MiSeLog.log("test suite end: {}", this.getName());
            MiSeLog.log("===================================================================");
        }

    }

    private void addTest(AbstractTest test){
        this.fTests.add(test);
    }

    private void addSuiteInfo(JSONObject config){
        super.addVariables(config.getJSONObject("variables"));
        // 2. // bind variable to variables (variables -> variables)
        // todo BindVariable.bind(super.variables, super.variables);
        // 3. add config.beforeClass to setUp
        super.addSetUp(config.getJSONArray("setup"));
        // 4. add config.beforeClass to teardown
        super.addTeardown(config.getJSONArray("teardown"));
    }

    private MiSe initMiSe(String browser, JSONObject config){
        return MiSe.builder()
                .bin(config.getString("bin"))
                .driverBin(config.getString("driver"))
                .browser(browser)
                .build();
    }

    @Override
    public void setUp(String type){
        this.miSe.implicitlyWait(60).pageLoadTimeout(60).maximize();
        super.setUp(type);
    }

    @Override
    public void teardown(String type){
        super.setUp(type);
        this.miSe.close();
        this.miSe.quit();
    }
}