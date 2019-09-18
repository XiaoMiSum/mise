package xyz.migoo.mise.framework;import com.alibaba.fastjson.JSONArray;import com.alibaba.fastjson.JSONObject;import xyz.migoo.mise.exception.ExtenderException;import xyz.migoo.mise.extender.ExtenderHelper;import xyz.migoo.mise.report.MiSeLog;/** * @author xiaomi * @date 2019/8/14 20:42 */public abstract class AbstractTest implements Test {    private String fName;    private JSONArray setUp = new JSONArray();    private JSONArray teardown = new JSONArray();    JSONObject variables = new JSONObject();    AbstractTest(String name){        this.fName = name;    }    /**     * Returns the name of the test. Not all     * test suites have a name and this method     * can return null.     *     * @return test name     */    public String getName() {        return fName;    }    /**     * Sets the name of the test.     *     * @param name the name to set     */    public void setName(String name) {        this.fName = name;    }    /**     * add the variables of test.     *     * @param variables the variables to set     */    public void addVariables(JSONObject variables) {        if (variables != null) {            this.variables.putAll(variables);        }    }    /**     * add the setUp of the suite or case.     *     * @param setUp the setUp to set     */    void addSetUp(JSONArray setUp) {        if (setUp != null) {            this.setUp.addAll(setUp);        }    }    /**     * invoke the setUp of the test.     */    public void setUp(String type) throws ExtenderException {        MiSeLog.log("{} begin", type);        for (int i = 0; i < setUp.size(); i++) {            ExtenderHelper.hook(setUp.getString(i), variables);        }        MiSeLog.log("{} end", type);    }    /**     * add the teardown of the test.     *     * @param teardown the teardown to set     */    void addTeardown(JSONArray teardown) {        if (teardown != null) {            this.teardown.addAll(teardown);        }    }    /**     * invoke the teardown of the test.     */    void teardown(String type)  {        // bind variable to setUp (this.variables -> this.teardown)        MiSeLog.log("{} begin", type);            teardown.forEach(value -> {                try {                    ExtenderHelper.hook((String)value, variables);                } catch (ExtenderException e) {                    MiSeLog.log(value + " error", e);                }            });        MiSeLog.log("{} end", type);    }}