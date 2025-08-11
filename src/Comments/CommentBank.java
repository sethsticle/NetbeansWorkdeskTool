/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comments;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author SETH.H
 */
public class CommentBank {

    private List<String> patRatingResults;
    private List<String> fatRatingResults;
    private List<String> academicContentCurrent;
    private List<String> academicContentNext;
    private List<String> termStartingContent;
    private List<String> termEndingContent;
    private List<RatingRange> patRatingRanges = List.of(
            new RatingRange(0, 39, 1),
            new RatingRange(40, 59, 2),
            new RatingRange(60, 79, 3),
            new RatingRange(80, 89, 4),
            new RatingRange(90, 100, 5)
    );

    public int getPatRating(int mark) {
        return patRatingRanges.stream()
                .filter(r -> r.inRange(mark))
                .findFirst()
                .map(RatingRange::getRating)
                .orElseThrow(() -> new IllegalArgumentException("Mark out of range: " + mark));
    }
    
    
    
    

}

