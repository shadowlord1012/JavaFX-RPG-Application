package Engine;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import CharacterInformation.Player;

public class DataLoader {
	
	//The URL will be the database in which the game connects
	private static final String URL ="jdbc:mysql://localhost:3306/RPG";
	
	//This will be where the user is able to access only the play information from there account
	private static final String USERNAME ="admin";
	private static final String PASSWORD = "North2024!";
	
	//Database connection information
	private Connection connection;
	private PreparedStatement selectPlayerInformation;
	private PreparedStatement selectStatusData;
	private PreparedStatement selectImageData;
	private static PreparedStatement selectMapData;
	
	public DataLoader() {
		try {
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			selectPlayerInformation = connection.prepareStatement(
					"SELECT * FROM Player ORDER BY ID");
			selectStatusData = connection.prepareStatement(
					"SELECT * FROM Status ORDER BY StatID");
			selectImageData = connection.prepareStatement(
					"SELECT * FROM Image ORDER BY ImageID");
			selectMapData = connection.prepareStatement(
					"SELECT * FROM Map ORDER BY MapID");
			
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public final Player LoadDataForPlayer() {
		
		Player playerLoad = new Player();
		
		//Loads the basic player information 
		try (ResultSet resultSet = selectPlayerInformation.executeQuery()){
			while (resultSet.next()) {
				
				if(resultSet.getString("PlayerID").equals(USERNAME))
				{
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
	
	//Loads the map in which the player is on
	public static final TileMap LoadDataForCurrentMap(Player player) throws IOException {
		
		TileMap mapLoad = new TileMap();
		
		//gets the information from the database
		try (ResultSet resultSet = selectMapData.executeQuery()){
			while (resultSet.next())
			{
				if(player.getMapHex().equals(resultSet.getString("MapID")))
				{
					mapLoad = new TileMap(
							resultSet.getString("Name"),
							resultSet.getInt("SizeX"),
							resultSet.getInt("SizeY"));
					player.setMap(resultSet.getString("Name"));
				}
			}
			
		} catch(SQLException e) {e.printStackTrace();}
		
		//Loads in each tile to the tile map
		if(mapLoad != null)
		{
			try(Scanner input = new Scanner(Paths.get("./Assets/tilemapdata/"+mapLoad.getName()+".txt")))
			{
				while(input.hasNext())
				{
					//Blank tile
					Tile tileLoad = new Tile();
					
					//All the information of the tile
					tileLoad.Initialize(input.next(), 
							input.next(), input.nextInt(), 
							input.nextInt(), input.nextInt(), 
							input.nextBoolean(), input.nextBoolean(),
							input.nextBoolean());
					
					//loads the tile to the map
					mapLoad.loadTileDataToMap(tileLoad, input.nextInt(), input.nextInt());
					
				}
			}
			
		}
		
		return mapLoad;
	}
	
	//Loads in the levelData and sets map name.
	public static final Level LoadDataForLevel(Player player) throws IOException {
		
		Level levelLoad = new Level();
		
		levelLoad.LoadMap(LoadDataForCurrentMap(player));
		levelLoad.SetMapName("map01");	//TODO Set map name to players last location which will be saved in the database
		
		
		return levelLoad;
	}
}
