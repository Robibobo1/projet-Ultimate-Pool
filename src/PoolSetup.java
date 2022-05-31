import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;

import java.awt.Point;

import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsStaticBox;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsStaticLine;

public class PoolSetup {
	App a ;
	
	int width = 800;
	int height = width / 2;
	
	Point midP = new Point();
	
	PhysicsCircle boule;
	PhysicsStaticBox boiteF;
	
	PoolSetup(App a)
	{
		this.a = a;
		
		midP.x = a.getWindowWidth() / 2;
		midP.y = a.getWindowHeight() / 2;
		
		PhysicsStaticLine ligne = new PhysicsStaticLine("murGauche", new Vector2(midP.x - width/2,midP.y - height/2), new Vector2(midP.x - width/2,midP.y + height/2));
		PhysicsStaticLine ligne2 = new PhysicsStaticLine("murHaut", new Vector2(midP.x - width/2,midP.y + height/2), new Vector2(midP.x + width/2,midP.y + height/2));
		PhysicsStaticLine ligne3 = new PhysicsStaticLine("murDroit", new Vector2(midP.x + width/2,midP.y + height/2), new Vector2(midP.x + width/2,midP.y - height/2));
		PhysicsStaticLine ligne4 = new PhysicsStaticLine("murBas", new Vector2(midP.x + width/2,midP.y - height/2), new Vector2(midP.x - width/2,midP.y - height/2));
	}
	
	void createPool()
	{
		boule = new PhysicsCircle("boule", new Vector2(200,250), 12, 10, 0.7f, 0);
		boiteF = new PhysicsStaticBox(null, new Vector2(300,250), 10, 10);
		
		FrictionJointDef frictionJointDef = new FrictionJointDef();
	    frictionJointDef.maxForce = 0.05f;
	    frictionJointDef.maxTorque = 0;
	    frictionJointDef.bodyA = boule.getBody();
	    frictionJointDef.bodyB = boiteF.getBody();
	    frictionJointDef.collideConnected = false;
	    
	    a.world.createJoint(frictionJointDef);
		ballPlacer(5);
	}
	
	void destroyPool()
	{
		
	}
	
	void ballPlacer(int rowLeft) {
		for (int i = 0; i < rowLeft; i++) {
			FrictionJointDef frictionJointDef = new FrictionJointDef();
		    frictionJointDef.maxForce = 0.1f;
		    frictionJointDef.maxTorque = 0;
		    frictionJointDef.bodyA = new PhysicsCircle(null, new Vector2(320+(25*rowLeft), 200+12*(5-rowLeft)+25*i), 12, 10, 0.7f, 0).getBody();;
		    frictionJointDef.bodyB = boiteF.getBody();
		    frictionJointDef.collideConnected = false;
		    a.world.createJoint(frictionJointDef);
			
		}
		if (rowLeft>1) {
			ballPlacer(rowLeft-1);
		}
		
	}
}
