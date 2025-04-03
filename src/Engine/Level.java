package Engine;

import java.util.HashMap;
import java.util.Map;

public class Level {
	
	//Variables and basic Methods
	private String name;	//Name of the level
	private String currentMapName = "map01";	//Current map 
	private Map<String,TileMap> tileMaps = new HashMap<>();	//String is the map name
	
	public String getLevelName() {return name;}
	public String getCurrentMapName() {return currentMapName;}
	public void SetMapName(String value) { currentMapName = value;}
	public TileMap getTileMap(String value) {return tileMaps.get(value);}
	
	/**
	 * Blank Level Data
	 */
	public Level()
	{
		this.name = "";
		this.currentMapName = "";
		this.tileMaps.put("blank", new TileMap());
	}
	
	/**
	 * Creates the start of the level data
	 * @param name
	 * @param currentMap
	 */
	public Level(String name, String currentMap)
	{
		this.name = name;
		this.currentMapName = currentMap;
	}
	
	/**
	 * Loads a tile map to the map of the level
	 * @param map
	 */
	public void LoadMap(TileMap map) {
		if(!this.tileMaps.containsKey(map.getName()))
		{
			this.tileMaps.put(map.getName(), map);
		}
	}
	
	/**
	 * updates only the set map on the screen
	 */
	public void Update() {	
		
		//updates only the map in which is currently loaded
		this.tileMaps.get(currentMapName).Update();
		
	}
	
	public void DrawTileMap(javafx.scene.canvas.GraphicsContext gc) {
		
		//draws only the images in the map that is currently loaded
		if(this.tileMaps.containsKey(currentMapName))
			this.tileMaps.get(currentMapName).DrawTiles(gc);
	}
}
