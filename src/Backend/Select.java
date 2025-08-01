package Backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class for executing SELECT queries safely.
 * 
 * 
 * HOW TO USE THIS: 
 
String selectQuery = "SELECT * FROM tblUsers WHERE email = ?";
Object[] values = { "user@example.com" };

ResultSet rs = Select.getResultSet(selectQuery, values);

try {
    while (rs != null && rs.next()) {
        System.out.println("User found: " + rs.getString("email"));
    }
} catch (SQLException e) {
    System.out.println("Error reading result set: " + e);
}
* 
 */
public class Select {

    public static ResultSet getResultSet(String sqlQuery, Object[] values) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Connect.makeConnection();
            stmt = conn.prepareStatement(sqlQuery);

            // Set values dynamically
            for (int i = 0; i < values.length; i++) {
                stmt.setObject(i + 1, values[i]);
            }

            rs = stmt.executeQuery();
            return rs;

        } catch (SQLException e) {
            System.out.println("Error in Select.getResultSet():\n" + e);
        }

        return null;
    }
}


//package Backend;
//
//import java.sql.ResultSet;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;
//import javax.swing.JOptionPane;
//
///**
// *
// * @author 
// */
//
//
//public class Select 
//{
//    public static ResultSet getResultSet(String sqlQuery, String message)
//    {
//        Connection conn = null;
//        Statement smt = null;
//        ResultSet rs = null;
//        try
//        {
//            conn = Connect.makeConnection();
//            smt = conn.createStatement();
//            rs = smt.executeQuery(sqlQuery);
//            
//            if (!message.equals("")) 
//            {
//                JOptionPane.showMessageDialog(null, message);
//            }
//        
//        return rs;   
//        } catch(Exception e)
//        {
//            System.out.println("Error in exception in Select method>>(e)\n"+e);
//            try
//            {
//                if (conn != null && !conn.isClosed()) 
//                {
//                    try
//                    { 
//                        conn.close();
//                    } catch(SQLException e3)
//                            {
//                             System.out.println("SQLException in getResultSet>>Select>>(e3)\n"+e3);
//                            }
//                }
//            } catch(SQLException e2)
//            {
//                System.out.println("SQLException in getResultSet>>Select>>(e2)\n"+e2);
//            }
//        }
//        return null;
//    }
//}
