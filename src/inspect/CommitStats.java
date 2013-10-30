package inspect;

import java.util.ArrayList;
import java.util.Arrays;

import scm.DiffFile;
import scm.Repository;

import analyze.Commit;

/**
 * Calculates the metrics for a commit 
 * @author toffer
 *
 */
public class CommitStats {
	
	private Repository repo = null;
	
	public CommitStats(Repository repo){
		this.repo = repo;
	}
	
	public void generateStats(Commit commit){
		ArrayList<DiffFile> diff = repo.performDiff(commit);
		addNS(commit, diff);
		addND(commit, diff);
		addNF(commit, diff);
	}
	
	/** ----- Diffusion ------ **/
	
	/**
	 * Function: 		addNS
	 * Description:		Adds the number of modified subsystems to a commit
	 * 				 	We use the root directory name as subsystem name
	 */
	void addNS(Commit commit, ArrayList<DiffFile> diff) {
		int modifiedSubSystems = 0;
		ArrayList<String> seenSubSystems = new ArrayList<String>();
		
		for(DiffFile diffFile : diff){
			String rootDir = (diffFile.getFileChanged()).split("/")[0];
			if(!seenSubSystems.contains(rootDir)){
				modifiedSubSystems++;
				seenSubSystems.add(rootDir);
			}
		}
		commit.setNS(modifiedSubSystems);
	}
	
	/**
	 * Function:		addND
	 * Description:		Adds the number of modified directories to a commit
	 * 					Currently counts any directory that has the same name
	 * 					as the same directory - even though they can be different.
	 * 					(e.g., root/foo/bar & root/bar/foo) 
	 */
	void addND(Commit commit, ArrayList<DiffFile> diff) {
		int modifiedDir = 0;
		ArrayList<String> seenDir = new ArrayList<String>();
		
		for(DiffFile diffFile: diff){
			String[] dirAndFile = (diffFile.getFileChanged()).split("/");
			String[] dirs = Arrays.copyOfRange(dirAndFile, 0, dirAndFile.length-1);
			for(String dir : dirs){
				if(!seenDir.contains(dir)){
					modifiedDir++;
					seenDir.add(dir);
				}
			}
		}
		
		commit.setND(modifiedDir);
	}
	
	/**
	 * Function:		addNF
	 * Description: 	Adds the number of modified files to a commit
	 */
	void addNF(Commit commit, ArrayList<DiffFile> diff){
		commit.setNF(diff.size());
	}
	
	/**
	 * Function:		addEntrophy
	 * Description:		Adds entrophy to the commit
	 * 					Measures the distribution of change across
	 * 					the file
	 */
	void addEntrophy(Commit commit, ArrayList<DiffFile> diff) {
		ArrayList<Integer> locModified = new ArrayList<Integer>();
		int totalLOCModified = 0;
		double entrophy = 0;
		
		for(DiffFile diffFile : diff){
			int modLOC = diffFile.getAllModifiedLOC();
			totalLOCModified += modLOC; locModified.add(modLOC);
		}
		
		for(DiffFile diffFile: diff){
			double value = (double)diffFile.getAllModifiedLOC()/(double)totalLOCModified;
			entrophy -= (value * (Math.log(value)/Math.log(2)));
		}
		commit.setEntrophy(entrophy);
	}
	
	/** END DIFFUSION **/
}
