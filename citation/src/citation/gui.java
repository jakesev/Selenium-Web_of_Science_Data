package citation;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class gui extends JFrame {
	private static final long serialVersionUID = 1L;
	private JFrame f;
	private JPanel p;
	private JLabel jl_scopus_email, jl_scopus_pass, jl_email,
			 	jl_year, jl_webpage, jl_FoR;
	private JTextField jt_scopus_email, jt_email, jt_year;
	private JPasswordField pf_scopus_pass;
	private GridBagConstraints gbc;
	private JButton btn_ok, btn_clear;
	private JRadioButton jr_scopus, jr_webscience, jr_currentFoR, jr_allFoR;
	private ButtonGroup bg_webpage, bg_FoR;
	private String scopus_pass, scopus_email, email, webpage,approved_year;
	private char charpass[];
	int bool, year;
	
	
	gui(){
		//Initializing the Variables
		f = new JFrame("Citation Grabber");
		btn_ok = new JButton("Submit");  					btn_clear = new JButton("Clear");		
		jl_scopus_email = new JLabel("Scopus Email: "); 	jl_scopus_pass = new JLabel("Scopus Pass: ");
		jl_email = new JLabel("Send links to Email: ");		jt_email = new JTextField(18); 		
		jl_year = new JLabel("Year: ");						jt_scopus_email = new JTextField(18); 
		pf_scopus_pass = new JPasswordField(18); 			jt_year = new JTextField(10);
		jr_scopus = new JRadioButton("Scopus");				jr_webscience = new JRadioButton("Web of Science");	
		jr_currentFoR = new JRadioButton("Current FOR");	jr_allFoR = new JRadioButton("All FOR");
		jl_webpage = new JLabel("Select Web Page:");		jl_FoR = new JLabel("Select FoR code:");
		bg_webpage = new ButtonGroup();						bg_FoR = new ButtonGroup();
		gbc = new GridBagConstraints();						bool = 0;
	}
	
	//Runs the functions together
	public void open_app() {create_frame();ui();}
	
	public void create_frame() {
		f.setVisible(true);
		f.setSize(380,400);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		p = new JPanel(new GridBagLayout());
		f.setResizable(false);
		f.add(p);
		p.setBorder(BorderFactory.createTitledBorder("Enter Details"));
	}
	
	//Creates the interface
	public void ui() {
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.insets = new Insets(25,5,10,10);
		gbc.gridx = 0; gbc.gridy = 0;	p.add(jl_scopus_email,gbc); 	//label for scopus email
		gbc.gridx = 1; gbc.gridy = 0;	p.add(jt_scopus_email,gbc); 	//text box for scopus email
		
		gbc.insets = new Insets(10,5,10,10);

		gbc.gridx = 0; gbc.gridy = 1; 	p.add(jl_scopus_pass,gbc); 		//label for scopus pass
		gbc.gridx = 1; gbc.gridy = 1; 	p.add(pf_scopus_pass,gbc); 		//text box for scopus pass
		
		gbc.gridx = 0; gbc.gridy = 2; 	p.add(jl_email,gbc); 			//label for email that will be receiving items
		gbc.gridx = 1; gbc.gridy = 2; 	p.add(jt_email,gbc); 			//text box for email that will be receiving items
		
		gbc.gridx = 0; gbc.gridy = 4; 	p.add(jl_year,gbc); 			//label for year
		gbc.gridx = 1; gbc.gridy = 4; 	p.add(jt_year,gbc); 			//text box for year
		
		
		bg_webpage.add(jr_scopus);		bg_webpage.add(jr_webscience);	//Group radio buttons for web pages
		gbc.insets = new Insets(5,2,0,0);
		gbc.gridx = 0; gbc.gridy = 5; 	p.add(jl_webpage,gbc);    		//label for WebPage
		gbc.gridx = 0; gbc.gridy = 6; 	p.add(jr_scopus,gbc);     		//radio for scopus
		gbc.gridx = 0; gbc.gridy = 7; 	p.add(jr_webscience,gbc); 		//radio for WebScience
		
		/*gbc.insets = new Insets(25,85,0,0);
		bg_FoR.add(jr_allFoR);  		bg_FoR.add(jr_currentFoR);		//Group radio buttons for FoR codes
		gbc.gridx = 1; gbc.gridy = 5; 	p.add(jl_FoR,gbc);    			//label for FoR
		gbc.gridx = 1; gbc.gridy = 6; 	p.add(jr_currentFoR,gbc);     	//radio for current FoR codes
		gbc.gridx = 1; gbc.gridy = 7; 	p.add(jr_allFoR,gbc); 			//radio for for All FoR codes
		*/
		gbc.weightx = 1; gbc.weighty = 25;
		gbc.insets = new Insets(25,120,0,0);
		gbc.gridx = 1; gbc.gridy = 11; 	p.add(btn_ok,gbc); //text box for password that will be receiving items
		//gbc.gridx = 0; gbc.gridy = 5; 	p.add(btn_clear,gbc); //text box for password that will be receiving items
		
		actionListeners();
	}


	//Submits form when all the criteria are met
	public void actionListeners() {
		btn_ok.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				
				try {year = Integer.parseInt(jt_year.getText());} catch (NumberFormatException ne) {ne.printStackTrace();}
				
				 if(jt_scopus_email.getText().isEmpty()) {		error_message_scopus_email();
				 } else if(pf_scopus_pass.getText().isEmpty()) {error_message_scopus_pass();
				 } else if(jt_email.getText().isEmpty()) {		error_message_email();						
				 } else if(jt_year.getText().isEmpty()) {		error_message_year();
				 }else if(year < 1992) { 						error_message_year_too_low();
				 } else if(jr_scopus.isSelected() == false && jr_webscience.isSelected() == false) { error_message_webpage();
				 } else {
					if(jr_scopus.isSelected())  webpage = "scopus";
						else if(jr_webscience.isSelected())  webpage = "webscience";
					
					scopus_email = jt_scopus_email.getText();	charpass = pf_scopus_pass.getPassword(); 
					scopus_pass = String.valueOf(charpass); 	email = jt_email.getText();
					approved_year  = Integer.toString(year); 
					f.dispose();
					bool = 1;
				}
			}	
		});
		
		//Listens to user input for year text field
		jt_year.addKeyListener(new KeyListener() {
			//Eliminate any unwanted Characters
			@Override
			public void keyTyped(KeyEvent e) {char c = e.getKeyChar();
				if(!(Character.isDigit(c)) || c==KeyEvent.VK_BACK_SPACE||c==KeyEvent.VK_DELETE) {e.consume();}
			}

			@Override 
			public void keyReleased(KeyEvent e){
				int current_year = Calendar.getInstance().get(Calendar.YEAR);
				
				//Eliminate any unwanted Characters and entered year is less than current year
				char c = e.getKeyChar();
				if(!(Character.isDigit(c)) || c==KeyEvent.VK_BACK_SPACE||c==KeyEvent.VK_DELETE) {e.consume();}
					else {
						try{year = Integer.parseInt(jt_year.getText());}catch(NumberFormatException ne){ne.printStackTrace();}
						
						if(year > current_year) {
							JOptionPane optionPane = new JOptionPane("Year must be equal to or lower than " + current_year, JOptionPane.ERROR_MESSAGE);
							JDialog dialog = optionPane.createDialog("Error");
							dialog.setAlwaysOnTop(true);	dialog.setVisible(true);
							jt_year.setText(Integer.toString(current_year));
						}
					}
				}
			@Override public void keyPressed(KeyEvent e){}	
		});	
	}
	
	//Show error message
	public void error_message_scopus_email() {
		 JOptionPane optionPane = new JOptionPane("Need to input Scopus Email!", JOptionPane.ERROR_MESSAGE);
		 JDialog dialog = optionPane.createDialog("Error");	 	dialog.setAlwaysOnTop(true);	dialog.setVisible(true);
	}
	public void error_message_scopus_pass() {
		JOptionPane optionPane = new JOptionPane("Need to input password!", JOptionPane.ERROR_MESSAGE);
		JDialog dialog = optionPane.createDialog("Error");		dialog.setAlwaysOnTop(true);	dialog.setVisible(true);
	}
	public void error_message_email() {
		JOptionPane optionPane = new JOptionPane("Need to input email!", JOptionPane.ERROR_MESSAGE);
		JDialog dialog = optionPane.createDialog("Error");	dialog.setAlwaysOnTop(true);	dialog.setVisible(true);
	}
	public void error_message_year() {
		JOptionPane optionPane = new JOptionPane("Need to input Year!", JOptionPane.ERROR_MESSAGE);
		JDialog dialog = optionPane.createDialog("Error");		dialog.setAlwaysOnTop(true);	dialog.setVisible(true);
	}
	public void error_message_year_too_low() {
		JOptionPane optionPane = new JOptionPane("Year must be equal to or higher than " + 1992 , JOptionPane.ERROR_MESSAGE);
		JDialog dialog = optionPane.createDialog("Error");		dialog.setAlwaysOnTop(true);	dialog.setVisible(true);
		jt_year.setText("1992");
	}
	public void error_message_webpage() {
		JOptionPane optionPane = new JOptionPane("Need to select Web Page option!", JOptionPane.ERROR_MESSAGE);
		JDialog dialog = optionPane.createDialog("Error");		dialog.setAlwaysOnTop(true);	dialog.setVisible(true);
	}
	public String get_y() {return approved_year;} //Returns year
	public String get_se() {return scopus_email;} //Returns Scopus Email
	public String get_sp() {return scopus_pass;} //Returns Scopus Pass
	public String get_wp() {return webpage;}    //Returns Web Page
	public String get_e() {	return email;}		//Returns Email
	public int getbool() {return bool;};		//Returns form submission, (1 == completed)
}