package mycontroller;
import world.World;
import java.util.HashMap;
import tiles.MapTile;
import utilities.Coordinate;

public class mapRecorder {
	
	public enum TileType {
		WALL, ROAD, PARCEL, START, EXIT
	}
	
	public MapTile[][] map;
	public TileType[][] tileType;
	
	public static final int MAP_WIDTH = World.MAP_WIDTH;
	public static final int MAP_HEIGHT = World.MAP_HEIGHT;
	
	public mapRecorder(HashMap<Coordinate, MapTile> m_hashMap) {
		// TODO Auto-generated constructor stub
	}

}
