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
	private ArrayList<Integer> laListBuggy = new ArrayList<Integer>();
	private ArrayList<Integer> laListNonBuggy = new ArrayList<Integer>();
	private ArrayList<Integer> ldListBuggy = new ArrayList<Integer>();
	private ArrayList<Integer> ldListNonBuggy = new ArrayList<Integer>();
 	
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
				laListBuggy.add(commit.getLa());
				ldListBuggy.add(commit.getLd());
			} else {
				nsListNonBuggy.add(commit.getNS());
				ndListNonBuggy.add(commit.getND());
				nfListNonBuggy.add(commit.getNF());
				entrophyListNonBuggy.add(commit.getEntrophy());
				laListNonBuggy.add(commit.getLa());
				ldListNonBuggy.add(commit.getLa());
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
			double pvalue;
			
			System.out.println(ldListBuggy.toString());
			
			/* NS */
			
			// check significance by wilcox test
			pvalue = (re.eval("wilcox.test(c" + replaceBrackets(nsListNonBuggy.toString()) + ",c" + 
					replaceBrackets(nsListBuggy.toString()) + ")$p.value")).asDouble();
			System.out.println("***** " + pvalue);
			// if significant
			if(pvalue >= 0.05){
				x = re.eval("median(c" + replaceBrackets(nsListNonBuggy.toString()) + ")");
				metrics.setNsNonBuggyMedian(x.asDouble());
				x = re.eval("median(c" + replaceBrackets(nsListBuggy.toString()) + ")");
				metrics.setNsBuggyMedian(x.asDouble());
			} else {
				metrics.setNsNonBuggyMedian(-1);
				metrics.setNsBuggyMedian(-1);
			}
			
			/* ND */
			
			pvalue = (re.eval("wilcox.test(c" + replaceBrackets(ndListNonBuggy.toString()) + ",c" + 
					replaceBrackets(ndListBuggy.toString()) + ")$p.value")).asDouble();
			
			System.out.println("***** " + pvalue);
			if(pvalue >= 0.05){
				x = re.eval("median(c" + replaceBrackets(ndListNonBuggy.toString()) + ")");
				metrics.setNdNonBuggyMedian(x.asDouble());
				x = re.eval("median(c" + replaceBrackets(ndListBuggy.toString()) + ")");
				metrics.setNdBuggyMedian(x.asDouble());
			} else {
				metrics.setNdNonBuggyMedian(-1);
				metrics.setNdBuggyMedian(-1);
			}
			
			/* NF */
			pvalue = (re.eval("wilcox.test(c" + replaceBrackets(nfListNonBuggy.toString()) + ",c" + 
					replaceBrackets(nfListBuggy.toString()) + ")$p.value")).asDouble();
			
			System.out.println("***** " + pvalue);
			if(pvalue >= 0.05){
				x = re.eval("median(c" + replaceBrackets(nfListNonBuggy.toString()) + ")");
				metrics.setNfNonBuggyMedian(x.asDouble());
				x = re.eval("median(c" + replaceBrackets(nfListBuggy.toString()) + ")");
				metrics.setNfBuggyMedian(x.asDouble());
			} else {
				metrics.setNfNonBuggyMedian(-1);
				metrics.setNfBuggyMedian(-1);
			}
			
			/* Entrophy */
			
			pvalue = (re.eval("wilcox.test(c" + replaceBrackets(entrophyListBuggy.toString()) + ",c" + 
					replaceBrackets(entrophyListNonBuggy.toString()) + ")$p.value")).asDouble();
			
			System.out.println("***** " + pvalue);
			if(pvalue >= 0.05){
				x = re.eval("median(c" + replaceBrackets(entrophyListBuggy.toString()) + ")");
				metrics.setEntrophyBuggyMedian(x.asDouble());
				x = re.eval("median(c" + replaceBrackets(entrophyListNonBuggy.toString()) + ")");
				metrics.setEntrophyNonBuggyMedian(x.asDouble());
			} else {
				metrics.setEntrophyBuggyMedian(-1);
				metrics.setEntrophyNonBuggyMedian(-1);
			}
			
			
			/* LA */
			pvalue = (re.eval("wilcox.test(c" + replaceBrackets(laListNonBuggy.toString()) + ",c" + 
					replaceBrackets(laListBuggy.toString()) + ")$p.value")).asDouble();
			
			System.out.println("***** " + pvalue);
			System.out.println("LA: " + laListNonBuggy);
			if(pvalue >= 0.05){
				x = re.eval("median(c" + replaceBrackets(laListNonBuggy.toString()) + ")");
				System.out.println(x.asDouble());
				metrics.setLaNonBuggyMedian(x.asDouble());
				x = re.eval("median(c" + replaceBrackets(laListBuggy.toString()) + ")");
				metrics.setLaBuggyMedian(x.asDouble());
			} else {
				metrics.setLaNonBuggyMedian(-1);
				metrics.setLaBuggyMedian(-1);
			}
			
			/* LD */
			pvalue = (re.eval("wilcox.test(c" + replaceBrackets(ldListNonBuggy.toString()) + ",c" + 
					replaceBrackets(ldListBuggy.toString()) + ")$p.value")).asDouble();
			
			System.out.println("***** " + pvalue);
			if(pvalue >= 0.05){
				x = re.eval("median(c" + replaceBrackets(ldListNonBuggy.toString()) + ")");
				metrics.setLdNonBuggyMedian(x.asDouble());
				x = re.eval("median(c" + replaceBrackets(ldListBuggy.toString()) + ")");
				metrics.setLdBuggyMedian(x.asDouble());
			} else {
				metrics.setLdNonBuggyMedian(-1);
				metrics.setLdBuggyMedian(-1);
			}
			
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
