
package SQLite1;

/**
 *
 * @author SETH.H
 */
import java.sql.*;

public class Test {
    public static void main(String[] args) {
        String name = "Seth", incorrectName = "F";
        String surname = "Hendrikz", incorrectSurname = "J";
        
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        con = DBConnection.connectToDB();
        String sql = "SELECT * FROM tblStudent WHERE Name = ? AND Surname = ?";
        
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, name);
            ps.setString(2, surname);
            
            rs = ps.executeQuery();
            
            while(rs.next()){
                System.out.println(rs.toString());
                System.out.println(rs.getString("Name"));
            }
        }catch(SQLException e){
            System.out.println("Failed with test.java catch"+ e);
        }
        
    }
}
