package citation;


import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


public class excel {
	private int cp, begin, cumulative_cell, cumulative_count, FoR_size;
	private List<String> ISSN_List;
	private List<Integer> checkpoint;
	private List<String> issn;
	private String issn_cell,FoR,id,id2, webpage;
    private Sheet sheet;
    private Workbook wb;
	File file;

	public excel() throws Exception{
		ISSN_List = new ArrayList<>();
		checkpoint = new ArrayList<>();
		issn = new ArrayList<>();
		cp = 0; begin = 0;
		//Open the File
		file = new File("C:\\Users\\30049683\\eclipse-workspace\\Automate Scopus Citation\\selenium2.xlsx");

	
	}
	
	
	public void run(){

		//if File exists then read Excel else print error
		if (file.isFile() && file.exists()) {
				try {readExcel(file);} catch (Exception e) {e.printStackTrace();}}
        	else { excel_file_not_found();}	
	}
	public void readExcel(File file) throws Exception {
	//Scans Through the excel sheet grabbing values
		iterate_sheet();		
		//ArrayList with 10k articles per index
		split_checkpoint();
		
	}
		
	//Scan through the excel sheet and grab the cell values
	public void iterate_sheet () throws Exception {
		wb = WorkbookFactory.create(new FileInputStream(file));
		
		if(webpage.equals("scopus"))cumulative_count = 10000;
		if(webpage.equals("webscience"))cumulative_count = 8000;

		sheet = wb.getSheetAt(0);
	    FoR = wb.getSheetAt(0).getRow(6).getCell(6).toString();

		 for (int i = 6;i < sheet.getLastRowNum()+1; i++) {

		    	//Grabs 2 ID's Current and Previous
		    	id = wb.getSheetAt(0).getRow(i).getCell(0).toString();
		    	id2 = wb.getSheetAt(0).getRow(i-1).getCell(0).toString();
		    	
		    	//Grabs Cumulative count in Excel
		    	cumulative_cell = (int) wb.getSheetAt(0).getRow(i).getCell(3).getNumericCellValue();
		    	
		    	//Grabs the ISSN numbers for Scopus

		    	if(webpage.equals("scopus"))	issn_cell = wb.getSheetAt(0).getRow(i).getCell(8).getStringCellValue();

		    	//Grabs the ISSN numbers for WebOfScience
		    	if(webpage.equals("webscience"))	issn_cell = wb.getSheetAt(0).getRow(i).getCell(9).getStringCellValue();
		    	
		  
		    	//Store all the ISSN in an Array List
		    	ISSN_List.add(issn_cell);	    	
		    	
		    	//if the cumulative count is 10k for scopus or 5k for webOfScince and id does not repeat 
		    	//Then set a checkpoint in an ArrayList and checks for the next 10k or 5k for webOfScince
		    	if(webpage.equals("scopus") && check_repeating_id()) {
		    		cumulative_count = cumulative_count + 10000;
	    			checkpoint.add(cp);
		    	}else if(webpage.equals("webscience") && check_repeating_id()) {
		    		cumulative_count = cumulative_count + 8000;
	    			checkpoint.add(cp);
		    	}
		    	
		    	
		    	//Cp adds checkpoints to help split the ISSN
		    	cp++;
		    }
		    //Final Checkpoint at the end
		 checkpoint.add(cp);
		 wb.close();
	}
	
	
	//Stores every 10k articles in an ArrayList
	private void split_checkpoint() {
		for(int i = 0; i<checkpoint.size(); i++) {	
			List<String> temp = ISSN_List.subList(begin, checkpoint.get(i));
			String temp_string = temp.toString();
			temp_string = temp_string.substring(1, temp_string.length()-1);
			begin = checkpoint.get(i);
			temp_string = temp_string.substring(3);
			issn.add(" ("+temp_string+")");
		}

	}


	//Displays a warning if Excel file is not found
	public void excel_file_not_found() {
		 JOptionPane optionPane = new JOptionPane("'selenium.xlsx' File is missing! Exiting.. Please try again...", JOptionPane.ERROR_MESSAGE);
		 JDialog dialog = optionPane.createDialog("Error");	 	dialog.setAlwaysOnTop(true);	dialog.setVisible(true);
		 System.exit(0);
	}
	
	//Sets a flag to true if cumulative cell is greater than 10k articles
	private boolean check_repeating_id() {
		if(cumulative_cell >= cumulative_count) 
			if(!id.equals(id2)) return true; else return false;
    	 else return false;
	}
	
	//return values of final ArrayList with 10k articles per index
	public ArrayList<String> get_issn(){return (ArrayList<String>) (issn);}
	
	//Returns Checkpoint
	int getCp() {return checkpoint.size();}
	int getFoRSize() {return FoR_size;}
	public void setWebPage(String webpage) {this.webpage = webpage;}
	public String getFoR() {return FoR;}
	
}