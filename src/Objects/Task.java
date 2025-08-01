
package Objects;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {
    private String description;
    private boolean completed;
    private String creationDate; // Format: yyyy-MM-dd
    
    public Task(String description, boolean completed, String creationDate) {
        this.description = description;
        this.completed = completed;
        this.creationDate = creationDate;
    }
    
    public Task(String description) {
        this(description, false, getCurrentDate());
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    public String getCreationDate() {
        return creationDate;
    }
    
    public String getFormattedDisplayText() {
        return formatTaskWithDate(description, creationDate);
    }
    
    private static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }
    
    private String formatTaskWithDate(String taskText, String dateStr) {
        try {
            // Parse the date from yyyy-MM-dd format
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inputFormat.parse(dateStr);
            // Format to DD/MM
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM");
            String formattedDate = outputFormat.format(date);
            return formattedDate + " - " + taskText;
        } catch (Exception e) {
            // If there's any error parsing the date, just return the original task
            return taskText;
        }
    }
    
    // For file storage
    public String toFileString() {
        String status = completed ? "1" : "0";
        return description + "|" + status + "|" + creationDate;
    }
    
    // For creating a Task from file data
    public static Task fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length >= 3) {
            String taskText = parts[0];
            boolean isCompleted = parts[1].equals("1");
            String date = parts[2];
            return new Task(taskText, isCompleted, date);
        }
        return null;
    }
}