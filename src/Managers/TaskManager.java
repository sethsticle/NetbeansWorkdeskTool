package Managers;

import Objects.Task;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

public class TaskManager {
    private List<Task> tasks;
    private final int MAX_TASKS = 10;
    private final String FILE_PATH = "todo.txt";
    
    public TaskManager() {
        tasks = new ArrayList<>();
        loadTasks();
    }
    
    public boolean addTask(String description) {
        if (tasks.size() >= MAX_TASKS) {
            return false;
        }
        
        Task newTask = new Task(description);
        tasks.add(newTask);
        saveTasks();
        return true;
    }
    
    public void markTaskCompleted(int index, boolean completed) {
        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).setCompleted(completed);
            saveTasks();
        }
    }
    
    public void deleteCompletedTasks() {
        tasks.removeIf(Task::isCompleted);
        saveTasks();
    }
    
    public int getTaskCount() {
        return tasks.size();
    }
    
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks); // Return a copy to prevent external modification
    }
    
    public int getMaxTasks() {
        return MAX_TASKS;
    }
    
    private void loadTasks() {
        tasks.clear();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = Task.fromFileString(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
    }
    
    public void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Task task : tasks) {
                writer.write(task.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }
    
    // Helper method to extract original task text from display format
    public static String extractOriginalTask(String displayedText) {
        if (displayedText.matches("\\d{2}/\\d{2} - .*")) {
            return displayedText.substring(displayedText.indexOf(" - ") + 3);
        }
        return displayedText;
    }
}