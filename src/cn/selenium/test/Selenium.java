package cn.selenium.test;

import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@RunWith(Parameterized.class)
public class Selenium {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  private String name, pwd, url;
  public Selenium(String name,String pwd,String url){
		this.name = name;
		this.pwd = pwd;
		this.url = url;
  }

  @Before
  public void setUp() throws Exception {
	System.setProperty("webdriver.firefox.bin", "C:/Program Files (x86)/Mozilla Firefox/firefox.exe"); 
    driver = new FirefoxDriver();
    baseUrl = "http://121.193.130.195:8080";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  //读取csv文件数据,记录下学号密码以及url;
  @Parameters
  public static Collection<Object[]> getData(){
	  Object[][] test = new Object[117][3];
	  File fi = new File("E:/inputgit.csv");
	  String csvSplitBy = ",";  
	  int add = 0;
	  try{
		  BufferedReader br = new BufferedReader(new FileReader(fi));
		  String line = br.readLine(); 
		  while ((line = br.readLine()) != null) { 
		 	  String [] temp = line.split(csvSplitBy);
			  test[add][0] = temp[0];
			  test[add][1] = temp[0].substring(4,10);
			  test[add][2] = temp[2];
			  add++;
          } 
          br.close();
	  } catch(FileNotFoundException e){ 
		  e.printStackTrace(); 
	  } catch(IOException e){
          e.printStackTrace();
      }
	  
	  return Arrays.asList(test);
	  
  }
  
  @Test
  public void testSelenium() throws Exception {
    driver.get(baseUrl + "/");
    driver.findElement(By.id("name")).clear();
    driver.findElement(By.id("name")).sendKeys(name);
    driver.findElement(By.id("pwd")).clear();
    driver.findElement(By.id("pwd")).sendKeys(pwd);
    driver.findElement(By.id("submit")).click();
    String infoAll = driver.findElement(By.xpath("//tbody[@id='table-main']")).getText();
    String infoAddr = infoAll.substring(infoAll.indexOf("http"));
    assertEquals(url, infoAddr);
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
