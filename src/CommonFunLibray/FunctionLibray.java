package CommonFunLibray;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import Utilities.PropertyFileUtil;
import freemarker.template.SimpleDate;

public class FunctionLibray {
	public static WebDriver driver;
//method start browser
public static WebDriver startBrowser()throws Throwable
{
	if(PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("chrome"))
	{
		System.setProperty("webdriver.chrome.driver", "./CommonDrivers/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
	}
	else if(PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("firefox"))
	{
		System.setProperty("webdriver.gecko.driver", "./CommonDrivers/geckodriver.exe");
		driver = new FirefoxDriver();
	}
	else
	{
		Reporter.log("Browser value is Not Matching",true);
	}
	return driver;
}
//method for login
public static void openApplication(WebDriver driver)throws Throwable
{
	driver.get(PropertyFileUtil.getValueForKey("Url"));
}
//method for wait for element 
public static void waitForElement(WebDriver driver,String locatorType,
		String locatorvalue,String waititme)
{
	WebDriverWait myWait = new WebDriverWait(driver, Integer.parseInt(waititme));
	if(locatorType.equalsIgnoreCase("name"))
	{
		myWait.until(ExpectedConditions.visibilityOfElementLocated(By.name(locatorvalue)));
	}
	else if(locatorType.equalsIgnoreCase("xpath"))
	{
		myWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatorvalue)));
	}
	else if(locatorType.equalsIgnoreCase("id"))
	{
		myWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locatorvalue)));
	}
}
//method for type Actions
public static void typeAction(WebDriver driver,String locatortype,String locatorvalue,String testdata)
{
	if(locatortype.equalsIgnoreCase("name"))
	{
		driver.findElement(By.name(locatorvalue)).clear();
		driver.findElement(By.name(locatorvalue)).sendKeys(testdata);
	}
	else if(locatortype.equalsIgnoreCase("xpath"))
	{
		driver.findElement(By.xpath(locatorvalue)).clear();
		driver.findElement(By.xpath(locatorvalue)).sendKeys(testdata);
	}
	else if(locatortype.equalsIgnoreCase("id"))
	{
		driver.findElement(By.id(locatorvalue)).clear();
		driver.findElement(By.id(locatorvalue)).sendKeys(testdata);
	}
}
//method for click action
public static void clickAction(WebDriver driver,String locatortype,String locatorvalue)
{
	if(locatortype.equalsIgnoreCase("id"))
	{
		driver.findElement(By.id(locatorvalue)).sendKeys(Keys.ENTER);
	}
	else if(locatortype.equalsIgnoreCase("name"))
	{
		driver.findElement(By.name(locatorvalue)).click();
	}
	else if(locatortype.equalsIgnoreCase("xpath"))
	{
		driver.findElement(By.xpath(locatorvalue)).click();
	}
}
//method for validate title
public static void validateTitle(WebDriver driver,String Exp_title)
{
	String Act_title=driver.getTitle();
	try {
	Assert.assertEquals(Act_title, Exp_title,"Title is not matching");
	}catch(Throwable t)
	{
		System.out.println(t.getMessage());
	}
}
//method for close browser
public static void closeBrowser(WebDriver driver)
{
	driver.close();
}
//method for capturing supplier number into notepad
public static void captureData(WebDriver driver,String locatortype,String locatorvalue)
throws Throwable{
	String supplierNum="";
	if(locatortype.equalsIgnoreCase("id"))
	{
		supplierNum=driver.findElement(By.id(locatorvalue)).getAttribute("value");
	}
	else if(locatortype.equalsIgnoreCase("name"))
	{
		supplierNum=driver.findElement(By.name(locatorvalue)).getAttribute("value");
	}
	//Create text file and write into it
	FileWriter fw = new FileWriter("./CaptureData/supnumber.txt");
	BufferedWriter bw = new BufferedWriter(fw);
	bw.write(supplierNum);
	bw.flush();
	bw.close();
}
//table validation for supplier
public static void supTableValidation(WebDriver driver,String column)throws Throwable
{
	//read data from note pad
	FileReader fr = new FileReader("./CaptureData/supnumber.txt");
	BufferedReader br = new BufferedReader(fr);
	String Exp_supnumber=br.readLine();
	System.out.println(Exp_supnumber);
	// String to int
	int coluNum =Integer.parseInt(column);
	//if search textbox is not displayed then click search panel
	if(!driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).isDisplayed())
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("serach-panel"))).click();
	Thread.sleep(5000);
WebElement searchtextbox =driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox")));
searchtextbox.clear();
searchtextbox.sendKeys(Exp_supnumber);
driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-button"))).click();
Thread.sleep(5000);	
WebElement table =driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("Webtable.path")));
//count no of rows in a table
List<WebElement> rows =table.findElements(By.tagName("tr"));
for(int i=1;i<rows.size(); i++)
{
//capture supplier number
	String ActualSnumber=driver.findElement(By.xpath("//table[@id='tbl_a_supplierslist']/tbody/tr["+i+"]/td["+coluNum+"]/div/span/span")).getText();
Thread.sleep(5000);
Assert.assertEquals(ActualSnumber, Exp_supnumber);
System.out.println(ActualSnumber+"           "+Exp_supnumber);
break;
}
}
//method for mouse click
public static void stockCategories(WebDriver driver)throws Throwable
{
	Actions ac = new Actions(driver);
	ac.moveToElement(driver.findElement(By.linkText("Stock Items"))).perform();
	Thread.sleep(5000);
	ac.moveToElement(driver.findElement(By.xpath("(//a[text()='Stock Categories'])[2]"))).click().perform();
}
public static void sttableValidation(WebDriver driver,String testdata)throws Throwable
{
	//if search textbox is not displayed then click search panel
		if(!driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).isDisplayed())
			driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("serach-panel"))).click();
		Thread.sleep(5000);
	WebElement searchtextbox =driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox")));
	searchtextbox.clear();
	searchtextbox.sendKeys(testdata);
	driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-button"))).click();
	Thread.sleep(5000);	
	WebElement table =driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("Webtable.path1")));
	List<WebElement> rows =table.findElements(By.tagName("tr"));
	for(int i=1;i<rows.size();i++)
	{
	String actualdata=driver.findElement(By.xpath("//table[@id='tbl_a_stock_categorieslist']/tbody/tr[1]/td[4]/div/span/span")).getText();
	Thread.sleep(5000);
	Reporter.log(actualdata+"     "+testdata,true);
	Assert.assertEquals(actualdata, testdata);
	break;
	}
}
//method for date generate
public static String generateDate()
{
	Date date = new Date();
	DateFormat df = new SimpleDateFormat("YYYY_MM_dd hh_mm_ss");
	return df.format(date);
}
}










