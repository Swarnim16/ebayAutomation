package test.java;

import org.openqa.selenium.By;

/**
 * <h1>Ebay Home Page</h1>
 * Class EbayHomePage is repository of elements of Ebay home page.  
 * It is to store the elements of 
 * home page for re-usability purpose.
 * <p>
 * <b>Note:</b> Page object modeling example.
 */

public class EbayHomePage {		
	public By searchInput = By.id("gh-ac");
	public By searchButton= By.id("gh-btn");
    public By groupsimilarlistingsButton = By.xpath("//*[@id=\"srp-river-results-SEARCH_STATUS_MODEL_V2-w0-w3\"]/a/div[1]/span[2]");
}
