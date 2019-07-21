package citation;



import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;



public class scopus {
	private String total_citation, total_articles;
	private String email_notification, file_name, FoR;
	private String saved_issn, url, advanced_tab, default_tab,refined_search;
	private int size;
	private String s_email, s_pass, year;
	private List<String> issn;
	WebDriverWait wait;
	WebElement user_n,pass,login;
	WebDriver driver;
	
	public scopus() {
		url ="https://www.scopus.com/customer/authenticate/loginfull.uri";
        advanced_tab = "https://www.scopus.com/search/form.uri?display=advanced&origin=searchbasic&txGid=526775849ea4aa06b2c4852684f5e249"; 
        default_tab = "https://www.scopus.com/search/form.uri?display=basic";
        issn = new ArrayList<>(); 
	}

	public void automate_scopus() throws Exception {
		refined_search = " AND  ( LIMIT-TO ( PUBYEAR ,  "+ year+" ) )  AND  ( LIMIT-TO ( DOCTYPE ,  \"ar\" )  OR  LIMIT-TO ( DOCTYPE ,  \"re\" )";
		for(int i = 0; i<1;i++) {
			
			saved_issn = issn.get(i).toString();
			saved_issn = saved_issn.substring(1, saved_issn.length());
			saved_issn = saved_issn+" "+ refined_search;
			System.setProperty("webdriver.chrome.driver","chromedriver");
	        driver = new ChromeDriver();
	        driver.get(url);
	        
	        try {
	        	Assert.assertEquals(url, driver.getCurrentUrl());
	        	System.out.println("Correct Web Page");	        	
	        }catch(Throwable pageNavigationError) {
	        	System.out.println("Incorrect Web Page");
	        }
	        
	        //Inputs UserName and password
	        user_n = driver.findElement(By.id("username"));
	        user_n.sendKeys(s_email);
	        pass = driver.findElement(By.id("password-input-password"));
	        pass.sendKeys(s_pass);
	        login = driver.findElement(By.id("login_submit_btn"));
	        login.submit();
	        
	        //checks if user goes through the login, if fails program exits
	        incorrect_credentials(default_tab);
	       
	        
	        //Once logged in Moves into the advanced tab
	        driver.get(advanced_tab);
	        checkWebPage(advanced_tab);

	        setClipboardContents(saved_issn);

			System.out.println(saved_issn);

	        
	        wait = new WebDriverWait(driver, 5);

	        try {
                wait.until(ExpectedConditions.elementToBeClickable(By.id("searchfield")));
                driver.findElement(By.id("searchfield")).sendKeys(Keys.CONTROL +"v");
                driver.findElement(By.id("searchfield")).sendKeys(Keys.RETURN);
	        }           
	        catch(Throwable pageNavigationError){
	        	System.out.println("Could not find the Search Field");
	        	driver.quit();
	        }

	        try{
                wait.until(ExpectedConditions.elementToBeClickable(By.id("closeBigCTODialog")));
                driver.findElement(By.id("emailCTO")).sendKeys(Keys.CONTROL +"v");
                //Waits for email to be sent properly
                Thread.sleep(1000);
                driver.findElement(By.id("emailCTO")).sendKeys(Keys.RETURN);
                total_citation = "unkown";
                file_name= FoR +" "+ year+" "+ total_articles +".txt";
                //Grabs the total number of articles
    	        total_articles = driver.findElement(By.className("resultsCount")).getText();

            }
            catch(Throwable pageNavigationError){
              //Grab FOR, ALL Publications, Citations and save it in.....
    	        total_articles = driver.findElement(By.className("resultsCount")).getText();
                total_citation = driver.findElement(By.id("grandtotal")).getText();
                file_name= FoR +" "+ year+" "+ total_articles +" "+email_notification+".txt";
            }
       
        PrintWriter writer = new PrintWriter(file_name, "UTF-8");
        writer.println("Year, "+ "No of articles,"+ " total_citations ");
        writer.println(year+", "+total_articles+", "+total_citation);
        writer.close();
		}
	}
	
	
	//If login Fails then exit the program
	public void incorrect_credentials(String webPage) {
		 try {
	        	Assert.assertEquals(webPage, driver.getCurrentUrl());
	        	System.out.println("Correct Web Page");	        	
	        }catch(Throwable pageNavigationError) {
	        	System.out.println("Incorrect Username/Password, Please try again");
	        	driver.quit();
	        	System.exit(0);
	        }
	}
	
	
	//checks if on correct web page, if not exits and warns user
	public void checkWebPage(String webPage) {
		 try {
	        	Assert.assertEquals(webPage, driver.getCurrentUrl());
	        }catch(Throwable pageNavigationError) {
	        	System.out.println("Web page was not reachable, please try again");
	        	driver.quit();
	        	System.exit(0);
	        }
	}
	   
    public static void setClipboardContents(String text) {
          StringSelection stringSelection = new StringSelection( text );
          Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
          clipboard.setContents(stringSelection, null);
        }
	
    public void set_year(String year) {this.year = year;}
	public void set_email(String s_email) {this.s_email = s_email;}
	public void set_pass(String s_pass) {this.s_pass = s_pass;}
	public void set_issn(List<String> issn){this.issn= issn;}
	public void set_size(int size) {this.size=size;}
	public void set_FoR(String FoR) {this.FoR=FoR;}
	
}