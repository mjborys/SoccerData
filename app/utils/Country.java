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
    public void UpdateScore(Integer inc) {
        this.score = this.score + inc;
    }
    
    public Integer GetScore() {
        return this.score;
    }
    
    public String GetName() {
        return this.name;
    }
}