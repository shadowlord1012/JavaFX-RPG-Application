package application;
	
import Controls.KeyHandingController;
import Engine.GameEngine;
import Engine.Layer;
import Settings.Global;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;


public class Main extends Application {
	
	private KeyHandingController keyBind = new KeyHandingController();
	
	AnimationTimer gameLoop;
	Layer playfield;
	GameEngine gameEngine;
	long lastTime = System.nanoTime();
	double nsPerTick = 1000000000D/60D;
	
	long lastTimer = System.currentTimeMillis();
	double delta = 0;
	public static final Canvas canvas = new Canvas(Global.RENDER_X,Global.RENDER_Y);
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			//Sets the pane in which everything rests on
			BorderPane root = new BorderPane();
			
			//Creates a new layer for the game to render on
			playfield = new Layer(Global.RENDER_X,Global.RENDER_Y);
			
			//Creates a new pane
			Pane layerPane = new Pane();
			
			layerPane.getChildren().addAll(playfield);
			
			//Sets the pane to the root
			root.setCenter(layerPane);
			
			//Creates the scene
			Scene scene = new Scene(root,Global.RENDER_X,Global.RENDER_Y);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			//Adds in the event handlers if keys are pressed on screen
			scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> { 
				keyBind.keyPressed(keyEvent);});	
			scene.addEventFilter(KeyEvent.KEY_RELEASED, keyEvent -> { 
				keyBind.keyReleased(keyEvent);});
			
			//Initializes the Game Engine itself
			gameEngine = new GameEngine(canvas.getGraphicsContext2D());

			//Adds the drawing canvas
			root.getChildren().add(canvas);
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
			//Starts the game audio
			gameEngine.audio.playBGM(1);
			
			//RUN
			startGame();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Runs the game using AnimationTimer
	 */
	private void startGame() {
		
		
		gameLoop = new AnimationTimer() {
			
			
			@Override
			public void handle(long now) {
				
				//TODO add in the controller that handles all information in the game EX. the game controller
				delta +=(now-lastTime)/nsPerTick;
				lastTime = now;
				while(delta >=1)
				{
					gameEngine.Update();
				
					gameEngine.Draw();
				delta-=1;
				}
			}
		};
		
		gameLoop.start();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
