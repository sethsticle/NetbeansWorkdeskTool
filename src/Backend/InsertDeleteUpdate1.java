package Backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Class for executing INSERT, DELETE, and UPDATE statements safely.
 * 
 * HOW TO USE THIS: 
 * 
String insertQuery = "INSERT INTO tblUsers (email, password) VALUES (?, ?)";
Object[] values = { "user@example.com", "securepassword123" };

InsertDeleteUpdate1.writeData(insertQuery, values, "User successfully registered!");
 */
public class InsertDeleteUpdate1 {

    public static void writeData(String sqlQuery, Object[] values, String message) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Connect.makeConnection();
            stmt = conn.prepareStatement(sqlQuery);

            // Set values dynamically
            for (int i = 0; i < values.length; i++) {
                stmt.setObject(i + 1, values[i]); // Parameter indexes start from 1
            }

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0 && !message.isEmpty()) {
                JOptionPane.showMessageDialog(null, message);
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception in writeData():\n" + e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e2) {
                System.out.println("Error closing resources in writeData():\n" + e2);
            }
        }
    }
}
//
//package Backend;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;
//import javax.swing.JOptionPane;
//
//
///**
// *
// * @author 
// */
//
//
//public class InsertDeleteUpdate1 
//{
//    public static void writeData(String sqlQuery, String message)
//    {
//        Connection conn = null;
//        PreparedStatement smt = null;
//        
//        try
//        {
//            conn = Connect.makeConnection();
//            smt = conn.createStatement();
//            smt.executeUpdate(sqlQuery);
//            
//            if (!message.equals("")) 
//            {
//                JOptionPane.showMessageDialog(null, message);
//            }
//            
//        }
//        catch(SQLException e)
//        {
//            System.out.println("SQLExcept error in writeData>>InsertUpdate...(e)\n"+e);
//        } 
//        finally
//        {
//            try
//            {
//                if (conn != null && !conn.isClosed()) 
//                {
//                    conn.close();
//                }
//            } catch(Exception e2)
//            {
//                System.out.println("Error in conn.close() (e2) writeData>>InsertUpdate...\n"+e2);
//            }
//        }
//    }
//    
//    
//    
//}

