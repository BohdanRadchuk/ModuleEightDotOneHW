import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class FiftyPicturesMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);

    }
}
