import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import ch.hevs.gdx2d.lib.GdxGraphics;

public class Cane {
	Vector2 position;
	float angle;
	int lenght = 120;
	int width = 10;
	
	Cane(Vector2 position,float angle)
	{
		this.position = position;
		this.angle = angle;
	}
	
	void setPosition(Vector2 position)
	{
		this.position = position;
	}
	
	void setAngle(float angle)
	{
		this.angle = angle;
	}
	
	void drawCane(GdxGraphics g)
	{
		//g.drawFilledRectangle((float)( position.x - lenght/2 * Math.sin(angle)),(float)( position.y - (lenght/2 * Math.cos(angle)) + width/2),(float) width, 8f, angle, Color.RED);
		g.drawRectangle(position.x, position.y, width, lenght, angle);
	}
}
