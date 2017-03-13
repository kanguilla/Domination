

public abstract class Heuristic {
	public abstract int score(BoardState state, int player);
}

class Stacking extends Heuristic{

	@Override
	public int score(BoardState state, int player) {
		return (int) (Math.random() * 100);
		//return state.presence[player];
	}	
}