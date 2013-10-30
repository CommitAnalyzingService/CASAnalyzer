package scm;

import java.io.File;
import java.util.ArrayList;

import analyze.Commit;


/**
 * Represent a version control repository
 * @author toffer
 *
 */
public abstract class Repository {
	
	File pathToRepo = null;
	
	public Repository(String pathToRepo){
		this.pathToRepo = new File(pathToRepo);
	}
	
	/**
	 * Returns the result of running a num diff from a commit
	 * @param Commit				The commit to run the cmd
	 * @return String of the result:
	 * 			Expecting: number of added and deleted lines in decimal notation and 
	 * 			pathname without abbreviation
	 * 		Sample:
	 * 			1	0	README.md
	 * 			
	 */
	public abstract String numDiff(Commit commit);
	
	/**
	 * Returns the names of the files that the commit has changed.
	 * @param Commit 				Commit to check
	 * @return ArrayList 			List containing strings of the files changed.
	 */
	public abstract ArrayList<String> filesChanged(Commit commit);
	
	/**
	 * Performs a diff and returns an array list of diffFiles containing the name of the file,
	 * lines added, and lines deleted
	 * 
	 * @param commit				The commit to perform a diff on.
	 * @return ArrayList			List of all diffFiles
	 */
	public abstract ArrayList<DiffFile> performDiff(Commit commit);

}
