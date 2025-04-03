package Controls;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyHandingController{

	public static boolean[] Movement = {false,false,false,false};	//Character Movement
	
	/**
	 * if a Key is typed
	 * @param e
	 */
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * If a key is pressed
	 * @param e
	 */
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getCode() == KeyCode.UP)
			Movement[0] = true;
		if(e.getCode() == KeyCode.LEFT)
			Movement[1] = true;
		if(e.getCode() == KeyCode.DOWN)
			Movement[2] = true;
		if(e.getCode() == KeyCode.RIGHT)
			Movement[3] = true;
	}

	/**
	 * if a key is released
	 * @param e
	 */
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getCode() == KeyCode.UP)
			Movement[0] = false;
		if(e.getCode() == KeyCode.LEFT)
			Movement[1] = false;
		if(e.getCode() == KeyCode.DOWN)
			Movement[2] = false;
		if(e.getCode() == KeyCode.RIGHT)
			Movement[3] = false;
	}
	
}
