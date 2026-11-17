// Import the Scanner class
//import java.util.Scanner;
import java.util.ArrayList;

/**
 * Texas Hold'em Poker game (Driver)
 *
 * @author alvaro licea sanchez jr
 * @created 3/2/2022
 * @version 11/16/2026
 */
public class Poker
{    
    private UserInput input = null;
    private Table mainTable = null;
    private Player[] players = null;    
    private int numPlayers = -1;
    private int smallBlind = -1;
    private int bigBlind = -1;
    private int currentPlayerIdx = -1;
    private int dealerIdx = -1;  // Tracks the button/dealer position

    public Poker() {
        input = new UserInput();
    }

    public void setMainTable() {
        mainTable = new Table();
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

    private int determineButton() {
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
        
        return maxValueIdx;
    }

    private void setButtonAndBlinds(int idx) {
        // Clear ALL players' flags first
        for (Player p : players) {
            p.setButtonStatus(false);
            p.setSmallBlindStatus(false);
            p.setBigBlindStatus(false);
            p.resetRoundState();
        }
        
        players[idx].setButtonStatus(true);
        players[(idx + 1) % players.length].setSmallBlindStatus(true);
        players[(idx + 2) % players.length].setBigBlindStatus(true);
        currentPlayerIdx = (idx + 3) % players.length;
    }

    private void startText() {
        System.out.println("Texas Hold 'em game by Alvaro Licea Jr. ");
    }

    public void game() {      
        startText();
        //set up table, players, and chips
        String[] inputNames = null;
        int inputChips = -1;
        //int[] blinds = null; // small blind is in index 0, big blind is in index 1
        inputNames = input.promptNames();
        if (inputNames == null || inputNames.length == 0) {
            System.out.println("No players entered. Exiting.");
            System.exit(0);
        }

        numPlayers = inputNames.length;
        inputChips = input.promptChips();
        int[] blinds = input.promptBlinds();

        smallBlind = blinds[0];
        bigBlind = blinds[1];
        //dealerIdx = (dealerIdx + 1) % players.length;

        //DEBUG: verify blind amounts are set
        //System.err.println("=== DEBUG game() START ===");
        //System.err.println("DEBUG: smallBlinds = " + smallBlind);
        //System.err.println("DEBUG: bigBlinds = " + bigBlind);

        //blinds = input.promptBlinds();
        //setBlinds(blinds[0], blinds[1]);        
        
        //DEBUG: verify mainTable is not null
        setPlayers(inputNames, inputChips);
        setMainTable();

        boolean play = true;
        
        while(play == true) { 
            //updateButton();
            mainTable.resetGame();
            dealCards();
            dealerIdx = determineButton();
            setButtonAndBlinds(dealerIdx);
            postBlinds();
            //PRE-FLOP BETTING ROUND

            System.out.println();
            showPlayerCards();
            System.out.println();
            System.out.println();
            printLine();
            runBettingRound(bigBlind);

            mainTable.getPot();

            //flop - burn 1 card, flop 3 on board, then players bet
            printLine();
            mainTable.flop();
            mainTable.showBoard();
            System.out.println();
            showPlayerCards();
            System.out.println();
            System.out.println();
            printLine();
            runBettingRound(0);
            printLine();
            mainTable.getPot();

            //turn - burn 1 card, turn 1 on board, then players bet
            printLine();
            mainTable.turn();
            mainTable.showBoard();
            System.out.println();
            showPlayerCards();
            System.out.println();
            System.out.println();
            printLine();
            runBettingRound(0);
            printLine();
            mainTable.getPot();

            //river - burn 1 card, river 1 on board, then players bet
            printLine();
            mainTable.river();
            mainTable.showBoard();
            System.out.println();
            showPlayerCards();
            System.out.println();
            System.out.println();
            printLine();
            runBettingRound(0);
            printLine();
            mainTable.getPot();

            //reveal, check for plays, and designate winner
            printLine();
            mainTable.showBoard();
            System.out.println();
            
            revealAllHands();
            awardPot();
            printLine();

            play = input.promptPlay();
        }
    }

    private void postBlinds() {
    
        int sbIdx = (dealerIdx + 1) % players.length;
        int bbIdx = (dealerIdx + 2) % players.length;

        int sbAmount = Math.min(smallBlind, players[sbIdx].getChips());
        players[sbIdx].setChips(players[sbIdx].getChips() - sbAmount);
        players[sbIdx].setLastBetAmount(sbAmount);
        mainTable.addBetToPot(sbAmount);

        int bbAmount = Math.min(bigBlind, players[bbIdx].getChips());
        players[bbIdx].setChips(players[bbIdx].getChips() - bbAmount);
        players[bbIdx].setLastBetAmount(bbAmount);
        mainTable.addBetToPot(bbAmount);

        System.out.println(players[sbIdx].getName() + " posts small blind: " + sbAmount);
        System.out.println(players[bbIdx].getName() + " posts big blind: " + bbAmount);
    
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

    private void printLine() {
        System.out.println("--------------------------------------------------------------------------------");
    }

    // Count how many players are still in the hand
    private int countActivePlayers() {
        int count = 0;
        for(Player p : players) {
            if(p != null && !p.isFolded() && (p.getChips() > 0 || p.isAllin())) {
                count++;
            }
        }
        return count;
    }

    // Advance to the next player, skipping folded players
    private void nextPlayer() {
        do {
            currentPlayerIdx = (currentPlayerIdx + 1) % players.length;
        } while (players[currentPlayerIdx].isFolded() || (players[currentPlayerIdx].getChips() <= 0 && !players[currentPlayerIdx].isAllin()));
    }

    /**
     * Runs a single betting round (Pre-flop, Flop, Turn, River).
     * @param streetBet The forced bet amount for this street (bigBlind for pre-flop, 0 for others).
     */
    private void runBettingRound(int streetBet) {
        int currentBet = streetBet;
        int activePlayers = countActivePlayers();
        
        // If only 1 player left, skip the round
        if (activePlayers <= 1) {
            return;
        }

        // Determine starting player based on the street
        // Pre-flop: Start left of the Big Blind (dealerIdx + 3)
        // Post-flop: Start left of the Dealer (dealerIdx + 1)
        boolean preFlop = (streetBet == bigBlind);
        if (preFlop) {
            currentPlayerIdx = (dealerIdx + 3) % players.length;
        } else {
            currentPlayerIdx = (dealerIdx + 1) % players.length;
        }

        // Reset street bet tracking.
        // Pre-flop: blinds KEEP their posted amounts; everyone else starts at 0.
        // Post-flop: everyone (including former blinds) starts at 0.
        for (Player p : players) {
            if (p != null && !p.isFolded()) {
                if (preFlop && (p.getSmallBlindStatus() || p.getBigBlindStatus())) {
                    // posted blind amount stays as-is
                } else {
                    p.setLastBetAmount(0);
                }
            }
        }

        boolean roundOver = false;
        int playersActed = 0;
        int maxActions = activePlayers * 4; // Safety cap to prevent infinite loops

        while (!roundOver && playersActed < maxActions) {
            Player currentPlayer = players[currentPlayerIdx];

            // Skip folded or all-in players
            if (currentPlayer.isFolded() || currentPlayer.isAllin() || currentPlayer.getChips() <= 0) {
                nextPlayer();
                continue;
            }

            // Display game state
            printOptions();
            System.out.println("Action on " + currentPlayer.getName() + 
                " | Pot: " + mainTable.getPot().getPot() + 
                " | Current Bet: " + currentBet + 
                " | Your Chips: " + currentPlayer.getChips() +
                " | Your Street Bet: " + currentPlayer.getLastBetAmount());
            showCurrentPlayerCards();

            // Get user input
            String actionInput = input.askForInput();
            
            // Execute action; -1 means invalid/no action consumed (same player retries)
            int result = executeAction(actionInput, currentBet);
            if (result == -1) {
                continue;
            }
            currentBet = result;

            // Check if round is complete
            if (allPlayersMatched(currentBet)) {
                roundOver = true;
            }

            playersActed++;
            nextPlayer();
        }

        // Final sync: active players' street bets reflect the final currentBet
        for (Player p : players) {
            if (p != null && !p.isFolded() && !p.isAllin()) {
                p.setLastBetAmount(currentBet);
            }
        }
    }

    /**
     * Helper: Checks if all active players have matched the current bet.
     * Returns true if every active player has either matched the bet or is all-in.
     */
    private boolean allPlayersMatched(int currentBet) {
        int activeCount = 0;
        boolean everyoneMatched = true;
        
        for (Player p : players) {
            if (p != null && !p.isFolded() && (p.getChips() > 0 || p.isAllin())) {
                activeCount++;
                // If a player hasn't matched the bet and isn't all-in, round isn't over
                if (p.getLastBetAmount() < currentBet && !p.isAllin()) {
                    everyoneMatched = false;
                }
            }
        }
        
        // Round is over if only 1 player remains OR everyone has matched
        return activeCount <= 1 || everyoneMatched;
    }

    private void revealAllHands() {
        if(players == null)
            System.err.println("Error, index is out of bounds");
        else {
            for(int i = 0; i < players.length; i++) {
                System.out.println(players[i].showPlayerInfo(false));
            }
        }
    }

    /**
     * Awards the pot: uncontested win if one player remains,
     * otherwise evaluates showdown hands and splits ties.
     */
    private void awardPot() {
        int potSize = mainTable.getPot().getPot();

        ArrayList<Player> contenders = new ArrayList<Player>();
        for (Player p : players) {
            if (p != null && !p.isFolded()) {
                contenders.add(p);
            }
        }

        if (contenders.size() == 0) {
            System.err.println("No players remain. Pot is discarded.");
            mainTable.getPot().setPot(0);
            return;
        }

        if (contenders.size() == 1) {
            Player winner = contenders.get(0);
            System.out.println("\n*** HAND COMPLETE ***");
            System.out.println(winner.getName() + " wins $" + potSize + " (uncontested)");
            winner.setChips(winner.getChips() + potSize);
            mainTable.getPot().setPot(0);
            winner.setAllin(false);
            return;
        }

        Card[] boardCards = mainTable.getBoardCards();
        if (boardCards.length < 5) {
            // Safety fallback: board incomplete, split evenly
            System.err.println("Board incomplete - splitting pot evenly");
            int split = potSize / contenders.size();
            for (Player p : contenders) {
                p.setChips(p.getChips() + split);
            }
            mainTable.getPot().setPot(0);
            return;
        }

        System.out.println("\n*** SHOWDOWN ***");
        int bestScore = -1;
        ArrayList<Player> winners = new ArrayList<Player>();
        for (Player p : contenders) {
            Card[] hole = p.getHand();
            Card[] seven = new Card[7];
            seven[0] = hole[0];
            seven[1] = hole[1];
            for (int i = 0; i < 5; i++) {
                seven[i + 2] = boardCards[i];
            }
            int score = HandEvaluator.evaluate(seven);
            System.out.println(p.getName() + " shows " + hole[0] + " " + hole[1] +
                " -> " + HandEvaluator.categoryName(score));

            if (score > bestScore) {
                bestScore = score;
                winners.clear();
                winners.add(p);
            } else if (score == bestScore) {
                winners.add(p);
            }
        }

        int split = potSize / winners.size();
        int remainder = potSize % winners.size();
        System.out.println("\n*** HAND COMPLETE ***");
        for (int i = 0; i < winners.size(); i++) {
            Player w = winners.get(i);
            int share = split + ((i == 0) ? remainder : 0);
            System.out.println(w.getName() + " wins $" + share +
                " with " + HandEvaluator.categoryName(bestScore) + "!");
            w.setChips(w.getChips() + share);
        }
        mainTable.getPot().setPot(0);

        for (Player p : players) {
            p.setAllin(false);
        }
    }

    /**
     * Executes the current player's action. Bets and raises are "total for
     * this street" (raise-to) semantics.
     * @param actionString raw user input code
     * @param currentBet the current bet total for this street
     * @return the new currentBet, or -1 if no action was consumed (retry same player)
     */
    private int executeAction(String actionString, int currentBet) {
        Player p = players[currentPlayerIdx];

        switch (actionString) {

            case "0": { // Call
                int callAmount = currentBet - p.getLastBetAmount();
                if (callAmount <= 0) {
                    System.out.println("Nothing to call! Use Check (5) instead.");
                    return -1;
                }
                int contribution = Math.min(callAmount, p.getChips());
                p.setChips(p.getChips() - contribution);
                mainTable.addBetToPot(contribution);
                p.setLastBetAmount(p.getLastBetAmount() + contribution);
                if (p.getChips() == 0) {
                    p.setAllin(true);
                    System.out.println(p.getName() + " calls All-In for " + contribution + "!");
                } else {
                    System.out.println(p.getName() + " calls " + contribution);
                }
                break;
            }

            case "1":   // Bet (open)
            case "3": { // Raise
                if (actionString.equals("1") && currentBet > 0) {
                    System.out.println("There's already a bet of " + currentBet + ". Use Raise (3).");
                    return -1;
                }
                if (actionString.equals("3") && currentBet == 0) {
                    System.out.println("No bet to raise. Use Bet (1).");
                    return -1;
                }
                int minRaiseTo = (currentBet == 0) ? bigBlind : currentBet + bigBlind;
                int maxTotal = p.getLastBetAmount() + p.getChips();
                int raiseTo = input.promptBetAmount(minRaiseTo, maxTotal);
                int delta = raiseTo - p.getLastBetAmount();
                p.setChips(p.getChips() - delta);
                mainTable.addBetToPot(delta);
                p.setLastBetAmount(raiseTo);
                currentBet = raiseTo;
                if (p.getChips() == 0) {
                    p.setAllin(true);
                    System.out.println(p.getName() + " is All-In, total street bet " + raiseTo + "!");
                } else if (actionString.equals("1")) {
                    System.out.println(p.getName() + " bets " + raiseTo);
                } else {
                    System.out.println(p.getName() + " raises to " + raiseTo);
                }
                break;
            }

            case "2": // Fold
                p.setFolded(true);
                System.out.println(p.getName() + " folds");
                break;

            case "4": { // All-In
                int contribution = p.getChips();
                mainTable.addBetToPot(contribution);
                p.setChips(0);
                p.setAllin(true);
                p.setLastBetAmount(p.getLastBetAmount() + contribution);
                if (p.getLastBetAmount() > currentBet) {
                    currentBet = p.getLastBetAmount();
                }
                System.out.println(p.getName() + " goes All-In for " + contribution + "!");
                break;
            }

            case "5": // Check
                if (currentBet > p.getLastBetAmount()) {
                    System.out.println("You must call or raise! (Current bet: " + currentBet +
                        ", Your street bet: " + p.getLastBetAmount() + ")");
                    return -1;
                }
                System.out.println(p.getName() + " checks");
                break;

            case "6": // Leave Table
                System.out.println(p.getName() + " leaves the table");
                p.setFolded(true);
                break;

            case "*": // Exit
                System.out.println("Exiting game...");
                System.exit(0);
                break;

            case "o": // Options
                printOptions();
                return -1; // Don't advance turn

            default:
                System.out.println("Invalid action. Enter 0-6, o for options, or * to quit.");
                return -1;
        }
        
        return currentBet;
    }

    private void printOptions() {
        System.out.println("Call\t0\nBet\t1 (total)\nFold\t2\nRaise\t3 (raise TO)\nAll in\t4\nCheck\t5\n" + 
            "Leave\t6\nQuit\t*\n");  
    }
}