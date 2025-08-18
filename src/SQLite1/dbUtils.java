 
package SQLite1;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author SETH.H
 */
public class dbUtils {

    protected static final String dbURL = "jdbc:sqlite:Student.db";
    
    // --- CONNECTION ---
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbURL);
    }
    
    // --- UPDATE / INSERT / DELETE ---
    public static boolean updateDB(String sql, Object... params) {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            setParameters(stmt, params);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("[DBUtils] Update failed: " + e.getMessage());
            return false;
        }
    }
    
    // --- SELECT (returns list of maps) ---
    public static List<Map<String, Object>> readDB(String sql, Object... params) {
       
        List<Map<String, Object>> results = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            setParameters(stmt, params);

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int colCount = meta.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = meta.getColumnLabel(i);
                        Object value = rs.getObject(i);
                        row.put(colName, value);
                    }
                    results.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("[DBUtils] Read failed: " + e.getMessage());
        }
        return results;
    }
    
    
    
    //--- PARAMETER BINDING ---///
    private static void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];

            if (param == null) {
                stmt.setObject(i + 1, null);
            } else if (param instanceof Integer) {
                stmt.setInt(i + 1, (Integer) param);
            } else if (param instanceof String) {
                stmt.setString(i + 1, (String) param);
            } else if (param instanceof Double) {
                stmt.setDouble(i + 1, (Double) param);
            } else if (param instanceof Boolean) {
                stmt.setBoolean(i + 1, (Boolean) param);
            } else if (param instanceof java.util.Date) {
                stmt.setTimestamp(i + 1, new java.sql.Timestamp(((java.util.Date) param).getTime()));
            } else {
                stmt.setObject(i + 1, param); // fallback for other types
            }
        }
    }
}
