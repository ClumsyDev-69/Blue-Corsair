package fxml_controllers;

import java.io.IOException;

import fxml_files.FXML_reference;
import game_physics.Level;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

public class GameCompleted {

	@FXML
	Button restart_game;
	
	public static void gameCompleted() {
		Runnable changeScene = new Runnable() {
			@Override
			public void run() {
				FXMLLoader loader = null;
				loader = new FXMLLoader(FXML_reference.class.getResource("Game Completed.fxml"));
				try {
					Level.stg.setScene(new Scene(loader.load()));
					Level.stg.setTitle("Game Completed! Congrats!");
					Level.stg.getIcons().add(new Image(FXML_reference.class.getResourceAsStream("Icon.png")));
				} catch (IOException e) {
				}
			}
		};
		Platform.runLater(changeScene);
		System.out.println("Game Completed! Congrats!");
	}
	
	public void restart_game() {
		Level.changeLevel(1);
		Level.level = 1;
	}
	
}
