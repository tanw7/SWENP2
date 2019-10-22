package mycontroller;
import world.World;
import java.util.HashMap;
import java.util.Map;

import tiles.MapTile;
import utilities.Coordinate;

public class mapRecorder {
	
	public enum TileType {
		WALL, ROAD, PARCEL, START, EXIT, NOTREACHED
	}
	
	public MapTile[][] map;
	public TileType[][] tileType;
	
	public static final int MAP_WIDTH = World.MAP_WIDTH;
	public static final int MAP_HEIGHT = World.MAP_HEIGHT;
	
	public mapRecorder(HashMap<Coordinate, MapTile> m_hashMap) {
		// TODO Auto-generated constructor stub
		map = new MapTile[MAP_WIDTH][MAP_HEIGHT];
		tileType = new TileType[MAP_WIDTH][MAP_HEIGHT];
		
		for (int i = 0; i<MAP_WIDTH; i++) {
			for (int j = 0; j< MAP_HEIGHT; j++) {
				if(map[i][j] == null) {
					tileType[i][j] = TileType.NOTREACHED;
				}
			}
		}
		
	}
	
	
	public void CarView(HashMap<Coordinate, MapTile> Carview) {
		
		for(Map.Entry<Coordinate, MapTile> entry : Carview.entrySet()) {
			int carX = entry.getKey().x;
			int carY = entry.getKey().y;
			
			
		}
	}

}
