package fredtest;



import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class HelloWorld {
    public static void main(String[] args) {
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
        WebDriver driver = new ChromeDriver(options);

        // Navigate to the FRED homepage
        driver.get("https://fred.stlouisfed.org");
        
        // Print the title of the page
        System.out.println(driver.getTitle());

        // Close the browser
        driver.quit();
    }
}




