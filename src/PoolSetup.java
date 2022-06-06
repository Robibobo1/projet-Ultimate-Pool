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
		buildPocket();
		
		boiteF = new PhysicsStaticBox(null, new Vector2(midP.x, midP.y), 10, 10);
		
		placeWhite( new Vector2((int) (midP.x - (0.25 * width)), (int) (midP.y)));
		
		placeTriangle(new Point((int) (midP.x + (0.25 * width)), (int) (midP.y)));

	}
	
	void placeWhite(Vector2 position)
	{
		ballArray[0] = null;
		ballArray[0] = new PhysicsCircle("0", position,(float) ballRadius, 10, 0.7f, 0) {
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
		
		FrictionJointDef frictionJointDef = new FrictionJointDef();
		frictionJointDef.maxForce = 0.2f;
		frictionJointDef.maxTorque = 0.1f;
		frictionJointDef.bodyA = ballArray[0].getBody();
		frictionJointDef.bodyB = boiteF.getBody();
		frictionJointDef.collideConnected = false;
		a.world.createJoint(frictionJointDef);
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

			
			ballArray[1] = new PhysicsCircle("1" , // Name
					new Vector2((float) (position.x), // Position x
							(float) (position.y)), // Position y
					(float) ballRadius, ballDensity, 0.7f, 0); // Radius, density, restitution and friction
			
			ballArray[2] = new PhysicsCircle("2" , // Name
					new Vector2((float) (position.x + 2*ballRadius/1.1), // Position x
							(float) (position.y+ballRadius)), // Position y
					(float) ballRadius, ballDensity, 0.7f, 0); // Radius, density, restitution and friction
			
			ballArray[3] = new PhysicsCircle("3" , // Name
					new Vector2((float) (position.x + 4 * ballRadius/1.1), // Position x
							(float) (position.y-2*ballRadius)), // Position y
					(float) ballRadius, ballDensity, 0.7f, 0); // Radius, density, restitution and friction
			
			ballArray[4] = new PhysicsCircle("4" , // Name
					new Vector2((float) (position.x + 6 * ballRadius/1.1), // Position x
							(float) (position.y-ballRadius)), // Position y
					(float) ballRadius, ballDensity, 0.7f, 0); // Radius, density, restitution and friction
			
			ballArray[5] = new PhysicsCircle("5" , // Name
					new Vector2((float) (position.x + 6 * ballRadius/1.1), // Position x
							(float) (position.y+3*ballRadius)), // Position y
					(float) ballRadius, ballDensity, 0.7f, 0); // Radius, density, restitution and friction
			
			ballArray[6] = new PhysicsCircle("6" , // Name
					new Vector2((float) (position.x + 8 * ballRadius/1.1), // Position x
							(float) (position.y-4*ballRadius)), // Position y
					(float) ballRadius, ballDensity, 0.7f, 0); // Radius, density, restitution and friction
			
			ballArray[7] = new PhysicsCircle("7" , // Name
					new Vector2((float) (position.x + 8 * ballRadius/1.1), // Position x
							(float) (position.y+2*ballRadius)), // Position y
					(float) ballRadius, ballDensity, 0.7f, 0); // Radius, density, restitution and friction
			
			ballArray[8] = new PhysicsCircle("8" , // Name
					new Vector2((float) (position.x + 4 * ballRadius/1.1), // Position x
							(float) (position.y)), // Position y
					(float) ballRadius, ballDensity, 0.7f, 0); // Radius, density, restitution and friction
			
			ballArray[9] = new PhysicsCircle("9" , // Name
					new Vector2((float) (position.x + 2 * ballRadius/1.1), // Position x
							(float) (position.y-ballRadius)), // Position y
					(float) ballRadius, ballDensity, 0.7f, 0); // Radius, density, restitution and friction
			
			ballArray[10] = new PhysicsCircle("10" , // Name
					new Vector2((float) (position.x + 4 * ballRadius/1.1), // Position x
							(float) (position.y+2*ballRadius)), // Position y
					(float) ballRadius, ballDensity, 0.7f, 0); // Radius, density, restitution and friction
			
			ballArray[11] = new PhysicsCircle("11" , // Name
					new Vector2((float) (position.x + 6 * ballRadius/1.1), // Position x
							(float) (position.y-3*ballRadius)), // Position y
					(float) ballRadius, ballDensity, 0.7f, 0); // Radius, density, restitution and friction
			
			ballArray[12] = new PhysicsCircle("12" , // Name
					new Vector2((float) (position.x + 6 * ballRadius/1.1), // Position x
							(float) (position.y+ballRadius)), // Position y
					(float) ballRadius, ballDensity, 0.7f, 0); // Radius, density, restitution and friction
			
			ballArray[13] = new PhysicsCircle("13" , // Name
					new Vector2((float) (position.x + 8 * ballRadius/1.1), // Position x
							(float) (position.y-2*ballRadius)), // Position y
					(float) ballRadius, ballDensity, 0.7f, 0); // Radius, density, restitution and friction
			
			ballArray[14] = new PhysicsCircle("14" , // Name
					new Vector2((float) (position.x + 8 * ballRadius/1.1), // Position x
							(float) (position.y)), // Position y
					(float) ballRadius, ballDensity, 0.7f, 0); // Radius, density, restitution and friction
			
			ballArray[15] = new PhysicsCircle("15" , // Name
					new Vector2((float) (position.x + 8 * ballRadius/1.1), // Position x
							(float) (position.y+4*ballRadius)), // Position y
					(float) ballRadius, ballDensity, 0.7f, 0); // Radius, density, restitution and friction
			
			for (int j = 1; j < ballArray.length; j++) {
				FrictionJointDef frictionJointDef = new FrictionJointDef();
				frictionJointDef.maxForce = 0.2f;
				frictionJointDef.maxTorque = 0.1f;
				frictionJointDef.bodyA = ballArray[j].getBody();
				frictionJointDef.bodyB = boiteF.getBody();
				frictionJointDef.collideConnected = false;
				a.world.createJoint(frictionJointDef);
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
	
	void buildPocket() {
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
