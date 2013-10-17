package categorize;

import java.util.ArrayList;

/**
 * This class labels a commit into a category.
 * @author toffer
 *
 */
public class CommitClassifier {
	ArrayList<Category> categories;
	
	public CommitClassifier() {
		categories = new ArrayList<Category>();
		
		Corrective correctiveCat = new Corrective();
		categories.add(correctiveCat);
	}
	
	// TBD
	public Category categorizeCommit(){
		return categories.get(0);
	}
	
}
