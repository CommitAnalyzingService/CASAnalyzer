package inspect;

import java.util.ArrayList;
import java.util.List;

import scm.Repository;

import analyze.BugFinder;
import analyze.Commit;
import analyze.CommitDbAccess;

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
	private CommitDbAccess dbAccess = null;
	private CommitStats commitStats = null;
	private BugFinder bugFinder = null;
	
	/**
	 * Constructor
	 * @param dbAccess		The database access object
	 * @param repo			The repository
	 */
	public CommitsInspector(CommitDbAccess dbAccess, Repository repo){
		this.correctiveCat = new Corrective();
		this.dbAccess = dbAccess;
		this.commitStats = new CommitStats(repo);
		this.bugFinder = new BugFinder(repo, dbAccess);
	}
	
	/**
	 * Find the index of the corrective commit in the allCommits list. This way, we don't keep 
	 * looking at commits we don't care about when looking for bugs 
	 * (those that are more recent then the bug correcting commit)
	 * 
	 * @param corrCommit	The corrective commit
	 * @param allCommits	List of all commits
	 * @param startIndex	The index to start looking for the corrective commit
	 * @return 				The index of the corrective commit in the list of all commits
	 */
	private int findCorrectiveIndex(Commit corrCommit, List<Commit> allCommits, int startIndex){
		
		int corrIndex = allCommits.size() -1;
		for (int index = startIndex; index < allCommits.size(); index++){
			if (corrCommit.getCommitHash() == allCommits.get(index).getCommitHash()){
				corrIndex = index;
			}
		}
		
		return corrIndex;
	}
	
	/**
	 * Inspects all commits. Also performs metrics
	 * Updates the commits table for the columns isBuggy, ns, nf, and nd
	 * 
	 * @param commits	An ArrayList of all the commits
	 */
	public void inspectAllCommits(ArrayList<Commit> allCommits){
		int corrIndex = 0; // keep track on the index of the corrective commit
		
		for (Commit commit: allCommits) {
			//System.out.println("       inspecting: " + commit.getCommitHash());
		
			// If it is a corrective commit, check to see where the bug was introduced.
			if (correctiveCat.belongs(commit)){
				corrIndex = findCorrectiveIndex(commit,allCommits, corrIndex);
				bugFinder.findBugInducingCommit(commit, allCommits.subList(corrIndex, allCommits.size()-1 ));
			}
			// generate the metrics for the commit
			commitStats.generateStats(commit);
			dbAccess.updateMetrics(commit);
		}
	}
	

}
