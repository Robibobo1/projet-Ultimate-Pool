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
	PoolSetup p;

	App()
	{
		super(1920,1080);	
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
		p = new PoolSetup(this);
		p.createPool();
		
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
	

	
}
