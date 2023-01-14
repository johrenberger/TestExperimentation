package fredtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class fredSimple_UT {
	
	private static WebDriver driver;
	private static Utilities util = new Utilities();
	private String url = "https://fred.stlouisfed.org/";
	private String homeSearchBarKey = "st";
	private String homeSearchButtonClass = "homepage-search-button";
	private String searchResultByClass = "search-result-list";
	private List<String> filteredUrls = Arrays.asList("https://uapi.stlouisfed.org/","https://apis.google.com/","https://www.googletagmanager.com/","https://fonts.gstatic.com/");
	
	@BeforeClass
	public static void beforeTests() {
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
		
	}
	
	@AfterClass
	public static void afterTests() {
		 // Close the browser
        driver.quit();
	}
	
	@Test
    public void testSearch() {
		
        // Navigate to the FRED homepage
        driver.get(url);

        // Locate the search bar
        WebElement searchBar = driver.findElement(By.name(homeSearchBarKey));

        // Enter "cpi" into the search bar
        searchBar.sendKeys("cpi");

     // Click the search button
        driver.findElement(By.className(homeSearchButtonClass)).click();

        
        evaluateSearchResults(driver);
        
       
    }
	
	@Test
	public void testPageLoad() {	
		// Navigate to the FRED homepage
        driver.get(url);
		util.measurePageLoad(driver, 3000, url);
	}
	
	private void evaluateSearchResults(WebDriver driver) {
		 
		System.out.println("Testing Results");
		 // Create a new WebDriverWait object
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

	        // Wait for the search results to be displayed
	        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search-results")));

	        // Store the search results
	        WebElement searchResults = driver.findElement(By.id("search-results"));

	        // Print the search results
//	        System.out.println(searchResults.getText());

	        // Assert that the search results are greater than 0
	        assertTrue(searchResults.getText().length() > 0);
	 }
	
	@Test
    public void testLinks() {
		// Navigate to the FRED homepage
        driver.get(url);
//        driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
        // Wait for the page to load
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until((ExpectedCondition<Boolean>) driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));

        // Get all links on the page
        List<WebElement> links = driver.findElements(By.tagName("link"));

        // Iterate through each link and test its validity
        for (WebElement link : links) {
            String url = link.getAttribute("href");
            System.out.println(url);
        }
        for (WebElement link : links) {
        	String url = link.getAttribute("href");
        	if(!filteredUrls.contains(url)) {
                System.out.println(url);
                testLink(url);	
        	}
                
        }
        System.out.println("Links validated: " + links.size());
    }

    private void testLink(String url) {
        try {
            URL link = new URL(url);
            HttpURLConnection httpConn = (HttpURLConnection) link.openConnection();
            httpConn.setConnectTimeout(2000);
            httpConn.connect();

            // Assert that the link returns a 200 OK status
            assertEquals(httpConn.getResponseCode(), 200);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
	
}
