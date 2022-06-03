
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
	
	
	
	int roundMade = 0;
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
		super(width, height, false);
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
			waitForSomething();
			break;
		default:
			break;
		}
		// System.out.println(myCane.debug());
		g.drawFPS();
		g.drawString(20, 100, "" + playerTurn + " " + gameMode.name()  + " "+ p.debugCollisionList() + " " + stateNow.name());
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

		}
	}

	boolean canePlacement() {

		Vector2 mousePosition = new Vector2(Gdx.input.getX(), this.height - Gdx.input.getY());
		float angle = 90 + (float) Math
				.toDegrees(Math.atan((mousePosition.y - ballPosition.y) / (mousePosition.x - ballPosition.x)));
		if (mousePosition.x - ballPosition.x < 0)
			angle = angle + 180;
		force.setAngle(angle + 90);
		
		Vector2 collisionPoint = CollisionDetection.pointInMeter(p.ballArray[0], myCane);
		
		switch (clickCnt) {
		case 0:
			myCane.setPosition(mousePosition);
			myCane.setAngle(angle);
			break;
		case 1:
			myCane.setPosition(mousePosition);
			if (collisionPoint != null) {
				float lenght = myCane.getVelocity().len() / 3;
				force.setLength(lenght);
				force.setAngle(angle + 90);
				p.ballArray[0].applyBodyForce(force, collisionPoint, CreateLwjglApplication);
				clickCnt = 0;
				return true;
			}
			break;
		case 2:
			double slope = (myCane.hitPoints[1].y - myCane.position.y) / (myCane.hitPoints[1].x - myCane.position.x);
			float offset = (float) (myCane.position.y - slope * myCane.position.x);
			float yLinearValue = (float) (slope * mousePosition.x + offset);
			Vector2 linearPosition = new Vector2(mousePosition.x, yLinearValue);
			myCane.setPosition(linearPosition);
			if (collisionPoint != null) {
				float lenght = myCane.getVelocity().len() / 3;
				force.setLength(lenght);
				force.setAngle(angle + 90);
				p.ballArray[0].applyBodyForce(force, collisionPoint, CreateLwjglApplication);
				clickCnt = 0;
				return true;
			}
			break;
		default:
			clickCnt = 0;
			break;
		}
		return false;
	}

	void play(boolean player, Mode gameMode) {
		
		if(gameMode == Mode.Place)
		{
			
		}
		else
		{
			if(canePlacement()) {
				p.collisionList.clear();
				stateNow = State.Wait;
			}
		}
	}

	boolean roundEnded() {
		for(PhysicsCircle c : p.ballArray)
		{
			if(c.getBodyLinearVelocity().len() > 0.001f)
			{
				return false;
			}
		}
		return true;
	}

	void waitForSomething() {
		
		checkBallInHole();
		
		if(roundEnded())
		{
			boolean didFault = checkForFault();
			stateNow = State.Play;
			if(gameMode == Mode.Double && didFault == false)
			{
				gameMode = Mode.Normal;
				return;
			}
			playerTurn = !playerTurn;
		}
	}
	
	boolean checkForFault()
	{
		if(p.collisionList.isEmpty())
		{
			gameMode = Mode.Double;
			return true;
		}
		return false;
	}
	
	void checkBallInHole()
	{
		if(p.lastCollision != null)
		{
			for (int i = 20; i < 26; i++) {
				if(p.lastCollision[0] == i) // Balle normale dans trou
				{
					p.ballArray[p.lastCollision[1]].setBodyLinearVelocity(0, 0);
					p.ballArray[p.lastCollision[1]].destroy();
					p.lastCollision = null;	
					return;
				}
			}
			
			if(p.lastCollision[0] == 0) // Balle blanche dans trou
			{
				if(p.lastCollision[1] >= 20)
				{
					p.ballArray[0].destroy();
					p.lastCollision = null;	
					return;
				}
			}
		}
	}
	

}
