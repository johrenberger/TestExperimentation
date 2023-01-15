package fredtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@RunWith(Parameterized.class)
public class FredSearchResults_UT {

	private static WebDriver driver;

	
	private String validUrl;

	public FredSearchResults_UT(String validUrl) {
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

		Collection<Object[]> data = new ArrayList<>();
		// Setting system properties of ChromeDriver
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
		// Navigate to the FRED homepage
		driver.get("https://fred.stlouisfed.org/");

		// Locate the search bar
		WebElement searchBar = driver.findElement(By.name("st"));

		// Enter "cpi" into the search bar
		searchBar.sendKeys("cpi");

		// Click the search button
		driver.findElement(By.className("homepage-search-button")).click();

		data = evaluateSearchResults(driver, data);

		return data;

	}

	private static Collection<Object[]> evaluateSearchResults(WebDriver driver, Collection<Object[]> data) {

		System.out.println("Testing Results");
		// Create a new WebDriverWait object
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

		// Wait for the search results to be displayed
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search-results")));

		// Store the search results
		WebElement searchResults = driver.findElement(By.id("search-results"));

		// Assert that the search results are greater than 0
		assertTrue(searchResults.getText().length() > 0);

		// Get all links on the page
		List<WebElement> links = driver.findElements(By.tagName("a"));

		for (WebElement link : links) {
			String href = link.getAttribute("href");
			
			data.add(new Object[] { href });

		}

		return data;
	}

	@Test
	public void testLink() {
		 ExecutorService executor = Executors.newSingleThreadExecutor();
	        executor.submit(() -> {
	        	try {
	    			URL link = new URL(validUrl);
	    			HttpURLConnection httpConn = (HttpURLConnection) link.openConnection();
	    			httpConn.setConnectTimeout(2000);
	    			httpConn.connect();
	    			System.out.println(validUrl);
	    			// Assert that the link returns a 200 OK status
	    			assertEquals("Failed " + validUrl + " with code: " + httpConn.getResponseCode(), httpConn.getResponseCode(),
	    					200);
	    		} catch (Exception e) {
	    			System.out.println(e.getMessage());
	    		}
	        });
	        executor.shutdown();		
	}

}
