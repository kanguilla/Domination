import java.util.Map.Entry;


public abstract class Heuristic {
	public abstract int score(BoardState state, int player);
}

class StackCount extends Heuristic{

	@Override
	public int score(BoardState state, int player) {
		return state.presence[player] - state.presence[Players.other(player)];
	}	
}

class StackSize extends Heuristic{

	@Override
	public int score(BoardState state, int player) {
		int h = 0;
		for (Entry<BoardState.Pair, String> e : state.board.entrySet()){
			String s = e.getValue();
			if (s == null || s == " " || s.length() < 1)continue;
			if (s.charAt(s.length()-1) == Players.players[player].charAt(0)){
				h += s.length();
			}
		}
		return state.presence[player] + h - state.presence[Players.other(player)];
	}	
}

class Hoard extends Heuristic{

	@Override
	public int score(BoardState state, int player) {
		return state.presence[player] + (state.reserve[player]*10) - ((state.lowImpact) ? 10 : 0) - state.presence[Players.other(player)];
	}	
}

class Attack extends Heuristic{
	@Override
	public int score(BoardState state, int player) {
		return state.presence[player] + (state.capture[player]*10) - state.presence[Players.other(player)];
	}	
}

class Random extends Heuristic{

	@Override
	public int score(BoardState state, int player) {
		return (int) (Math.random() * 100);
	}
}