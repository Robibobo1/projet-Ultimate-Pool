import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle;
import ch.hevs.gdx2d.components.physics.utils.PhysicsScreenBoundaries;
import ch.hevs.gdx2d.desktop.PortableApplication;
import ch.hevs.gdx2d.desktop.physics.DebugRenderer;
import ch.hevs.gdx2d.lib.GdxGraphics;
import ch.hevs.gdx2d.lib.physics.PhysicsWorld;

public class App extends PortableApplication {
	LinkedList<PhysicsCircle> list = new LinkedList<PhysicsCircle>();
	
	DebugRenderer dbgRenderer;
	World world = PhysicsWorld.getInstance();
	
	App()
	{
		super(500,600);
	}
	
	public static void main(String[] args) {
		new App();
	}

	@Override
	public void onInit() {
		// TODO Auto-generated method stub
		world.setGravity(new Vector2(5, 10));
		dbgRenderer = new DebugRenderer();
		new PhysicsScreenBoundaries(getWindowWidth(), getWindowHeight());
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

		g.drawFPS();
		
	}
	
	@Override
	public void onClick(int x, int y, int button) {
		super.onClick(x, y, button);

		if (button == Input.Buttons.LEFT)
			addBall(x, y);
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
