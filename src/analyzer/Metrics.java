package analyzer;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Calculates the metrics for a commit 
 * @author toffer
 *
 */
public class Metrics {
	
	private Repository repo = null;
	
	public Metrics(Repository repo){
		this.repo = repo;
	}
	
	public void generateMetrics(Commit commit){
		
		ArrayList<String> filesChanged = repo.filesChanged(commit);
		addNS(commit, filesChanged);
		addND(commit, filesChanged);
		addNF(commit, filesChanged);
	}
	
	/** ----- Diffusion ------ **/
	
	/**
	 * Function: 		addNS
	 * Description:		Adds the number of modified subsystems to a commit
	 * 				 	We use the root directory name as subsystem name
	 */
	void addNS(Commit commit, ArrayList<String> filesChanged) {
		
		int modifiedSubSystems = 0;
		ArrayList<String> seenSubSystems = new ArrayList<String>();
		
		for(String file : filesChanged){
			String rootDir = file.split("/")[0];
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
	void addND(Commit commit, ArrayList<String> filesChanged) {
		int modifiedDir = 0;
		ArrayList<String> seenDir = new ArrayList<String>();
		
		for(String file: filesChanged){
			String[] dirAndFile = file.split("/");
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
	void addNF(Commit commit, ArrayList<String> filesChanged){
		commit.setNF(filesChanged.size());
	}
	
	/**
	 * Function:		addEntrophy
	 * Description:		Adds entrophy to the commit
	 */
	void addEntrophy() {
		// TBD
	}
	
	
	/** END DIFFUSION **/

}
