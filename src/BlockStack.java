public class BlockStack{
	private Block[] ds;
	private int tail = -1;
	public BlockStack(int size){
		ds = new Block[size];
	}
	public Block push(Block blockToAdd){ //adds a new block to the top of the stack
		if(tail == (ds.length - 1)){ //checks if stack is full
			System.out.println("Stack is full!");
			return ds[tail];
		}
		tail++;
		ds[tail] = blockToAdd;
		return blockToAdd;
	}
	public Block pop(){
		Block top = ds[tail];
		tail--;
		return top;
	}
	public void popUntil(int firstIndex){ //removes from the top of the stack until a certain index becomes the tail
		while(tail > firstIndex)
			pop();
	}
	public Block peek(){
		return ds[tail];
	}
	public Block getBlock(int index){ //returns individual block from the stack
		return ds[index];
	}
	public Block[] getAllBlocks(){ //returns array of blocks contained in the stack
		Block[] arr = new Block[tail];
		for(int x = 0; x < tail; x++)
			arr[x] = ds[x];
		return arr;
	}
	public int getMaxSize(){ //returns maximum size of the stack
		return ds.length;
	}
	public boolean isEmpty(){ //checks if all blocks have been removed from the stack
		return tail <= 0;
	}
}
