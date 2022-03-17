package fxml_controllers;

import java.util.LinkedHashMap;

import game_physics.Collision;
import game_physics.Level;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Level_1 extends Level {

	@FXML
	public Line ground, l1, l2;
	@FXML
	public Rectangle square, target;
	@FXML
	public AnchorPane pane;

	@SuppressWarnings("serial")
	@Override
	public void nextFrame() {
		LinkedHashMap<Line, Collision> bounceLines = new LinkedHashMap<>() {{
				put(l1, bounce);
				put(l2, bounce);
				put(ground, bounce);
			}};
		Level.square = square;
		prevY = y;
		prevX = x;
		bounce.frameSideCollision(pane, square);
		allCollision(square, bounceLines, new LinkedHashMap<Rectangle, Collision>(){{put(target, nextLvl);}});
		updateValues();
	}

}
