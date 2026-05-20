import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.control.ScrollPane;
import java.io.*;
import java.net.*;

public class ChatClientGUI extends Application {

    private TextArea chatArea;
    private TextField messageField;
    private PrintWriter writer;
    private VBox root;
    private VBox messagesBox;
    @Override
    public void start(Stage stage) {

        messagesBox = new VBox(10);
        ScrollPane scrollPane =
                new ScrollPane(messagesBox);

        scrollPane.setFitToWidth(true);
        messageField = new TextField();
        messageField.setPromptText("Type message...");

        Button sendButton = new Button("Send");

        Button imageButton = new Button("Send Image");

        HBox bottomLayout =
                new HBox(10, messageField, sendButton, imageButton);

        root = new VBox(10, scrollPane, bottomLayout);
        root.setStyle(
                "-fx-background-color: #ffc0cb;"
        );
        root.setPadding(new Insets(10));

        Scene scene =
                new Scene(root, 400, 500);

        stage.setTitle("Chat App");
        stage.setScene(scene);
        stage.show();

        connectToServer();

        sendButton.setOnAction(e -> sendMessage());

        imageButton.setOnAction(e-> sendImage());
        messageField.setOnAction(e -> sendMessage());}

    public void sendImage() {
            FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Image Files",
                        "*.png",
                        "*.jpg",
                        "*.jpeg",
                        "*.gif"
                )
        );
            fileChooser.setTitle("Choose Image");
            File file = fileChooser.showOpenDialog(null);

            if (file != null) {
                writer.println( "IMAGE:" + file.getAbsolutePath());
            }
        }



    public void connectToServer() {

        try {

            Socket socket =
                    new Socket("localhost", 5000);

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()
                            )
                    );

            writer =
                    new PrintWriter(
                            socket.getOutputStream(),
                            true
                    );

            Thread receiveThread = new Thread(() -> {

                try {

                    String message;

                    while ((message =
                            reader.readLine()) != null) {

                        String finalMessage = message;

                        Platform.runLater(() -> {

                           if(finalMessage.startsWith("IMAGE:")) {
                               String imagePath = finalMessage.substring(6);
                               Image image = new Image(new File(imagePath).toURI().toString());
                               ImageView imageView = new ImageView(image);
                               imageView.setFitWidth(200);
                               imageView.setPreserveRatio(true);
                               messagesBox.getChildren().add(imageView);                           }
                           else {
                               Label label = new Label(finalMessage);

                               messagesBox.getChildren().add(label);                           }

                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            receiveThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() {

        String message =
                messageField.getText();

        if (!message.isEmpty()) {

            writer.println(message);
            System.out.println("Sending: " + message);
            messageField.clear();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
