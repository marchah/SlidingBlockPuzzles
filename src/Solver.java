import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;


public class Solver {
	private static HashMap<String, Integer> mapStatusBoard;
	private static Queue<Board> fifo;
	
	private static void calNextStep(Board board, Block block, Point nextTopLetf) {
		if (board.canMove(block.getTopLeft(), nextTopLetf)) {
			Board tmp = board.move(block.getTopLeft(), nextTopLetf);
			if (tmp.blockInGoal()) {
				for (String move : tmp.getListMove()) {
					System.out.println(move);
				}
				System.exit(Board.EXIT_PUZZLE_SOLVED);
			}
			fifo.add(tmp);
		}
	}
	
	public static void solve(Board b) {
		Board board = null;
		fifo.add(b);
		 while ((board = fifo.poll()) != null) {
			String statusTray = board.getStatusTray();
			if (mapStatusBoard.containsKey(statusTray)) {
				continue;
			}
			ArrayList<Block> listBlock = board.getListBlock();
			mapStatusBoard.put(statusTray, 0);
			for (Block block : listBlock) {
				calNextStep(board, block, new Point(block.getTopLeft().x-1, block.getTopLeft().y));
				calNextStep(board, block, new Point(block.getTopLeft().x, block.getTopLeft().y+1));
				calNextStep(board, block, new Point(block.getTopLeft().x+1, block.getTopLeft().y));
				calNextStep(board, block, new Point(block.getTopLeft().x, block.getTopLeft().y-1));
			}
		}
	}
	
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("Invalid Usage");
			System.exit(Board.EXIT_INVALID_USAGE);
		}

		Board b = new Board(args[0], args[1], false);
		mapStatusBoard = new HashMap<String, Integer>();
		fifo = new LinkedList<Board>();
		solve(b);
		System.exit(Board.EXIT_PUZZLE_NOT_SOLVED);
	}

}
