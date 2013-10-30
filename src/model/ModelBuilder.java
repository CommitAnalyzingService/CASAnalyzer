package model;

import java.util.ArrayList;

import analyze.Commit;
import analyze.CommitDbAccess;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

/** 
 * Builds the model
 * @author toffer
 *
 */
public class ModelBuilder {

	private String repoName = null;
	private MetricDbAccess metricDbAccess = null;
	private CommitDbAccess commitDbAccess = null;
	
	private ArrayList<Integer> nsListBuggy = new ArrayList<Integer>();
	private ArrayList<Integer> nsListNonBuggy = new ArrayList<Integer>();
	private ArrayList<Integer> ndListBuggy = new ArrayList<Integer>();
	private ArrayList<Integer> ndListNonBuggy = new ArrayList<Integer>();
	private ArrayList<Integer> nfListBuggy = new ArrayList<Integer>();
	private ArrayList<Integer> nfListNonBuggy = new ArrayList<Integer>();
	private ArrayList<Double> entrophyListBuggy = new ArrayList<Double>();
	private ArrayList<Double> entrophyListNonBuggy = new ArrayList<Double>();
	
	public ModelBuilder(String repoName, MetricDbAccess mDbAccess, CommitDbAccess cDbAccess){
		this.metricDbAccess = mDbAccess;
		this.commitDbAccess = cDbAccess;
		this.repoName = repoName;
	}
	
	/**
	 * Build the lists of metrics for each commit
	 */
	public void buildLists() {
		ArrayList<Commit> allCommits = commitDbAccess.getAllCommits();
		
		for(Commit commit : allCommits){
			if(commit.isBuggy()){
				nsListBuggy.add(commit.getNS());
				ndListBuggy.add(commit.getND());
				nfListBuggy.add(commit.getNF());
				entrophyListBuggy.add(commit.getEntrophy());
			} else {
				nsListNonBuggy.add(commit.getNS());
				ndListNonBuggy.add(commit.getND());
				nfListNonBuggy.add(commit.getNF());
				entrophyListNonBuggy.add(commit.getEntrophy());
			}
		}
		
		calculateMedianMetrics();
	}
	
	/**
	 * REXP expects a vector as "(1,2,3)'
	 * However, ArrayList returns "[1,2,3]".
	 * This function makes returns [1,2,3] as (1,2,3).
	 * 
	 * @param toModify		String in format [1,2,3]
	 * @return String		String in format (1,2,3)
	 */
	public String replaceBrackets(String toModify){
		return ((toModify.replace("[", "(")).replace("]", ")"));
	}
	
	/**
	 * Calculate the medians
	 */
	public void calculateMedianMetrics(){
		
		Metrics metrics = new Metrics();
		
		// calculate the medians 
		if(!Rengine.versionCheck()){
			System.err.println("** Version mismatch - Java files don't match library version.");
			System.exit(1);
		}

		String[] test = new String[] {"--vanilla"};
		Rengine re=new Rengine(test, false, new RTextConsole()); //false, new RTextConsole());
		System.out.println("Rengine created, waiting for R");
		
		// the engine creates R is a new thread, so we should wait until it's ready
		if (!re.waitForR()) {
			System.out.println("Cannot load R");
			return;
		}

		try{

			REXP x;
			
			x = re.eval("median(c" + replaceBrackets(nsListNonBuggy.toString()) + ")");
			metrics.setNsNonBuggyMedian(x.asDouble());
			
			x = re.eval("median(c" + replaceBrackets(nsListBuggy.toString()) + ")");
			metrics.setNsBuggyMedian(x.asDouble());

			x = re.eval("median(c" + replaceBrackets(ndListNonBuggy.toString()) + ")");
			metrics.setNdNonBuggyMedian(x.asDouble());

			x = re.eval("median(c" + replaceBrackets(ndListBuggy.toString()) + ")");
			metrics.setNdBuggyMedian(x.asDouble());
			
			x = re.eval("median(c" + replaceBrackets(nfListNonBuggy.toString()) + ")");
			metrics.setNfNonBuggyMedian(x.asDouble());
			
			x = re.eval("median(c" + replaceBrackets(nfListBuggy.toString()) + ")");
			metrics.setNfBuggyMedian(x.asDouble());
			
			x = re.eval("median(c" + replaceBrackets(entrophyListBuggy.toString()) + ")");
			metrics.setEntrophyBuggyMedian(x.asDouble());
			
			x = re.eval("median(c" + replaceBrackets(entrophyListNonBuggy.toString()) + ")");
			metrics.setEntrophyNonBuggyMedian(x.asDouble());
			
			// update the metrics table
			metricDbAccess.updateMetric(metrics, repoName);
			
			System.out.println("Done...");
			
		} catch (Exception e) {
			System.out.println("EX:"+e);
			e.printStackTrace();
		}
	}
	
	public void generateModel() {
		buildLists();
	}
		
}
