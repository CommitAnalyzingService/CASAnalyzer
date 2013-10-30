package scm;

/**
 * Represents a scm 'diff' for a single file
 * 
 * @author toffer
 *
 */
public class DiffFile {
	private String fileChanged = null;
	private int linesAdded = 0;
	private int linesDeleted = 0;
	
	public DiffFile(String fileChanged, int linesAdded, int linesDeleted){
		this.fileChanged = fileChanged;
		this.linesAdded = linesAdded;
		this.linesDeleted = linesDeleted;
	}
	
	public String getFileChanged(){
		return this.fileChanged;
	}
	
	public int getLinesAdded(){
		return this.linesAdded;
	}
	
	public int getLinesDeleted(){
		return this.linesDeleted;
	}
}
