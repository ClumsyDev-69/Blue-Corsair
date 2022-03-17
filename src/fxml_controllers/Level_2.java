package fxml_controllers;

import java.util.LinkedHashMap;

import game_physics.Collision;
import game_physics.Level;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Level_2 extends Level{

	@FXML
	Line l1, l2, l3, l4, l5, l6, l7, l8, lava;
	@FXML
	Rectangle square, target;
	@FXML
	AnchorPane pane;
	
	@Override
	public void nextFrame() {
		LinkedHashMap<Line, Collision> lines = new LinkedHashMap<>() {
			private static final long serialVersionUID = 1L;
		{
			put(l1, bounce);
			put(l2, bounce);
			put(l3, bounce);
			put(l4, bounce);
			put(l5, bounce);
			put(l6, bounce);
			put(l7, bounce);
			put(l8, bounce);
			put(lava, prevLvl);
		}};
		LinkedHashMap<Rectangle, Collision> rects = new LinkedHashMap<>() {
			private static final long serialVersionUID = 1L;
		{
			put(target, nextLvl);
		}};
		Level.square = square;
		prevX = x;
		prevY = y;
		bounce.frameSideCollision(pane, square);
		allCollision(square, lines, rects);
		updateValues();
	}
}
