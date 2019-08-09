import javafx.scene.shape.*;
import javafx.scene.paint.*;
public class Block extends Rectangle{
	private static final Paint col = Color.rgb(0,255,0);
	public Block(double x, double y, double width, double height){
		this.setWidth(width);
		this.setHeight(height);
		this.setX(x);
		this.setY(y);
		this.setFill(col); //ZAP! Accessed in a "static" way. Happy now, compiler?
	}
	public boolean checkCoordinates(Ball ball){ //deprecated, checks whether the coordinates of the ball fall within the current block
		double x = ball.getX();
		double y = ball.getY();
		return checkCoordinates(x, y);
	}
	public boolean checkCoordinates(double x, double y){ //deprecated, checks whether coordinates fall within the current block
		return (x >= this.getX() && x <= this.getX() + this.getWidth()) && (y >= this.getY() && y <= this.getY() + this.getHeight()); //returns true if the x and y fall within the bounds of the paddle
	}
	public boolean isColliding(Block block){ //checks whether another block is colliding at all with the current block
		double xMinT = block.getX();
		double xMaxT = xMinT + block.getWidth();
		double yMinT = block.getY();
		double yMaxT = yMinT + block.getHeight();
		double xMinS = this.getX();
		double xMaxS = xMinS + this.getWidth();
		double yMinS = this.getY();
		double yMaxS = yMinS + this.getHeight();
		return ((xMinT >= xMinS && xMinT <= xMaxS) || (xMaxT >= xMinS && xMaxT <= xMaxS)) && ((yMinT >= yMinS && yMinT <= yMaxS) || (yMaxT >= yMinS && yMaxT <= yMaxS));
	}
	public boolean isCollidingVertical(Ball block){ //the error variable is for margin of error (equal to vY of ball)
		double error = block.getVy();
		double yMinT = block.getY();
		double yMaxT = yMinT + block.getHeight();
		double yMinS = this.getY();
		double yMaxS = yMinS + this.getHeight();
		if(isColliding(block))
			return (yMinT > yMinS + error && yMinT < yMaxS + error) || (yMaxT > yMinS + error && yMaxT < yMaxS + error); //checks whether another block is colliding with the side of the current block
		return false;
	}
}
