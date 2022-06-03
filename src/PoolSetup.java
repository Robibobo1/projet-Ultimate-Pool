import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;

import java.awt.Point;
import java.util.Vector;

import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsStaticBox;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsStaticLine;
import ch.hevs.gdx2d.lib.physics.AbstractPhysicsObject;
import ch.hevs.gdx2d.lib.utils.Logger;

public class PoolSetup {
	App a;

	Vector<int[]> collisionList = new Vector<int[]>();
	int[] lastCollision = null;

	int height = 600;
	int width = height * 2;

	int ballDensity = 10;
	double ballRadius = ((double) height * 0.05089) / 2;

	double sidePocket = height * 0.11875;
	double cornerPocketRad = Math.sqrt(Math.pow(height * 0.10446, 2) / 2);

	double sideDepth = height * 0.04553;

	int ballNumber = 0;

	PhysicsCircle[] ballArray = new PhysicsCircle[16];

	Point midP = new Point();

	PhysicsStaticBox boiteF;
	PhysicsStaticBox box1;

	PoolSetup(App a) {
		this.a = a;

		midP.x = a.getWindowWidth() / 2;
		midP.y = a.getWindowHeight() / 2;

	}

	void createPool() {
		
		buildWall();
		
		ballArray[0] = new PhysicsCircle("0", new Vector2((int) (midP.x - (0.25 * width)), (int) (midP.y)),
				(float) ballRadius, 10, 0.7f, 0) {
			public void collision(AbstractPhysicsObject other, float energy) {
				int[] collision = new int[2];
				try {
					//Logger.log(name + " collided " + other.name + " with energy " + energy);
					collision[0] = Integer.parseInt(name);
					collision[1] = Integer.parseInt(other.name);
					lastCollision = collision;
					collisionList.add(collision);
				} catch (Exception e) {

				}
			}
		};

		boiteF = new PhysicsStaticBox(null, new Vector2(midP.x, midP.y), 10, 10);

		// Collision box
		new Hole("21", new Vector2(midP.x, (float) (midP.y + (height / 2) + 0.75 * sidePocket)), (float) sidePocket,
				(float) sidePocket,this);
		new Hole("24", new Vector2(midP.x, (float) (midP.y - (height / 2) - 0.75 * sidePocket)), (float) sidePocket,
				(float) sidePocket,this);

		new Hole("20",
				new Vector2((float) (midP.x - width / 2 - 0.75 * sideDepth),
						(float) (midP.y - (height / 2) - 0.75 * sideDepth)),
				(float) sidePocket, (float) sidePocket, (float) (Math.PI / 4),this);
		new Hole("22",
				new Vector2((float) (midP.x - width / 2 - 0.75 * sideDepth),
						(float) (midP.y + (height / 2) + 0.75 * sideDepth)),
				(float) sidePocket, (float) sidePocket, (float) (Math.PI / 4),this);

		new Hole("23",
				new Vector2((float) (midP.x + width / 2 + 0.75 * sideDepth),
						(float) (midP.y - (height / 2) - 0.75 * sideDepth)),
				(float) sidePocket, (float) sidePocket, (float) (Math.PI / 4),this);

		new Hole("25",
				new Vector2((float) (midP.x + width / 2 + 0.75 * sideDepth),
						(float) (midP.y + (height / 2) + 0.75 * sideDepth)),
				(float) sidePocket, (float) sidePocket, (float) (Math.PI / 4),this);

		FrictionJointDef frictionJointDef = new FrictionJointDef();
		frictionJointDef.maxForce = 0.2f;
		frictionJointDef.maxTorque = 0.1f;
		frictionJointDef.bodyA = ballArray[0].getBody();
		frictionJointDef.bodyB = boiteF.getBody();
		frictionJointDef.collideConnected = false;

		a.world.createJoint(frictionJointDef);
		placeTriangle(new Point((int) (midP.x + (0.25 * width)), (int) (midP.y - 4 * ballRadius)));

	}


	void destroyBalls() {
		for(PhysicsCircle b : ballArray)
		{
			b.destroy();
		}

	}

	void placeTriangle(Point position) {
		ballPlacer(5, position);
		collisionList.clear();
	}

	void ballPlacer(int rowLeft, Point position) {
		for (int i = 0; i < rowLeft; i++) {

			ballNumber++;
			ballArray[ballNumber] = new PhysicsCircle("" + ballNumber, // Name
					new Vector2((float) (position.x + 1 + (ballRadius * 2 * rowLeft)), // Position x
							(float) (position.y + 1 + ballRadius * (5 - rowLeft) + ballRadius * 2 * i)), // Position y
					(float) ballRadius, ballDensity, 0.7f, 0); // Radius, density, restitution and friction
			FrictionJointDef frictionJointDef = new FrictionJointDef();
			frictionJointDef.maxForce = 0.2f;
			frictionJointDef.maxTorque = 0.1f;
			frictionJointDef.bodyA = ballArray[ballNumber].getBody();
			frictionJointDef.bodyB = boiteF.getBody();
			frictionJointDef.collideConnected = false;
			a.world.createJoint(frictionJointDef);

		}
		if (rowLeft > 1) {
			ballPlacer(rowLeft - 1, position);
		}
	}

	String debugCollisionList() {
		String out = "";
		for (int[] a : collisionList) {
			out += a[0];
			out += " with ";
			out += a[1];
			out += "  -  ";
		}
		return out;
	}
	
	void buildWall() {
		// le coté gauche du billard, a partir du trou du haut
		new PhysicsStaticLine("Gauche",
				new Vector2(midP.x - (width / 2), (float) (midP.y + height / 2 - cornerPocketRad)),
				new Vector2(midP.x - (width / 2), (float) (midP.y - height / 2 + cornerPocketRad)));
		// On commence par le haut du billard, a partir du trou sur le coté
		new PhysicsStaticLine("Haut droite", new Vector2((float) (midP.x + (sidePocket / 2)), midP.y + height / 2),
				new Vector2((float) (midP.x + width / 2 - cornerPocketRad), midP.y + height / 2));
		new PhysicsStaticLine("Haut gauche", new Vector2((float) (midP.x - (sidePocket / 2)), midP.y + height / 2),
				new Vector2((float) (midP.x - width / 2 + cornerPocketRad), midP.y + height / 2));

		// le bas du billard, a partir du trou sur le coté
		new PhysicsStaticLine("Bas droite", new Vector2((float) (midP.x + (sidePocket / 2)), midP.y - height / 2),
				new Vector2((float) (midP.x + width / 2 - cornerPocketRad), midP.y - height / 2));
		new PhysicsStaticLine("Bas gauche", new Vector2((float) (midP.x - (sidePocket / 2)), midP.y - height / 2),
				new Vector2((float) (midP.x - width / 2 + cornerPocketRad), midP.y - height / 2));

		// le coté droit du billard, a partir du trou du haut
		new PhysicsStaticLine("Droite",
				new Vector2(midP.x + (width / 2), (float) (midP.y + height / 2 - cornerPocketRad)),
				new Vector2(midP.x + (width / 2), (float) (midP.y - height / 2 + cornerPocketRad)));

		// le coté droit du billard, a partir du trou du haut
		new PhysicsStaticLine("Gauche",
				new Vector2(midP.x - (width / 2), (float) (midP.y + height / 2 - cornerPocketRad)),
				new Vector2(midP.x - (width / 2), (float) (midP.y - height / 2 + cornerPocketRad)));

		// Side pocket up
		new PhysicsStaticLine("SP", new Vector2((float) (midP.x + (sidePocket / 2)), midP.y + height / 2),
				new Vector2((float) (midP.x + (sidePocket * 0.4)), (float) (midP.y + height / 2 + sideDepth)));
		new PhysicsStaticLine("SP", new Vector2((float) (midP.x - (sidePocket / 2)), midP.y + height / 2),
				new Vector2((float) (midP.x - (sidePocket * 0.4)), (float) (midP.y + height / 2 + sideDepth)));

		// Side pocket down
		new PhysicsStaticLine("SP", new Vector2((float) (midP.x + (sidePocket / 2)), midP.y - height / 2),
				new Vector2((float) (midP.x + (sidePocket * 0.4)), (float) (midP.y - height / 2 - sideDepth)));
		new PhysicsStaticLine("SP", new Vector2((float) (midP.x - (sidePocket / 2)), midP.y - height / 2),
				new Vector2((float) (midP.x - (sidePocket * 0.4)), (float) (midP.y - height / 2 - sideDepth)));

		// corner pockets
		new PhysicsStaticLine("CP", new Vector2((float) (midP.x + width / 2 - cornerPocketRad), midP.y + height / 2),
				new Vector2((float) (midP.x + width / 2 - cornerPocketRad + sideDepth),
						(float) (midP.y + height / 2 + sideDepth)));
		new PhysicsStaticLine("CP", new Vector2(midP.x + (width / 2), (float) (midP.y + height / 2 - cornerPocketRad)),
				new Vector2((float) (midP.x + (width / 2) + sideDepth),
						(float) (midP.y + height / 2 - cornerPocketRad + sideDepth)));

		new PhysicsStaticLine("CP", new Vector2(midP.x - (width / 2), (float) (midP.y - height / 2 + cornerPocketRad)),
				new Vector2((float) (midP.x - (width / 2) - sideDepth),
						(float) (midP.y - height / 2 + cornerPocketRad - sideDepth)));
		new PhysicsStaticLine("CP", new Vector2((float) (midP.x - width / 2 + cornerPocketRad), midP.y - height / 2),
				new Vector2((float) (midP.x - width / 2 + cornerPocketRad - sideDepth),
						(float) (midP.y - height / 2 - sideDepth)));

		new PhysicsStaticLine("CP", new Vector2(midP.x + (width / 2), (float) (midP.y - height / 2 + cornerPocketRad)),
				new Vector2((float) (midP.x + (width / 2) + sideDepth),
						(float) (midP.y - height / 2 + cornerPocketRad - sideDepth)));
		new PhysicsStaticLine("CP", new Vector2((float) (midP.x + width / 2 - cornerPocketRad), midP.y - height / 2),
				new Vector2((float) (midP.x + width / 2 - cornerPocketRad + sideDepth),
						(float) (midP.y - height / 2 - sideDepth)));

		new PhysicsStaticLine("CP", new Vector2((float) (midP.x - width / 2 + cornerPocketRad), midP.y + height / 2),
				new Vector2((float) (midP.x - width / 2 + cornerPocketRad - sideDepth),
						(float) (midP.y + height / 2 + sideDepth)));
		new PhysicsStaticLine("CP", new Vector2(midP.x - (width / 2), (float) (midP.y + height / 2 - cornerPocketRad)),
				new Vector2((float) (midP.x - (width / 2) - sideDepth),
						(float) (midP.y + height / 2 - cornerPocketRad + sideDepth)));
	}


}
