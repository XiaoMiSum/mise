package xyz.migoo.mise.framework.selenium;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import xyz.migoo.mise.exception.MiSeException;

import java.io.File;
import java.util.regex.Pattern;

import static org.openqa.selenium.remote.BrowserType.*;

/**
 * @author xiaomi
 * @date 2019/8/13 20:11
 */
public class DriverBuilder{
    private static final Pattern PATTERN = Pattern.compile(
            "^http[s]*://[\\w\\.\\-]+(:\\d*)*(?:/|(?:/[\\w\\.\\-]+)*)?$", Pattern.CASE_INSENSITIVE);

    private WebDriver driver;
    private String browser;
    private File bin;
    private String exePath;
    private BrowserVersion version;
    private Boolean headless = Boolean.FALSE;

    DriverBuilder(){
    }

    WebDriver driver(){
        return driver;
    }

    public DriverBuilder browser(String browser){
        this.browser = browser;
        return this;
    }
    public DriverBuilder chrome(){
        this.browser = CHROME;
        return this;
    }

    public DriverBuilder firefox(){
        this.browser = FIREFOX;
        return this;
    }

    public DriverBuilder safari(){
        this.browser = SAFARI;
        return this;
    }

    public DriverBuilder htmlUnit() {
        return this.htmlUnit(null);
    }

    public DriverBuilder htmlUnit(BrowserVersion version) {
        this.browser = HTMLUNIT;
        if(version != null){
            this.version = version;
        }
        return this;
    }

    /**
     * If your browser is not installed in the default directory, set the browser bin file path
     * @param bin browser bin file path
     * @return this
     */
    public DriverBuilder bin(String bin) {
        if (!"".equals(bin) && bin != null) {
            this.bin = new File(bin);
            if (!this.bin.exists() || this.bin.isDirectory()) {
                throw new MiSeException("browser bin not exists or is directory");
            }
        }
        return this;
    }

    /**
     * the browser driver bin path
     * need set driver list：firefox  chrome  safari
     * @param exePath driver bin file path
     * @return this
     */
    public DriverBuilder driverBin(String exePath) {
        if (!"".equals(exePath) && exePath != null) {
            File driverBin = new File(exePath);
            if (!driverBin.exists() || driverBin.isDirectory()) {
                throw new MiSeException("driver bin not exists or is directory");
            }
            this.exePath = exePath;
        }
        return this;
    }

    /**
     * headless mode
     * The results of running in Headless mode may differ from those of a normal browser startup
     * @param headless true / false
     * @return this
     */
    public DriverBuilder headless(boolean headless) {
        this.headless = headless;
        return this;
    }

    private void setDriver() {
        if (FIREFOX.equals(browser)) {
            System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, exePath);
            firefoxOptions();
        } else if (CHROME.equals(browser)) {
            System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, exePath);
            chromeOptions();
        } else if (SAFARI.equals(browser)){
            safariOptions();
        }else {
            htmlUnitOptions();
        }
    }

    private void firefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        if (bin != null) {
            FirefoxBinary binary = new FirefoxBinary(bin);
            options.setBinary(binary);
        }
        options.setHeadless(headless);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        driver = new FirefoxDriver(options);
    }

    private void chromeOptions() {
        ChromeOptions options = new ChromeOptions();
        if (bin != null) {
            options.setBinary(bin);
        }
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.setHeadless(headless);
        driver = new ChromeDriver(options);
    }

    private void safariOptions() {
        SafariOptions options = new SafariOptions();
        driver = new SafariDriver(options);
    }

    private void htmlUnitOptions(){
        if (version != null){
            driver = new HtmlUnitDriver(version, true);
        }else {
            driver = new HtmlUnitDriver(true);
        }
    }

    public MiSe build() {
        if ("".equals(browser) || null == browser) {
            throw new MiSeException("browser == null");
        }
        this.setDriver();
        return new MiSe(this);
    }
}