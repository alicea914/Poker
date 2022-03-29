
import java.util.*;
/**
 * Board showing current cards at play (Texas Holdem)
 *
 * @author alvaro
 * @version 3/2/2022
 */
public class Board
{
    private Card[] currBoard = null;
    private Stack<Card> muckPile = null;
    private int boardIndex =  -1;
     
    public Board()
    {
        clearBoard();
    }
    
    public void muckCard(Card a) {
        muckPile.push(a);
    }

    public void addCardToBoard(Card a) {
        currBoard[boardIndex] = new Card(a.getRank(), a.getSuit(), false);
        //System.out.println("Board Index in addCardToBoard method: " + boardIndex);
        boardIndex++;
    }
    
    public void clearBoard() {
        currBoard = new Card[5];
        muckPile = new Stack<Card>();
        boardIndex = 0;
    }
    
    public String toString() {
        if(boardIndex < 0 || (boardIndex > (currBoard.length))) {
            return ("Board Index out of bounds");        
        } else {
            String retVal = "Board: ";
            for (int i = 0; i < boardIndex; i++) {
                retVal += currBoard[i].toString();
            }
            return retVal;
        }        
    }
}
