import com.badlogic.gdx.math.Vector2;

import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle;
import ch.hevs.gdx2d.lib.physics.AbstractPhysicsObject;

//------------------------------------------------------------------
// PoolBall
//------------------------------------------------------------------
// Classe qui extend PhysicsCircle
// Sert à stocker des informations supplémentaire que PhysiscsCircle
// ne pouvait pas donner
// Sert aussi à définir la méthode collision
//------------------------------------------------------------------
public class PoolBall extends PhysicsCircle {

	App a;
	boolean isInHole = false;
	int holeNbr = 0;

	// ------------------------------------------------------------------
	// PoolBall
	// ------------------------------------------------------------------
	// Constructeur de la classse PoolBall
	// Apelle le constructeur de PhysicsCircle
	// ------------------------------------------------------------------
	public PoolBall(String name, Vector2 position, float radius, float density, float restitution, float friction,
			App a) {
		super(name, position, radius, density, restitution, friction);
		this.a = a;
		// TODO Auto-generated constructor stub
	}

	// ------------------------------------------------------------------
	// collision
	// ------------------------------------------------------------------
	// Méthode appelée à cahque collision avec un autre objet physique
	// Ici utilisé pour créer des sons pour chaque choc
	// ------------------------------------------------------------------
	public void collision(AbstractPhysicsObject other, float energy) {
		int obj1;
		int obj2;

		try {
			obj1 = Integer.parseInt(name);
			obj2 = Integer.parseInt(other.name);
			float speed = a.pool.ballArray[obj1].getBody().getLinearVelocity().len();
			System.out.println(speed);
			if (obj1 >= 20 || obj2 >= 20) {
				a.collisionSound = 0;
				return;
			}
			
			if(speed < 0.1f) 
				a.collisionSound = -1;
			
			if (speed < 3) 
				a.collisionSound = 2;
			
			if (speed >= 3) 
				a.collisionSound = 1;
			
		} catch (Exception e) {
		}
	}

}
