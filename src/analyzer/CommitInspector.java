package analyzer;

import java.util.ArrayList;
import java.util.List;


/**
 * The commit inspector "inspects" all commits.
 * It finds the bug inducing commits and generates metrics for each 
 * individual commit.
 * 
 * @author toffer
 *
 */
public class CommitInspector {
	
	private Repository repo = null;
	private DatabaseAccess dbAccess = null;
	private Metrics metrics = null;
	
	/**
	 * Constructor
	 * @param allCommits			All commits in descending order. 
	 * @param correctiveCommits		Commits that fix bugs/ are corrective
	 * @param repo 			 		The version-control repository
	 */
	public CommitInspector(Repository repo, DatabaseAccess db) {
		this.repo = repo;
		this.dbAccess = db;
		this.metrics = new Metrics(repo);
	}
	
	/**
	 * Find a bug inducing commit based on a commit that is corrective.
	 * That is - it fixes something in the code. It checks all commits prior to the
	 * corrective commit, and determines it to be bug inducing if it changes the same
	 * files. 
	 * 
	 * @param correctiveCommit		A commit that corrects/fixes something in the code
	 * @return Commit 				The bug inducing commit: null if none is found.
	 */
	private void findBugInducingCommit(Commit correctiveCommit, List<Commit> allCommits){

		ArrayList<String> filesChangedCorrectiveCommit = repo.filesChanged(correctiveCommit);
		
		for(Commit commit: allCommits){
			
			if(commit.getUnixTimeStamp() < correctiveCommit.getUnixTimeStamp()){
				ArrayList<String> filesChanged = repo.filesChanged(commit);
				
		
				
				for(String file: filesChanged){
					
					// This introduced the bug!
					if(filesChangedCorrectiveCommit.contains(file)){
						commit.setAsBuggy(); 
						dbAccess.markAsBugInducing(commit);
						return;
					} 
				} 
			}
		}
	}
	
	/**
	 * Helper function for findAllBugInducingCommits. Find the index of the corrective commit 
	 * in the allCommits list. This way, we don't keep looking at commits we don't care about
	 * when looking for bugs (those that are more recent then the bug correcting commit)
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
	 * Find all bug inducing commits given a set of commits that correct/fixes a bug.
	 * Updates the commits table to signify those it believes are bug introducing.
	 * 
	 * @param correctiveCommits			 An array list of commits that we believe introduced a bug.
	 * @param allCommits				 An array list containing all commits
	 * @return ArrayList<Commit> 		All bug inducing commits found
	 */
	public void inspectCommits(ArrayList<Commit> correctiveCommits, ArrayList<Commit> allCommits){
		
		int corrIndex = 0;
	
		for(Commit commit: correctiveCommits){ 
			corrIndex = findCorrectiveIndex(commit,allCommits, corrIndex);
			findBugInducingCommit(commit, allCommits.subList(corrIndex, allCommits.size()-1 ));
			metrics.generateMetrics(commit);
			dbAccess.updateMetrics(commit);
		}
	} 

}
