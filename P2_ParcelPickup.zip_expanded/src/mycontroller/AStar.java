package mycontroller;


import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import utilities.Coordinate;
import mycontroller.MapRecorder;
 
public class AStar {
    private final List<Node> open;
    private final List<Node> closed;
    private final List<Node> path;
    private final MapRecorder mapRecorder;
    private Node now;
    private final int xstart;
    private final int ystart;
    private int xTarget, yTarget;
    private final boolean diag;
 

    public class Node implements Comparable<Node> {
        public Node parent;
        public Coordinate coord;
        public int g;
        public int h;
        
        public Node(Node parent, Coordinate coord, int g, int h) {
            this.parent = parent;
            this.coord = coord;
            this.g = g;
            this.h = h;
       }
        
        public Node(int x, int y) {
            this.coord = new Coordinate(x, y);
        }
        
       // Compare by f value (g + h)
       @Override
       public int compareTo(Node o) {
    	       if (o == null) {
    		       return -1;
    	       }
    	       if (g + h > o.g + o.h) {
    	    	   	   return 1;
    	       }
    	       else if (g + h < o.g + o.h) {
        	   	   return -1;
           }
           return 0;
       }
   }
 
    public AStar(MapRecorder mapRecorder, Coordinate coord, boolean diag) {
        this.open = new ArrayList<>();
        this.closed = new ArrayList<>();
        this.path = new ArrayList<>();
        this.mapRecorder = mapRecorder;
        this.now = new Node(null, coord, 0, 0);
        this.diag = diag;
    }
    /*
    ** Finds path to xend/yend or returns null
    **
    ** @param (int) xend coordinates of the target position
    ** @param (int) yend
    ** @return (List<Node> | null) the path
    */
    public List<Node> findPathTo(int xTarget, int yTarget) {
        this.xTarget = xTarget;
        this.yTarget = yTarget;
        this.closed.add(this.now);
        addNeigborsToOpenList();
        while (this.now.coord.x != this.xTarget || this.now.coord.y != this.yTarget) {
            if (this.open.isEmpty()) { // Nothing to examine
                return null;
            }
            this.now = this.open.get(0); // get first node (lowest f score)
            this.open.remove(0); // remove it
            this.closed.add(this.now); // and add to the closed
            addNeigborsToOpenList();
        }
        this.path.add(0, this.now);
        while (this.now.coord.x != this.xstart || this.now.coord.y != this.ystart) {
            this.now = this.now.parent;
            this.path.add(0, this.now);
        }
        return this.path;
    }
    /*
    ** Looks in a given List<> for a node
    **
    ** @return (bool) NeightborInListFound
    */
    private static boolean findNeighborInList(List<Node> array, Node node) {
        return array.stream().anyMatch((n) -> (n.coord.x == node.coord.x && n.coord.y == node.coord.y));
    }
    /*
    ** Calulate distance between this.now and xend/yend
    **
    ** @return (int) distance
    */
    private double distance(int dx, int dy) {
        if (this.diag) { // if diagonal movement is alloweed
            return Math.hypot(this.now.coord.x + dx - this.xTarget, this.now.coord.y + dy - this.yTarget); // return hypothenuse
        } else {
            return Math.abs(this.now.coord.x + dx - this.xTarget) + Math.abs(this.now.coord.y + dy - this.yTarget); // else return "Manhattan distance"
        }
    }
    private void addNeigborsToOpenList() {
        Node node;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (!this.diag && x != 0 && y != 0) {
                    continue; // skip if diagonal movement is not allowed
                }
                node = new Node(this.now, this.now.coord, this.now.g, (int)this.distance(x, y));
                if ((x != 0 || y != 0) // not this.now
                    && this.now.coord.x + x >= 0 && this.now.coord.x + x < this.maze[0].length // check maze boundaries
                    && this.now.coord.y + y >= 0 && this.now.coord.y + y < this.maze.length
                    && this.mapRecorder[this.now.y + y][this.now.x + x] != -1 // check if square is walkable
                    && !findNeighborInList(this.open, node) && !findNeighborInList(this.closed, node)) { // if not already done
                        node.g = node.parent.g + 1.; // Horizontal/vertical cost = 1.0
                        node.g += maze[this.now.y + y][this.now.x + x]; // add movement cost for this square
 
                        // diagonal cost = sqrt(hor_cost² + vert_cost²)
                        // in this example the cost would be 12.2 instead of 11
                        /*
                        if (diag && x != 0 && y != 0) {
                            node.g += .4;	// Diagonal movement cost = 1.4
                        }
                        */
                        this.open.add(node);
                }
            }
        }
        Collections.sort(this.open);
    }
