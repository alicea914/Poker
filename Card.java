
/**
 * Playing Card. Can be 2-10 or AJQK with suits heart, diamond, spade, or club
 *
 * @author alvaro
 * @version 3/22/2022
 */
public class Card {    
    private int rank = -1; // Ace = 1, Jack = 11, Queen = 12, King = 13
    private int suit = -1; // Club = 0, Diamond = 1, Heart = 2, Spade = 3
    private boolean facedown = true;

    public Card() {}

    public Card(int r, int s) {
        setRank(r);
        setSuit(s);
    }

    public Card(int r, int s, boolean f) {
        setRank(r);
        setSuit(s);
        setFacedown(f);
    }

    public Card(Card c) {
        setRank(c.getRank());
        setSuit(c.getSuit());
        setFacedown(c.isFacedown());
    }

    public Card(Card c, boolean f) {
        setRank(c.getRank());
        setSuit(c.getSuit());
        setFacedown(f);
    }
    
    public int getTrueValue() {
        //returns value of card from 1 - 52
        //rank order: 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K, A
        //suit order: Clubs, Diamonds, Hearts, Spades
        //examples: 2 of clubs = 1, ace of spades = 52, ace of diamonds = 26
        if(getRank() == 1) {
            return(13 + (13 * getSuit()));
        } else {
            return ((getRank() - 1) + (13 * getSuit()));
        }
    }

    public boolean isEqual(Card c) {
        if(c.getRank() == getRank() && c.getSuit() == getSuit())
            return true;
        else
            return false;
    }    

    public int compareTo(Card c) {
        if(c.getRank() == getRank() && c.getSuit() == getSuit()) {
            return 0;
        } else if (getRank() > c.getRank()) {
            return 1;
        } else if(getRank() == c.getRank()){
            if(getSuit() > c.getSuit()) {
                return 1;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public int getRank() {
        return rank;
    }

    public int getSuit() {
        return suit;
    }

    public boolean isFacedown() {
        return facedown;
    }

    public void setFacedown(boolean f) {
        facedown = f;
    }

    public void setRank(int r) {
        rank = r;
    }

    public void setSuit(int s) {
        suit = s;
    }

    public String toString() {
        if(rank == -1 || suit == -1)
            return "INVALID CARD";
        else {
            if(isFacedown()) 
                return "[  ]";
            else
                return printRank() + printSuit();    
        }
    }

    private String printRank() {
        switch (rank) {
            case 1:
                return "[A";
            case 11:
                return "[J";
            case 12:
                return "[Q";
            case 13:
                return "[K";
            default:
                return ("[" + rank);
        }
    }

    private String printSuit() {
        switch (suit) {
            case 0:
                return "C]";
            case 1:
                return "D]";
            case 2:
                return "H]";
            case 3:
                return "S]";
            default:
                return "INVALID SUIT";
        }
    }
}
