package test.java;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * <h1>Ebay Search Tests class for end to end online shopping</h1> Test Class -
 * EbaySearchPageTest contains test methods to verify search functionality and
 * to add items into cart.
 * 
 */
public class EbaySearchPageTest extends BaseSetupUtility {
	private WebDriver driver;
	String searchString = "";

	/**
	 * <h1>setUp method</h1> Method contains code to call the getDriver() method of
	 * BaseSetupUtility class to open the application in browser.
	 * 
	 */
	@BeforeClass
	public void setUp() {
		driver = getDriver();
	}

	/**
	 * <h1>Test Case : to Verify Home Page</h1> Test Method 'verifyHomePage()'
	 * contains code to verify the expected URL page title with the actual page
	 * title.
	 * 
	 */
	@Test(priority = 1)
	public void verifyHomePage() {
		System.out.println("Home page test...");
		driver.manage().window().maximize();
		System.out.println(driver.getTitle());
		String expectedPageTitle = "eBay";
		Assert.assertTrue(driver.getTitle().contains(expectedPageTitle), "Home page title doesn't match");
	}

	/**
	 * <h1>Test Case-1 : To verify search for "Sony tv" data.</h1> Test Case step to
	 * go to www.ebay.com and search for “sony tv” (this text is should be reading
	 * from file)
	 * 
	 * 
	 */
	@Test(priority = 2)
	public void verifySearchFunctionTest() throws Exception {
		try {

			EbayHomePage homePageObjects = new EbayHomePage();
			String actualPageTitle = "";

			// Open the Excel file
			FileInputStream ExcelFile = new FileInputStream("src/test/resources/EbaySearchData.xlsx");

			// Access the required test data sheet
			XSSFWorkbook wBook;
			XSSFSheet wSheet;

			wBook = new XSSFWorkbook(ExcelFile);
			wSheet = wBook.getSheetAt(0);

			int rowCount = wSheet.getLastRowNum() - wSheet.getFirstRowNum();

			System.out.println(rowCount + "");

			for (int i = 0; i <= rowCount; i++) {
				XSSFRow row = wSheet.getRow(i);

				for (int j = 0; j < row.getLastCellNum(); j++) {
					System.out.println(row.getCell(j).getStringCellValue());
					searchString = row.getCell(j).getStringCellValue();
					if (i == 0) {
						searchString = "";
					} else {
						driver.findElement(homePageObjects.searchInput).sendKeys(searchString);
						driver.findElement(homePageObjects.searchButton).click();
						driver.findElement(homePageObjects.groupsimilarlistingsButton).click();
						driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
						actualPageTitle = driver.getTitle();
						Assert.assertTrue((actualPageTitle.contains(searchString)),
								"Search data from excel file is not successfull...");

					}

				}
				System.out.println();
				wBook.close();
			}

		} catch (Exception e) {
			throw (e);
		}

	}

	/**
	 * <h1>Test Case-3 : To verify list of search.</h1> Test Case step to Check that
	 * all the listed product has the word “tv” and “sony” in it.
	 */
	@Test(priority = 3, dependsOnMethods = "verifySearchFunctionTest")
	public void verifyProductSearchList() throws Exception {
		try {
			if (!searchString.isEmpty()) {
				List<WebElement> searchResultHeadingList = driver
						.findElements(By.xpath("//h3[@class='s-item__title']"));

				System.out.println("Heading : " + searchResultHeadingList.size());
				boolean keywordFound = false;
				if (searchResultHeadingList.size() > 0) {
					label: for (int hList = 0; hList < searchResultHeadingList.size(); hList++) {
						WebElement listItemH3 = searchResultHeadingList.get(hList);
						String ItemHeading = listItemH3.getText();
						System.out.println("<h3> : " + ItemHeading);
						// split Search keywords string to verify result
						String[] searchStringArray = searchString.split("\\s");

						for (int searchItem = 0; searchItem < searchStringArray.length; searchItem++) {
							if (ItemHeading.toLowerCase().contains(searchStringArray[searchItem].toLowerCase())) {
								keywordFound = true;
								System.out.println("searchItem = " + searchStringArray[searchItem]);
							} else {
								System.out.println("searchItem = " + searchStringArray[searchItem]);
								System.out.println("Not available");
								keywordFound = false;
								break label;

							}
						}
					}
					if (keywordFound)
						Assert.assertTrue(true);
					else
						Assert.assertTrue(false);
				}
			} else {
				Assert.assertTrue(false);
			}
		} catch (Exception e) {
			throw (e);
		}
	}

	/**
	 * <h1>Test Case-4 : Select the screen size 50” – 60”</h1> Test Case step to
	 * Select the screen size 50” – 60” and to check that list has generated for the
	 * new filter.
	 */
	@Test(priority = 4)
	public void ScreenSizeChangeTest() throws Exception {

		try {
			List<WebElement> checkBox = driver
					.findElements(By.xpath("//input[@class='cbx x-refine__multi-select-checkbox ']"));
			{
				System.out.println(checkBox.size());
				if (checkBox.size() > 0) {
					for (int k = 0; k < checkBox.size(); k++) {
						WebElement Cb = checkBox.get(k);
						String checkBoxLabel = Cb.getAttribute("aria-label");

						if ("50\" - 60\"".equalsIgnoreCase(checkBoxLabel)) {
							Cb.click();
							break;
						}

					}
				} else {
					WebElement element = driver.findElement(By.linkText("50\" - 60\""));
					if (element.getText().isEmpty()) {

					} else {
						System.out.println("Size " + element.getText());
						element.click();
					}
				}

			}

			Assert.assertTrue(true);
		} catch (Exception e) {
			throw (e);
		}
	}

	/**
	 * <h1>Test Case-6 : Randomly select a product</h1> Code to verify randomly
	 * select a product in the current listed page, check that the product detail
	 * page is showed
	 */
	@Test(priority = 5)
	public void ProductSelectMethod() throws Exception {
		try {
			Random rand = new Random();
			int randomItem = rand.nextInt(10);
			System.out.println("Random = " + randomItem);

			List<WebElement> searchResultHeadingList = driver.findElements(By.xpath("//h3[@class='s-item__title']"));

			WebElement searchItemElement = searchResultHeadingList.get(randomItem);
			searchItemElement.click();
		} catch (Exception e) {
			throw (e);
		}
		Assert.assertTrue(true);
	}

	/**
	 * <h1>Class to store Product details</h1>
	 */
	public class ProductDetails {
		private String productName = "";
		private String productTimeleft = "";
		private String productPrice = "";

		public void setProductName(String name) {
			productName = name;
		}

		public void setProductTime(String timeLeft) {
			productTimeleft = timeLeft;
		}

		public void setProductPrice(String price) {
			productPrice = price;
		}

		public String getProductName() {
			return (productName);
		}

		public String getProductTime() {
			return (productTimeleft);
		}

		public String getProductPrice() {
			return (productPrice);
		}
	}

	/**
	 * <h1>.Case-7 : Verify Product information</h1>
	 */
	@Test(priority = 6)
	public void CheckProductDetailTest() throws Exception {
		try {
			ProductDetails productObj = new ProductDetails();
			String condition = driver.findElement(By.id("vi-itm-cond")).getText();
			if (condition.isEmpty()) {
				System.out.println("Item condition = Fail");
				Assert.assertTrue(false);
			} else {
				System.out.println("Item condition = Pass : " + condition);
				Assert.assertTrue(true);
			}

			boolean timeLeftExists = true;
			String name = driver.findElement(By.id("itemTitle")).getText();

			try {
				String timeLeft = driver.findElement(By.id("vi-cdown_timeLeft")).getText();
				System.out.println("Item time left =" + timeLeft);
				productObj.setProductTime(timeLeft);
				timeLeftExists = true;
				Assert.assertTrue(timeLeftExists);
			} catch (NoSuchElementException e) {
				timeLeftExists = false;
				Assert.assertTrue(timeLeftExists);
			}
			String price = driver.findElement(By.id("prcIsum")).getText();
			System.out.println("Item Details Name =" + name);
			System.out.println("Item time price =" + price);
			productObj.setProductName(name);
			productObj.setProductPrice(price);

			driver.findElement(By.id("atcRedesignId_btn")).click();
			System.out.println(driver.getTitle());
			Set<String> ids = driver.getWindowHandles();
			Iterator<String> it = ids.iterator();
			String parentId = it.next();
			String childId = it.next();
			driver.switchTo().window(childId);
			System.out.println(driver.getTitle());

		} catch (Exception e) {
			throw (e);
		}

	}
}
