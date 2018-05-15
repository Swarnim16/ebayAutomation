package test.java;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

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

public class EbaySearchPageTest extends BaseSetupUtility{
private WebDriver driver;
	

	@BeforeClass
	public void setUp() {
		driver=getDriver();
	}
	
	@Test(priority = 1)
	public void verifyHomePage() {
		System.out.println("Home page test...");
		//BasePage basePage = new BasePage(driver);
		//driver.get("https://www.ebay.com/");
		driver.manage().window().maximize();
		System.out.println(driver.getTitle());
		String expectedPageTitle="eBay";
		Assert.assertTrue(driver.getTitle().contains(expectedPageTitle), "Home page title doesn't match");
	}
	
	@Test(priority=2)
	public void verifySearchFunctionTest() throws Exception
	{
		try {
		
			EbayHomePage homePageObjects = new EbayHomePage();
			 
   			// Open the Excel file
			FileInputStream ExcelFile = new FileInputStream("src/test/resources/EbaySearchData.xlsx");

			// Access the required test data sheet
			XSSFWorkbook wBook;
			XSSFSheet wSheet ;

			wBook = new XSSFWorkbook(ExcelFile);			
			wSheet = wBook.getSheetAt(0);
			
			
			int rowCount = wSheet.getLastRowNum() - wSheet.getFirstRowNum();
			
			System.out.println(rowCount +"");
			
			String searchStr;
			for(int i=0; i<= rowCount; i++)
			{
				XSSFRow row = wSheet.getRow(i);
				
				for(int j=0; j < row.getLastCellNum(); j++)
				{
					System.out.println(row.getCell(j).getStringCellValue());
					searchStr=row.getCell(j).getStringCellValue();
					if (i == 0)
					{searchStr="";}
					else					
					{
					 driver.findElement(homePageObjects.searchInput).sendKeys(searchStr);
					 driver.findElement(homePageObjects.searchButton).click();
					 driver.findElement(homePageObjects.groupsimilarlistingsButton).click();
					 
					 //....Case-3: Check that all the listed product has the word “tv” and “sony” in it.....//
					 List<WebElement> searchResultHeadingList = driver.findElements(By.xpath("//h3[@class='s-item__title']"));							
					 {
						 System.out.println("Heading : "+ searchResultHeadingList.size());
						 for(int hList=0; hList < searchResultHeadingList.size(); hList++)
						 {
							 WebElement listItemH3= searchResultHeadingList.get(hList);							 
							 String ItemHeading = listItemH3.getText();
							 System.out.println("<h3> : "+ ItemHeading);
							 							 							
							 												
							//split Search keywords string to verify result
							 String[] searchStringArray= searchStr.split("\\s");
							 int count=0;
							 for(int searchItem=0; searchItem < searchStringArray.length; searchItem++)
							 {
								 if (ItemHeading.toLowerCase().contains(searchStringArray[searchItem].toLowerCase()))
								 {
									 count++;
									 System.out.println("searchItem = "+ searchStringArray[searchItem]);
									 System.out.println("count = " + count);
								 }
								 else
								 {
									 System.out.println("searchItem = "+ searchStringArray[searchItem]);
									 System.out.println("Not available");
									 
								 }
								 System.out.println();
								 
							 }
							 if (count==searchResultHeadingList.size())
								 Assert.assertTrue(true);
							 else
								 Assert.assertTrue(false);
							 
						 
						 }
					 }
					
				
					}
					searchStr="";
				}
				
				System.out.println();
				wBook.close();
			}
			
		} 
		catch (Exception e)
		{
			throw(e);
		}
		
		

	}

	//...Case-4 : Select the screen size 50” – 60” (in red square of step 2)....	
	//dependsOnMethods = "verifySearchFunctionTest"
    @Test(priority=3)
	public void ScreenSizeChangeTest() throws Exception
	{
		 
		try
		{
		 List<WebElement> checkBox = driver.findElements(By.xpath("//input[@class='cbx x-refine__multi-select-checkbox ']"));
		 {
			 System.out.println(checkBox.size());
			 if (checkBox.size() > 0)
			 {
			 for(int k=0; k<checkBox.size(); k++)
			 {
				 WebElement Cb= checkBox.get(k);
				 String checkBoxLabel = Cb.getAttribute("aria-label");
				 
				 if ("50\" - 60\"".equalsIgnoreCase(checkBoxLabel))
				 {								 
					 Cb.click();
					break; 
				 }
				 
			 
			 }
			 }
			 else
			 {
				 WebElement element=driver.findElement(By.linkText("50\" - 60\""));
				 if (element.getText().isEmpty())
				 {
					 
				 }
				 else
				 {
					 System.out.println("Size "+element.getText());
					 element.click();
				 }
			 }
			 
		 }
		 //return 1;
		 //List<WebElement> filterList = this.driver.findElements(By.xpath("//li[@='srp-controls__control']"));
		 Assert.assertTrue(true);
		}
		catch(Exception e)
		{			
			throw(e);		
		}
	}
	
	//..Case-6 : Randomly select a product in the current listed page, check that the product detail page is showed
    @Test(priority=4)
		public void ProductSelectMethod() throws Exception
		{
			try
			{
				Random rand = new Random();
				int randomItem= rand.nextInt(10);
				System.out.println("Random = "+ randomItem);
				
				List<WebElement> searchResultHeadingList = driver.findElements(By.xpath("//h3[@class='s-item__title']"));
				
				WebElement searchItemElement = searchResultHeadingList.get(randomItem);
				searchItemElement.click();
			}
			catch(Exception e)
			{			
				throw(e);		
			}
			Assert.assertTrue(true);
		}
    
    
    //Class to store Product details
    public class ProductDetails{
		private  String productName="";
		private  String productTimeleft="";
		private  String productPrice="";
		
		
		public void setProductName(String name)
		{
			productName = name;
		}
		public void setProductTime(String timeLeft)
		{
			productTimeleft = timeLeft;
		}
		public void setProductPrice(String price)
		{
			productPrice = price;
		}
		
		public String getProductName()
		{
			return(productName);
		}
		public String getProductTime()
		{
			return(productTimeleft) ;
		}
		public String getProductPrice()
		{
			return(productPrice);
		}
	}
		

  //...Case-7 : Verify Product information:
    @Test(priority = 5)
  	public void CheckProductDetailTest() throws Exception
  	{
  		try
  		{	
  			ProductDetails productObj =new ProductDetails();
  			String condition=driver.findElement(By.id("vi-itm-cond")).getText();
  			if (condition.isEmpty())
  			{
  				System.out.println("Item condition = Fail");
  				Assert.assertTrue(false);
  			}
  			else
  			{
  				System.out.println("Item condition = Pass : "+ condition);
  				Assert.assertTrue(true);
  			}
  			
  			//String timeLeft= driver.findElement(By.id("vi-cdown_timeLeft")).getText();
  			boolean timeLeftExists=true;
  			String name= driver.findElement(By.id("itemTitle")).getText();
  			//if (driver.findElement(By.id("vi-cdown_timeLeft"))condition.ex)
  			try
  			{
  				String timeLeft= driver.findElement(By.id("vi-cdown_timeLeft")).getText();
  				System.out.println("Item time left ="+ timeLeft);
  				productObj.setProductTime(timeLeft);
  				timeLeftExists=true;
  				Assert.assertTrue(timeLeftExists);
  			}
  			catch(NoSuchElementException e)
  			{
  				timeLeftExists =false;
  				Assert.assertTrue(timeLeftExists);
  			}
  			String price= driver.findElement(By.id("prcIsum")).getText();
  			System.out.println("Item Details Name ="+ name);
  			//System.out.println("Item time left ="+ timeLeft);
  			System.out.println("Item time price ="+ price);
  			productObj.setProductName(name);
  			//productObj.setProductTime(timeLeft);
  			productObj.setProductPrice(price);
  			
  			//...Case
  			driver.findElement(By.id("atcRedesignId_btn")).click();
  			System.out.println(driver.getTitle());
  			Set<String> ids=driver.getWindowHandles();
  			Iterator<String> it=ids.iterator();
  			String parentId=it.next();
  			String childId=it.next();
  			driver.switchTo().window(childId);
  			System.out.println(driver.getTitle());
  		
  		}
  		catch(Exception e)
  		{			
  			throw(e);		
  		}
  		
  	}
}
