package fredtest;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Utilities {
	
	public void measurePageLoad(WebDriver driver, int loadMeasure, String url) {
		// Measure the page load time
	        long startTime = System.currentTimeMillis();
	        driver.get(url);
	        long endTime = System.currentTimeMillis();
	        long loadTime = endTime - startTime;

	        // Assert that the page load time is within 3 seconds
	        assert loadTime < loadMeasure : "Page load time is greater than " + loadMeasure + " seconds";
	 }
	
	public WebDriver establishChromeWebDriver () {
		//Setting system properties of ChromeDriver
		System.setProperty("webdriver.chrome.driver", "E:/coding/chromedriver_win32_109/chromedriver.exe");
		
		ChromeOptions options = new ChromeOptions();
		List<String> list = new ArrayList<String>();
		list.add("--headless");
		list.add("--no-sandbox");
		list.add("--disable-dev-shm-usage");
		options.addArguments(list);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		options.merge(capabilities);

		// Create a new instance of the Chrome driver
	    return new ChromeDriver(options);
	}

}
