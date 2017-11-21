import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class TextEditor extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void textEdit(Pane root){
        TextField textField = new TextField("enter path to the file");
        final TextField[] text = new TextField[1];
        Button loadButton = new Button("Load");
        loadButton.setTranslateX(400);
        loadButton.setOnAction(event -> {
            String inputPath = textField.getText();
            Callable<String> callable = () -> fileDownload(inputPath);
            FutureTask <String> futureTask = new FutureTask<>(callable);
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
                    text[0].setMinSize(500,300);
                    text[0].setTranslateY(200);
                    root.getChildren().addAll(text[0]);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        });


        root.getChildren().addAll(textField,loadButton);
    }

    public String fileDownload (String loadFrom) {
        File file = new File(loadFrom);
        String text = "";
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNextLine()) {
            text += scanner.nextLine();
        }
        return text;
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setWidth(600);
        primaryStage.setHeight(600);
        primaryStage.show();
        textEdit(root);
    }
}
