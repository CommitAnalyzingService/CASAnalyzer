package analyze;

import java.util.ArrayList;
import java.util.List;

import scm.Repository;


/**
 * The bug finder "inspects" all commits to find those that potentially
 * caused a bug.
 * 
 * @author toffer
 *
 */
public class BugFinder {
	
	private Repository repo = null;
	private CommitDbAccess dbAccess = null;
	
	/**
	 * Constructor
	 * @param allCommits			All commits in descending order. 
	 * @param correctiveCommits		Commits that fix bugs/ are corrective
	 * @param repo 			 		The version-control repository
	 */
	public BugFinder(Repository repo, CommitDbAccess db) {
		this.repo = repo;
		this.dbAccess = db;
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
	public void findBugInducingCommit(Commit correctiveCommit, List<Commit> allCommits){

		ArrayList<String> filesChangedCorrectiveCommit = repo.filesChanged(correctiveCommit);
		for(Commit commit: allCommits){
			
			if(commit.getUnixTimeStamp() < correctiveCommit.getUnixTimeStamp()){
				System.out.println("Is buggy?? " + commit.getCommitHash());
				ArrayList<String> filesChanged = repo.filesChanged(commit);
	
				for(String file: filesChanged){
					
					// This introduced the bug!
					if(filesChangedCorrectiveCommit.contains(file)){
						System.out.println("YESS!");
						commit.setAsBuggy(); 
						dbAccess.markAsBugInducing(commit);
						return;
					} 
				} 
			}
		}
	}

}
