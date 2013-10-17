package discover;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import analyzer.Commit;

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
		
		File gitDir = new File(this.pathToRepo);	
		Process p = null;
		
		try {
			//ProcessBuilder pb = new ProcessBuilder("git", "diff", "b3ab51231bbc5726a778e233ccc7ce1b718d84e2","--numstat");
			ProcessBuilder pb = new ProcessBuilder(cmdList);
			pb.directory(gitDir); 
			p = pb.start();
			BufferedReader in = new BufferedReader(
					new InputStreamReader(p.getInputStream()));
			
			String line = null;
			while((line = in.readLine()) != null){
				//System.out.println(line);
				output+=line+"\n";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return output;
	}

	public String numDiff(Commit commit) {
		String[] cmdList = new String[]{"git","diff",commit.getCommitHash(),"--numstat"};
		return runGitCommand(cmdList);
		
	}

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
