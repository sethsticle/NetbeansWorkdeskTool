package Objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Registration class to represent attendance data for a learner
 */
public class Registration {
    private int learnerId;
    private String learnerName;
    private Map<String, String> attendanceRecords; // Date -> Status (Present/Absent)
    
    public Registration(int learnerId, String learnerName) {
        this.learnerId = learnerId;
        this.learnerName = learnerName;
        this.attendanceRecords = new HashMap<>();
    }
    
    public void addAttendanceRecord(String date, String status) {
        attendanceRecords.put(date, status);
    }
    
    // Getters and setters
    public int getLearnerId() {
        return learnerId;
    }
    
    public String getLearnerName() {
        return learnerName;
    }
    
    public Map<String, String> getAttendanceRecords() {
        return attendanceRecords;
    }
    
    public String getAttendanceStatus(String date) {
        return attendanceRecords.getOrDefault(date, "Present"); // Default is Present
    }
}