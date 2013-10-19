package analyzer;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used to access the postgresql database 
 * @author toffer
 *
 */
public class DatabaseAccess {

	String commitDbUrl = null;
	Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
     
    String user = null;
    String password = null;

	
	public DatabaseAccess(String server, String repo, String user, String password){
		this.commitDbUrl = "jdbc:postgresql://" + server + "/" + repo;
		this.user = user;
		this.password = password;
	}
	
	/** 
	 * Retrieve all commits from the commit database sorted by the Unix time stamp
	 * of author in descending order
	 * 
	 * @return an array list of all commits in the repo.
	 */
	public ArrayList<Commit> getAllCommits() {
		ArrayList<Commit> commits = new ArrayList<Commit>();
		try {
			 con = DriverManager.getConnection(commitDbUrl, user, password);
	         pst = con.prepareStatement("SELECT * FROM commits");
	         rs = pst.executeQuery();
	         
	         // iterate through all the records in the table
	         while (rs.next()) {
	        	 
	        	 String commitHash = rs.getString(1);
	        	 String treeHash = rs.getString(10);
	        	 String message = rs.getString(17);
	        	 String authorName = rs.getString(7);
	        	 String unixTimeStamp = rs.getString(11);
	        	 
	        	 Commit commit = new Commit(commitHash,treeHash,message,authorName,unixTimeStamp);
	        	 commits.add(commit);
	        
	            }
	      
	         
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseAccess.class.getName());
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
                Logger lgr = Logger.getLogger(DatabaseAccess.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
		}
		
		Collections.sort(commits, Collections.reverseOrder());
		return commits;
	}
	
	/**
	 * Mark a commit as bug inducing in the commits table.
	 * @param commit Commit		A bug inducing commit
	 */
	public void markAsBugInducing(Commit commit){
		
		try{
			con = DriverManager.getConnection(commitDbUrl, user, password);
	        pst = con.prepareStatement("UPDATE commits SET contains_bug = 'true' WHERE commit_hash = '" + 
	        								commit.getCommitHash() + "'");
	        pst.executeUpdate();
		} catch (SQLException ex){
			Logger lgr = Logger.getLogger(DatabaseAccess.class.getName());
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
                Logger lgr = Logger.getLogger(DatabaseAccess.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
		}
		
	}
}
