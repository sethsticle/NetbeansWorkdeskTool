package UI;

import Backend.Select;
import Managers.RegistrationManager;
import Managers.TaskManager;
import Managers.UpcomingDateManager;
import Objects.Registration;
import Objects.Task;
import Objects.UpcomingDates;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author SETH.H
 */
public class A1GUI extends javax.swing.JFrame {

    private JPanel activePanel = null;
    private final Color DEFAULT_COLOR = new Color(35, 45, 63); //  original background color
    private final Color ACTIVE_COLOR = new Color(25, 35, 53);  // A darker shade for active state
    private List<UpcomingDates> upcomingDates_list;
    private File selectedFileTimetable;
    private boolean showingCompletedTasks = false; // Track the state of completed tasks visibility

    private final int MAX_NOTES = 11;
    private int current_notes = 0;

    private final Map<String, JPanel> panels = new HashMap<>();

    private TaskManager taskManager;

    public A1GUI() {
        // System.out.println(getClass().getResource("/icon/angle-left.png")); // Debug path ->Macbook issues on image runtime
        initComponents();  // Ensure components are initialized first
        taskManager = new TaskManager();
        onStartup();
    }

    private void onStartup() {

        loadStoredTimetable();
        loadIcon();
        loadDatesToTable();
        displayTasks(false);
        btnShowCompleted.setText("Show");
        btnDelete.setVisible(false);

        refreshTableData();

        //add hover for all icons and their panels
        addHoverEffect(iconShowHide, labelIconShowHide);
        addHoverEffect(iconHome, labelIconHome);
        addHoverEffect(iconTime, labelIconTime);
        addHoverEffect(iconLearners, labelIconLearners);
        addHoverEffect(iconMarks, labelIconMarks);

        // Add this for debugging needs
        iconShowHide.setName("Show-Hide");
        iconHome.setName("Home");
        iconTime.setName("Time");
        iconLearners.setName("Learners");
        iconMarks.setName("Marks");

        // Add click listeners to all panels
        addClickListener(iconShowHide);
        addClickListener(iconHome);
        addClickListener(iconTime);
        addClickListener(iconLearners);
        addClickListener(iconMarks);

        btnAddTask.addActionListener(evt -> addNewTask());
        btnShowCompleted.addActionListener(evt -> toggleCompletedTasks());
        btnDelete.addActionListener(evt -> deleteCompletedTasks());

        //actionlisteners for the registration panels
        btnMarkPresent.addActionListener(evt -> MarkPresent());
        btnMarkAbsent.addActionListener(evt -> MarkAbsent());

        // Also add existing panels to the map
        panels.put("home", contentPanelHomePage);
        panels.put("registration", contentPanelRegistrationPage);
        panels.put("classList", contentPanelClassLists);
        // Add other panels as needed

        contentPanelHomePage.setVisible(true);
        contentPanelRegistrationPage.setVisible(false);
        contentPanelClassLists.setVisible(false);

        // Set Home as the default active panel
        setActivePanel(iconHome);

        setupTabChangeListener();
    }

    private void showPanel(String panelKey) {
        if (!panels.containsKey(panelKey)) {
            System.out.println("Error: Panel with key '" + panelKey + "' not found in panels map");
            return;
        }

        // Hide all panels first
        for (Object key : panels.keySet()) {
            JPanel panel = (JPanel) panels.get(key);
            panel.setVisible(false);
        }

        // Show only the requested panel
        JPanel panelToShow = (JPanel) panels.get(panelKey);
        panelToShow.setVisible(true);

        System.out.println("Panel '" + panelKey + "' is now active");
    }

    ////////////////////////---------UpcomingDates----------////////////////
        private void loadDatesToTable() {
        UpcomingDateManager DM = new UpcomingDateManager();
        upcomingDates_list = DM.getDates();

        String[] columnNames = {"ID", "Grade", "Test Info", "Test Date"};

        // Create table data
        Object[][] data = new Object[upcomingDates_list.size()][4];
        for (int i = 0; i < upcomingDates_list.size(); i++) {
            UpcomingDates d = upcomingDates_list.get(i);
            data[i][0] = d.getId();
            data[i][1] = d.getGrade();
            data[i][2] = d.getTest_info();
            data[i][3] = d.getTest_date();
        }

        jTBLDates.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    ////////////////////////---------UpcomingDates----------////////////////
    
    
        
    
    ////////////////////////---------Registration----------////////////////
    private void MarkPresent() {
        int[] selectedRows = tblLearners.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(null, "No learners selected!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Integer> learnerIds = new ArrayList<>();
        for (int row : selectedRows) {
            int learnerId = (int) tblLearners.getValueAt(row, 0); // Assuming LearnerID is in column 0
            learnerIds.add(learnerId);
        }

        RegistrationManager.markLearnerPresent(learnerIds);
        JOptionPane.showMessageDialog(null, "Marked selected learners as Present.", "Success", JOptionPane.INFORMATION_MESSAGE);
        refreshTableData(); // Reload the JTable after the update
    }

    private void MarkAbsent() {
        int[] selectedRows = tblLearners.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(null, "No learners selected!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Integer> learnerIds = new ArrayList<>();
        for (int row : selectedRows) {
            int learnerId = (int) tblLearners.getValueAt(row, 0); // Assuming LearnerID is in column 0
            learnerIds.add(learnerId);
        }

        RegistrationManager.markLearnerAbsent(learnerIds);
        JOptionPane.showMessageDialog(null, "Marked selected learners as Absent.", "Success", JOptionPane.INFORMATION_MESSAGE);
        refreshTableData(); // Reload the JTable after the update
    }

    private void refreshTableData() {
        // Get learners with attendance data (already limited to most recent 5 days in RegistrationManager)
        List<Registration> learners = RegistrationManager.getAllLearners();

        // Get the formatted date columns for display
        List<String> dateColumns = RegistrationManager.getRecentDatesFormatted();

        // Create a table model with columns for ID, Name, and the recent dates
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");

        // Add date columns
        for (String date : dateColumns) {
            model.addColumn(date);
        }

        // Populate the table with data
        for (Registration learner : learners) {
            Object[] row = new Object[2 + dateColumns.size()]; // ID, Name + date columns
            row[0] = learner.getLearnerId();
            row[1] = learner.getLearnerName();

            // Add attendance status for each date
            Map<String, String> attendanceRecords = learner.getAttendanceRecords();
            int colIndex = 2; // Start after ID and Name

            for (String dateColumn : RegistrationManager.getDateColumns()) { // Get raw column names
                String status = attendanceRecords.get(dateColumn);
                row[colIndex++] = status != null ? status : "Present"; // Default to Present if null
            }

            model.addRow(row);
        }

        // Apply the model to your JTable
        tblLearners.setModel(model);

        // Optional: Format the table to highlight absences
        formatAttendanceTable();
    }

    /**
     * Optional method to format the attendance table (e.g., highlight absences)
     */
    private void formatAttendanceTable() {
        // Use a custom cell renderer to highlight cells
        tblLearners.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                // Only format the attendance columns (skip ID and Name)
                if (column >= 2 && value != null) {
                    if ("Absent".equals(value.toString())) {
                        c.setBackground(new Color(255, 200, 200)); // Light red for absences
                    } else {
                        c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                    }
                } else {
                    c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                }

                return c;
            }
        });

        // Optional: Adjust column widths
        tblLearners.getColumnModel().getColumn(0).setPreferredWidth(40); // ID column
        tblLearners.getColumnModel().getColumn(1).setPreferredWidth(150); // Name column

        // Make date columns more compact
        for (int i = 2; i < tblLearners.getColumnCount(); i++) {
            tblLearners.getColumnModel().getColumn(i).setPreferredWidth(70);
        }
    }

    ////////////////////////---------Registration----------////////////////

    
    
    ////////////////////////---------Tasks----------////////////////
    private void displayTasks(boolean showCompleted) {
        // Clear existing tasks from panel
        todoPanel.removeAll();

        for (Task task : taskManager.getAllTasks()) {
            JCheckBox taskCheckBox = new JCheckBox(task.getFormattedDisplayText());
            taskCheckBox.setSelected(task.isCompleted());

            // Only show completed tasks if showCompleted is true
            if (task.isCompleted() && !showCompleted) {
                taskCheckBox.setVisible(false);
            }

            taskCheckBox.addActionListener(evt -> {
                if (taskCheckBox.isSelected()) {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Mark task as completed?",
                            "Confirm",
                            JOptionPane.OK_CANCEL_OPTION);
                    if (confirm == JOptionPane.OK_OPTION) {
                        int index = Arrays.asList(todoPanel.getComponents()).indexOf(taskCheckBox);
                        taskManager.markTaskCompleted(index, true);
                        taskCheckBox.setVisible(false);
                    } else {
                        taskCheckBox.setSelected(false);
                    }
                }
            });

            todoPanel.add(taskCheckBox);
        }

        todoPanel.revalidate();
        todoPanel.repaint();
    }

    ///
    private void addNewTask() {
        if (taskManager.getTaskCount() < taskManager.getMaxTasks()) {
            String task = JOptionPane.showInputDialog(this, "Enter a new task:");
            if (task != null && !task.trim().isEmpty()) {
                boolean added = taskManager.addTask(task);
                if (added) {
                    // Refresh the task display
                    displayTasks(showingCompletedTasks);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "Max Note Limit Reached Buddy....consider actually completing a few tasks");
        }
    }

    ///
    private void toggleCompletedTasks() {
        showingCompletedTasks = !showingCompletedTasks;
        btnShowCompleted.setText(showingCompletedTasks ? "Hide" : "Show");
        btnDelete.setVisible(showingCompletedTasks);

        displayTasks(showingCompletedTasks);
    }

    ///
    private void deleteCompletedTasks() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete all completed tasks?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            taskManager.deleteCompletedTasks();
            displayTasks(showingCompletedTasks);
        }
    }

    ////////////////////////---------Tasks----------////////////////


    //Toolbar click listener for icons and labels inside
    private void addClickListener(JPanel panel) {
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                System.out.println("Clicked on: " + panel.getName());
                setActivePanel(panel);
                navigateToFunction(panel);
            }
        });

        // Also add to the label inside the panel (assuming each panel has a label)
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JLabel) {
                comp.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        System.out.println("Clicked on label in: " + panel.getName());
                        setActivePanel(panel);
                        navigateToFunction(panel);
                    }
                });
                break; // Assuming only one label per panel
            }
        }
    }

    /////////////////// /////////////////// ///////////////////
    
    //customise toolbar icon and Home Page Background
    private void loadIcon() {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/UI/flame.png"));
            this.setIconImage(icon.getImage());
        } catch (Exception e) {
            System.err.println("Icon not found! Make sure flame.png is inside 'src/UI/'");
        }
        try {
            // Load the original image
            ImageIcon originalIcon = new javax.swing.ImageIcon(getClass().getResource("/UI/evgeni-tcherkasski-FET2QYDjDXE-unsplash.jpg"));
            Image originalImage = originalIcon.getImage();

            // Scale it to the exact size needed
            Image scaledImage = originalImage.getScaledInstance(1030, 540, Image.SCALE_SMOOTH);

            // Create a new ImageIcon with the scaled image
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            // Set it to your JLabel
            homePageBackground.setIcon(scaledIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /////////////////// /////////////////// ///////////////////
    
    private void loadStoredTimetable() {
        File savedImage = new File("timetables/timetable.png");

        if (savedImage.exists()) {
            ImageIcon icon = new ImageIcon(new ImageIcon(savedImage.getAbsolutePath()).getImage()
                    .getScaledInstance(labelImage.getWidth(), labelImage.getHeight(), Image.SCALE_SMOOTH));
            labelImage.setIcon(icon);
        } else {
            System.out.println("No saved timetable found.");
        }
    }

    /////////////////// /////////////////// ///////////////////
    
    //Toobar change color of icons background
    private void setActivePanel(JPanel panel) {
        // Reset previously active panel color
        if (activePanel != null) {
            activePanel.setBackground(DEFAULT_COLOR);
        }

        // Set new active panel color
        panel.setBackground(ACTIVE_COLOR);
        activePanel = panel;

    }

    /////////////////// /////////////////// ///////////////////
    
    //toolbar icon clicked directory function
    private void navigateToFunction(JPanel panel) {
        if (panel == iconShowHide) {
            functionToRunShowHide();
        } else if (panel == iconHome) {
            functionToRunHomeIcon();
        } else if (panel == iconTime) {
            functionToRunTimeIcon();
        } else if (panel == iconLearners) {
            functionToRunLearnersIcon();
        } else if (panel == iconMarks) {
            functionToRunMarksIcon();
        }
    }

    /////////////////// /////////////////// ///////////////////
    
    
//    // Navigation functions
    private void functionToRunShowHide() {
//        if (AdditionalMenu.isVisible()) {
//            // Hide the additional menu
//            AdditionalMenu.setVisible(false);
//            // Expand the content panel to fill the space
//            ContentPanel.setPreferredSize(new Dimension(1025, 575));
//            // Change icon to indicate menu can be shown
//            labelIconShowHide.setIcon(new javax.swing.ImageIcon(getClass().getResource("/a1/icon/angle-right.png")));
//        } else {
//            // Show the additional menu
//            AdditionalMenu.setVisible(true);
//            AdditionalMenu.setPreferredSize(new Dimension(190, 575));
//            // Restore the content panel's original size
//            ContentPanel.setPreferredSize(new Dimension(835, 575));
//            // Change icon to indicate menu can be hidden
//            labelIconShowHide.setIcon(new javax.swing.ImageIcon(getClass().getResource("/a1/icon/angle-left.png")));
//        }
//        // Important: Revalidate and repaint to apply the changes
//        Menu.revalidate();
//        Menu.repaint();
//        ContentPanel.revalidate();
//        ContentPanel.repaint();
    }

    private void functionToRunHomeIcon() {
        System.out.println("Home function called");
        showPanel("home");
    }

    //
    private void functionToRunTimeIcon() {
        System.out.println("Time function called");
        showPanel("registration");

    }

    //
    private void functionToRunLearnersIcon() {
        System.out.println("Learners function called");
        showPanel("classList");
        refreshClassListData();
    }

    //
    private void functionToRunMarksIcon() {
        System.out.println("Marks function called");
        // Your marks function implementation here
    }

    /////////////////// /////////////////// ///////////////////

    //toolbar icons hover effects
    private void addHoverEffect(JPanel panel, JLabel label) {
        java.awt.event.MouseAdapter hoverAdapter = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                System.out.println("Mouse Entered on: " + panel.getName());  // Debugging
                hoverEffect(panel, true);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                System.out.println("Mouse Exited on: " + panel.getName());  // Debugging
                hoverEffect(panel, false);
            }
        };

        panel.addMouseListener(hoverAdapter);
        label.addMouseListener(hoverAdapter);
    }

    //
    private void hoverEffect(JPanel panel, boolean isHover) {
        Color original = panel.getBackground();
        int factor = isHover ? -28 : 28;  // Reduce brightness on hover, restore on exit
        int red = Math.max(0, Math.min(255, original.getRed() + factor));
        int green = Math.max(0, Math.min(255, original.getGreen() + factor));
        int blue = Math.max(0, Math.min(255, original.getBlue() + factor));
        panel.setBackground(new Color(red, green, blue));
    }

    /////////////////// /////////////////// ///////////////////
    
    
    
  private void setupTabChangeListener() {
        // Add change listener to the tabbed pane to refresh data when switching tabs
        tabbedPaneClassLists.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // This will be called when the tab is changed
                int selectedTab = tabbedPaneClassLists.getSelectedIndex();
                int grade = selectedTab + 8; // Convert tab index to grade (8-12)
                System.out.println("Tab changed to index: " + selectedTab + ", Grade: " + grade);
                refreshClassListData();
            }
        });
    }

// Method to refresh the current table data
    private void refreshClassListData() {
        // Get the selected tab index
        int selectedTab = tabbedPaneClassLists.getSelectedIndex();

        // If no tab is selected (can happen during initialization), default to first tab
        if (selectedTab < 0) {
            selectedTab = 0;
        }

        int grade = selectedTab + 8; // Convert tab index to grade (8-12)
        System.out.println("Refreshing data for grade: " + grade);

        // Get the correct table model
        JTable currentTable = getTableForGrade(grade);

        // Make sure we got a valid table
        if (currentTable == null) {
            System.out.println("ERROR: Table for grade " + grade + " is null!");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) currentTable.getModel();

        // Clear existing data
        model.setRowCount(0);

        try {
            // Use  Select class to get data
            String query = "SELECT * FROM tblClassList WHERE Grade = ?";
            System.out.println("Executing query: " + query + " with Grade = " + grade);
            Object[] values = {grade};
            ResultSet rs = Select.getResultSet(query, values);

            while (rs != null && rs.next()) {
                Object[] row = {
                    rs.getInt("ClassLearnerID"),
                    rs.getString("Name"),
                    rs.getString("Surname"),
                    rs.getInt("Grade"),
                    rs.getString("Subject"),
                    rs.getString("E-Class")
                };
                model.addRow(row);
            }

            // If no records found or database not yet set up, add dummy data
            if (model.getRowCount() == 0) {
                System.out.println("NO RECORDS COMING UP");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading class list data: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

// Helper method to get the table for a specific grade
    private JTable getTableForGrade(int grade) {
        switch (grade) {
            case 8:
                return tableGrade8;
            case 9:
                return tableGrade9;
            case 10:
                return tableGrade10;
            case 11:
                return tableGrade11;
            case 12:
                return tableGrade12;
            default:
                return tableGrade8;
        }
    }

    // Add dummy data for testing or when database is empty
    private void addDummyData(DefaultTableModel model, int grade) {
        // Add a few rows of sample data for each grade
        String[] eClasses = {"E1", "E2", "E3", "E4", "E5"};
        String[] subjects = {"CoRo", "CoRo", "I.T", "I.T", "I.T"};

        for (int i = 1; i <= 10; i++) {
            int id = (grade * 100) + i;
            String eClass = eClasses[i % eClasses.length];
            String subject = subjects[i % subjects.length];

            Object[] row = {
                id,
                "Student" + i,
                "Surname" + i,
                grade,
                subject,
                eClass
            };
            model.addRow(row);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialogAddLearner = new javax.swing.JDialog();
        jButton6 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        dialogLearnersName = new javax.swing.JTextField();
        dialogLearnersSurname = new javax.swing.JTextField();
        jSpinner1 = new javax.swing.JSpinner();
        jSpinner2 = new javax.swing.JSpinner();
        jSpinner3 = new javax.swing.JSpinner();
        jPanel12 = new javax.swing.JPanel();
        dialogAddUpcomingDate = new javax.swing.JDialog();
        jPanel8 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jSpinGrade = new javax.swing.JSpinner();
        jDCDate = new com.toedter.calendar.JDateChooser();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTXAInfo = new javax.swing.JTextArea();
        jPanel10 = new javax.swing.JPanel();
        jBTConfirmAddDate = new javax.swing.JButton();
        Menu = new javax.swing.JPanel();
        MenuBar = new javax.swing.JPanel();
        lineShowHide = new javax.swing.JPanel();
        iconShowHide = new javax.swing.JPanel();
        labelIconShowHide = new javax.swing.JLabel();
        lineHome = new javax.swing.JPanel();
        iconHome = new javax.swing.JPanel();
        labelIconHome = new javax.swing.JLabel();
        lineTime = new javax.swing.JPanel();
        iconTime = new javax.swing.JPanel();
        labelIconTime = new javax.swing.JLabel();
        lineLearners = new javax.swing.JPanel();
        iconLearners = new javax.swing.JPanel();
        labelIconLearners = new javax.swing.JLabel();
        lineMarks = new javax.swing.JPanel();
        iconMarks = new javax.swing.JPanel();
        labelIconMarks = new javax.swing.JLabel();
        Header = new javax.swing.JPanel();
        ContentPanel = new javax.swing.JPanel();
        contentPanelClassLists = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        tabbedPaneClassLists = new javax.swing.JTabbedPane();
        panelGrade8 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableGrade8 = new javax.swing.JTable();
        panelGrade9 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableGrade9 = new javax.swing.JTable();
        panelGrade10 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableGrade10 = new javax.swing.JTable();
        panelGrade11 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableGrade11 = new javax.swing.JTable();
        panelGrade12 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tableGrade12 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        btnAddLearner = new javax.swing.JButton();
        btnRemoveLearner = new javax.swing.JButton();
        contentPanelRegistrationPage = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLearners = new javax.swing.JTable();
        btnAdmin = new javax.swing.JButton();
        btnMarkAbsent = new javax.swing.JButton();
        btnMarkPresent = new javax.swing.JButton();
        contentPanelHomePage = new javax.swing.JPanel();
        jPNLLeft = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        linkSynergy = new RoundedButton("Synergy");
        ;
        linkMoodle = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPNLUpcomingDates = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTBLDates = new javax.swing.JTable();
        jBTNAddDate = new javax.swing.JButton();
        jBTDeleteDate = new javax.swing.JButton();
        jPNLRight = new javax.swing.JPanel();
        jPNLTimetable = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        labelImage = new javax.swing.JLabel();
        btnUpload = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        jPNLTasks = new javax.swing.JPanel();
        btnAddTask = new javax.swing.JButton();
        btnShowCompleted = new javax.swing.JButton();
        todoPanel = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        homePageBackground = new javax.swing.JLabel();

        dialogAddLearner.setAutoRequestFocus(false);
        dialogAddLearner.setBackground(new java.awt.Color(255, 0, 102));
        dialogAddLearner.setResizable(false);
        dialogAddLearner.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton6.setText("jButton6");
        dialogAddLearner.getContentPane().add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 210, -1, -1));

        jPanel9.setOpaque(false);

        jLabel4.setText("Learners Name");

        jLabel8.setText("Learners Surname");

        jLabel9.setText("Grade");

        jLabel11.setText("Subject");

        jLabel10.setText("E-Class");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel8)
                    .addComponent(jLabel4))
                .addGap(73, 73, 73))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        dialogAddLearner.getContentPane().add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 120, 170));

        jPanel11.setOpaque(false);

        dialogLearnersSurname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dialogLearnersSurnameActionPerformed(evt);
            }
        });

        jSpinner1.setModel(new javax.swing.SpinnerListModel(new String[] {"8", "9", "10", "11", "12"}));

        jSpinner2.setModel(new javax.swing.SpinnerListModel(new String[] {"1", "2", "3", "4", "5"}));

        jSpinner3.setModel(new javax.swing.SpinnerListModel(new String[] {"Tourism", "Coding and Robotics", "Information Technology"}));
        jSpinner3.setToolTipText("");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dialogLearnersName)
                    .addComponent(dialogLearnersSurname)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jSpinner3, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dialogLearnersName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dialogLearnersSurname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dialogAddLearner.getContentPane().add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 240, 190));

        jPanel12.setBackground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 586, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 571, Short.MAX_VALUE)
        );

        dialogAddUpcomingDate.setMinimumSize(new java.awt.Dimension(431, 334));
        dialogAddUpcomingDate.setPreferredSize(new java.awt.Dimension(431, 334));
        dialogAddUpcomingDate.getContentPane().setLayout(null);

        jLabel5.setText("Grade");

        jLabel6.setText("Test_Date");

        jLabel7.setText("Test_Info");

        jSpinGrade.setModel(new javax.swing.SpinnerListModel(new String[] {"8", "9", "10", "11", "12"}));

        jTXAInfo.setColumns(20);
        jTXAInfo.setRows(5);
        jScrollPane8.setViewportView(jTXAInfo);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel7))
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSpinGrade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDCDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jSpinGrade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jDCDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dialogAddUpcomingDate.getContentPane().add(jPanel8);
        jPanel8.setBounds(0, 0, 408, 200);

        jBTConfirmAddDate.setText("Add");
        jBTConfirmAddDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBTConfirmAddDateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(316, Short.MAX_VALUE)
                .addComponent(jBTConfirmAddDate)
                .addGap(16, 16, 16))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jBTConfirmAddDate)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        dialogAddUpcomingDate.getContentPane().add(jPanel10);
        jPanel10.setBounds(0, 250, 408, 70);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Get it!");
        setBackground(new java.awt.Color(0, 0, 0));

        Menu.setBackground(new java.awt.Color(0, 102, 102));
        Menu.setPreferredSize(new java.awt.Dimension(50, 675));
        Menu.setLayout(new java.awt.BorderLayout());

        MenuBar.setBackground(new java.awt.Color(35, 45, 63));
        MenuBar.setPreferredSize(new java.awt.Dimension(50, 575));
        MenuBar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lineShowHide.setBackground(new java.awt.Color(51, 51, 255));
        lineShowHide.setPreferredSize(new java.awt.Dimension(50, 5));

        javax.swing.GroupLayout lineShowHideLayout = new javax.swing.GroupLayout(lineShowHide);
        lineShowHide.setLayout(lineShowHideLayout);
        lineShowHideLayout.setHorizontalGroup(
            lineShowHideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        lineShowHideLayout.setVerticalGroup(
            lineShowHideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1, Short.MAX_VALUE)
        );

        MenuBar.add(lineShowHide, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 50, 1));

        iconShowHide.setBackground(new java.awt.Color(35, 45, 63));
        iconShowHide.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconShowHideMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconShowHideMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconShowHideMouseExited(evt);
            }
        });
        iconShowHide.setLayout(new java.awt.BorderLayout());

        labelIconShowHide.setBackground(new java.awt.Color(255, 204, 102));
        labelIconShowHide.setForeground(new java.awt.Color(255, 255, 255));
        labelIconShowHide.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelIconShowHide.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/angle-right.png"))); // NOI18N
        labelIconShowHide.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelIconShowHideMouseClicked(evt);
            }
        });
        iconShowHide.add(labelIconShowHide, java.awt.BorderLayout.CENTER);

        MenuBar.add(iconShowHide, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 50, 50));

        lineHome.setBackground(new java.awt.Color(51, 51, 255));
        lineHome.setPreferredSize(new java.awt.Dimension(50, 5));

        javax.swing.GroupLayout lineHomeLayout = new javax.swing.GroupLayout(lineHome);
        lineHome.setLayout(lineHomeLayout);
        lineHomeLayout.setHorizontalGroup(
            lineHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        lineHomeLayout.setVerticalGroup(
            lineHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1, Short.MAX_VALUE)
        );

        MenuBar.add(lineHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 50, 1));

        iconHome.setBackground(new java.awt.Color(35, 45, 63));
        iconHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconHomeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconHomeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconHomeMouseExited(evt);
            }
        });
        iconHome.setLayout(new java.awt.BorderLayout());

        labelIconHome.setBackground(new java.awt.Color(255, 204, 102));
        labelIconHome.setForeground(new java.awt.Color(255, 255, 255));
        labelIconHome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelIconHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/home.png"))); // NOI18N
        labelIconHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelIconHomeMouseClicked(evt);
            }
        });
        iconHome.add(labelIconHome, java.awt.BorderLayout.CENTER);

        MenuBar.add(iconHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 50, 50));

        lineTime.setBackground(new java.awt.Color(51, 51, 255));
        lineTime.setPreferredSize(new java.awt.Dimension(50, 5));

        javax.swing.GroupLayout lineTimeLayout = new javax.swing.GroupLayout(lineTime);
        lineTime.setLayout(lineTimeLayout);
        lineTimeLayout.setHorizontalGroup(
            lineTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        lineTimeLayout.setVerticalGroup(
            lineTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1, Short.MAX_VALUE)
        );

        MenuBar.add(lineTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 50, 1));

        iconTime.setBackground(new java.awt.Color(35, 45, 63));
        iconTime.setPreferredSize(new java.awt.Dimension(50, 50));
        iconTime.setLayout(new java.awt.BorderLayout());

        labelIconTime.setBackground(new java.awt.Color(255, 255, 51));
        labelIconTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelIconTime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/user-time.png"))); // NOI18N
        iconTime.add(labelIconTime, java.awt.BorderLayout.CENTER);

        MenuBar.add(iconTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 50, -1));

        lineLearners.setBackground(new java.awt.Color(153, 255, 255));
        lineLearners.setPreferredSize(new java.awt.Dimension(50, 5));

        javax.swing.GroupLayout lineLearnersLayout = new javax.swing.GroupLayout(lineLearners);
        lineLearners.setLayout(lineLearnersLayout);
        lineLearnersLayout.setHorizontalGroup(
            lineLearnersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        lineLearnersLayout.setVerticalGroup(
            lineLearnersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1, Short.MAX_VALUE)
        );

        MenuBar.add(lineLearners, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, -1, 1));

        iconLearners.setBackground(new java.awt.Color(35, 45, 63));
        iconLearners.setPreferredSize(new java.awt.Dimension(50, 50));
        iconLearners.setLayout(new java.awt.BorderLayout());

        labelIconLearners.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelIconLearners.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/child-head.png"))); // NOI18N
        iconLearners.add(labelIconLearners, java.awt.BorderLayout.CENTER);

        MenuBar.add(iconLearners, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, -1, -1));

        lineMarks.setBackground(new java.awt.Color(51, 255, 102));
        lineMarks.setPreferredSize(new java.awt.Dimension(50, 5));

        javax.swing.GroupLayout lineMarksLayout = new javax.swing.GroupLayout(lineMarks);
        lineMarks.setLayout(lineMarksLayout);
        lineMarksLayout.setHorizontalGroup(
            lineMarksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        lineMarksLayout.setVerticalGroup(
            lineMarksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1, Short.MAX_VALUE)
        );

        MenuBar.add(lineMarks, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, -1, 1));

        iconMarks.setBackground(new java.awt.Color(35, 45, 63));
        iconMarks.setPreferredSize(new java.awt.Dimension(50, 50));
        iconMarks.setLayout(new java.awt.BorderLayout());

        labelIconMarks.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelIconMarks.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/book-bookmark.png"))); // NOI18N
        iconMarks.add(labelIconMarks, java.awt.BorderLayout.CENTER);

        MenuBar.add(iconMarks, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, -1, -1));

        Menu.add(MenuBar, java.awt.BorderLayout.LINE_START);

        getContentPane().add(Menu, java.awt.BorderLayout.LINE_START);

        Header.setBackground(new java.awt.Color(102, 102, 102));

        javax.swing.GroupLayout HeaderLayout = new javax.swing.GroupLayout(Header);
        Header.setLayout(HeaderLayout);
        HeaderLayout.setHorizontalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1080, Short.MAX_VALUE)
        );
        HeaderLayout.setVerticalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        getContentPane().add(Header, java.awt.BorderLayout.PAGE_START);

        ContentPanel.setBackground(new java.awt.Color(0, 129, 112));
        ContentPanel.setOpaque(false);
        ContentPanel.setPreferredSize(new java.awt.Dimension(1030, 540));
        ContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        contentPanelClassLists.setBackground(new java.awt.Color(102, 102, 102));

        panelGrade8.setLayout(new java.awt.BorderLayout());

        tableGrade8.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ClassLearnerID", "Name", "Surname", "Grade", "Subject", "E-Class"
            }
        ));
        jScrollPane2.setViewportView(tableGrade8);

        panelGrade8.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        tabbedPaneClassLists.addTab("tab1", panelGrade8);

        panelGrade9.setLayout(new java.awt.BorderLayout());

        tableGrade9.setModel(tableGrade8.getModel());
        jScrollPane3.setViewportView(tableGrade9);
        if (tableGrade9.getColumnModel().getColumnCount() > 0) {
            tableGrade9.getColumnModel().getColumn(0).setHeaderValue("ClassLearnerID");
            tableGrade9.getColumnModel().getColumn(1).setHeaderValue("Name");
            tableGrade9.getColumnModel().getColumn(2).setHeaderValue("Surname");
            tableGrade9.getColumnModel().getColumn(3).setHeaderValue("Grade");
            tableGrade9.getColumnModel().getColumn(4).setHeaderValue("Subject");
            tableGrade9.getColumnModel().getColumn(5).setHeaderValue("E-Class");
        }

        panelGrade9.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        tabbedPaneClassLists.addTab("tab2", panelGrade9);

        panelGrade10.setLayout(new java.awt.BorderLayout());

        tableGrade10.setModel(tableGrade8.getModel());
        jScrollPane4.setViewportView(tableGrade10);
        if (tableGrade10.getColumnModel().getColumnCount() > 0) {
            tableGrade10.getColumnModel().getColumn(0).setHeaderValue("ClassLearnerID");
            tableGrade10.getColumnModel().getColumn(1).setHeaderValue("Name");
            tableGrade10.getColumnModel().getColumn(2).setHeaderValue("Surname");
            tableGrade10.getColumnModel().getColumn(3).setHeaderValue("Grade");
            tableGrade10.getColumnModel().getColumn(4).setHeaderValue("Subject");
            tableGrade10.getColumnModel().getColumn(5).setHeaderValue("E-Class");
        }

        panelGrade10.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        tabbedPaneClassLists.addTab("tab3", panelGrade10);

        panelGrade11.setLayout(new java.awt.BorderLayout());

        tableGrade11.setModel(tableGrade8.getModel());
        jScrollPane5.setViewportView(tableGrade11);
        if (tableGrade11.getColumnModel().getColumnCount() > 0) {
            tableGrade11.getColumnModel().getColumn(0).setHeaderValue("ClassLearnerID");
            tableGrade11.getColumnModel().getColumn(1).setHeaderValue("Name");
            tableGrade11.getColumnModel().getColumn(2).setHeaderValue("Surname");
            tableGrade11.getColumnModel().getColumn(3).setHeaderValue("Grade");
            tableGrade11.getColumnModel().getColumn(4).setHeaderValue("Subject");
            tableGrade11.getColumnModel().getColumn(5).setHeaderValue("E-Class");
        }

        panelGrade11.add(jScrollPane5, java.awt.BorderLayout.CENTER);

        tabbedPaneClassLists.addTab("tab4", panelGrade11);

        panelGrade12.setLayout(new java.awt.BorderLayout());

        tableGrade12.setModel(tableGrade10.getModel());
        jScrollPane6.setViewportView(tableGrade12);
        if (tableGrade12.getColumnModel().getColumnCount() > 0) {
            tableGrade12.getColumnModel().getColumn(0).setHeaderValue("ClassLearnerID");
            tableGrade12.getColumnModel().getColumn(1).setHeaderValue("Name");
            tableGrade12.getColumnModel().getColumn(2).setHeaderValue("Surname");
            tableGrade12.getColumnModel().getColumn(3).setHeaderValue("Grade");
            tableGrade12.getColumnModel().getColumn(4).setHeaderValue("Subject");
            tableGrade12.getColumnModel().getColumn(5).setHeaderValue("E-Class");
        }

        panelGrade12.add(jScrollPane6, java.awt.BorderLayout.CENTER);

        tabbedPaneClassLists.addTab("tab5", panelGrade12);

        jButton1.setText("DummyData");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnAddLearner.setText("Add Learner");
        btnAddLearner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddLearnerActionPerformed(evt);
            }
        });

        btnRemoveLearner.setText("Remove Learner");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(127, 127, 127)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnRemoveLearner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAddLearner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1))
                .addGap(53, 53, 53)
                .addComponent(tabbedPaneClassLists, javax.swing.GroupLayout.PREFERRED_SIZE, 642, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(81, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(tabbedPaneClassLists, javax.swing.GroupLayout.PREFERRED_SIZE, 496, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(183, 183, 183)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(btnAddLearner)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnRemoveLearner)))
                .addContainerGap(123, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout contentPanelClassListsLayout = new javax.swing.GroupLayout(contentPanelClassLists);
        contentPanelClassLists.setLayout(contentPanelClassListsLayout);
        contentPanelClassListsLayout.setHorizontalGroup(
            contentPanelClassListsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentPanelClassListsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        contentPanelClassListsLayout.setVerticalGroup(
            contentPanelClassListsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentPanelClassListsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ContentPanel.add(contentPanelClassLists, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1030, 680));

        tblLearners.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblLearners);

        btnAdmin.setText("jButton1");

        btnMarkAbsent.setText("Mark Absent");

        btnMarkPresent.setText("Mark Present");

        javax.swing.GroupLayout contentPanelRegistrationPageLayout = new javax.swing.GroupLayout(contentPanelRegistrationPage);
        contentPanelRegistrationPage.setLayout(contentPanelRegistrationPageLayout);
        contentPanelRegistrationPageLayout.setHorizontalGroup(
            contentPanelRegistrationPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contentPanelRegistrationPageLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(contentPanelRegistrationPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAdmin)
                    .addComponent(btnMarkAbsent)
                    .addComponent(btnMarkPresent))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 249, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 670, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        contentPanelRegistrationPageLayout.setVerticalGroup(
            contentPanelRegistrationPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contentPanelRegistrationPageLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(contentPanelRegistrationPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(contentPanelRegistrationPageLayout.createSequentialGroup()
                        .addComponent(btnMarkAbsent)
                        .addGap(18, 18, 18)
                        .addComponent(btnMarkPresent)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAdmin))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE))
                .addContainerGap())
        );

        ContentPanel.add(contentPanelRegistrationPage, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1030, 680));

        contentPanelHomePage.setBackground(new java.awt.Color(35, 45, 63));
        contentPanelHomePage.setMaximumSize(new java.awt.Dimension(1030, 675));
        contentPanelHomePage.setPreferredSize(new java.awt.Dimension(1030, 675));
        contentPanelHomePage.setLayout(new java.awt.GridLayout(1, 2));

        jPNLLeft.setOpaque(false);
        jPNLLeft.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setMaximumSize(new java.awt.Dimension(233, 50));
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(233, 50));
        jPanel3.setRequestFocusEnabled(false);
        jPanel3.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Cinzel Decorative", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText(" Home Page");
        jPanel3.add(jLabel1, java.awt.BorderLayout.CENTER);

        jPNLLeft.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 0, 510, 80));

        jPanel4.setOpaque(false);

        jPanel5.setOpaque(false);

        jLabel2.setFont(new java.awt.Font("Cinzel Decorative", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Links");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
        );

        jPanel6.setOpaque(false);

        linkSynergy.setBackground(new java.awt.Color(51, 51, 255));
        linkSynergy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UI/flame.png"))); // NOI18N
        linkSynergy.setText("Synergy");
        linkSynergy.setBorder(null);
        linkSynergy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                linkSynergyActionPerformed(evt);
            }
        });
        jPanel6.add(linkSynergy);

        linkMoodle.setText("Moodle");
        linkMoodle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                linkMoodleActionPerformed(evt);
            }
        });
        jPanel6.add(linkMoodle);

        jButton3.setText("jButton3");
        jPanel6.add(jButton3);

        jButton4.setText("jButton3");
        jPanel6.add(jButton4);

        jButton5.setText("jButton3");
        jPanel6.add(jButton5);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPNLLeft.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 515, -1));

        jTBLDates.setForeground(new java.awt.Color(0, 0, 0));
        jTBLDates.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTBLDates.setSelectionForeground(new java.awt.Color(102, 102, 102));
        jScrollPane7.setViewportView(jTBLDates);

        jBTNAddDate.setText("Add Date");
        jBTNAddDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBTNAddDateActionPerformed(evt);
            }
        });

        jBTDeleteDate.setText("Remove");
        jBTDeleteDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBTDeleteDateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPNLUpcomingDatesLayout = new javax.swing.GroupLayout(jPNLUpcomingDates);
        jPNLUpcomingDates.setLayout(jPNLUpcomingDatesLayout);
        jPNLUpcomingDatesLayout.setHorizontalGroup(
            jPNLUpcomingDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPNLUpcomingDatesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPNLUpcomingDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPNLUpcomingDatesLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jBTDeleteDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBTNAddDate)))
                .addContainerGap())
        );
        jPNLUpcomingDatesLayout.setVerticalGroup(
            jPNLUpcomingDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPNLUpcomingDatesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPNLUpcomingDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBTNAddDate)
                    .addComponent(jBTDeleteDate))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPNLLeft.add(jPNLUpcomingDates, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 510, 470));

        contentPanelHomePage.add(jPNLLeft);

        jPNLRight.setLayout(new java.awt.GridLayout(2, 1, 0, 10));

        jPNLTimetable.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel3.setText("Timetable");

        btnUpload.setText("Upload");
        btnUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadActionPerformed(evt);
            }
        });

        btnRemove.setText("Remove");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPNLTimetableLayout = new javax.swing.GroupLayout(jPNLTimetable);
        jPNLTimetable.setLayout(jPNLTimetableLayout);
        jPNLTimetableLayout.setHorizontalGroup(
            jPNLTimetableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPNLTimetableLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPNLTimetableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPNLTimetableLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnUpload)
                        .addGap(18, 18, 18)
                        .addComponent(btnRemove)
                        .addGap(0, 170, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPNLTimetableLayout.setVerticalGroup(
            jPNLTimetableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPNLTimetableLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPNLTimetableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpload)
                    .addComponent(btnRemove))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelImage, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPNLRight.add(jPNLTimetable);

        jPNLTasks.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        btnAddTask.setText("Add ");

        btnShowCompleted.setText("Show");

        todoPanel.setAlignmentX(0.0F);
        todoPanel.setMaximumSize(new java.awt.Dimension(391, 269));
        todoPanel.setPreferredSize(new java.awt.Dimension(391, 269));
        todoPanel.setLayout(new javax.swing.BoxLayout(todoPanel, javax.swing.BoxLayout.Y_AXIS));

        btnDelete.setText("Delete");

        javax.swing.GroupLayout jPNLTasksLayout = new javax.swing.GroupLayout(jPNLTasks);
        jPNLTasks.setLayout(jPNLTasksLayout);
        jPNLTasksLayout.setHorizontalGroup(
            jPNLTasksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPNLTasksLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(todoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPNLTasksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPNLTasksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnAddTask, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnShowCompleted, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(btnDelete, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPNLTasksLayout.setVerticalGroup(
            jPNLTasksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPNLTasksLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPNLTasksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(todoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPNLTasksLayout.createSequentialGroup()
                        .addComponent(btnAddTask)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnShowCompleted)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDelete)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPNLRight.add(jPNLTasks);

        contentPanelHomePage.add(jPNLRight);

        ContentPanel.add(contentPanelHomePage, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1030, 680));
        ContentPanel.add(homePageBackground, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1030, 680));

        getContentPane().add(ContentPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void labelIconHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelIconHomeMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_labelIconHomeMouseClicked

    private void iconHomeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconHomeMouseExited
        // TODO add your handling code here:

    }//GEN-LAST:event_iconHomeMouseExited

    private void iconHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconHomeMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_iconHomeMouseClicked

    private void iconHomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconHomeMouseEntered


    }//GEN-LAST:event_iconHomeMouseEntered

    private void iconShowHideMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconShowHideMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_iconShowHideMouseClicked

    private void iconShowHideMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconShowHideMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_iconShowHideMouseEntered

    private void iconShowHideMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconShowHideMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_iconShowHideMouseExited

    private void linkSynergyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linkSynergyActionPerformed
        try {
            Desktop.getDesktop().browse(new URI("https://synergy2.curro.co.za/synergy/ui"));
        } catch (IOException | URISyntaxException e) {
            JOptionPane.showMessageDialog(this,
                    "Error opening website: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

        }
    }//GEN-LAST:event_linkSynergyActionPerformed

    private void linkMoodleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linkMoodleActionPerformed

        try {
            Desktop.getDesktop().browse(new URI("https://digital.curro.co.za"));
        } catch (IOException | URISyntaxException e) {
            JOptionPane.showMessageDialog(this,
                    "Error opening website: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

        }
    }//GEN-LAST:event_linkMoodleActionPerformed

    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadActionPerformed

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a Timetable Image");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg"));

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFileTimetable = fileChooser.getSelectedFile();

            // Define storage directory inside the project
            File directory = new File("timetables");
            if (!directory.exists()) {
                directory.mkdirs(); // Create folder if it doesn't exist
            }

            // Define destination file inside the project folder
            File destination = new File(directory, "timetable.png");

            try {
                // Copy file to project folder
                Files.copy(selectedFileTimetable.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Display the copied image
                ImageIcon icon = new ImageIcon(new ImageIcon(destination.getAbsolutePath()).getImage()
                        .getScaledInstance(labelImage.getWidth(), labelImage.getHeight(), Image.SCALE_SMOOTH));
                labelImage.setIcon(icon);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving timetable: " + e.getMessage());
            }
        }


    }//GEN-LAST:event_btnUploadActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        labelImage.setIcon(null);
        selectedFileTimetable = null;
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        int selectedTab = tabbedPaneClassLists.getSelectedIndex();
        int grade = selectedTab + 8; // Convert tab index to grade (8-12)

        // Get the correct table model
        JTable currentTable = getTableForGrade(grade);
        DefaultTableModel model = (DefaultTableModel) currentTable.getModel();

        // Clear existing data
        model.setRowCount(0);

        addDummyData(model, grade);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnAddLearnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddLearnerActionPerformed

        dialogAddLearner.setSize(433, 295);
        dialogAddLearner.setLocationRelativeTo(ContentPanel);
        dialogAddLearner.setVisible(true);
        System.out.println("Should be visible now...");
        //InsertDeleteUpdate1.writeData(sqlQuery, values, message);
    }//GEN-LAST:event_btnAddLearnerActionPerformed

    private void dialogLearnersSurnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dialogLearnersSurnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dialogLearnersSurnameActionPerformed

    private void jBTNAddDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBTNAddDateActionPerformed
        dialogAddUpcomingDate.setVisible(true);
    }//GEN-LAST:event_jBTNAddDateActionPerformed

    private void jBTConfirmAddDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBTConfirmAddDateActionPerformed
        String test_grade = jSpinGrade.getValue().toString();
        Date test_date = jDCDate.getDate();
        String test_info = jTXAInfo.getText();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String test_date_string = sdf.format(test_date);

        UpcomingDateManager man = new UpcomingDateManager();
        if (man.addDate(test_grade, test_info, test_date_string)) {
            JOptionPane.showMessageDialog(this, "Test Added Succesfully");
            loadDatesToTable();
            dialogAddUpcomingDate.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add test.");
        }


    }//GEN-LAST:event_jBTConfirmAddDateActionPerformed

    private void labelIconShowHideMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelIconShowHideMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_labelIconShowHideMouseClicked

    private void jBTDeleteDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBTDeleteDateActionPerformed
        UpcomingDateManager man = new UpcomingDateManager();
        int selectedRow = jTBLDates.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a date to delete first.");
            return;
        }

        UpcomingDates selected = upcomingDates_list.get(selectedRow);
        String idToDelete = selected.getId();

        if (man.deleteDateById(idToDelete)) {
            JOptionPane.showMessageDialog(this, "Date deleted successfully.");
            loadDatesToTable(); // 🔁 Reload table after deletion
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete date.");
        }
    }//GEN-LAST:event_jBTDeleteDateActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(A1GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(A1GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(A1GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(A1GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }//        }

        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new A1GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ContentPanel;
    private javax.swing.JPanel Header;
    private javax.swing.JPanel Menu;
    private javax.swing.JPanel MenuBar;
    private javax.swing.JButton btnAddLearner;
    private javax.swing.JButton btnAddTask;
    private javax.swing.JButton btnAdmin;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnMarkAbsent;
    private javax.swing.JButton btnMarkPresent;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnRemoveLearner;
    private javax.swing.JButton btnShowCompleted;
    private javax.swing.JButton btnUpload;
    private javax.swing.JPanel contentPanelClassLists;
    private javax.swing.JPanel contentPanelHomePage;
    private javax.swing.JPanel contentPanelRegistrationPage;
    private javax.swing.JDialog dialogAddLearner;
    private javax.swing.JDialog dialogAddUpcomingDate;
    private javax.swing.JTextField dialogLearnersName;
    private javax.swing.JTextField dialogLearnersSurname;
    private javax.swing.JLabel homePageBackground;
    private javax.swing.JPanel iconHome;
    private javax.swing.JPanel iconLearners;
    private javax.swing.JPanel iconMarks;
    private javax.swing.JPanel iconShowHide;
    private javax.swing.JPanel iconTime;
    private javax.swing.JButton jBTConfirmAddDate;
    private javax.swing.JButton jBTDeleteDate;
    private javax.swing.JButton jBTNAddDate;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private com.toedter.calendar.JDateChooser jDCDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPNLLeft;
    private javax.swing.JPanel jPNLRight;
    private javax.swing.JPanel jPNLTasks;
    private javax.swing.JPanel jPNLTimetable;
    private javax.swing.JPanel jPNLUpcomingDates;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSpinner jSpinGrade;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JTable jTBLDates;
    private javax.swing.JTextArea jTXAInfo;
    private javax.swing.JLabel labelIconHome;
    private javax.swing.JLabel labelIconLearners;
    private javax.swing.JLabel labelIconMarks;
    private javax.swing.JLabel labelIconShowHide;
    private javax.swing.JLabel labelIconTime;
    private javax.swing.JLabel labelImage;
    private javax.swing.JPanel lineHome;
    private javax.swing.JPanel lineLearners;
    private javax.swing.JPanel lineMarks;
    private javax.swing.JPanel lineShowHide;
    private javax.swing.JPanel lineTime;
    private javax.swing.JButton linkMoodle;
    private javax.swing.JButton linkSynergy;
    private javax.swing.JPanel panelGrade10;
    private javax.swing.JPanel panelGrade11;
    private javax.swing.JPanel panelGrade12;
    private javax.swing.JPanel panelGrade8;
    private javax.swing.JPanel panelGrade9;
    private javax.swing.JTabbedPane tabbedPaneClassLists;
    private javax.swing.JTable tableGrade10;
    private javax.swing.JTable tableGrade11;
    private javax.swing.JTable tableGrade12;
    private javax.swing.JTable tableGrade8;
    private javax.swing.JTable tableGrade9;
    private javax.swing.JTable tblLearners;
    private javax.swing.JPanel todoPanel;
    // End of variables declaration//GEN-END:variables
}
