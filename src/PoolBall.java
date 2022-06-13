import com.badlogic.gdx.math.Vector2;

import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle;

public class PoolBall extends PhysicsCircle {
	
	boolean isInHole = false;
	int holeNbr = 0;

	public PoolBall(String name, Vector2 position, float radius, float density, float restitution, float friction) {
		super(name, position, radius, density, restitution, friction);
		// TODO Auto-generated constructor stub
	}
	
	


}
