package mycontroller;

import java.awt.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

//https://www.baeldung.com/java-dijkstra

public class Dijkstra implements IControlStrategy{
	public void action() {
	}
	
	public class Graph {
		 
	    private Set<Node> nodes = new HashSet<>();
	     
	    public void addNode(Node nodeA) {
	        nodes.add(nodeA);
	    }
	 
	    // getters and setters 
	}
	
	public class Node {
	     
	    private String name;
	     
	    private List<Node> shortestPath = new LinkedList<>();
	     
	    private Integer distance = Integer.MAX_VALUE;
	     
	    Map<Node, Integer> adjacentNodes = new HashMap<>();
	 
	    public void addDestination(Node destination, int distance) {
	        adjacentNodes.put(destination, distance);
	    }
	  
	    public Node(String name) {
	        this.name = name;
	    }
	     
	    // getters and setters
	}
	
	public static Graph calculateShortestPathFromSource(Graph graph, Node source) {
	    source.setDistance(0);
	 
	    Set<Node> settledNodes = new HashSet<>();
	    Set<Node> unsettledNodes = new HashSet<>();
	 
	    unsettledNodes.add(source);
	 
	    while (unsettledNodes.size() != 0) {
	        Node currentNode = getLowestDistanceNode(unsettledNodes);
	        unsettledNodes.remove(currentNode);
	        for (Entry < Node, Integer> adjacencyPair: 
	          currentNode.getAdjacentNodes().entrySet()) {
	            Node adjacentNode = adjacencyPair.getKey();
	            Integer edgeWeight = adjacencyPair.getValue();
	            if (!settledNodes.contains(adjacentNode)) {
	                calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
	                unsettledNodes.add(adjacentNode);
	            }
	        }
	        settledNodes.add(currentNode);
	    }
	    return graph;
	}
	
	private static Node getLowestDistanceNode(Set < Node > unsettledNodes) {
	    Node lowestDistanceNode = null;
	    int lowestDistance = Integer.MAX_VALUE;
	    for (Node node: unsettledNodes) {
	        int nodeDistance = node.getDistance();
	        if (nodeDistance < lowestDistance) {
	            lowestDistance = nodeDistance;
	            lowestDistanceNode = node;
	        }
	    }
	    return lowestDistanceNode;
	}

	private static void CalculateMinimumDistance(Node evaluationNode,
	  Integer edgeWeigh, Node sourceNode) {
	    Integer sourceDistance = sourceNode.getDistance();
	    if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
	        evaluationNode.setDistance(sourceDistance + edgeWeigh);
	        LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
	        shortestPath.add(sourceNode);
	        evaluationNode.setShortestPath(shortestPath);
	    }
	}
	
}
