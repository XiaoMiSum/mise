package xyz.migoo.mise.framework.selenium;

import com.alibaba.fastjson.JSONObject;
import org.openqa.selenium.*;
import xyz.migoo.mise.config.ByEnum;
import xyz.migoo.mise.exception.MiSeException;
import xyz.migoo.mise.report.MiSeLog;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaomi
 * @date 2019/8/13 20:01
 */
public class MiSe {
    private WebDriver driver;

    private static final String XPATH_1 = ".//";
    private static final String XPATH_2 = "//";

    public static DriverBuilder builder(){
        return new DriverBuilder();
    }

    MiSe(DriverBuilder builder){
        this.driver = builder.driver();
    }

    /**
     * increasing the implicit wait timeout, timeUnit: second
     *
     * @param time time
     * @return this
     */
    public MiSe implicitlyWait(int time) {
        if (this.driver != null) {
            this.driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
        }
        return this;
    }

    /**
     * wait for page load timeout, timeUnit: second
     *
     * @param time time
     * @return this
     */
    public MiSe pageLoadTimeout(int time) {
        if (this.driver != null) {
            this.driver.manage().timeouts().pageLoadTimeout(time, TimeUnit.SECONDS);
        }
        return this;
    }

    /**
     * Maximizes the current window if it is not already maximized
     *
     * @return this
     */
    public MiSe maximize() {
        if (this.driver != null) {
            this.driver.manage().window().maximize();
            MiSeLog.log("window maximize");
        }
        return this;
    }

    /**
     * load a new web page in the current browser window.
     * @param url The URL to load. It is best to use a fully qualified URL
     */
    public void get(String url){
        driver.get(url);
        MiSeLog.log("goto url: {}", url);
    }

    /**
     * Get a string representing the current URL that the browser is looking at.
     *
     * @return The URL of the page currently loaded in the browser
     */
    public String getCurrentUrl(){
        MiSeLog.log("get current url: {}", driver.getCurrentUrl());
        return driver.getCurrentUrl();
    }

    /**
     * The title of the current page.
     *
     * @return The title of the current page, with leading and trailing whitespace stripped, or null
     *         if one is not already set
     */
    public String getTitle(){
        MiSeLog.log("get title: {}", driver.getTitle());
        return driver.getTitle();
    }

    /**
     * Find the first {@link WebElement} using the given method.
     *
     * @param selector The locating mechanism
     * @return The first matching element on the current page
     */
    public WebElement findElement(String selector){
        return driver.findElement(parseSelector(selector));
    }

    /**
     * Find all elements within the current page using the given mechanism.
     *
     * @param selector The locating mechanism
     * @return A list of all {@link WebElement}s, or an empty list if nothing matches
     */
    public List<WebElement> findElements(String selector){
        return driver.findElements(parseSelector(selector));
    }

    /**
     * Get the source of the last loaded page.
     * @return The source of the current page
     */
    public String getPageSource(){
        MiSeLog.log("get page source");
        return driver.getPageSource();
    }

    /**
     * Close the current window, quitting the browser if it's the last window currently open.
     */
    public void close(){
        if (driver != null) {
            MiSeLog.log("window close: {}", driver.getWindowHandle());
            driver.close();
        }
    }

    /**
     * Quits this driver, closing every associated window.
     */
    public void quit(){
        if (driver != null) {
            MiSeLog.log("driver quit");
            driver.quit();
        }
    }

    /**
     * Add a specific cookie. If the cookie's domain name is left blank, it is assumed that the
     * cookie is meant for the domain of the current document.
     *
     * @param cookie The cookie to add.
     */
    public void addCookie(Object cookie){
        Cookie c;
        if (cookie instanceof String){
            JSONObject json = JSONObject.parseObject(cookie.toString());
            c = new Cookie(json.getString("name"), json.getString("value"),
                    json.getString("domain"), json.getString("path"), json.getDate("expiry"));
        }else if (cookie instanceof JSONObject){
            JSONObject json = (JSONObject) cookie;
            c = new Cookie(json.getString("name"), json.getString("value"),
                    json.getString("domain"), json.getString("path"), json.getDate("expiry"));
        } else {
            c = (Cookie) cookie;
        }
        MiSeLog.log("add cookie: {}", c);
        driver.manage().addCookie(c);
    }

    /**
     * Get all the cookies for the current domain. This is the equivalent of calling
     * "document.cookie" and parsing the result
     *
     * @return A Set of cookies for the current domain.
     */
    public Set<Cookie> getCookies(){
        MiSeLog.log("get cookie");
        return driver.manage().getCookies();
    }

    /**
     * Refresh the current page
     */
    public void refresh(){
        MiSeLog.log("driver refresh");
        driver.navigate().refresh();
    }
    /**
     * Move back a single "item" in the browser's history.
     */
    public void back(){
        MiSeLog.log("driver back");
        driver.navigate().back();
    }

    /**
     * Click this element.
     * @param selector The locating mechanism
     */
    public void click(String selector){
        MiSeLog.log("click: {}", selector);
        this.findElement(selector).click();
    }

    /**
     * If this current element is a form, or an element within a form, then this will be submitted to
     * the remote server. If this causes the current page to change, then this method will block until
     * the new page is loaded.
     *
     * @param selector The locating mechanism
     * @throws NoSuchElementException If the given element is not within a form
     */
    public void submit(String selector){
        MiSeLog.log("submit: {}", selector);
        this.findElement(selector).submit();
    }

    /**
     * Use this method to simulate typing into an element, which may set its value.
     *
     * @param selector The locating mechanism
     * @param keysToSend character sequence to send to the element
     *
     * @throws IllegalArgumentException if keysToSend is null
     */
    public void sendKeys(String selector, String keysToSend){
        this.findElement(selector).clear();
        this.findElement(selector).sendKeys(keysToSend);
        MiSeLog.log("find element: {}, sendKeys: {}", selector, keysToSend);
    }

    /**
     * Get the value of the given attribute of the element. Will return the current value, even if
     * this has been modified after the page has been loaded.
     * @param selector The locating mechanism
     * @param name The name of the attribute.
     * @return The attribute/property's current value or null if the value is not set.
     */
    public String getAttribute(String selector, String name){
        String value = this.findElement(selector).getAttribute(name);
        MiSeLog.log("find element: {}, attribute: {}, value: {}", selector, name, value);
        return value;
    }

    /**
     * Determine whether or not this element is selected or not. This operation only applies to input
     * elements such as checkboxes, options in a select and radio buttons.
     *
     * @param selector The locating mechanism
     * @return True if the element is currently selected or checked, false otherwise.
     */
    public boolean isSelected(String selector){
        boolean result = this.findElement(selector).isSelected();
        MiSeLog.log("find element: {}, isSelected: {}", selector, result);
        return result;
    }

    /**
     * Is the element currently enabled or not? This will generally return true for everything but
     * disabled input elements.
     *
     * @param selector The locating mechanism
     * @return True if the element is enabled, false otherwise.
     */
    public boolean isEnabled(String selector){
        boolean result = this.findElement(selector).isEnabled();
        MiSeLog.log("find element: {}, isEnabled: {}", selector, result);
        return result;
    }

    /**
     * Get the visible (i.e. not hidden by CSS) text of this element, including sub-elements.
     *
     * @param selector The locating mechanism
     * @return The visible text of this element.
     */
    public String getText(String selector){
        String text = this.findElement(selector).getText();
        MiSeLog.log("find element: {}, text: {}", selector, text);
        return text;
    }

    /**
     * Is this element displayed or not? This method avoids the problem of having to parse an
     * element's "style" attribute.
     *
     * @param selector The locating mechanism
     * @return Whether or not the element is displayed
     */
    public boolean isDisplayed(String selector){
        boolean result = this.findElement(selector).isDisplayed();
        MiSeLog.log("find element: {}, isDisplayed: {}", selector, result);
        return result;
    }

    private By parseSelector(String selector){
        if (selector.startsWith(XPATH_1) || selector.startsWith(XPATH_2)) {
            selector = String.format("%s=%s", ByEnum.XPATH.value(), selector);
        }
        String[] str = selector.split(ByEnum.SPLITTER.value());
        switch (ByEnum.valueOf(str[0])){
            case ID:
                return By.id(str[1]);
            case CSS:
                return By.cssSelector(str[1]);
            case XPATH:
                return By.xpath(str[1]);
            case PARTIAL_LINK:
                return By.partialLinkText(str[1]);
            case LINK:
                return By.linkText(str[1]);
            case CLASS:
                return By.className(str[1]);
            case TAG_NAME:
                return By.tagName(str[1]);
            case NAME:
                return By.name(str[1]);
            default:
                throw new MiSeException(String.format("unknown selector '%s'.", selector));
        }
    }

}