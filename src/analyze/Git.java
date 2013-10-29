package analyze;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * A Git implementation of Repository
 * @author toffer
 *
 */
public class Git extends Repository{

	public Git(String pathToRepo) {
		super(pathToRepo);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Runs a git command 
	 * @param cmd String of the git command to run
	 * @return String output of the results 
	 */
	public String runGitCommand(String[] cmdList){
		String output = "";
		Process p = null;
		
		try {
			ProcessBuilder pb = new ProcessBuilder(cmdList);
			pb.directory(this.pathToRepo); 
			p = pb.start();
		
			BufferedReader in = new BufferedReader(
					new InputStreamReader(p.getInputStream()));
		
			String line = null;
			while((line = in.readLine()) != null){
				output+=line+"\n";
			}
			
			in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return output;
	}

	/**
	 * Perform a num diff on a commit 
	 * @param commit 	The commit to run a git numdiff 
	 * @return String	The result
	 */
	public String numDiff(Commit commit) {
		String[] cmdList = new String[]{"git","diff",commit.getCommitHash(),"--numstat"};
		return runGitCommand(cmdList);
	}

	/**
	 * Find all the files changed within a commit.
	 * @param commit				The commit to check for files changed
	 * @return ArrayList<String>	An ArrayList with all files changed
	 */
	public ArrayList<String> filesChanged(Commit commit) {
		String diffResult = numDiff(commit);
		
		ArrayList<String> filesChanged = new ArrayList<String>();
		String[] lines = diffResult.split("\n");
		
		for(String line : lines ){
			String[] diffLine = line.split("	");
			
			// Saftey check
			if (diffLine.length > 2){
				String file = diffLine[2];
				filesChanged.add(file);
			}
		}
		return filesChanged;
	}

}
