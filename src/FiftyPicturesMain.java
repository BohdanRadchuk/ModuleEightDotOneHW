import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FiftyPicturesMain extends Application {

    public static final String LOAD_FROM = "files/links.txt";               // есть файл links1.txt - там ссылки на большие картинки, но оно долго думает.
    private ArrayList<URL> urls = new ArrayList<>(50);

    public static void main(String[] args) {
        launch(args);
    }

    public void fileLinksReading() {
        File file = new File(LOAD_FROM);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNextLine()) {
            try {
                this.urls.add(new URL(scanner.nextLine()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public void showPictures(Pane root) {
        fileLinksReading();
        Random random = new Random();
        final int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(processors);
        int rows = 0;
        for (int i = 0; i < 25; i++) {
            ImageView imageView = new ImageView();
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            if (i > 0 && (i % 5) == 0) {
                rows++;
            }
            if (rows > 0)
                imageView.setTranslateY(100 * rows);
            imageView.setTranslateX(100 * (i % 5));
            root.getChildren().addAll(imageView);
            pool.submit(() -> {
                try {
                    URL url = urls.get(random.nextInt(50));
                    Image image = new Image(url.openStream());
                    Platform.runLater(() -> {
                        imageView.setImage(image);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        pool.shutdown();
    }

    public void pressButton(Pane root){
        Button button = new Button("Обновить");
        button.setTranslateX(510);
        button.setOnAction(event -> {
            root.getChildren().clear();
            root.getChildren().addAll(button);
            showPictures(root);});
        root.getChildren().addAll(button);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setWidth(600);
        primaryStage.setHeight(600);
        primaryStage.show();
        pressButton(root);

    }
}
