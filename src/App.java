
import java.awt.Dimension;
import java.awt.Toolkit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import ch.hevs.gdx2d.components.bitmaps.BitmapImage;
import ch.hevs.gdx2d.components.bitmaps.Spritesheet;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle;
import ch.hevs.gdx2d.components.physics.utils.PhysicsScreenBoundaries;
import ch.hevs.gdx2d.desktop.PortableApplication;
import ch.hevs.gdx2d.desktop.physics.DebugRenderer;
import ch.hevs.gdx2d.lib.GdxGraphics;
import ch.hevs.gdx2d.lib.physics.PhysicsWorld;

public class App extends PortableApplication {

	enum State {
		Play, Wait, Place, End
	}

	State stateNow = State.Play;

	enum Mode {
		Normal, Double, Place
	}

	Mode gameMode = Mode.Normal;

	Texture imgSol;
	Texture imgTable;
	Texture gradient;
	Spritesheet balls;

	Player pNow, pOther, p1, p2;

	private BitmapFont titleFont, textFont;

	int waitPress = 0;
	int isPressed = 0;
	int hasBeenPressed = 0;
	int forceCanne = 0;
	float forceScaleWidth;
	
	

	int playerTurn = 0;
	PoolSetup p;
	Dimension screenSize;
	Vector2 ballPosition;
	DebugRenderer dbgRenderer;
	World world = PhysicsWorld.getInstance();
	Cane myCane;
	int clickCnt = 0;
	Vector2 force = new Vector2(1, 1);

	App(Dimension screenSize) {
		super(screenSize.width, screenSize.height, false);
		this.screenSize = screenSize;
		ballPosition = new Vector2(this.screenSize.width / 2, this.screenSize.height / 2);

		p1 = new Player(1);
		p2 = new Player(2);

		pNow = p1;
		pOther = p2;
	}

	public static void main(String[] args) {
		Dimension size = new Dimension(1920,1080);//Toolkit.getDefaultToolkit().getScreenSize();
		new App(size);

	}

	@Override
	public void onInit() {
		// TODO Auto-generated method stub

		FileHandle Dosis = Gdx.files.internal("data/font/Dosis.ttf");
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Dosis);
		parameter.size = generator.scaleForPixelHeight(70);
		parameter.color = Color.BLACK;
		titleFont = generator.generateFont(parameter);
		parameter.size = generator.scaleForPixelHeight(40);
		textFont = generator.generateFont(parameter);

		world.setGravity(new Vector2(0, 0));
		World.setVelocityThreshold(0.0001f);
		dbgRenderer = new DebugRenderer();
		new PhysicsScreenBoundaries(getWindowWidth(), getWindowHeight());

		p = new PoolSetup(this);
		p.createPool();
		myCane = new Cane(new Vector2(300, 150), 0);
		forceScaleWidth = 1;
		imgSol = new Texture("data/images/Sol.png");
		imgTable = new Texture("data/images/Table2.png");
		gradient = new Texture("data/images/gradient.png");
		balls = new Spritesheet("data/images/Boules.png", 100, 100);
	}

	@Override
	public void onGraphicRender(GdxGraphics g) {
		// TODO Auto-generated method stub
		g.clear();
		g.draw(imgSol, 0, 0,screenSize.width,screenSize.height);
		g.draw(imgTable, 302 + (screenSize.width - 1920f)/2 , 165 + (screenSize.height - 1080f)/2, p.poolSize.width + 158 , p.poolSize.height + 164);
		g.draw(gradient, screenSize.width / 2 - 200, screenSize.height - 90, forceScaleWidth, 25, 500, 500, 0, 0); // à améliorer

		showGameInfo(g);

		PhysicsWorld.updatePhysics(Gdx.graphics.getDeltaTime());

		for (int i = 0; i < 16; i++) {
			if (!p.ballArray[i].isInHole) {
				g.draw(balls.sprites[0][i], (float) (p.ballArray[i].getBodyPosition().x - p.ballRadius),
						(float) (p.ballArray[i].getBodyPosition().y - p.ballRadius), (float) p.ballRadius * 2,
						(float) p.ballRadius * 2);
			}
		}

		dbgRenderer.render(world, g.getCamera().combined);

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
			Vector2 mousePosition = new Vector2(Gdx.input.getX(), screenSize.height - Gdx.input.getY());
			if (clickCnt >= 1) {
				p.placeWhite(mousePosition);
				gameMode = Mode.Normal;
				stateNow = State.Play;
				clickCnt = 0;
			}
			break;
		case End:
			System.out.println("END");
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
			pNow.ballsInAll.add(2);

		}
	}

	boolean canePlacement() throws InterruptedException {

		Vector2 mousePosition = new Vector2(Gdx.input.getX(), screenSize.height - Gdx.input.getY());
		float angle = 90 + (float) Math
				.toDegrees(Math.atan((mousePosition.y - ballPosition.y) / (mousePosition.x - ballPosition.x)));
		if (mousePosition.x - ballPosition.x < 0)
			angle = angle + 180;
		force.setAngle(angle + 90);

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
			waitPress = 1;
			if (isPressed == 1) {

				float newPosX;
				float newPosY;

				hasBeenPressed = 1;

				if (forceCanne <= 200) {

					forceCanne++;

					forceScaleWidth = 2 * forceCanne;

					newPosX = (float) (myCane.position.x - Math.cos(Math.toRadians(myCane.angle + 90)) * 2);
					newPosY = (float) (myCane.position.y - Math.sin(Math.toRadians(myCane.angle + 90)) * 2);

					myCane.setPosition(new Vector2(newPosX, newPosY));
				}

			}
			if (isPressed == 0 && hasBeenPressed == 1) {
				System.out.println(forceCanne);
				float newPosX;
				float newPosY;

				forceScaleWidth = 1;

				newPosX = (float) (myCane.position.x + Math.cos(Math.toRadians(myCane.angle + 90)) * forceCanne / 2);
				newPosY = (float) (myCane.position.y + Math.sin(Math.toRadians(myCane.angle + 90)) * forceCanne / 2);

				myCane.setPosition(new Vector2(newPosX, newPosY));
				if (collisionPoint != null) {
					float lenght = myCane.getVelocity().len() / 3;
					force.setLength(lenght);
					force.setAngle(myCane.getVelocity().angle());
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

	public void onKeyDown(int input) {
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

			if (pNow.playerType == null) {
				nextPlayer();
				return;
			}

			if (gameMode == Mode.Double && !didFault) {
				gameMode = Mode.Normal;
				return;
			}
			if (!pNow.ballsInTmp.isEmpty() && !didFault) {
				pNow.ballsInTmp.clear();
				gameMode = Mode.Normal;
				return;
			}
			pNow.ballsInTmp.clear();
			nextPlayer();
		}
	}

	boolean checkForFault() {

		if (!pNow.ballsInTmp.isEmpty()) {

			if (pNow.playerType == null) {
				int firstBall = pNow.ballsInTmp.firstElement();
				if (isStriped(firstBall)) {
					pNow.playerType = Player.BallType.Striped;
					pOther.playerType = Player.BallType.Solid;
				}
				if (isSolid(firstBall)) {
					pNow.playerType = Player.BallType.Solid;
					pOther.playerType = Player.BallType.Striped;
				}
			}

			for (int ballIn : pNow.ballsInTmp) {
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

				if (pNow.playerType == null)
					return false;

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
					pNow.ballsInTmp.add(p.lastCollision[1]);
					pNow.ballsInAll.add(p.lastCollision[1]);
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
		out += p1.debugBall() + "\n";
		out += p2.debugBall();
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

	void showGameInfo(GdxGraphics g) {

		Color backColor = new Color(222f / 255, 183f / 255, 127f / 255, 1);

		int titleConst = (int) (775f/1080f*screenSize.height);
		int leftConst = 58;
		int rightConst = screenSize.width - 240;

		if (pNow.number == 1) {
			g.drawFilledRectangle(leftConst + 90, titleConst - 235, 220, 510, 0, Color.YELLOW);
			g.drawFilledRectangle(rightConst + 90, titleConst - 235, 220, 510, 0, Color.BLACK);
		} else {
			g.drawFilledRectangle(leftConst + 90, titleConst - 235, 220, 510, 0, Color.BLACK);
			g.drawFilledRectangle(rightConst + 90, titleConst - 235, 220, 510, 0, Color.YELLOW);
		}

		g.drawFilledRectangle(leftConst + 90, titleConst - 235, 210, 500, 0, backColor);
		g.drawString(leftConst, titleConst, "Player " + p1.number, titleFont);
		g.drawString(leftConst, titleConst - 65, "Score : " + p1.score, textFont);
		g.drawString(leftConst, titleConst - 115, "Ball type : ", textFont);
		String pType;
		if (p1.playerType == null)
			pType = "--- ";
		else
			pType = p1.playerType.name();
		g.drawString(leftConst, titleConst - 165, pType, textFont);

		int collumn = 0;
		int line = 0;

		for (int ball : p1.ballsInAll) {
			g.draw(balls.sprites[0][ball], leftConst + 5 + collumn * 60, titleConst - 260 - line * 60, 45, 45);
			collumn++;
			if (collumn == 3) {
				collumn = 0;
				line++;
			}
		}

		g.drawFilledRectangle(rightConst + 90, titleConst - 235, 210, 500, 0, backColor);
		g.drawString(rightConst, titleConst, "Player " + p2.number, titleFont);
		g.drawString(rightConst, titleConst - 65, "Score : " + p2.score, textFont);
		g.drawString(rightConst, titleConst - 115, "Ball type : ", textFont);
		if (p2.playerType == null)
			pType = "--- ";
		else
			pType = p2.playerType.name();
		g.drawString(rightConst, titleConst - 165, pType, textFont);

		collumn = 0;
		line = 0;
		for (int ball : p2.ballsInAll) {
			g.draw(balls.sprites[0][ball], rightConst + 5 + collumn * 60, titleConst - 260 - line * 60, 45, 45);
			collumn++;
			if (collumn == 3) {
				collumn = 0;
				line++;
			}
		}

	}

}
