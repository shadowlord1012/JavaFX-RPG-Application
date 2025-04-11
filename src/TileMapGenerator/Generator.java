package TileMapGenerator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class Generator extends Application{

	TileEngineGeneratorController controller;
	@Override
	public void start(Stage primaryStage) {
		try {
			
			Parent root = FXMLLoader.load(getClass().getResource("TileGeneratorFXML.fxml"));
			
			Scene scene = new Scene(root,668,347);

			
			primaryStage.setScene(scene);
			controller = new TileEngineGeneratorController();
			controller.initialize(primaryStage);
			primaryStage.show();
			
			
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
