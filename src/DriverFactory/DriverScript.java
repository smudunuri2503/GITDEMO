package DriverFactory;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import CommonFunLibray.FunctionLibray;
import Utilities.ExcelFileUtil;

public class DriverScript {
	ExtentReports report;
	ExtentTest test;
public static WebDriver driver;
String inputpath="E:\\OjtAutomation_Framework\\ERP_StockAccounting\\TestInput\\HybridTest.xlsx";
String outputpath ="E:\\OjtAutomation_Framework\\ERP_StockAccounting\\TestOutPut\\HybridResults.xlsx";
public void startTest()throws Throwable
{
	//creating object for accessing excel util
	ExcelFileUtil xl = new ExcelFileUtil(inputpath);
	//iterate all rows in Mastertestcases sheet
	for(int i=1;i<=xl.rowCount("MasterTestCases");i++)
	{
		String moduleStatus="";
		if(xl.getCellData("MasterTestCases", i, 2).equalsIgnoreCase("Y"))
		{
			//Define Module Name
			String TCModule=xl.getCellData("MasterTestCases", i, 1);
			report= new ExtentReports("./Reports/"+TCModule+"  "+FunctionLibray.generateDate()+".html");
			//iterate all in TCModule sheet
			for(int j=1;j<=xl.rowCount(TCModule);j++)
			{
				test=report.startTest(TCModule);
			//read all cell
				String Description =xl.getCellData(TCModule, j, 0);
				String Function_Name =xl.getCellData(TCModule, j, 1);
				String Locator_Type =xl.getCellData(TCModule, j, 2);
				String Locator_Value = xl.getCellData(TCModule, j, 3);
				String Test_Data = xl.getCellData(TCModule, j, 4);
				//calling methods
				try
				{
				if(Function_Name.equalsIgnoreCase("startBrowser"))
				{
					driver =FunctionLibray.startBrowser();
					test.log(LogStatus.INFO, Description);
				}
				else if(Function_Name.equalsIgnoreCase("openApplication"))
				{
					FunctionLibray.openApplication(driver);
					test.log(LogStatus.INFO, Description);
				}
				else if(Function_Name.equalsIgnoreCase("waitForElement"))
				{
					FunctionLibray.waitForElement(driver, Locator_Type, Locator_Value, Test_Data);
					test.log(LogStatus.INFO, Description);
				}
				else if(Function_Name.equalsIgnoreCase("typeAction"))
				{
					FunctionLibray.typeAction(driver, Locator_Type, Locator_Value, Test_Data);
					test.log(LogStatus.INFO, Description);
				}
				else if(Function_Name.equalsIgnoreCase("clickAction"))
				{
					FunctionLibray.clickAction(driver, Locator_Type, Locator_Value);
					test.log(LogStatus.INFO, Description);
				}
				else if(Function_Name.equalsIgnoreCase("validateTitle"))
				{
					FunctionLibray.validateTitle(driver, Test_Data);
					test.log(LogStatus.INFO, Description);
				}
				else if(Function_Name.equalsIgnoreCase("closeBrowser"))
				{
					FunctionLibray.closeBrowser(driver);
					test.log(LogStatus.INFO, Description);
				}
				else if(Function_Name.equalsIgnoreCase("captureData"))
				{
					FunctionLibray.captureData(driver, Locator_Type, Locator_Value);
					test.log(LogStatus.INFO, Description);
				}
				else if(Function_Name.equalsIgnoreCase("supTableValidation"))
				{
					FunctionLibray.supTableValidation(driver, Test_Data);
					test.log(LogStatus.INFO, Description);
				}
				else if(Function_Name.equalsIgnoreCase("stockCategories"))
				{
					FunctionLibray.stockCategories(driver);
					test.log(LogStatus.INFO, Description);
				}
				else if(Function_Name.equalsIgnoreCase("sttableValidation"))
				{
					FunctionLibray.sttableValidation(driver, Test_Data);
					test.log(LogStatus.INFO, Description);
				}
				//write as pass into status cell in TCModule
				xl.setCellData(TCModule, j, 5, "Pass", outputpath);
				test.log(LogStatus.PASS, Description);
				moduleStatus="true";
				}catch(Exception e)
				{
					System.out.println(e.getMessage());
				//write as fail into status cell in TCModule
				xl.setCellData(TCModule, j, 5, "Fail", outputpath);
				test.log(LogStatus.FAIL, Description);
				moduleStatus="false";
				}
				if(moduleStatus.equalsIgnoreCase("True"))
				{
					xl.setCellData("MasterTestCases", i, 3, "Pass", outputpath);
				}
				else
				{
					xl.setCellData("MasterTestCases", i, 3, "Fail", outputpath);
				}
				report.endTest(test);
				report.flush();
			}
		}
		else
		{
			//write as Not Executed in mastertestcases sheet 
			xl.setCellData("MasterTestCases", i, 3, "Not Executed", outputpath);
		}
		
}
}
}