/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Managers;

import Backend.Connect;
import Objects.UpcomingDates;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author SETH.H
 */
public class UpcomingDateManager 
{
    private List<UpcomingDates> dates;
    private static final String TABLE_NAME="tblUpcomingDates";
    
    public UpcomingDateManager(){
        dates = new ArrayList<>();
        loadDates();
    }
    
    
    public List<UpcomingDates> getDates() {
        return dates;
    }
    
    public void loadDates() {
        dates.clear(); // Clear previous entries

        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY grade ASC, test_date ASC";
        
        try (
             Connection conn = Connect.makeConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("ID");
                String grade = rs.getString("Grade");
                String testInfo = rs.getString("Test_Info");


                Date testDateSql = rs.getDate("test_date");// Convert java.sql.Date to string format (for table display)
                String testDate = new SimpleDateFormat("dd-MMM").format(testDateSql);

                
                UpcomingDates date = new UpcomingDates(id, grade, testInfo, testDate);
                dates.add(date);
            }

        } catch (SQLException e) {
            System.out.println("Error loading upcoming dates: " + e.getMessage());
        }
    } 
    
    public boolean deleteDateById(String id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        try (Connection conn = Connect.makeConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting record: " + e.getMessage());
            return false;
        }
    }
    
     public boolean addDate(String grade, String testInfo, String testDateStr) {
        String sql = "INSERT INTO " + TABLE_NAME + " (grade, test_info, test_date) VALUES (?, ?, ?)";

        try (Connection conn = Connect.makeConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) 
        {

            // Convert string to java.sql.Date
            java.util.Date parsed = new SimpleDateFormat("yyyy-MM-dd").parse(testDateStr);
            java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());

            stmt.setString(1, grade);
            stmt.setString(2, testInfo);
            stmt.setDate(3, sqlDate);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error adding date: " + e.getMessage());
            return false;
        }
    }
}
