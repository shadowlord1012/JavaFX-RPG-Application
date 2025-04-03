package Settings;

import Engine.Camera;

/**
 * Global Setting values for the entire game
 */
public class Global {
	public static int TILE_SIZE = 16;	//Default tile size
	public static int SCALE = 3;	//Scale for graphic rendering
	public static int SCREEN_COL = 16;	//default Number of tiles per x axis
	public static int SCREEN_ROW = 12;	//default Number of tiles per y axis
	public static final int RENDER_X = (TILE_SIZE*SCALE)*SCREEN_COL; //defaults Size of the screen
	public static final int RENDER_Y = (TILE_SIZE*SCALE)*SCREEN_ROW; //default size of the screen
	public static final int DEBUGGING_X_OFFSET = 20; //used in the debugging menu only
	public static Camera WORLD_CAMERA = new Camera();
	public static float MASTER_VOLUME = 0;	//Master volume control
	public static float SOUND_EFFECT_VOLUME = 0;	//Sound effect control
}
