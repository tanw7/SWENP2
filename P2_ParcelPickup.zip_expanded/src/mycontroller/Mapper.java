package mycontroller;
import utilities.Coordinate;

public class Mapper {
	private Tree<Coordinate> mapTree;
	
	// Use either a tree or hashmap for this data structure *undecided*
	public Mapper(Coordinate startingPosition) {
		mapTree = new Tree<Coordinate>(startingPosition);
		Tree.Node<Coordinate> tile = new Tree.Node<Coordinate>();
		mapTree.root.children.add(tile);
	}
}
