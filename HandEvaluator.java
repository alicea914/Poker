/**
 * Static hand evaluator for 5-7 card poker hands (hole cards + board).
 * Packs a category and five kickers into one int score; higher score wins.
 *
 * @author alvaro
 * @version 11/16/2026
 */
public class HandEvaluator
{
    public static final int STRAIGHT_FLUSH = 8;
    public static final int FOUR_OF_A_KIND = 7;
    public static final int FULL_HOUSE = 6;
    public static final int FLUSH = 5;
    public static final int STRAIGHT = 4;
    public static final int THREE_OF_A_KIND = 3;
    public static final int TWO_PAIR = 2;
    public static final int ONE_PAIR = 1;
    public static final int HIGH_CARD = 0;

    /**
     * Scores an array of 5-7 cards. Aces count high and low for straights.
     * @param cards cards to evaluate (hole cards + community cards)
     * @return packed score where a higher score beats a lower score; -1 on invalid input
     */
    public static int evaluate(Card[] cards) {
        if(cards == null || cards.length < 5) {
            return -1;
        }

        int[] rankCount = new int[15];              // index 2-14, ace = 14
        boolean[][] suitHasRank = new boolean[4][15];
        int[] suitCount = new int[4];

        for(int i = 0; i < cards.length; i++) {
            if(cards[i] == null) {
                return -1;
            }
            int r = cards[i].getRank();
            int s = cards[i].getSuit();
            if(r == 1) {
                r = 14;
            }
            if(r < 2 || r > 14 || s < 0 || s > 3) {
                return -1;
            }
            rankCount[r]++;
            suitCount[s]++;
            suitHasRank[s][r] = true;
        }

        //find flush suit, if any
        int flushSuit = -1;
        for(int s = 0; s < 4; s++) {
            if(suitCount[s] >= 5) {
                flushSuit = s;
            }
        }

        //straight flush
        if(flushSuit >= 0) {
            int sfHigh = straightHigh(suitHasRank[flushSuit]);
            if(sfHigh > 0) {
                return pack(STRAIGHT_FLUSH, sfHigh, 0, 0, 0, 0);
            }
        }

        //grouped ranks: quads, up to 2 trips, up to 3 pairs
        int quadRank = 0;
        int tripHi = 0;
        int tripLo = 0;
        int pairHi = 0;
        int pairMid = 0;
        int pairLo = 0;

        for(int r = 14; r >= 2; r--) {
            int count = rankCount[r];
            if(count == 4) {
                quadRank = r;
            } else if(count == 3) {
                if(tripHi == 0) {
                    tripHi = r;
                } else if(tripLo == 0) {
                    tripLo = r;
                }
            } else if(count == 2) {
                if(pairHi == 0) {
                    pairHi = r;
                } else if(pairMid == 0) {
                    pairMid = r;
                } else if(pairLo == 0) {
                    pairLo = r;
                }
            }
        }

        //four of a kind
        if(quadRank > 0) {
            int[] k = kickers(rankCount, quadRank, 0, 1);
            return pack(FOUR_OF_A_KIND, quadRank, k[0], 0, 0, 0);
        }

        //full house: top trips + best pair (a second trips counts as a pair)
        if(tripHi > 0) {
            int pairCandidate = Math.max(tripLo, pairHi);
            if(pairCandidate > 0) {
                return pack(FULL_HOUSE, tripHi, pairCandidate, 0, 0, 0);
            }
        }

        //flush
        if(flushSuit >= 0) {
            int[] fr = flushRanks(suitHasRank[flushSuit]);
            return pack(FLUSH, fr[0], fr[1], fr[2], fr[3], fr[4]);
        }

        //straight
        int sHigh = straightHigh(rankPresent(rankCount));
        if(sHigh > 0) {
            return pack(STRAIGHT, sHigh, 0, 0, 0, 0);
        }

        //three of a kind
        if(tripHi > 0) {
            int[] k = kickers(rankCount, tripHi, 0, 2);
            return pack(THREE_OF_A_KIND, tripHi, k[0], k[1], 0, 0);
        }

        //two pair
        if(pairHi > 0 && pairMid > 0) {
            int[] k = kickers(rankCount, pairHi, pairMid, 1);
            return pack(TWO_PAIR, pairHi, pairMid, k[0], 0, 0);
        }

        //one pair
        if(pairHi > 0) {
            int[] k = kickers(rankCount, pairHi, 0, 3);
            return pack(ONE_PAIR, pairHi, k[0], k[1], k[2], 0);
        }

        //high card
        int[] k = kickers(rankCount, 0, 0, 5);
        return pack(HIGH_CARD, k[0], k[1], k[2], k[3], k[4]);
    }

    /**
     * Gets the human-readable category name for a score from evaluate().
     * @param score packed score from evaluate()
     * @return category name such as "Full House" or "High Card"
     */
    public static String categoryName(int score) {
        switch(score >> 20) {
            case STRAIGHT_FLUSH:
                return "Straight Flush";
            case FOUR_OF_A_KIND:
                return "Four of a Kind";
            case FULL_HOUSE:
                return "Full House";
            case FLUSH:
                return "Flush";
            case STRAIGHT:
                return "Straight";
            case THREE_OF_A_KIND:
                return "Three of a Kind";
            case TWO_PAIR:
                return "Two Pair";
            case ONE_PAIR:
                return "Pair";
            default:
                return "High Card";
        }
    }

    private static int pack(int cat, int k1, int k2, int k3, int k4, int k5) {
        return (cat << 20) | (k1 << 16) | (k2 << 12) | (k3 << 8) | (k4 << 4) | k5;
    }

    private static int straightHigh(boolean[] hasRank) {
        for(int high = 14; high >= 6; high--) {
            boolean ok = true;
            for(int r = high; r > high - 5; r--) {
                if(!hasRank[r]) {
                    ok = false;
                    break;
                }
            }
            if(ok) {
                return high;
            }
        }
        //wheel: A-2-3-4-5
        if(hasRank[14] && hasRank[5] && hasRank[4] && hasRank[3] && hasRank[2]) {
            return 5;
        }
        return 0;
    }

    private static boolean[] rankPresent(int[] rankCount) {
        boolean[] hasRank = new boolean[15];
        for(int r = 2; r <= 14; r++) {
            hasRank[r] = rankCount[r] > 0;
        }
        return hasRank;
    }

    private static int[] kickers(int[] rankCount, int exclude1, int exclude2, int howMany) {
        int[] ks = new int[howMany];
        int filled = 0;
        for(int r = 14; r >= 2 && filled < howMany; r--) {
            if(rankCount[r] > 0 && r != exclude1 && r != exclude2) {
                ks[filled++] = r;
            }
        }
        return ks;
    }

    private static int[] flushRanks(boolean[] hasRank) {
        int[] ks = new int[5];
        int filled = 0;
        for(int r = 14; r >= 2 && filled < 5; r--) {
            if(hasRank[r]) {
                ks[filled++] = r;
            }
        }
        return ks;
    }
}