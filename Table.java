import java.util.Stack;
/**
 * Table has a dealer, deck, and player(s)
 *
 * @author alvaro
 * @version 3/22/2022
 */
public class Table
{
    private Deck mainDeck = null;
    private Board board = null;
    private Pot pot = null;
    
    /**
     * Holds the deck, burn pile, players, and board
     */
    public Table()
    {
        mainDeck = new Deck();
        //players = new Player[2];
        //numPlayers = 0;
        //burnPile = new Stack();
        board = new Board();
        pot = new Pot();
    }
    
    public Table(String[] names, int startingChips) {
        // if(names == null) {
            // System.err.println("no names in array null");
            // System.exit(0);
        // } else if(names.length == 0) {
            // System.err.println("no names in array 0");
            // System.exit(0);        
        // }
        mainDeck = new Deck();
        //players = new Player[names.length];
        //numPlayers = 0;
        //setPlayers(names, startingChips);
        //burnPile = new Stack();
        board = new Board();
        pot = new Pot();
    }
    
    public void resetGame() {
        mainDeck.reset();
        board.clearBoard();
        //burnPile.clear();
        pot.setPot(0);
    }
    
    public void showPot() {
        System.out.println(pot);
    }
    
    public void displayDeck() {
        mainDeck.showDeck();
    }
    
    public void addBetToPot(int bet) {
        pot.add(bet);
    }
    
    public void showBoard() {
        System.out.println(board.toString());
    }
    
    public void cardsLeftInDeck() {
        System.out.println(mainDeck.length() - mainDeck.getTopIdxValue());
    }
    
    public Card getTopCardFromDeck() {
        return mainDeck.getTopCard();
    }
    
    public void flop() {
        board.muckCard(mainDeck.getTopCard());
        for(int i = 0; i < 3; i++) {
            board.addCardToBoard(mainDeck.getTopCard());
        }
    }
    
    public void turn() {
        board.muckCard(mainDeck.getTopCard());
        board.addCardToBoard(mainDeck.getTopCard());
    }
    
    public void river() {
        board.muckCard(mainDeck.getTopCard());
        board.addCardToBoard(mainDeck.getTopCard());
    }
}
