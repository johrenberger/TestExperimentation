package fredtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;


@RunWith(Parameterized.class)
public class FredDynamicLinks_UT {
	private static WebDriver driver;
	
	private static String url = "https://fred.stlouisfed.org/";
	private static List<String> filteredUrls;
	private String validUrl;
	
	public FredDynamicLinks_UT(String validUrl) {
		this.validUrl = validUrl;
	}
	
	@BeforeClass
	public static void beforeTests() {
		
		
	}
	
	@AfterClass
	public static void afterTests() {
		 // Close the browser
        driver.quit();
	}
	
	
	
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
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
    	driver = new ChromeDriver(options);
    	filteredUrls = Arrays.asList("https://uapi.stlouisfed.org/","https://apis.google.com/","https://www.googletagmanager.com/","https://fonts.gstatic.com/");
    	
		// Navigate to the FRED homepage
        driver.get(url);
        // Wait for the page to load
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until((ExpectedCondition<Boolean>) driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));

        // Get all links on the page
        List<WebElement> links = driver.findElements(By.tagName("link"));

        Collection<Object[]> data = new ArrayList<>();
        
        for (WebElement link : links) {
        	if(!filteredUrls.contains(link.getAttribute("href"))) {
        		data.add(new Object[] {link.getAttribute("href")});
        	}
        }
        System.out.println("Number of valid URLs: " + data.size());
                
        return data;
        
        
    }

    @Test
    public void testLink() {
        try {
            URL link = new URL(validUrl);
            HttpURLConnection httpConn = (HttpURLConnection) link.openConnection();
            httpConn.setConnectTimeout(2000);
            httpConn.connect();
            System.out.println(validUrl);
            // Assert that the link returns a 200 OK status
            assertEquals("Failed " + validUrl + " with code: " + httpConn.getResponseCode(),httpConn.getResponseCode(), 200);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
