import java.awt.Dimension;
import java.util.Random;
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
import ch.hevs.gdx2d.components.audio.MusicPlayer;
import ch.hevs.gdx2d.components.audio.SoundSample;
import java.awt.Point;
import ch.hevs.gdx2d.components.bitmaps.BitmapImage;
import ch.hevs.gdx2d.components.bitmaps.Spritesheet;

import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle;
import ch.hevs.gdx2d.components.physics.utils.PhysicsScreenBoundaries;
import ch.hevs.gdx2d.desktop.PortableApplication;
import ch.hevs.gdx2d.desktop.physics.DebugRenderer;
import ch.hevs.gdx2d.lib.GdxGraphics;
import ch.hevs.gdx2d.lib.physics.PhysicsWorld;

//------------------------------------------------------------------
// App
//------------------------------------------------------------------
// Classe principale du jeu, extends la classe PortableApplication
// C'est ici que tous les autres objets sont crées et que la 
// logique du jeu est décidée
//------------------------------------------------------------------
public class App extends PortableApplication {

	enum State {
		Play, Wait, Place, Choose, Win, Lose
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
	BitmapImage imgTable, yellow;
	Texture gradient;
	Spritesheet balls; // i love titi toto <3
	Spritesheet cues;

	Player pNow, pOther, p1, p2;

	BitmapFont titleFont, textFont, endFont, endLittleFont;

	Dimension screenSize;
	Vector2 ballPosition;

	SoundSample hitHard;
	SoundSample hitCane;
	SoundSample pocket;
	SoundSample hitSoft;

	// Pocket = 0
	// ball = 1
	// soft = 2
	// cane = 3

	int collisionSound = -1;

	// Point milieu des trous
	Point[] holePoint;

	// Variables booléennes pour la force de canne
	boolean waitPress = false;
	boolean isPressed = false;
	boolean hasBeenPressed = false;

	// Variables de forces pour la canne
	int forceCane = 0;
	float forceScaleWidth = 1;
	Vector2 force = new Vector2(1, 1);

	// Compteur de clic souris
	int clickCnt = 0;

	// Vecteur position de la souris
	Vector2 mousePosition;

	// ------------------------------------------------------------------
	// Constructeur de App
	// ------------------------------------------------------------------
	// Paramètres : un type Dimension contenant la taille de l'écran
	// Crée la fenêtre de portable application et instancie les joueurs
	// ------------------------------------------------------------------
	App(Dimension screenSize) {
		// Crée la fenêtre Portable Application
		super(screenSize.width, screenSize.height, false);
		this.screenSize = screenSize;
		ballPosition = new Vector2(0, 0);

		// Crée les joueurs
		p1 = new Player(1);
		p2 = new Player(2);

		// Met un skin de manière random entre les 2 joueurs
		Random rand = new Random();
		p1.skin = rand.nextInt(2) * 2;
		p2.skin = rand.nextInt(2) * 2 + 1;

		pNow = p1;
		pOther = p2;
	}

	// ------------------------------------------------------------------
	// Main
	// ------------------------------------------------------------------
	// Récupère la taille de l'écran et crée un objet App
	// ------------------------------------------------------------------
	public static void main(String[] args) {
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		if (size.width < 1920)
			size = new Dimension(1920, 1080);
		new App(size);
	}
	
	
	// ------------------------------------------------------------------
	// Init
	// ------------------------------------------------------------------
	// Appellé lors de l'initialisation de la fenêtre
	// Crée les polices d'écritures, télécharges les images et les sons
	@Override
	public void onInit() {

		FileHandle Dosis = Gdx.files.internal("data/font/Dosis.ttf");
		FileHandle Jokerman = Gdx.files.internal("data/font/Jokerman.ttf");
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Dosis);

		parameter.size = generator.scaleForPixelHeight(70);
		parameter.color = Color.BLACK;
		titleFont = generator.generateFont(parameter);

		parameter.size = generator.scaleForPixelHeight(40);
		textFont = generator.generateFont(parameter);

		parameter.size = generator.scaleForPixelHeight(100);
		parameter.color = Color.WHITE;
		generator = new FreeTypeFontGenerator(Jokerman);
		endFont = generator.generateFont(parameter);

		parameter.size = generator.scaleForPixelHeight(60);
		endLittleFont = generator.generateFont(parameter);

		world.setGravity(new Vector2(0, 0));
		World.setVelocityThreshold(0.0001f);
		dbgRenderer = new DebugRenderer();
		new PhysicsScreenBoundaries(getWindowWidth(), getWindowHeight());

		pool = new Pool(this);
		pool.createPool();
		cane = new Cane(new Vector2(300, 150), 0);

		holePoint = new Point[6];
		for (int i = 0; i < holePoint.length; i++) {
			holePoint[i] = new Point((int) pool.holesArray[i].getBodyPosition().x,
					(int) pool.holesArray[i].getBodyPosition().y);
		}

		imgSol = new Texture("data/images/Sol.png");
		gradient = new Texture("data/images/gradient.png");
		balls = new Spritesheet("data/images/Boules.png", 100, 100);
		cues = new Spritesheet("data/images/gameCues.png", 3578, 100);
		hitHard = new SoundSample("data/Sounds/hitHard.mp3");
		hitSoft = new SoundSample("data/Sounds/hitSoft.mp3");
		pocket = new SoundSample("data/Sounds/inPocket.mp3");
		hitCane = new SoundSample("data/Sounds/hitCane.mp3");
		imgTable = new BitmapImage("data/images/Table.png");
		yellow = new BitmapImage("data/images/Jaune.png");
		
		hitSoft.setVolume(0.06f);
		hitHard.setVolume(0.1f);
		hitCane.setVolume(0.08f);
		pocket.setVolume(0.07f);
	}
	
	// ------------------------------------------------------------------
	// OnGraphicrender
	// ------------------------------------------------------------------
	// Est appelé chaque 1/60 seconde ( temps d'une image pour 60Hz)
	// Toute la logique du jeu est faite dans cett efonction, tout 
	// comme le rendu graphique.
	// ------------------------------------------------------------------

	@Override
	public void onGraphicRender(GdxGraphics g) {
		// Efface le contenu de l'écran
		g.clear();
		
		// Dessines les images
		g.draw(imgSol, 0, 0, screenSize.width, screenSize.height);
		g.drawPicture(screenSize.width / 2, screenSize.height / 2, imgTable);
		g.draw(gradient, screenSize.width / 2 - 200, screenSize.height - 90, forceScaleWidth, 25, 500, 500, 0, 0); // à

		// Ecrit les infos des joueurs
		setPlayerScore();
		showGameInfo(g);

		// Met a jour la physique et la position de la souris
		PhysicsWorld.updatePhysics(Gdx.graphics.getDeltaTime());
		mousePosition = new Vector2(Gdx.input.getX(), screenSize.height - Gdx.input.getY());
		
		// Dessine les boules sur le terrain
		for (int i = 0; i < 16; i++) {
			if (!pool.ballArray[i].isInHole) {
				g.draw(balls.sprites[0][i], (float) (pool.ballArray[i].getBodyPosition().x - pool.ballRadius),
						(float) (pool.ballArray[i].getBodyPosition().y - pool.ballRadius), (float) pool.ballRadius * 2,
						(float) pool.ballRadius * 2);
			}
		}

		if (gameMode != Mode.Place)
			ballPosition = pool.ballArray[0].getBodyPosition();

		// Switch case pour le son des collisions
		switch (collisionSound) {
		case 0:
			pocket.play();
			break;
		case 1:
			hitHard.play();
			break;
		case 2:
			hitSoft.play();
			break;
		case 3:	
			hitCane.play();
			break;
		default:
			break;
		}
		collisionSound = -1;
		
		// Switch case principal de sélection de jeu
		switch (stateNow) {
		case Play: // Lorsqe le joueur joue avec la canne
			if (gameMode == Mode.Place) {
				stateNow = State.Place;
				break;
			}

			if (pNow.score == 7 && !pNow.holeChosed) {
				stateNow = State.Choose;
				clickCnt = 0;
				break;
			}

			cane.updateHitPoint();
			drawCane(g);
			if (canePlacement()) {
				pool.collisionList.clear();
				stateNow = State.Wait;
			}
			break;
		case Wait: // Quand les Balles sont en mouvement
			waitForSomething();
			break;
		case Place: // Quand le joueur doit placer la blanche
			if (clickCnt >= 1) {
				pool.placeWhite(mousePosition);
				gameMode = Mode.Normal;
				stateNow = State.Play;
				clickCnt = 0;
			}
			break;
		case Choose: // Quand le joueur choisi le trou pour la noire
			chooseHole(g);
			break;
		case Lose: // Quand le joueur à perdu
			youLoose(g);
			break;
		case Win: // Quand le jouer à gagné
			youWin(g);
			break;

		default:
			break;
		}

		g.drawFPS();
		//g.drawString(20, 200, debugGameEngine()); // String de debeug
	}
	
	// ------------------------------------------------------------------
	// OnClick
	// ---------------------------------------------------------------------
	// Quand le joueur clic avec sa souris récupère la position et le bouton
	// ---------------------------------------------------------------------
	@Override
	public void onClick(int x, int y, int button) {
		super.onClick(x, y, button);
		
		// Clic gauche
		if (button == Input.Buttons.LEFT) {
			if (!CollisionDetection.hasCollision(pool.ballArray[0], cane))
				clickCnt++;
		}
		// Clic droit
		if (button == Input.Buttons.RIGHT) {
			if (!CollisionDetection.hasCollision(pool.ballArray[0], cane))
				clickCnt--;
		}
	}
	
	// ------------------------------------------------------------------
	// canePlacement
	// ------------------------------------------------------------------
	// Méthode qui sert à gérer le placement de la canne, l'angle et la
	// force de frappe. La détection de collision entre la canne et la
	// boule blanche est aussi géré ici.
	// Retourne true lorsque la canne frappe la balle blanche
	// ------------------------------------------------------------------
	boolean canePlacement() {

		float angle = 90 + (float) Math
				.toDegrees(Math.atan((mousePosition.y - ballPosition.y) / (mousePosition.x - ballPosition.x)));
		if (mousePosition.x - ballPosition.x < 0)
			angle = angle + 180;
		force.setAngle(angle + 90);
		Vector2 collisionPoint = CollisionDetection.pointInMeter(pool.ballArray[0], cane);
		force.set(1f, 1f);

		switch (clickCnt) {
		case 0:
			// Change la position et l'angle
			cane.setPosition(mousePosition);
			cane.setAngle(angle);
			break;
		case 1:
			// Change uniquement la position
			cane.setPosition(mousePosition);
			break;
		case 2:
			// Attend la barre espace pour la force de la canne
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
					collisionSound = 3;
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

	// ------------------------------------------------------------------
	// chooseHole
	// ------------------------------------------------------------------
	// Methode appelée lorsque le joueur doit choisir l'emplacement de la 
	// balle noire. Dessine graphiquement le trou choisis dans g
	// ------------------------------------------------------------------
	void chooseHole(GdxGraphics g) {
		pNow.holeChosed = false;
		int radius = 100;
		Point mousePoint = new Point((int) mousePosition.x, (int) mousePosition.y);
		for (int i = 0; i < holePoint.length; i++) {
			if (mousePoint.distance(holePoint[i]) < radius) {
				g.drawAlphaPicture(new Vector2(holePoint[i].x, holePoint[i].y), 0.5f, yellow);
				if (clickCnt >= 1) {
					pNow.holeChoosedNbr = pool.holesArray[i].number;
					pNow.holeChosed = true;
					stateNow = State.Play;
					clickCnt = 0;
				}
			}
		}
		clickCnt = 0;
	}

	// ------------------------------------------------------------------
	// OnKeyDown
	// ------------------------------------------------------------------
	// Appelée lorsque on appuie sur une touche du clavier
	// ------------------------------------------------------------------
	public void onKeyDown(int input) {
		if (input == Input.Keys.SPACE && waitPress)
			isPressed = true;
		if (input == Input.Keys.ENTER)
			System.exit(1);
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

	// ------------------------------------------------------------------
	// OnKeyUp
	// ------------------------------------------------------------------
	// Appelée lorsque on relache une touche du clavier
	// ------------------------------------------------------------------
	public void onKeyUp(int input) {
		if (input == Input.Keys.SPACE)
			isPressed = false;	
	}

	// ------------------------------------------------------------------
	// roundEnded
	// ------------------------------------------------------------------
	// Renvoie true lorsque toutes les balles sont arrêtées
	// ------------------------------------------------------------------
	boolean roundEnded() {
		for (PhysicsCircle c : pool.ballArray) {
			if (c.getBodyLinearVelocity().len() > 0.001f)
				return false;
		}
		return true;
	}

	// ------------------------------------------------------------------
	// waitForSomething
	// ------------------------------------------------------------------
	// Méthode appelée 60x par seconde lorsque les balles sont encore
	// en mouvement. Dès que les balles sont arrêtées, on passe une seule
	// fois dans le if(Roundended) pour la détecction des faute et le 
	// changement de State.
	// ------------------------------------------------------------------
	void waitForSomething() {

		checkBallInHole();

		if (roundEnded()) {
			clickCnt = 0;
			boolean didFault = checkForFault();

			if (stateNow == State.Lose || stateNow == State.Win)
				return;
			else
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
			pNow.holeChosed = false;
			pNow.ballsInTmp.clear();
			nextPlayer();
		}
	}
	
	// ------------------------------------------------------------------
	// checkForfault
	// ------------------------------------------------------------------
	// Méthode appelée pour tester si le joueur pNow a fait une erreur.
	// Retourne true si c'est le cas.
	// Les règles du jeu sont expliquées dans le rapport
	// ------------------------------------------------------------------
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

				if (pNow.playerType == null && !pNow.ballsInTmp.contains(0))
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
				if (ballIn == 8) {
					if (pool.ballArray[8].holeNbr == pNow.holeChoosedNbr)
						stateNow = State.Win;
					else
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

	// ------------------------------------------------------------------
	// checkBallInHole
	// ------------------------------------------------------------------
	// Regarde 60x par seconde si une des balles est rentrée dans un 
	// des trous. Si c'est le cas, on place l'indexe de la balle dans
	// 2 listes, une qui est temporaire pour le round et une permanente
	// durant la partie.
	// ------------------------------------------------------------------
	void checkBallInHole() {
		if (!pool.collisionList.isEmpty()) {
			if (pool.collisionList.lastElement()[0] == 0) // Balle blanche dans trou
			{
				if (pool.collisionList.lastElement()[1] >= 20) {
					pool.ballArray[0].holeNbr = pool.collisionList.lastElement()[1];
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
					pool.ballArray[pool.collisionList.lastElement()[1]].holeNbr = pool.collisionList.lastElement()[0];
					pool.ballArray[pool.collisionList.lastElement()[1]].setBodyLinearVelocity(0, 0);
					pool.ballArray[pool.collisionList.lastElement()[1]].destroy();
					pool.ballArray[pool.collisionList.lastElement()[1]].isInHole = true;
					pool.collisionList.remove(pool.collisionList.size() - 1);
					return;
				}
			}
		}
	}

	// ------------------------------------------------------------------
	// debugGameEngine
	// ------------------------------------------------------------------
	// Méthode créant un string de debeug
	// ------------------------------------------------------------------
	String debugGameEngine() {
		String out = "";
		out += "Player " + pNow.number + " - " + pNow.playerType + " - " + pNow.score;
		out += "\nPlayer " + pOther.number + " - " + pOther.playerType + " - " + pOther.score;
		out += "\nState: " + stateNow;
		out += "\nMode: " + gameMode + "\n";
		out += pNow.holeChosed + "\n";
		out += pool.ballArray[8].holeNbr + "\n";
		out += pNow.holeChoosedNbr;
		return out;
	}
	
	// ------------------------------------------------------------------
	// NextPlayer
	// ------------------------------------------------------------------
	// La méthode change de joueur quand elle est appellée
	// ------------------------------------------------------------------
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
	
	// ------------------------------------------------------------------
	// isStriped
	// ------------------------------------------------------------------
	// Retourne true si la balle mise en paramètre est rayée
	// ------------------------------------------------------------------
	boolean isStriped(int ballNbr) {
		if (ballNbr >= 9 && ballNbr <= 15) {
			return true;
		}
		return false;
	}
	
	// ------------------------------------------------------------------
	// isSolid
	// ------------------------------------------------------------------
	// Retourne true si la balle mise en paramètre est pleine
	// ------------------------------------------------------------------
	boolean isSolid(int ballNbr) {
		if (ballNbr >= 1 && ballNbr <= 7) {
			return true;
		}
		return false;
	}

	// ------------------------------------------------------------------
	// showGameInfo
	// ------------------------------------------------------------------
	// Ecrit sur l'écran toutes les informations utiles pour les joueurs.
	// La méthode écrit sur l'interface graphique g
	// ------------------------------------------------------------------
	void showGameInfo(GdxGraphics g) {

		Color backColor = new Color(222f / 255, 183f / 255, 127f / 255, 1);

		int titleConst = (int) (775f / 1080f * screenSize.height);
		int leftConst = 58;
		int rightConst = screenSize.width - 240;

		Color pColor;
		if (gameMode == Mode.Double)
			pColor = Color.ORANGE;
		else
			pColor = Color.YELLOW;

		if (pNow.number == 1) {
			g.drawFilledRectangle(leftConst + 90, titleConst - 235, 220, 510, 0, pColor);
			g.drawFilledRectangle(rightConst + 90, titleConst - 235, 220, 510, 0, Color.BLACK);
		} else {
			g.drawFilledRectangle(leftConst + 90, titleConst - 235, 220, 510, 0, Color.BLACK);
			g.drawFilledRectangle(rightConst + 90, titleConst - 235, 220, 510, 0, pColor);
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
	}
	
	// ------------------------------------------------------------------
	// drawCane
	// ------------------------------------------------------------------
	// La méthode dessine le sprite de la canne au bon endroit sur l'écran
	// ------------------------------------------------------------------
	void drawCane(GdxGraphics g) {
		int spriteWidth = 25;
		double spriteAngle = Math.toDegrees(Math.atan(((double) spriteWidth) / ((double) cane.lenght)));
		g.draw(cues.sprites[pNow.skin][0],
				(float) (cane.position.x + Math.sin(Math.toRadians(-spriteAngle - cane.angle)) * (cane.lenght / 2)),
				(float) (cane.position.y + Math.cos(Math.toRadians(-spriteAngle - cane.angle)) * (cane.lenght / 2)), 0,
				0, 600, spriteWidth, 1, 1, cane.angle - 90);
	}

	// ------------------------------------------------------------------
	// youWin
	// ------------------------------------------------------------------
	// Dessine l'écran de victoire
	// ------------------------------------------------------------------
	void youWin(GdxGraphics g) {
		g.drawFilledRectangle(g.getScreenWidth() / 2, g.getScreenHeight() / 2, 830, 310, 0, Color.PINK);
		g.drawFilledRectangle(g.getScreenWidth() / 2, g.getScreenHeight() / 2, 820, 300, 0, Color.BLACK);
		g.drawString(g.getScreenWidth() / 2 - 365, g.getScreenHeight() / 2 + 105, "Bravo tu as gagné !", endFont);
		g.drawString(g.getScreenWidth() / 2 - 370, g.getScreenHeight() / 2 - 35,
				"Appuie sur Enter pour éteindre le jeu.", endLittleFont);
	}
	
	// ------------------------------------------------------------------
	// youLoose
	// ------------------------------------------------------------------
	// Dessine l'écran de défaite
	// ------------------------------------------------------------------
	void youLoose(GdxGraphics g) {
		g.drawFilledRectangle(g.getScreenWidth() / 2, g.getScreenHeight() / 2, 830, 310, 0, Color.RED);
		g.drawFilledRectangle(g.getScreenWidth() / 2, g.getScreenHeight() / 2, 820, 300, 0, Color.BLACK);
		g.drawString(g.getScreenWidth() / 2 - 365, g.getScreenHeight() / 2 + 105, "Tu as perdu ahah", endFont);
		g.drawString(g.getScreenWidth() / 2 - 370, g.getScreenHeight() / 2 - 35,
				"Appuie sur Enter pour éteindre le jeu.", endLittleFont);
	}

}
