import java.util.Iterator;

import java.util.LinkedList;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.FrictionJoint;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;

import ch.hevs.gdx2d.components.physics.primitives.PhysicsBox;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsStaticBox;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsStaticLine;
import ch.hevs.gdx2d.components.physics.utils.PhysicsConstants;
import ch.hevs.gdx2d.components.physics.utils.PhysicsScreenBoundaries;
import ch.hevs.gdx2d.desktop.PortableApplication;
import ch.hevs.gdx2d.desktop.physics.DebugRenderer;
import ch.hevs.gdx2d.lib.GdxGraphics;
import ch.hevs.gdx2d.lib.physics.PhysicsWorld;


public class App extends PortableApplication {
	
	
	DebugRenderer dbgRenderer;
	World world = PhysicsWorld.getInstance();
	

	
	
	PhysicsCircle boule;
	PhysicsStaticBox boiteF;
	
	
	App()
	{
		super(600,500);
		
	}
	
	public static void main(String[] args) {
		new App();
		
	}

	@Override
	public void onInit() {
		// TODO Auto-generated method stub
		world.setGravity(new Vector2(0,0));
		world.setVelocityThreshold(0.0001f);
		dbgRenderer = new DebugRenderer();
		new PhysicsScreenBoundaries(getWindowWidth(), getWindowHeight());
		PoolSetup();
		
	}

	@Override
	public void onGraphicRender(GdxGraphics g) {
		// TODO Auto-generated method stub
		g.clear();

		// Physics update

		PhysicsWorld.updatePhysics(Gdx.graphics.getDeltaTime());
		dbgRenderer.render(world, g.getCamera().combined);

		g.drawFPS(Color.GREEN);
		
	}
	
	@Override
	public void onClick(int x, int y, int button) {
		super.onClick(x, y, button);

		if (button == Input.Buttons.LEFT)
			boule.applyBodyForceToCenter(new Vector2(200,0), true);
	}
	
	
void PoolSetup(){
		
	boule = new PhysicsCircle("boule", new Vector2(200,250), 12, 10, 0.7f, 0);
	boiteF = new PhysicsStaticBox(null, new Vector2(300,250), 10, 10);
	
	PhysicsStaticLine ligne = new PhysicsStaticLine("boite1", new Vector2(100,100), new Vector2(500,100));
	PhysicsStaticLine ligne2 = new PhysicsStaticLine("boite2", new Vector2(500,100), new Vector2(500,400));
	PhysicsStaticLine ligne3 = new PhysicsStaticLine("boite3", new Vector2(500,400), new Vector2(100,400));
	PhysicsStaticLine ligne4 = new PhysicsStaticLine("boite4", new Vector2(100,400), new Vector2(100,100));
	
	FrictionJointDef frictionJointDef = new FrictionJointDef();
    frictionJointDef.maxForce = 0.05f;
    frictionJointDef.maxTorque = 0;
    frictionJointDef.bodyA = boule.getBody();
    frictionJointDef.bodyB = boiteF.getBody();
    frictionJointDef.collideConnected = false;
	
	
	
	world.createJoint(frictionJointDef);
		ballPlacer(5);
		
	}
	
	void ballPlacer(int rowLeft) {
		for (int i = 0; i < rowLeft; i++) {
			FrictionJointDef frictionJointDef = new FrictionJointDef();
		    frictionJointDef.maxForce = 0.1f;
		    frictionJointDef.maxTorque = 0;
		    frictionJointDef.bodyA = new PhysicsCircle(null, new Vector2(320+(25*rowLeft), 200+12*(5-rowLeft)+25*i), 12, 10, 0.7f, 0).getBody();;
		    frictionJointDef.bodyB = boiteF.getBody();
		    frictionJointDef.collideConnected = false;
		    world.createJoint(frictionJointDef);
			
		}
		if (rowLeft>1) {
			ballPlacer(rowLeft-1);
		}
		
	}
}
