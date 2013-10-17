package discover;

import java.util.ArrayList;

import analyzer.Commit;

/**
 * Find bug-introducing commits
 * @author toffer
 *
 */
public class BugFinder {
	
	private Repository repo = null;
	
	/**
	 * Constructor
	 * @param allCommits			All commits in descending order. 
	 * @param correctiveCommits		Commits that fix bugs/ are corrective
	 * @param repo 			 		The version-control repository
	 */
	public BugFinder(Repository repo) {
		this.repo = repo;
	}
	
	/**
	 * Find a bug inducing commit based on a commit that is corrective.
	 * That is - it fixes something in the code.
	 * @param correctiveCommit	A commit that corrects/fixes something in the code
	 */
	public void findBugInducingCommit(Commit correctiveCommit, ArrayList<Commit> allCommits){

		ArrayList<String> filesChangedCorrectiveCommit = repo.filesChanged(correctiveCommit);
		
		/* TESTING
		for(String file : filesChanged){
			System.out.println(file);
		} */
		
		// Start by looking at all commits before the corrective commit
		for(Commit commit: allCommits){
			if(commit.getUnixTimeStamp() < correctiveCommit.getUnixTimeStamp()){
				// check if this commit modified a file changed by the corrective commit
				ArrayList<String> filesChanged = repo.filesChanged(commit);
				for(String file: filesChanged){
					
					// This introduced the bug!
					if(filesChangedCorrectiveCommit.contains(file)){
						System.out.println("Commit " + commit.getCommitHash() +  " was buggy, fixed in " +
								correctiveCommit.getCommitHash());
						// Update table
						// TBD
						return;
					}
				}
			}
		}
	}
	
	/**
	 * Find all bug inducing commits given a set of commits that correct/fixes a bug.
	 * @param correctiveCommits	 An array list of commits that we believe introduced a bug.
	 * @param allCommits		 An array list containing all commits
	 */
	public void findAllBugInducingCommits(ArrayList<Commit> correctiveCommits, ArrayList<Commit> allCommits){
		ArrayList<Commit> bugCommits = new ArrayList<Commit>();
		
		// try to find where the bug was introduced by looking at the commits that
		// fixed something i.e. corrective commits
		for(Commit commit: correctiveCommits){
			findBugInducingCommit(commit, allCommits);
		}
		
	}

}
