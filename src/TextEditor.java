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
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;

public class TextEditor extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void textEdit(Pane root, Stage primaryStage) {
        Semaphore semaphoreFib = new Semaphore(1);
        TextField textField = new TextField("Enter path to file or Fibonacci count");      //"enter path to the file"
        TextField[] text = new TextField[1];
        Button fibonacci = new Button("Fibonacci");
        fibonacci.setTranslateX(400);
        fibonacci.setTranslateY(60);
        fibonacci.setOnAction(event -> {
            int fibPos = Integer.parseInt(textField.getText());
            Runnable runnable = () -> {
                try {
                    semaphoreFib.acquire();
                    System.out.println("начало работы семафора");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String resultOfSearch = fibonacciSearch(fibPos);
                try {
                    System.out.println("засыпаем");
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("поспали");
                semaphoreFib.release();
                try {
                    semaphoreFib.acquire();
                    System.out.println("save start");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    File file = saverDialog(primaryStage);
                    if (file != null) {
                        try {
                            fileSave(resultOfSearch, file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                System.out.println("save end");
                semaphoreFib.release();
            };
            new Thread(runnable).start();
        });
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
                    /*ScrollPane scrollPane = new ScrollPane(text[0]);             //scroll pane не очень получился
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
        saveButton.setTranslateY(120);
        saveButton.setOnAction(event -> {
            String enteredText = text[0].getText();
            Runnable runnable = () -> {

                Platform.runLater(() -> {
                    File file = saverDialog(primaryStage);
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
        root.getChildren().addAll(textField, loadButton, saveButton, fibonacci);
    }

    public File saverDialog(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(primaryStage);
        return file;
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

    public void fileSave(String text, File file) throws IOException {                     //сохранение в файл сначала сделал через текстовое поле, но потом переделал на этот вариант и удалил
        //прошлый. Потом уже дочитал что нужно именно через текстовое поле сохранять, но возвращать обратно не захотелось
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(text);
        fileWriter.flush();
        fileWriter.close();
    }

    public String fibonacciSearch(int x) {
        long timer = System.currentTimeMillis();
        System.out.println("начало работы фибоначи поиска " + timer);
        String result = "";
        ArrayList<Integer> fib = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            if (i == 0 || i == 1)
                fib.add(i);
            else fib.add(fib.get(i - 2) + fib.get(i - 1));
            result += fib.get(i) + "; ";
        }
        System.out.println("конец работы  за " + (-1) * (timer - System.currentTimeMillis()) + "ms");
        return result;
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
