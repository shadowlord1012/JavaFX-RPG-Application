module SliverMoon {
	requires javafx.controls;
	requires java.desktop;
	requires javafx.swing;
	requires javafx.graphics;
	requires java.sql;
	
	opens application to javafx.graphics, javafx.fxml;
}
