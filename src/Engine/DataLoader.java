package Engine;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import CharacterInformation.Abilities;
import CharacterInformation.Player;

public class DataLoader {
	
	//The URL will be the database in which the game connects
	private static final String URL ="jdbc:mysql://localhost:3306/RPG";
	
	//This will be where the user is able to access only the play information from there account
	private static final String USERNAME ="admin";
	private static final String PASSWORD = "North2024!";
	private static String currentMapName;
	
	//Database connection information
	private Connection connection;
	private PreparedStatement selectPlayerInformation;
	private PreparedStatement selectStatusData;
	private PreparedStatement selectImageData;
	private static PreparedStatement selectMapData;
	private PreparedStatement selectAbilityData;
	
	public DataLoader() {
		try {
			//Connects to the database
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			
			//Sets up all the SQL statments that are needed for the game data
			selectPlayerInformation = connection.prepareStatement(
					"SELECT * FROM Player ORDER BY ID");
			selectStatusData = connection.prepareStatement(
					"SELECT * FROM Status ORDER BY StatID");
			selectImageData = connection.prepareStatement(
					"SELECT * FROM Image ORDER BY ImageID");
			selectMapData = connection.prepareStatement(
					"SELECT * FROM Map ORDER BY MapID");
			selectAbilityData = connection.prepareStatement(
					"SELECT * FROM Ability ORDER BY ID");
			
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
	/**
	 * Loads all the information about the player character
	 * @return
	 */
	public final Player LoadDataForPlayer() {
		
		//Creates basic player object
		Player playerLoad = new Player();
		
		//Loads the basic player information 
		try (ResultSet resultSet = selectPlayerInformation.executeQuery()){
			while (resultSet.next()) {
				
				//Checks to see if the player is linked to the user name
				if(resultSet.getString("PlayerID").equals(USERNAME))
				{
					//Sets all the values of the player 
					playerLoad.setName(resultSet.getString("Name"));
					playerLoad.setHexCode(resultSet.getString("StatID"));
					playerLoad.setImageHexCode(resultSet.getString("ImageID"));
					playerLoad.setPosition(new Vector2(
							resultSet.getDouble("PositionX"),
							resultSet.getDouble("PositionY")));
					playerLoad.setMapHex(resultSet.getString("MapID"));
				}
			}
			
		}catch(SQLException e) {e.printStackTrace();}
		
		//Loads all the status based off the Status ID code
		try (ResultSet resultSet = selectStatusData.executeQuery()){
			while(resultSet.next())
			{
				//checks to make sure all the status values are belonging to that player
				if(resultSet.getString("StatID").equals(playerLoad.getHexCode()))
				{
					playerLoad.setStatusByKeyValuePair("Health", resultSet.getInt("Health"));
					playerLoad.setStatusByKeyValuePair("Magic", resultSet.getInt("Magic"));
					playerLoad.setStatusByKeyValuePair("Attack", resultSet.getInt("Attack"));
					playerLoad.setStatusByKeyValuePair("PhysicalDefence", resultSet.getInt("PhysicalDefence"));	
					playerLoad.setStatusByKeyValuePair("MagicalAttack", resultSet.getInt("MagicalAttack"));	
					playerLoad.setStatusByKeyValuePair("MagicalDefence", resultSet.getInt("MagicalDefence"));	
					playerLoad.SetMovementSpeed(resultSet.getInt("MovementSpeed"));
				}
				
			}
		}catch (SQLException e) {e.printStackTrace();}
		
		//Loads all the imaging information
		try (ResultSet resultSet = selectImageData.executeQuery()){
			while(resultSet.next())
			{
				//Checks to make sure the correct Images are connected to the player
				if(resultSet.getString("ImageID").equals(playerLoad.getImageHexCode()))
				{
					playerLoad.setFilePath(resultSet.getString("Path"));
					playerLoad.setEntityWidth(resultSet.getInt("Width"));
					playerLoad.setEntityHeight(resultSet.getInt("Height"));
					playerLoad.setImageRow(resultSet.getInt("ImageCol"));
				}
			}
			
		}catch (SQLException e) {e.printStackTrace();}
		
		if(playerLoad.getHexCode() == null)
			return null;
		else
		{
			playerLoad.Initialize();
			return playerLoad;
		}
	}
	
	/**
	 * Loads the Tile Map in which the player is last known to be on
	 * @param player
	 * @return
	 * @throws IOException
	 */
	public static final TileMap LoadDataForCurrentMap(Player player) throws IOException {
		
		TileMap mapLoad = new TileMap();
		
		//gets the information from the database
		try (ResultSet resultSet = selectMapData.executeQuery()){
			while (resultSet.next())
			{
				//Makes sure the correct map is loaded (Last known location in the world)
				if(player.getMapHex().equals(resultSet.getString("MapID")))
				{
					//Sets which map name the player was on
					player.setMap(resultSet.getString("Name"));
					
					//Sets the current map name for when the level first loads
					currentMapName = resultSet.getString("Name");
					
					//Reads the json text file for the map that is loading
					try(FileReader rd = new FileReader("/home/mike/eclipse-workspace/JavaRPGApplication/SliverMoon/Assets/tilemapdata/"+resultSet.getString("Name")+".txt"))
					{
						Gson gson = new GsonBuilder().create();
						
						mapLoad = gson.fromJson(rd, TileMap.class);
					}
				}
			}
			
		} catch(SQLException e) {e.printStackTrace();}
		
		//Loads all the tile images on the map
		for(int x = 0; x < mapLoad.getXTiles(); x++)
			for(int y = 0; y < mapLoad.getYTiles();y++)
				mapLoad.getTilesOnMap()[x][y].LoadImages();
		
		return mapLoad;
	}
	
	//Loads in the levelData and sets map name.
	public static final Level LoadDataForLevel(Player player) throws IOException {
		
		//Creates a basic Level object
		Level levelLoad = new Level();
		
		//Adds the current map to the level
		levelLoad.LoadMap(LoadDataForCurrentMap(player));
		
		//sets the level to the current location the player is at
		levelLoad.SetMapName(currentMapName);	
		//TODO Set map name to players last location which will be saved in the database
		
		return levelLoad;
	}

	/**
	 * Loads all the Abilities into the game 
	 * @return
	 */
	public final Map<String, Abilities> LoadAbilityData(){
		
		Map<String, Abilities> abilityLoad = new HashMap<>();
		
		try (ResultSet resultSet = selectAbilityData.executeQuery())
		{
			while(resultSet.next()) {
				Abilities temp = new Abilities();
				
				temp.Initialize(resultSet.getString("Name"),resultSet.getString("FilePath"),
						resultSet.getString("HexCode"),
						resultSet.getInt("MovementSpeed"),resultSet.getInt("NumberOfImages"),
						resultSet.getInt("RenderSpeed"),new Vector2(resultSet.getInt("MinDamage"), resultSet.getInt("MaxDamage")),
						resultSet.getInt("Cost"));
				if(!abilityLoad.containsKey(temp.getName()))
				{
					abilityLoad.put(temp.getName(), temp);
				}
				else {
					//TODO add in error for not loading abilities correct
				}
			}
		}catch(SQLException e) {e.printStackTrace();}
		
		return abilityLoad;
	}
}
