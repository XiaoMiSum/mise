package xyz.migoo.mise.framework.core;import com.alibaba.fastjson.JSONObject;/** * @author yacheng.xiao * @date 2019/8/14 20:09 */public interface Test {    /**     * Returns count of the test.     * can return zero.     *     * @return test count     */    int countTestCases();    /**     * running test     * @param result the test result     * @param vars variables     */    void run(TestResult result, JSONObject vars);}