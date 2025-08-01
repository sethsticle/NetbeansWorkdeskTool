
package Managers;

import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author SETH.H
 */


public class TestMain {
    
    public static void main(String[] args) throws SQLException {
        
        DBManager db = new DBManager();
        System.out.println(db.SelectAll("tblUsers"));
        
        String email = JOptionPane.showInputDialog("email");
        String password = JOptionPane.showInputDialog("password");
        
        System.out.println("Login returns: " + db.Login(email, password));
    }
   
}
