package test.java;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.GregorianCalendar;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

//ITestListener interface
public class Listeners implements  ITestListener{
	
	private WebDriver driver;

	@Override
	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		System.out.println("Test Case Start at : "+ LocalDateTime.now());
		
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		System.out.println("Test Case PASSED : "+ result.getName());		
		
	}

	@Override
	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub
		System.out.println("Test Case FAILED : "+ result.getName());
		this.driver = ((EbaySearchPageTest)result.getInstance()).getDriver();
		try {
			takeScreenShot(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		
	}
	
	public void takeScreenShot(ITestResult result) throws IOException {
		 if (!result.isSuccess()) {
		 File imageFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String failureImageFileName = result.getMethod().getMethodName()+ new SimpleDateFormat("MM-dd-yyyy_HH-ss").format(new GregorianCalendar().getTime()) + ".jpg";
		        File failureImageFile = new File(System.getProperty("user.dir")+"\\screenshots\\"+failureImageFileName);
		 FileUtils.copyFile(imageFile, failureImageFile);
		  }
	}

}
