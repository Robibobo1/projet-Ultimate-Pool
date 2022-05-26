import java.awt.geom.Ellipse2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.awt.geom.Ellipse2D.Float;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import ch.hevs.gdx2d.components.physics.utils.PhysicsConstants;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle;
import ch.hevs.gdx2d.components.physics.utils.PhysicsScreenBoundaries;
import ch.hevs.gdx2d.desktop.PortableApplication;
import ch.hevs.gdx2d.desktop.physics.DebugRenderer;
import ch.hevs.gdx2d.lib.GdxGraphics;
import ch.hevs.gdx2d.lib.physics.PhysicsWorld;

public class App extends PortableApplication {

	int width, height;
	Vector2 ballPosition;
	DebugRenderer dbgRenderer;
	World world = PhysicsWorld.getInstance();
	PhysicsCircle whiteBall;
	Cane myCane;
	int clickCnt = 0;
	Vector2 force = new Vector2(1, 1);

	App(int width, int height) {
		super(width, height);
		this.width = width;
		this.height = height;
		ballPosition = new Vector2(this.width / 2, this.height / 2);
		whiteBall = new PhysicsCircle("White", ballPosition, 20);
	}

	public static void main(String[] args) {
		new App(600, 600);
	}

	@Override
	public void onInit() {
		// TODO Auto-generated method stub
		world.setGravity(new Vector2(0, 0));
		dbgRenderer = new DebugRenderer();
		new PhysicsScreenBoundaries(getWindowWidth(), getWindowHeight());
		myCane = new Cane(new Vector2(300, 150), 0);
	}

	@Override
	public void onGraphicRender(GdxGraphics g) {
		// TODO Auto-generated method stub
		g.clear();
		
		PhysicsWorld.updatePhysics(Gdx.graphics.getDeltaTime());
		dbgRenderer.render(world, g.getCamera().combined);
		
		ballPosition = whiteBall.getBodyPosition();
		canePlacement();
		myCane.drawCane(g);
		//System.out.println(myCane.debug());
		g.drawFPS();

	}

	@Override
	public void onClick(int x, int y, int button) {
		super.onClick(x, y, button);
		
		if (button == Input.Buttons.LEFT) {
			clickCnt++;
		}

		if (button == Input.Buttons.RIGHT) {
			clickCnt--;
		}
	}

	void canePlacement() {
		
		Vector2 mousePosition = new Vector2(Gdx.input.getX(), this.height - Gdx.input.getY());
		float angle = 90 + (float) Math.toDegrees(Math.atan((mousePosition.y - ballPosition.y) / (mousePosition.x - ballPosition.x)));
		if (mousePosition.x - ballPosition.x < 0)
			angle = angle + 180;
		
		force.setAngle(angle + 90);
		//System.out.println(myCane.getVelocity().len());
		force.setLength(myCane.getVelocity().len() + 0.01f);
		System.out.println(force);

		Vector2 collisionPoint = CollisionDetection.pointInMeter(whiteBall, myCane);
		switch (clickCnt) {
		case 0:
			if (collisionPoint == null) {
				myCane.setPosition(mousePosition);
			} else {
				whiteBall.applyBodyForce(force, collisionPoint, CreateLwjglApplication);
			}

			myCane.setAngle(angle);
			break;
		case 1:
			if (collisionPoint == null)
				myCane.setPosition(mousePosition);
			else
				whiteBall.applyBodyForce(force, collisionPoint, CreateLwjglApplication);
			break;
		default:
			clickCnt = 0;
			break;
		}
	}
}
