public class CustomStack {
	private int[] ds;
	private static int maxSize = 8;
	private int tail = -1;
	public CustomStack(){
		ds = new int[maxSize];
	}
	public int[] getStack(){
		return ds;
	}
	public static int getMaxSize(){
		return maxSize;
	}
	public int getTail(){
		return tail;
	}
	public int push(int numToAdd){
		if(tail == (ds.length - 1)){ //checks if stack is full
			System.out.println("Stack is full!");
			return 0;
		}
		tail++;
		ds[tail] = numToAdd;
		return numToAdd;
	}
	public int pop(){
		int top = ds[tail];
		ds[tail] = 0;
		tail--;
		return top;
	}
	public int peek(){
		return ds[tail];
	}
}
