import java.util.*;
public class GameBoard {
	private static CustomStack[] stacks = new CustomStack[10];
	private static Scanner consoleInput = new Scanner(System.in);
	public static void main(String[] args){
		initializeStacks();
		printStacks(true);
		do{
			System.out.print("Choose a stack number to hit (1-10): ");
			int stackNum = Integer.parseInt(consoleInput.nextLine()) - 1;
			hitStack(stackNum);
		}
		while(!isWon());
	}
	public static void initializeStacks(){
		for(int x = 0; x < stacks.length; x++){
			stacks[x] = new CustomStack();
			for(int y = 0; y < 1 + (int)(Math.random() * CustomStack.getMaxSize()); y++){ //max possible blocks is 1 less than stack size to allow the player to hit it
				stacks[x].push(y);
			}
		}
	}
	public static void printStacks(boolean showPlayer){
		System.out.println();
		for(int x = 0; x < stacks.length; x++){
			if(x < 9)
				System.out.print(0); //lines up the stacks for when there are two digit numbers
			System.out.print(x+1);
			System.out.print(Arrays.toString(stacks[x].getStack()).replace(',', ' ').replace('0', ' ')); //replaces zeros and commas with spaces
			if(x == 4 && showPlayer)
				System.out.println("\t\t\t\t\t8 <--Player");
			else
				System.out.println();
		}
	}
	public static void hitStack(int stackNum){
		stacks[stackNum].push(8);
		printStacks(false);
		stacks[stackNum].pop();
		stacks[stackNum].pop(); //removes the player and removes the top piece
		printStacks(true);
		if(isWon()){
			System.out.println("You won!");
		}
	}
	public static boolean isWon(){
		for(int x = 0; x < stacks.length; x++){
			if(stacks[x].getTail() == -1){
				return true;
			}
		}
		return false;
	}
}