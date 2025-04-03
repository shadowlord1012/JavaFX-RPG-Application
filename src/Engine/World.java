package Engine;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import CharacterInformation.Player;
import javafx.scene.canvas.GraphicsContext;

public class World {
	List<Level> levelList = new ArrayList<>();
	Player playerRef;
	
	public Level currentLevel(String name) { 
		for(Level e : levelList)
		{
			if(e.getCurrentMapName().equals(name))
				return e;
		}
		return null;
	}
	
	public World(Player player) throws IOException
	{
		playerRef = player;
		initialize();
	}
	
	private void initialize() throws IOException
	{
		levelList.add(DataLoader.LoadDataForLevel(playerRef));
	}
	
	public void Update()
	{
		levelList.forEach(e -> e.Update());
	}
	
	public void Draw(GraphicsContext gc) {
		
		levelList.forEach(e -> e.DrawTileMap(gc));
	}
}
