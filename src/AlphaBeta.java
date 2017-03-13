import java.util.ArrayList;
import java.util.Collections;

public class AlphaBeta {

	class Move {
		public Move(int score, BoardState node){
			this.score = score;
			this.node = node;
		}
		int score;
		BoardState node;
	}
	
	public Move alphaBeta(BoardState state, int depth, int alpha, int beta, String activeChar, boolean maximize){
		
		BoardState best = null;
		
		if (depth == 0 || (state.stat().isComplete())){
			return new Move(state.stat().score(other(activeChar)), state);
		}
		
		if (maximize){
			int v = Integer.MIN_VALUE;

			ArrayList<State> children = state.expand(activeChar);
			Collections.shuffle(children);
			
			for (State s : children){
				int child = alphaBeta((BoardState) s, depth-1, alpha, beta, other(activeChar), false).score;
				//System.out.println("child has value " + child);
				if (child > v){
					v = child;
					best = (BoardState) s;
				}
				
				//alpha = Math.max(alpha, v);
				//System.out.println(activeChar + " is maximizing: " + v + " at depth " + depth + " from " + s.history);
				if (beta <= alpha){
					break;
				}
			}
			return new Move(v, best);
		}else{
			int v = Integer.MAX_VALUE;
			
			ArrayList<State> children = state.expand(activeChar);
			//Collections.shuffle(children);
			
			for (State s :children){
				int child = alphaBeta((BoardState) s, depth-1, alpha, beta, other(activeChar), true).score;
				
				if (child < v){
					v = child;
					best = (BoardState) s;
				}
				
				alpha = Math.min(alpha, v);	
				//System.out.println(activeChar + " is minimizing: " + v + " at depth " + depth + " from " + s.history);
				if (beta <= alpha){
					break;
				}
			}
			return new Move(v, best);
		}
	}

	private String other(String p){
		return (p == "X") ? "O" :"X";
	}
}
