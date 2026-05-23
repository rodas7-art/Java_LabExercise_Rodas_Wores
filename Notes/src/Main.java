import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.Priority;
import javafx.scene.layout.HBox;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Main extends Application {

        public void start(Stage primaryStage) {
                TextArea notepadArea = new TextArea();
                notepadArea.setPromptText("Type your notes here");

                VBox.setVgrow(notepadArea, Priority.ALWAYS);
                Button saveButton = new Button("save");
                Button newButton = new Button("New Note");
                Button anotherOne = new Button( "Open");
                Button delete = new Button("Delete");

                newButton.setOnAction( e -> notepadArea.clear());
                HBox buttonLayout = new HBox(10, anotherOne, newButton, delete, saveButton);
                VBox layout = new VBox(10, notepadArea, buttonLayout);

                anotherOne.setOnAction( e-> {

                        FileChooser fileChooser = new FileChooser();

                        fileChooser.setTitle("Open Note");
                        fileChooser.getExtensionFilters().add
                                ( new FileChooser.ExtensionFilter("Text Files", "*.txt"));

                        File file = fileChooser.showOpenDialog(primaryStage);

                        if(file != null) {

                                try {
                                        BufferedReader reader = new BufferedReader(new FileReader(file));

                                        StringBuilder content = new StringBuilder();
                                        String line;

                                        while ((line = reader.readLine()) != null) {
                                                content.append(line).append("\n");
                                        }
                                        reader.close();
                                        notepadArea.setText(content.toString());
                                } catch (IOException ex) {
                                        ex.printStackTrace();
                                }
                        } });
                saveButton.setOnAction(e -> {
                                String text = notepadArea.getText();
                        FileChooser fileChooser= new FileChooser();
                        fileChooser.setTitle("Save Note");
                        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

                        File file = fileChooser.showSaveDialog(primaryStage);

                try {
                        FileWriter writer = new FileWriter(file, true);
                        writer.write(text + "\n---\n");
                        writer.close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("we made we did it!");
                alert.setHeaderText(null);
                alert.setContentText("Your thoughts are safe here!") ;
                alert.showAndWait();
                System.out.println("hoorayy saved successfully");
                        System.out.println(file.getAbsolutePath());
                                } catch (IOException ex) {
                                        ex.printStackTrace();
                                }
                        });
                delete.setOnAction( e->{
                       FileChooser fileChooser = new FileChooser();
                       fileChooser.setTitle("Select File To Delete");
                       fileChooser.getExtensionFilters().add(
                               new FileChooser.ExtensionFilter("Text Files", "*.txt")
                       );

                       File file = fileChooser.showOpenDialog(primaryStage);
                       if (file !=null){

                               Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                               confirmAlert.setTitle("Delete Confirmation");
                               confirmAlert.setHeaderText(null);
                               confirmAlert.setContentText(
                                       "ARE YOU SURE YOU WANT TO DELETE THIS FILE?"
                               );

                               confirmAlert.showAndWait();
                               if (confirmAlert.getResult().getText().equals("OK")) {

                                       boolean deleted = file.delete();
                                       Alert resultAlert;
                                       if(deleted) {
                                               resultAlert = new Alert(Alert.AlertType.INFORMATION);
                                               resultAlert.setTitle("Deleted");
                                               resultAlert.setHeaderText(null);
                                               resultAlert.setContentText("File deleted successfully!");

                                       } else {
                                               resultAlert = new Alert(Alert.AlertType.ERROR);
                                               resultAlert.setTitle("Error");
                                               resultAlert.setContentText("Could not delete file.");
                                       }
                                       resultAlert.showAndWait();
                               }


                       }
                });
                Scene scene = new Scene(layout, 800, 500);

                primaryStage.setTitle("My Notepad");
                primaryStage.setScene(scene);
                primaryStage.show();
        }

        public static void main(String[] args) {
                launch(args);
        }
}

