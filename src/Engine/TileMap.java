package Engine;

import Settings.Global;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;

/**
 * Loads all the tiles to be rendered on the screen
 * 
 */


public class TileMap {

	//Variables and basic Methods
	private String name;	//Name of the map
	private Tile[][] tilesOnMap;	//Two dimensional array of tiles on screen [x][y]
	private int xTiles;	//number of tiles on the x axis
	private int yTiles;	//NUmber of tiles on the y axis 
	
	public Tile[][] getTilesOnMap(){return tilesOnMap;}
	public String getName() {return name;}
	public int getXTiles() {return xTiles;}
	public int getYTiles() {return yTiles;}
	public Tile getTileOnMapByLocation(int x, int y) {return tilesOnMap[x][y];}
	
	/**
	 * Blank Tile Map
	 */
	public TileMap()
	{
		this.name ="";
		this.tilesOnMap = new Tile[0][0];
		this.xTiles = 0;
		this.yTiles = 0;
	}
	
	/**
	 * Default TileMap 
	 */
	public TileMap(String name, int xTile, int yTile)
	{
		this.name = name;
		this.xTiles = xTile;
		this.yTiles = yTile;
		this.tilesOnMap = new Tile[xTile][yTile];
	}
	
	/**
	 * Loads the tiles to the tile map at a set Location x and y
	 */
	public void loadTileDataToMap(Tile tile, int x, int y)
	{
		//makes sure the tile map has already been made
		if(this.tilesOnMap != null)
		{
			this.tilesOnMap[x][y] = tile;	//adds tile at that location
		}
	}
	
	/**
	 * Updates the tile map
	 */
	public void Update() {
		
		for(int x = 0;x < xTiles;x++)
		{
			for(int y = 0;y < yTiles;y++)
			{
				this.tilesOnMap[x][y].Update();
			}
		}
	}
	
	/**
	 * Draws the tile map on the screen
	 * @param gc
	 */
	public void DrawTiles(javafx.scene.canvas.GraphicsContext gc) {
		
		for(int x = 0; x < xTiles;x++)
		{
			for(int y = 0; y < yTiles;y++)
			{
				//converts a Swing Element to a FX element
				Image tileImage = SwingFXUtils.toFXImage(
						tilesOnMap[x][y].getImage().getSubimage(
								(0+(tilesOnMap[x][y].getWidth()*tilesOnMap[x][y].getRenderableCounter()[1])), 
								0,
								tilesOnMap[x][y].getWidth(), 
								tilesOnMap[x][y].getHeight()),
						null);
				
				//Draws it on the screen
				gc.drawImage(tileImage,
						(x*tilesOnMap[x][y].getWidth()*Global.SCALE)+ Global.WORLD_CAMERA.Position.X,
						(y*tilesOnMap[x][y].getHeight()*Global.SCALE)+ Global.WORLD_CAMERA.Position.Y,
						tilesOnMap[x][y].getWidth()*Global.SCALE,
						tilesOnMap[x][y].getHeight()*Global.SCALE);
			}
		}
	}
}
