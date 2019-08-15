package xyz.migoo.mise.runner;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.mise.exception.ReaderException;
import xyz.migoo.mise.framework.core.TestResult;
import xyz.migoo.mise.framework.core.TestSuite;
import xyz.migoo.mise.log.MiSeLog;

/**
 * @author yacheng.xiao
 * @date 2019/8/15 23:54
 */
public class TestRunner {

    private JSONObject env;

    public void run(Object pathOrTestSet, Object env, String... browsers){
        try {
            this.parseEnv(env);
            JSONObject testSet = this.parseTestSets(pathOrTestSet);
            if (browsers != null){
                for (String browser : browsers){
                    TestSuite suite = new TestSuite(testSet, browser);
                    TestResult result = new TestResult(1, suite.getName());
                    suite.run(result, this.env.getJSONObject("vars"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseEnv(Object env){
        if (env instanceof String){
            try {
                this.env = JSONObject.parseObject(env.toString());
            } catch (JSONException e){
                // todo this.env =
            }
        }
        if (env instanceof JSONObject){
            this.env = (JSONObject) env;
        }
    }

    private JSONObject parseTestSets(Object pathOrTestSet) throws Exception{
        JSONObject testSet = null;
        if (pathOrTestSet instanceof JSONObject){
            testSet = (JSONObject) pathOrTestSet;
        } else {
            try {
                testSet = JSONObject.parseObject(pathOrTestSet.toString());
            }  catch (JSONException e){
                // todo testSet
            }
        }
        return testSet;
    }
}