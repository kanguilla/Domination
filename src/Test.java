
public class Test {

	public static void main(String[] args) {
		String captured = "XXXOOXX";
		System.out.println(captured.length() - captured.replace("X", "").length());
	}

}
