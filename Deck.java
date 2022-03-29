import java.util.concurrent.ThreadLocalRandom;
import java.util.Random;

/**
 * Deck of 52 Cards (See Card class for description of a single card)
 *
 * @author alvaro
 * @version 3/22/2022
 */
public class Deck
{
    private Card[] cards;   // array of 52 Card objects
    private int top = 0;    // points to the top of the deck
    private static int SUITS = 4;  // number of suits in the deck (H, D, S, C)
    public static int FACES = 13;  // number of faces in the deck (2-10, A, J, Q, & K)
    
    public Deck() {
        //Build 52-card deck
        build();
        //Shuffle
        shuffle();
    }
    
    public Card getTopCard() {
        if(cards.length == 0 || top == (cards.length)) {
            System.err.println("No cards in deck");
            return null;
        } else {            
            Card retVal = new Card(cards[top]);            
            top++;
            //System.out.println("getTopCard() in Deck and top value: " + retVal + " " + top);
            return retVal;
        }
    }
    
    public int getTopIdxValue() {
        return top;
    }
    
    public int length() {
        return (cards.length - top);
    }
    
    public void reset() {
        top = 0;
        shuffle();
    }
    
    private void build() {
        cards = new Card[52];
        for(int i = 0; i < SUITS; i++) {
            for(int j = 1; j <= FACES; j++) { 
                //System.out.println("i: " + i + " j: " + j);
                cards[top] = new Card(j, i);
                //System.out.println("top: " + top + " card: " + cards[top]);
                top++;
            }
        }
        top = 0;
    }
    
    public void showDeck() {
        for(int i = 0; i < cards.length; i++) {
            System.out.println(cards[i]);
        }
    }
    
    public void shuffle() {
        if(cards.length == 0) {
            System.err.println("No cards to shuffle");
        } else if(top < (cards.length - 1)) {
            Random rnd = ThreadLocalRandom.current();
            for(int i = (cards.length - 1); i > top; i--) {
                int idx = rnd.nextInt(i + 1);
                // swap
                Card swap = new Card(cards[idx]);
                cards[idx] = new Card(cards[i]);
                cards[i] = new Card(swap);
            }
        }
    }
    
    public String toString() {
        String retVal = ("Top = " + top + "\n");
        for(int i = 0; i < cards.length; i++) {
            retVal += (cards[i] + " ");
        }        
        return retVal;
    }
    
    public boolean isEmpty() {
        if (cards == null) {
            return true;
        } else if (top < cards.length) {
            return false;
        } else
            return true;
    }
}
