package model;

import java.util.ArrayList;

import analyze.Commit;
import analyze.DatabaseAccess;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

/** 
 * Builds the model
 * @author toffer
 *
 */
public class ModelBuilder {
	private DatabaseAccess dbAccess = null;
	private ArrayList<Integer> nsListBuggy = new ArrayList<Integer>();
	private ArrayList<Integer> nsListNonBuggy = new ArrayList<Integer>();
	private ArrayList<Integer> ndListBuggy = new ArrayList<Integer>();
	private ArrayList<Integer> ndListNonBuggy = new ArrayList<Integer>();
	private ArrayList<Integer> nfListBuggy = new ArrayList<Integer>();
	private ArrayList<Integer> nfListNonBuggy = new ArrayList<Integer>();
	
	public ModelBuilder(DatabaseAccess dbAccess){
		this.dbAccess = dbAccess;
	}
	public void buildMetrics() {
		ArrayList<Commit> allCommits = dbAccess.getAllCommits();
		
		for(Commit commit : allCommits){
			if(commit.isBuggy()){
				nsListBuggy.add(commit.getNS());
				ndListBuggy.add(commit.getND());
				nfListBuggy.add(commit.getNF());
			} else {
				nsListNonBuggy.add(commit.getNS());
				ndListNonBuggy.add(commit.getND());
				nfListNonBuggy.add(commit.getNF());
			}
		}
		
		// calculate the medians 
		if(!Rengine.versionCheck()){
			 System.err.println("** Version mismatch - Java files don't match library version.");
			 System.exit(1);
		}
		
		String[] test = new String[] {"--vanilla"};
		Rengine re=new Rengine(test, false, new RTextConsole()); //false, new RTextConsole());
        System.out.println("Rengine created, waiting for R");
		// the engine creates R is a new thread, so we should wait until it's ready
        if (!re.waitForR()) {
            System.out.println("Cannot load R");
            return;
        }
        
        try{
        	REXP x;
        	System.out.println(re.eval("sqrt(36)"));
        } catch (Exception e) {
			System.out.println("EX:"+e);
			e.printStackTrace();
		}
		
	}
}
