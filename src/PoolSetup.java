import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;

import java.awt.Point;

import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsStaticBox;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsStaticLine;

public class PoolSetup {
	App a ;
	
	
	int height = 600;
	int width = height*2;
	
	int ballDensity = 10;
	double ballRadius = ((double)height * 0.05089)/2;
	
	double sidePocket = height*0.11875;
	double cornerPocketRad = Math.sqrt(Math.pow(height*0.10446, 2)/2);
	
	double sideDepth = height*0.04553;
	
	int ballNumber = 0;
	
	PhysicsCircle[] ballArray = new PhysicsCircle[16];
	
	Point midP = new Point();
	
	PhysicsCircle boule;
	
	PhysicsStaticBox boiteF;
	
	
	
	PoolSetup(App a)
	{
		this.a = a;
		
		midP.x = a.getWindowWidth() / 2;
		midP.y = a.getWindowHeight() / 2;
		
//		PhysicsStaticLine ligne = new PhysicsStaticLine("murGauche", new Vector2(midP.x - width/2,midP.y - height/2), new Vector2(midP.x - width/2,midP.y + height/2));
//		PhysicsStaticLine ligne2 = new PhysicsStaticLine("murHaut", new Vector2(midP.x - width/2,midP.y + height/2), new Vector2(midP.x + width/2,midP.y + height/2));
//		PhysicsStaticLine ligne3 = new PhysicsStaticLine("murDroit", new Vector2(midP.x + width/2,midP.y + height/2), new Vector2(midP.x + width/2,midP.y - height/2));
//		PhysicsStaticLine ligne4 = new PhysicsStaticLine("murBas", new Vector2(midP.x + width/2,midP.y - height/2), new Vector2(midP.x - width/2,midP.y - height/2));
	
		//On commence par le haut du billard, a partir du trou sur le coté
		new PhysicsStaticLine("Haut, droite", new Vector2((float) (midP.x + (sidePocket/2)), midP.y + height/2), new Vector2((float) (midP.x + width/2 - cornerPocketRad),midP.y + height/2));
		new PhysicsStaticLine("Haut, gauche", new Vector2((float) (midP.x - (sidePocket/2)), midP.y + height/2), new Vector2((float) (midP.x - width/2 + cornerPocketRad),midP.y + height/2));
	
		//le bas du billard, a partir du trou sur le coté
		new PhysicsStaticLine("Bas, droite", new Vector2((float) (midP.x + (sidePocket/2)), midP.y - height/2), new Vector2((float) (midP.x + width/2 - cornerPocketRad),midP.y - height/2));
		new PhysicsStaticLine("Bas, gauche", new Vector2((float) (midP.x - (sidePocket/2)), midP.y - height/2), new Vector2((float) (midP.x - width/2 + cornerPocketRad),midP.y - height/2));
			
		//le coté droit du billard, a partir du trou du haut
		new PhysicsStaticLine("Droite", new Vector2(midP.x + (width/2), (float) (midP.y + height/2 - cornerPocketRad)), new Vector2(midP.x + (width/2),(float) (midP.y - height/2 + cornerPocketRad)));
		
		//le coté gauche du billard, a partir du trou du haut
		new PhysicsStaticLine("Gauche", new Vector2(midP.x - (width/2), (float) (midP.y + height/2 - cornerPocketRad)), new Vector2(midP.x - (width/2),(float) (midP.y - height/2 + cornerPocketRad)));
		
		//Side pocket up
		new PhysicsStaticLine("SP", new Vector2((float) (midP.x + (sidePocket/2)), midP.y + height/2), new Vector2((float) (midP.x + (sidePocket*0.4)), (float) (midP.y + height/2+sideDepth)));
		new PhysicsStaticLine("SP", new Vector2((float) (midP.x - (sidePocket/2)), midP.y + height/2), new Vector2((float) (midP.x - (sidePocket*0.4)), (float) (midP.y + height/2+sideDepth)));
	
		//Side pocket down
		new PhysicsStaticLine("SP", new Vector2((float) (midP.x + (sidePocket/2)), midP.y - height/2), new Vector2((float) (midP.x + (sidePocket*0.4)), (float) (midP.y - height/2-sideDepth)));
		new PhysicsStaticLine("SP", new Vector2((float) (midP.x - (sidePocket/2)), midP.y - height/2), new Vector2((float) (midP.x - (sidePocket*0.4)), (float) (midP.y - height/2-sideDepth)));
		
		//corner pockets
		new PhysicsStaticLine("CP", new Vector2((float) (midP.x + width/2 - cornerPocketRad),midP.y + height/2), new Vector2((float) (midP.x + width/2 - cornerPocketRad+sideDepth),(float)(midP.y + height/2+sideDepth)));
		new PhysicsStaticLine("CP", new Vector2(midP.x + (width/2) ,(float) (midP.y + height/2 - cornerPocketRad)), new Vector2((float) (midP.x + (width/2)+sideDepth) ,(float) (midP.y + height/2 - cornerPocketRad+sideDepth)));
	
		new PhysicsStaticLine("CP", new Vector2(midP.x - (width/2),(float) (midP.y - height/2 + cornerPocketRad)), new Vector2((float) (midP.x - (width/2)-sideDepth),(float) (midP.y - height/2 + cornerPocketRad-sideDepth)));
		new PhysicsStaticLine("CP", new Vector2((float) (midP.x - width/2 + cornerPocketRad),midP.y - height/2), new Vector2((float) (midP.x - width/2 + cornerPocketRad-sideDepth),(float) (midP.y - height/2-sideDepth)));

		new PhysicsStaticLine("CP", new Vector2(midP.x + (width/2),(float) (midP.y - height/2 + cornerPocketRad)), new Vector2((float) (midP.x + (width/2)+sideDepth),(float) (midP.y - height/2 + cornerPocketRad-sideDepth)));
		new PhysicsStaticLine("Bas, droite", new Vector2((float) (midP.x + width/2 - cornerPocketRad),midP.y - height/2), new Vector2((float) (midP.x + width/2 - cornerPocketRad+sideDepth),(float) (midP.y - height/2-sideDepth)));
		
		new PhysicsStaticLine("Haut, gauche", new Vector2((float) (midP.x - width/2 + cornerPocketRad),midP.y + height/2), new Vector2((float) (midP.x - width/2 + cornerPocketRad-sideDepth),(float) (midP.y + height/2+sideDepth)));
		new PhysicsStaticLine("Gauche", new Vector2(midP.x - (width/2), (float) (midP.y + height/2 - cornerPocketRad)), new Vector2((float) (midP.x - (width/2)-sideDepth), (float) (midP.y + height/2 - cornerPocketRad+sideDepth)));

	}
	
	void createPool()
	{
		boule = new PhysicsCircle("boule", new Vector2((int) (midP.x-(0.25*width)),(int) (midP.y)), (float) ballRadius, 10, 0.7f, 0);
		ballArray[0] = boule;
		
		boiteF = new PhysicsStaticBox(null, new Vector2(midP.x,midP.y), 10, 10);
		
		FrictionJointDef frictionJointDef = new FrictionJointDef();
	    frictionJointDef.maxForce = 0.05f;
	    frictionJointDef.maxTorque = 0;
	    frictionJointDef.bodyA = boule.getBody();
	    frictionJointDef.bodyB = boiteF.getBody();
	    frictionJointDef.collideConnected = false;
	    
	    a.world.createJoint(frictionJointDef);
	    placeTriangle(new Point((int) (midP.x+(0.25*width)),(int) (midP.y-4*ballRadius)));
	}
	
	void destroyPool()
	{
		
	}
	
	
	void placeTriangle(Point position)
	{
		ballPlacer(5,position);
	}
	
	
	void ballPlacer(int rowLeft,Point position) {
		for (int i = 0; i < rowLeft; i++) {
			
			ballNumber ++;
			ballArray[ballNumber] = new PhysicsCircle(null, new Vector2((float) (position.x +(ballRadius*2*rowLeft)), (float) (position.y + ballRadius*(5-rowLeft)+ ballRadius*2*i)), (float) ballRadius, ballDensity, 0.7f, 0);
			
			FrictionJointDef frictionJointDef = new FrictionJointDef();
		    frictionJointDef.maxForce = 0.1f;
		    frictionJointDef.maxTorque = 0;
		    frictionJointDef.bodyA = ballArray[ballNumber].getBody();
		    frictionJointDef.bodyB = boiteF.getBody();
		    frictionJointDef.collideConnected = false;
		    a.world.createJoint(frictionJointDef);
			
		}
		if (rowLeft>1) {
			ballPlacer(rowLeft-1,position);
		}
	}
	
	
}
