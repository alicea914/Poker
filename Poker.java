// Import the Scanner class
//import java.util.Scanner;
/**
 * Texas Hold'em Poker game (Driver)
 *
 * @author alvaro
 * @version 3/22/2022
 */
public class Poker
{    
    private UserInput input = null;
    private Table mainTable = null;
    private Player[] players = null;    
    private int numPlayers = -1;
    private int smallBlinds = -1;
    private int bigBlinds = -1;
    private int currentPlayerIdx = -1;
    //private static ActionLog log = null;

    public Poker() {
        input = new UserInput();
        //log = new ActionLog();
    }

    public void setMainTable() {
        mainTable = new Table();
    }

    public int getCurrentPlayerChips() {
        return players[currentPlayerIdx].getChips();
    }

    public Player[] getPlayers() {
        return players;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setPlayers(String[] names, int startingChips) {        
        setNumPlayers(names.length);
        players = new Player[numPlayers];
        for(int i = 0; i < numPlayers; i++) {
            players[i] = new Player(names[i]);
            players[i].setChips(startingChips);
        }
    }

    public void setNumPlayers(int n) {
        numPlayers = n;
    }

    //private void soloMultiPrompt() {
     //   System.out.println("Welcome to Texas Hold 'em. Please enter 1 if solo, or 2 if multi");
    //}

    private void determineButton() {
        Deck tempDeck = null;
        Card[] playerCards = null;
        Card tempCard = null;
        int tempMaxValue = 0;
        int currentValue = 0;
        int maxValueIdx = -1;

        //give each player a card face-up & whoever has the highest card is the button
        //if rank is tied, suit rankings from lowest to highest respectively are as follows: 
        //Clubs, Diamonds, Hearts, Spades

        tempDeck = new Deck();
        playerCards = new Card[numPlayers];
        System.out.println("Drawing cards to determine the button");
        printLine();

        for(int i = 0; i < numPlayers; i++) {
            tempCard = new Card(tempDeck.getTopCard(), false);
            //System.out.println(tempCard);
            playerCards[i] = new Card(tempCard);
            currentValue = playerCards[i].getTrueValue();
            System.out.println(players[i].getName() + " " + tempCard);
            if(currentValue > tempMaxValue) {
                tempMaxValue = currentValue;
                maxValueIdx = i;
            }
        }
        setButtonAndBlinds(maxValueIdx);

        printLine();
        System.out.println(players[maxValueIdx].getName() + " is the button!");
        System.out.println(players[(maxValueIdx + 1) % players.length].getName() + " is the small blind.");
        System.out.println(players[(maxValueIdx + 2) % players.length].getName() + " is the big blind.");
        printLine();
    }

    private void setButtonAndBlinds(int idx) {
        players[idx].setButtonStatus(true);
        players[(idx + 1) % players.length].setSmallBlindStatus(true);
        players[(idx + 2) % players.length].setBigBlindStatus(true);
        currentPlayerIdx = (idx + 3) % players.length;
    }

    private void startText() {
        System.out.println("Texas Hold 'em game by Alvaro");
    }

    public void game() {      
        startText();
        //set up table, players, and chips
        String[] inputNames = null;
        int inputChips = -1;
        //int[] blinds = null; // small blind is in index 0, big blind is in index 1
        inputNames = input.promptNames();
        numPlayers = inputNames.length;
        inputChips = input.promptChips();

        //blinds = input.promptBlinds();
        //setBlinds(blinds[0], blinds[1]);        

        setPlayers(inputNames, inputChips);
        setMainTable();

        //mainTable.displayDeck();
        determineButton();

        boolean play = true;
        // startText();

        while(play == true) { 
            //updateButton();
            mainTable.resetGame();
            dealCards();

            // pre-flop - players bet
            //printLine();
            //preFlop();
            System.out.println();
            showPlayerCards();
            System.out.println();
            System.out.println();
            printLine();
            System.out.println("Action on " + players[currentPlayerIdx].getName());
            showCurrentPlayerCards();
            playerAction();         //input.promptAction();
            mainTable.showPot();

            //flop - burn 1 card, flop 3 on board, then players bet
            printLine();
            mainTable.flop();
            mainTable.showBoard();
            System.out.println();
            showPlayerCards();
            System.out.println();
            System.out.println();
            printLine();
            System.out.println("Action on " + players[currentPlayerIdx].getName());
            showCurrentPlayerCards();
            playerAction();         //input.promptAction();
            printLine();
            mainTable.showPot();

            //turn - burn 1 card, turn 1 on board, then players bet
            printLine();
            mainTable.turn();
            mainTable.showBoard();
            System.out.println();
            showPlayerCards();
            System.out.println();
            System.out.println();
            printLine();
            System.out.println("Action on " + players[currentPlayerIdx].getName());
            showCurrentPlayerCards();
            playerAction();         //input.promptAction();
            printLine();
            mainTable.showPot();

            //river - burn 1 card, river 1 on board, then players bet
            printLine();
            mainTable.river();
            mainTable.showBoard();
            System.out.println();
            showPlayerCards();
            System.out.println();
            System.out.println();
            printLine();
            System.out.println("Action on " + players[currentPlayerIdx].getName());
            showCurrentPlayerCards();
            playerAction();         //input.promptAction();
            printLine();
            mainTable.showPot();

            //reveal, check for plays, and designate winner
            printLine();
            mainTable.showBoard();
            System.out.println();
            
            revealAllHands();
            printLine();
            //play again? y = reset, n = break
            play = input.promptPlay();
            //play = playAgain();            
        }
    }

    public void showCurrentPlayerCards() {
        System.out.println(players[currentPlayerIdx]);//.showHand());
        players[currentPlayerIdx].hideHand();
    }

    public void dealCards() {
        for(int i = 0; i < numPlayers; i++) {
            players[i].addCard(mainTable.getTopCardFromDeck(), 0);
        }
        for(int i = 0; i < players.length; i++) {
            players[i].addCard(mainTable.getTopCardFromDeck(), 1);
        }
    }

    public void showPlayerCards() {
        if(players == null)
            System.err.println("Error, index is out of bounds");
        else {
            for(int i = 0; i < players.length; i++) {
                //printLine();
                if(i != currentPlayerIdx)                
                    System.out.println(players[i].showPlayerInfo(true));
            }
            printLine();
        }
    }

    private void preFlop() {
        System.out.println("Pre-flop");
    }

    private void printLine() {
        System.out.println("--------------------------------------------------------------------------------");
    }

    private void revealAllHands() {
        //System.out.println("reveal:");
        if(players == null)
            System.err.println("Error, index is out of bounds");
        else {
            for(int i = 0; i < players.length; i++) {
                //if(i == currentPlayerIdx) {
                System.out.println(players[i].showPlayerInfo(false));
                //} else
                //System.out.println(players[i].showPlayerInfo(false));
            }
        }
    }

    //private void evaluate(Table table) {
        //high card
        //In a board + hand of mixed faces & suits, the player with the highest card(s) wins
        //put both players' hands and boards in separate arrays of 7
        //sort array and check largest cards
        //if highest 5 cards are board only, both players tie
        //if one player has highest card and the other doesn't, the first player wins
        //if players have highest card, their next card determines the winner
        //if players have highest card, but low cards are lower than 4 on the board, tie        

        //pair
        //Check board for pairs then check players hands for pairs
        //if highest pair is on board, the player with the highest card wins
        //if one player has a pair and the other doesn't have a pair, they win
        //if both players have a pair, the highest card wins
        //if one player has a pocket pair, and other has one with board, highest pair wins
        //if both players pair and highest cards are the same, its a tie

        //2-Pair
        //same as pair, but this time, check for 2 pairs
        //if one player has 2 pairs, and the other doesn't, the first player wins
        //if both players have the same pairs, their highest card wins
        //if both players have the same pairs and highest cards, it's a tie

        //3 of a kind
        //check hand & board for set/trips
        //player with highest 3 of a kind wins
        //if both have same 3 of a kind, the player with the highest card(s) wins
        //if both have same 3 of a kind and same cards, its a tie

        //straight
        //check hand and board for 5 consecutive numbers regardless of suit
        //player with highest card(s) in the straight wins
        //if entire board is straight and players hands don't add to the straight, tie

        //flush
        //check hand and board for 5 cards with the same suit
        //player with highest card in the flush wins
        //if the board is flush, the player with the highest card in the flush wins
        //if the board is flush and players don't have a card with the same suit, tie

        //full house
        //check hand and board for 3 of a kind and pair
        //player with highest card in 3 of a kind win

        //4 of a kind
        //check players hand and board for 4 of a kind
        //player with highest 4 of a kind wins
        //if there is a 4 of a kind on the board, the player with the highest card wins
        //if 4 of a kind on board and players cards are lower than last card on board, tie

        //straight flush
        //check players hand and board for 5 consecutive cards with the same suit
        //player with highest card wins

        //royal flush
        //check players hand and board for 5 consecutive cards with the same suit
        //has to be AKQJ10 to win
        //poker
    //}

    private void playerAction() {
        String actionString = "";
        //ActionFactory af = null;
        printPrompt();
        actionString = input.askForInput();  
        //af = new ActionFactory(actionString);
        //Action playerAction = af.getAction();
        //playerAction.executeAction(mainTable, players, currentPlayerIdx)
        executeAction(actionString);        
    }

    /*
     * Executes action based on the type of Action object and stores action to ActionLog
     */

    private void executeAction(Action a) {
        String actionType = a.getActionString();

        switch(actionType) {
            case "Call":
                System.out.println("Call");
                break;
            case "Bet":  
                playerBet();
                break;
            case "Fold":
                System.out.println("Fold");
                break;
            case "Raise":
                System.out.println("Raise");
                break;
            case "AllIn":
                System.out.println("All in!");
                break;                
            case "Check":
                System.out.println("Check");
                break;
            case "LeaveTable":
                System.out.println("Left Table");
            case "Exit":
                System.out.println("Exit");    
                System.exit(0);
                break;
            case "Options":
                printOptions();
                break;
            default:
                System.out.println("Default");
                break;
        }
    }

    private void playerBet() {
        //String currPlayer = getCurrentPlayerName();
        int bet = getBetAmount();                
        players[currentPlayerIdx].setChips(getCurrentPlayerChips()-bet);
        mainTable.addBetToPot(bet);
    }

    private String getCurrentPlayerName() {
        return players[currentPlayerIdx].getName();    
    }

    //private void bet() {

    //}

    private int getBetAmount() {
        return input.promptBetAmount(bigBlinds, players[currentPlayerIdx].getChips());  
    }

    private void executeAction(String actionString) {
        executeActionRecursive(actionString);
    }

    private void printPrompt() {
        System.out.println("Enter 0 - 6, o for options, or * to quit");
    }

    private void printOptions() {
        System.out.println("Call=0\nBet=1\nFold=2\nRaise=3\nAll in=4\nCheck=5\n" + 
            "Leave Table=6\nQuit=*\n");  
    }

    private void executeActionRecursive(String actionString) {
        String recursive = "";
        switch(actionString) {            
            case "0":
                System.out.println("Call");
                break;
            case "1":
                System.out.println("Bet");
                break;
            case "2":
                System.out.println("Fold");
                break;
            case "3":
                System.out.println("Raise");
                break;
            case "4":
                System.out.println("All in!");
                break;                
            case "5":
                System.out.println("Check");
                break;
            case "6":
                System.out.println("Lave Table");
            case "*":
                System.out.println("Exit");    
                System.exit(0);
                break;
            case "o":
                printOptions();
                printPrompt();
                recursive = input.askForInput();
                executeActionRecursive(recursive);
                break;
            default:
                printPrompt();
                recursive = input.askForInput();
                executeActionRecursive(recursive);
                break;
        }
    }
}