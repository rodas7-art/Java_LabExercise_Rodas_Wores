package Game;

import Model.Card;

import java.util.ArrayList;
import java.util.HashMap;

public class HandEvaluator {

    public static int evaluateHand(ArrayList<Card> hand) {

        HashMap<Integer, Integer> counts = new HashMap<>();

        for (Card card : hand) {

            int value = card.getValue();

            counts.put(
                    value,
                    counts.getOrDefault(value, 0) + 1
            );
        }

        boolean pair = false;
        boolean twoPair = false;
        boolean threeKind = false;

        int pairCount = 0;

        for (int count : counts.values()) {

            if (count == 3) {
                threeKind = true;
            }

            if (count == 2) {
                pairCount++;
            }
        }

        if (pairCount == 1) {
            pair = true;
        }

        if (pairCount == 2) {
            twoPair = true;
        }

        if (threeKind) {
            return 4;
        }

        if (twoPair) {
            return 3;
        }
        if (pair) {
            return 2;
        }
        return 1;
    }
    public static String getHandName(int score) {

        switch (score) {

            case 4:
                return "Three of a Kind";

            case 3:
                return "Two Pair";

            case 2:
                return "One Pair";

            default:
                return "High Card";
        }
    }
}
