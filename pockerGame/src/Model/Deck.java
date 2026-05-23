package Model;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    private ArrayList<Card> cards;

    public Deck() {

        cards = new ArrayList<>();

        String[] suits = {"♠", "♥", "♦", "♣"};
        String[] ranks = {
                "2", "3", "4", "5", "6",
                "7", "8", "9", "10",
                "J", "Q", "K", "A"
        };

        int[] values = {
                2,3,4,5,6,7,8,9,10,
                11,12,13,14
        };

        for (String suit : suits) {

            for (int i = 0; i < ranks.length; i++) {

                Card card = new Card(
                        suit,
                        ranks[i],
                        values[i]
                );

                cards.add(card);
            }
        }
    }
    public void shuffleDeck() {
        Collections.shuffle(cards);
    }

    public Card dealCard() {
        return cards.remove(0);
    }
}