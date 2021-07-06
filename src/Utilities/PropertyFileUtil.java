package Utilities;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertyFileUtil {
public static String getValueForKey(String Key)throws Throwable
{
	Properties configprop = new Properties();
	configprop.load(new FileInputStream("E:\\OjtAutomation_Framework\\ERP_StockAccounting\\PropertyFile\\Environment.properties"));
	return configprop.getProperty(Key);
}
}
