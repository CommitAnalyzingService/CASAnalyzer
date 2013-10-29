package categorize;

import java.util.ArrayList;

import analyzer.Commit;

/**
 * Categorize commit messages
 * @author toffer
 *
 */
public class CommitsCategorizer {
	
	public Corrective correctiveCat = null;
	
	public CommitsCategorizer(){
		correctiveCat = new Corrective();
	}
	
	public ArrayList<Commit> getAllCorrectiveCommits(ArrayList<Commit> commits){
		
		ArrayList<Commit> correctiveCommits = new ArrayList<Commit>();
		
		for (Commit commit: commits) {
			if (correctiveCat.belongs(commit)){
				correctiveCommits.add(commit);
			}
		}
		
		System.out.println(correctiveCommits.size());

		return correctiveCommits;
	}
	

}
