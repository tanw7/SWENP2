package mycontroller;
import world.World;
import java.util.HashMap;
import java.util.Map;

import tiles.MapTile;
import tiles.MapTile.Type;
import utilities.Coordinate;

public class mapRecorder {
	

	
	public MapTile[][] map;
	public Type[][] type;
	
	public static final int MAP_WIDTH = World.MAP_WIDTH;
	public static final int MAP_HEIGHT = World.MAP_HEIGHT;
	
	public mapRecorder(HashMap<Coordinate, MapTile> m_hashMap) {
		// TODO Auto-generated constructor stub
		map = new MapTile[MAP_WIDTH][MAP_HEIGHT];
		type = new Type[MAP_WIDTH][MAP_HEIGHT];
		
		
	
		for (int i = 0; i<MAP_WIDTH; i++) {
			for (int j = 0; j< MAP_HEIGHT; j++) {
				if(map[i][j] == null) {
					type[i][j] = Type.EMPTY;
				}
			}
		}
		
	}
	
	
	public void CarView(HashMap<Coordinate, MapTile> Carview) {
		
		for(Map.Entry<Coordinate, MapTile> entry : Carview.entrySet()) {
			int carX = entry.getKey().x;
			int carY = entry.getKey().y;
			
			if (!TileOutOfRange(carX, carY)) {
				type[carX][carY] = entry.getValue().getType();
				map[carX][carY] = entry.getValue();
			}
			
		}
	}
	
	public boolean TileOutOfRange(int x, int y) {
		return (x<0 || x > MAP_WIDTH || y <0 || y > MAP_HEIGHT);
	}

}
