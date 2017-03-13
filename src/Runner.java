import java.util.Scanner;

public class Runner {
	
	public static void main(String[] args) {
		

		
		BoardState state = BoardState.setupRandom();
		System.out.println(state);
		AlphaBeta ab = new AlphaBeta(2, new Hoard(), false);
		
		Integer moves = null;
		int c = 0;
		Scanner s = new Scanner(System.in);
		while (s!=null){
			
			int player = Players.nextPlayer();
			System.out.println("\n" + Players.name(player)+ "'s turn. Press ENTER to execute...");
			
			String input = s.nextLine();
			if(input.startsWith("=")){
				Pair loc = new Pair(Integer.parseInt(input.split("=")[1].substring(0, 1)), Integer.parseInt(input.split("=")[1].substring(1, 2)));
				System.out.println(state.getContents(loc));
				continue;
			}
			
			c++;
			
			if (input.isEmpty() && (moves == null || c < moves)){
				state = ab.alphaBeta(state, player).node;
				
			}else{
				
				String select = input.split("->")[0];
				String spec = input.split("->")[1];
				if (select != "r"){
					Pair target = new Pair(Integer.parseInt(spec.substring(0, 1)), Integer.parseInt(spec.substring(1, 2)));
					state = state.getMove(null, target, 0, player);
				}else{
					Pair init = new Pair(Integer.parseInt(select.substring(0, 1)), Integer.parseInt(select.substring(1, 2)));
					String loc = spec.split("\\.")[0];
					int height = Integer.parseInt(spec.split("\\.")[1]);
					Pair target = new Pair(Integer.parseInt(loc.substring(0, 1)), Integer.parseInt(loc.substring(1, 2)));
					state = state.getMove(init, target, height, player);
				}
				
			}
			
			System.out.println(state.history);
			System.out.println(state);
			if (state.isComplete()){
				System.out.println("Game over. Took " + c + " turns.");
				break;
			}
		}
		s.close();
	}

}
