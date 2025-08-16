
package SQLite1;

import java.sql.*;

/**
 *
 * @author SETH.H
 */
public class DBConnection {
    
    Connection con = null;
    
    public static Connection connectToDB(){
        
        try{
                Class.forName("org.sqlite.JDBC");
                Connection con = DriverManager.getConnection("jdbc:sqlite:Student.db");
                System.out.println("Connection Success");
                return con;
        }
        catch(Exception e){
            System.err.println("Catch HIT Connection Failed To Student.db");
            return null;
        }
    }
    
}
