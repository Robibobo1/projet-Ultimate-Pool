import java.awt.Dimension;
import java.util.Random;

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

import ch.hevs.gdx2d.components.audio.MusicPlayer;
import ch.hevs.gdx2d.components.audio.SoundSample;
import ch.hevs.gdx2d.components.bitmaps.Spritesheet;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle;
import ch.hevs.gdx2d.components.physics.utils.PhysicsScreenBoundaries;
import ch.hevs.gdx2d.desktop.PortableApplication;
import ch.hevs.gdx2d.desktop.physics.DebugRenderer;
import ch.hevs.gdx2d.lib.GdxGraphics;
import ch.hevs.gdx2d.lib.physics.PhysicsWorld;

public class App extends PortableApplication {

	enum State {
		Play, Wait, Place, Win, Lose
	}

	State stateNow = State.Play;

	enum Mode {
		Normal, Double, Place
	}

	Mode gameMode = Mode.Normal;

	World world = PhysicsWorld.getInstance();
	Pool pool;
	Cane cane;
	DebugRenderer dbgRenderer;

	Texture imgSol;
	Texture imgTable;
	Texture gradient;
	Spritesheet balls;
	Spritesheet cues;

	Player pNow, pOther, p1, p2;

	BitmapFont titleFont, textFont;

	Dimension screenSize;
	Vector2 ballPosition;
	
	SoundSample hitHard;
	SoundSample hitCane;
	SoundSample pocket;
	SoundSample hitSoft;
	boolean collisionDetectedPocket = false;
	boolean collisionDetectedBall = false;
	boolean collisionDetectedBallSoft = false;
	boolean collisionDetectedCane = false;

	boolean waitPress = false;
	boolean isPressed = false;
	boolean hasBeenPressed = false;

	int forceCane = 0;
	float forceScaleWidth = 1;

	int clickCnt = 0;
	Vector2 force = new Vector2(1, 1);

	App(Dimension screenSize) {
		super(screenSize.width, screenSize.height, true);
		this.screenSize = screenSize;
		ballPosition = new Vector2(0, 0);

		p1 = new Player(1);
		p2 = new Player(2);

		Random rand = new Random();
		p1.skin = rand.nextInt(2) * 2;
		p2.skin = rand.nextInt(2) * 2 + 1;

		pNow = p1;
		pOther = p2;
	}

	public static void main(String[] args) {
		Dimension size = new Dimension(1920, 1080); // Toolkit.getDefaultToolkit().getScreenSize();
		new App(size);
	}

	@Override
	public void onInit() {

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

		pool = new Pool(this);
		pool.createPool();
		cane = new Cane(new Vector2(300, 150), 0);

		imgSol = new Texture("data/images/Sol.png");
		imgTable = new Texture("data/images/Table.png");
		gradient = new Texture("data/images/gradient.png");
		balls = new Spritesheet("data/images/Boules.png", 100, 100);
		cues = new Spritesheet("data/images/gameCues.png", 3578, 100);
		
		hitHard = new SoundSample("data/Sounds/hitHard.mp3");
		hitSoft = new SoundSample("data/Sounds/hitSoft.mp3");
		pocket = new SoundSample("data/Sounds/inPocket.mp3");
		hitCane = new SoundSample("data/Sounds/hitCane.mp3");
	}

	@Override
	public void onGraphicRender(GdxGraphics g) {
		g.clear();
		g.draw(imgSol, 0, 0, screenSize.width, screenSize.height);
		g.draw(imgTable, 302 + (screenSize.width - 1920f) / 2, 168 + (screenSize.height - 1080f) / 2,
				pool.poolSize.width + 158, pool.poolSize.height + 164);
		g.draw(gradient, screenSize.width / 2 - 200, screenSize.height - 90, forceScaleWidth, 25, 500, 500, 0, 0); // à

		setPlayerScore();
		showGameInfo(g);

		PhysicsWorld.updatePhysics(Gdx.graphics.getDeltaTime());

		for (int i = 0; i < 16; i++) {
			if (!pool.ballArray[i].isInHole) {
				g.draw(balls.sprites[0][i], (float) (pool.ballArray[i].getBodyPosition().x - pool.ballRadius),
						(float) (pool.ballArray[i].getBodyPosition().y - pool.ballRadius), (float) pool.ballRadius * 2,
						(float) pool.ballRadius * 2);
			}
		}

		// dbgRenderer.render(world, g.getCamera().combined);

		if (gameMode != Mode.Place)
			ballPosition = pool.ballArray[0].getBodyPosition();
		
		if (collisionDetectedPocket) {
			hitHard.setVolume((float) 0.05);
			pocket.play();
			System.out.println("sound played");
			collisionDetectedPocket = false;
		}
		
		if (collisionDetectedBall) {
			hitHard.setVolume((float) 0.05);
			hitHard.play();
			System.out.println("sound played");
			collisionDetectedBall = false;
		}

		if (collisionDetectedBallSoft) {
			hitSoft.setVolume((float) 0.05);
			hitSoft.play();
			System.out.println("sound played (soft)");
			collisionDetectedBallSoft = false;
		}
		
		if (collisionDetectedCane) {
			hitCane.setVolume((float) 0.1);
			hitCane.play();
			System.out.println("sound played (Cane)");
			collisionDetectedCane = false;
		}

		switch (stateNow) {
		case Play:
			if (gameMode == Mode.Place) {
				stateNow = State.Place;
				break;
			}

			cane.updateHitPoint();
			drawCane(g);
			if (canePlacement()) {
				pool.collisionList.clear();
				stateNow = State.Wait;
			}
			break;
		case Wait:
			waitForSomething();
			break;
		case Place:
			Vector2 mousePosition = new Vector2(Gdx.input.getX(), screenSize.height - Gdx.input.getY());
			if (clickCnt >= 1) {
				pool.placeWhite(mousePosition);
				gameMode = Mode.Normal;
				stateNow = State.Play;
				clickCnt = 0;
			}
			break;
		case Lose:
			System.out.println("Lose");
			break;	
		case Win:
			System.out.println("Win");
			break;
			
		default:
			break;
		}

		g.drawFPS();
		g.drawString(20, 200, debugGameEngine());
	}

	@Override
	public void onClick(int x, int y, int button) {
		super.onClick(x, y, button);

		if (button == Input.Buttons.LEFT) {
			if (!CollisionDetection.hasCollision(pool.ballArray[0], cane))
				clickCnt++;
		}

		if (button == Input.Buttons.RIGHT) {
			if (!CollisionDetection.hasCollision(pool.ballArray[0], cane))
				clickCnt--;
		}

		if (button == Input.Buttons.MIDDLE)
			pNow.ballsInAll.add(2);
	}

	boolean canePlacement() {

		Vector2 mousePosition = new Vector2(Gdx.input.getX(), screenSize.height - Gdx.input.getY());
		float angle = 90 + (float) Math
				.toDegrees(Math.atan((mousePosition.y - ballPosition.y) / (mousePosition.x - ballPosition.x)));
		if (mousePosition.x - ballPosition.x < 0)
			angle = angle + 180;
		force.setAngle(angle + 90);

		Vector2 collisionPoint = CollisionDetection.pointInMeter(pool.ballArray[0], cane);
		force.set(1f, 1f);

		switch (clickCnt) {
		case 0:
			cane.setPosition(mousePosition);
			cane.setAngle(angle);
			break;
		case 1:				
			cane.setPosition(mousePosition);
			if (collisionPoint != null) {
				float lenght = cane.getVelocity().len() / 3;
				force.setLength(lenght);
				force.setAngle(cane.getVelocity().angle());
				if (!Double.isNaN(force.len()))
					pool.ballArray[0].applyBodyForce(force, collisionPoint, CreateLwjglApplication);
				clickCnt = 0;
				return true;
				}	
			break;
		case 2:
			waitPress = true;
			if (isPressed) {
				float newPosX;
				float newPosY;
				hasBeenPressed = true;

				if (forceCane <= 200) {
					forceCane++;
					forceScaleWidth = 2 * forceCane;
					newPosX = (float) (cane.position.x - Math.cos(Math.toRadians(cane.angle + 90)) * 2);
					newPosY = (float) (cane.position.y - Math.sin(Math.toRadians(cane.angle + 90)) * 2);
					cane.setPosition(new Vector2(newPosX, newPosY));
				}
			}
			if (!isPressed && hasBeenPressed) {
				System.out.println(forceCane);
				float newPosX;
				float newPosY;

				forceScaleWidth = 1;
				newPosX = (float) (cane.position.x + Math.cos(Math.toRadians(cane.angle + 90)) * forceCane / 2);
				newPosY = (float) (cane.position.y + Math.sin(Math.toRadians(cane.angle + 90)) * forceCane / 2);
				cane.setPosition(new Vector2(newPosX, newPosY));
				if (collisionPoint != null) {
					float lenght = cane.getVelocity().len() / 3;
					force.setLength(lenght);
					force.setAngle(cane.getVelocity().angle());
					if (!Double.isNaN(force.len()))
						pool.ballArray[0].applyBodyForce(force, collisionPoint, CreateLwjglApplication);
					clickCnt = 0;
					forceCane = 0;
					hasBeenPressed = false;
					collisionDetectedCane = true;
					return true;
				}
			}
			break;
		default:
			clickCnt = 2;
			break;
		}
		return false;
	}

	public void onKeyDown(int input) {
		if (input == Input.Keys.SPACE && waitPress)
			isPressed = true;
	}

	public void onKeyUp(int input) {
		if (input == Input.Keys.SPACE)
			isPressed = false;
		if (input == Input.Keys.NUM_1)
			pNow.skin = 0;
		if (input == Input.Keys.NUM_2)
			pNow.skin = 1;
		if (input == Input.Keys.NUM_3)
			pNow.skin = 2;
		if (input == Input.Keys.NUM_4)
			pNow.skin = 3;
		if (input == Input.Keys.NUM_5)
			pNow.skin = 4;
	}

	boolean roundEnded() {
		for (PhysicsCircle c : pool.ballArray) {
			if (c.getBodyLinearVelocity().len() > 0.001f)
				return false;
		}
		return true;
	}

	void waitForSomething() {

		checkBallInHole();

		if (roundEnded()) {
			clickCnt = 0;
			boolean didFault = checkForFault();

			if (stateNow != State.Lose)
				stateNow = State.Play;

			if (gameMode == Mode.Place)
				stateNow = State.Place;

			if (!pNow.ballsInTmp.isEmpty() && pNow.playerType == null) {
				stateNow = State.Play;
				int firstIn = pNow.ballsInTmp.firstElement();
				if (firstIn == 0 && pNow.ballsInTmp.size() > 1)
					firstIn = pNow.ballsInTmp.elementAt(1);

				if (isStriped(firstIn)) {
					pNow.playerType = Player.BallType.Striped;
					pOther.playerType = Player.BallType.Solid;
					return;
				}
				if (isSolid(firstIn)) {
					pNow.playerType = Player.BallType.Solid;
					pOther.playerType = Player.BallType.Striped;
					return;
				}
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

		if (gameMode == Mode.Place) {
			return true;
		}

		if (pool.collisionList.isEmpty()) { // Si on touche rien
			gameMode = Mode.Double;
			return true;

		} else {

			int[] firstCollision = pool.collisionList.firstElement();

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

		if (!pNow.ballsInTmp.isEmpty()) {

			for (int ballIn : pNow.ballsInTmp) {
				if (ballIn == 8 && pNow.score < 7) {
					stateNow = State.Lose;
					return true;
				}
				if (isStriped(ballIn) && pNow.playerType == Player.BallType.Solid) {
					if (gameMode == Mode.Normal)
						gameMode = Mode.Double;
					return true;
				}
				if (isSolid(ballIn) && pNow.playerType == Player.BallType.Striped) {
					if (gameMode == Mode.Normal)
						gameMode = Mode.Double;
					return true;
				}
			}
		}

		return false;
	}

	void checkBallInHole() {
		if (!pool.collisionList.isEmpty()) {
			if (pool.collisionList.lastElement()[0] == 0) // Balle blanche dans trou
			{
				if (pool.collisionList.lastElement()[1] >= 20) {
					pool.ballArray[0].setBodyLinearVelocity(0, 0);
					pool.ballArray[0].destroy();
					pool.ballArray[0].isInHole = true;
					gameMode = Mode.Place;
					pool.collisionList.remove(pool.collisionList.size() - 1);
					return;
				}
			}

			for (int i = 20; i < 26; i++) {
				if (pool.collisionList.lastElement()[0] == i) // Balle normale dans trou
				{
					pNow.ballsInTmp.add(pool.collisionList.lastElement()[1]);
					pNow.ballsInAll.add(pool.collisionList.lastElement()[1]);
					pool.ballArray[pool.collisionList.lastElement()[1]].setBodyLinearVelocity(0, 0);
					pool.ballArray[pool.collisionList.lastElement()[1]].destroy();
					pool.ballArray[pool.collisionList.lastElement()[1]].isInHole = true;
					pool.collisionList.remove(pool.collisionList.size() - 1);
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
		out += pool.debugCollisionList() + "\n";
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

		int titleConst = (int) (775f / 1080f * screenSize.height);
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
			if (ball == 0)
				continue;
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
			if (ball == 0)
				continue;
			g.draw(balls.sprites[0][ball], rightConst + 5 + collumn * 60, titleConst - 260 - line * 60, 45, 45);
			collumn++;
			if (collumn == 3) {
				collumn = 0;
				line++;
			}
		}
	}

	void setPlayerScore() {
		int cnt = 0;
		if (pNow.playerType == Player.BallType.Solid) {
			for (int i = 1; i < 8; i++) {
				if (pool.ballArray[i].isInHole)
					cnt++;
			}
		}
		if (pNow.playerType == Player.BallType.Striped) {
			for (int i = 9; i < 16; i++) {
				if (pool.ballArray[i].isInHole)
					cnt++;
			}
		}
		pNow.score = cnt;
		if (pNow.score == 7) {
			pNow.allIn = true;
		}
	}

	void drawCane(GdxGraphics g) {
		int spriteWidth = 25;
		double spriteAngle = Math.toDegrees(Math.atan(((double) spriteWidth) / ((double) cane.lenght)));
		g.draw(cues.sprites[pNow.skin][0],
				(float) (cane.position.x + Math.sin(Math.toRadians(-spriteAngle - cane.angle)) * (cane.lenght / 2)),
				(float) (cane.position.y + Math.cos(Math.toRadians(-spriteAngle - cane.angle)) * (cane.lenght / 2)), 0,
				0, 600, spriteWidth, 1, 1, cane.angle - 90);
	}

}
