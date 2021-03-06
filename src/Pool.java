import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Vector;

import ch.hevs.gdx2d.components.physics.primitives.PhysicsStaticBox;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsStaticLine;
import ch.hevs.gdx2d.lib.physics.AbstractPhysicsObject;

//------------------------------------------------------------------
// Pool
//------------------------------------------------------------------
// Classe créant tous les attributs et les objets physiques 
// nécessaires pour une table de billard. Stock par la même occasion
// les collisions de chaque round.
//------------------------------------------------------------------
public class Pool {
	App a;
	
	PoolBall[] ballArray = new PoolBall[16];
	Vector<int[]> collisionList = new Vector<int[]>();
	Hole[] holesArray = new Hole[6];

	Dimension poolSize = new Dimension();
	Point midP = new Point();
	
	PhysicsStaticBox frictionBox;
	
	int ballDensity = 10;
	double ballRadius;
	double sidePocket;
	double cornerPocketRad;
	double sideDepth;

	// ------------------------------------------------------------------
	// Pool
	// ------------------------------------------------------------------
	// Constructeur de la classe Pool
	// Définit toutes les valeurs de taille selon l'objet App a en paramètre
	// ------------------------------------------------------------------
	Pool(App a) {
		this.a = a;

		midP.x = a.screenSize.width / 2;
		midP.y = a.screenSize.height / 2;

		poolSize.height = 580;
		poolSize.width = poolSize.height * 2;

		ballRadius = ((double) poolSize.height * 0.05089) / 2;
		sidePocket = poolSize.height * 0.14;
		cornerPocketRad = Math.sqrt(Math.pow(poolSize.height * 0.10446, 2) / 2);
		sideDepth = poolSize.height * 0.04553;
	}

	// ------------------------------------------------------------------
	// createPool
	// ------------------------------------------------------------------
	// Construit tous les éléments physique de la table de billard
	// ------------------------------------------------------------------
	void createPool() {
		buildWall();
		buildPocket();

		frictionBox = new PhysicsStaticBox(null, new Vector2(midP.x, midP.y), 10, 10);

		placeWhite(new Vector2((int) (midP.x - (0.25 * poolSize.width)), (int) (midP.y)));
		placeTriangle(new Point((int) (midP.x + (0.25 * poolSize.width)), (int) (midP.y)));
	}

	// ------------------------------------------------------------------
	// placeWhite
	// ------------------------------------------------------------------
	// Crée une nouvelle balle blanche et la place à la position donnée
	// en paramètre.
	// ------------------------------------------------------------------
	void placeWhite(Vector2 position) {
		ballArray[0] = null;
		ballArray[0] = new PoolBall("0", position, (float) ballRadius, 10f, 0.7f, 0f, a) {
			@Override
			public void collision(AbstractPhysicsObject other, float energy) {
				//System.out.println("blanche collision");
				int[] collision = new int[2];
				try {
					collision[0] = Integer.parseInt(name);
					collision[1] = Integer.parseInt(other.name);
					collisionList.add(collision);
				} catch (Exception e) {
					
				}
			}
		};

		FrictionJointDef frictionJointDef = new FrictionJointDef();
		frictionJointDef.maxForce = 0.2f;
		frictionJointDef.maxTorque = 0.1f;
		frictionJointDef.bodyA = ballArray[0].getBody();
		frictionJointDef.bodyB = frictionBox.getBody();
		frictionJointDef.collideConnected = false;
		a.world.createJoint(frictionJointDef);
	}
	
	// ------------------------------------------------------------------
	// placeTriangle
	// ------------------------------------------------------------------
	// Place toutes les balles du triangle de billard
	// ------------------------------------------------------------------
	void placeTriangle(Point position) {
		collisionList.clear();
		
		ballArray[1] = new PoolBall("1", // Name
				new Vector2((float) (position.x), // Position x
						(float) (position.y)), // Position y
				(float) ballRadius, ballDensity, 0.7f, 0, a); // Radius, density, restitution and friction

		ballArray[2] = new PoolBall("2", // Name
				new Vector2((float) (position.x + 2 * ballRadius / 1.1), // Position x
						(float) (position.y + ballRadius)), // Position y
				(float) ballRadius, ballDensity, 0.7f, 0, a); // Radius, density, restitution and friction

		ballArray[3] = new PoolBall("3", // Name
				new Vector2((float) (position.x + 4 * ballRadius / 1.1), // Position x
						(float) (position.y - 2 * ballRadius)), // Position y
				(float) ballRadius, ballDensity, 0.7f, 0, a); // Radius, density, restitution and friction

		ballArray[4] = new PoolBall("4", // Name
				new Vector2((float) (position.x + 6 * ballRadius / 1.1), // Position x
						(float) (position.y - ballRadius)), // Position y
				(float) ballRadius, ballDensity, 0.7f, 0, a); // Radius, density, restitution and friction

		ballArray[5] = new PoolBall("5", // Name
				new Vector2((float) (position.x + 6 * ballRadius / 1.1), // Position x
						(float) (position.y + 3 * ballRadius)), // Position y
				(float) ballRadius, ballDensity, 0.7f, 0, a); // Radius, density, restitution and friction

		ballArray[6] = new PoolBall("6", // Name
				new Vector2((float) (position.x + 8 * ballRadius / 1.1), // Position x
						(float) (position.y - 4 * ballRadius)), // Position y
				(float) ballRadius, ballDensity, 0.7f, 0, a); // Radius, density, restitution and friction

		ballArray[7] = new PoolBall("7", // Name
				new Vector2((float) (position.x + 8 * ballRadius / 1.1), // Position x
						(float) (position.y + 2 * ballRadius)), // Position y
				(float) ballRadius, ballDensity, 0.7f, 0, a); // Radius, density, restitution and friction

		ballArray[8] = new PoolBall("8", // Name
				new Vector2((float) (position.x + 4 * ballRadius / 1.1), // Position x
						(float) (position.y)), // Position y
				(float) ballRadius, ballDensity, 0.7f, 0, a); // Radius, density, restitution and friction

		ballArray[9] = new PoolBall("9", // Name
				new Vector2((float) (position.x + 2 * ballRadius / 1.1), // Position x
						(float) (position.y - ballRadius)), // Position y
				(float) ballRadius, ballDensity, 0.7f, 0, a); // Radius, density, restitution and friction

		ballArray[10] = new PoolBall("10", // Name
				new Vector2((float) (position.x + 4 * ballRadius / 1.1), // Position x
						(float) (position.y + 2 * ballRadius)), // Position y
				(float) ballRadius, ballDensity, 0.7f, 0, a); // Radius, density, restitution and friction

		ballArray[11] = new PoolBall("11", // Name
				new Vector2((float) (position.x + 6 * ballRadius / 1.1), // Position x
						(float) (position.y - 3 * ballRadius)), // Position y
				(float) ballRadius, ballDensity, 0.7f, 0, a); // Radius, density, restitution and friction

		ballArray[12] = new PoolBall("12", // Name
				new Vector2((float) (position.x + 6 * ballRadius / 1.1), // Position x
						(float) (position.y + ballRadius)), // Position y
				(float) ballRadius, ballDensity, 0.7f, 0, a); // Radius, density, restitution and friction

		ballArray[13] = new PoolBall("13", // Name
				new Vector2((float) (position.x + 8 * ballRadius / 1.1), // Position x
						(float) (position.y - 2 * ballRadius)), // Position y
				(float) ballRadius, ballDensity, 0.7f, 0, a); // Radius, density, restitution and friction

		ballArray[14] = new PoolBall("14", // Name
				new Vector2((float) (position.x + 8 * ballRadius / 1.1), // Position x
						(float) (position.y)), // Position y
				(float) ballRadius, ballDensity, 0.7f, 0, a); // Radius, density, restitution and friction

		ballArray[15] = new PoolBall("15", // Name
				new Vector2((float) (position.x + 8 * ballRadius / 1.1), // Position x
						(float) (position.y + 4 * ballRadius)), // Position y
				(float) ballRadius, ballDensity, 0.7f, 0,a); // Radius, density, restitution and friction

		for (int j = 1; j < ballArray.length; j++) {
			FrictionJointDef frictionJointDef = new FrictionJointDef();
			frictionJointDef.maxForce = 0.2f;
			frictionJointDef.maxTorque = 0.1f;
			frictionJointDef.bodyA = ballArray[j].getBody();
			frictionJointDef.bodyB = frictionBox.getBody();
			frictionJointDef.collideConnected = false;
			a.world.createJoint(frictionJointDef);
		}

	}
	
	// string de debeug
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

	// ------------------------------------------------------------------
	// buildPocket
	// ------------------------------------------------------------------
	// Construit les trous dans le moteur physique
	// ------------------------------------------------------------------
	void buildPocket() {
		holesArray[1] = new Hole(21, new Vector2(midP.x, (float) (midP.y + (poolSize.height / 2) + 0.75 * sidePocket)),
				(float) sidePocket, (float) sidePocket, this);
		
		holesArray[4] = new Hole(24, new Vector2(midP.x, (float) (midP.y - (poolSize.height / 2) - 0.75 * sidePocket)),
				(float) sidePocket, (float) sidePocket, this);

		holesArray[0] = new Hole(20,
				new Vector2((float) (midP.x - poolSize.width / 2 - 1.5 * sideDepth),
						(float) (midP.y - (poolSize.height / 2) - 1.5 * sideDepth)),
				(float) sidePocket, (float) sidePocket, (float) (Math.PI / 4), this);
		holesArray[2] = new Hole(22,
				new Vector2((float) (midP.x - poolSize.width / 2 - 1.5 * sideDepth),
						(float) (midP.y + (poolSize.height / 2) + 1.5 * sideDepth)),
				(float) sidePocket, (float) sidePocket, (float) (Math.PI / 4), this);

		holesArray[3] = new Hole(23,
				new Vector2((float) (midP.x + poolSize.width / 2 + 1.5 * sideDepth),
						(float) (midP.y - (poolSize.height / 2) - 1.5 * sideDepth)),
				(float) sidePocket, (float) sidePocket, (float) (Math.PI / 4), this);

		holesArray[5] = new Hole(25,
				new Vector2((float) (midP.x + poolSize.width / 2 + 1.5 * sideDepth),
						(float) (midP.y + (poolSize.height / 2) + 1.5 * sideDepth)),
				(float) sidePocket, (float) sidePocket, (float) (Math.PI / 4), this);
	}
	
	// ------------------------------------------------------------------
	// buildWall
	// ------------------------------------------------------------------
	// Construit les murs du billard dans le moteur physique
	// ------------------------------------------------------------------
	void buildWall() {
		// le coté gauche du billard, a partir du trou du haut
		new PhysicsStaticLine("Gauche",
				new Vector2(midP.x - (poolSize.width / 2), (float) (midP.y + poolSize.height / 2 - cornerPocketRad)),
				new Vector2(midP.x - (poolSize.width / 2), (float) (midP.y - poolSize.height / 2 + cornerPocketRad)));
		// On commence par le haut du billard, a partir du trou sur le coté
		new PhysicsStaticLine("Haut droite",
				new Vector2((float) (midP.x + (sidePocket / 2)), midP.y + poolSize.height / 2),
				new Vector2((float) (midP.x + poolSize.width / 2 - cornerPocketRad), midP.y + poolSize.height / 2));
		new PhysicsStaticLine("Haut gauche",
				new Vector2((float) (midP.x - (sidePocket / 2)), midP.y + poolSize.height / 2),
				new Vector2((float) (midP.x - poolSize.width / 2 + cornerPocketRad), midP.y + poolSize.height / 2));

		// le bas du billard, a partir du trou sur le coté
		new PhysicsStaticLine("Bas droite",
				new Vector2((float) (midP.x + (sidePocket / 2)), midP.y - poolSize.height / 2),
				new Vector2((float) (midP.x + poolSize.width / 2 - cornerPocketRad), midP.y - poolSize.height / 2));
		new PhysicsStaticLine("Bas gauche",
				new Vector2((float) (midP.x - (sidePocket / 2)), midP.y - poolSize.height / 2),
				new Vector2((float) (midP.x - poolSize.width / 2 + cornerPocketRad), midP.y - poolSize.height / 2));

		// le coté droit du billard, a partir du trou du haut
		new PhysicsStaticLine("Droite",
				new Vector2(midP.x + (poolSize.width / 2), (float) (midP.y + poolSize.height / 2 - cornerPocketRad)),
				new Vector2(midP.x + (poolSize.width / 2), (float) (midP.y - poolSize.height / 2 + cornerPocketRad)));

		// le coté droit du billard, a partir du trou du haut
		new PhysicsStaticLine("Gauche",
				new Vector2(midP.x - (poolSize.width / 2), (float) (midP.y + poolSize.height / 2 - cornerPocketRad)),
				new Vector2(midP.x - (poolSize.width / 2), (float) (midP.y - poolSize.height / 2 + cornerPocketRad)));

		// Side pocket up
		new PhysicsStaticLine("SP", new Vector2((float) (midP.x + (sidePocket / 2)), midP.y + poolSize.height / 2),
				new Vector2((float) (midP.x + (sidePocket * 0.4)), (float) (midP.y + poolSize.height / 2 + sideDepth)));
		new PhysicsStaticLine("SP", new Vector2((float) (midP.x - (sidePocket / 2)), midP.y + poolSize.height / 2),
				new Vector2((float) (midP.x - (sidePocket * 0.4)), (float) (midP.y + poolSize.height / 2 + sideDepth)));

		// Side pocket down
		new PhysicsStaticLine("SP", new Vector2((float) (midP.x + (sidePocket / 2)), midP.y - poolSize.height / 2),
				new Vector2((float) (midP.x + (sidePocket * 0.4)), (float) (midP.y - poolSize.height / 2 - sideDepth)));
		new PhysicsStaticLine("SP", new Vector2((float) (midP.x - (sidePocket / 2)), midP.y - poolSize.height / 2),
				new Vector2((float) (midP.x - (sidePocket * 0.4)), (float) (midP.y - poolSize.height / 2 - sideDepth)));

		// corner pockets
		new PhysicsStaticLine("CP",
				new Vector2((float) (midP.x + poolSize.width / 2 - cornerPocketRad), midP.y + poolSize.height / 2),
				new Vector2((float) (midP.x + poolSize.width / 2 - cornerPocketRad + sideDepth),
						(float) (midP.y + poolSize.height / 2 + sideDepth)));

		new PhysicsStaticLine("CP",
				new Vector2(midP.x + (poolSize.width / 2), (float) (midP.y + poolSize.height / 2 - cornerPocketRad)),
				new Vector2((float) (midP.x + (poolSize.width / 2) + sideDepth),
						(float) (midP.y + poolSize.height / 2 - cornerPocketRad + sideDepth)));

		new PhysicsStaticLine("CP",
				new Vector2(midP.x - (poolSize.width / 2), (float) (midP.y - poolSize.height / 2 + cornerPocketRad)),
				new Vector2((float) (midP.x - (poolSize.width / 2) - sideDepth),
						(float) (midP.y - poolSize.height / 2 + cornerPocketRad - sideDepth)));
		new PhysicsStaticLine("CP",
				new Vector2((float) (midP.x - poolSize.width / 2 + cornerPocketRad), midP.y - poolSize.height / 2),
				new Vector2((float) (midP.x - poolSize.width / 2 + cornerPocketRad - sideDepth),
						(float) (midP.y - poolSize.height / 2 - sideDepth)));

		new PhysicsStaticLine("CP",
				new Vector2(midP.x + (poolSize.width / 2), (float) (midP.y - poolSize.height / 2 + cornerPocketRad)),
				new Vector2((float) (midP.x + (poolSize.width / 2) + sideDepth),
						(float) (midP.y - poolSize.height / 2 + cornerPocketRad - sideDepth)));
		new PhysicsStaticLine("CP",
				new Vector2((float) (midP.x + poolSize.width / 2 - cornerPocketRad), midP.y - poolSize.height / 2),
				new Vector2((float) (midP.x + poolSize.width / 2 - cornerPocketRad + sideDepth),
						(float) (midP.y - poolSize.height / 2 - sideDepth)));

		new PhysicsStaticLine("CP",
				new Vector2((float) (midP.x - poolSize.width / 2 + cornerPocketRad), midP.y + poolSize.height / 2),
				new Vector2((float) (midP.x - poolSize.width / 2 + cornerPocketRad - sideDepth),
						(float) (midP.y + poolSize.height / 2 + sideDepth)));
		new PhysicsStaticLine("CP",
				new Vector2(midP.x - (poolSize.width / 2), (float) (midP.y + poolSize.height / 2 - cornerPocketRad)),
				new Vector2((float) (midP.x - (poolSize.width / 2) - sideDepth),
						(float) (midP.y + poolSize.height / 2 - cornerPocketRad + sideDepth)));
	}

}
