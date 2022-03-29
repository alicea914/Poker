
/**
 * Player for poker game
 *
 * @author alvaro
 * @version 3/22/2022
 */
public class Player
{
    private String name = "";
    private Card[] hand = null;
    private int chips = -1;
    private boolean button = false;
    private boolean smallBlind = false;
    private boolean bigBlind = false;

    public Player() {
        name = "player";
        hand = new Card[2];
        chips = 0;
        button = false;
        smallBlind = false;
        bigBlind = false;
    }

    public Player(String n) {
        name = n;
        hand = new Card[2];
        chips = 0;
        button = false;
        smallBlind = false;
        bigBlind = false;
    }

    public Player(String n, Card[] h) {
        name = n;
        hand = h;
        chips = 0;
        button = false;
        smallBlind = false;
        bigBlind = false;
    }

    public Player(String n, Card a, Card b) {
        name = n;
        hand = new Card[2];
        setHand(a, b);
        chips = 0;
        button = false;
        smallBlind = false;
        bigBlind = false;
    }

    public Player(String n, Card[] h, int c) {
        name = n;
        hand = h;
        chips = c;
        button = false;
        smallBlind = false;
        bigBlind = false;
    }

    public Player(String n, Card a, Card b, int c) {
        name = n;
        hand = new Card[2];
        setHand(a, b);
        chips = c;
        button = false;
        smallBlind = false;
        bigBlind = false;
    }

    public Player(String n, int c) {
        name = n;
        hand = new Card[2];
        chips = c;
        button = false;
        smallBlind = false;
        bigBlind = false;
    }

    public Player(Player a) {
        name = a.getName();
        hand = a.getHand();
        chips = a.getChips();
        button = a.getButtonStatus();
        smallBlind = a.getSmallBlindStatus();
        bigBlind = a.getBigBlindStatus();
    }

    public boolean getSmallBlindStatus() {
        return smallBlind;
    }

    public void setSmallBlindStatus(boolean s) {
        smallBlind = s;
    }

    public boolean getBigBlindStatus() {
        return bigBlind;
    }

    public void setBigBlindStatus(boolean b) {
        bigBlind = b;
    }

    public boolean getButtonStatus() {
        return button;
    }

    public void setButtonStatus(boolean d) {
        button = d;
    }

    public int getChips() {
        return chips;
    }

    public void setChips(int c) {
        chips = c;
    }

    public Card[] getHand() {
        Card[] retVal = new Card[2];
        retVal[0] = hand[0];
        retVal[1] = hand[1];
        return retVal;
    }

    public String showBlindsHand() {
        hand[0].setFacedown(false);
        hand[1].setFacedown(false);
        // if(smallBlind && button) {
        // return (name + " " + hand[0] + " " + hand[1] + "<small blind> <button>");
        // } else if (smallBlind && bigBlind) {
        // return (name + " " + hand[0] + " " + hand[1] + <>");
        // } else if (bigBlind && button) {
        // return (name + " " + hand[0] + " " + hand[1] + "<big blind> <button>");
        // } else {
        // return (name + " " + hand[0] + " " + hand[1]);
        // }

        //edit options
        return "";
    }

    /*
     * shows player name, their cards, chips, and if they're the button or blinds
     */
    public String showPlayerInfo(boolean f) {
        String retVal = "";
        if(!f) {
            hand[0].setFacedown(false);
            hand[1].setFacedown(false);
        }
        // hand[0].setFacedown(false);
        // hand[1].setFacedown(false);
        // if(smallBlind) {
        // return (name + " " + hand[0] + " " + hand[1] + " chips = " + chips + " <small blind>");
        // } else if (bigBlind) {
        // return (name + " " + hand[0] + " " + hand[1] + " chips = " + chips + " <big blind>");
        // } else if (button) {
        // return (name + " " + hand[0] + " " + hand[1] + " chips = " + chips + " <button>");
        // } else {
        retVal = (name + "\t\t" + hand[0] + " " + hand[1] + "\t\tchips = " + chips);
        hand[0].setFacedown(true);
        hand[1].setFacedown(true);
        if (button && (smallBlind && bigBlind)) {
            //retVal += " <action>";

        } else {
            if(button) {
                retVal += "\t<button>";
                //return (retVal + " <button>");
            }
            if(smallBlind) {
                retVal += "\t<small blind>";

                //return (name + " " + hand[0] + " " + hand[1] + " chips = " + chips + " <small blind>");
            }
            if (bigBlind) {
                retVal += "\t<big blind>";
                //return (name + " " + hand[0] + " " + hand[1] + " chips = " + chips + " <big blind>");
            } else {}

        }
        return retVal;
    }

    public void hideHand() {
        hand[0].setFacedown(true);
        hand[1].setFacedown(true);
    }

    public void setHand(Card a, Card b) {
        if(hand == null) {
            System.err.println("Hand array is null");
        } else {
            hand[0] = new Card(a);
            hand[1] = new Card(b);
        }
    }

    public void addCard(Card c, int idx) {
        if (idx > 2) {
            System.err.println("Cannot add more than 2 cards");
        } else if (idx < 0) {
            System.err.println("Cannot add card to this index");
        } else {
            hand[idx] = new Card(c);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public String toString() {
        hand[0].setFacedown(false);
        hand[1].setFacedown(false);
        // if(button) {
        // return (name + " " + hand[0] + " " + hand[1] + " chips = " + chips + " <button>");
        // } else if(smallBlind) {
        // return (name + " " + hand[0] + " " + hand[1] + " chips = " + chips + " <small blind>");
        // } else if (bigBlind) {
        // return (name + " " + hand[0] + " " + hand[1] + " chips = " + chips + " <big blind>");
        // } else
        // return (name + " " + hand[0] + " " + hand[1] + " chips = " + chips);

        String retVal = (name + "\t\t" + hand[0] + " " + hand[1] + "\t\tchips = " + chips);

        hand[0].setFacedown(true);
        hand[1].setFacedown(true);
        if (button && (smallBlind && bigBlind)) {
            retVal += " <action>";

        } else {
            if(button) {
                retVal += "\t<button>";
                //return (retVal + " <button>");
            }
            if(smallBlind) {
                retVal += " <small blind>";

                //return (name + " " + hand[0] + " " + hand[1] + " chips = " + chips + " <small blind>");
            }
            if (bigBlind) {
                retVal += " <big blind>";
                //return (name + " " + hand[0] + " " + hand[1] + " chips = " + chips + " <big blind>");
            } else {
                //retVal == "cool guy";
            }
        }
        //return (name + " " + hand[0] + " " + hand[1] + " chips = " + chips);
        return retVal;
    }
}
