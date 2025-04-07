package CharacterInformation;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Engine.Vector2;
import Settings.Global;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Abilities {
	//Variables
	private String name;	//Name of the Ability
	private String filePath;	//File path of the image
	private String ownerName;	//Who used the entity
	private String hexCode;		//Hex code for the database
	
	private Vector2 position;	//Current Position 
	private Vector2 duration;	//How long it will last
	private int movementSpeed;	//How fast it travels on the screen
	private int numberOfImage;	//How many images are in the sprite line
	private int updateSpeed;	//How fast the image will update itself
	private boolean isMoving;	//Is it moving on the screen
	private boolean isActive;	//Is the image visible
	private int directionTravelling;	//Which direction it will be moving 
	private int cost;	//How much magical energy it will use 
	private Vector2 minMaxDamage;	//For the damage range
	private Rectangle hitBox;	//Hit box location for damage
	private BufferedImage img;	//Image of the ability
	private int imageCounter[] = {0,0};	//Counters for the image
	
	public String getName() {return name;}
	public void setName(String value) { name = value;}
	public void setFilePath(String value) { filePath = value;}
	public String getOwnerName() {return ownerName;}
	public void setOwnerName(String value) {ownerName = value;}
	public Vector2 getPosition() {return position;}
	public void setDuration(int value) { duration.Y = value;}
	public int getCost() {return cost;}
	public Vector2 getMinMaxDamage() {return minMaxDamage;}
	public Rectangle getHitBox() {return hitBox;}
	public String getHexCode() {return hexCode;}
	public void setHexCode(String value) {hexCode = value;}
	public boolean IsActive() {return isActive;}
	
	/**
	 * Blank ability Object. Need to initialize separately
	 */
	public Abilities() {}
	
	/**
	 * Initializes the Ability object
	 */
	public void Initialize(String name, String filePath, String hexCode,int movementSpeed, int numberofImages, int updateSpeed,
			Vector2 damageRange, int cost) {
		this.name = name;
		this.filePath = filePath;
		this.hexCode = hexCode;
		this.movementSpeed = movementSpeed;
		this.numberOfImage = numberofImages;
		this.updateSpeed = updateSpeed;
		this.minMaxDamage = damageRange;
		this.cost = cost;
		
		try {
			img = ImageIO.read(new File(this.filePath));
		}catch (IOException e) {e.printStackTrace();}
	}
	
	private void changeImage() {
		//counter for the image change
		imageCounter[0]++;
		
		//changes image once the speed of image change has reached
		if(imageCounter[0] > updateSpeed) {
			imageCounter[0] = 0;
			imageCounter[1]++;
		}
		
		//Will go back to the first image in the sprite line
		if(imageCounter[1] > numberOfImage)
		{
			imageCounter[1] = 0;
		}
	}
	
	private void movement() {
		
		//Starts the duration counter
		duration.X++;
		
		//If the counter is higher then the max amount try everything off
		if(duration.X > duration.Y)
		{
			isMoving = false;
			isActive = false;
			duration.X = 0;
		}
		
		//Changes the position based on which way the entity was facing
		if(isMoving)
		{
			switch(directionTravelling)
			{
				case 0:
					position.Y -= movementSpeed;
					break;
				case 1:
					position.X += movementSpeed;
					break;
				case 2:
					position.Y += movementSpeed;
					break;
				case 3:
					position.X -= movementSpeed;
					break;
			}
		}
	}
	
	/**
	 * Updates the Ability. Using a reference for an entity to get the position and keep it there until done
	 * @param ref
	 */
	public void Update(Entity ref) {
		if(!this.isActive)
		{
			this.position = ref.getPosition();
			this.directionTravelling = ref.getDirection();
		}
		else {
			changeImage();
			movement();
		}
	}
	
	public void Draw(GraphicsContext gc) {
		if(isActive)
		{
			Image abitilyImage = SwingFXUtils.toFXImage(
					img.getSubimage((img.getWidth()/numberOfImage)*imageCounter[1],
							0, (img.getWidth()/numberOfImage), img.getHeight()), null);
			
			gc.drawImage(abitilyImage,
					position.X, 
					position.Y,
					(img.getWidth()/numberOfImage)*Global.SCALE,
					img.getHeight()*Global.SCALE);
		}
	}
}
