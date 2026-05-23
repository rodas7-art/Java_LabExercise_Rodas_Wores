import Game.*;
import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.stage.*;
import Model.*;

import java.util.*;

public class Main extends Application {

    private FlowPane playerPane = new FlowPane(10, 10);
    private FlowPane computerPane = new FlowPane(10, 10);

    private Label result = new Label();

    private Player player = new Player("You");
    private Player computer = new Player("Computer");

    private Deck deck;

    private boolean drawPhase = false;

    private ArrayList<Integer> selected =
            new ArrayList<>();

    private Button button =
            new Button("START GAME");

    @Override
    public void start(Stage stage) {

        Label title =
                createText("♠ SIMPLE DRAW POKER ♠", 32);

        Label compText =
                createText("Computer", 24);

        Label playerText =
                createText("Your Cards", 24);

        result.setFont(new Font(22));
        result.setTextFill(Color.YELLOW);

        playerPane.setAlignment(Pos.CENTER);
        computerPane.setAlignment(Pos.CENTER);

        styleButton();

        button.setOnAction(e -> {

            if (!drawPhase)
                startGame();
            else
                drawCards();
        });

        VBox root = new VBox(
                30,
                title,
                compText,
                computerPane,
                button,
                playerText,
                playerPane,
                result
        );

        root.setAlignment(Pos.CENTER);

        root.setPadding(new Insets(20));

        root.setStyle(
                "-fx-background-color: darkgreen;"
        );

        Scene scene =
                new Scene(root, 1000, 700);

        stage.setTitle("Poker Game");

        stage.setScene(scene);

        stage.show();
    }

    private void startGame() {

        drawPhase = true;

        selected.clear();

        player.clearHand();
        computer.clearHand();

        playerPane.getChildren().clear();
        computerPane.getChildren().clear();

        result.setText("");

        deck = new Deck();

        deck.shuffleDeck();

        for (int i = 0; i < 5; i++) {

            player.addCard(deck.dealCard());

            computer.addCard(deck.dealCard());
        }

        displayPlayerCards();

        showHiddenComputerCards();

        button.setText("DRAW CARDS");
    }

    private void drawCards() {

        for (Integer i : selected)
            player.replaceCard(i, deck.dealCard());

        Random r = new Random();

        int changes = r.nextInt(3);

        for (int i = 0; i < changes; i++) {

            int randomIndex = r.nextInt(5);

            computer.replaceCard(
                    randomIndex,
                    deck.dealCard()
            );
        }

        displayPlayerCards();

        revealComputerCards();

        showWinner();

        drawPhase = false;

        button.setText("PLAY AGAIN");
    }

    private void displayPlayerCards() {

        playerPane.getChildren().clear();

        for (int i = 0; i < 5; i++) {

            Card card =
                    player.getHand().get(i);

            Label label = createCard(card);

            int index = i;

            label.setOnMouseClicked(e -> {

                if (selected.contains(index)) {

                    selected.remove((Integer) index);

                    label.setStyle(normalStyle());

                } else {

                    selected.add(index);

                    label.setStyle(selectedStyle());
                }
            });

            playerPane.getChildren().add(label);
        }
    }

    private void revealComputerCards() {

        computerPane.getChildren().clear();

        for (Card card : computer.getHand())
            computerPane.getChildren()
                    .add(createCard(card));
    }

    private void showWinner() {

        int playerScore =
                HandEvaluator.evaluateHand(
                        player.getHand()
                );

        int computerScore =
                HandEvaluator.evaluateHand(
                        computer.getHand()
                );

        if (playerScore > computerScore) {

            result.setText(
                    "🎉 YOU WIN!\n"
                            + HandEvaluator.getHandName(playerScore)
            );

        } else if (computerScore > playerScore) {

            result.setText(
                    "💻 COMPUTER WINS!\n"
                            + HandEvaluator.getHandName(computerScore)
            );

        } else {

            result.setText("🤝 TIE!");
        }
    }

    private void showHiddenComputerCards() {

        for (int i = 0; i < 5; i++) {

            Label hidden = new Label("🂠");

            hidden.setMinSize(90, 130);

            hidden.setAlignment(Pos.CENTER);

            hidden.setFont(new Font(40));

            hidden.setStyle(
                    "-fx-background-color: navy;" +
                            "-fx-text-fill: white;" +
                            "-fx-border-color: white;" +
                            "-fx-border-width: 3;" +
                            "-fx-background-radius: 10;" +
                            "-fx-border-radius: 10;"
            );

            computerPane.getChildren().add(hidden);
        }
    }

    private Label createCard(Card card) {

        Label label =
                new Label(card.toString());

        label.setMinSize(90, 130);

        label.setAlignment(Pos.CENTER);

        label.setFont(new Font(26));

        if (
                card.getSuit().equals("♥") ||
                        card.getSuit().equals("♦")
        )
            label.setTextFill(Color.RED);
        else
            label.setTextFill(Color.BLACK);

        label.setStyle(normalStyle());

        return label;
    }

    private Label createText(String text, int size) {

        Label label = new Label(text);

        label.setFont(new Font(size));

        label.setTextFill(Color.WHITE);

        return label;
    }

    private void styleButton() {

        button.setStyle(
                "-fx-background-color: gold;" +
                        "-fx-font-size: 18;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 10;"
        );
    }

    private String normalStyle() {

        return
                "-fx-background-color: white;" +
                        "-fx-border-color: black;" +
                        "-fx-border-width: 3;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;";
    }

    private String selectedStyle() {

        return
                "-fx-background-color: gold;" +
                        "-fx-border-color: red;" +
                        "-fx-border-width: 4;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;";
    }

    public static void main(String[] args) {
        launch();
    }
}