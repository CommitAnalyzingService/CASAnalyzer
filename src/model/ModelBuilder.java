package model;

import java.util.ArrayList;

import analyzer.Commit;
import analyzer.DatabaseAccess;

/** 
 * Builds the model
 * @author toffer
 *
 */
public class ModelBuilder {
	private DatabaseAccess dbAccess = null;
	private ArrayList<Integer> nsList = new ArrayList<Integer>();
	private ArrayList<Integer> ndList = new ArrayList<Integer>();
	private ArrayList<Integer> nfList = new ArrayList<Integer>();
	
	public ModelBuilder(DatabaseAccess dbAccess){
		this.dbAccess = dbAccess;
	}
	public void buildMetrics() {
		ArrayList<Commit> allCommits = dbAccess.getAllCommits();
		
		for(Commit commit : allCommits){
			nsList.add(commit.getNF());
			ndList.add(commit.getND());
			nfList.add(commit.getNF());
		}
		
	}
}
