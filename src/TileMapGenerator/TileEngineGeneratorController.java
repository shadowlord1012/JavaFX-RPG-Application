package TileMapGenerator;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import Engine.Tile;
import Engine.TileMap;
import Engine.Vector2;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class TileEngineGeneratorController {

	//All The fxml information
    @FXML
    private MenuItem NewFileMenu;

    @FXML
    private MenuItem OpenFileMenu;

    @FXML
    private MenuItem OpenTileSheetMenu;

    @FXML
    private MenuItem QuitMenu;

    @FXML
    private ImageView TileMapImage;

    @FXML
    private CheckBox activeTile;

    @FXML
    private TextField currentTileX;

    @FXML
    private TextField currentTileY;

    @FXML
    private CheckBox damagingTile;

    @FXML
    public Canvas finishMap;

    @FXML
    private TextField numberOfImages;

    @FXML
    private Label outputInformation;

    @FXML
    private CheckBox passibleTile;

    @FXML
    private MenuItem saveAsMap;

    @FXML
    private MenuItem saveCurrentMap;

    @FXML
    private TextField tileMapHeight;

    @FXML
    private TextField tileMapName;

    @FXML
    private TextField tileMapWidth;

    @FXML
    private ImageView tileSelectedImage;

    @FXML
    private TextField tileSizeX;

    @FXML
    private TextField tileSizeY;
    
    @FXML
    private TextField tileNameText;
    
    //Variables
    private String tileSheetFilePath;
    private int[] tileSheetSize = {0,0}; // [0] = width(X) ||  [1] = Height(1)
    private int[] sizeOfTile = {0,0};
    private boolean isDamaging;
    private boolean isActive;
    private boolean isPassible;
    private int numberOfImagesInLine;
    private String mapName;
    private int mapWidth;
    private int mapHeight;
    private boolean isSaved = false;
    private boolean[] isAddedOnce = {false,false,false,false};
    private boolean placeTile = true;
    private Vector2 tileLocation = new Vector2(0,0);
    private Vector2 tileSheetTileLocation = new Vector2(0,0);
    private Vector2 aspectRatio = new Vector2(0,0);
    private Image tileSheetImg;
    private WritableImage tileImg;
    private javafx.scene.image.Image[][] tilesOnMap = new javafx.scene.image.Image[mapWidth][mapHeight];
    private GraphicsContext gc;
    private TileMap map;
    
    private FileChooser fileChooser = new FileChooser();
    private Stage currentStage;
    
    /**
     * Sets up the initial Values for the Tile Engine Generator Controller
     * @param stage
     */
    public void initialize(Stage stage) {
    	currentStage = stage;
    	finishMap = new Canvas(305,240);
    }
    
    //disables all the text fields
    private void disableInput() {
    	tileMapHeight.setEditable(false);
    	tileMapWidth.setEditable(false);
    	tileSizeX.setEditable(false);
    	tileSizeY.setEditable(false);
    	tileMapName.setEditable(false);
    }    
    
    /**
     * Draws the tile map on the screen
     */
    public void drawMap()
    {
    	//Goes through the 2 dimentional array and displays all the value images on the canvas
		for(int x = 0; x < mapWidth;x++)
		{
			for(int y = 0; y < mapHeight;y++)
			{
				if(tilesOnMap[x][y] != null)
				{
					gc.drawImage(tilesOnMap[x][y], 
							x*tilesOnMap[x][y].getWidth(),
							y*tilesOnMap[x][y].getHeight());
				}
			}
		}
    }
    
    //creates the time for the map
    private Tile setTile(String name) {
    	
    	//creates a blank tile
    	Tile tile = new Tile();
    	    	
    	//checks to see if it is an active tile
    	if(activeTile.isSelected() && !numberOfImages.getText().isBlank())
    		numberOfImagesInLine = Integer.parseInt(numberOfImages.getText());
    	else
    		numberOfImagesInLine = 0;    	
    	
    	//does it cause damage
    	isDamaging = damagingTile.isSelected();
    	
    	//is it an active moving tile
    	isActive = activeTile.isSelected();
    	
    	//can the entity move over it
    	isPassible = passibleTile.isSelected();
    	
    	//Creates the tile
    	tile.Initialize(name, tileSheetFilePath, 
    			((int)Double.parseDouble(currentTileX.getText())),
    			((int)Double.parseDouble(currentTileY.getText())),
    			sizeOfTile[0], sizeOfTile[1], numberOfImagesInLine, 
    			isPassible, isDamaging, isActive);
    	
    	//returns the tile
    	return tile;
    }
   
    @FXML
    void getXYOnMouseMoved(MouseEvent event) {
    	if(mapWidth > 0 && mapHeight > 0 && sizeOfTile[0] > 0 && sizeOfTile[1] > 0)
    	{
    		//gets the X and Y of the tile
    		tileLocation.X = (int)(event.getX()/sizeOfTile[0]);
    		tileLocation.Y = (int)(event.getY()/sizeOfTile[1]);
    		
    		//Will not allow you to place a tile on the map if its bigger then the map itself
    		if(tileLocation.X > mapWidth || tileLocation.Y > mapHeight) {
    			outputInformation.setText("Unable to place tile");
    			placeTile = false;
    		}
    		else {
    			placeTile = true;
    		}
    	}
    }

    @FXML
    void placeOnMapOnMouseClicked(MouseEvent event) {
    	
    	//places the tile on the canvas
    	if(placeTile)
    	{   		
    		//Only loads this on first time placement
    		if(tilesOnMap.length == 0) {
    			//sets how big the map will be
    			tilesOnMap = new javafx.scene.image.Image[mapWidth][mapHeight];
    			
    			//creates the graphics context to draw the new map
    	    	gc = finishMap.getGraphicsContext2D();
    	    	
    	    	//creates a tile map to store all the tiles
    	    	map = new TileMap(mapName,mapWidth,mapHeight);
    	    	
    	    	//Disable set inputs so the user can not change until a new map is selected
    	    	disableInput();
    		}
    		
    		//which image is to be drawn on the canvas at a set x y coord
    		tilesOnMap[(int)tileLocation.X][(int)tileLocation.Y] = tileImg; 		
    		
    		//Loads the tile data to the map that is being created
    		map.loadTileDataToMap(setTile(tileNameText.getText()), (int)tileLocation.X, (int)tileLocation.Y);
    		
    		//Draws the map on the canvas
    		drawMap();  		
    	}
    }
    /*
     * TIle Sheet methods
     * 
     */
    
    @FXML
    void getXYTileSheetOnMouseMoved(MouseEvent event) {
    	
    	//Makes sure the image is valid
    	if(sizeOfTile[0] > 0 && sizeOfTile[1] > 0 && tileSheetImg != null)
    	{
    		//Gets the aspect ratio if the tile sheet is greater then 200 px by 200 px
    		aspectRatio.X = tileSheetImg.getWidth(null)/TileMapImage.getFitWidth();
    		aspectRatio.Y = tileSheetImg.getHeight(null)/TileMapImage.getFitHeight();
    		
    		//gets where the mouse is for a vector 2 position
    		Vector2 mouseLocation = new Vector2(event.getX(),event.getY());
    		
    		//gets the actual location on the tile sheet itself
    		mouseLocation.multiplyVec(aspectRatio);
    		
    		//gets the location of the tile on the tile sheet
    		tileSheetTileLocation.X = (int)(mouseLocation.X/sizeOfTile[0]);
    		tileSheetTileLocation.Y = (int)(mouseLocation.Y/sizeOfTile[1]);
    		
    		
    	}
    	
    }

    @FXML
    void tileSelectOnClick(MouseEvent event) {
    	    
    	//Changes a Java.awt image to a usable  image for the Image view on the screen
    	tileImg = SwingFXUtils.toFXImage(
				((BufferedImage)tileSheetImg).getSubimage(
						(int)tileSheetTileLocation.X*sizeOfTile[0], (int)tileSheetTileLocation.Y*sizeOfTile[1], sizeOfTile[0], sizeOfTile[1]),null);

    	//Sets the current tile location on the tile sheet
		currentTileX.setText(tileSheetTileLocation.X+"");
		currentTileY.setText(tileSheetTileLocation.Y+"");
		
		//Sets the image to the image view
		tileSelectedImage.setImage(tileImg);
    }
    

    @FXML
    void placeOnMapOnMouseEntered(MouseEvent event) {
    }
    
    @FXML
    void OpenExsitingMap(ActionEvent event) {
    	   	
    }
    
    @FXML
    void OpenTileSheet(ActionEvent event) {
    	
    	//opens a single file only
    	File file = fileChooser.showOpenDialog(currentStage);
    	
    	//As long as a file is selected
    	if(file != null)
    	{
    		//Only if the file path is an image file Ex. png, jpg, or jpeg
    		if(file.getPath().contains(".png") || file.getPath().contains(".jpg") ||file.getPath().contains(".jpeg") )
    		{  				
    			//trys to load the image
				try {
					
					//gets the image file
					tileSheetImg = ImageIO.read(file);
					
					tileSheetFilePath = file.getPath();
					//changes the image to be used in javafx from swing
	    			javafx.scene.image.Image imgfx = SwingFXUtils.toFXImage((BufferedImage) tileSheetImg, null);
	    			
	    			//Sets the tileSheetSize for aspect ratio later
	    			tileSheetSize[0] = tileSheetImg.getWidth(null);
	    			tileSheetSize[1] = tileSheetImg.getHeight(null);
	    			
	    			//Sets the image to the Image view where it fills the 200x200 space. Does not keep original ratio
	    			TileMapImage.setImage(imgfx);
	    			TileMapImage.setFitWidth(200);
	    			TileMapImage.setFitHeight(200);
	    			TileMapImage.setPreserveRatio(false);
	    			
				} catch (IOException e) {e.printStackTrace();}
				
    		}
    	}
    }

    @FXML
    void numberOfImagesOnChange(InputMethodEvent event) {

    }

    @FXML
    void openNewMapFile(ActionEvent event) {
    	tileMapHeight.setEditable(true);
    	tileMapWidth.setEditable(true);
    	tileSizeX.setEditable(true);
    	tileSizeY.setEditable(true);
    	tileMapName.setEditable(true);
    	TileMapImage.setImage(null);
    	tileSelectedImage.setImage(null);
    	tileSizeX.setText(null);
    	tileSizeY.setText(null);
    	tileMapHeight.setText(null);
    	tileMapWidth.setText(null);
    	tileNameText.setText(null);
    	tileMapName.setText(null); 
    	//TODO finish deleting values
    }

    @FXML
    void quitOnAction(ActionEvent event) {
    	
    	//Will only quit if your work has been saved only one warning
    	if(!isSaved)
    	{
    		outputInformation.setText("Saved work? Click quit again to exit without saving");
    		
    		isSaved = !isSaved;
    	}
    	else 
    		System.exit(1);
    }

    @FXML
    void saveAsMapOnAction(ActionEvent event) { 
    	
    	//TODO add in through file chooser dialog a place in where you can save the file instead of a default
    	
    	isSaved = true;
    }

    /**
     * Creates a Json file of the tile map to be loaded into the game 
     * @param map
     * @param fileName
     * @return Creates a Json map file
     */
    private void saveMap(TileMap map, String fileName)
    {
    	//Opens a new File writer stream
    	try(FileWriter wr = new FileWriter(fileName))
    	{
    		//Creates a gson (json) builder to make the json file
    		Gson gson = new GsonBuilder().setPrettyPrinting().create();
    		
    		//Writes all the information to the hard drive  
    		gson.toJson(map,wr);
    		
    		System.out.println(fileName);
    		
    		wr.close();
    		
    	}catch(IOException e) {e.printStackTrace();} 
    	catch(JsonIOException e) {e.printStackTrace();}
    }
    
    @FXML
    void saveCurrentMapOnAction(ActionEvent event) {
    	
    	boolean unableToSave = false;
    	
    	//Checks the Tile Map object to see if everything is on the map
    	for(int x = 0; x< mapWidth;x++)
    	{
    		for(int y = 0; y < mapHeight;y++)
    		{
    			//If the map is missing any data
    			if(map.getTilesOnMap()[x][y] == null)
    			{
    				unableToSave = true;
    			}
    		}
    	}
    	
    	//Will only save if the map has been built
    	if(!unableToSave) {
    		
    		//Lets the user know the map has been saved in the GUI
    		outputInformation.setText("Map Saved");
    		
    		//Creates the file path string for the json to save two
    		String filePathString = "/home/mike/eclipse-workspace/JavaRPGApplication/SliverMoon/Assets/tilemapdata/"+tileMapName.getText()+".txt";
    		
    		//Runs the gson to save the map to the specified location
    		saveMap(map,filePathString);
    		
    		//So you can quit the application
          	isSaved = true;
    	}
    	else {
    		
    		//Lets the user know the map is not complete
    		outputInformation.setText("Unable to save map");
    	}
    	
    	
    }

    @FXML
    void sizeOFXTileOnChange(MouseEvent event) {
    	
    	//Only adds the listener to the Text field once
    	if(isAddedOnce[1] == false)
    	{
    		//adds the listener and gets the value of the X coord of the tile
    		tileSizeX.textProperty().addListener((observable,oldValue,newValue) -> {
    			if(!newValue.isBlank() && newValue != null) {
    				
    				//Gets the new value and stores it
    				sizeOfTile[0] = Integer.parseInt(newValue);
    				System.out.println(sizeOfTile[0]);
    				
    				// makes it so it cant add another listener
    				isAddedOnce[1] = true;
    			}
        	});   
    	}
    }

    @FXML
    void sizeOFYTileOnChange(MouseEvent event) {
    	
    	//Only adds the listener to the Text field once
    	if(isAddedOnce[0] == false)
    	{
    		//adds the listener and gets the value  of the Y coord of the tile
    		tileSizeY.textProperty().addListener((observable,oldValue,newValue) -> {
    			if(!newValue.isBlank() && newValue != null) {
    				
    				//Sets the size of the tile
    				sizeOfTile[1] = Integer.parseInt(newValue);
    				
    				//makes it so it can add another listener
    				isAddedOnce[0] = true;
    			}
        	});   
    	}
    	
    	
    }

    @FXML
    void tileMapHeightOnChange(MouseEvent event) {
    	//Only adds the listener to the Text field once
    	if(isAddedOnce[2] == false)
    	{
    		//adds the listener and gets the value  of the Y coord of the tile
    		tileMapHeight.textProperty().addListener((observable,oldValue,newValue) -> {
    			if(!newValue.isBlank() && newValue != null) {
    				
    				//sets the how many tiles high the map will be
    				mapHeight = Integer.parseInt(newValue);
    				
    				//makes it so it can add another listener
    				isAddedOnce[2] = true;
    			}
        	});   
    	}
    }
    
    @FXML
    void tileMapWidthOnChange(MouseEvent event) {
    	//Only adds the listener to the Text field once
    	if(isAddedOnce[3] == false)
    	{
    		//adds the listener and gets the value  of the Y coord of the tile
    		tileMapWidth.textProperty().addListener((observable,oldValue,newValue) -> {
    			if(!newValue.isBlank() && newValue != null) {
    				
    				//Sets how many tiles long the map will be
    				mapWidth = Integer.parseInt(newValue);
    				
    				//makes it so it can add another listener
    				isAddedOnce[3] = true;
    			}
        	});   
    	}
    }

}
