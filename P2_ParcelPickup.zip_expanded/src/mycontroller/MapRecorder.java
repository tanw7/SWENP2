package mycontroller;
import world.World;
import java.util.HashMap;
import java.util.Map;

import tiles.MapTile;
import utilities.Coordinate;

public class MapRecorder {
	
	public enum TileType {
		SEARCHED, UNSEARCHED, WALL
	}
	
	public int[][] maze;
	public MapTile[][] map;
	public TileType[][] tileType;
	
	public static final int MAP_WIDTH = World.MAP_WIDTH;
	public static final int MAP_HEIGHT = World.MAP_HEIGHT;
	
	public MapRecorder(HashMap<Coordinate, MapTile> m_hashMap) {
		// TODO Auto-generated constructor stub
		maze = new int[MAP_WIDTH][MAP_HEIGHT];
		map = new MapTile[MAP_WIDTH][MAP_HEIGHT];
		tileType = new TileType[MAP_WIDTH][MAP_HEIGHT];
		
		for (Map.Entry<Coordinate, MapTile> entry : m_hashMap.entrySet()) {
			int i = entry.getKey().x;
			int j = entry.getKey().y;
			
			map[i][j] = entry.getValue();
		}
		
	
		for (int i = 0; i<MAP_WIDTH; i++) {
			for (int j = 0; j< MAP_HEIGHT; j++) {
				if(map[i][j].isType(MapTile.Type.WALL)) {
					maze[i][j] = 100;
				} else {
					maze[i][j] = 0;
				}
			}
		}
		
	}
	
	
	public void CarView(HashMap<Coordinate, MapTile> Carview) {
		
		for(Map.Entry<Coordinate, MapTile> entry : Carview.entrySet()) {
			int carX = entry.getKey().x;
			int carY = entry.getKey().y;
			
			if (!TileOutOfRange(carX, carY)) {
				tileType[carX][carY] = TileType.SEARCHED;
				map[carX][carY] = entry.getValue();
			}
			
		}
	}
	
	public boolean TileOutOfRange(int x, int y) {
		return (x<0 || x > MAP_WIDTH || y <0 || y > MAP_HEIGHT);
	}
	

}
