package CharacterInformation;


import Controls.KeyHandingController;
import Engine.Vector2;
import Engine.World;
import Settings.Global;

public class Player extends Entity{

	private String mapHex;	//Which map the player is located on in Hex
	private String mapID;
	
	public void setMapHex(String value) {mapHex = value;}
	public String getMapHex() { return mapHex;}
	
	public void setMap(String value) {mapID = value;}
	public String getMap() {return mapID;}
	
	public Player() {
		super();
	}
	
	private void movement(World world)
	{
		if(KeyHandingController.Movement[0]|| KeyHandingController.Movement[1]
				|| KeyHandingController.Movement[2] || KeyHandingController.Movement[3])
		{
			this.IsMoving = true;
			if(KeyHandingController.Movement[0]) // UP
			{
				if(this.getPosition().Y+this.getEntityHeight() < 0 && Global.WORLD_CAMERA.Position.Y <=0)
					this.setPosition(new Vector2(this.getPosition().X, this.getPosition().Y+this.MovementSpeed()));
				else
					this.setPosition(new Vector2(this.getPosition().X, this.getPosition().Y-this.MovementSpeed()));
					
				this.SetRow(4);
			}
			else if(KeyHandingController.Movement[1]) //left
			{
				if(this.getPosition().X+this.MovementSpeed() < -this.getEntityWidth() && Global.WORLD_CAMERA.Position.X <=0)
					this.setPosition(new Vector2(this.getPosition().X+this.MovementSpeed(), this.getPosition().Y));
				else
					this.setPosition(new Vector2(this.getPosition().X-this.MovementSpeed(), this.getPosition().Y));
					
				this.SetRow(7);
			}
			else if(KeyHandingController.Movement[2]) //down
			{
				if(this.getPosition().Y + this.MovementSpeed()+(-Global.WORLD_CAMERA.Position.Y) > 
				(world.currentLevel(this.mapID).getTileMap(this.mapID).getYTiles()*Global.SCALE*Global.TILE_SIZE)-(this.getEntityHeight()*2) &&
				Global.WORLD_CAMERA.Position.Y < 0)
					this.setPosition(new Vector2(this.getPosition().X, this.getPosition().Y-this.MovementSpeed()));
				else 
					this.setPosition(new Vector2(this.getPosition().X, this.getPosition().Y+this.MovementSpeed()));
					
				this.SetRow(6);
			}
			else if(KeyHandingController.Movement[3]) //right
			{
				if(this.getPosition().X + this.MovementSpeed()+(-Global.WORLD_CAMERA.Position.X) >
				(world.currentLevel(this.mapID).getTileMap(this.mapID).getXTiles()*Global.SCALE*Global.TILE_SIZE)-(this.getEntityWidth()*2) &&
				Global.WORLD_CAMERA.Position.X < 0)
					this.setPosition(new Vector2(this.getPosition().X-this.MovementSpeed(), this.getPosition().Y));
				else 
					this.setPosition(new Vector2(this.getPosition().X+this.MovementSpeed(), this.getPosition().Y));
					
				this.SetRow(5);
			}
			else {
			}
		}
		else
		{
			this.IsMoving = false;
		}
	}
	
	
	private void cameraMovement() {
		
		if(this.getPosition().X + (this.getEntityWidth()) > Global.RENDER_X-(this.getEntityWidth()))
		{
			this.setPosition(new Vector2 (this.getPosition().X-this.MovementSpeed(), this.getPosition().Y));
			Global.WORLD_CAMERA.Position.X -= this.MovementSpeed();
		}
		if(this.getPosition().X < 0 && Global.WORLD_CAMERA.Position.X < 0)
		{
			this.setPosition(new Vector2(0,this.getPosition().Y));
			Global.WORLD_CAMERA.Position.X += this.MovementSpeed();
		}
		if(this.getPosition().Y +(this.getEntityHeight()) > Global.RENDER_Y-this.getEntityHeight()) {
			this.setPosition(new Vector2(this.getPosition().X, this.getPosition().Y-this.MovementSpeed()));
			Global.WORLD_CAMERA.Position.Y -= this.MovementSpeed();
		}
		if(this.getPosition().Y < 0 && Global.WORLD_CAMERA.Position.Y < 0)
		{
			this.setPosition(new Vector2 (this.getPosition().X,0));
			Global.WORLD_CAMERA.Position.Y += this.MovementSpeed();
		}
	}
	@Override
	public void Update(World world) {
		
		
		movement(world);

		cameraMovement();
		
		super.Update(world);
	}
	
	@Override
	public void DrawEntity(javafx.scene.canvas.GraphicsContext gc)
	{
		super.DrawEntity(gc);
	}
}
