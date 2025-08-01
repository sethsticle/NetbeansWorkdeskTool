
package Objects;

/**
 *
 * @author SETH.H
 */
public class UpcomingDates 
{
        private String id;
        private String grade;
        private String test_info;
        private String test_date;  // Format: yyyy-MM-dd

    public UpcomingDates(String id, String grade, String test_info, String test_date) {
        this.id = id;
        this.grade = grade;
        this.test_info = test_info;
        this.test_date = test_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getTest_info() {
        return test_info;
    }

    public void setTest_info(String test_info) {
        this.test_info = test_info;
    }

    public String getTest_date() {
        return test_date;
    }

    public void setTest_date(String test_date) {
        this.test_date = test_date;
    }
        
        
        
}
