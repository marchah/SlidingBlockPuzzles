import java.awt.Point;
import java.util.Scanner;

public class Checker {
	public static void main(String[] args) {
		if (args.length != 2)
			System.exit(Board.EXIT_INVALID_USAGE);
		Board b = new Board(args[0], args[1], true);
		if (b.blockInGoal())
			System.exit(Board.EXIT_PUZZLE_SOLVED);
		String line;
		Scanner sc = new Scanner(System.in);
		try {
		while ((line = sc.nextLine()) != null) {
			try {
				String[] info = line.split(" ");
				Point oldTopLeft = new Point();
				Point newTopLeft = new Point();
				oldTopLeft.x = Integer.parseInt(info[0]);
				oldTopLeft.y = Integer.parseInt(info[1]);
				newTopLeft.x = Integer.parseInt(info[2]);
				newTopLeft.y = Integer.parseInt(info[3]);
				if (!b.canMove(oldTopLeft, newTopLeft)) {
					System.exit(Board.EXIT_CHECKER_IMPOSSIBLE_MOVE);
				}
				b = b.move(oldTopLeft, newTopLeft);	
				if (b.blockInGoal()) {
					sc.close();
					System.exit(Board.EXIT_PUZZLE_SOLVED);
				}
			} catch (Exception e) {System.exit(Board.EXIT_CHECKER_MOVE_BAD_FORMAT);}
		}
		}
		catch (Exception e) {}
		sc.close();
		System.exit(Board.EXIT_PUZZLE_NOT_SOLVED);
	}
}