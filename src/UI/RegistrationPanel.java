
package UI;

import Objects.Registration;
import Managers.RegistrationManager;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 * Registration Panel UI Component
 */
public class RegistrationPanel extends JPanel {
    
    private JTable tblRegistration;
    private DefaultTableModel tableModel;
    private JButton btnMarkAbsent;
    private JButton btnRefresh;
    private JLabel lblDate;
    private JLabel lblTitle;
    private JScrollPane scrollPane;
    
    public RegistrationPanel() {
        initializeComponents();
        loadRegistrationData();
    }
    
    private void initializeComponents() {
        setLayout(null); // Using absolute positioning for simplicity
        setBackground(new Color(35, 45, 63)); // Match your app's color scheme
        
        // Title label
        lblTitle = new JLabel("Learner Registration");
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(20, 20, 250, 30);
        add(lblTitle);
        
        // Current date label
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy");
        String dateStr = sdf.format(new Date());
        lblDate = new JLabel("Date: " + dateStr);
        lblDate.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblDate.setForeground(Color.WHITE);
        lblDate.setBounds(20, 50, 250, 20);
        add(lblDate);
        
        // Create table model with non-editable cells except for the status column
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        
        // Add columns
        tableModel.addColumn("ID");
        tableModel.addColumn("Learner Name");
        tableModel.addColumn("Today's Status");
        
        // Create table and configure it
        tblRegistration = new JTable(tableModel);
        tblRegistration.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tblRegistration.setRowHeight(25);
        tblRegistration.getTableHeader().setReorderingAllowed(false);
        
        // Create scroll pane for the table
        scrollPane = new JScrollPane(tblRegistration);
        scrollPane.setBounds(20, 80, 500, 300);
        add(scrollPane);
        
        // Mark Absent button
        btnMarkAbsent = new JButton("Mark Selected as Absent");
        btnMarkAbsent.setBounds(20, 390, 200, 30);
        btnMarkAbsent.addActionListener((ActionEvent e) -> {
            markSelectedLearnersAbsent();
        });
        add(btnMarkAbsent);
        
        // Refresh button
        btnRefresh = new JButton("Refresh");
        btnRefresh.setBounds(230, 390, 100, 30);
        btnRefresh.addActionListener((ActionEvent e) -> {
            loadRegistrationData();
        });
        add(btnRefresh);
    }
    
    /**
     * Load registration data from the database
     */
    public final void loadRegistrationData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Initialize the registration table if needed
        RegistrationManager.initializeRegistrationTable();
        
        // Get all learners
        List<Registration> learners = RegistrationManager.getAllLearners();
        String currentDate = RegistrationManager.getCurrentDateColumn();
        
        // Add learners to the table
        for (Registration learner : learners) {
            String status = learner.getAttendanceStatus(currentDate);
            tableModel.addRow(new Object[]{
                learner.getLearnerId(),
                learner.getLearnerName(),
                status
            });
        }
        
        // Refresh UI
        tblRegistration.repaint();
    }
    
    /**
     * Mark selected learners as absent
     */
    private void markSelectedLearnersAbsent() {
        int[] selectedRows = tblRegistration.getSelectedRows();
        
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, 
                "Please select at least one learner.", 
                "No Selection", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        List<Integer> learnerIds = new ArrayList<>();
        
        for (int row : selectedRows) {
            int learnerId = (int) tableModel.getValueAt(row, 0);
            String currentStatus = (String) tableModel.getValueAt(row, 2);
            
            // Only update if not already marked as absent
            if (!"Absent".equals(currentStatus)) {
                learnerIds.add(learnerId);
                tableModel.setValueAt("Absent", row, 2);
            }
        }
        
        if (!learnerIds.isEmpty()) {
            // Update the database
            RegistrationManager.markLearnerAbsent(learnerIds);
            JOptionPane.showMessageDialog(this, 
                learnerIds.size() + " learner(s) marked as absent.", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "All selected learners are already marked as absent.", 
                "No Changes", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}