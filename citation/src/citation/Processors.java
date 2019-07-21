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


public class Processors{
	gui ui;
	String year, s_email, s_pass, webpage, email;
	
	//First Thread to run and wait
	public void produce() throws InterruptedException {
		//Runs First
		synchronized(this) {
			ui = new gui();
			ui.open_app();
			wait(); //locks and waits
			System.out.println("Resumed...");
			
			year = ui.get_y();		s_email = ui.get_se();
			s_pass = ui.get_sp();	webpage = ui.get_wp(); 
			email = ui.get_e(); 
		}
	}
	
	//Second Thread Listens
	public void consume() throws InterruptedException{
		Thread.sleep(1000);
			synchronized(this) {
				do {
					Thread.sleep(1500);
					if(ui.getbool() == 1) notify(); //Returns back to produce 
				}while(ui.getbool() != 1); 
			}			
	}
	
	public String get_y() {return year;} //Returns year
	public String get_se() {return s_email;} //Returns Scopus Email
	public String get_sp() {return s_pass;} //Returns Scopus Pass
	public String get_wp() {return webpage;}    //Returns Web Page
	public String get_e() {	return email;}		//Returns Email
}