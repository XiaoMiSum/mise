package xyz.migoo.mise.runner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.mise.exception.ExtenderException;
import xyz.migoo.mise.extender.ExtenderHelper;
import xyz.migoo.mise.loader.TestLoader;
import xyz.migoo.mise.loader.reader.ReaderException;
import xyz.migoo.mise.framework.TestResult;
import xyz.migoo.mise.framework.TestSuite;
import xyz.migoo.mise.report.MiSeLog;
import xyz.migoo.mise.report.Report;

import java.io.File;

import static org.openqa.selenium.remote.BrowserType.CHROME;
import static org.openqa.selenium.remote.BrowserType.FIREFOX;
import static xyz.migoo.mise.config.Platform.IGNORE_DIRECTORY;

/**
 * @author xiaomi
 * @date 2019/8/15 23:54
 */
public class TestRunner {

    private JSONObject env;

    private Report report = new Report();

    public TestRunner(){}

    public TestRunner(String projectName){
        report.setProjectName(projectName);
    }

    public void run(String path, String env, String... browsers){
        try {
            this.env = TestLoader.load(env);
            this.parseEnv(env);
            for (String browser : browsers) {
                this.run(new File(path), browser);
                report.generateIndex();
            }
        } catch (Exception e) {
            MiSeLog.log("init test exception.", e);
        }
    }

    public void firefox(String path, String env) {
        try {
            this.parseEnv(env);
            this.run(new File(path), FIREFOX);
            // todo firefox_index
        } catch (Exception e) {
            MiSeLog.log("init test exception.", e);
        }
    }

    public void chrome(String path, String env) {
        try {
            this.parseEnv(env);
            this.run(new File(path), CHROME);
            // todo chrome_index
        } catch (Exception e) {
            MiSeLog.log("init test exception.", e);
        }
    }

    private void  run(File file, String browser) throws ReaderException {
        if (file.isDirectory()){
            File[] fList = file.listFiles();
            assert fList != null;
            for (File f : fList) {
                if (file.getName().startsWith(".") || IGNORE_DIRECTORY.contains(file.getName())) {
                    continue;
                }
                run(f, browser);
            }
        }else {
            TestSuite suite = new TestSuite(TestLoader.load(file.getPath()));
            TestResult result = this.runTest(suite, browser);
            report.addResult(result);
            report.generateReport();
        }
    }

    private TestResult runTest(TestSuite suite, String browser){
        TestResult result = new TestResult(1, suite.getName());
        result.setStartTime(System.currentTimeMillis());
        suite.addVariables(env.getJSONObject("vars"));
        suite.browser(browser, env.getJSONObject("browser"))
                .run(result);
        result.setEndTime(System.currentTimeMillis());
        return result;
    }

    private void parseEnv(String env) throws ReaderException, ExtenderException {
        this.env = TestLoader.load(env);
        if (this.env != null){
            JSONObject vars = this.env.getJSONObject("vars") == null? new JSONObject() : this.env.getJSONObject("vars");
            ExtenderHelper.bindAndEval(vars, vars);
            JSONArray hook = this.env.getJSONArray("hook") == null ? new JSONArray() : this.env.getJSONArray("hook");
            for (int i = 0; i < hook.size(); i++) {
                ExtenderHelper.hook(hook.getString(i), vars);
            }
        } else {
            this.env = new JSONObject();
        }
    }
}