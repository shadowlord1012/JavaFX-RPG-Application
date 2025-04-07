module SliverMoon {
	requires javafx.controls;
	requires java.desktop;
	requires javafx.swing;
	requires javafx.graphics;
	requires java.sql;
	requires javafx.fxml;
	
	opens application to javafx.graphics, javafx.fxml;
	opens TileMapGenerator to javafx.graphics, javafx.fxml;
}
