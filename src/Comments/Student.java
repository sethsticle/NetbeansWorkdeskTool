
package Comments;

/**
 *
 * @author SETH.H
 */
public class Student {
    
    private String firstName, lastName;
    private double patMark, fatMark;
    private String gender;
    private int patRating, fatRating;
    private String generatedComment;

    public Student(String firstName, String lastName, double patMark, double fatMark, String gender, int patRating, int fatRating) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patMark = patMark;
        this.fatMark = fatMark;
        this.gender = gender;
        this.patRating = patRating;
        this.fatRating = fatRating;
    }

    public Student(String firstName, String lastName, double patMark, double fatMark, String gender, int patRating, int fatRating, String generatedComment) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patMark = patMark;
        this.fatMark = fatMark;
        this.gender = gender;
        this.patRating = patRating;
        this.fatRating = fatRating;
        this.generatedComment = generatedComment;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getPatMark() {
        return patMark;
    }

    public void setPatMark(double patMark) {
        this.patMark = patMark;
    }

    public double getFatMark() {
        return fatMark;
    }

    public void setFatMark(double fatMark) {
        this.fatMark = fatMark;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getPatRating() {
        return patRating;
    }

    public void setPatRating(int patRating) {
        this.patRating = patRating;
    }

    public int getFatRating() {
        return fatRating;
    }

    public void setFatRating(int fatRating) {
        this.fatRating = fatRating;
    }

    public String getGeneratedComment() {
        return generatedComment;
    }

    public void setGeneratedComment(String generatedComment) {
        this.generatedComment = generatedComment;
    }

    @Override
    public String toString() {
        return "Student{" + "firstName=" + firstName + ", lastName=" + lastName + ", patMark=" + patMark + ", fatMark=" + fatMark + ", gender=" + gender + ", patRating=" + patRating + ", fatRating=" + fatRating + ", generatedComment=" + generatedComment + '}';
    }
    
    
}
