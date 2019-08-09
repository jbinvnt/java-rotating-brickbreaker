import java.util.*;

import javafx.application.*;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.transform.*;
import javafx.scene.text.*;
import javafx.scene.paint.*;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;

public class MainGame extends Application{
	public static final int BOARD_WIDTH = 720;
	public static final int BOARD_HEIGHT = 720;
	public static final int MOVE_SPEED = 10; //pixels moved per 10ms
	public static final int BLOCK_NUM = 8; //the number of blocks in each stack and the number of stacks total
	public static final int FONT_SIZE = 30;
	public static final int NUM_LIVES = 3;
	private static final Color BACKGROUND_COLOR = Color.rgb(255, 255, 255);
	
	private static boolean isWon = false;
	private static int stacksCleared = 0;
	private static String playerName;
	private static Scanner consoleInput = new Scanner(System.in);
	private static Font mainFont = new Font(FONT_SIZE);
	//private static FontMetrics met = Toolkit.getToolkit().getFontLoader().getFontMetrics(mainFont); //setup for finding the width of onscreen text //FontMetrics deprecated
	private static boolean triggerRotate = false;
	private static boolean allowRotate = false;
	private static Scene scene;
	
	private Group root = new Group();
	private Group blocks = new Group();
	private Paddle player = new Paddle(BOARD_WIDTH/2, BOARD_HEIGHT-100, 150, 40);
	private Ball ball; //the ball is initialized along with the blocks
	private static Timer tr = new Timer();
	private TimerTask leftTask;
	private TimerTask rightTask;
	private Text mainMessage;
	private boolean initialized = false;
	private BlockStack[] stacks;
	private Ball[] lives;
	private int currentLives;

	public void start(Stage stage){
		scene = new Scene(root, BOARD_WIDTH, BOARD_HEIGHT, BACKGROUND_COLOR);
		lives = new Ball[NUM_LIVES];
		currentLives = NUM_LIVES;
		setMessage("Press Space to begin");
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() { //setup for keyboard shortcuts
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                	case LEFT:  moveLeft(true); break; //starts moving the paddle left
                    case RIGHT: moveRight(true); break; //starts moving the paddle right
                    case SPACE: onSpace(); break; //executes functions specific to the space bar (initialize game, use up a life)
                    case TAB: ball.toggleAI(); break; //toggles the AI feature
                    case S: ball.toggleExtraSpeed(); break; //toggles extra speed for the ball
                    case R: allowRotate = !allowRotate; break; //toggles the rotate on collision feature
                }
            }
		});
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() { //ends the player's movement if left or right is released
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                	case LEFT:  moveLeft(false); break; //stops moving the paddle left
                    case RIGHT: moveRight(false); break; //stops moving the paddle right
                }
            }
		});
		stage.setTitle("Breakout");
		stage.setScene(scene);
        stage.show();
	}
	public static void main(String[] args) {
		System.out.println("Breakout Game - Joseph Black");
		System.out.println("Move with left and right arrow keys. Press tab to toggle AI, press S to toggle extra speed.");
		System.out.println("You have three extra lives. Press space to use up a life and resume playing if you lose the ball.");
		System.out.print("Choose a player name: ");
		playerName = consoleInput.nextLine();
		launch(args);
	}
	public static void rotate(){
		if(allowRotate){
			triggerRotate = true;
			//removed feature making background change red upon rotate
		}
	}
	public void moveLeft(boolean start){ //method to start or end the task that moves the player left
		if(start){
			try{ //needed because the system repeats keys if held down
				tr.schedule(leftTask, 10, 10);
			}
			catch(Exception e){
				
			}
		}
		else{
			leftTask.cancel();
			initializeLeft();
		}
	}
	public void moveRight(boolean start){ //method to start or end the task that moves the player right
		if(start){
			try{ //needed because the system repeats keys if held down
				tr.schedule(rightTask, 10, 10);
			}
			catch(Exception e){
				
			}
		}
		else{
			rightTask.cancel();
			initializeRight();
		}
	}
	public void initializeBall(){
		tr.schedule(new TimerTask(){
			public void run(){
				Platform.runLater(new Runnable(){
					public void run(){
						ball.move(player);
						updateBlocks(); //sets up the timer to move the ball every 10ms
						if(triggerRotate){
							triggerRotate = false;
							root.getTransforms().add(new Rotate(90, BOARD_WIDTH/2, BOARD_HEIGHT/2));
							//removed a feature that made the background color change back to normal after rotating
						}
					}
				});
			}
		}, 10, 10);
	}
	public void onSpace(){
		if(!initialized){
			initializeGame(); //this is so everything isn't re-initialized on space press each time
		}
		else{
			ball.resetBall(); //if everything is already initialized, use up a life and reset the ball
			loseLife();
		}
	}
	public void initializeGame(){ //adds gameplay items to the screen
		setMessage("");
		initializeBlocks(BLOCK_NUM, BLOCK_NUM);
		initializeLeft();
		initializeRight();
		initializeBall();
		showLives(NUM_LIVES);
		initialized = true;
		root.getChildren().add(player);
		root.getChildren().add(ball);
		root.getChildren().add(blocks);
	}
	public void initializeLeft(){
		leftTask = new TimerTask(){
			public void run(){
				Platform.runLater(new Runnable(){
					public void run(){
						player.shiftLeft(MOVE_SPEED); //each 10ms, moves the player left as long as the key is pressed
					}
				});
			}
		};
	}
	public void initializeRight(){
		rightTask = new TimerTask(){
			public void run(){
				Platform.runLater(new Runnable(){
					public void run(){
						player.shiftRight(MOVE_SPEED); //each 10ms, moves the player right as long as the key is pressed
					}
				});
			}
		};
	}
	public void showLives(int num){
		for(int x = 0; x < num; x++){
			lives[x] = new Ball((x+1)*25, BOARD_HEIGHT - 25, 20, 20); //positions the extra lives in a row at the bottom of the screen, slightly apart
			root.getChildren().add(lives[x]);
		}
	}
	public void initializeBlocks(int stackHeight, int stackNum){
		stacks = new BlockStack[stackNum];
		double blockWidth = BOARD_WIDTH/(stackNum+1);
		double widthSpace = blockWidth/(stackNum+1);
		double blockHeight = 0.5*BOARD_HEIGHT/(stackNum+1);
		double heightSpace = blockHeight/(stackNum+1);
		ball = new Ball(640, 550-(blockWidth/3), blockWidth/3, blockWidth/3);
		for(int x = 0; x < stackNum; x++){
			stacks[x] = new BlockStack(stackHeight);
			for(int y = 0; y < stackHeight; y++){
				stacks[x].push(new Block(blockWidth*(x) + widthSpace*(x+1), blockHeight*(y) + heightSpace*(y+1), blockWidth, blockHeight)); //positions the blocks in a grid
			}
		}
		refreshBlocks();
	}
	public void refreshBlocks(){
		blocks.getChildren().clear();
		for(BlockStack stack : stacks){
			for(Block block : stack.getAllBlocks()){
				blocks.getChildren().add(block); //adds blocks to the screen
			}
		}
	}
	public void updateBlocks(){
		for(int x = 0; x < stacks.length; x++){
			for(int y = 0; y < stacks[x].getAllBlocks().length;){
				Block currentBlock = stacks[x].getBlock(y);
				if(currentBlock.isColliding(ball)){
					if(currentBlock.isCollidingVertical(ball)) //checks which side the ball is colliding against
						ball.collide(false);
					else
						ball.collide(true);
					stacks[x].popUntil(y); //removes blocks until the current one is reached
					refreshBlocks(); //matches the content of the stacks with the content onscreen
				}
				else
					y++;
			}
			if(stacks[x].isEmpty()){
				this.stacksCleared++;
			}
		}
		if(stacksCleared >= BLOCK_NUM){
			isWon = true; //if all blocks are removed, you won the game
			endGame();
		}
		else
			stacksCleared = 0;
	}
	public void loseLife(){
		if(!isWon){
			currentLives--;
			if(currentLives < 0)
				endGame(); //if you are out of lives, you lose the game
			else
				root.getChildren().remove(lives[currentLives]); //removes one extra ball from the bottom of the screen
		}
	}
	public void setMessage(String message){
		if(mainMessage != null)
			root.getChildren().remove(mainMessage); //removes the old message
		mainMessage = new Text((BOARD_WIDTH/2)-(getTextWidth(message)/2), (BOARD_HEIGHT/2)-FONT_SIZE, message); //centers the text onscreen
		mainMessage.setFont(mainFont);
		root.getChildren().add(mainMessage);
	}
	public void endGame(){
		tr.cancel();
		if(isWon){
			setMessage("Congratulations " + playerName + ", you won the game!");
		}
		else{
			setMessage("Sorry, " + playerName + ", you lost!");
		}
	}
	public static double getTextWidth(String message) {
		Text txt = new Text(message);
		txt.setFont(mainFont);
		return txt.getLayoutBounds().getWidth();
	}
}