import java.awt.geom.Point2D;

import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle;
import ch.hevs.gdx2d.lib.GdxGraphics;

public class CollisionDetection {
	
	static boolean test(PhysicsCircle ball,Cane c,GdxGraphics g)
	{
		float radius = ball.getBodyRadius();
		for (double i = 0; i < 360; i++) {
			Point2D pointOnCircle = new Point2D.Double();
			pointOnCircle.setLocation(Math.sin(Math.toRadians(i)) * radius + 200, Math.cos(Math.toRadians(i)) * radius + 200);
			g.setPixel((float) pointOnCircle.getX(), (float) pointOnCircle.getY());
			g.drawRectangle(c.hitBox.x, c.hitBox.y, c.hitBox.width, c.hitBox.height, 0);
			System.out.println(c.hitBox);
			if(c.hitBox.contains(pointOnCircle)) return true;
		}
		return false;
	}
	
	
}
