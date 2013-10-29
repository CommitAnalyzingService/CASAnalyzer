package analyzer;
import java.util.ArrayList;

import model.ModelBuilder;

import categorize.CommitsCategorizer;


/**
 * CommitAnalyzer
 * @author toffer
 *
 */
public class CommitAnalyzer {
	
	CommitsCategorizer commitsCategorizer = null;
	DatabaseAccess dbAccess = null;
	Repository repo = null;
	CommitInspector commitInspector = null;
	
	/**
	 * Constructor 
	 * @param database 			The name of the database 
	 * @param commitTable		The table holding the commits
	 * @param dbUser			Valid user name w/ permission to read from database
	 * @param dbPass			The password of the dbuser
	 * @param pathToRepo		Path to the directory holding the repository
	 * @param repoType			The repository type i.e. git,svn,etc
	 */
	public CommitAnalyzer(String database, String commitTable, String dbUser, String dbPass, String pathToRepo, String repoType) {
		commitsCategorizer = new CommitsCategorizer();
		dbAccess = new DatabaseAccess(database,commitTable,dbUser,dbPass);
		
		if(repoType.toLowerCase() == "git"){
			repo = new Git(pathToRepo);
		} else{
			System.out.println(repoType + " is not currently supported. Exiting");
			System.exit(1);
		}
		
		commitInspector = new CommitInspector(repo, dbAccess);
	}
	

	/**
	 * Start analyzing the commits 
	 */
	public void start() {
		 
		ArrayList<Commit> commits = dbAccess.getAllCommits();
		System.out.println("Getting Corrective...");
		ArrayList<Commit> correctiveCommits = commitsCategorizer.getAllCorrectiveCommits(commits);
		System.out.println("Inspecting..");
		commitInspector.inspectCommits(correctiveCommits, commits);
		
		
	/*	// Testing
		for(Commit commit : correctiveCommits){
			System.out.println(commit.getCommitHash() + " " + 
					commit.getMsg() + " " + commit.getUnixTimeStamp());
		} */
		
	}
	
	public void createMetrics() {
		ModelBuilder mb = new ModelBuilder(dbAccess);
		mb.buildMetrics();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		CommitAnalyzer codeAnalyzer = new CommitAnalyzer("localhost","leaflet","toffer","",
												"/Users/toffer/Work/RiskyChanges/leaflet", "git");
		//codeAnalyzer.start();
		codeAnalyzer.createMetrics();
	}

}
