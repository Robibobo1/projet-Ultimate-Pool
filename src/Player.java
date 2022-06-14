import java.util.Vector;

//------------------------------------------------------------------
// Player
//------------------------------------------------------------------
// Classe stockant toutes les infos sur le joueur, les balles mises,
// ses points, la couleur de son équipe et le sprite de sa canne.
//------------------------------------------------------------------
public class Player {

	static enum BallType {
		Solid, Striped
	}

	BallType playerType = null;

	int number;
	int score = 0;
	int skin = 0;
	
	boolean holeChosed = false;
	int holeChoosedNbr = 0;

	Vector<Integer> ballsInTmp = new Vector<Integer>();
	Vector<Integer> ballsInAll = new Vector<Integer>();

	// ------------------------------------------------------------------
	// Player
	// ------------------------------------------------------------------
	// Constructeur de la classe Player
	// Reçoit le numéro du joeur en paramètre
	// ------------------------------------------------------------------
	Player(int number) {
		this.number = number;
	}

	// String de debeug
	String debugBall() {
		String out = "";
		for (int ball : ballsInAll) {
			out += ball;
			out += " ";
		}
		return out;
	}
}
