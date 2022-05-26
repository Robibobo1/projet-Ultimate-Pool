import java.awt.Rectangle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.awt.Point;
import ch.hevs.gdx2d.lib.GdxGraphics;

public class Cane {

	Vector2 oldPosition, position;
	Vector2 oldVelocity, velocity;
	Vector2 acceleration;

	float angle;
	Point[] hitPoints = new Point[3];
	double magicAngle;
	int lenght = 120;
	int width = 10;

	float deltaTime = 1f / 60f;

	Cane(Vector2 position, float angle) {
		
		this.oldPosition = position;
		this.position = position;
		this.oldVelocity = new Vector2(1, 1);
		this.velocity = new Vector2(2, 2);
		this.acceleration = new Vector2(3, 3);
		
		this.angle = angle;
		for (int i = 0; i < 3; i++) {
			hitPoints[i] = new Point();
		}
		magicAngle = Math.toDegrees(Math.atan(((double) width) / ((double) lenght)));
		updateHitPoint();
	}

	void setPosition(Vector2 position) {
		oldPosition = this.position;
		this.position = position;
		
		oldVelocity = this.velocity;

		Vector2 deltaPos = new Vector2(position.x - oldPosition.x, position.y - oldPosition.y);
		this.velocity.x = deltaPos.x / deltaTime;
		this.velocity.y = deltaPos.y / deltaTime;

		Vector2 deltaVelo = new Vector2(velocity.x - oldVelocity.x, velocity.y - oldVelocity.y);
		acceleration.x = deltaVelo.x / deltaTime;
		acceleration.y = deltaVelo.y / deltaTime;
	}

	void setAngle(float angle) {
		this.angle = angle;
	}

	void drawCane(GdxGraphics g) {
		updateHitPoint();
		g.drawRectangle(position.x, position.y, width, lenght, angle);
		for (int i = 0; i < hitPoints.length; i++) {
			g.setPixel(hitPoints[i].x, hitPoints[i].y, Color.RED);
		}

	}

	void updateHitPoint() {
		hitPoints[0].x = (int) (position.x + Math.sin(Math.toRadians(magicAngle - angle)) * lenght / 2);
		hitPoints[0].y = (int) (position.y + Math.cos(Math.toRadians(magicAngle - angle)) * lenght / 2);
		hitPoints[1].x = (int) (position.x + Math.sin(Math.toRadians(-angle)) * lenght / 2);
		hitPoints[1].y = (int) (position.y + Math.cos(Math.toRadians(-angle)) * lenght / 2);
		hitPoints[2].x = (int) (position.x + Math.sin(Math.toRadians(-magicAngle - angle)) * lenght / 2);
		hitPoints[2].y = (int) (position.y + Math.cos(Math.toRadians(-magicAngle - angle)) * lenght / 2);
	}

	Vector2 getVelocity() {
		return velocity;
	}

	Vector2 getAcceleration() {
		return acceleration;
	}
	
	String debug() {
		String out = "";
		out += "oldPosition: " + oldPosition + "  Position: " + position + "  oldVelocity: " + oldVelocity + "  velocity: " + velocity + "  accel: " + acceleration ; 
		return out;
	}
}