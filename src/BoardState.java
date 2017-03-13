import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BoardState extends State{
	
	public Stat stat() {
		Stat stat = new Stat();

		for (Entry<Pair, String> e : board.entrySet()){
			if (e.getValue() == null || e.getValue() == " " || e.getValue().length() < 1)continue;
			if (e.getValue().charAt(e.getValue().length()-1) == 'X'){
				stat.scoreX++;
				stat.presenceX++;
				//stat.scoreX += e.getValue().length();
				stat.scoreX += captureX * 10;
				//stat.scoreX += reserveX * 10;
			}else{
				stat.scoreO++;
				stat.presenceO++;
				//stat.scoreO += e.getValue().length();
				stat.scoreO += captureO * 10;
				//stat.scoreO += reserveO * 10;
			}
		}
		return stat;
		
	}
	
	class Stat {
		int scoreX = 0;
		int scoreO = 0;
		
		int presenceX = 0;
		int presenceO = 0;
		
		public int score(String player){
			return (player == "X") ? scoreX  : scoreO;
			}
		public boolean isComplete() {
			return presenceX == 0 || presenceO == 0;
		}
	}
	
	//data
	
	public BoardState(int depth, int distance) {
		super(depth, distance);
	}
	
	Map<Pair, String> board = new HashMap<Pair, String>();
	int reserveX;
	int captureX;
	
	int reserveO;
	int captureO;
	
	
	public boolean setStack(String stack, int x, int y){
		board.put(new Pair(x, y), stack);
		return true;
	}
	
	public String addPiece(String piece, int x, int y){
		
		String newStack = board.get(new Pair(x, y)) + piece;
		String captured = newStack.substring(0, Math.max(newStack.length()-5, 0));
		String leftover = newStack.substring(Math.max(newStack.length()-5, 0), newStack.length());
		
		board.put(new Pair(x, y), leftover);
		return captured;
	}

	@Override
	public int difference(State other) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<State> expand(String activeChar) {
	
		ArrayList<State> out = new ArrayList<State>();
		for (Entry<Pair, String> e : board.entrySet()){
			if (e.getValue() == null)continue;
			int x = e.getKey().x;
			int y = e.getKey().y;
			String s = e.getValue();
			
			if (s.endsWith(activeChar)){
				out.addAll(moves(x, y, s, true));
				out.addAll(moves(x, y, s, false));
			}
		}
		out.addAll(movesReserve(activeChar));
		//System.out.println("Expanded " + out.size() + " options for " + activeChar);
		return out;
	}

	public ArrayList<State> movesReserve(String activeChar){
		ArrayList<State> out = new ArrayList<State>();
		
		int i = (activeChar == "X") ? reserveX : reserveO;
		
		if (i > 0){
			for (Entry<Pair, String> e : board.entrySet()){
				if (e.getValue() == null)continue;
				int x = e.getKey().x;
				int y = e.getKey().y;
				String stack = e.getValue();
				
				if (stack.length() > 0){
					BoardState ns = new BoardState(this.depth+1, 0);
					//ns.history = stack.charAt(stack.length()-1) + " " +  new Pair(x, y) + " can move " + ((vertical) ? new Pair(x + i, y) : new Pair(x, y + i));
					ns.board = new HashMap<Pair, String>(this.board);
					ns.reserveX = this.reserveX;
					ns.reserveO = this.reserveO;
					ns.captureX = this.captureX;
					ns.captureO = this.captureO;
					
					String captured = ns.addPiece(activeChar, x, y);
					
					if (activeChar == "X"){
						ns.reserveX += occurrences("X", captured);
						ns.captureX += occurrences("O", captured);
						ns.reserveX --;
					}else{
						ns.reserveO += occurrences("O", captured);
						ns.captureO += occurrences("X", captured);
						ns.reserveO --;
					}
					out.add(ns);
				}
			}
		}
		
		return out;
	}
	
	public ArrayList<State> moves(int x, int y, String stack, boolean vertical){
		ArrayList<State> out = new ArrayList<State>();
		
		for (int height = stack.length(); height > 0; height--){
			for (int i = -height; i<=height; i++){
				
				Pair newLoc = (vertical) ? new Pair(x + i, y) : new Pair(x, y + i);
				
				if (i == 0)continue;
				String neighbour = board.get(newLoc);
				if (neighbour != null){
		
					BoardState ns = new BoardState(this.depth+1, 0);
					ns.history = stack.charAt(stack.length()-1) + " " +  new Pair(x, y) + " can move " + ((vertical) ? new Pair(x + i, y) : new Pair(x, y + i));
					ns.board = new HashMap<Pair, String>(this.board);
					ns.reserveX = this.reserveX;
					ns.reserveO = this.reserveO;
					ns.captureX = this.captureX;
					ns.captureO = this.captureO;
					
					String top = stack.substring(stack.length()-height);
					String bottom = stack.substring(0, stack.length()-height);
					
					ns.board.put(new Pair(x, y), bottom);
					
					String newStack = neighbour + top;
					String captured = newStack.substring(0, Math.max(newStack.length()-5, 0));
					String leftover = newStack.substring(Math.max(newStack.length()-5, 0), newStack.length());
	
					if (stack.charAt(stack.length()-1) == "X".charAt(0)){
						ns.reserveX += occurrences("X", captured);
						ns.captureX += occurrences("O", captured);
					}else{
						ns.reserveO += occurrences("O", captured);
						ns.captureO += occurrences("X", captured);
					}
					
					ns.board.put(newLoc, leftover);

					out.add(ns);
				}
			}
		}
		return out;
	}
	
	@Override
	public String toString() {
		
		String out = "";
		
		int maxX = 8;
		int maxY = 8;
		for (Entry<Pair, String> e : board.entrySet()){
			maxX = Math.max(maxX, e.getKey().x + 1);
			maxY = Math.max(maxY, e.getKey().y + 1);
		}
		
		for(int i = -1; i<maxX+1;i++){
			
			for(int j = -1; j<maxY+1;j++){
				
				if (i == -1 || i == maxX){
					if (j < maxY)out+=("----");
				}else if (j == -1 || j == maxY){
					out+=("| ");
				}else{		
					String stack = board.get(new Pair(i, j));
					
					if (stack == null){
						out+=(" -  ");
					}else if(stack.length() == 0){
						out+=("    ");
					}else if(stack.length() > 1){
						out+=(" " + stack.charAt(stack.length()-1) + String.valueOf(stack.length()) + " ");
					}else{
						out+=(" " + stack + "  ");
					}
				}
			}
			if (i < maxX){
				out+="\n";
				out+=("| ");
				for (int k = -1; k<maxX-1;k++){
					out+=("    ");
				}
				out+=("|\n");
			}
		}
		Stat s = stat();
		out+=("\nX: " + s.scoreX + "  O: " + s.scoreO);
		out+=("\nX Reserve: " + reserveX + "  Captured: " + captureX);
		out+=("\nO Reserve: " + reserveO + "  Captured: " + captureO);
		return out;
	}
	
	static BoardState setupDefault(){
		BoardState b = new BoardState(0, 0);
		int n = 0;
		boolean c = true;
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				if (i == 0 || j == 0 || i == 7 || j == 7){
					b.setStack(null, i, j);
				}else{
					if (n == 2){
						c = !c;
						n = 0;
					}
					b.setStack((c)? "O" : "X", i, j);
					n++;
				}
			}
		}
		
		for (int k = 0 ; k < 4; k++){
			b.setStack("", 0, k+2);
			b.setStack("", 7, k+2);
			b.setStack("", k+2, 0);
			b.setStack("", k+2, 7);
		}
		
//		b.setStack("OX", 4, 1);
//		b.setStack("XXO", 2, 2);
//		b.setStack("OXXX", 6, 3);
		
		return b;
	}
	
	@Override
	public boolean equals(Object other) {
		BoardState b = (BoardState) other;
		return board.equals(b);
	}

	@Override
	public int hashCode() {
		int result = 17;
        result = 31 * result + board.hashCode();
        result = 31 * result + totalCost;
        return result;
	}
	
	public int occurrences(String exp, String s){
		return (s.length() - s.replace(exp, "").length());
	}
	
	class Pair {
		int x;
		int y;
		
		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public boolean equals(Object other) {
			return (this.x == ((Pair)other).x && this.y == ((Pair)other).y);
		}

		@Override
		public int hashCode() {
	        int result = 17;
	        result = 31 * result + x + y;
	        return result;
	    }

		public int difference(Pair key) {
			return (int) Math.hypot(Math.abs(key.x - x),  Math.abs(key.y - y));
		}
		@Override
		public String toString(){
			return "(" + x + "," + y + ")";
		}
	}
}
