package Managers;

import Backend.Select;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;

/**
 *
 * @author SETH.H
 */
public class DBManager {

    final String dbURL = "jdbc:ucanaccess://TestDatabase.accdb";
    private final String[] allowedTables = {"tblUsers", "tblStudents", ""}; // Add your tables
     public String SelectAll(String tableName) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        StringBuilder returnMe = new StringBuilder();

        try {
            // Validate table name (prevent injection)
            if (!isValidTableName(tableName)) {
                return "Error: Invalid table name!";
            }

            // Connect to database
            conn = DriverManager.getConnection(dbURL);
            System.out.println("Database connection successful");

            // Execute query
            String sql = "SELECT * FROM " + tableName; // Safe if table name is validated
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            // Retrieve metadata to determine column count
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Print column headers
            for (int i = 1; i <= columnCount; i++) {
                returnMe.append(metaData.getColumnName(i)).append("\t");
            }
            returnMe.append("\n");

            // Print all rows dynamically
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    returnMe.append(rs.getString(i)).append("\t");
                }
                returnMe.append("\n");
            }

        } catch (SQLException ex) {
            System.out.println("Exception in SelectAll(): " + ex);
        } finally {
            // Close resources properly
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources in SelectAll(): " + e);
            }
        }

        return returnMe.toString();
    }

    /**
     * Validates table name against a whitelist of known tables.
     */
    private boolean isValidTableName(String tableName) {
        
        for (String validTable : allowedTables) {
            if (validTable.equalsIgnoreCase(tableName)) {
                return true;
            }
        }
        return false;
    }

    public boolean Login(String email, String password) throws SQLException {

        String selectQuery = "SELECT * FROM tblUsers WHERE email = ? AND password = ?";
        Object[] values = {email, password};

        ResultSet rs = Select.getResultSet(selectQuery, values);

        try {
            while (rs != null && rs.next()) {
                System.out.println("User found: " + rs.getString("email"));
                return true;
            }
        } catch (SQLException e) {
            
            System.out.println("Error reading result set: " + e);
        }
        return false;

    }

}
