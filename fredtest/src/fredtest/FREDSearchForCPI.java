package fredtest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FREDSearchForCPI {
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
	        driver.get("https://fred.stlouisfed.org/");

	        // Locate the search bar
	        WebElement searchBar = driver.findElement(By.name("st"));

	        // Enter "cpi" into the search bar
	        searchBar.sendKeys("cpi");

	        // Click the search button
	        driver.findElement(By.className("homepage-search-button")).click();

	        
	        evaluateSearchResults(driver);
	        measurePageLoad(driver);
	        // Close the browser
	        driver.quit();
	 }
	 
	 private static void evaluateSearchResults(WebDriver driver) {
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
	        assert searchResults.getText().length() == 0 : "Search results are empty";
	 }
	 
	 private static void measurePageLoad(WebDriver driver) {
		// Measure the page load time
	        long startTime = System.currentTimeMillis();
	        driver.get("https://fred.stlouisfed.org/");
	        long endTime = System.currentTimeMillis();
	        long loadTime = endTime - startTime;

	        // Assert that the page load time is within 3 seconds
	        assert loadTime < 1000 : "Page load time is greater than 3 seconds";
	 }
	    

}
