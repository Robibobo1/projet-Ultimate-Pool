import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle;
import ch.hevs.gdx2d.lib.GdxGraphics;


public class CollisionDetection {
	
	static Vector2 test(PhysicsCircle ball,Cane c)
	{
		float radius = ball.getBodyRadius();		
		Ellipse2D circle = new Ellipse2D.Double(ball.getBodyPosition().x - radius,ball.getBodyPosition().y - radius,2*radius,2*radius);
		for (int i = 0; i < c.hitPoints.length; i++) {
			if(circle.contains(c.hitPoints[i])) 
			{
				System.out.println("Collision");
				return new Vector2(0,0);
			}
		}
		return null;
	}
	
}
