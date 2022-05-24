import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import ch.hevs.gdx2d.components.physics.utils.PhysicsConstants;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle;
import ch.hevs.gdx2d.components.physics.utils.PhysicsScreenBoundaries;
import ch.hevs.gdx2d.desktop.PortableApplication;
import ch.hevs.gdx2d.desktop.physics.DebugRenderer;
import ch.hevs.gdx2d.lib.GdxGraphics;
import ch.hevs.gdx2d.lib.physics.PhysicsWorld;

public class App extends PortableApplication {
	
	
	DebugRenderer dbgRenderer;
	World world = PhysicsWorld.getInstance();
	PhysicsCircle whiteBall;
	Cane myCane;
	
	
	App(int width,int height)
	{
		super(width ,height);
		whiteBall = new PhysicsCircle("White", new Vector2(width/2,height/2), 20);
	}
	
	public static void main(String[] args) {
		new App((int) (8 * PhysicsConstants.METERS_TO_PIXELS),(int) (8 * PhysicsConstants.METERS_TO_PIXELS));
	}

	@Override
	public void onInit() {
		// TODO Auto-generated method stub
		world.setGravity(new Vector2(0, 0));
		dbgRenderer = new DebugRenderer();
		new PhysicsScreenBoundaries(getWindowWidth(), getWindowHeight());
		myCane = new Cane(new Vector2(300,150),2);
	}

	@Override
	public void onGraphicRender(GdxGraphics g) {
		// TODO Auto-generated method stub
		g.clear();
		
		
		PhysicsWorld.updatePhysics(Gdx.graphics.getDeltaTime());
		dbgRenderer.render(world, g.getCamera().combined);
		myCane.drawCane(g);
		g.drawFPS();
		
	}
	
	@Override
	public void onClick(int x, int y, int button) {
		super.onClick(x, y, button);

		if (button == Input.Buttons.LEFT)
		{
			myCane.setPosition(new Vector2(x,y));
		}
		
		if(button == Input.Buttons.RIGHT)
		{
			myCane.setAngle(myCane.angle + 10);
		}
			
	}
	
}
