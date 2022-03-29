import java.util.*;
/**
 * Write a description of class UserInput here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class UserInput
{
    // instance variables - replace the example below with your own
    private Scanner input = null;

    public UserInput() {
        input = new Scanner(System.in);
    }

    public String askForInput() {        
        // //System.out.println("Enter 0-7, o for options, or * to quit");
        return input.nextLine();
    }

    public Action promptAction() {
        //prompt user for action and return an action object based on input     
        return null;
    }

    public int promptBetAmount(int bigBlind, int playerChips) {
        int minBet = bigBlind * 2;
        if(playerChips <= minBet) {
            System.out.println("Betting all in!");
            return playerChips;
        } else {
            return promptBetAmountRecursive(minBet, playerChips);
        }        
    }

    public int promptBetAmountRecursive(int minBet, int playerChips) {
        int amount = 0;
        String inputString = "";
        int inputStringLength = 0;
        boolean flag = true;

        System.out.println("Enter bet:");
        inputString = input.nextLine();
        inputStringLength = inputString.length();

        if(inputStringLength == 0 || inputString.equals("")) {
            while(inputStringLength == 0) {
                System.out.println("Enter a valid bet or enter * to quit:");
                inputString = input.nextLine();
                inputStringLength = inputString.length();
            }
        }

        for(int i = 0; i < inputStringLength;i++) {
            if(i==0 && inputString.charAt(i) == '-')
                continue;
            if(!Character.isDigit(inputString.charAt(i)))
                flag = false;
        }

        if(flag) {
            amount = Integer.parseInt(inputString);
            if(amount < minBet) {
                System.out.println("Minimum bet has to be " + minBet + 
                    ". Enter a valid bet or enter * to quit"); 
                return promptBetAmountRecursive(minBet, playerChips);
            } else if(amount > playerChips) {
                System.out.println("Maximum bet is all in (" + playerChips + 
                    "). Enter a valid bet or enter * to quit");
                return promptBetAmountRecursive(minBet, playerChips);
            } else {
                return amount;
            }
        } else {
            if(inputString.equals("*")) {
                System.exit(0);
                return -1;
            }
            System.out.println("Bet must be a number between " + 
                minBet + " and " + playerChips + 
                ". Enter a valid bet or enter * to quit");
            return promptBetAmountRecursive(minBet, playerChips);
        }
    }

    public String[] promptNames() {        
        return getNamesRecursion();
    }

    private String[] getNamesRecursion() {
        String[] namesArr = null;
        String playerName = "";
        String inputString = "";
        int inputStringLength = 0;
        int players = 0;
        boolean flag = true;

        System.out.println("Enter # of players (up to 22) or * to quit:");
        inputString = input.nextLine();
        inputStringLength = inputString.length();

        if(inputStringLength == 1 && inputString.charAt(0) == '*') {
            System.exit(0);
            return null;
        }

        if(inputStringLength == 0 || inputString.equals("")) {
            while(inputStringLength == 0) {
                System.out.print("Invalid input. ");
                System.out.println("Enter # of players (up to 22) or * to quit:");
                inputString = input.nextLine();
                inputStringLength = inputString.length();
            }
        }

        for(int i = 0; i < inputStringLength;i++) {
            if(i==0 && inputString.charAt(i) == '-')
                continue;
            if(!Character.isDigit(inputString.charAt(i)))
                flag = false;
        }

        //if input is an integer between 1 and 22 inclusive, user inputs names
        if(flag) {
            //System.out.println("Valid integer");
            players = Integer.parseInt(inputString);
            if(players <= 22 && players > 0) {
                namesArr = new String[players];
                for(int k = 0; k < players; k++) {
                    System.out.println("Enter Player " + (k + 1) + "'s name or * to quit:");
                    playerName = input.nextLine();
                    if(playerName.length() == 1 && playerName.charAt(0) == '*') {
                        System.exit(0);
                    } else {

                        while(playerName.length() == 0) {
                            System.out.println("Invalid name. Enter Player " + (k + 1) + 
                                "'s name or * to quit:");
                            playerName = input.nextLine();
                        }
                        namesArr[k] = playerName;
                    }
                }
                return namesArr;
            } else {
                System.out.println("Invalid input");
                return getNamesRecursion();
            }
        } else {
            //if input is *, close app, else ask for input again
            if(inputString.charAt(0) == '*') {
                System.exit(0);
                return null;
            }
            else {
                System.out.println("Invalid input");
                return getNamesRecursion();
            }
        }
    }

    public int promptChips() {
        return getStartingChipsRecursion();        
    }

    private int getStartingChipsRecursion() {
        int maxChips = 1000000;
        String inputString = "";
        int inputStringLength = 0;
        boolean flag = true;

        System.out.println("Enter starting chips for each player (maximum " + 
            maxChips + ") or enter * to quit:");
        inputString = input.nextLine();
        inputStringLength = inputString.length();

        if(inputStringLength == 1 && inputString.charAt(0) == '*') {
            System.exit(0);
            return -1;
        }

        if(inputStringLength == 0 || inputString.equals("")) {
            while(inputStringLength == 0) {
                System.out.println("Invalid amount. Enter starting chips for each player (maximum " + 
                    maxChips + ") or enter * to quit:");
                inputString = input.nextLine();
                inputStringLength = inputString.length();
            }
        }

        for(int i = 0; i < inputStringLength;i++) {
            if(i==0 && inputString.charAt(i) == '-')
                continue;
            if(!Character.isDigit(inputString.charAt(i)))
                flag = false;
        }

        //if input is an integer between 1 and 1,000,000 inclusive, chips are in
        if(flag) {
            int chips = Integer.parseInt(inputString);
            if(chips <= maxChips && chips > 0) {
                return chips;
            } else {
                System.out.println("Starting chips must be between 1 and " + maxChips);
                return getStartingChipsRecursion();
            }
        } else {
            //if input is *, close app, else ask for input again
            if(inputString.charAt(0) == '*') {
                System.exit(0);
                return -1;
            }
            else {
                System.out.println("Invalid amount.");
                return getStartingChipsRecursion();
            }
        }
    }

    public boolean promptPlay() {
        System.out.println("Play another hand?");
        printPlayOptions();
        return playAgainRecursion();
    }

    private boolean playAgainRecursion() {
        String inputString = askForInput();

        switch (inputString) {
            case "y":
                return true;
            case "n":
                return false;
            case "*":
                System.out.println("Thanks for playing!");
                System.exit(0);
                return false;
            default:
                System.out.println("Please enter either y, n, or enter * to quit:");
                return playAgainRecursion();
        }
    }

    private void printPlayOptions() {
        System.out.println("y = yes, n = no, * = quit");
    }
}
