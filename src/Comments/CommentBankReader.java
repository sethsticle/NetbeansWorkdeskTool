//package Comments;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.io.File;
//import java.io.IOException;
//import java.util.Map;
//
///**
// *
// * @author SETH.H
// */
//public class CommentBankReader {
//
//    private Map<String, Map<String, String>> commentBank;
//
//    /**
//     * Reads the given JSON file into the commentBank map.
//     *
//     * @param filePath Path to commentBank.json
//     * @throws IOException If file reading or parsing fails
//     */
//    
//    public CommentBankReader(String filePath) throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        commentBank = mapper.readValue(
//                new File(filePath),
//                new TypeReference<Map<String, Map<String, String>>>() {
//        }
//        );
//    }
//
//    public void debugPrint() {
//        commentBank.forEach((category, map) -> {
//            System.out.println("Category: " + category);
//            map.forEach((key, value) -> System.out.println("  " + key + " → " + value));
//        });
//    }
//
//    // ======= Generic access =======
//    public Map<String, String> getCategory(String categoryName) {
//        return commentBank.get(categoryName);
//    }
//
//    public String getValue(String categoryName, String key) {
//        Map<String, String> category = commentBank.get(categoryName);
//        if (category == null) {
//            return null;
//        }
//        return category.get(key);
//    }
//
//    // ======= Specific helpers =======
//    public String getPatRatingResult(int rating) {
//        return getValue("patRatingResults", String.valueOf(rating));
//    }
//
//    public String getFatRatingResult(int rating) {
//        return getValue("fatRatingResults", String.valueOf(rating));
//    }
//
//    public String getAcademicContentCurrent(String key) {
//        return getValue("academicContentCurrent", key);
//    }
//
//    public String getAcademicContentNext(String key) {
//        return getValue("academicContentNext", key);
//    }
//
//    public String getIntroPhrase(String key) {
//        return getValue("introPhrases", key);
//    }
//
//    public String getEndingPhrase(String key) {
//        return getValue("endingPhrases", key);
//    }
//}
//
//
