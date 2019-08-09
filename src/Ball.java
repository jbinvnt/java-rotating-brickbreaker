import javafx.scene.paint.*;
public class Ball extends Block{
	private static final Paint col = Color.rgb(255,0,0);
	private static final double EXTRA_SPEED_FACTOR = 10;
	private static final double SLIGHT_CHANGE_MARGIN = 0.125; //the percent change in speed each collision (expressed as a decimal)
	private static final double VX_START = 3;
	private static final double VY_START = -3;
	private double vX;
	private double vY;
	private boolean enableAI = false;
	private boolean extraSpeed = false;
	private boolean isColliding = false;
	public Ball(double x, double y, double width, double height){
		super(x, y, width, height);
		this.setArcWidth(width);
		this.setArcHeight(height);
		this.setFill(col);
		vX = 3;
		vY = -3;
	}
	public void move(Paddle paddle){ //called every 10ms, moves the ball and checks for collisions with the paddle or edges of screen
		if(isColliding) //do not keep moving if ball is in the process of colliding
			return;
		double newX = this.getX() + vX;
		double newY = this.getY() + vY;
		this.setX(newX);
		this.setY(newY);
		if(enableAI)
			paddle.setX(newX + (this.getWidth()/2) - (paddle.getWidth()/2)); //moves the middle of the paddle to the middle of the ball
		if(newY <= 0)
			collide(true);
		if(newX <= 0 || newX >= MainGame.BOARD_WIDTH - this.getWidth())
			collide(false);
		if(paddle.isColliding(this)){
			MainGame.rotate(); //rotate the board 90 degrees, only works if ALLOW_ROTATE is true in MainGame.java
			if(paddle.isCollidingVertical(this)){ //only if ball is colliding with either side of the paddle
				collide(false);
				this.setY(MainGame.BOARD_HEIGHT-100-this.getHeight());
			}
			else
				collide(true);
		}
	}
	public void forceMove(){
		double newX = this.getX() - vX; //reverses the move right before a collision to prevent the ball getting stuck
		double newY = this.getY() - vY; //reverses the move right before a collision to prevent the ball getting stuck
		this.setX(newX);
		this.setY(newY);
	}
	public void collide(boolean horizontal){
		isColliding = true;
		forceMove();
		double origVx = Math.abs(VX_START);
		double origVy = Math.abs(VY_START);
		if(extraSpeed){
			origVx *= EXTRA_SPEED_FACTOR;
			origVy *= EXTRA_SPEED_FACTOR;
		}
		double slightChange;
		int changeType = (int)(Math.random()*4); //randomly chooses how the speed (and thus angle) will change
		switch(changeType){
			case 0:
				slightChange = 1-SLIGHT_CHANGE_MARGIN; //the speed will decrease by the margin
			break;
			case 1:
				slightChange = 1/(1-SLIGHT_CHANGE_MARGIN); //the speed will increase by the margin
				break;
			case 2:
				slightChange = 1; //the speed will stay the same
				break;
			default: //replaces "case 3" to make the compiler happy
				slightChange = 0; //this signals that the speed will revert to normal
				break;
		}
		if(slightChange == 0){ //the speed gets reset
			if(horizontal){
				vY = origVy*getSign(vY)*-1;
			}
			else{
				vX = origVx*getSign(vX)*-1;
			}
		}
		else{ //the speed gets changed
			if(horizontal){
				vY *= -1 * slightChange;
			}
			else{
				vX *= -1 * slightChange;
			}
		}
		isColliding = false;
	}
	public void toggleAI(){
		enableAI = !enableAI;
	}
	public void toggleExtraSpeed(){
		if(extraSpeed){
			vX /= EXTRA_SPEED_FACTOR;
			vY /= EXTRA_SPEED_FACTOR;
			extraSpeed = false;
		}
		else{
			vX *= EXTRA_SPEED_FACTOR;
			vY *= EXTRA_SPEED_FACTOR;
			extraSpeed = true;
		}
	}
	public void resetBall(){
		setX(640);
		setY(500);
		setVx(VX_START);
		setVy(VY_START);
		if(extraSpeed)
			extraSpeed = false;
	}
	public void setVx(double x){
		vX = x;
	}
	public void setVy(double y){
		vY = y;
	}
	public double getVx(){
		return vX;
	}
	public double getVy(){
		return vY;
	}
	public boolean checkNumRebounds(double origNum, double newNum, int num){ //Method not needed anymore, was used to detect when speed should be reset
		return (origNum-newNum)/((origNum+newNum)/2) < num*SLIGHT_CHANGE_MARGIN; //returns true if the percent difference is less than a certain number times the margin
	}
	public int getSign(double num){ //returns 1 or -1 based on the sign of a number
		return (int)(num/Math.abs(num));
	}
}
