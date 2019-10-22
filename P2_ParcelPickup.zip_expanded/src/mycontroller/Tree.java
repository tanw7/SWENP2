package mycontroller;
import java.util.*;

// Referenced from https://stackoverflow.com/questions/3522454/java-tree-data-structure
public class Tree<T> {
	// T = data type to store
	public Node<T> root;

    public Tree(T rootData) {
        root = new Node<T>();
        root.data = rootData;
        root.children = new ArrayList<Node<T>>();
    }

    public static class Node<T> {
        public T data;
        public Node<T> parent;
        public ArrayList<Node<T>> children;
        
        public Node<T> addChild(Node<T> child) {
        		child.setParent(this);
        		this.children.add(child);
        		return child;
        }
       
        public void setParent(Node<T> parent) {
        		this.parent = parent;
        }
    }
}
