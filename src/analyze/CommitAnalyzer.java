package analyze;
import inspect.CommitsInspector;

import java.util.ArrayList;

import scm.Git;
import scm.Repository;

import model.MetricDbAccess;
import model.ModelBuilder;



/**
 * CommitAnalyzer
 * @author toffer
 *
 */
public class CommitAnalyzer {
	
	private CommitsInspector commitsInspector = null;
	private MetricDbAccess metricDbAccess = null;
	private CommitDbAccess commitDbAccess = null;
	private Repository repo = null;
	private String repoName = null;
	
	
	/**
	 * Constructor 
	 * @param database 			The name of the database 
	 * @param commitTable		The table holding the commits
	 * @param dbUser			Valid user name w/ permission to read from database
	 * @param dbPass			The password of the dbuser
	 * @param pathToRepo		Path to the directory holding the repository
	 * @param repoType			The repository type i.e. git,svn,etc
	 * @param repoName			The repository name
	 */
	public CommitAnalyzer(String database, String commitTable, String dbUser, String dbPass, String pathToRepo, String repoType,
			String repoName) {
		
		commitDbAccess = new CommitDbAccess(database,commitTable,dbUser,dbPass);
		metricDbAccess = new MetricDbAccess(database, commitTable, dbUser,dbPass);
		this.repoName = repoName;
		
		if(repoType.toLowerCase() == "git"){
			repo = new Git(pathToRepo);
		} else{
			System.out.println(repoType + " is not currently supported. Exiting");
			System.exit(1);
		}
		
		commitsInspector = new CommitsInspector(commitDbAccess, repo);
	}
	

	/**
	 * Inspect all commits.
	 * Performs metrics on them and determines which are bug inducing
	 */
	public void inspectCommits() {
		ArrayList<Commit> commits = commitDbAccess.getAllCommits();
		commitsInspector.inspectAllCommits(commits);
	}
	
	/**
	 * Creates the metrics for bug inducing versus non bug inducing
	 * commits.
	 */
	public void createMetrics() {
		ModelBuilder mb = new ModelBuilder(repoName, metricDbAccess, commitDbAccess);
		mb.generateModel();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		CommitAnalyzer commitAnalyzer = new CommitAnalyzer("localhost","leaflet","toffer","",
												"/Users/toffer/Work/RiskyChanges/leaflet", "git", "leaflet");
		//commitAnalyzer.inspectCommits();
		System.out.println("--- DONE INSPECTING!");
		commitAnalyzer.createMetrics();
	}

}
