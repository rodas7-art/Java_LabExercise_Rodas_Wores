package Model;

import java.util.ArrayList;

public class Player {

    private String name;
    private ArrayList<Card> hand;

    public Player(String name) {

        this.name = name;
        hand = new ArrayList<>();
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }

    public void clearHand() {
        hand.clear();
    }

    public void replaceCard(int index, Card newCard) {
        hand.set(index, newCard);
    }
}