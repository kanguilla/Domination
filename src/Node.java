import java.util.ArrayList;

public class Node<E>{
	ArrayList<Node<E>> children = new ArrayList<Node<E>>();
	E data;
	
	Node<E> parent;
	
	public Node (E s, Node<E> parent){
		this.data = s;
		this.parent = parent;
	}
	
	public int addChild(Node<E> node){
		children.add(node);
		return children.size();
	}
	
	public void setData(E data){
		this.data = data;
	}
	
	public E getData(){
		return data;
	}
}