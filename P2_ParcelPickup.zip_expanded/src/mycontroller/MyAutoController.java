package mycontroller;

import controller.CarController;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import mycontroller.AStar.Node;
import mycontroller.FloodFill.Direction;
import mycontroller.MyAutoController.Move;
import swen30006.driving.Simulation;
import world.Car;
import world.World;

import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;
import java.lang.*;
import java.util.List;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Path;

import java.util.ArrayList;
import java.util.Collections;

public class MyAutoController extends CarController{
		// How many minimum units the wall is away from the player.
		private int wallSensitivity = 1;
		private StrategyFactory strategyFactory = StrategyFactory.getInstance();
		private boolean isFollowingWall = false; // This is set to true when the car starts sticking to a wall.
		List<Node> path ;

		// Car Speed to move at
		private final int CAR_MAX_SPEED = 1;

		private AStar aStar;
		private MapRecorder map;
		private AdjacentMovementControl movementControl;
		private Queue<Move> queue;
		private String[] currentCoordinate;
		

		public enum Move {
			ACCELERATE, REVERSE, BRAKE, LEFT, RIGHT, SKIP
		}


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
			this.movementControl = new AdjacentMovementControl(car, AdjacentMovementControl.State.STOP);
			map = new MapRecorder(this.getMap());
			map.CarView(getView());
			//this.queue = new LinkedList<>();
		}
		
		
		private ArrayList<Move> movementQueue = new ArrayList<Move>();
		private ArrayList<Coordinate> astarQueue = new ArrayList<Coordinate>();
		
        public void addQueue(ArrayList<Move> queue, Move movement) {
            queue.add(movement);
        }
        
        public void addQueue(ArrayList<Coordinate> queue, Coordinate movement) {
            queue.add(movement);
        }

        public Move removeQueue(ArrayList<Move> queue) {
            if (queue.isEmpty()) {
                return Move.SKIP;
            }
            return queue.remove(0);
        }
        
        public Coordinate removeAstar(ArrayList<Coordinate> queue) {
        	if (queue.isEmpty()) {
        		System.out.println("<<Warning: queue should not be empty.>>");
        		return new Coordinate(0,0);
        	}
        	return queue.remove(0);
        }
        
        private int getCurrentX() {
        	currentCoordinate = this.getPosition().split(",");
			String x = currentCoordinate[0];
			return Integer.parseInt(x);
        }
        
        private int getCurrentY() {
        	currentCoordinate = this.getPosition().split(",");
			String y = currentCoordinate[0];
			return Integer.parseInt(y);
        }
        
        ArrayList<Move> movementList;
        Move nextStep;
        Coordinate nextTile;
        // Coordinate initialGuess;
        // boolean notSouth = true;
        @Override
        public void update() {
        	/* For Manual Control
            Set<Integer> parcels = Simulation.getParcels();
            Simulation.resetParcels();
            for (int k : parcels){
                 switch (k){
                    case Input.Keys.B:
                        applyBrake();
                        break;
                    case Input.Keys.UP:
                        applyForwardAcceleration();
                        break;
                    case Input.Keys.DOWN:
                        applyReverseAcceleration();
                        break;
                    case Input.Keys.LEFT:
                        turnLeft();
                        break;
                    case Input.Keys.RIGHT:
                        turnRight();
                        break;
                    default:
               }
            }
			*/
        	if (astarQueue.isEmpty()) {
        		System.out.println("A* queue is empty, proceed to get random location.");
        		aStar = new AStar(map.maze, getCurrentX(), getCurrentY(), false);
        		Coordinate randomTile = map.randomItem(map.list);
        		System.out.println("Moving randomly to:" + randomTile.x +","+ randomTile.y);
        		this.path = aStar.findPathTo(randomTile.x, randomTile.y);
    			this.path.remove(0);
    			if (this.path != null) {
    				 this.path.forEach((n) -> {
    					 //ArrayList<Move> movementList = movementControl.nextMove(n.x, n.y);
    					 //movementList.forEach((m)->{
    					 //	 System.out.println("MOVEMENT "+m);
    					 //});
    					 addQueue(astarQueue, new Coordinate(n.x, n.y));
    	                 System.out.print("[" + n.x + ", " + n.y + "] ");
    	                 map.maze[n.y][n.x] = -1;
    		            });
    		            System.out.printf("\nTotal cost: %.02f\n", path.get(path.size() - 1).g);
    			}
        	}
        	//System.out.println(astarQueue);
        	if (movementQueue.isEmpty()) {
        		nextTile = removeAstar(astarQueue);
        		System.out.println("Go to tile:" + nextTile.x + "," + nextTile.y);
        		movementList = movementControl.nextMove(nextTile.x, nextTile.y);
        		
        		for (int i = 0; i < movementList.size(); i++) {
            		addQueue(movementQueue, movementList.get(i));
            	}
        	}
        	
        	nextStep = removeQueue(movementQueue);
        	
        	
        	System.out.println("----------");
        	switch(nextStep) {
	            case ACCELERATE:
	              System.out.println("ACCELERATE");
	              applyForwardAcceleration();
	              break;
	            case REVERSE:
	               System.out.println("REVERSE");
	               applyReverseAcceleration();
	              break;
	            case BRAKE:
	              System.out.println("BRAKE");
	              applyBrake();
	              break;
	            case LEFT:
		          System.out.println("LEFT");
		          turnLeft();
		          break;
	            case RIGHT:
		          System.out.println("RIGHT");
		          turnRight();
		          break;
            }
        	map.CarView(getView());
        	


        	
            //System.out.println(removeQueue());
            //strategyFactory.getStrategy().action(); // choose the appropriate strategy, needs an input TBD
            //publishPropertyEvent("MyAutoController", "mapping", "some value"); //update the map with recent view
            //System.out.println(this.getPosition());
        }

		// Coordinate initialGuess;
		// boolean notSouth = true;


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
		/*
		public Queue<Move> queueGet() {
			return queue;

		}
		public void queueAdd(Move move) {
			queue.add(move);

		}

		*/
		

	}
