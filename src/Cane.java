
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import java.awt.Point;
import ch.hevs.gdx2d.lib.GdxGraphics;

//------------------------------------------------------------------
// Cane
//------------------------------------------------------------------
// Clase définissant tous les points important de la canne de billard.
// Ici sont calculés les hitPoints servant à détecter la collision 
// avec la balle blanche.
//------------------------------------------------------------------
public class Cane {

	Vector2 oldPosition, position;
	Vector2 velocity;

	float angle;
	Point[] hitPoints = new Point[24];
	double magicAngle;
	int lenght = 450;
	int width = 10;

	final float deltaTime = 1f / 60f;

	// ------------------------------------------------------------------
	// Cane
	// ------------------------------------------------------------------
	// Constructeur de la classe Cane
	// Reçoit la positio et l'angle en argument
	// ------------------------------------------------------------------
	Cane(Vector2 position, float angle) {
		this.oldPosition = position;
		this.position = position;

		this.velocity = new Vector2(0.1f, 0.1f);

		this.angle = angle;
		for (int i = 0; i < hitPoints.length; i++) {
			hitPoints[i] = new Point();
		}
		magicAngle = Math.toDegrees(Math.atan(((double) width) / ((double) lenght)));
		updateHitPoint();
	}

	// ------------------------------------------------------------------
	// setPosition
	// ------------------------------------------------------------------
	// Définit la nouvelle position de la canne
	// Dérive la position pour obtenir la vitesse par la même occasion
	// ------------------------------------------------------------------
	void setPosition(Vector2 position) {
		oldPosition = this.position;
		this.position = position;
		
		Vector2 deltaPos = new Vector2(position.x - oldPosition.x, position.y - oldPosition.y);
		this.velocity.x = deltaPos.x / deltaTime;
		this.velocity.y = deltaPos.y / deltaTime;
	}
	
	// ------------------------------------------------------------------
	// setAngle
	// ------------------------------------------------------------------
	// Définit le nouvel angle de la canne
	// ------------------------------------------------------------------
	void setAngle(float angle) {
		this.angle = angle;
	}
	
	// ------------------------------------------------------------------
	// drawCaneDebug
	// ------------------------------------------------------------------
	// Dessine la canne de manière simpliste avec ses hitPoints pour debug
	// ------------------------------------------------------------------
	void drawCaneDebug(GdxGraphics g) {
		updateHitPoint();
		g.drawRectangle(position.x, position.y, width, lenght, angle);
		for (int i = 0; i < hitPoints.length; i++) {
			g.setPixel(hitPoints[i].x, hitPoints[i].y, Color.RED);
		}
	}

	// ------------------------------------------------------------------
	// updateHitPoint
	// ------------------------------------------------------------------
	// Met a jour la position de tous les hitPoint de la canne
	// ------------------------------------------------------------------
	void updateHitPoint() {
		int distBetween = 12;
		for (int i = 0; i < hitPoints.length / 3; i++) {
			hitPoints[0 + 3 * i].x = (int) (position.x
					+ Math.sin(Math.toRadians(magicAngle - angle)) * (lenght / 2 - i * distBetween));
			hitPoints[0 + 3 * i].y = (int) (position.y
					+ Math.cos(Math.toRadians(magicAngle - angle)) * (lenght / 2 - i * distBetween));
			hitPoints[1 + 3 * i].x = (int) (position.x
					+ Math.sin(Math.toRadians(-angle)) * (lenght / 2 - i * distBetween));
			hitPoints[1 + 3 * i].y = (int) (position.y
					+ Math.cos(Math.toRadians(-angle)) * (lenght / 2 - i * distBetween));
			hitPoints[2 + 3 * i].x = (int) (position.x
					+ Math.sin(Math.toRadians(-magicAngle - angle)) * (lenght / 2 - i * distBetween));
			hitPoints[2 + 3 * i].y = (int) (position.y
					+ Math.cos(Math.toRadians(-magicAngle - angle)) * (lenght / 2 - i * distBetween));
		}

	}

	// ------------------------------------------------------------------
	// getVelocity
	// ------------------------------------------------------------------
	// Retourne la vitesse de la canne
	// ------------------------------------------------------------------
	Vector2 getVelocity() {
		return velocity;
	}

	// String de debeug
	String debug() {
		String out = "";
		out += "oldPosition: " + oldPosition + "  Position: " + position + "  velocity: " + velocity;
		return out;
	}
}