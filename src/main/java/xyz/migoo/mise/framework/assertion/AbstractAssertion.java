package xyz.migoo.mise.framework.assertion;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.mise.framework.selenium.MiSe;
import xyz.migoo.mise.utils.StringUtil;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * @author xiaomi
 * @date 2019/8/14 16:36
 */
public abstract class AbstractAssertion {

    private JSONObject validate;

    protected Object actual;

    protected Object expected;

    private static ThreadLocal<DecimalFormat> decimalFormatter =
            ThreadLocal.withInitial(AbstractAssertion::createDecimalFormat);

    private static DecimalFormat createDecimalFormat() {
        DecimalFormat decimalFormatter = new DecimalFormat("#.#");
        // java.text.DecimalFormat.DOUBLE_FRACTION_DIGITS == 340
        decimalFormatter.setMaximumFractionDigits(340);
        decimalFormatter.setMinimumFractionDigits(1);
        return decimalFormatter;
    }
    public AbstractAssertion(){}

    public AbstractAssertion(Object actual, Object expected){
        this.actual = actual;
        this.expected = expected;
    }

    protected String objectToString(Object subj) {
        String str;
        if (subj == null || StringUtil.isEmpty(subj.toString())) {
            str = "null";
        } else if (subj instanceof List) {
            str = ((List) subj).isEmpty()?"null": JSONArray.toJSONString(subj);
        } else if (subj instanceof Map) {
            //noinspection unchecked
            str = ((Map) subj).isEmpty()?"null": JSONObject.toJSONString(subj);
        } else if (subj instanceof Double || subj instanceof Float) {
            str = decimalFormatter.get().format(subj);
        } else {
            str = subj.toString();
        }
        return str;
    }

    public void setValidate(JSONObject validate){
        this.validate = validate;
    }

    public void setExpected(Object expected){
        this.expected = expected;
    }

    public void actual(){
        Object obj = validate.get("actual");
        if (obj instanceof JSONObject) {
            this.setActualByDriver((JSONObject) obj);
        } else {
            this.actual = obj;
        }
    }

    public Object getActual(){
        return actual;
    }

    public Object getExpected(){
        return expected;
    }

    /**
     * assert
     *
     * @return result
     * @throws Exception exception
     */
    public abstract boolean assertThat() throws Exception;

    private void setActualByDriver(JSONObject json){
        MiSe miSe = (MiSe) validate.get("driver");
        switch (json.getString("operation")) {
            case "isSelected":
                this.actual = miSe.isSelected(json.getString("selector"));
                break;
            case "isEnabled":
                this.actual = miSe.isEnabled(json.getString("selector"));
                break;
            case "isDisplayed":
                this.actual = miSe.isDisplayed(json.getString("selector"));
                break;
            case "getAttribute":
                this.actual = miSe.getAttribute(json.getString("selector"), json.getString("object"));
                break;
            case "getCurrentUrl":
                this.actual = miSe.getCurrentUrl();
                break;
            case "getTitle":
                this.actual = miSe.getTitle();
                break;
            case "getPageSource":
                this.actual = miSe.getPageSource();
                break;
            default:
                this.actual = miSe.getText(json.getString("selector"));
        }
    }
}