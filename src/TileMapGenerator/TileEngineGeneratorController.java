package TileMapGenerator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;

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
    private ScrollPane finishMap;

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
    
    //Variables
    private String tileSheetFilePath;
    private String tileName;
    private int xTile;
    private int yTile;
    private int[] currentTileSelected;
    private int[] sizeOfTile;
    private boolean isDamaging;
    private boolean isActive;
    private boolean isPassible;
    private int numberOfImagesInLine;
    private String mapName;
    private int mapWidth;
    private int mapHeight;
    private String outputInformationString;
    
    
    
    @FXML
    void OpenExsitingMap(ActionEvent event) {

    }

    @FXML
    void OpenTileSheet(ActionEvent event) {

    }

    @FXML
    void numberOfImagesOnChange(InputMethodEvent event) {

    }

    @FXML
    void openNewMapFile(ActionEvent event) {

    }

    @FXML
    void quitOnAction(ActionEvent event) {

    }

    @FXML
    void saveAsMapOnAction(ActionEvent event) {

    }

    @FXML
    void saveCurrentMapOnAction(ActionEvent event) {

    }

    @FXML
    void sizeOFXTileOnChange(InputMethodEvent event) {

    }

    @FXML
    void sizeOFYTileOnChange(InputMethodEvent event) {

    }

    @FXML
    void tileMapHeightOnChange(InputMethodEvent event) {

    }

    @FXML
    void tileMapNameOnChange(InputMethodEvent event) {

    }

    @FXML
    void tileMapWidthOnChange(InputMethodEvent event) {

    }

    @FXML
    void tileSelectOnClick(MouseEvent event) {

    }

}
