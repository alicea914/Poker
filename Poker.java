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
    private int smallBlind = -1;
    private int bigBlind = -1;
    private int currentPlayerIdx = -1;
    private int lastBetAmount = -1;
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
        numPlayers = inputNames.length;
        inputChips = input.promptChips();
        int[] blinds = input.promptBlinds();

        smallBlind = blinds[0];
        bigBlind = blinds[1];
        
        //DEBUG: verify blind amounts are set
        System.err.println("=== DEBUG game() START ===");
        System.err.println("DEBUG: smallBlinds = " + smallBlind);
        System.err.println("DEBUG: bigBlinds = " + bigBlind);

        //blinds = input.promptBlinds();
        //setBlinds(blinds[0], blinds[1]);        
        
        //DEBUG: verify mainTable is not null
        setPlayers(inputNames, inputChips);
        setMainTable();

        System.err.println("DEBUG: mainTable is null? " + (mainTable == null));
        
        if (mainTable != null) {
            System.err.println("DEBUG: mainTable.getPot is null? " + (mainTable.getPot() == null));
        }

        //mainTable.displayDeck();
        int dealerIdx = determineButton();
        setButtonAndBlinds(dealerIdx);

        //DEBUG: verify pot after blinds
        System.err.println("DEBUG: Pot after blinds = " + mainTable.getPot().getPot());
        System.err.println("=== DEBUG game() END ===");
    
        boolean play = true;
        // startText();

        while(play == true) { 
            //updateButton();
            mainTable.resetGame();
            dealCards();

            setButtonAndBlinds(dealerIdx);
            
            //PRE-FLOP BETTING ROUND

            //printLine();
            //preFlop();
            System.out.println();
            showPlayerCards();
            System.out.println();
            System.out.println();
            printLine();
            System.out.println("Action on " + players[currentPlayerIdx].getName());
            showCurrentPlayerCards();
            runBettingRound(bigBlind);

            //playerAction();         //input.promptAction();
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
            System.out.println("Action on " + players[currentPlayerIdx].getName());
            showCurrentPlayerCards();
            runBettingRound(0);
            playerAction();         //input.promptAction();
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
            System.out.println("Action on " + players[currentPlayerIdx].getName());
            showCurrentPlayerCards();
            runBettingRound(0);
            playerAction();         //input.promptAction();
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
            System.out.println("Action on " + players[currentPlayerIdx].getName());
            showCurrentPlayerCards();
            runBettingRound(0);
            playerAction();         //input.promptAction();
            printLine();
            mainTable.getPot();

            //reveal, check for plays, and designate winner
            printLine();
            mainTable.showBoard();
            System.out.println();
            
            revealAllHands();
            printLine();
            //play again? y = reset, n = break
            play = input.promptPlay();
            //play = playAgain();

            //resetBlindFlags();
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

    // Count how many players are still in the hand
    private int countActivePlayers() {
        int count = 0;
        for(Player p : players) {
            if(p != null && !p.isFolded() && p.getChips() > 0) {
                count++;
            }
        }
        return count;
    }

    // Check if all active players have matched the current bet
    private boolean allPlayersMatched(int currentBet) {
        for (Player p : players) {
            if(p != null && !p.isFolded() && p.getChips() > 0) {
                if(p.getLastBetAmount() != currentBet && !p.isAllin()) {
                    return false;
                }
            }
        }
        return true;
    }

    // Advance to the next player, skipping folded players
    private void nextPlayer() {
        do {
            currentPlayerIdx = (currentPlayerIdx + 1) % players.length;
        } while (players[currentPlayerIdx].isFolded() || players[currentPlayerIdx].getChips() <= 0);
    }

    private void runBettingRound(int currentBet) {
        int playersActed = 0;
        int activePlayers = countActivePlayers();
        int maxRounds = activePlayers * 2;
        
        // Reset lastBetAmount for all players at start of round
        for (Player p : players) {
            if (p != null) {
                p.setLastBetAmount(0);
            }
        }

        while (activePlayers > 1 && playersActed < maxRounds) {
            Player currentPlayer = players[currentPlayerIdx];

            //skip if folded or all-in
            if(currentPlayer.isFolded() || currentPlayer.isAllin()) {
                nextPlayer();
                continue;
            }

            System.out.println("Action on " + currentPlayer.getName() + " (Current Bet: " + currentBet + ")");
            showCurrentPlayerCards();

            String actionInput = input.askForInput();
            currentBet = executeAction(actionInput, currentBet);

            playersActed++;

            if(allPlayersMatched(currentBet)) {
                break;
            }

            nextPlayer();
        }
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
        //printPrompt();
        printPrompt();
        actionString = input.askForInput();  
        //af = new ActionFactory(actionString);
        //Action playerAction = af.getAction();
        //playerAction.executeAction(mainTable, players, currentPlayerIdx)
        executeAction(actionString);        
    }

    private void playerAction(int currentBet) {
        String actionString = "";
        printPrompt(); //show the action options
        actionString = input.askForInput();

        //validate action before executing
        actionString = validateAction(actionString, currentBet);

        //only execute if void
        if(actionString != null) {
            currentBet = executeAction(actionString, currentBet);
        } else {
            //Invalid action - retry
            playerAction(currentBet); //Recursive retry
        }
    }

    private String validateAction(String action, int currentBet) {
    Player p = players[currentPlayerIdx];
    
    switch (action) {
        case "0": // Call
            // Validate: There must be something to call
            int callAmount = currentBet - p.getLastBetAmount();
            if (callAmount <= 0) {
                System.out.println("Nothing to call! (Current bet: " + currentBet + ", Your last bet: " + p.getLastBetAmount() + ")");
                return null;  // Retry
            }
            // Check if player has enough chips
            if (p.getChips() < callAmount) {
                System.out.println("Not enough chips to call. Going All-In instead.");
                return "4";  // Auto-switch to All-In
            }
            return action;
            
        case "1": // Bet
            // Validate: Must be first to act in round (currentBet == 0)
            if (currentBet > 0) {
                System.out.println("Cannot bet when there's already a bet. Use Call or Raise instead.");
                return null;
            }
            // Get bet amount with validation
            int minBet = bigBlind;  // Minimum opening bet
            int betAmount = input.promptBetAmount(minBet, p.getChips());
            if (betAmount <= 0) {
                System.out.println("Invalid bet amount. Try again.");
                return null;
            }
            lastBetAmount = betAmount;  // Store for executeAction
            return action;
            
        case "2": // Fold
            // Validate: Can always fold (unless only player left)
            int activePlayers = countActivePlayers();
            if (activePlayers <= 1) {
                System.out.println("You're the only player left! Cannot fold.");
                return null;
            }
            return action;
            
        case "3": // Raise
            // Validate: Must raise at least 2x current bet or big blind
            int minRaise = Math.max(bigBlind, currentBet * 2);
            if (currentBet == 0) {
                System.out.println("Cannot raise when there's no bet. Use Bet instead.");
                return null;
            }
            int raiseAmount = input.promptBetAmount(minRaise, p.getChips());
            if (raiseAmount <= 0) {
                System.out.println("Invalid raise amount. Try again.");
                return null;
            }
            lastBetAmount = raiseAmount;  // Store for executeAction
            return action;
            
        case "4": // All-In
            // Validate: Player must have chips
            if (p.getChips() <= 0) {
                System.out.println("You have no chips left! Cannot go All-In.");
                return null;
            }
            return action;
            
        case "5": // Check
            // Validate: Can only check if no bet to match
            if (currentBet > p.getLastBetAmount()) {
                System.out.println("You must call or raise! (Current bet: " + currentBet + ", Your last bet: " + p.getLastBetAmount() + ")");
                return null;
            }
            return action;
            
        case "6": // Leave Table
            // Validate: Can always leave (but folds hand)
            System.out.println("Are you sure you want to leave? (y/n)");
            String confirm = input.askForInput();
            if (!confirm.equalsIgnoreCase("y")) {
                System.out.println("Staying at table.");
                return null;
            }
            return action;
            
        case "*": // Exit
            // Validate: Confirm exit
            System.out.println("Are you sure you want to exit the game? (y/n)");
            String confirmExit = input.askForInput();
            if (!confirmExit.equalsIgnoreCase("y")) {
                System.out.println("Staying in game.");
                return null;
            }
            return action;
            
        case "o": // Options
            // No validation needed - just display options
            return action;
            
        default:
            System.out.println("Invalid action. Enter 0-6, o for options, or * to quit.");
            return null;
    }
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
            case "Play":
                System.out.println("Playing Hand");
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
        return input.promptBetAmount(bigBlind, players[currentPlayerIdx].getChips());  
    }

    private void executeAction(String actionString) {
        executeActionRecursive(actionString);
    }

    private int executeAction(String actionString, int currentBet) {
        Player p = players[currentPlayerIdx];
        int betAmount = 0;
    
        switch (actionString) {

            case "0": // Call
                int callAmount = currentBet - p.getLastBetAmount();
                if (callAmount <= 0) {
                    System.out.println("Nothing to call!");
                    return currentBet;
                }
                if (p.getChips() < callAmount) {
                    // All-in
                    mainTable.addBetToPot(p.getChips());
                    p.setChips(0);
                    p.setAllin(true);
                    p.setLastBetAmount(p.getLastBetAmount() + p.getChips());
                    System.out.println(p.getName() + " goes All-In for " + callAmount + "!");
                } else {
                    p.setChips(p.getChips() - callAmount);
                    mainTable.addBetToPot(callAmount);
                    p.setLastBetAmount(currentBet);
                    System.out.println(p.getName() + " calls " + callAmount);
                }
                break;
                
            case "1": // Bet/Raise
                betAmount = input.promptBetAmount(bigBlind, p.getChips());
                if (betAmount <= 0) {
                    System.out.println("Invalid bet amount!");
                    return currentBet;
                }
                p.setChips(p.getChips() - betAmount);
                mainTable.addBetToPot(betAmount);
                p.setLastBetAmount(currentBet + betAmount);
                currentBet = currentBet + betAmount;
                System.out.println(p.getName() + " bets/raises " + betAmount);
                break;
                
            case "2": // Fold
                p.setFolded(true);
                System.out.println(p.getName() + " folds");
                break;
                
            case "3": // Raise (same as Bet, but explicit)
                betAmount = input.promptBetAmount(bigBlind, p.getChips());
                if (betAmount <= 0) {
                    System.out.println("Invalid raise amount!");
                    return currentBet;
                }
                p.setChips(p.getChips() - betAmount);
                mainTable.addBetToPot(betAmount);
                p.setLastBetAmount(currentBet + betAmount);
                currentBet = currentBet + betAmount;
                System.out.println(p.getName() + " raises " + betAmount);
                break;
                
            case "4": // All-In
                int allInAmount = p.getChips();
                mainTable.addBetToPot(allInAmount);
                p.setChips(0);
                p.setAllin(true);
                p.setLastBetAmount(currentBet + allInAmount);
                currentBet = currentBet + allInAmount;
                System.out.println(p.getName() + " goes All-In for " + allInAmount + "!");
                break;
                
            case "5": // Check
                if (currentBet > p.getLastBetAmount()) {
                    System.out.println("You must call or raise! (Current bet: " + currentBet + ", Your last bet: " + p.getLastBetAmount() + ")");
                    return currentBet; // Don't advance turn
                }
                p.setLastBetAmount(currentBet);
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
                return currentBet; // Don't advance turn
                
            default:
                System.out.println("Invalid action. Try again.");
                return currentBet; // Don't advance turn
        }
        
        return currentBet;
}

    private void printPrompt() {
        System.out.println("Enter 0 - 6, o for options, or * to quit");
    }

    private void printPrompt(int flag) {
        System.out.println("Enter p to play, or q to quit");
    }

    private void printOptions() {
        System.out.println("Call\t0\nBet\t1\nFold\t2\nRaise\t3\nAll in\t4\nCheck\t5\n" + 
            "Leave Table\t6\nQuit\t*\n");  
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
                //printPrompt();
                printPrompt(0);
                recursive = input.askForInput();
                executeActionRecursive(recursive);
                break;
            case "p":
                System.out.println("Playing Hand");
                break;
            case "q":
                System.out.println("Quit");
                System.exit(0);
                break;
            default:
                //printPrompt();
                printPrompt(0);
                recursive = input.askForInput();
                executeActionRecursive(recursive);
                break;
        }
    }
}