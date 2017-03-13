
public class Players {
	
	static String[] players = {"X", "O"};
	
	public static String name(int index){
		return players[index];
	}
	
	public static int other(int index){
		return Math.abs(index -1);
	}
	
	
	static int starting_player = 0;
	public static int nextPlayer(){
		starting_player = other(starting_player);
		return starting_player;
	}
}
