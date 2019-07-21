package citation;


public class citation_grabber {

	public static void main(String[] args) throws Exception, InterruptedException  {		
		excel ex = new excel();
		//scopus scps = new scopus();
		Processors p = new Processors();
		web_of_science wos= new web_of_science();

		//Start first Thread when User Interface opens and Lock until form is completed
		Thread t1 = new Thread(new Runnable() {@Override public void run() {
			try {p.produce();} catch (InterruptedException ex) {ex.printStackTrace();}}
		});
		
		//Once form is completed notify thread 1 to unlock
		Thread t2 = new Thread(new Runnable() {@Override public void run() {
			try {p.consume();} catch (InterruptedException ex) {ex.printStackTrace();}}
		});
	
		//MultiThread Synchronization
		t1.start();t2.start();
		
		try {t1.join();} catch (InterruptedException e) {e.printStackTrace();}
		try {t2.join();} catch (InterruptedException e) {e.printStackTrace();}
		
		
		
		ex.setWebPage(p.get_wp());
		ex.run();
		
		if(p.get_wp().equals("scopus")){
			//Passes the Values to Scopus
			/*
			scps.set_scopus_email(p.get_se());	scps.set_pass(p.get_sp());
			scps.set_email(p.get_e());			scps.set_year(p.get_y());
			scps.set_size(ex.getCp());			scps.set_issn(ex.get_issn());
			scps.set_FoR(ex.getFoR());
			scps.automate_scopus();
			*/	
		} else 
		{
			wos.set_year(p.get_y());			wos.set_size(ex.getCp());			
			wos.set_FoR(ex.getFoR());			wos.set_issn(ex.get_issn());
			//wos.automate_scopus();
		}
	 }
}