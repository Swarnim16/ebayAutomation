package test.java;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

/**
 * <h1>Setup Utility</h1> Class BaseSetupUtility contains data and method
 * members to invoke Browser. Object is used by other classes to open Chrome and
 * Firefox browser. We can add code for other browsers also.
 * <p>
 * <b>Note:</b> Code re-usability example.
 */
public class BaseSetupUtility {

	private WebDriver driver;
	static String driverPath = "src/test/resources/";

	/**
	 * <h1>getDriver</h1> Method called to return specified Browser.
	 */
	public WebDriver getDriver() {
		return driver;
	}

	/**
	 * <h1>setDriver method</h1> Method taking 2 parameters
	 * 
	 * @param browserType
	 * @param appURL
	 *            and calling methods by passing URL path value on the bases of
	 *            browser type value.
	 */
	private void setDriver(String browserType, String appURL) {
		switch (browserType) {
		case "chrome":
			driver = initChromeDriver(appURL);
			break;
		case "firefox":
			driver = initFirefoxDriver(appURL);
			break;
		default:
			System.out.println("browser : " + browserType + " is invalid, Launching Firefox as browser of choice..");
			driver = initFirefoxDriver(appURL);
		}
	}

	/**
	 * <h1>initChromeDriver method</h1> Method taking 1 parameter
	 * 
	 * @param appURL
	 *            Code to initialize the Chrome browser and to open the given URL
	 *            address.
	 */
	private static WebDriver initChromeDriver(String appURL) {
		System.out.println("Launching google chrome with new profile..");
		System.setProperty("webdriver.chrome.driver", driverPath + "chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.navigate().to(appURL);
		return driver;
	}

	/**
	 * <h1>initFirefoxDriver method</h1> Method taking 1 parameter
	 * 
	 * @param appURL
	 *            Code to initialize the Firefox browser and to open the given URL
	 *            address.
	 */
	private static WebDriver initFirefoxDriver(String appURL) {
		System.out.println("Launching Firefox browser..");
		WebDriver driver = new FirefoxDriver();
		driver.manage().window().maximize();
		driver.navigate().to(appURL);
		return driver;
	}

	/**
	 * <h1>initializeTestBaseSetup method</h1> Method taking 2 parameters
	 * 
	 * @param appURL
	 * @param browserType
	 *            from the XML file. Code to call setDriver() method by passing 2
	 *            parameters.
	 */
	@Parameters({ "browserType", "appURL" })
	@BeforeClass
	public void initializeTestBaseSetup(String browserType, String appURL) {
		try {
			setDriver(browserType, appURL);

		} catch (Exception e) {
			System.out.println("Error....." + e.getStackTrace());
		}
	}

	/**
	 * <h1>tearDown method</h1> It is using to close all window of the browser that
	 * is opened by the driver object.
	 */
	@AfterClass
	public void tearDown() {
		driver.quit();
	}

}
