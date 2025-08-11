/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comments;

/**
 *
 * @author SETH.H
 */
public class RatingRange {
    private int min, max, rating;
    
    public RatingRange(int min, int max, int rating){
        this.min = min;
        this.max = max;
        this.rating = rating;
    }
    
    public boolean inRange(int mark){
        return mark >= min && mark <= max;
    }
    
    public int getRating(){
        return rating;
    }
}
