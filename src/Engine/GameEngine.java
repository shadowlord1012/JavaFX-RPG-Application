package Engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import CharacterInformation.Player;
import Settings.Global;
import javafx.scene.canvas.GraphicsContext;

public class GameEngine {
	
	private static GraphicsContext GC;
	private Map<String,World> gameWorldDirectory = new HashMap<>();
	private String worldName = "Map01";
	public static Audio audio;
	List<Player> playerList = new ArrayList<>();
	private DataLoader data= new DataLoader();
	
	public GameEngine(GraphicsContext gs) {
		GC = gs;
		initialize();
	}
	
	private void initialize() {
		audio = new Audio();
		playerList.add(data.LoadDataForPlayer());
		playerList.forEach(e -> {
				if(e.getName().equals("Shadow Lord")) {
					try {
						gameWorldDirectory.put("Map01",new World(e));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}});
	}
	
	public void Update() {
		
		if(gameWorldDirectory.containsKey(worldName))
			gameWorldDirectory.get(worldName).Update();
		
		playerList.forEach(e -> e.Update(gameWorldDirectory.get(worldName)));
	}
	
	public void Draw() {
		
		GC.clearRect(0, 0, Global.RENDER_X, Global.RENDER_Y);
		
		if(gameWorldDirectory.containsKey(worldName))
			gameWorldDirectory.get(worldName).Draw(GC);
			
		playerList.forEach(e -> e.DrawEntity(GC));
	}
}
