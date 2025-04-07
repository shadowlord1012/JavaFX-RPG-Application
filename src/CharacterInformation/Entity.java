package CharacterInformation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import Engine.Vector2;
import Engine.World;
import Settings.Global;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

public abstract class Entity extends Region{
	
	//variable and basic methods
	private String name;	//Name of the entity
	private String hexCode;	//Hex code in the database
	private String imageHexCode; //Hex code for image in database
	private String filePath;	//Where the image is located
	private Map<String,Integer> status = new HashMap<>(); //key is status name	
	private int width;	//Width of the entity
	private int height;	//height of the entity
	private BufferedImage img;	//Image of the entity
	private int numberOfXImages;	//How many images in a row
	private int renderingCounter[] = {0,0,6}; // what is rendering
	private int renderSpeed = 3; //How fast the images renders
	private int movementSpeed;	//How fast the entity will move
	private Rectangle hitBox;	//What area will cause damage to the entity
	private boolean isActive = true;
	private Vector2 position;
	private int directionFacing;	//Which way is the entity facing
	private Abilities[] activeAbilities = new Abilities[4];
	
	public boolean IsMoving = false;
	
	public Vector2 getPosition() {return position;}	//Position of the entity
	public void setPosition(Vector2 value) {position = value;}
	
	public String getHexCode() {return hexCode;}
	public void setHexCode(String value) {hexCode = value;}
	public String getImageHexCode() { return imageHexCode;}
	public void setImageHexCode(String value) {imageHexCode = value;}
	public String getName() {return name;}
	public void setName(String value) {name = value;}
	public String getFilePath() {return filePath;}
	public void setFilePath(String value) { filePath = value;}
	public Map<String,Integer> getStatus(){return status;}
	public void setStatus(Map<String,Integer> value) { status = value;}
	public int getEntityWidth() {return width;}
	public void setEntityWidth(int value) {width = value;}
	public int getEntityHeight() {return height;}
	public void setEntityHeight(int value) {height = value;}
	public Rectangle HitBox() {return hitBox;}
	public void SetHitBox(Rectangle value) {hitBox = value;}
	public void setImageRow(int value) {numberOfXImages = value;}
	public int MovementSpeed() {return movementSpeed;}
	public void SetMovementSpeed(int value) {movementSpeed = value;}
	public void SetRow(int value) {renderingCounter[2] = value;}
	public void setDirection(int value) {directionFacing = value;}
	public int getDirection() {return directionFacing;}
	public Abilities getActiveAbilityAtIndex(int index) { return activeAbilities[index];}
	
	/**
	 * Creates a blank empty entity
	 */
	public Entity() {}
	
	public void Initialize()
	{
		try {
			img = ImageIO.read(new File(this.filePath));
		}catch(IOException e) {}
	}
	
	public void setStatusByKeyValuePair(String key, int value) {
		
		if(status.containsKey(key))
			status.put(key, status.get(key)+value);
		else
			status.put(key,value);
	}
	
	public void SelectAbilityLocation(String abilityName,int location)
	{
		if(activeAbilities[location] != null)
		{
			//TODO Add in a verification to the player to change said ability with a new one 
		}
		else 
		{
			if(Global.ABILITIES_LIST.containsKey(abilityName))
			{
				activeAbilities[location] = Global.ABILITIES_LIST.get(abilityName);
			}
			else
			{
				//TODO display a warning to the player that no such ability exists
			}
		}
	}
	/**
	 * Updates the entities information
	 */
	public void Update(World world) {
		
		//Only update the entity once it is moving
		if(this.IsMoving)
		{
			this.renderingCounter[0]++;
			if(this.renderingCounter[0] >= this.renderSpeed)
			{
				this.renderingCounter[0] = 0;
				this.renderingCounter[1]++;
			}
			if(this.renderingCounter[1] > this.numberOfXImages-1) {
				this.renderingCounter[1] = 0;
			}
		}
		else {
			this.renderingCounter[1] = 0;
		}
		
		//updates the ability that is being used
		for(Abilities x : activeAbilities)
		{
			if(x != null)
				if(x.IsActive())
				{
					x.Update(this);
				}
		}
	}
	
	/**
	 * Draws the entity on the screen
	 * @param gc
	 */
	public void DrawEntity(javafx.scene.canvas.GraphicsContext gc) {
		
		//Will only draw the image if it is active
		if(this.isActive)
		{
			Image entityImage = SwingFXUtils.toFXImage(
					this.img.getSubimage(this.width*this.renderingCounter[1],
							this.height*this.renderingCounter[2], 
							this.width, this.height), null);
			
			gc.drawImage(entityImage, 
					this.getPosition().X,
					this.getPosition().Y,
					this.width*Global.SCALE,
					this.height*Global.SCALE);
			
			//Only draws the ability on the screen if it is active
			for(Abilities x : activeAbilities)
			{
				if(x != null)
					if(x.IsActive())
					{	
						x.Draw(gc);
					}
			}
			
		}
	}
}
