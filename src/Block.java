import java.awt.Point;

public class Block {
	
	private Point topLeft;
	private Point bottomRight;
	private int id;
	
	private static int CURRENT_ID = 0; 
	
	public Block(Point topLeft, Point bottomRight) {
		this.topLeft = topLeft;
		this.bottomRight = bottomRight;
		this.id = CURRENT_ID++;
	}
	
	public Block(Block b) {
		this.topLeft = new Point(b.topLeft.x, b.topLeft.y);
		this.bottomRight = new Point(b.bottomRight.x, b.bottomRight.y);
		this.id = b.id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Block other = (Block) obj;
		if (bottomRight == null) {
			if (other.bottomRight != null)
				return false;
		} else if (!bottomRight.equals(other.bottomRight))
			return false;
		if (topLeft == null) {
			if (other.topLeft != null)
				return false;
		} else if (!topLeft.equals(other.topLeft))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Block [topLeft=" + topLeft + ", bottomRight=" + bottomRight
				+ ", id=" + id + "]";
	}

	public boolean collideWithBlock(Block b) {
		for (int xb1 = topLeft.x; xb1 != bottomRight.x; xb1++) {
			for (int yb1 = topLeft.y; yb1 != bottomRight.y; yb1++) {
				for (int xb2 = b.topLeft.x; xb2 != b.bottomRight.x; xb2++) {
					for (int yb2 = b.topLeft.y; yb2 != b.bottomRight.y; yb2++) {
						if (xb1 == xb2 && yb1 == yb2)
							return true;
					}
				}
			}
		}
		return false;
	}

	public Point getTopLeft() 		{return topLeft;}
	public Point getBottomRight() 	{return bottomRight;}
	public int getId()				{return id;}

	public void setTopLeft(Point topLeft) 			{this.topLeft = topLeft;}
	public void setBottomRight(Point bottomRight) 	{this.bottomRight = bottomRight;}
}