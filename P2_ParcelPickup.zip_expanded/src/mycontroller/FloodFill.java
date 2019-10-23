package mycontroller;



import world.Car;

public class FloodFill implements IControlStrategy{
	public enum Direction { EAST, WEST, SOUTH, NORTH}
	
	public void action(Direction direction, Car car) {
		if (car.getOrientation().equals(Direction.EAST)) {
			
			switch(direction){
			case EAST:
				car.applyForwardAcceleration();
				break;
			case NORTH:
				car.turnLeft();
			case SOUTH:
				car.turnRight();
			case WEST:
				car.applyReverseAcceleration();
				car.applyReverseAcceleration();
				break;
			}	
		}else if(car.getOrientation().equals(Direction.WEST)) {
			switch(direction){
			case EAST:
				
				car.applyReverseAcceleration();
				car.applyReverseAcceleration();				
				break;
			case NORTH:
				car.turnLeft();
			case SOUTH:
				car.turnRight();
			case WEST:
				car.applyForwardAcceleration();
				break;
			}
		}
		else if(car.getOrientation().equals(Direction.NORTH)) {
			switch(direction){
			case EAST:
				car.turnRight();		
				break;
			case NORTH:
				car.applyForwardAcceleration();
				
			case SOUTH:
				car.applyReverseAcceleration();
				car.applyReverseAcceleration();	
				
			case WEST:
				car.turnLeft();
				break;
			}
		}
		
		
		else if(car.getOrientation().equals(Direction.SOUTH)) {
			switch(direction){
			case EAST:
				car.turnRight();			
				break;
			case NORTH:
				car.applyReverseAcceleration();
				car.applyReverseAcceleration();
			case SOUTH:
				car.applyForwardAcceleration();
			case WEST:
				car.turnLeft();
				break;
			}
		}
			
	}
	
	static void floodFillUtil(int screen[][], int x, int y,  
            int prevC, int newC, int maxWidth, int maxHeight, Car car, Direction direction) 
	{ 
	// Base cases 
	if (x < 0 || x >= maxWidth|| y < 0 || y >= maxHeight) {
		return; 
	}
	if (screen[x][y] != prevC) {
		return; 
	}
	// Replace the color at (x, y) ie visited 
	screen[x][y] = newC; 
	
	// Recur for north, east, south and west 
	if(car.getVelocity() > 0 ) {
		floodFillUtil(screen, x+1, y, prevC, newC, maxWidth , maxHeight,car,Direction.EAST); 
		floodFillUtil(screen, x-1, y, prevC, newC, maxWidth , maxHeight,car,Direction.WEST); 
		floodFillUtil(screen, x, y+1, prevC, newC, maxWidth , maxHeight,car,Direction.NORTH); 
		floodFillUtil(screen, x, y-1, prevC, newC, maxWidth , maxHeight,car,Direction.SOUTH); 
	}
	if(car.getVelocity() == 0 ) {
		if(car.getOrientation().equals(Direction.NORTH) || car.getOrientation().equals(Direction.WEST) ) {
			floodFillUtil(screen, x, y+1, prevC, newC, maxWidth , maxHeight,car,Direction.NORTH); 
			floodFillUtil(screen, x, y-1, prevC, newC, maxWidth , maxHeight,car,Direction.SOUTH); 
		}else if(car.getOrientation().equals(Direction.SOUTH) || car.getOrientation().equals(Direction.EAST) ) {
			floodFillUtil(screen, x+1, y, prevC, newC, maxWidth , maxHeight,car,Direction.EAST); 
			floodFillUtil(screen, x-1, y, prevC, newC, maxWidth , maxHeight,car,Direction.WEST); 
		}
	}
	
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	} 
}
