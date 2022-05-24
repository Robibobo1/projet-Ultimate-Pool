import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import ch.hevs.gdx2d.components.physics.primitives.PhysicsBox;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsStaticBox;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsStaticLine;
import ch.hevs.gdx2d.components.physics.utils.PhysicsScreenBoundaries;
import ch.hevs.gdx2d.desktop.PortableApplication;
import ch.hevs.gdx2d.desktop.physics.DebugRenderer;
import ch.hevs.gdx2d.lib.GdxGraphics;
import ch.hevs.gdx2d.lib.physics.PhysicsWorld;

public class App extends PortableApplication {
	LinkedList<PhysicsCircle> list = new LinkedList<PhysicsCircle>();
	
	
	
	DebugRenderer dbgRenderer;
	World world = PhysicsWorld.getInstance();
	
	
	
//	PhysicsStaticLine ligne = new PhysicsStaticLine("boite1", new Vector2(100,100), new Vector2(500,100));
//	PhysicsStaticLine ligne2 = new PhysicsStaticLine("boite2", new Vector2(500,100), new Vector2(500,400));
//	PhysicsStaticLine ligne3 = new PhysicsStaticLine("boite3", new Vector2(500,400), new Vector2(100,400));
//	PhysicsStaticLine ligne4 = new PhysicsStaticLine("boite4", new Vector2(100,400), new Vector2(100,100));
//	PhysicsBox queue = new PhysicsBox("queue", new Vector2(250,300), 200, 4, 50, 0, 0);
	
	//addBall(400,302);
	//addBall(210,302);
	
	//queue.applyBodyForce(new Vector2(0.01f,0), new Vector2(449,302), true);
	
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
		dbgRenderer = new DebugRenderer();
		new PhysicsScreenBoundaries(getWindowWidth(), getWindowHeight());
		
		new PoolSetup();
		//PhysicyCircle boule = new 
	}

	@Override
	public void onGraphicRender(GdxGraphics g) {
		// TODO Auto-generated method stub
		g.clear();

		// Physics update
		for (Iterator<PhysicsCircle> it = list.iterator(); it.hasNext(); ) {
			PhysicsCircle c = it.next();
			Float radius = c.getBodyRadius();
			Vector2 pos = c.getBodyPosition();
			g.drawCircle(pos.x, pos.y, radius);
			c.setBodyAwake(true);
		}
		
		PhysicsWorld.updatePhysics(Gdx.graphics.getDeltaTime());
		dbgRenderer.render(world, g.getCamera().combined);

		g.drawFPS(Color.GREEN);
		
	}
	
	@Override
	public void onClick(int x, int y, int button) {
		super.onClick(x, y, button);

		if (button == Input.Buttons.LEFT)
			addBall(x, y);
			//queue.applyBodyForceToCenter(new Vector2(1500,0), CreateLwjglApplication);
	}
	
	public void addBall(int x, int y) {
		// If there exists enough ball already, remove the oldest one
		if (list.size() > 50) {
			PhysicsCircle b = list.poll();
			b.destroy();
		}

		float size = (float) ((Math.random() * 15.0f)) + 15f;
		PhysicsCircle b = new PhysicsCircle(null, new Vector2(x, y), size);

		// Add the ball to the list of existing balls
		list.add(b);
	}
}
