import java.util.ArrayList;

public abstract class State {
	
	String history = "";
	int totalCost = 0;
	int depth;
	int distance;
	
	public State(int depth, int distance){
		this.depth = depth;
		this.distance = distance;
	}
	
	public abstract int difference(State other);
	public abstract ArrayList<State> expand(String s);
	@Override
	public abstract boolean equals(Object other);
	@Override
	public abstract int hashCode();
	@Override
	public abstract String toString();
}
