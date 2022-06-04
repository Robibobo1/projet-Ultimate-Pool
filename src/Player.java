import java.util.Vector;

public class Player {
	
	static enum BallType {
		Solid, Striped
	}
	
	BallType playerType = null;
	
	int number;
	int score = 0;
	Vector<Integer> ballsIn = new Vector<Integer>();
	
	Player(int number){
		this.number = number;
	}
	
	void ballIn(int ballNbr)
	{
		ballsIn.add(ballNbr);
	}
	
	void restartPlayer()
	{
		ballsIn.clear();
	}
}
