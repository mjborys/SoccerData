/* Simple class to represent country
*/
package utils;


public class Country {
    private String name;
    private Integer score;
    
    public Country(String name) {
        this.name = name;
        this.score = new Integer(0);
    }
    
    //add increment to current score
    public void updateScore(Integer inc) {
        this.score = this.score + inc;
    }
    
    public Integer getScore() {
        return this.score;
    }
    
    public String getName() {
        return this.name;
    }
}