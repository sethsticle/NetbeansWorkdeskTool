
package SQLite1;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SETH.H
 */
public class tempValidationUI {
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

}
