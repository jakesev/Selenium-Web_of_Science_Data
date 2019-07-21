package citation;


import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


//java -jar auto.jar

public class web_of_science {
	private String citation, articles;
	private String  file_name ,FoR, path;
	private String saved_issn, advanced_tab,refined_search;
	private int size, total_citation, total_articles, temp_a, temp_c;
	private String  year;
	private List<String> issn;
	WebDriverWait wait;
	WebElement user_n,pass,login;
	WebDriver driver;
	
	public web_of_science() {
        advanced_tab = "https://apps.webofknowledge.com/WOS_AdvancedSearch_input.do?SID=D3eFoQ4Rn2tatG3qcsu&product=WOS&search_mode=AdvancedSearch";
        path = "C:\\Users\\30049683\\eclipse-workspace\\Citation Grabber\\Citations\\Web Of Science";
		System.setProperty("webdriver.chrome.driver","C:\\chrome\\chromedriver.exe");
        issn = new ArrayList<>(); 
        total_articles = 0;
        total_citation = 0;
        
	}

	
	public void automate_scopus() throws Exception {
		//Filters the search by Article type: Journal Articles, Reviews and Year
		refine_search();
		
		//Creates a new directory if doesn't exist
    	create_folder();
		
	
		
		for(int i = 0; i<size;i++) {
			//Create a new Web driver if its first time opening
			if(i == 0) driver = new ChromeDriver();
	        
	       
	        
	        //Grabs ISSN from ArrayList and stores into a string per iteration
			store_issn(i);
	        
			//loads web of science advanced page and checks if page connected
	        driver.get(advanced_tab);
	        checkWebPage(advanced_tab);
	        
	    
	        
	        //Allows a function like CTR+C; Copies all the ISSN to a ClipBoard
	        setClipboardContents(saved_issn);
	        
	        //Waits for the web page to load, if not found display error and exit
	        //If page is found selects the Search box and pasts the ISSN numbers
	        wait = new WebDriverWait(driver, 5);
	        try {
	        	
	 	        
                wait.until(ExpectedConditions.elementToBeClickable(By.id("value(input1)")));
                driver.findElement(By.id("value(input1)")).clear();
                driver.findElement(By.id("value(input1)")).sendKeys(Keys.CONTROL +"v");               
	        }           
	        catch(Throwable pageNavigationError){
	        	searchbox_not_found();
	        	driver.quit();
	        }
	        
	       //Selects Article and Review type Documents
	       selectTheDropDownList(driver);
	       
	       
	       //If first time loading the Page then clear the history search
 	       if(i == 0) {
 	        	driver.findElement(By.id("selectallTop")).click();	
 	        	driver.findElement(By.id("deleteTop")).click();
 	        }
	       
 	       //Submits the Query
	       driver.findElement(By.id("search-button")).click();
	       
	    
	       
	       //Clicks the Results found in Web Of Science		
	       driver.findElement(By.id("set_"+Integer.toString(i+1)+"_div")).click();
	       
	       try {
               wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[class = 'snowplow-citation-report citation-report-summary-link']")));
    	       driver.findElement(By.cssSelector("a[class = 'snowplow-citation-report citation-report-summary-link']")).click();
	        }           
	        catch(Throwable pageNavigationError){
	        	JOptionPane optionPane = new JOptionPane("'Could not find View Citation Link...! Please try again.....", JOptionPane.ERROR_MESSAGE);
	    		JDialog dialog = optionPane.createDialog("Error");	 	dialog.setAlwaysOnTop(true);	dialog.setVisible(true);
	        	driver.quit();
	        }
	        
	       articles = driver.findElement(By.id("hitCount.top")).getText(); 		articles = articles.replace(",", "");
		   citation = driver.findElement(By.id("GRAND_TOTAL_TC2")).getText();	citation = citation.replace(",", "");
		   temp_a = Integer.parseInt(articles);			total_articles = total_articles+ temp_a;
		   temp_c =  Integer.parseInt(citation);		total_citation = total_citation+temp_c;
	
		  
		}
		
		
		write_to_file();
		driver.quit();
	}
	
	public void write_to_file() throws FileNotFoundException, UnsupportedEncodingException 
	{
         	file_name= FoR +" "+ year + ".txt";
	       //Writes into a file the year and number of articles
	       PrintWriter writer = new PrintWriter(new FileOutputStream(path+"\\"+file_name, false));
	       writer.println("Year,"+ "articles,"+ "Citation");
	       writer.println(year+","+total_articles+","+total_citation);
	       writer.close();
		
	}
	public static void selectTheDropDownList(WebDriver driver)
	{
		Select dd1 = new Select(driver.findElement(By.id("value(input3)")));
		dd1.selectByVisibleText("Article");
		dd1.selectByVisibleText("Review"); 
	}
	

	//If login Fails then exit the program
	public void check_incorrect_credentials(String webPage) {
		 try {
	        	Assert.assertEquals(webPage, driver.getCurrentUrl());
	        	System.out.println("Correct Web Page");	        	
	        }catch(Throwable pageNavigationError) {
	        	driver.quit();
	        	try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
	    		JOptionPane optionPane = new JOptionPane("Login page Failed! Program Terminated.....  ", JOptionPane.ERROR_MESSAGE);
	    		JDialog dialog = optionPane.createDialog("Error");		dialog.setAlwaysOnTop(true);	dialog.setVisible(true);
	        	System.exit(0);
	        }
	}
	
	
	//Sets the filter on the Search
	public void refine_search() {refined_search = " AND PY = "+ year;}
	
	//Displays a warning if search box not found 
	public void searchbox_not_found() {
		JOptionPane optionPane = new JOptionPane("'Could not find the Search Box to input ISSN...! Please try again.....", JOptionPane.ERROR_MESSAGE);
		JDialog dialog = optionPane.createDialog("Error");	 	dialog.setAlwaysOnTop(true);	dialog.setVisible(true);
		driver.quit();		System.exit(0);
	}
	
	//checks if on correct web page, if not exits and warns user
	public void checkWebPage(String webPage) {
		 try {
	        	Assert.assertEquals(webPage, driver.getCurrentUrl());
	        }catch(Throwable pageNavigationError) {
	        	JOptionPane optionPane = new JOptionPane("'WebPage not Found...! Please try again.....", JOptionPane.ERROR_MESSAGE);
	    		JDialog dialog = optionPane.createDialog("Error");	 	dialog.setAlwaysOnTop(true);	dialog.setVisible(true);
	        	driver.quit();		System.exit(0);
	        }
	}
	   //Allows a CTRL + C feature
    public static void setClipboardContents(String text) {
          StringSelection stringSelection = new StringSelection( text );
          Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
          clipboard.setContents(stringSelection, null);
        }
	
    //Takes the ISSN from ArrayList and stores into string every iteration
    //These ISSN will be added to the Search box limited to every 10k articles
    public void store_issn(int i) {
    	saved_issn = issn.get(i).toString();
		saved_issn = saved_issn.substring(1, saved_issn.length());
		saved_issn = saved_issn+" "+ refined_search;
    }
    
    public void create_folder() throws IOException {
    	String folderName = "C:\\Users\\30049683\\eclipse-workspace\\Citation Grabber\\Citations\\Web Of Science";

        Path path = Paths.get(folderName);

        if (!Files.exists(path)) {
            
            Files.createDirectory(path);
            System.out.println("Directory created");
        } else {
            
            System.out.println("Directory already exists");
        }
    }
    
    public void set_year(String year) {this.year = year;}			//sets year
	public void set_issn(List<String> issn){this.issn= issn;}		//Where all the ISSN are stored
	public void set_size(int size) {this.size=size;}				//sets index size of ISSN 
	public void set_FoR(String FoR) {this.FoR=FoR;}					//Sets the for code
}