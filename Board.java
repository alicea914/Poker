import java.util.*;
/**
 * Board showing current cards at play (Texas Holdem)
 *
 * @author alvaro licea sanchez jr
 * @created 3/2/2022
 * @version 11/16/2026 
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

    /**
     * Returns copies of the cards currently dealt to the board.
     * @return array of board cards dealt so far (length equals cards on board)
     */
    public Card[] getBoardCards() {
        Card[] retVal = new Card[boardIndex];
        for(int i = 0; i < boardIndex; i++) {
            retVal[i] = new Card(currBoard[i]);
        }
        return retVal;
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