
package SQLite1;

/**
 *
 * @author SETH.H
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        String name = "Seth", incorrectName = "F";
        String surname = "Hendrikz", incorrectSurname = "J";
        
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        String sql = "SELECT * FROM tblStudents";
        List<Map<String,Object>> rows = dbUtils.readDB(sql);
        
        for(Map<String,Object> row: rows){
            System.out.println(row.get("Name") + " "+ row.get("Surname"));
        }
        
        
        //input needs to be of the form
        // { Name, Surname, Grade, E_Class, Gender }
        //sample pasted excel data: 
        
        //Seth\tHendrikz\nJane\tDoe
        //or
        //Seth Kenan\nJane\nTommy 
        
        //Grade is set once per class
        //E_Class is set once per class in subject comments
        
        List<String> inputNames = new ArrayList<String>();
        List<String> inputSurnames = new ArrayList<String>();
        inputNames.add("Seth");
        inputNames.add("Jane");
        inputNames.add("Tommy");
        inputSurnames.add("Hendrikz");
        inputSurnames.add("Doe");
        inputSurnames.add("Sushi");
        
        int grade = 12;
        
        int e_class = 8;
        
        
        
        
        
        
    }
}
