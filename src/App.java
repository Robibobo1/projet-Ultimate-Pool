import java.awt.event.KeyEvent;
import java.util.Iterator;

import java.util.LinkedList;
import java.util.Vector;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.FrictionJoint;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;

import ch.hevs.gdx2d.components.bitmaps.BitmapImage;
import ch.hevs.gdx2d.components.bitmaps.Spritesheet;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsBox;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsStaticBox;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsStaticLine;
import ch.hevs.gdx2d.components.physics.utils.PhysicsConstants;
import ch.hevs.gdx2d.components.physics.utils.PhysicsScreenBoundaries;
import ch.hevs.gdx2d.desktop.PortableApplication;
import ch.hevs.gdx2d.desktop.physics.DebugRenderer;
import ch.hevs.gdx2d.lib.GdxGraphics;
import ch.hevs.gdx2d.lib.physics.AbstractPhysicsObject;
import ch.hevs.gdx2d.lib.physics.PhysicsWorld;

public class App extends PortableApplication  {

	enum State {
		Play, Wait, Place, End
	}

	State stateNow = State.Play;

	enum Mode {
		Normal, Double, Place
	}

	Mode gameMode = Mode.Normal;

	BitmapImage imgSol;
	Texture imgTable;
	Texture gradient;
	Spritesheet balls;

	Player pNow, pOther, p1, p2;

	int waitPress = 0;
	int isPressed = 0;
	int hasBeenPressed=0;
	int forceCanne = 0;
	float forceScaleWidth;

	
	int playerTurn = 0;
	PoolSetup p;
	int width, height;
	Vector2 ballPosition;
	DebugRenderer dbgRenderer;
	World world = PhysicsWorld.getInstance();
	Cane myCane;
	int clickCnt = 0;
	Vector2 force = new Vector2(1, 1);

	App(int width, int height) {
		super(width, height, true);
		this.width = width;
		this.height = height;
		ballPosition = new Vector2(this.width / 2, this.height / 2);

		p1 = new Player(1);
		p2 = new Player(2);

		pNow = p1;
		pOther = p2;
	}

	public static void main(String[] args) {
		new App(1920, 1080);
		
	}

	@Override
	public void onInit() {
		// TODO Auto-generated method stub

		world.setGravity(new Vector2(0, 0));
		World.setVelocityThreshold(0.0001f);
		dbgRenderer = new DebugRenderer();
		new PhysicsScreenBoundaries(getWindowWidth(), getWindowHeight());

		p = new PoolSetup(this);
		p.createPool();
		myCane = new Cane(new Vector2(300, 150), 0);
		forceScaleWidth = 1;
		imgSol = new BitmapImage("data/images/Sol.png");
		imgTable = new Texture("data/images/Table.png");
		gradient = new Texture("data/images/gradient.png");
		balls = new Spritesheet("data/images/Boules.png", 100, 100);
	}

	@Override
	public void onGraphicRender(GdxGraphics g) {
		// TODO Auto-generated method stub
		g.clear();

		g.drawBackground(imgSol, 0, 0);
		g.draw(imgTable, 280, 158, 1359, 765);
		g.draw(gradient, width/2-200, height-90, forceScaleWidth , 25,500,500,0,0); //à améliorer


		for (int i = 0; i < 16; i++) {
			if (!p.ballArray[i].isInHole) {
				g.draw(balls.sprites[0][i], (float) (p.ballArray[i].getBodyPosition().x - p.ballRadius),
						(float) (p.ballArray[i].getBodyPosition().y - p.ballRadius), (float) p.ballRadius * 2,
						(float) p.ballRadius * 2);
			}
		}

		PhysicsWorld.updatePhysics(Gdx.graphics.getDeltaTime());
		//dbgRenderer.render(world, g.getCamera().combined);

		if (gameMode != Mode.Place)
			ballPosition = p.ballArray[0].getBodyPosition();

		switch (stateNow) {
		case Play:
			try {
				if (canePlacement()) {
					p.collisionList.clear();
					stateNow = State.Wait;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (gameMode != Mode.Place)
				myCane.drawCane(g);
			break;
		case Wait:
			waitForSomething();
			break;
		case Place:
			Vector2 mousePosition = new Vector2(Gdx.input.getX(), this.height - Gdx.input.getY());
			if (clickCnt >= 1) {
				p.placeWhite(mousePosition);
				gameMode = Mode.Normal;
				stateNow = State.Play;
				clickCnt = 0;
			}
			break;
		case End:
			System.out.println("yes");
			break;
		default:
			break;
		}
		// System.out.println(myCane.debug());
		g.drawFPS();
		g.drawString(20, 200, debugGameEngine());
	}

	@Override
	public void onClick(int x, int y, int button) {
		super.onClick(x, y, button);

		if (button == Input.Buttons.LEFT) {
			if (!CollisionDetection.hasCollision(p.ballArray[0], myCane))
				clickCnt++;
		}

		if (button == Input.Buttons.RIGHT) {
			if (!CollisionDetection.hasCollision(p.ballArray[0], myCane))
				clickCnt--;
		}

		if (button == Input.Buttons.MIDDLE) {
			System.out.println();
			
		}
	}

	boolean canePlacement() throws InterruptedException {

		Vector2 mousePosition = new Vector2(Gdx.input.getX(), this.height - Gdx.input.getY());
		float angle = 90 + (float) Math
				.toDegrees(Math.atan((mousePosition.y - ballPosition.y) / (mousePosition.x - ballPosition.x)));
		if (mousePosition.x - ballPosition.x < 0)
			angle = angle + 180;
		force.setAngle(angle + 90);
		
		float angleF = 90 + (float) Math
				.toDegrees(Math.atan((myCane.position.y - ballPosition.y) / (myCane.position.x - ballPosition.x)));
		if (myCane.position.x - ballPosition.x < 0)
			angleF = angleF + 180;

		Vector2 collisionPoint = CollisionDetection.pointInMeter(p.ballArray[0], myCane);
		force.set(1f, 1f);

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
				force.setAngle(myCane.getVelocity().angle());
				if (!Double.isNaN(force.len()))
					p.ballArray[0].applyBodyForce(force, collisionPoint, CreateLwjglApplication);
				clickCnt = 0;
				return true;
			}
			break;
		case 2:

			
//			double slope = (myCane.hitPoints[1].y - myCane.position.y) / (myCane.hitPoints[1].x - myCane.position.x);
//			float offset = (float) (myCane.position.y - slope * myCane.position.x);
//			float yLinearValue = (float) (slope * mousePosition.x + offset);
//			Vector2 linearPosition = new Vector2(mousePosition.x, yLinearValue);
//			myCane.setPosition(linearPosition);
//			if (collisionPoint != null) {
//				float lenght = myCane.getVelocity().len() / 3;
//				force.setLength(lenght);
//				force.setAngle(angle + 90);
//				if (!Double.isNaN(force.len()))
//					p.ballArray[0].applyBodyForce(force, collisionPoint, CreateLwjglApplication);
//				clickCnt = 0;
//				return true;
//			}
//			
			
			
			waitPress = 1;
			if (isPressed == 1) {
				
				float newPosX;
				float newPosY;
				
				hasBeenPressed = 1;
				
				
				if (forceCanne <= 200) {
					
					forceCanne++;
					
					forceScaleWidth = 2*forceCanne;

		
					newPosX = (float) (myCane.position.x-Math.cos(Math.toRadians(myCane.angle+90))*2);
					newPosY = (float) (myCane.position.y-Math.sin(Math.toRadians(myCane.angle+90))*2);
					
					myCane.setPosition(new Vector2(newPosX,newPosY));
				}
	
			}
			if (isPressed == 0 && hasBeenPressed == 1) {
				System.out.println(forceCanne);
				float newPosX;
				float newPosY;
				
				forceScaleWidth = 1;
				
				newPosX = (float) (myCane.position.x+Math.cos(Math.toRadians(myCane.angle+90))*forceCanne/2);
				newPosY = (float) (myCane.position.y+Math.sin(Math.toRadians(myCane.angle+90))*forceCanne/2);
				
				myCane.setPosition(new Vector2(newPosX,newPosY));
				if (collisionPoint != null) {
					float lenght = myCane.getVelocity().len() / 3;
					force.setLength(lenght);
					force.setAngle(angleF + 90);
					if (!Double.isNaN(force.len()))
						p.ballArray[0].applyBodyForce(force, collisionPoint, CreateLwjglApplication);
					clickCnt = 0;
					forceCanne = 0;
					hasBeenPressed = 0;
					return true;
				}

			}
							
			break;
		default:
			clickCnt = 0;
			break;
		}
		return false;
	}
	
	public void onKeyDown(int input){
		if (input == Input.Keys.SPACE && waitPress == 1) {
			isPressed = 1;
		}
	}

	public void onKeyUp(int input) {
		if (input == Input.Keys.SPACE) {
			isPressed = 0;
		}
	}
	
	boolean roundEnded() {
		for (PhysicsCircle c : p.ballArray) {
			if (c.getBodyLinearVelocity().len() > 0.001f) {
				return false;
			}
		}
		return true;
	}

	void waitForSomething() {

		checkBallInHole();

		if (roundEnded()) {
			clickCnt = 0;
			boolean didFault = checkForFault();

			if (stateNow != State.End)
				stateNow = State.Play;

			if (gameMode == Mode.Place)
				stateNow = State.Place;
			
			if(pNow.playerType == null)
				return;

			if (gameMode == Mode.Double && !didFault) {
				gameMode = Mode.Normal;
				return;
			}
			if (!pNow.ballsIn.isEmpty() && !didFault) {
				pNow.ballsIn.clear();
				gameMode = Mode.Normal;
				return;
			}

			pNow.ballsIn.clear();
			nextPlayer();
		}
	}

	boolean checkForFault() {

		if (!pNow.ballsIn.isEmpty()) {
			
			if (pNow.playerType == null) {
				int firstBall = pNow.ballsIn.firstElement();
				if (isStriped(firstBall)) {
					pNow.playerType = Player.BallType.Striped;
					pOther.playerType = Player.BallType.Solid;
				}
				if (isSolid(firstBall)) {
					pNow.playerType = Player.BallType.Solid;
					pOther.playerType = Player.BallType.Striped;
				}
			}

			for (int ballIn : pNow.ballsIn) {
				if (ballIn == 8 && pNow.score < 7) {
					stateNow = State.End;
					return true;
				}
				if (isStriped(ballIn) && pNow.playerType == Player.BallType.Solid) {
					if (gameMode == Mode.Normal)
						gameMode = Mode.Double;
					pOther.score++;
					return true;
				}
				if (isSolid(ballIn) && pNow.playerType == Player.BallType.Striped) {
					if (gameMode == Mode.Normal)
						gameMode = Mode.Double;
					pOther.score++;
					return true;
				}
				if (isStriped(ballIn) && pNow.playerType == Player.BallType.Striped) {
					pNow.score++;
				}
				if (isSolid(ballIn) && pNow.playerType == Player.BallType.Solid) {
					pNow.score++;
				}

			}

		}

		if (gameMode == Mode.Place) {
			return true;
		}

		if (p.collisionList.isEmpty()) { // Si on touche rien
			gameMode = Mode.Double;
			return true;
			
		} else {
			int[] firstCollision = p.collisionList.firstElement();
			
			if (firstCollision[0] == 0) { // Lis la première collision de la balle
				
				if(pNow.playerType == null) return false;
				
				if (isStriped(firstCollision[1]) && pNow.playerType == Player.BallType.Solid) {
					gameMode = Mode.Double;
					return true;
				}
				if (isSolid(firstCollision[1]) && pNow.playerType == Player.BallType.Striped) {
					gameMode = Mode.Double;
					return true;
				}
			}
		}

		return false;
	}

	void checkBallInHole() {
		if (p.lastCollision != null) {
			for (int i = 20; i < 26; i++) {
				if (p.lastCollision[0] == i) // Balle normale dans trou
				{
					pNow.ballsIn.add(p.lastCollision[1]);
					p.ballArray[p.lastCollision[1]].setBodyLinearVelocity(0, 0);
					p.ballArray[p.lastCollision[1]].destroy();
					p.ballArray[p.lastCollision[1]].isInHole = true;
					p.lastCollision = null;
					return;
				}
			}

			if (p.lastCollision[0] == 0) // Balle blanche dans trou
			{
				if (p.lastCollision[1] >= 20) {
					p.ballArray[0].setBodyLinearVelocity(0, 0);
					p.ballArray[0].destroy();
					p.ballArray[0].isInHole = true;
					gameMode = Mode.Place;
					p.lastCollision = null;
					return;
				}
			}
		}
	}

	String debugGameEngine() {
		String out = "";
		out += "Player " + pNow.number + " - " + pNow.playerType + " - " + pNow.score;
		out += "\nPlayer " + pOther.number + " - " + pOther.playerType + " - " + pOther.score;
		out += "\nState: " + stateNow;
		out += "\nMode: " + gameMode + "\n";
		out += p.debugCollisionList() + "\n";
		out += p.ballArray[0].getBodyLinearVelocity().len() + "\n";
		out += myCane.getVelocity().angle();
		return out;
	}

	void nextPlayer() {

		if (pNow.number == p1.number) {
			p1 = pNow;
			p2 = pOther;
			pNow = p2;
			pOther = p1;
		} else {
			p1 = pOther;
			p2 = pNow;
			pNow = p1;
			pOther = p2;
		}
	}

	boolean isStriped(int ballNbr) {
		if (ballNbr >= 9 && ballNbr <= 15) {
			return true;
		}
		return false;
	}

	boolean isSolid(int ballNbr) {
		if (ballNbr >= 1 && ballNbr <= 7) {
			return true;
		}
		return false;
	}

	void drawBalls(PhysicsCircle ball, GdxGraphics g, Color c) {
		g.drawFilledCircle(ball.getBodyPosition().x, ball.getBodyPosition().y, ball.getBodyRadius(), c);

		int ballNbr = Integer.parseInt(ball.name);

		if (isSolid(ballNbr) || ballNbr == 8) {

		}
		if (isStriped(ballNbr)) {

		}

	}
}
