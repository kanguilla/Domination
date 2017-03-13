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

	Heuristic h;
	boolean shuffle;
	
	public AlphaBeta(Heuristic h, boolean shuffle){
		this.h = h;
		this.shuffle = shuffle;
	}
	
	public Move alphaBeta(BoardState state, int depth, int alpha, int beta, int player, boolean maximize){
		
		state.recount();
		if (depth == 0 || (state.isComplete())){
			
			int score = h.score(state, Players.other(player));
			return new Move(score, state);
		}
		
		if (maximize){
			int v = Integer.MIN_VALUE;

			ArrayList<State> children = state.expand(player);
			if (children.size() == 0){
				//System.out.println("MAX+ ("+player+") had no moves left");
				return new Move(v, state);
			}
				
			if (shuffle)Collections.shuffle(children);
			BoardState best = (BoardState) children.get(0);
			
			for (State s : children){
				
				int child = alphaBeta((BoardState) s, depth-1, alpha, beta, Players.other(player), false).score;
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
			
			ArrayList<State> children = state.expand(player);
			if (children.size() == 0){
				//System.out.println("MIN- ("+player+") had no moves left");
				return new Move(v, state);
			}
			
			if (shuffle)Collections.shuffle(children);
			BoardState best = (BoardState) children.get(0);
			
			for (State s :children){
				int child = alphaBeta((BoardState) s, depth-1, alpha, beta, Players.other(player), true).score;
				
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
}
