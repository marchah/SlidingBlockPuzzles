import java.awt.Point;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Board {
	
	public static int EXIT_PUZZLE_SOLVED = 0;
	public static int EXIT_PUZZLE_NOT_SOLVED = 1;
	public static int EXIT_INVALID_USAGE = 2;
	public static int EXIT_FILE_NOT_FOUND = 3;
	public static int EXIT_CHECKER_MOVE_BAD_FORMAT = 4;
	public static int EXIT_SOLVER_FILE_BAD_FORMAT = 4;
	public static int EXIT_CHECKER_FILE_BAD_FORMAT = 5;
	public static int EXIT_CHECKER_IMPOSSIBLE_MOVE = 6;
	
	private int height;
	private int width;
	private ArrayList<Block> listBlock;
	private ArrayList<Block> listGoal;
	
	private ArrayList<String> listMove;
	
	public Board(String pathInit, String pathGoal, boolean isChecker) {
		listBlock = new ArrayList<Block>();
		listGoal = new ArrayList<Block>();
		
		String init = null;
		String goal = null;
		
		try {
			init = new String(Files.readAllBytes(Paths.get(pathInit)), StandardCharsets.UTF_8);
			goal = new String(Files.readAllBytes(Paths.get(pathGoal)), StandardCharsets.UTF_8);
		} catch (Exception e) {System.err.println("File Not Found"); System.exit(EXIT_FILE_NOT_FOUND);}
		
		Parser p = new Parser();
		
		p.parse(init.split("\n"), true, isChecker);
		p.parse(goal.split("\n"), false, isChecker);
		listMove = new ArrayList<String>();
	}
	
	public Board(int height, int width, ArrayList<Block> listBlock, ArrayList<Block> listGoal, ArrayList<String> listMove) {
		this.height = height;
		this.width = width;
		this.listBlock = listBlock;
		this.listGoal = listGoal;
		this.listMove = listMove;
	}
	
	public boolean blockInGoal() {
		int count = 0;
		for (Block g : listGoal) {
			for (Block b : listBlock) {
				if (g.equals(b))
					count++;
			}
		}
		return (count == listGoal.size());
	}
	
	public boolean canMove(Point oldTopLeft, Point newTopLeft) {
		int vX = newTopLeft.x - oldTopLeft.x;
		int vY = newTopLeft.y - oldTopLeft.y;
		if (Math.abs(vX) + Math.abs(vY) > 1)
			return false;
		if (newTopLeft.x < 0 || newTopLeft.y < 0 || newTopLeft.x >= height || newTopLeft.y >= width)
			return false;
		Block theBlock = null;
		for (Block b : listBlock) {
			if (b.getTopLeft().equals(oldTopLeft) && b.getBottomRight().x + vX <= height && b.getBottomRight().y + vY <= width) {
				theBlock = b; break;
			}
		}
		if (theBlock == null) {
			return false;
		}
		for (Block b : listBlock) {
			if (!b.getTopLeft().equals(oldTopLeft) && b.collideWithBlock(new Block(newTopLeft, new Point(theBlock.getBottomRight().x + vX, theBlock.getBottomRight().y + vY)))) {
				return false;
			}
		}
		return true;
	}
	
	public Board move(Point oldTopLeft, Point newTopLeft) {
		ArrayList<Block> tmp = new ArrayList<Block>();
		for (Block b : listBlock) {
			tmp.add(new Block(b));
		}	
		ArrayList<String> listMoveTmp = new ArrayList<String>();
		for (String move : listMove) {
			listMoveTmp.add(new String(move));
		}
		StringBuilder move = new StringBuilder();
		move.append(oldTopLeft.x);
		move.append(" ");
		move.append(oldTopLeft.y);
		move.append(" ");
		move.append(newTopLeft.x);
		move.append(" ");
		move.append(newTopLeft.y);
		listMoveTmp.add(move.toString());
		for (Block b : tmp) {
			if (b.getTopLeft().equals(oldTopLeft)) {
				b.setTopLeft(newTopLeft);
				b.setBottomRight(new Point(b.getBottomRight().x + (newTopLeft.x - oldTopLeft.x), b.getBottomRight().y + (newTopLeft.y - oldTopLeft.y)));
				return new Board(height, width, tmp, listGoal, listMoveTmp);
			}
		}
		return this;
	}
	
	public String getStatusTray() {
		char[][] tray = getVisualTray();
		StringBuilder status = new StringBuilder();
		
		for (char[] tab : tray) {
			for (char b : tab) {
				status.append(b);
			}
			status.append("\n");
		}
		return status.toString();
	}
	
	private char[][]getVisualTray() {
		char[][] res = new char[height][width];
		for (int x = 0; x < height; ++x) { 
			for (int y = 0; y < width; ++y) { 
				res[x][y] = ' '; 
			}
		} 
		for (Block b : listBlock) {
			for (int x = b.getTopLeft().x; x != b.getBottomRight().x; x++) {
				for (int y = b.getTopLeft().y;  y != b.getBottomRight().y; y++) {
						res[x][y] = (char)(65 + b.getId());
				} 
			} 
		} 
		return res;
	}

	public ArrayList<Block> getListBlock()	{return listBlock;}
	public ArrayList<Block> getListGoal()	{return listGoal;}
	public ArrayList<String> getListMove() {return listMove;}

	private class Parser {
		
		public void parse(String[] file, boolean flag, boolean isChecker) {
			if (flag)
				parseBoardSize(file[0], isChecker);
			for (int i = (flag ? 1 : 0); i != file.length; i++)
				parseBoardBlock(file[i], flag, isChecker);
		}
		
		private void parseBoardSize(String line, boolean isChecker) {
			String[] info = line.split(" ");
			try {
				height = Integer.parseInt(info[0]);
				width = Integer.parseInt(info[1]);
			}
			catch (NumberFormatException e) { if (isChecker) System.exit(EXIT_CHECKER_FILE_BAD_FORMAT); else System.exit(EXIT_SOLVER_FILE_BAD_FORMAT);}
		}
		
		private void parseBoardBlock(String line, boolean flag, boolean isChecker) {
			Point topLeft = new Point();
			Point bottomRight = new Point();
			String[] info = line.split(" ");
			try {
				topLeft.x = Integer.parseInt(info[0]);
				topLeft.y = Integer.parseInt(info[1]);
				bottomRight.x = Integer.parseInt(info[2]) + 1;
				bottomRight.y = Integer.parseInt(info[3]) + 1;
				if (flag)
					listBlock.add(new Block(topLeft, bottomRight));
				else
					listGoal.add(new Block(topLeft, bottomRight));
			}
			catch (Exception e) { if (isChecker) System.exit(EXIT_CHECKER_FILE_BAD_FORMAT); else System.exit(EXIT_SOLVER_FILE_BAD_FORMAT);}
		}	
	}
}