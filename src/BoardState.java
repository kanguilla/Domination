import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BoardState extends State{
	//data
	
	public BoardState(int depth, int distance) {
		super(depth, distance);
	}
	
	Map<Pair, String> board = new HashMap<Pair, String>();
	
	int presence[] = {18, 18};
	int reserve[] = new int[2];
	int capture[] = new int[2];
	
	
	public boolean isComplete(){
		recount();
		return (presence[0] == 0 || presence[1] == 0);
	}
	
	public void recount(){
		presence[0] = 0;
		presence[1] = 0;
		for (Entry<Pair, String> e : board.entrySet()){
			String s = e.getValue();
			if (s == null || s == " " || s.length() < 1)continue;
			if (s.charAt(s.length()-1) == Players.players[0].charAt(0)){
				presence[0]++;
			}
			if (s.charAt(s.length()-1) == Players.players[1].charAt(0)){
				presence[1]++;
			}
		}
	}
	
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
	public ArrayList<State> expand(int player) {
	
		ArrayList<State> out = new ArrayList<State>();
		for (Entry<Pair, String> e : board.entrySet()){
			if (e.getValue() == null)continue;
			int x = e.getKey().x;
			int y = e.getKey().y;
			String s = e.getValue();
			
			if (s.endsWith(Players.name(player))){
				out.addAll(moves(x, y, s, true, player));
				out.addAll(moves(x, y, s, false, player));
			}
		}
		out.addAll(movesReserve(player));
		//System.out.println("Expanded " + out.size() + " options for " + activeChar);
		return out;
	}

	public ArrayList<State> movesReserve(int player){
		ArrayList<State> out = new ArrayList<State>();
		
		int i = reserve[player];
		
		if (i > 0){
			for (Entry<Pair, String> e : board.entrySet()){
				if (e.getValue() == null)continue;
				int x = e.getKey().x;
				int y = e.getKey().y;
				String stack = e.getValue();
				
				if (stack.length() > 0){
					BoardState ns = new BoardState(this.depth+1, 0);
					ns.history = stack.charAt(stack.length()-1) + " puts a reserved piece on " + new Pair(x, y + i);
					ns.board = new HashMap<Pair, String>(this.board);
					ns.reserve[0] = this.reserve[0];
					ns.reserve[1] = this.reserve[1];
					ns.capture[0] = this.capture[0];
					ns.capture[1] = this.capture[1];
					
					String captured = ns.addPiece(Players.name(player), x, y);
					
					ns.reserve[player] += occurrences(Players.name(player), captured);
					ns.capture[player] += occurrences(Players.name(Players.other(player)), captured);
					ns.reserve[player] --;
					
					ns.recount();
					
					out.add(ns);
				}
			}
		}
		
		return out;
	}
	
	public ArrayList<State> moves(int x, int y, String stack, boolean vertical, int player){
		ArrayList<State> out = new ArrayList<State>();
		
		for (int height = stack.length(); height > 0; height--){
			for (int i = -height; i<=height; i++){
				
				Pair newLoc = (vertical) ? new Pair(x + i, y) : new Pair(x, y + i);
				
				if (i == 0)continue;
				String neighbour = board.get(newLoc);
				if (neighbour != null){
		
					BoardState ns = new BoardState(this.depth+1, 0);
					ns.history = stack.charAt(stack.length()-1) + 
							" moves a stack of size " + height + " from " + new Pair(x, y) + " to " + 
							((vertical) ? new Pair(x + i, y) : new Pair(x, y + i));
					
					
					ns.board = new HashMap<Pair, String>(this.board);
					ns.reserve[0] = this.reserve[0];
					ns.reserve[1] = this.reserve[1];
					ns.capture[0] = this.capture[0];
					ns.capture[1] = this.capture[1];
					
					String top = stack.substring(stack.length()-height);
					String bottom = stack.substring(0, stack.length()-height);
					
					ns.board.put(new Pair(x, y), bottom);
					
					String newStack = neighbour + top;
					String captured = newStack.substring(0, Math.max(newStack.length()-5, 0));
					String leftover = newStack.substring(Math.max(newStack.length()-5, 0), newStack.length());
			
					ns.reserve[player] += occurrences(Players.name(player), captured);
					ns.capture[player] += occurrences(Players.name(Players.other(player)), captured);
					
					ns.board.put(newLoc, leftover);
					
					ns.recount();
					
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
		
		
		for (int i = 0; i < Players.players.length; i++){
			out+=("\n"+Players.name(i)+" -> Stacks:" + presence[i] + " Reserve:" + reserve[i] + " Capture:" + capture[i]);
		}
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
					b.setStack(Players.players[(c)? 0 : 1], i, j);
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
		
		return b;
	}
	
	static BoardState setupRandom(){
		BoardState b = new BoardState(0, 0);
		int xs = 18;
		int os = 18;

		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				if (i == 0 || j == 0 || i == 7 || j == 7){
					b.setStack(null, i, j);
				}else{
					if (Math.random() < 0.5){
						if (xs > 0){
							xs--;
							b.setStack(Players.players[0], i, j);
						}else{
							os--;
							b.setStack(Players.players[1], i, j);
						}
					}else{
						if (os > 0){
							os--;
							b.setStack(Players.players[1], i, j);
						}else{
							xs--;
							b.setStack(Players.players[0], i, j);
						}
					}
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
