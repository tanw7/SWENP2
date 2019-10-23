package mycontroller;

import java.util.HashMap;

import exceptions.UnsupportedModeException;
import swen30006.driving.Simulation;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.World;
import world.WorldSpatial;

public class MovementControl {
	public State state;
	private int wallSensitivity = 1;
	private boolean isFollowingWall = false; // This is set to true when the car starts sticking to a wall.
	
	
	public enum State {
		STOP, FORWARD, REVERSE
	}
	private Car car;
	
	public MovementControl(Car car, State state) {
		this.car = car;
		this.state = State.STOP;
	}
	
	public void ReverseForward(Car car) {
		this.car.applyReverseAcceleration();
		this.car.brake();
		this.car.applyForwardAcceleration();
	}
	
	public void ForwardReverse(Car car) {
		this.car.applyForwardAcceleration();
		this.car.brake();
		this.car.applyReverseAcceleration();
	}

	public void BrakeRF(Car car) {
		this.car.brake();
		this.car.applyReverseAcceleration();
		this.car.brake();
		this.car.applyForwardAcceleration();
	}
	
	public void BrakeFR(Car car) {
		this.car.brake();
		this.car.applyForwardAcceleration();
		this.car.brake();
		this.car.applyReverseAcceleration();
	}
	
	public void MoveToAdjcentLeft(Car car) {
		HashMap<Coordinate, MapTile> currentView = this.car.getView();
		WorldSpatial.Direction orientation =  this.car.getOrientation();
		
		if (this.state == State.STOP) {
			if(this.checkWallAhead(orientation, currentView)) {
				ReverseForward(car);
				this.car.turnLeft();
				this.state = State.FORWARD;
			} else if(this.checkWallBehind(orientation, currentView)){
				ForwardReverse(car);
				this.car.turnLeft();
				this.state = State.REVERSE;
			} else {
				ReverseForward(car);
				this.car.turnLeft();
				this.state = State.FORWARD;
			}
	
		}
		if (this.state == State.FORWARD) {
			if(this.checkWallAhead(orientation, currentView)) {
				BrakeRF(car);
				this.car.turnLeft();
				this.state = State.FORWARD;
			} else {
				BrakeRF(car);
				this.car.turnLeft();
				this.state = State.FORWARD;
			}
		}
		if(this.state  == State.REVERSE) {
			if(this.checkWallBehind(orientation, currentView)) {
				BrakeFR(car);
				this.car.turnLeft();
				this.state = State.REVERSE;
			} else {
				BrakeFR(car);
				this.car.turnLeft();
				this.state = State.REVERSE;
			}
		}
		
	}
	
	public void MoveToAdjcentRight(Car car) {
		HashMap<Coordinate, MapTile> currentView = this.car.getView();
		WorldSpatial.Direction orientation =  this.car.getOrientation();
		
		if (this.state == State.STOP) {
			if(this.checkWallAhead(orientation, currentView)) {
				ReverseForward(car);
				this.car.turnRight();
				this.state = State.FORWARD;
			} else if(this.checkWallBehind(orientation, currentView)){
				ForwardReverse(car);
				this.car.turnRight();
				this.state = State.REVERSE;
			} else {
				ReverseForward(car);
				this.car.turnRight();
				this.state = State.FORWARD;
			}
	
		}
		if (this.state == State.FORWARD) {
			if(this.checkWallAhead(orientation, currentView)) {
				BrakeRF(car);
				this.car.turnRight();
				this.state = State.FORWARD;
			} else {
				BrakeRF(car);
				this.car.turnRight();
				this.state = State.FORWARD;
			}
		}
		if(this.state  == State.REVERSE) {
			if(this.checkWallBehind(orientation, currentView)) {
				BrakeFR(car);
				this.car.turnRight();
				this.state = State.REVERSE;
			} else {
				BrakeFR(car);
				this.car.turnRight();
				this.state = State.REVERSE;
			}
		}
		
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
