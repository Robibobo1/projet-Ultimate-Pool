import java.util.Vector;

public class Player {
	
	static enum BallType {
		Solid, Striped
	}
	
	BallType playerType = null;
	
	int number;
	int score = 0;
	Vector<Integer> ballsInTmp = new Vector<Integer>();
	Vector<Integer> ballsInAll = new Vector<Integer>();
	
	Player(int number){
		this.number = number;
	}
	
	void ballIn(int ballNbr)
	{
		ballsInTmp.add(ballNbr);
	}
	
	void restartPlayer()
	{
		ballsInTmp.clear();
		ballsInAll.clear();
	}
	
	String debugBall()
	{
		String out = "";
		for(int ball : ballsInAll)
		{
			out+= ball;
			out+= " ";
		}
		return out;
	}
}
