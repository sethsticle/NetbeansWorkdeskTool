/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package SQLite1;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author SETH.H
 */
public class tempInputForm extends javax.swing.JFrame {

    protected DefaultTableModel modelTempUITable;
//
//    private void loadTempUITable() {
//        modelTempUITable = new DefaultTableModel(
//                new Object[]{"First Name", "Surname", "Gender", "E-class", "Grade", "PAT Mark", "FAT Mark"}, 0);
//        tempUITable.setModel(modelTempUITable);
//        TableColumn genderColumn = tempUITable.getColumnModel().getColumn(2);
//        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"He", "She"});
//        genderComboBox.setEditable(true); // allows typing
//        genderComboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
//
//        genderColumn.setCellEditor(new DefaultCellEditor(genderComboBox) {
//            @Override
//            public boolean stopCellEditing() {
//                String value = (String) getCellEditorValue();
//                if ("s".equalsIgnoreCase(value)) {
//                    value = "She"; // auto-correct
//                } else if ("h".equalsIgnoreCase(value)) {
//                    value = "He";
//                }
//                delegate.setValue(value);
//                return super.stopCellEditing();
//            }
//        });
//
//        tempUITable.setAutoResizeMode(tempUITable.AUTO_RESIZE_OFF);
//        tempUITable.getColumnModel().getColumn(0).setPreferredWidth(120); // First Name
//        tempUITable.getColumnModel().getColumn(1).setPreferredWidth(120); // Surname
//        tempUITable.getColumnModel().getColumn(2).setPreferredWidth(60);  // Gender
//        // repeat for other columns...
//    }

    private void loadTempUITable() {
        modelTempUITable = new DefaultTableModel(
                new Object[]{"First Name", "Surname", "Gender", "E-class", "Grade", "PAT Mark", "FAT Mark"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Only Gender column is editable
            }
        };
        tempUITable.setModel(modelTempUITable);

        // Fix column widths
        tempUITable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tempUITable.getColumnModel().getColumn(0).setPreferredWidth(120);
        tempUITable.getColumnModel().getColumn(1).setPreferredWidth(120);
        tempUITable.getColumnModel().getColumn(2).setPreferredWidth(60);

        // Gender combo box setup
        TableColumn genderColumn = tempUITable.getColumnModel().getColumn(2);
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"He", "She"});
        genderComboBox.setEditable(false); // strictly pick from dropdown

        // Add KeyListener for immediate H/S selection
        genderComboBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = Character.toUpperCase(e.getKeyChar());
                if (c == 'H') {
                    genderComboBox.setSelectedItem("He");
                } else if (c == 'S') {
                    genderComboBox.setSelectedItem("She");
                }
            }
        });

        genderColumn.setCellEditor(new DefaultCellEditor(genderComboBox) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value,
                    boolean isSelected, int row, int column) {
                genderComboBox.setSelectedItem(value != null ? value : ""); // default value
                return genderComboBox;
            }
        });

        tempUITable.setSurrendersFocusOnKeystroke(true); // makes keyboard navigation smoother
    }

    /**
     * Creates new form tempInputForm
     */
    public tempInputForm() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        topBar = new javax.swing.JPanel();
        rightBar = new javax.swing.JPanel();
        bottomBar = new javax.swing.JPanel();
        leftBar = new javax.swing.JPanel();
        center = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        SPe_class = new javax.swing.JSpinner();
        jScrollPane1 = new javax.swing.JScrollPane();
        TAmark1 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        TAsurname = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        TAname = new javax.swing.JTextArea();
        jScrollPane7 = new javax.swing.JScrollPane();
        TAmark2 = new javax.swing.JTextArea();
        SPgrade = new javax.swing.JSpinner();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tempUITable = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.CardLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        topBar.setBackground(new java.awt.Color(204, 204, 255));

        javax.swing.GroupLayout topBarLayout = new javax.swing.GroupLayout(topBar);
        topBar.setLayout(topBarLayout);
        topBarLayout.setHorizontalGroup(
            topBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 682, Short.MAX_VALUE)
        );
        topBarLayout.setVerticalGroup(
            topBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel1.add(topBar, java.awt.BorderLayout.PAGE_START);

        rightBar.setBackground(new java.awt.Color(255, 204, 204));

        javax.swing.GroupLayout rightBarLayout = new javax.swing.GroupLayout(rightBar);
        rightBar.setLayout(rightBarLayout);
        rightBarLayout.setHorizontalGroup(
            rightBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        rightBarLayout.setVerticalGroup(
            rightBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 331, Short.MAX_VALUE)
        );

        jPanel1.add(rightBar, java.awt.BorderLayout.LINE_END);

        bottomBar.setBackground(new java.awt.Color(204, 204, 255));

        javax.swing.GroupLayout bottomBarLayout = new javax.swing.GroupLayout(bottomBar);
        bottomBar.setLayout(bottomBarLayout);
        bottomBarLayout.setHorizontalGroup(
            bottomBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 682, Short.MAX_VALUE)
        );
        bottomBarLayout.setVerticalGroup(
            bottomBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel1.add(bottomBar, java.awt.BorderLayout.PAGE_END);

        leftBar.setBackground(new java.awt.Color(255, 204, 204));

        javax.swing.GroupLayout leftBarLayout = new javax.swing.GroupLayout(leftBar);
        leftBar.setLayout(leftBarLayout);
        leftBarLayout.setHorizontalGroup(
            leftBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        leftBarLayout.setVerticalGroup(
            leftBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 331, Short.MAX_VALUE)
        );

        jPanel1.add(leftBar, java.awt.BorderLayout.LINE_START);

        center.setBackground(new java.awt.Color(51, 51, 51));
        center.setLayout(new javax.swing.BoxLayout(center, javax.swing.BoxLayout.LINE_AXIS));

        jPanel3.setBackground(new java.awt.Color(255, 153, 0));

        jLabel1.setText("jLabel1");

        jLabel2.setText("jLabel2");

        jLabel3.setText("jLabel3");

        jLabel4.setText("jLabel4");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(105, 105, 105))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jLabel1)
                .addGap(53, 53, 53)
                .addComponent(jLabel2)
                .addGap(58, 58, 58)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(40, 40, 40))
        );

        center.add(jPanel3);

        TAmark1.setColumns(1);
        TAmark1.setRows(1);
        TAmark1.setMaximumSize(new java.awt.Dimension(23, 20));
        TAmark1.setMinimumSize(new java.awt.Dimension(23, 20));
        jScrollPane1.setViewportView(TAmark1);

        TAsurname.setColumns(1);
        TAsurname.setRows(1);
        TAsurname.setMaximumSize(new java.awt.Dimension(23, 20));
        TAsurname.setMinimumSize(new java.awt.Dimension(23, 20));
        jScrollPane3.setViewportView(TAsurname);

        TAname.setColumns(1);
        TAname.setRows(1);
        TAname.setMaximumSize(new java.awt.Dimension(23, 20));
        TAname.setMinimumSize(new java.awt.Dimension(23, 20));
        jScrollPane6.setViewportView(TAname);

        TAmark2.setColumns(1);
        TAmark2.setRows(1);
        TAmark2.setMaximumSize(new java.awt.Dimension(23, 20));
        TAmark2.setMinimumSize(new java.awt.Dimension(23, 20));
        jScrollPane7.setViewportView(TAmark2);

        jButton1.setText("ok");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("jButton2");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(SPgrade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(SPe_class, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jScrollPane1, jScrollPane3, jScrollPane6, jScrollPane7});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(SPe_class, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(SPgrade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addGap(129, 129, 129))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jScrollPane1, jScrollPane3, jScrollPane6});

        center.add(jPanel2);

        jPanel1.add(center, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, "card2");

        tempUITable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tempUITable);

        jButton3.setText("jButton3");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(113, 113, 113)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(53, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addGap(70, 70, 70))))
        );

        getContentPane().add(jPanel4, "card3");

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        loadTempUITable();
        String name = TAname.getText(), surname = TAsurname.getText();
        String mark1 = TAmark1.getText(), mark2 = TAmark2.getText();
        int grade = Integer.parseInt(SPgrade.getValue().toString()), e_class = Integer.parseInt(SPe_class.getValue().toString());

        //validate those inputs at some point
        if (name.length() == 0 || !tempUIService.isSingleColumnPaste(name)) {
            JOptionPane.showMessageDialog(this, "Learner names must be a single column.");
            return;
        }
        if (!tempUIService.isSingleColumnPaste(surname) || surname.length() == 0) {
            JOptionPane.showMessageDialog(this, "Learner names must be a single column.");
            return;
        }
        if (!tempUIService.isSingleColumnPaste(mark1) || mark1.length() == 0) {
            JOptionPane.showMessageDialog(this, "Learner names must be a single column.");
            return;
        }
        if (!tempUIService.isSingleColumnPaste(mark2) || mark2.length() == 0) {
            JOptionPane.showMessageDialog(this, "Learner names must be a single column.");
            return;
        }

        List<String> nameList = Arrays.asList(name.split("\\r?\\n|\\t"));
        List<String> surnameList = Arrays.asList(surname.split("\\r?\\n|\\t"));
        List<String> mark1List = Arrays.asList(mark1.split("\\r?\\n|\\t"));
        List<String> mark2List = Arrays.asList(mark2.split("\\r?\\n|\\t"));

        System.out.println("debug for names List: " + nameList.toString());
        System.out.println("name size: " + nameList.size());
        System.out.println("surname size: " + surnameList.size());
        System.out.println("mark1: " + mark1List.size());
        System.out.println("Mark2: " + mark2List.size());

        //VALIDATE THAT ALL LISTS ARE THE SAME SIZE
        if (nameList.size() != surnameList.size() && surnameList.size() != mark1List.size() && mark1List.size() != mark2List.size()) {
            JOptionPane.showInputDialog("ERROR: I am picking up a difference in the length of on of the 4 important fields...please re-enter data. Soz.");
            return;
        }

        for (int i = 0; i < nameList.size(); i++) {
            modelTempUITable.addRow(new Object[]{
                nameList.get(i),
                surnameList.get(i),
                "",
                e_class,
                grade,
                mark1List.get(i),
                mark2List.get(i)
            });
        }
        jPanel1.setVisible(false);
        jPanel4.setVisible(true);


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        saveTableToDBWithUtils();
    }//GEN-LAST:event_jButton3ActionPerformed
    private void saveTableToDBWithUtils() {
        for (int i = 0; i < modelTempUITable.getRowCount(); i++) {
            String name = (String) modelTempUITable.getValueAt(i, 0);
            String surname = (String) modelTempUITable.getValueAt(i, 1);
            String gender = (String) modelTempUITable.getValueAt(i, 2);
            int e_class = (Integer) modelTempUITable.getValueAt(i, 3);
            int grade = (Integer) modelTempUITable.getValueAt(i, 4);
            double mark1 = Double.parseDouble(modelTempUITable.getValueAt(i, 5).toString());
            double mark2 = Double.parseDouble(modelTempUITable.getValueAt(i, 6).toString());

            // Insert into tblStudents
            String studentSQL = "INSERT INTO tblStudents(Name, Surname, Grade, E_Class, Gender) VALUES(?,?,?,?,?)";
            boolean inserted = dbUtils.updateDB(studentSQL, name, surname, grade, e_class, gender);

            if (!inserted) {
                JOptionPane.showMessageDialog(this, "Failed to insert student: " + name);
                continue; // skip to next row
            }
            System.out.print("Inserted student: "+name);
            // Get last inserted row ID in SQLite
            List<Map<String, Object>> result = dbUtils.readDB("SELECT ID AS id FROM tblStudents WHERE Name = ?", name);
            
            if (result.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Failed to retrieve ID for student: " + name);
                continue;
            }
            int studentID = ((Number) result.get(0).get("id")).intValue();
            System.out.println("student ID: "+studentID);
            // Insert marks
            String marksSQL = "INSERT INTO tblMarks(studentID, mark1, mark2) VALUES(?,?,?)";
            dbUtils.updateDB(marksSQL, studentID, mark1, mark2);
        }
    }

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
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(tempInputForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(tempInputForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(tempInputForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(tempInputForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new tempInputForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner SPe_class;
    private javax.swing.JSpinner SPgrade;
    private javax.swing.JTextArea TAmark1;
    private javax.swing.JTextArea TAmark2;
    private javax.swing.JTextArea TAname;
    private javax.swing.JTextArea TAsurname;
    private javax.swing.JPanel bottomBar;
    private javax.swing.JPanel center;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JPanel leftBar;
    private javax.swing.JPanel rightBar;
    private javax.swing.JTable tempUITable;
    private javax.swing.JPanel topBar;
    // End of variables declaration//GEN-END:variables
}
