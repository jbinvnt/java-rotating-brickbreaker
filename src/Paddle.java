import javafx.scene.paint.*;
public class Paddle extends Block{
	private static final Paint col = Color.rgb(0,0,0);
	public Paddle(double x, double y, double width, double height){
		super(x, y, width, height);
		this.setFill(col);
		this.setArcHeight(height);
		this.setArcWidth(height);
	}
	public void shiftLeft(int distance){
		if(this.getX() > 0)
			this.setX(this.getX() - distance);
	}
	public void shiftRight(int distance){
		if(this.getX() < MainGame.BOARD_WIDTH - this.getWidth())
			this.setX(this.getX() + distance);
	}
}
