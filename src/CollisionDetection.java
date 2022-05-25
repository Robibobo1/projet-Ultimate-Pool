import java.awt.Point;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import com.badlogic.gdx.graphics.Color;

import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle;
import ch.hevs.gdx2d.lib.GdxGraphics;


public class CollisionDetection {
	
	static boolean test(PhysicsCircle ball,Cane c,GdxGraphics g)
	{
		Line2D contactLine = new Line2D.Double(c.hitPoint1,c.hitPoint2);
		float radius = ball.getBodyRadius();
		for (double i = 0; i < 2 * Math.PI ; i += 0.01) {
			Point circle = new Point();
			circle.x = (int) (ball.getBodyPosition().x + (radius * Math.sin(i)));
			circle.y = (int) (ball.getBodyPosition().y + (radius * Math.cos(i)));
			//System.out.println(bout.ptLineDist(circle));
			if(contactLine.ptLineDist(circle) < 0.5) System.out.println("BOOM");
			g.setPixel(circle.x, circle.y,Color.PINK);
		}
		
		return false;
	}
	
	
}
