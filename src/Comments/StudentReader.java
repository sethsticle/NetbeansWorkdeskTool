package Comments;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SETH.H
 */
public class StudentReader {

    public static boolean isSingleColumnPaste(String input) {
        // Remove trailing/leading whitespace
        String trimmed = input.trim();

        // If it contains a tab character → more than 1 column
        if (trimmed.contains("\t")) {
            return false;
        }

        // If it contains at least 2 lines → probably valid
        String[] lines = trimmed.split("\\r?\\n");
        return lines.length > 0;
    }

    public static List<String[]> parseExcelPaste(String pastedData) {
        List<String[]> rows = new ArrayList<>();
        String[] lines = pastedData.strip().split("\\r?\\n|\\t"); // split rows ->//r for old macbooks
        for (String line : lines) {
            String[] cols = line.split("\\t"); // split columns
            rows.add(cols);
        }
        return rows;
    }


//// Example usage for first paste (FAT)
        //    List<String[]> firstPaste = parseExcelPaste(pasteBox1.getText());
        //    for (String[] cols : firstPaste) {
        //    String firstName = cols[0];
        //    String surname = cols[1];
        //    double fat = Double.parseDouble(cols[2]);
        //    students.add(new Student(firstName, surname, 0, fat, "M", 0, 0)); // PAT=0 for now
        //}
        //
        //// Example usage for second paste (PAT)
        //List<String[]> secondPaste = parseExcelPaste(pasteBox2.getText());
        //for (int i = 0; i < secondPaste.size(); i++) {
        //    double pat = Double.parseDouble(secondPaste.get(i)[0]);
        //    students.get(i).setPatMark(pat); // match by row order
        //}
    
}
