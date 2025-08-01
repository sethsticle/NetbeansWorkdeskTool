package Backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author sethsticle
 */

public class Connect 
{
    public static Connection makeConnection()
    {
        String dbURL = "jdbc:ucanaccess://TestDatabase.accdb";
        Connection conn = null;
        
        try
        {
            conn = DriverManager.getConnection(dbURL);
            
        }
        catch(SQLException e)
        {
            System.out.println("Error in Connect method (e):\n"+e);
            try
            {
                if (conn != null && !conn.isClosed()) 
                {
                    try{
                        conn.close();
                    } catch(SQLException e2)
                    {
                        System.out.println("Error in third level (e2) try-catch, connect method.\n"+e2);
                    }
                }
            } catch(SQLException e3)
            {
                System.out.println("Error in second level (e3) try-catch in connect method.\n"+e3);
            }
            finally {
                return null;
            }
        }
        return conn;
        
    }
    
    
}

