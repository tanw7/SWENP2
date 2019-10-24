package mycontroller;

import controller.CarController;
import mycontroller.AStar.Node;
import world.Car;
import world.World;

import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class MyAutoController extends CarController{		
		// How many minimum units the wall is away from the player.
		private int wallSensitivity = 1;
		private StrategyFactory strategyFactory = StrategyFactory.getInstance();
		private boolean isFollowingWall = false; // This is set to true when the car starts sticking to a wall.
		
		// Car Speed to move at
		private final int CAR_MAX_SPEED = 1;
		
		private AStar aStar;
		private MapRecorder map;
		
		
		//SubjectBase
		protected ArrayList<ControllerListener> observers;
		public void addObserver(ControllerListener observer) {
			observers.add(observer);
		}
		
		public void removeObserver(ControllerListener observer) {
			observers.remove(observer);
		}
		
		public void publishPropertyEvent(String source, String name, String value) {
				for(ControllerListener observer: this.observers) {
					observer.onPropertyEvent(source,name,value);
				}
		}
		
		
		public MyAutoController(Car car) {
			super(car);
			this.observers = new ArrayList<ControllerListener>();
			map = new MapRecorder(this.getMap());
			System.out.println(this.getPosition());
			String[] coordinates = this.getPosition().split(",");
			String x = coordinates[0];
			String y = coordinates[1];
			aStar = new AStar(map.maze, Integer.parseInt(x), Integer.parseInt(y), false);
			List<Node> path = aStar.findPathTo(5, 1);
			
			
			if (path != null) {
	            path.forEach((n) -> {
	                System.out.print("[" + n.x + ", " + n.y + "] ");
	                map.maze[n.y][n.x] = -1;
	            });
	            System.out.printf("\nTotal cost: %.02f\n", path.get(path.size() - 1).g);
			}
		}
		
		
		// Coordinate initialGuess;
		// boolean notSouth = true;
		@Override
		public void update() {
			//strategyFactory.getStrategy().action(); // choose the appropriate strategy, needs an input TBD
			//publishPropertyEvent("MyAutoController", "mapping", "some value"); //update the map with recent view
			//System.out.println(this.getPosition());
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
			Coordinate currentPosition = new Coordinate(getPosition());
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
			Coordinate currentPosition = new Coordinate(getPosition());
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
			Coordinate currentPosition = new Coordinate(getPosition());
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
			Coordinate currentPosition = new Coordinate(getPosition());
			for(int i = 0; i <= wallSensitivity; i++){
				MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y-i));
				if(tile.isType(MapTile.Type.WALL)){
					return true;
				}
			}
			return false;
		}
		
	}
