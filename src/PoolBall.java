import com.badlogic.gdx.math.Vector2;

import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle;
import ch.hevs.gdx2d.lib.physics.AbstractPhysicsObject;

public class PoolBall extends PhysicsCircle {
	
	App a;
	boolean isInHole = false;
	boolean collisionDetected;

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
			if (obj1 >= 20 || obj2 >= 20) {
				a.collisionDetectedPocket = true;
			}
			else
			{
				a.collisionDetectedBall = true;
			}
			//a.pool.ballArray[obj1].getBody().getLinearVelocity();
		} catch (Exception e) {

		}
		
	}

}
