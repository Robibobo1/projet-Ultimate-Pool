import com.badlogic.gdx.math.Vector2;

import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle;
import ch.hevs.gdx2d.lib.physics.AbstractPhysicsObject;

public class PoolBall extends PhysicsCircle {
	
	App a;
	boolean isInHole = false;
	boolean collisionDetected;
	int holeNbr = 0;

	public PoolBall(String name, Vector2 position, float radius, float density, float restitution, float friction, App a) {
		super(name, position, radius, density, restitution, friction);
		this.a = a;
		// TODO Auto-generated constructor stub
	}
	
	public void collision(AbstractPhysicsObject other, float energy) {
	//System.out.println("autre collision");
		int obj1;
		int obj2;
		
		try {
			obj1 = Integer.parseInt(name);
			obj2 = Integer.parseInt(other.name);
			System.out.println("vitesse");
			System.out.println(a.pool.ballArray[obj1].getBody().getLinearVelocity().len());
			if (obj1 >= 20 || obj2 >= 20) {
				a.collisionSound = 0;
			}
			else if(a.pool.ballArray[obj1].getBody().getLinearVelocity().len() < 3)
			{
				a.collisionSound = 2;
			}
			else if(a.pool.ballArray[obj1].getBody().getLinearVelocity().len() >= 3){
				a.collisionSound = 1;
			}
		} catch (Exception e) {
		}
	}

}
