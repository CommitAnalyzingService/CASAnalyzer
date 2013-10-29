package analyze;
import inspect.CommitsInspector;

import java.util.ArrayList;

import model.ModelBuilder;



/**
 * CommitAnalyzer
 * @author toffer
 *
 */
public class CommitAnalyzer {
	
	CommitsInspector commitsInspector = null;
	DatabaseAccess dbAccess = null;
	Repository repo = null;
	
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
		dbAccess = new DatabaseAccess(database,commitTable,dbUser,dbPass);
		
		if(repoType.toLowerCase() == "git"){
			repo = new Git(pathToRepo);
		} else{
			System.out.println(repoType + " is not currently supported. Exiting");
			System.exit(1);
		}
		
		commitsInspector = new CommitsInspector(dbAccess, repo);
	}
	

	/**
	 * Inspect all commits.
	 * Performs metrics on them and determines which are bug inducing
	 */
	public void inspectCommits() {
		ArrayList<Commit> commits = dbAccess.getAllCommits();
		commitsInspector.inspectAllCommits(commits);
	}
	
	/**
	 * Creates the metrics for bug inducing versus non bug inducing
	 * commits.
	 */
	public void createMetrics() {
		ModelBuilder mb = new ModelBuilder(dbAccess);
		mb.generateModel();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		CommitAnalyzer codeAnalyzer = new CommitAnalyzer("localhost","leaflet","toffer","",
												"/Users/toffer/Work/RiskyChanges/leaflet", "git");
		//codeAnalyzer.inspectCommits();
		codeAnalyzer.createMetrics();
	}

}
