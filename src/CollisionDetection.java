import java.awt.Point;
import com.badlogic.gdx.math.Vector2;
import ch.hevs.gdx2d.components.physics.utils.PhysicsConstants;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle;


public class CollisionDetection {
	
	static Vector2 pointInPixel(PhysicsCircle ball,Cane c)
	{
		Point collisionPoint = getPoint(ball,c);
		if(collisionPoint == null) return null;
		else 
		{
			return new Vector2(collisionPoint.x,collisionPoint.y);
		}
	}
	
	static Vector2 pointInMeter(PhysicsCircle ball,Cane c)
	{
		Point collisionPoint = getPoint(ball,c);
		if(getPoint(ball,c) == null) return null;
		else 
		{
			float cst = PhysicsConstants.PIXEL_TO_METERS;
			return new Vector2((collisionPoint.x * cst),(collisionPoint.y * cst));
		}
	}
	
	static Point getPoint(PhysicsCircle ball,Cane c)
	{
		float radius = ball.getBodyRadius();		
		Point middlePoint = new Point((int) ball.getBodyPosition().x,(int) ball.getBodyPosition().y);
		for (int i = 0; i < c.hitPoints.length; i++) {
			if(c.hitPoints[i].distance(middlePoint) <= radius) 
			{
				//System.out.println("Collision");
				return c.hitPoints[i];
			}
		}
		return null;
	}
	
	static boolean hasCollision(PhysicsCircle ball,Cane c)
	{
		if(getPoint(ball,c) == null) return false;
		else return true;
	}
	
}
