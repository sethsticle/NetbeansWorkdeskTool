
package SQLite1;

/**
 *
 * @author SETH.H
 */
import java.sql.*;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        String name = "Seth", incorrectName = "F";
        String surname = "Hendrikz", incorrectSurname = "J";
        
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        String sql = "SELECT * FROM tblStudent";
        List<Map<String,Object>> rows = dbUtils.readDB(sql);
        
        for(Map<String,Object> row: rows){
            System.out.println(row.get("Name") + " "+ row.get("Surname"));
        }
        
    }
}
