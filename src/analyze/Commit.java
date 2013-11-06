package analyze;

/**
 * Object representing a commit message
 * Also holds metrics for that object
 * 
 * @author toffer
 */
public class Commit implements Comparable<Commit> {
	private String commitHash = null;
	private String treeHash = null;
	private String message = null;
	private String authorName = null;
	private String unixTimeStamp = null;
	
	/* METRICS */
	// size
	private int la = 0; // lines of code added
	private int ld = 0; // lines of code deleted
	private int lt = 0; // lines of code in file b4 change
	
	// diffusion
	private int ns = 0; // number of modified subsystems
	private int nd = 0; // number of modified directories
	private int nf = 0; // number of modified files
	private double entrophy = 0; // distribution of modified code across each file
	
	// purpose 
	private boolean fix = false; // whether or not change is a defect fix
	
	// history
	private int ndev = 0;
	private int age = 0;
	private int nuc = 0; // number of unique changes to modified files
	
	// experience
	private int exp = 0; // developer experience
	private int rexp = 0; // recent developer experience
	private int sexp = 0; // experience on subsystem
	
	
	private boolean buggy = false;
	
	public Commit(String commitHash, String treeHash, String message, String authorName, String unixTimeStamp, int ns,
			int nd, int nf, boolean isBuggy, double entrophy){
		this.commitHash = commitHash;
		this.treeHash = treeHash;
		this.message = message;
		this.authorName = authorName;
		this.unixTimeStamp = unixTimeStamp;
		this.ns = ns;
		this.nd = nd;
		this.nf = nf;
		this.buggy = isBuggy;
		this.entrophy = entrophy;
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
	
	/**
	 * Sets the commit as buggy.
	 */
	public void setAsBuggy(){
		this.buggy = true;
	}
	
	/**** GETTERS AND SETTERSFOR METRICS *****/
	
	public void setNS(int ns){
		this.ns = ns;
	}
	
	public int getNS(){
		return this.ns;
	}
	
	public void setND(int nd){
		this.nd = nd;
	}
	
	public int getND(){
		return this.nd;
	}
	
	public void setNF(int nf){
		this.nf = nf;
	}
	
	public int getNF(){
		return this.nf;
	}
	public void setEntrophy(double entrophy){
		this.entrophy = entrophy;
	}
	
	public double getEntrophy(){
		return this.entrophy;
	}
	
	public boolean isBuggy(){
		return this.buggy;
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

	public int getLa() {
		return la;
	}

	public void setLa(int la) {
		this.la = la;
	}

	public int getLd() {
		return ld;
	}

	public void setLd(int ld) {
		this.ld = ld;
	}

	public int getLt() {
		return lt;
	}

	public void setLt(int lt) {
		this.lt = lt;
	}
}
