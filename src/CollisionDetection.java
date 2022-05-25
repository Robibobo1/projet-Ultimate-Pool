import java.awt.geom.Point2D;

import java.awt.geom.Ellipse2D;

import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle;
import ch.hevs.gdx2d.lib.GdxGraphics;


public class CollisionDetection {
	
	static boolean test(PhysicsCircle ball,Cane c,GdxGraphics g)
	{
		float radius = ball.getBodyRadius();
		Ellipse2D ballPerimeter = new Ellipse2D.Float(ball.getBodyPosition().x,ball.getBodyPosition().y,radius,radius);
		if(ballPerimeter.contains(c.hitPoint1) == true || ballPerimeter.contains(c.hitPoint2) == true)
		{
			return true;
		}
		return false;
	}
	
	
}
