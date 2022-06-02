
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

	enum State {
		Play, Wait, Fault, Destroy, End
	}

	State stateNow = State.Play;

	enum Mode {
		Normal, Double, Place
	}

	Mode gameMode = Mode.Normal;

	boolean playerTurn = false;
	PoolSetup p;
	int width, height;
	Vector2 ballPosition;
	DebugRenderer dbgRenderer;
	World world = PhysicsWorld.getInstance();
	Cane myCane;
	int clickCnt = 0;
	Vector2 force = new Vector2(1, 1);

	App(int width, int height) {
		super(width, height);
		this.width = width;
		this.height = height;
		ballPosition = new Vector2(this.width / 2, this.height / 2);
	}

	public static void main(String[] args) {
		new App(1920, 1080);
	}

	@Override
	public void onInit() {
		// TODO Auto-generated method stub

		world.setGravity(new Vector2(0, 0));
		world.setVelocityThreshold(0.0001f);
		dbgRenderer = new DebugRenderer();
		new PhysicsScreenBoundaries(getWindowWidth(), getWindowHeight());

		p = new PoolSetup(this);
		p.createPool();
		myCane = new Cane(new Vector2(300, 150), 0);

	}

	@Override
	public void onGraphicRender(GdxGraphics g) {
		// TODO Auto-generated method stub
		g.clear();

		PhysicsWorld.updatePhysics(Gdx.graphics.getDeltaTime());
		dbgRenderer.render(world, g.getCamera().combined);

		ballPosition = p.ballArray[0].getBodyPosition();

		switch (stateNow) {
		case Play:
			play(playerTurn, gameMode);
			canePlacement();
			myCane.drawCane(g);
			break;
		case Wait:

			break;
		default:
			break;
		}
		// System.out.println(myCane.debug());
		g.drawFPS();
		g.drawString(400, 100, "a");
	}

	@Override
	public void onClick(int x, int y, int button) {
		super.onClick(x, y, button);

		if (button == Input.Buttons.LEFT) {
			if (!CollisionDetection.hasCollision(p.ballArray[0], myCane))
				clickCnt++;
		}

		if (button == Input.Buttons.RIGHT) {
			clickCnt--;
		}

		if (button == Input.Buttons.MIDDLE) {

			for (int[] a : p.collisionList) {
				System.out.print(a[0]);
				System.out.println("    " + a[1]);
			}

		}
	}

	void canePlacement() {

		Vector2 mousePosition = new Vector2(Gdx.input.getX(), this.height - Gdx.input.getY());
		float angle = 90 + (float) Math
				.toDegrees(Math.atan((mousePosition.y - ballPosition.y) / (mousePosition.x - ballPosition.x)));
		if (mousePosition.x - ballPosition.x < 0)
			angle = angle + 180;
		force.setAngle(angle + 90);
		
		//force.setLength(myCane.getVelocity().len() + 0.01f);
		Vector2 collisionPoint = CollisionDetection.pointInMeter(p.ballArray[0], myCane);
		switch (clickCnt) {
		case 0:
			myCane.setPosition(mousePosition);
			myCane.setAngle(angle);
			break;
		case 1:
			myCane.setPosition(mousePosition);
			break;
		case 2:
			double slope = (myCane.hitPoints[1].y - myCane.position.y) / (myCane.hitPoints[1].x - myCane.position.x);
			float offset = (float) (myCane.position.y - slope * myCane.position.x);
			float yLinearValue = (float) (slope * mousePosition.x + offset);
			Vector2 linearPosition = new Vector2(mousePosition.x, yLinearValue);
			myCane.setPosition(linearPosition);
			if (collisionPoint != null) {
				float lenght = myCane.getVelocity().len() / 1;
				force.setLength(lenght);
				force.setAngle(angle + 90);
				p.ballArray[0].applyBodyForce(force, collisionPoint, CreateLwjglApplication);
				clickCnt = 0;
				playerTurn = !playerTurn;
				stateNow = State.Wait;
			}
			break;
		default:
			clickCnt = 0;
			break;
		}
	}

	void play(boolean player, Mode gameMode) {
		
		canePlacement();
	}

	boolean roundEnded() {
		return false;
	}

	void waitForSomething() {

	}
	

}
