import com.badlogic.gdx.math.Vector2;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsStaticBox;
import ch.hevs.gdx2d.lib.physics.AbstractPhysicsObject;

//------------------------------------------------------------------
// Hole
//------------------------------------------------------------------
// Classe qui extends PhysicsStaticBox
// Objet utilisé pour représenter les trous du billard
// Sert à définir la méthode collision et stock un nombre qui le 
// représente sur la table de billard
//------------------------------------------------------------------
public class Hole extends PhysicsStaticBox {

	Pool p;
	int number;
	
	// ------------------------------------------------------------------
	// Hole
	// ------------------------------------------------------------------
	// Constructeur de la classe Hole
	// Apelle le constructeur de PhysicsStaticBox
	// ------------------------------------------------------------------
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
	
	// ------------------------------------------------------------------
	// collision
	// ------------------------------------------------------------------
	// Méthode appellée à chaque collision entre 2 objets physiques
	// Ici utilisée de manière à enregister les collisions dans une
	// liste de tableau 2 éléements de int.
	// ------------------------------------------------------------------
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
	
	// ------------------------------------------------------------------
	// setCollision
	// ------------------------------------------------------------------
	// Active le listeneer de collision
	// ------------------------------------------------------------------
	private void setCollision() {
		this.setSensor(true);
		this.enableCollisionListener();
	}

}
