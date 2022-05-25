import java.awt.Rectangle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.awt.Point;
import ch.hevs.gdx2d.lib.GdxGraphics;

public class Cane {
	Vector2 position;
	float angle;
	Point hitPoint1, hitPoint2;

	
	int lenght = 120;
	int width = 10;
	
	Cane(Vector2 position,float angle)
	{
		this.position = position;
		this.angle = angle;
		hitPoint1 = new Point();
		hitPoint2 = new Point();
		updateHitPoint();
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
		updateHitPoint();
		//System.out.println(angle);
		g.drawRectangle(position.x, position.y, width, lenght, angle);
		g.setPixel(hitPoint1.x, hitPoint1.y, Color.RED);
		g.setPixel(hitPoint2.x, hitPoint2.y, Color.RED);
	}
	
	void updateHitPoint()
	{
		hitPoint1.x = (int) (position.x + Math.sin(Math.toRadians(4.76 - angle)) * lenght/2);
		hitPoint1.y = (int) (position.y +  Math.cos(Math.toRadians(4.76 - angle )) * lenght/2);
		hitPoint2.x = (int) (position.x + Math.sin(Math.toRadians(-4.76 - angle)) * lenght/2);
		hitPoint2.y = (int) (position.y +  Math.cos(Math.toRadians(-4.76 - angle )) * lenght/2);
	}
}