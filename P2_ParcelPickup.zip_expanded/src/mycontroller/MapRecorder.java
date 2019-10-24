package mycontroller;
import world.World;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import tiles.MapTile;
import utilities.Coordinate;
import java.util.ArrayList;


public class MapRecorder {
	
	public enum TileType {
		SEARCHED, UNSEARCHED, PARCEL, UNREACHABLE, WALL
	}
	
	public int[][] maze;
	public MapTile[][] map;
	public TileType[][] tileType;
	public ArrayList<Coordinate> list; // unvisited coordinates list
	public Coordinate coordinate;
	public Random rand;
	
	public static final int MAP_WIDTH = World.MAP_WIDTH;
	public static final int MAP_HEIGHT = World.MAP_HEIGHT;
	
	public MapRecorder(HashMap<Coordinate, MapTile> m_hashMap) {
		// TODO Auto-generated constructor stub
		maze = new int[MAP_WIDTH][MAP_HEIGHT];
		map = new MapTile[MAP_WIDTH][MAP_HEIGHT];
		tileType = new TileType[MAP_WIDTH][MAP_HEIGHT];
		list = new ArrayList<Coordinate>();
		
		System.out.println("Instantiating MapRecorder");
		
		for (Map.Entry<Coordinate, MapTile> entry : m_hashMap.entrySet()) {
			int i = entry.getKey().x;
			int j = entry.getKey().y;
			//System.out.print("("+String.format("%4s", entry.getKey())+")");
			map[i][j] = entry.getValue();
			if(map[i][j].isType(MapTile.Type.WALL)) {
				tileType[i][j] = TileType.WALL;
			}
		}
		
		for (int i = 0; i < MAP_WIDTH; i++) {
			for (int j = 0; j<MAP_HEIGHT; j++) {
				if(tileType[i][j] == null && !map[i][j].isType(MapTile.Type.WALL)) {
					tileType[i][j] = TileType.UNSEARCHED;
					addToList(i, j, list);
				}
			}
		}
		
		System.out.println("hopefully goes in?");
		for (int j = MAP_HEIGHT - 1; j>=0; j--) {
			for (int i = 0; i< MAP_WIDTH; i++) {
				//System.out.print(map[i][j].getType());
				if(map[i][j].isType(MapTile.Type.WALL)) {
					maze[i][j] = -1;
					//System.out.print(MAP_HEIGHT);
					//System.out.print(map[5][3].getType());
				} else {
					maze[i][j] = 0;
				}
				//System.out.print(maze[i][j]);
				System.out.print(String.format("%4s", maze[i][j]));
				System.out.print(" ");
			}
			System.out.print("\n");
		}
		
	
	}
	
	
	
	
	public void CarView(HashMap<Coordinate, MapTile> Carview) {
		
		for(Map.Entry<Coordinate, MapTile> entry : Carview.entrySet()) {
			int carX = entry.getKey().x;
			int carY = entry.getKey().y;
			
			if (!TileOutOfRange(carX, carY) && tileType[carX][carY] == TileType.UNSEARCHED) {
				tileType[carX][carY] = TileType.SEARCHED;
				if(map[carX][carY].getType() == MapTile.Type.TRAP) {
					tileType[carX][carY] = TileType.PARCEL;
				}
				DeleteFromList(carX, carY, list);
			}
			
		}
	}
	
	public ArrayList<Coordinate> addToList(int x, int y, ArrayList<Coordinate> list) {
		list.add(new Coordinate(x, y));
		return list;
	}
	
	public ArrayList<Coordinate> DeleteFromList(int x, int y, ArrayList<Coordinate> list) {
		list.remove(new Coordinate(x,y));
		return list;
	}
	
	public static Coordinate randomItem(ArrayList<Coordinate> mylist) {
	    Random rand = new Random(); 
	    Coordinate randomCoord = mylist.get(rand.nextInt(mylist.size()));
	    return randomCoord;
	}
	
	
	public boolean TileOutOfRange(int x, int y) {
		return (x<0 || x >= MAP_WIDTH - 1 || y <0 || y >= MAP_HEIGHT - 1);
	}


}
