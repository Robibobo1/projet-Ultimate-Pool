import java.awt.Point;
import com.badlogic.gdx.math.Vector2;
import ch.hevs.gdx2d.components.physics.utils.PhysicsConstants;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle;

//------------------------------------------------------------------
// CollisionDetection
//------------------------------------------------------------------
// Classe contenant les méthode statique pouvant détecter la collison
// entre un objet physicsCircle et un objet cane.
//------------------------------------------------------------------
public class CollisionDetection {

	// ------------------------------------------------------------------
	// pointInPixel
	// ------------------------------------------------------------------
	// Méthode statique retournant le point de contact sur la balle 
	// L'unité est en pixel
	// ------------------------------------------------------------------
	static Vector2 pointInPixel(PhysicsCircle ball, Cane c) {
		Point collisionPoint = getPoint(ball, c);
		if (collisionPoint == null)
			return null;
		else {
			return new Vector2(collisionPoint.x, collisionPoint.y);
		}
	}
	
	// ------------------------------------------------------------------
	// pointInMeter
	// ------------------------------------------------------------------
	// Méthode statique retournant le point de contact sur la balle
	// L'unité est en mètre
	// ------------------------------------------------------------------
	static Vector2 pointInMeter(PhysicsCircle ball, Cane c) {
		Point collisionPoint = getPoint(ball, c);
		if (getPoint(ball, c) == null)
			return null;
		else {
			float cst = PhysicsConstants.PIXEL_TO_METERS;
			return new Vector2((collisionPoint.x * cst), (collisionPoint.y * cst));
		}
	}
	
	// ------------------------------------------------------------------
	// getPoint
	// ------------------------------------------------------------------
	// Retourne null si il n'y a pas de contact, sinon retourne le point
	// de contact entre la balle blanche et la canne
	// ------------------------------------------------------------------
	static Point getPoint(PhysicsCircle ball, Cane c) {
		float radius = ball.getBodyRadius();
		Point middlePoint = new Point((int) ball.getBodyPosition().x, (int) ball.getBodyPosition().y);
		for (int i = 0; i < c.hitPoints.length; i++) {
			if (c.hitPoints[i].distance(middlePoint) <= radius) {
				return c.hitPoints[i];
			}
		}
		return null;
	}

	// ------------------------------------------------------------------
	// hasCollision
	// ------------------------------------------------------------------
	// Retourne true si il y a eu une collison, sinon false
	// ------------------------------------------------------------------
	static boolean hasCollision(PhysicsCircle ball, Cane c) {
		if (getPoint(ball, c) == null)
			return false;
		else
			return true;
	}

}
