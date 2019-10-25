package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

import com.sun.javafx.scene.traversal.Direction;

import exceptions.UnsupportedModeException;
import mycontroller.MyAutoController.Move;
import swen30006.driving.Simulation;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.World;
import world.WorldSpatial;

public class AdjacentMovementControl {
	public State state;
	private int wallSensitivity = 1;
	
	
	public enum State {
		STOP, FORWARD, REVERSE
	}
	private Car car;
	
	public AdjacentMovementControl(Car car, State state) {
		this.car = car;
		this.state = State.STOP;
	}
	
	public void Brake(ArrayList<Move> movementList) {
		movementList.add(Move.BRAKE);
		this.state = State.STOP;
	}
	
	public void ReverseForward(Car car, ArrayList<Move> movementList) {
		movementList.add(Move.REVERSE);
		movementList.add(Move.BRAKE);
		movementList.add(Move.ACCELERATE);
	}
	
	public void ForwardReverse(Car car,ArrayList<Move> movementList) {
		movementList.add(Move.ACCELERATE);
		movementList.add(Move.BRAKE);
		movementList.add(Move.REVERSE);
	}

	public void BrakeRF(Car car,ArrayList<Move> movementList) {
		movementList.add(Move.BRAKE);
		movementList.add(Move.REVERSE);
		movementList.add(Move.BRAKE);
		movementList.add(Move.ACCELERATE);

	}
	
	public void BrakeFR(Car car,ArrayList<Move> movementList) {
		movementList.add(Move.BRAKE);
		movementList.add(Move.ACCELERATE);
		movementList.add(Move.BRAKE);
		movementList.add(Move.REVERSE);

	}
	
	public  ArrayList<Move>  nextMove (int target_x, int target_y) {
		//System.out.println("next move is:" + target_x+","+target_y);
		System.out.println("current location is: " + car.getX()+", "+(MapRecorder.MAP_HEIGHT - 1 - car.getY()));
		ArrayList<Move> movementList = new  ArrayList<Move> ();
		float current_x = car.getX();
		float current_y = MapRecorder.MAP_HEIGHT - 1 - car.getY();
		if(current_x ==target_x && current_y == target_y) {
			movementList.add(Move.BRAKE);
			this.state = State.STOP;
			return movementList;
		}
		
		
		// car facing north
		if (car.getOrientation().equals(WorldSpatial.Direction.NORTH)) {
			//in front of the car
			if (target_x == current_x && target_y == (current_y-1)){
				MoveToFront(car , movementList);
				return movementList;
			}
			// right of the car
			else if(target_x == (current_x+1) && target_y == (current_y)) {
				MoveToAdjacentRight(car,movementList);
			}
			// behind of the car
			else if(target_x == (current_x) && target_y == (current_y+1)) {
				movementList.add(Move.REVERSE);
				return movementList;
			}
			// left of the car
			else if(target_x == (current_x-1) && target_y == (current_y)) {
				MoveToAdjacentLeft(car, movementList);
			}
		}
		// car facing east
		else if (car.getOrientation().equals(WorldSpatial.Direction.EAST)) {
			//in front of the car
			//System.out.println(car.getOrientation());
			if (target_x == (current_x+1) && target_y == (current_y)){

				MoveToFront(car , movementList);
			}
			// right of the car
			else if(target_x == (current_x) && target_y == (current_y+1)) {
				System.out.println("RIGHT");
				MoveToAdjacentRight(car,movementList);
			}
			// behind of the car
			else if(target_x == (current_x-1) && target_y == (current_y)) {
				System.out.println("BEHIND");
				MoveToBehind(car, movementList);
			}
			// left of the car
			else if(target_x == (current_x) && target_y == (current_y-1)) {
				System.out.println("Move to adjacent left");
				MoveToAdjacentLeft(car, movementList);
				
	
			}
		}
		// car facing south
		else if (car.getOrientation().equals(WorldSpatial.Direction.SOUTH)) {
			//in front of the car
			if (target_x == (current_x) && target_y == (current_y+1)){
				MoveToFront(car , movementList);
			}
			// right of the car
			else if(target_x == (current_x-1) && target_y == (current_y)) {
				MoveToAdjacentRight(car,movementList);
			}
			// behind of the car
			else if(target_x == (current_x) && target_y == (current_y-1)) {
				MoveToBehind(car, movementList);
			}
			// left of the car
			else if(target_x == (current_x+1) && target_y == (current_y)) {
				MoveToAdjacentLeft(car, movementList);
			}
		}
		// car facing west
		else if (car.getOrientation().equals(WorldSpatial.Direction.WEST)) {
			//in front of the car
			if (target_x == (current_x-1) && target_y == (current_y)){
				MoveToFront(car , movementList);
			}
			// right of the car
			else if(target_x == (current_x) && target_y == (current_y-1)) {
				MoveToAdjacentRight(car,movementList);
			}
			// behind of the car
			else if(target_x == (current_x+1) && target_y == (current_y)) {
				MoveToBehind(car, movementList);
			}
			// left of the car
			else if(target_x == (current_x) && target_y == (current_y+1)) {
				MoveToAdjacentLeft(car, movementList);
			}
		}
		return movementList;
	}
	public ArrayList<Move>  MoveToFront(Car car,ArrayList<Move> movementList) {

		if (this.state == State.REVERSE) {
			movementList.add(Move.BRAKE);
			movementList.add(Move.ACCELERATE);
			this.state = state.FORWARD;
			return movementList;
		}
		else if (this.state == State.FORWARD) {
			movementList.add(Move.ACCELERATE);
			this.state = State.FORWARD;
			return movementList;
		}
		else if (this.state == State.STOP) {			
			movementList.add(Move.ACCELERATE);
			this.state = State.FORWARD;
			return movementList;

		}
		return movementList;
		
		
	}
	
	public  ArrayList<Move> MoveToBehind(Car car,ArrayList<Move> movementList) {

		if (this.state == State.FORWARD) {
			movementList.add(Move.BRAKE);
			movementList.add(Move.REVERSE);
			this.state = State.REVERSE;
			return movementList;
		}
		else if (this.state == State.REVERSE) {
			movementList.add(Move.REVERSE);
			this.state = State.REVERSE;
			return movementList;
		}
		else if (this.state == State.STOP) {			
			movementList.add(Move.REVERSE);
			this.state = State.REVERSE;
			return movementList;
		}
		return movementList;
		
		
	}
	
	public ArrayList<Move> MoveToAdjacentLeft(Car car,ArrayList<Move> movementList) {
		HashMap<Coordinate, MapTile> currentView = this.car.getView();
		WorldSpatial.Direction orientation =  this.car.getOrientation();
		
		if (this.state == State.STOP) {
			if(this.checkWallAhead(orientation, currentView)) {
				ReverseForward(car, movementList);
				movementList.add(Move.LEFT);
				this.state = State.FORWARD;
				return movementList;
			} else if(this.checkWallBehind(orientation, currentView)){
				ForwardReverse(car, movementList);
				movementList.add(Move.LEFT);
				this.state = State.REVERSE;
				return movementList;
			} else {
				ReverseForward(car, movementList);
				movementList.add(Move.LEFT);
				this.state = State.FORWARD;
				return movementList;
			}
	
		}
		else if (this.state == State.FORWARD) {
			if(this.checkWallAhead(orientation, currentView)) {
				movementList.add(Move.LEFT);
				this.state = State.FORWARD;
				return movementList;
			} else {
				movementList.add(Move.LEFT);
				this.state = State.FORWARD;
				return movementList;
			}
		}
		else if (this.state  == State.REVERSE) {
			if(this.checkWallBehind(orientation, currentView)) {
				
				movementList.add(Move.LEFT);
				this.state = State.REVERSE;
				return movementList;
			} else {
				BrakeFR(car, movementList);
				movementList.add(Move.LEFT);
				this.state = State.REVERSE;
				return movementList;
			}
		}
		return movementList;
		
	}
	
	public ArrayList<Move> MoveToAdjacentRight(Car car,ArrayList<Move> movementList) {
		HashMap<Coordinate, MapTile> currentView = this.car.getView();
		WorldSpatial.Direction orientation =  this.car.getOrientation();
		if (this.state == State.STOP) {
			if(this.checkWallAhead(orientation, currentView)) {
				ReverseForward(car, movementList);
				movementList.add(Move.RIGHT);
				this.state = State.FORWARD;
				return movementList;
			} else if(this.checkWallBehind(orientation, currentView)){
				ForwardReverse(car, movementList);
				movementList.add(Move.RIGHT);
				this.state = State.REVERSE;
				return movementList;
			} else {
				ReverseForward(car, movementList);
				movementList.add(Move.RIGHT);;
				this.state = State.FORWARD;
				return movementList;
			}
	
		}
		else if (this.state == State.FORWARD) {
			if(this.checkWallAhead(orientation, currentView)) {
			
				movementList.add(Move.RIGHT);
				this.state = State.FORWARD;
				return movementList;
			} else {
				
				movementList.add(Move.RIGHT);
				this.state = State.FORWARD;
				return movementList;
			}
		}
		else if(this.state  == State.REVERSE) {
			if(this.checkWallBehind(orientation, currentView)) {
		
				movementList.add(Move.RIGHT);;
				this.state = State.REVERSE;
				return movementList;
			} else {
				
				movementList.add(Move.RIGHT);
				this.state = State.REVERSE;
				return movementList;
			}
		}
		return movementList;
		
	}
	
	/**
	 * Check if you have a wall in front of you!
	 * @param orientation the orientation we are in based on WorldSpatial
	 * @param currentView what the car can currently see
	 * @return
	 */
	private boolean checkWallAhead(WorldSpatial.Direction orientation, HashMap<Coordinate, MapTile> currentView){
		switch(orientation){
		case EAST:
			return checkEast(currentView);
		case NORTH:
			return checkNorth(currentView);
		case SOUTH:
			return checkSouth(currentView);
		case WEST:
			return checkWest(currentView);
		default:
			return false;
		}
	}
	
	private boolean checkWallBehind(WorldSpatial.Direction orientation, HashMap<Coordinate, MapTile> currentView){
		switch(orientation){
		case EAST:
			return checkWest(currentView);
		case NORTH:
			return checkSouth(currentView);
		case SOUTH:
			return checkNorth(currentView);
		case WEST:
			return checkEast(currentView);
		default:
			return false;
		}
	}
	
	
	/**
	 * Check if the wall is on your left hand side given your orientation
	 * @param orientation
	 * @param currentView
	 * @return
	 */
	private boolean checkFollowingWall(WorldSpatial.Direction orientation, HashMap<Coordinate, MapTile> currentView) {
		
		switch(orientation){
		case EAST:
			return checkNorth(currentView);
		case NORTH:
			return checkWest(currentView);
		case SOUTH:
			return checkEast(currentView);
		case WEST:
			return checkSouth(currentView);
		default:
			return false;
		}	
	}
	
	/**
	 * Method below just iterates through the list and check in the correct coordinates.
	 * i.e. Given your current position is 10,10
	 * checkEast will check up to wallSensitivity amount of tiles to the right.
	 * checkWest will check up to wallSensitivity amount of tiles to the left.
	 * checkNorth will check up to wallSensitivity amount of tiles to the top.
	 * checkSouth will check up to wallSensitivity amount of tiles below.
	 */
	public boolean checkEast(HashMap<Coordinate, MapTile> currentView){
		// Check tiles to my right
		Coordinate currentPosition = new Coordinate(this.car.getPosition());
		for(int i = 0; i <= wallSensitivity; i++){
			MapTile tile = currentView.get(new Coordinate(currentPosition.x+i, currentPosition.y));
			if(tile.isType(MapTile.Type.WALL)){
				return true;
			}
		}
		return false;
	}
	
	public boolean checkWest(HashMap<Coordinate,MapTile> currentView){
		// Check tiles to my left
		Coordinate currentPosition = new Coordinate(this.car.getPosition());
		for(int i = 0; i <= wallSensitivity; i++){
			MapTile tile = currentView.get(new Coordinate(currentPosition.x-i, currentPosition.y));
			if(tile.isType(MapTile.Type.WALL)){
				return true;
			}
		}
		return false;
	}
	
	public boolean checkNorth(HashMap<Coordinate,MapTile> currentView){
		// Check tiles to towards the top
		Coordinate currentPosition = new Coordinate(this.car.getPosition());
		for(int i = 0; i <= wallSensitivity; i++){
			MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y+i));
			if(tile.isType(MapTile.Type.WALL)){
				return true;
			}
		}
		return false;
	}
	
	public boolean checkSouth(HashMap<Coordinate,MapTile> currentView){
		// Check tiles towards the bottom
		Coordinate currentPosition = new Coordinate(this.car.getPosition());
		for(int i = 0; i <= wallSensitivity; i++){
			MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y-i));
			if(tile.isType(MapTile.Type.WALL)){
				return true;
			}
		}
		return false;
	}

}
