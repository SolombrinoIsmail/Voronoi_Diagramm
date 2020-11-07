import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

//This Class handles the Window that will be displayed as sonn as you lunch the main app.
public class VornoiWindow extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Vornoi Diagramm Animation");
        primaryStage.setScene(new Scene(root, 1225, 900));
        primaryStage.setResizable(false);//makes the windows not resizable(lazy solution for dynamic)
        primaryStage.show();
    }
}