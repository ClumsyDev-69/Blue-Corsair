package fxml_controllers;

import fxml_files.FXML_reference;
import game_physics.Level;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Start_Scene extends Application{

	/*
	 VM arguments: --module-path "${workspace_loc}\Prerequisites\JavaFX\lib" --add-modules javafx.controls,javafx.fxml 
	 Run on cmd with Blue Corsair.jar: java --module-path "src\prerequisites\JavaFX\lib" --add-modules javafx.controls,javafx.fxml -jar Blue Corsair.jar
	 */
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		FXMLLoader loader = new FXMLLoader(FXML_reference.class.getResource("Start_Scene.fxml"));
		Parent root = loader.load();
		Level.stg.setScene(new Scene(root));
		Level.stg.show();
		Level.stg.setTitle("Game Name");
		Level.stg.setOnCloseRequest(event -> System.exit(0));
		Level.stg.getIcons().add(new Image(FXML_reference.class.getResourceAsStream("Icon.png")));
		arg0 = Level.stg;
	}
	
	public void start() {
		FXMLLoader loader = new FXMLLoader(FXML_reference.class.getResource("Level-" + Level.level + ".fxml"));
		try {
			Level.stg.setScene(new Scene(loader.load()));
			Level.initiateLevel(loader, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
