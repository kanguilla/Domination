import java.util.Scanner;

public class Runner {
	
	public static void main(String[] args) {
		BoardState state = BoardState.setupRandom();
		System.out.println(state);
		
		AlphaBeta ab = new AlphaBeta(new Stacking(), false);
		
		AlphaBeta.Move move;
		
		Integer moves = null;
		int c = 0;
		
		Scanner s = new Scanner(System.in);
		while (s!=null){
			int player = Players.nextPlayer();
			System.out.println("\n" + Players.name(player)+ "'s turn. Press ENTER to execute...");
			String input = "";//s.nextLine();
			if (input.isEmpty() && (moves == null || c < moves)){
				c++;
				move = ab.alphaBeta(state, 2, Integer.MIN_VALUE, Integer.MAX_VALUE, player, true);
				state = move.node;
				System.out.println(state.history);
				System.out.println(state);
				if (state.isComplete()){
					System.out.println("Game over");
					break;
				}
			}else{
				break;
			}
		}
		s.close();
	}

}
