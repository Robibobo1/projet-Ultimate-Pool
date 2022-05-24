import java.awt.Rectangle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import ch.hevs.gdx2d.lib.GdxGraphics;

public class Cane {
	Vector2 position;
	float angle;
	Rectangle hitBox;
	int lenght = 120;
	int width = 10;
	
	Cane(Vector2 position,float angle)
	{
		this.position = position;
		this.angle = angle;
		hitBox = new Rectangle((int) position.x, (int) position.y, width, lenght);
	}
	
	void setPosition(Vector2 position)
	{
		this.position = position;
		hitBox.x = (int) position.x;
		hitBox.y = (int) position.y;
	}
	
	void setAngle(float angle)
	{
		this.angle = angle;
	}
	
	void drawCane(GdxGraphics g)
	{
		g.drawRectangle(position.x, position.y, width, lenght, angle);
	}
}
