package game_physics;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import fxml_controllers.GameCompleted;
import fxml_files.FXML_reference;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public abstract class Level {

	public static Timer t;
	public static int level = 1;
	public static Stage stg = new Stage();
	protected static Rectangle square;
	public final static int g = 8;
	public static double xVelocity, yVelocity, x, y, prevY, prevX;
	public static boolean friction, bouncedX, bouncedY, xMod, yMod, yIsZero;
	public static final int frameInterval = 100;
	public static final int maxLevel = 5;
	protected Collision bounce = new Collision() {
		@Override
		Runnable action(char collisionDir) {
			if (collisionDir == 'u') {
				yMod = true;
				friction = true;
				bouncedY = true;
			} else if (collisionDir == 'd') {
				yIsZero = true;
				yMod = true;
			} else {
				xMod = true;
				bouncedX = true;
			}
			return new Runnable() {
				@Override
				public void run() {
					return;
				}
			};
		}
	};
	protected Collision nextLvl = new Collision() {
		@Override
		Runnable action(char collisionDir) {
			bounce.action(collisionDir);
			return new Runnable() {
				@Override
				public void run() {
					updateValues();
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
					}
					changeLevel(++level);
					t.cancel();
				}
			};
		}
	};
	protected Collision prevLvl = new Collision() {
		@Override
		Runnable action(char collisionDir) {
			bounce.action(collisionDir);
			return new Runnable() {
				@Override
				public void run() {
					updateValues();
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
					}
					changeLevel(--level);
					t.cancel();
				}
			};
		}
	};

	public void physics() {
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				t = new Timer();
				TimerTask tmt = new TimerTask() {
					@Override
					public void run() {
						nextFrame();
					}
				};
				t.scheduleAtFixedRate(tmt, 0, frameInterval);
			}
		});
		t1.run();
	}

	public Double[] allCollision(Rectangle square, LinkedHashMap<Line, Collision> lineCol,
			LinkedHashMap<Rectangle, Collision> rectCol) {
		Double buffer = 0.0;
		Collision action;
		Line[] lines = new Line[lineCol.size()];
		Rectangle[] rects = new Rectangle[rectCol.size()];
		lines = lineCol.keySet().toArray(lines);
		rects = rectCol.keySet().toArray(rects);
		HashMap<Double, Entry<Collision, Character>> xVal = new HashMap<>();
		HashMap<Double, Entry<Collision, Character>> yVal = new HashMap<>();
		if (lines != null && lines.length > 0) {
			for (Line l : lines) {
				action = lineCol.get(l);
				buffer = action.lineHorizhontalCollision(l, square);
				if (buffer != null)
					xVal.put(buffer, Map.entry(action, action.colDir));
				buffer = action.lineVerticalCollision(l, square);
				if (buffer != null)
					yVal.put(buffer, Map.entry(action, action.colDir));
			}
		}
		if (rects != null && rects.length > 0) {
			for (Rectangle rect : rects) {
				action = rectCol.get(rect);
				buffer = action.rectangleHorizhontalCollision(rect, square);
				if (buffer != null)
					xVal.put(buffer, Map.entry(action, action.colDir));
				buffer = action.rectangleVerticalCollision(rect, square);
				if (buffer != null)
					yVal.put(buffer, Map.entry(action, action.colDir));
			}
		}
		if (xVal.size() > 0) {
			if (xVelocity > 0) {
				x = Collections.min(xVal.keySet());
			} else if (xVelocity < 0) {
				x = Collections.max(xVal.keySet());
			}
		}
		if (yVal.size() > 0) {
			if (yVelocity > 0) {
				y = Collections.min(yVal.keySet());
			} else if (yVelocity < 0) {
				y = Collections.max(yVal.keySet());
			}
		}
		Runnable actionX = new Runnable() {
			@Override
			public void run() {
				return;
			}
		};
		Runnable actionY = actionX;
		for (Entry<Double, Entry<Collision, Character>> col : xVal.entrySet()) {
			if (x == col.getKey()) {
				actionX = col.getValue().getKey().action(col.getValue().getValue());
			}
		}
		for (Entry<Double, Entry<Collision, Character>> col : yVal.entrySet()) {
			if (y == col.getKey()) {
				actionY = col.getValue().getKey().action(col.getValue().getValue());
			}
		}
		actionX.run();
		actionY.run();
		return new Double[] { x, y };
	}

	public static void changeLevel(int level) {
		System.out.println("Level = " + level);
		if (level > maxLevel) {
			GameCompleted.gameCompleted();
			return;
		}
		Runnable levelScene = new Runnable() {
			@Override
			public void run() {
				FXMLLoader loader = null;
				loader = new FXMLLoader(FXML_reference.class.getResource("Level-" + level + ".fxml"));
				try {
					stg.setScene(new Scene(loader.load()));
					stg.getIcons().add(new Image(FXML_reference.class.getResourceAsStream("Icon.png")));
				} catch (IOException e) {
					e.printStackTrace();
				}
				initiateLevel(loader, level);
			}
		};
		Platform.runLater(levelScene);
	}

	public static void initiateLevel(FXMLLoader loader, int level) {
		Level ctrl = loader.getController();
		stg.getScene().setOnKeyPressed(event -> keyPressed(event));
		stg.setTitle("Level: " + level);
		ctrl.physics();
		resetValues();
	}

	public static void updateValues() {
		if (yIsZero)
			yVelocity = 0;
		if (friction)
			xVelocity /= 1.2;
		else
			xVelocity /= 1.05;
		if (bouncedY)
			yVelocity = -yVelocity / 1.3;
		if (bouncedX) {
			xVelocity *= -0.75;
		}
		if (!yMod)
			y += yVelocity;
		if (!xMod)
			x += xVelocity;
		square.setX(x);
		square.setY(y);
		bouncedY = false;
		bouncedX = false;
		yMod = false;
		xMod = false;
		friction = false;
		yIsZero = false;
		yVelocity += g;
	}

	public static void resetValues() {
		x = 0;
		y = 0;
		xVelocity = 0;
		yVelocity = 0;
	}

	public static void keyPressed(KeyEvent e) {
		if (e.getCode().toString().equals("UP"))
			up();
		if (e.getCode().toString().equals("RIGHT"))
			right();
		if (e.getCode().toString().equals("LEFT"))
			left();
	}

	public static void up() {
		yVelocity = -30;
	}

	public static void right() {
		if (xVelocity < 50)
			xVelocity += 20;
	}

	public static void left() {
		if (xVelocity > -50)
			xVelocity += -20;
	}

	public abstract void nextFrame();

}
