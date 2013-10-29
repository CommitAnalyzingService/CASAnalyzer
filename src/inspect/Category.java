package inspect;
import analyze.Commit;

/**
 * Represent a category to characterize commits
 * @author toffer
 */
public interface Category {
	
	/**
	 * Check if a commit message belongs to this category
	 * @returns boolean
	 */
	public boolean belongs(Commit commit);
	
}
