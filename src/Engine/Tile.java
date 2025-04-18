package Engine;

import com.google.gson.annotations.SerializedName;

/**
 * Creates a basic tile Object.
 */
public class Tile {

	//Variables and basic methods
	
	@SerializedName("Name")
	private String name;	//Name of the tile
	@SerializedName("FilePath")
	private String filePath;	//file tile sheet path
	@SerializedName("TileSheetX")
	private int tileSheetX;	//The X location on the tile sheet
	@SerializedName("TileSheetY")
	private int tileSheetY;	//The Y location on the tile sheet
	@SerializedName("Width")
	private int width;	//width of the tile
	@SerializedName("Height")
	private int height;	//height of the tile
	@SerializedName("Stoppable")
	private boolean stoppable; 	//is the tile passable
	@SerializedName("damage")
	private boolean isDamaging;	//does it cause damage
	@SerializedName("Active")
	private boolean isActive;	//does it have rendering
	@SuppressWarnings("unused")
	private transient java.awt.image.BufferedImage img; //Image of the tile cannot change after tile is made
	@SerializedName("NumberOfImages")
	private int numberOfImages;	//Gets how many images are in a sprite Sheet
	private transient int renderableCounter[] = {0,0}; //used for the movement of images if needed
	private transient int renderSpeed = 10;
	
	public String getName() {return name;}	//gets the name
	public String getFilePath() {return filePath;}	//gets the file path 
	public int getWidth() {return width;}	//gets the width
	public int getHeight() {return height;} //gets the height
	public boolean Stoppable() {return stoppable;}	//gets the stoppable of the tile
	public boolean IsDamaging() {return isDamaging;} //gets the damaging of the tile
	public boolean IsActive() {return isActive;}	//get the active if needed
	public int getNumberOfImages() {return numberOfImages;}	//gets the number of images in a sprite sheet
	public java.awt.image.BufferedImage getImage(){return img;}
	public int[] getRenderableCounter() {return renderableCounter;}
	
	//methods
	
	/**
	 * Creates a basic blank tile to be used in the engine itself with NO image information 
	 * Loaded
	 * @return A blank empty tile for the Tile Map
	 */
	public void Initialize()
	{
		this.name = "";
		this.filePath = "";
		this.width = 0;
		this.height = 0;
		this.stoppable = false;
		this.isDamaging = false;
		this.isActive = false;
		this.numberOfImages = 0;
	}
	
	/**
	 * Creates a basic Tile With predefined parameters and loads the image
	 * @return A basic Tile for the Tile Map 
	 */
	public void Initialize(String name,String filePath, 
			int tileSheetLocationX, int tileSheetLocationY, 
			int width, int height, int imageNumber,
			boolean stop, boolean damage, 
			boolean active) {
		this.name = name;
		this.filePath = filePath;
		this.width = width;
		this.height = height;
		this.numberOfImages = imageNumber;
		this.stoppable = stop;
		this.isDamaging = damage;
		this.isActive = active;
		this.tileSheetX = tileSheetLocationX;
		this.tileSheetY = tileSheetLocationY;
		
	}
	
	public void LoadImages() {
		try {
			//Loads the main tile sheet
			java.awt.image.BufferedImage tileSheetImg = javax.imageio.ImageIO.read(new java.io.File(this.filePath));
			
			//gets the set image needed
			img = tileSheetImg.getSubimage(tileSheetX*width, tileSheetY*height, width, height);
			
		} catch(java.io.IOException e) {}
	}
	
	/**
	 * Updates the tile if its is a active image only
	 */
	public void Update() {
		
		if(this.isActive) {
			this.renderableCounter[0]++;
			if(this.renderableCounter[0] >= this.renderSpeed)
			{
				this.renderableCounter[1]++;
				this.renderableCounter[0] = 0;
			}
			
			if(this.renderableCounter[1] > this.numberOfImages)
			{
				this.renderableCounter[1] = 0;
			}
		}
	}
}
