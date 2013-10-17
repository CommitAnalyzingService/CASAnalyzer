package analyzer;

/**
 * Object representing a commit message
 * @author toffer
 *
 */
public class Commit implements Comparable<Commit> {
	private String commitHash = null;
	private String treeHash = null;
	private String message = null;
	private String authorName = null;
	private String unixTimeStamp = null;
	
	public Commit(String commitHash, String treeHash, String message, String authorName, String unixTimeStamp){
		this.commitHash = commitHash;
		this.treeHash = treeHash;
		this.message = message;
		this.authorName = authorName;
		this.unixTimeStamp = unixTimeStamp;
	}
	
	/**
	 * Get the commit message
	 * @return String: commit message
	 */
	public String getMsg(){
		return this.message;
	}
	
	/**
	 * Returns the unix time stamp
	 * @return Int: commit's unix time stamp
	 */
	public int getUnixTimeStamp(){
		try {
			return Integer.parseInt(unixTimeStamp);
		} catch (Exception e){
			return -1;
		}
	}
	
	/**
	 * Returns the commit's hash identifier
	 * @return String of the commit's hash
	 */
	public String getCommitHash() {
		return this.commitHash;
	}


	public int compareTo(Commit commit) {
		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;
		
		if(this.getUnixTimeStamp() == commit.getUnixTimeStamp()){
			return EQUAL;
		}else if(this.getUnixTimeStamp() < commit.getUnixTimeStamp()){
			return BEFORE;
		} else {
			return AFTER;
		}
	}
}
