import com.badlogic.gdx.math.Vector2;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsStaticBox;
import ch.hevs.gdx2d.lib.physics.AbstractPhysicsObject;

public class Hole extends PhysicsStaticBox {

	Pool p;
	int number;

	public Hole(int name, Vector2 position, float width, float height, float angle, Pool p) {
		super("" + name, position, width, height, angle);
		setCollision();
		this.p = p;
		number = name;
	}

	public Hole(int name, Vector2 position, float width, float height, Pool p) {
		super("" + name, position, width, height);
		setCollision();
		this.p = p;
		number = name;
	}

	@Override
	public void collision(AbstractPhysicsObject other, float energy) {
		int[] collision = new int[2];
		try {
			collision[0] = Integer.parseInt(name);
			collision[1] = Integer.parseInt(other.name);
			p.collisionList.add(collision);
		} catch (Exception e) {

		}
	}

	private void setCollision() {
		this.setSensor(true);
		this.enableCollisionListener();
	}

}
