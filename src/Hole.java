import com.badlogic.gdx.math.Vector2;

import ch.hevs.gdx2d.components.physics.primitives.PhysicsStaticBox;
import ch.hevs.gdx2d.lib.physics.AbstractPhysicsObject;
import ch.hevs.gdx2d.lib.utils.Logger;

public class Hole extends PhysicsStaticBox {

	public Hole(String name, Vector2 position, float width, float height, float angle) {
		super(name, position, width, height, angle);
		setCollision();
		// TODO Auto-generated constructor stub
	}

	public Hole(String name, Vector2 position, float width, float height) {
		super(name, position, width, height);
		setCollision();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void collision(AbstractPhysicsObject other, float energy) {
		Logger.log(name + " collided " + other.name + " with energy " + energy);
	}
	
	private void setCollision()
	{
		this.setSensor(true);
		this.enableCollisionListener();
	}

}
