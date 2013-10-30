package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import analyze.CommitDbAccess;


/**
 * Used to access the metrics table in the database
 * @author toffer
 *
 */
public class MetricDbAccess {
	
	String commitDbUrl = null;
	Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String user = null;
    String password = null;

	
	public MetricDbAccess(String server, String repo, String user, String password){
		this.commitDbUrl = "jdbc:postgresql://" + server + "/" + repo;
		this.user = user;
		this.password = password;
	}
	
 
	public void updateMetric(Metrics metric, String repoName){

		try{
	        
			con = DriverManager.getConnection(commitDbUrl, user, password);
	        pst = con.prepareStatement("UPDATE metrics SET nsbuggy = " + metric.getNsBuggyMedian() + 
	        							",nsnonbuggy = " + metric.getNsNonBuggyMedian() + 
	        							",ndbuggy = " + metric.getNdBuggyMedian() + 
	        							",ndnonbuggy = " + metric.getNdNonBuggyMedian() + 
	        							",nfbuggy = " + metric.getNfBuggyMedian() + 
	        							",nfnonbuggy = " + metric.getNfNonBuggyMedian() +
	        							",entrophyBuggy = " + metric.getEntrophyBuggyMedian() + 
	        							",entrophyNonBuggy = " + metric.getEntrophyNonBuggyMedian() + 
	        							"WHERE repo = '" + repoName + "'");
	        pst.executeUpdate();
		} catch (SQLException ex){
			Logger lgr = Logger.getLogger(CommitDbAccess.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
		} finally {
			try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(CommitDbAccess.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
		}
	}

}
