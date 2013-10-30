package model;

import analyze.Commit;

/**
 * Simply holds metrics for a commit
 * This way the order we pass metrics and such doesn't matter for the 
 * metrics db access object. 
 * 
 * @author toffer
 *
 */
public class Metrics {
	
	private double nsNonBuggyMedian;
	private double nsBuggyMedian;
	private double ndNonBuggyMedian;
	private double ndBuggyMedian;
	private double nfBuggyMedian;
	private double nfNonBuggyMedian;
	
	/** GETTERS AND SETTERS **/
	
	public double getNsNonBuggyMedian() {
		return nsNonBuggyMedian;
	}

	public void setNsNonBuggyMedian(double nsNonBuggyMedian) {
		this.nsNonBuggyMedian = nsNonBuggyMedian;
	}

	public double getNsBuggyMedian() {
		return nsBuggyMedian;
	}

	public void setNsBuggyMedian(double nsBuggyMedian) {
		this.nsBuggyMedian = nsBuggyMedian;
	}

	public double getNdNonBuggyMedian() {
		return ndNonBuggyMedian;
	}

	public void setNdNonBuggyMedian(double ndNonBuggyMedian) {
		this.ndNonBuggyMedian = ndNonBuggyMedian;
	}

	public double getNdBuggyMedian() {
		return ndBuggyMedian;
	}

	public void setNdBuggyMedian(double ndBuggyMedian) {
		this.ndBuggyMedian = ndBuggyMedian;
	}

	public double getNfBuggyMedian() {
		return nfBuggyMedian;
	}

	public void setNfBuggyMedian(double nfBuggyMedian) {
		this.nfBuggyMedian = nfBuggyMedian;
	}

	public double getNfNonBuggyMedian() {
		return nfNonBuggyMedian;
	}

	public void setNfNonBuggyMedian(double nfNonBuggyMedian) {
		this.nfNonBuggyMedian = nfNonBuggyMedian;
	}
	
	
}
