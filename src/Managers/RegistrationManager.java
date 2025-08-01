package Managers;

import Backend.Connect;
import Objects.Registration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author SETH.H
 */
public class RegistrationManager {

    private static final String TABLE_NAME = "tblRegistration";
    private static final int MAX_DAYS_TO_DISPLAY = 5; // Only show the most recent 5 days

    /**
     * Get current date in the format used for column names (YYYY_MM_DD)
     */
    public static String getCurrentDateColumn() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
        return sdf.format(new Date());
    }

    /**
     * Convert date column name to Date object
     */
    private static Date columnNameToDate(String columnName) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
            return sdf.parse(columnName);
        } catch (ParseException e) {
            System.out.println("Error parsing date column: " + e);
            return null;
        }
    }

    /**
     * Check if the column for today's date exists in the table
     */
    public static boolean dateColumnExists() {
        String dateColumn = getCurrentDateColumn();
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = Connect.makeConnection();
            rs = conn.getMetaData().getColumns(null, null, TABLE_NAME, dateColumn);
            return rs.next(); // If there's a result, the column exists
        } catch (SQLException e) {
            System.out.println("Error checking if date column exists: " + e);
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e);
            }
        }
    }

    /**
     * Create a new column for today's date if it doesn't exist
     */
    public static void createDateColumnIfNeeded() {
        if (!dateColumnExists()) {
            String dateColumn = getCurrentDateColumn();
            String alterQuery = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN [" + dateColumn + "] TEXT";

            Connection conn = null;
            Statement stmt = null;

            try {
                conn = Connect.makeConnection();
                stmt = conn.createStatement();
                stmt.executeUpdate(alterQuery);
                System.out.println("Created new date column: " + dateColumn);
            } catch (SQLException e) {
                System.out.println("Error creating date column: " + e);
                System.out.println("Using query: "+ alterQuery);
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    System.out.println("Error closing resources: " + e);
                }
            }
        }
    }

    /**
     * Get all date columns from the table
     */
    public static List<String> getDateColumns() {
        List<String> dateColumns = new ArrayList<>();
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = Connect.makeConnection();
            rs = conn.getMetaData().getColumns(null, null, TABLE_NAME, null);
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                // Only include columns that match date format (YYYY_MM_DD)
                if (columnName.matches("\\d{4}_\\d{2}_\\d{2}")) {
                    dateColumns.add(columnName);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting date columns: " + e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e);
            }
        }

        // Sort date columns by descending date (newest first)
        Collections.sort(dateColumns, new Comparator<String>() {
            @Override
            public int compare(String col1, String col2) {
                Date date1 = columnNameToDate(col1);
                Date date2 = columnNameToDate(col2);
                if (date1 == null || date2 == null) {
                    return 0;
                }
                // Reverse order - newest first
                return date2.compareTo(date1);
            }
        });

        // Limit to MAX_DAYS_TO_DISPLAY most recent days using subList in case they are starting out the app
        if (dateColumns.size() > MAX_DAYS_TO_DISPLAY) {
            dateColumns = dateColumns.subList(0, MAX_DAYS_TO_DISPLAY);
        }

        return dateColumns;
    }

    /**
     * Get all learners with their attendance status for the recent dates
     * (limited to MAX_DAYS_TO_DISPLAY)
     */
    public static List<Registration> getAllLearners() {
        List<Registration> learners = new ArrayList<>();
        createDateColumnIfNeeded(); // Ensure today's column exists

        // Get most recent date columns (limited to MAX_DAYS_TO_DISPLAY)
        List<String> recentDates = getDateColumns();

        // If no date columns found, just return empty list
        if (recentDates.isEmpty()) {
            return learners;
        }

        // Build query to include only the recent date columns
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT LearnerID, LearnerName");

        for (String dateColumn : recentDates) {
            queryBuilder.append(", [").append(dateColumn).append("]");
        }

        queryBuilder.append(" FROM ").append(TABLE_NAME);

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Connect.makeConnection();
            stmt = conn.prepareStatement(queryBuilder.toString());
            rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("LearnerID");
                String name = rs.getString("LearnerName");

                Registration reg = new Registration(id, name);

                // Add attendance records for each recent date
                for (String dateColumn : recentDates) {
                    String status = rs.getString(dateColumn);
                    reg.addAttendanceRecord(dateColumn, status == null ? "Present" : status);
                }

                learners.add(reg);
            }
        } catch (SQLException e) {
            System.out.println("Error getting learners: " + e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e);
            }
        }

        return learners;
    }

    /**
     * Get list of recent dates in a human-readable format
     */
    public static List<String> getRecentDatesFormatted() {
        List<String> dateColumns = getDateColumns();
        List<String> formattedDates = new ArrayList<>();
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy_MM_dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM d");

        for (String dateColumn : dateColumns) {
            try {
                Date date = inputFormat.parse(dateColumn);
                formattedDates.add(outputFormat.format(date));
            } catch (ParseException e) {
                formattedDates.add(dateColumn); // Fallback to original format
            }
        }

        return formattedDates;
    }

    /**
     * Mark selected learners as absent for the current date
     */
    public static void markLearnerAbsent(List<Integer> learnerIds) {
        if (learnerIds.isEmpty()) {
            return;
        }

        String dateColumn = getCurrentDateColumn();
        createDateColumnIfNeeded(); // Ensure today's column exists

        // Prepare the SQL query with parameters for each learner ID
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("UPDATE ").append(TABLE_NAME)
                .append(" SET [").append(dateColumn).append("] = 'Absent'")
                .append(" WHERE LearnerID IN (");

        for (int i = 0; i < learnerIds.size(); i++) {
            queryBuilder.append("?");
            if (i < learnerIds.size() - 1) {
                queryBuilder.append(",");
            }
        }
        queryBuilder.append(")");

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Connect.makeConnection();
            stmt = conn.prepareStatement(queryBuilder.toString());

            // Set the learner IDs as parameters
            for (int i = 0; i < learnerIds.size(); i++) {
                stmt.setInt(i + 1, learnerIds.get(i));
            }

            int rowsAffected = stmt.executeUpdate();
            System.out.println("Marked " + rowsAffected + " learners as absent for " + dateColumn);
        } catch (SQLException e) {
            System.out.println("Error marking learners as absent: " + e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e);
            }
        }
    }

    /**
     * Create the registration table if it doesn't exist
     */
    public static void initializeRegistrationTable() {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = Connect.makeConnection();
            stmt = conn.createStatement();

            // Check if table exists
            ResultSet tables = conn.getMetaData().getTables(null, null, TABLE_NAME, null);
            if (!tables.next()) {
                // Table doesn't exist, create it
                String createTableSQL = "CREATE TABLE " + TABLE_NAME + " ("
                        + "LearnerID INT PRIMARY KEY, "
                        + "LearnerName TEXT NOT NULL)";
                stmt.executeUpdate(createTableSQL);
                System.out.println("Created table: " + TABLE_NAME);
            }
            // Add some sample data if needed
            String[] sampleNames = {"John Smith", "Emily Johnson", "Michael Brown",
                "Sarah Davis", "David Wilson", "Jessica Moore",
                "Christopher Taylor", "Jennifer Anderson", "Matthew Thomas",
                "Elizabeth Jackson", "Daniel White", "Lisa Harris",
                "Andrew Martin", "Rebecca Thompson", "Ryan Garcia"};

            for (int i = 0; i < sampleNames.length; i++) {
                // Check if this ID already exists
                String checkSQL = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE LearnerID = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSQL);
                checkStmt.setInt(1, i + 1);
                ResultSet rs = checkStmt.executeQuery();
                rs.next();
                int exists = rs.getInt(1);
                checkStmt.close();

                // Only insert if ID doesn't exist
                if (exists == 0) {
                    String insertSQL = "INSERT INTO " + TABLE_NAME + " (LearnerID, LearnerName) VALUES (?, ?)";
                    PreparedStatement pstmt = conn.prepareStatement(insertSQL);
                    pstmt.setInt(1, i + 1);
                    pstmt.setString(2, sampleNames[i]);
                    pstmt.executeUpdate();
                    pstmt.close();
                }
            }
            System.out.println("Added sample learners to the registration table");

            // Create today's column
            createDateColumnIfNeeded();

        } catch (SQLException e) {
            System.out.println("Error initializing registration table: " + e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e);
            }
        }
    }



public static void markLearnerPresent(List<Integer> learnerIds) {
    if (learnerIds.isEmpty()) {
        return;
    }
    String dateColumn = getCurrentDateColumn();
    createDateColumnIfNeeded(); // Ensure today's column exists

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("UPDATE ").append(TABLE_NAME)
            .append(" SET [").append(dateColumn).append("] = 'Present'")
            .append(" WHERE LearnerID IN (");
    for (int i = 0; i < learnerIds.size(); i++) {
        queryBuilder.append("?");
        if (i < learnerIds.size() - 1) {
            queryBuilder.append(",");
        }
    }
    queryBuilder.append(")");

    try (Connection conn = Connect.makeConnection();
         PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString())) {
        for (int i = 0; i < learnerIds.size(); i++) {
            stmt.setInt(i + 1, learnerIds.get(i));
        }
        int rowsAffected = stmt.executeUpdate();
        System.out.println("Marked " + rowsAffected + " learners as present for " + dateColumn);
    } catch (SQLException e) {
        System.out.println("Error marking learners as present: " + e);
    }
}
//
//public static void markLearnerAbsent(List<Integer> learnerIds) {
//    if (learnerIds.isEmpty()) {
//        return;
//    }
//    String dateColumn = getCurrentDateColumn();
//    createDateColumnIfNeeded(); // Ensure today's column exists
//
//    StringBuilder queryBuilder = new StringBuilder();
//    queryBuilder.append("UPDATE ").append(TABLE_NAME)
//            .append(" SET [").append(dateColumn).append("] = 'Absent'")
//            .append(" WHERE LearnerID IN (");
//    for (int i = 0; i < learnerIds.size(); i++) {
//        queryBuilder.append("?");
//        if (i < learnerIds.size() - 1) {
//            queryBuilder.append(",");
//        }
//    }
//    queryBuilder.append(")");
//
//    try (Connection conn = Connect.makeConnection();
//         PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString())) {
//        for (int i = 0; i < learnerIds.size(); i++) {
//            stmt.setInt(i + 1, learnerIds.get(i));
//        }
//        int rowsAffected = stmt.executeUpdate();
//        System.out.println("Marked " + rowsAffected + " learners as present for " + dateColumn);
//    } catch (SQLException e) {
//        System.out.println("Error marking learners as present: " + e);
//    }
//}
}


//public class RegistrationManager {
//    
//    private static final String TABLE_NAME = "tblRegistration";
//    private static final int MAX_DAYS_TO_DISPLAY = 5; // Only show the most recent 5 days
//    
//    /**
//     * Get current date in the format used for column names (YYYY_MM_DD)
//     */
//    public static String getCurrentDateColumn() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
//        return sdf.format(new Date());
//    }
//    
//    /**
//     * Convert date column name to Date object
//     */
//    private static Date columnNameToDate(String columnName) {
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
//            return sdf.parse(columnName);
//        } catch (ParseException e) {
//            System.out.println("Error parsing date column: " + e);
//            return null;
//        }
//    }
//    
//    
//    /**
//     * Check if the column for today's date exists in the table
//     */
//   public static boolean dateColumnExists() {
//        String dateColumn = getCurrentDateColumn();
//        Connection conn = null;
//        ResultSet rs = null;
//        
//        try {
//            conn = Connect.makeConnection();
//            ResultSetMetaData metaData = conn.getMetaData().getColumns(null, null, TABLE_NAME, dateColumn);
//            rs = metaData.getColumns(null, null, TABLE_NAME, dateColumn);
//            return rs.next(); // If there's a result, the column exists
//        } catch (SQLException e) {
//            System.out.println("Error checking if date column exists: " + e);
//            return false;
//        } finally {
//            try {
//                if (rs != null) rs.close();
//                if (conn != null) conn.close();
//            } catch (SQLException e) {
//                System.out.println("Error closing resources: " + e);
//            }
//        }
//    }
//    
//    /**
//     * Create a new column for today's date if it doesn't exist
//     */
//    public static void createDateColumnIfNeeded() {
//        if (!dateColumnExists()) {
//            String dateColumn = getCurrentDateColumn();
//            String alterQuery = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + dateColumn + " TEXT DEFAULT 'Present'";
//            
//            Connection conn = null;
//            Statement stmt = null;
//            
//            try {
//                conn = Connect.makeConnection();
//                stmt = conn.createStatement();
//                stmt.executeUpdate(alterQuery);
//                System.out.println("Created new date column: " + dateColumn);
//            } catch (SQLException e) {
//                System.out.println("Error creating date column: " + e);
//            } finally {
//                try {
//                    if (stmt != null) stmt.close();
//                    if (conn != null) conn.close();
//                } catch (SQLException e) {
//                    System.out.println("Error closing resources: " + e);
//                }
//            }
//        }
//    }
//   
//    
//    /**
//     * Get all learners with their attendance status for the current date
//     */
//    public static List<Registration> getAllLearners() {
//        List<Registration> learners = new ArrayList<>();
//        String dateColumn = getCurrentDateColumn();
//        createDateColumnIfNeeded(); // Ensure today's column exists
//        
//        String query = "SELECT LearnerID, LearnerName, " + dateColumn + " FROM " + TABLE_NAME;
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        
//        try {
//            conn = Connect.makeConnection();
//            stmt = conn.prepareStatement(query);
//            rs = stmt.executeQuery();
//            
//            while (rs.next()) {
//                int id = rs.getInt("LearnerID");
//                String name = rs.getString("LearnerName");
//                String status = rs.getString(dateColumn);
//                
//                Registration reg = new Registration(id, name);
//                reg.addAttendanceRecord(dateColumn, status == null ? "Present" : status);
//                learners.add(reg);
//            }
//        } catch (SQLException e) {
//            System.out.println("Error getting learners: " + e);
//        } finally {
//            try {
//                if (rs != null) rs.close();
//                if (stmt != null) stmt.close();
//                if (conn != null) conn.close();
//            } catch (SQLException e) {
//                System.out.println("Error closing resources: " + e);
//            }
//        }
//        
//        return learners;
//    }
//    
//    /**
//     * Mark selected learners as absent for the current date
//     */
//    public static void markLearnerAbsent(List<Integer> learnerIds) {
//        if (learnerIds.isEmpty()) {
//            return;
//        }
//        
//        String dateColumn = getCurrentDateColumn();
//        createDateColumnIfNeeded(); // Ensure today's column exists
//        
//        // Prepare the SQL query with parameters for each learner ID
//        StringBuilder queryBuilder = new StringBuilder();
//        queryBuilder.append("UPDATE ").append(TABLE_NAME)
//                    .append(" SET ").append(dateColumn).append(" = 'Absent'")
//                    .append(" WHERE LearnerID IN (");
//        
//        for (int i = 0; i < learnerIds.size(); i++) {
//            queryBuilder.append("?");
//            if (i < learnerIds.size() - 1) {
//                queryBuilder.append(",");
//            }
//        }
//        queryBuilder.append(")");
//        
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        
//        try {
//            conn = Connect.makeConnection();
//            stmt = conn.prepareStatement(queryBuilder.toString());
//            
//            // Set the learner IDs as parameters
//            for (int i = 0; i < learnerIds.size(); i++) {
//                stmt.setInt(i + 1, learnerIds.get(i));
//            }
//            
//            int rowsAffected = stmt.executeUpdate();
//            System.out.println("Marked " + rowsAffected + " learners as absent for " + dateColumn);
//        } catch (SQLException e) {
//            System.out.println("Error marking learners as absent: " + e);
//        } finally {
//            try {
//                if (stmt != null) stmt.close();
//                if (conn != null) conn.close();
//            } catch (SQLException e) {
//                System.out.println("Error closing resources: " + e);
//            }
//        }
//    }
//    
//    /**
//     * Create the registration table if it doesn't exist
//     */
//    public static void initializeRegistrationTable() {
//        Connection conn = null;
//        Statement stmt = null;
//        
//        try {
//            conn = Connect.makeConnection();
//            stmt = conn.createStatement();
//            
//            // Check if table exists
//            ResultSet tables = conn.getMetaData().getTables(null, null, TABLE_NAME, null);
//            if (!tables.next()) {
//                // Table doesn't exist, create it
//                String createTableSQL = "CREATE TABLE " + TABLE_NAME + " (" +
//                                       "LearnerID INT PRIMARY KEY, " +
//                                       "LearnerName TEXT NOT NULL)";
//                stmt.executeUpdate(createTableSQL);
//                System.out.println("Created table: " + TABLE_NAME);
//                
//                // Add some sample data if needed
//                String[] sampleNames = {"John Smith", "Emily Johnson", "Michael Brown", 
//                                       "Sarah Davis", "David Wilson", "Jessica Moore", 
//                                       "Christopher Taylor", "Jennifer Anderson", "Matthew Thomas",
//                                       "Elizabeth Jackson", "Daniel White", "Lisa Harris",
//                                       "Andrew Martin", "Rebecca Thompson", "Ryan Garcia"};
//                
//                for (int i = 0; i < sampleNames.length; i++) {
//                    String insertSQL = "INSERT INTO " + TABLE_NAME + " (LearnerID, LearnerName) VALUES (?, ?)";
//                    PreparedStatement pstmt = conn.prepareStatement(insertSQL);
//                    pstmt.setInt(1, i + 1);
//                    pstmt.setString(2, sampleNames[i]);
//                    pstmt.executeUpdate();
//                    pstmt.close();
//                }
//                
//                System.out.println("Added sample learners to the registration table");
//            }
//            
//            // Create today's column
//            createDateColumnIfNeeded();
//            
//        } catch (SQLException e) {
//            System.out.println("Error initializing registration table: " + e);
//        } finally {
//            try {
//                if (stmt != null) stmt.close();
//                if (conn != null) conn.close();
//            } catch (SQLException e) {
//                System.out.println("Error closing resources: " + e);
//            }
//        }
//    }
//}
