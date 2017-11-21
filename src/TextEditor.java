import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.*;

public class TextEditor extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void textEdit(Pane root, Stage primaryStage) {
        TextField textField = new TextField("files/links1.txt");      //"enter path to the file"
        TextField[] text = new TextField[1];
        Button loadButton = new Button("Load");
        loadButton.setTranslateX(400);
        loadButton.setOnAction(event -> {
            String inputPath = textField.getText();
            Callable<String> callable = () -> fileDownload(inputPath);
            FutureTask<String> futureTask = new FutureTask<>(callable);
            new Thread(futureTask).start();
            try {
                Platform.runLater(() -> {
                    try {
                        text[0] = new TextField(futureTask.get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    /*ScrollPane scrollPane = new ScrollPane();             //scroll pane не очень получился
                    scrollPane.setFitToWidth(true);
                    scrollPane.setContent(text[0]);*/
                    text[0].setMinSize(500, 100);
                    text[0].setTranslateY(200);



                    root.getChildren().addAll(text[0]/*, scrollPane*/);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        Button saveButton = new Button("Save");
        saveButton.setTranslateX(400);
        saveButton.setTranslateY(60);
        saveButton.setOnAction(event -> {
            String enteredText = text[0].getText();
            Runnable runnable = () -> {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);
                Platform.runLater(() -> {
                    File file = fileChooser.showSaveDialog(primaryStage);
                    if (file != null) {
                        try {
                            fileSave(enteredText, file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            };
            new Thread(runnable).start();
        });

        root.getChildren().addAll(textField, loadButton, saveButton);
    }

    public String fileDownload(String loadFrom) {
        File file = new File(loadFrom);
        String text = "";
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNextLine()) {
            text = text + scanner.nextLine() + "; ";
        }
        return text;
    }

    public void fileSave(String text, File file) throws IOException {                     //сохранение в файл

        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(text);
        fileWriter.flush();
        fileWriter.close();
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setWidth(600);
        primaryStage.setHeight(600);
        primaryStage.show();
        textEdit(root, primaryStage);
    }
}
