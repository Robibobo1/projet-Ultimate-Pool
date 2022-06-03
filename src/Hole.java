import com.badlogic.gdx.math.Vector2;

import ch.hevs.gdx2d.components.physics.primitives.PhysicsStaticBox;
import ch.hevs.gdx2d.lib.physics.AbstractPhysicsObject;
import ch.hevs.gdx2d.lib.utils.Logger;

public class Hole extends PhysicsStaticBox {
	
	PoolSetup p;

	public Hole(String name, Vector2 position, float width, float height, float angle,PoolSetup p) {
		super(name, position, width, height, angle);
		setCollision();
		this.p = p;
		// TODO Auto-generated constructor stub
	}

	public Hole(String name, Vector2 position, float width, float height, PoolSetup p) {
		super(name, position, width, height);
		setCollision();
		this.p = p;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void collision(AbstractPhysicsObject other, float energy) {
		int[] collision = new int[2];
		try {
			//Logger.log(name + " collided " + other.name + " with energy " + energy);
			collision[0] = Integer.parseInt(name);
			collision[1] = Integer.parseInt(other.name);
			p.lastCollision = collision;
			p.collisionList.add(collision);
		} catch (Exception e) {

		}
	}
	
	private void setCollision()
	{
		this.setSensor(true);
		this.enableCollisionListener();
	}

}
