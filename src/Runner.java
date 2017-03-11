import java.util.Scanner;

public class Runner {
	
	public static void main(String[] args) {
		BoardState state = BoardState.setupDefault();
		System.out.println(state);
		
		AlphaBeta ab = new AlphaBeta();
		
		AlphaBeta.Move move;
		Scanner s = new Scanner(System.in);
		while (s!=null){
			String player = nextPlayer();
			System.out.println(player + "'s turn. Press ENTER to execute...");
			String input = "";//s.nextLine();
			if (input.isEmpty()){
				
				move = ab.alphaBeta(state, 3, Integer.MIN_VALUE, Integer.MAX_VALUE, player, true);
				state = move.node;
				System.out.println(state);
			}else{
				break;
			}
		}
		s.close();
	}
	
	static String activeChar = "X";
	public static String nextPlayer(){
		if(activeChar == "X"){
			activeChar = "O";
		}else{
			activeChar = "X";
		}
		return activeChar;
	}

}
