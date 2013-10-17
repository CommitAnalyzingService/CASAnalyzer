package categorize;
import analyzer.Commit;

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
