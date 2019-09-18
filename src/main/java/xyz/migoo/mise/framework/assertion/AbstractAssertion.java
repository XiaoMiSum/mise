package xyz.migoo.mise.framework.assertion;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.mise.framework.selenium.MiSe;

/**
 * @author xiaomi
 * @date 2019/8/14 16:36
 */
public abstract class AbstractAssertion {

    private JSONObject validate;

    private Object actual;


    public void setValidate(JSONObject validate){
        this.validate = validate;
    }
    public void actual(){
        Object obj = validate.get("actual");
        if (obj instanceof JSONObject) {
            this.setActualByDriver((JSONObject) obj);
        } else {
            this.actual = obj;
        }
    }

    /**
     * assert
     *
     * @param expected  expected value
     *
     * @throws AssertionFailure assert result is false
     * @throws ExecuteError running error
     */
    public abstract void assertThat(Object expected) throws AssertionFailure, ExecuteError;

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