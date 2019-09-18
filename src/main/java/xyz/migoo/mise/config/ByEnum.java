package xyz.migoo.mise.config;

/**
 * @author xiaomi
 * @date 2019/8/14 11:40
 */
public enum ByEnum {

    /**
     * selenium or appium support localing mechanism
     */
    XPATH("xpath"),
    ID("id"),
    PARTIAL_LINK("partiallink"),
    LINK("link"),
    CSS("css"),
    CLASS("class"),
    TAG_NAME("tagname"),
    NAME("name"),
    ACCESSIBILITY_ID("accessibilityId"),
    UI_AUTOMATOR("uiautomator"),
    UI_AUTOMATION("uiautomation"),
    PREDICATE("predicate"),
    IOS_CLASS("iosclass"),
    SPLITTER("=");

    private String value;

    ByEnum(String value){
        this.value = value;
    }

    public String value(){
        return this.value;
    }
}