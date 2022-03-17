package game_physics;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public abstract class Collision {

	char colDir;
	Line collided_line;
	Rectangle collided_rect;
	
	public Double lineVerticalCollision(Line line, Rectangle square) {
		double y = Level.y;
		double yVelocity = Level.yVelocity;
		double expectedY = y + yVelocity;
		boolean inXBounds = Level.x + square.getLayoutX() < line.getLayoutX() + line.getEndX()
				&& Level.x + square.getLayoutX() + square.getWidth() > line.getLayoutX() + line.getStartX();
		double liney = line.getLayoutY() - square.getLayoutY() - square.getHeight() - (line.getStrokeWidth() / 2);

		if (inXBounds) {
			if (expectedY >= liney && y <= liney) {
				collided_line = line;
				colDir = 'u';
				return liney;
			}
			if (expectedY <= liney + square.getHeight() && y >= liney + square.getHeight() - 1) {
				collided_line = line;
				colDir = 'd';
				return liney + square.getHeight() + line.getStrokeWidth() + 1;
			}
		}
		return null;
	}

	public Double lineHorizhontalCollision(Line line, Rectangle square) {
		double x = Level.x;
		double expectedX = x + Level.xVelocity;
		boolean inYBounds = Level.y + square.getLayoutY() < line.getLayoutY() + line.getEndY() - 1
				&& Level.y + square.getLayoutY() + square.getHeight() > line.getLayoutY() + line.getStartY() + 1;
		if (inYBounds) {
			double lineEndX = line.getLayoutX() - square.getLayoutX() + ((line.getEndX() - line.getStartX()) / 2);
			if (lineEndX > expectedX && lineEndX < x) {
				collided_line = line;
				colDir = 'r';
				return lineEndX + 1;
			}
			double lineStartX = line.getLayoutX() - square.getLayoutX() - ((line.getEndX() - line.getStartX()) / 2)
					- square.getWidth();
			if (lineStartX < expectedX && lineStartX > x) {
				collided_line = line;
				colDir = 'l';
				return lineStartX - 1;
			}
		}
		return null;
	}

	public Double rectangleVerticalCollision(Rectangle target, Rectangle square) {
		double expectedY = Level.y + Level.yVelocity;
		double rectY = target.getLayoutY() - square.getLayoutY() - square.getHeight();// + target.getHeight();
		boolean inXBounds = Level.x + square.getLayoutX() < target.getLayoutX() + target.getX() + target.getWidth()
				&& Level.x + square.getLayoutX() + square.getWidth() > target.getLayoutX() + target.getX();
		boolean collisionUp = expectedY >= rectY && Level.y <= rectY;
		boolean collisionDown = expectedY < rectY + target.getHeight() + square.getHeight()
				&& Level.y > rectY + target.getHeight() + square.getHeight();
		if (inXBounds) {
			if (collisionUp) {
				collided_rect = target;
				colDir = 'u';
				return rectY;
			} else if (collisionDown) {
				collided_rect = target;
				colDir = 'd';
				return rectY + target.getHeight() + square.getHeight() + 1;
			}
		}
		return null;
	}

	public Double rectangleHorizhontalCollision(Rectangle target, Rectangle square) {
		double expectedX = Level.x + Level.xVelocity;
		double rectX = target.getLayoutX() - square.getLayoutX() - square.getWidth();
		boolean inXBounds = Level.y < target.getLayoutY() - square.getLayoutY() + target.getY() + target.getHeight()
				- 1 && Level.y > target.getLayoutY() - square.getLayoutY() + target.getY() - square.getHeight() + 1;
		if (inXBounds) {
			if (expectedX > rectX && Level.x <= rectX) {
				collided_rect = target;
				colDir = 'l';
				return rectX;
			} else if (Level.x > rectX + target.getWidth()
					&& expectedX <= rectX + target.getWidth() + square.getWidth()) {
				collided_rect = target;
				colDir = 'r';
				return rectX + target.getWidth() + square.getWidth();
			}

		}
		return null;
	}

	public void frameSideCollision(AnchorPane pane, Rectangle square) {
		double rightX = pane.getWidth() - square.getLayoutX() - square.getWidth();
		double leftX = -square.getLayoutX();
		double expectedX = Level.x + Level.xVelocity;
		if (Level.x < rightX + 1 && expectedX > rightX + 1) {
			Level.x = rightX;
			Level.xMod = true;
			colDir = 'r';
			action(colDir);
		}
		if (Level.x > leftX - 1 && expectedX < leftX - 1) {
			Level.x = leftX;
			Level.xMod = true;
			colDir = 'l';
			action(colDir);
		}
	}

	abstract Runnable action(char collisionDir);

}
