package inspect;

import java.util.ArrayList;

import analyze.BugFinder;
import analyze.Commit;
import analyze.DatabaseAccess;
import analyze.Repository;

/**
 * Categorize commit messages and performs metrics on them. This way we can need 
 * only do one pass to get the metrics and find those that are bug inducing. We 
 * can't build the model yet until we know which are bug inducing and which are not,
 * therefore we will need to do a second pass later.  
 * 
 * @author toffer
 *
 */
public class CommitsInspector {
	
	private Corrective correctiveCat = null;
	private DatabaseAccess dbAccess = null;
	private Metrics metrics = null;
	private BugFinder bugFinder = null;
	
	/**
	 * Constructor
	 * @param dbAccess		The database access object
	 * @param repo			The repository
	 */
	public CommitsInspector(DatabaseAccess dbAccess, Repository repo){
		this.correctiveCat = new Corrective();
		this.dbAccess = dbAccess;
		this.metrics = new Metrics(repo);
		this.bugFinder = new BugFinder(repo, dbAccess);
	}
	
	/**
	 * Returns an ArrayList of commits of all corrective commits. Also performs metrics
	 * on all commits. Updates the commits table for the columns isBuggy, ns, nf, and nd
	 * 
	 * @param commits	An ArrayList of all the commits
	 */
	public void inspectAllCommits(ArrayList<Commit> allCommits){
		
		for (Commit commit: allCommits) {
			
			// If it is a corrective commit, check to see where the bug was introduced.
			if (correctiveCat.belongs(commit)){
				bugFinder.findBugInducingCommit(commit, allCommits);
			}
			
			// generate the metrics for the commit
			metrics.generateMetrics(commit);
			dbAccess.updateMetrics(commit);
		}
	}
	

}
