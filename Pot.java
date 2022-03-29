
/**
 * Write a description of class Pot here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Pot
{
    private int total = 0;
    
    public Pot() {
        total = 0;
    }
    
    public Pot(int bets) {
        total = bets;
    }
    
    public int getPot() {
        return total;
    }
    
    public void setPot(int p) {
        total = p;
    }
    
    public void add(int b) {
        total = total + b;
    }
    
    public String toString() {
        return ("Pot size = " + total);
    }
}
