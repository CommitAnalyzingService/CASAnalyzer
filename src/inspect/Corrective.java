package inspect;

import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import analyze.Commit;

/**
 * The corrective category includes words that are associated with
 * commits that correct/fix code.
 * 
 * @author toffer
 *
 */
public class Corrective implements Category {
	
	ArrayList<String> associatedWords = new ArrayList<String>();
	final String fileLoc = "Resources/correctiveWords.csv";
	
	public Corrective() {
		associatedWords = new ArrayList<String>();
		readInCorrectiveWords();
	}
	
	// private method to be used internally only
	private void readInCorrectiveWords(){
		BufferedReader br = null;
		String line = "";
		
		try {
			br = new BufferedReader(new FileReader(fileLoc));
			while ( (line = br.readLine()) != null) {
				String[] words = line.split(",");
				for (String word : words){
					associatedWords.add(word);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( br != null) {
				try {
					br.close();
				} catch (IOException e){
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean belongs(Commit commit) {
	
		String message = commit.getMsg();
		String[] messageArr = message.split(" "); // split on each word
		
		for (String word : messageArr){
			
			// we need to go beyond array contains i.e. fixed = fix
			for (String assocWord : associatedWords){
				
				if(word.toLowerCase().contains(assocWord.toLowerCase())){
					return true;
				}
			}
		}
	
		return false;
	}

}
