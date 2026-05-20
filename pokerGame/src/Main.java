import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int chips = 100;
        
        System.out.println("======================================");
        System.out.println("      WELCOME TO EASY JAVA POKER      ");
        System.out.println("======================================");
        System.out.println("Rules: Deal 5 cards, choose which to swap,");
        System.out.println("and win chips based on your final hand!");
        System.out.println("Start with: " + chips + " chips.");

        while (chips > 0) {
            System.out.println("\n--------------------------------------");
            System.out.println("Your chips: " + chips);
            System.out.print("Enter bet amount (0 to quit): ");
            
            String betInput = scanner.nextLine().trim();
            if (betInput.isEmpty()) continue;
            
            int bet;
            try {
                bet = Integer.parseInt(betInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
                continue;
            }
            
            if (bet == 0) {
                break;
            }
            if (bet < 0 || bet > chips) {
                System.out.println("Invalid bet! You can't bet more than you have.");
                continue;
            }

            // Deduct the bet from chips
            chips -= bet;

            // 1. Create and Shuffle the Deck
            List<Card> deck = createDeck();
            Collections.shuffle(deck);

            // 2. Deal 5 Cards to the Player's Hand
            List<Card> hand = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                hand.add(deck.remove(0));
            }

            // 3. Show current hand
            System.out.println("\nYour dealt hand:");
            for (int i = 0; i < 5; i++) {
                System.out.println("Card " + (i + 1) + ": " + hand.get(i));
            }

            // 4. Let the player swap cards
            System.out.print("\nEnter card numbers to swap (e.g. '1 3 5', or press Enter to keep all): ");
            String swapInput = scanner.nextLine().trim();

            if (!swapInput.isEmpty()) {
                String[] tokens = swapInput.split("\\s+");
                for (String token : tokens) {
                    try {
                        int index = Integer.parseInt(token) - 1;
                        if (index >= 0 && index < 5) {
                            // Replace the card at index with a new card from the deck
                            hand.set(index, deck.remove(0));
                        }
                    } catch (NumberFormatException e) {
                        // Ignore invalid card numbers entered by the user
                    }
                }
            }

            // Show final hand
            System.out.println("\nYour final hand: " + hand);

            // 5. Evaluate the hand and calculate winnings
            HandResult result = evaluateHand(hand);
            int winnings = bet * result.multiplier;
            chips += winnings;

            System.out.println("Hand Type: " + result.handName + " (Payout: " + result.multiplier + "x)");
            if (winnings > 0) {
                System.out.println("Awesome! You won " + winnings + " chips!");
            } else {
                System.out.println("No winning combinations. Better luck next time!");
            }
        }

        System.out.println("\n======================================");
        System.out.println("GAME OVER! You ended with " + chips + " chips.");
        System.out.println("Thank you for playing Easy Java Poker!");
        System.out.println("======================================");
        scanner.close();
    }

    // Helper method to create a standard deck of 52 cards
    private static List<Card> createDeck() {
        List<Card> deck = new ArrayList<>();
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        String[] suits = {"♥", "♦", "♣", "♠"};
        
        for (int i = 0; i < ranks.length; i++) {
            for (String suit : suits) {
                // Card value ranges from 2 (for "2") to 14 (for "A")
                deck.add(new Card(ranks[i], suit, i + 2));
            }
        }
        return deck;
    }

    // Simple and robust poker hand evaluation
    private static HandResult evaluateHand(List<Card> hand) {
        // Sort cards by value (lowest to highest) to make checks easier
        hand.sort(Comparator.comparingInt(c -> c.value));

        int[] values = new int[5];
        for (int i = 0; i < 5; i++) {
            values[i] = hand.get(i).value;
        }

        // Count how many cards of each rank we have
        int[] counts = new int[15]; // index 2 to 14 correspond to rank values
        for (Card card : hand) {
            counts[card.value]++;
        }

        // Identify duplicates: pairs, three-of-a-kinds, four-of-a-kinds
        int pairs = 0;
        boolean threeOfAKind = false;
        boolean fourOfAKind = false;
        
        for (int count : counts) {
            if (count == 2) pairs++;
            if (count == 3) threeOfAKind = true;
            if (count == 4) fourOfAKind = true;
        }

        // Check if all cards have the same suit (Flush)
        boolean isFlush = true;
        for (int i = 1; i < 5; i++) {
            if (!hand.get(i).suit.equals(hand.get(0).suit)) {
                isFlush = false;
                break;
            }
        }

        // Check if all cards are in consecutive order (Straight)
        boolean isStraight = true;
        for (int i = 1; i < 5; i++) {
            if (values[i] != values[i - 1] + 1) {
                isStraight = false;
            }
        }
        // Special case: Ace-low straight (A, 2, 3, 4, 5) which sorts to [2, 3, 4, 5, 14]
        if (!isStraight && values[0] == 2 && values[1] == 3 && values[2] == 4 && values[3] == 5 && values[4] == 14) {
            isStraight = true;
        }

        // Determine Hand Name and payout multiplier
        String handName = "High Card";
        int multiplier = 0;

        if (isStraight && isFlush) {
            // If the lowest card of the straight is 10, it's a Royal Flush
            if (values[0] == 10) {
                handName = "Royal Flush";
                multiplier = 250;
            } else {
                handName = "Straight Flush";
                multiplier = 50;
            }
        } else if (fourOfAKind) {
            handName = "Four of a Kind";
            multiplier = 25;
        } else if (threeOfAKind && pairs == 1) {
            handName = "Full House";
            multiplier = 9;
        } else if (isFlush) {
            handName = "Flush";
            multiplier = 6;
        } else if (isStraight) {
            handName = "Straight";
            multiplier = 4;
        } else if (threeOfAKind) {
            handName = "Three of a Kind";
            multiplier = 3;
        } else if (pairs == 2) {
            handName = "Two Pair";
            multiplier = 2;
        } else if (pairs == 1) {
            handName = "One Pair";
            multiplier = 1;
        }

        return new HandResult(handName, multiplier);
    }
}

// Simple Card representation
class Card {
    String rank; // "2", "3", ..., "10", "J", "Q", "K", "A"
    String suit; // "♥", "♦", "♣", "♠"
    int value;   // 2 to 14

    Card(String rank, String suit, int value) {
        this.rank = rank;
        this.suit = suit;
        this.value = value;
    }

    @Override
    public String toString() {
        return rank + suit;
    }
}

// Container for hand evaluation results
class HandResult {
    String handName;
    int multiplier;

    HandResult(String handName, int multiplier) {
        this.handName = handName;
        this.multiplier = multiplier;
    }
}