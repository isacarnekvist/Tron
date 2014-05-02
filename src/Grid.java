import java.util.ArrayList;
import org.ejml.simple.SimpleMatrix;

/**
 * Handles entire grid.
 */
public class Grid {
	
	private int rows;		// How many squares in a row
	private int cols;		// How many squares in a column
	private SimpleMatrix upperLeftCenter;
	private SimpleMatrix upperLeftCorner;
	private ArrayList<ArrayList<GridSquare>> squares;
	private final int SQUARE_WIDTH = 256;
	private final int X = 0;
	private final int Y = 1;

	public Grid(int width, int height) {
		int centerX = width/2;
		int centerY = height/2;
		int squaresOnTop = (int)((double)centerY/SQUARE_WIDTH - 1.0/2.0) + 1;
		int squaresToLeft = (int)((double)centerX/SQUARE_WIDTH - 1.0/2.0) + 1;
		cols = 1 + 2*squaresToLeft;
		rows = 1 + 2*squaresOnTop;
		upperLeftCenter = new SimpleMatrix(1, 2, true, centerX - squaresToLeft*SQUARE_WIDTH,
													   centerY - squaresOnTop*SQUARE_WIDTH);
		upperLeftCorner = new SimpleMatrix(1, 2, true, upperLeftCenter.get(X) - SQUARE_WIDTH/2,
													   upperLeftCenter.get(Y) - SQUARE_WIDTH/2);
		squares = new ArrayList<ArrayList<GridSquare>>();
		
		for(int row = 0; row < rows; row++) {
			squares.add(new ArrayList<GridSquare>());
			for(int col = 0; col < cols; col++) {
				squares.get(row).add(new GridSquare((int)upperLeftCenter.get(X) + col*SQUARE_WIDTH,
													(int)upperLeftCenter.get(Y) + row*SQUARE_WIDTH));
			}
		}
		
		upperLeftCenter.print();
		upperLeftCorner.print();
		
	}
	
	/**
	 * Render the grid.
	 * @param bikePos Array of bikes positions
	 */
	public void render(SimpleMatrix bike1Pos, SimpleMatrix bike2pos) {
		notifySquareOfBike(bike1Pos);
		notifySquareOfBike(bike2pos);
		for(ArrayList<GridSquare> v : squares) {
			for(GridSquare g : v) {
				g.render();
			}
		}
	}
	
	private void notifySquareOfBike(SimpleMatrix bikePos) {
		int xSteps = (int)(bikePos.get(X) - upperLeftCorner.get(X))/SQUARE_WIDTH;
		int ySteps = (int)(bikePos.get(Y) - upperLeftCorner.get(Y))/SQUARE_WIDTH;
		
		squares.get(ySteps).get(xSteps).bikeOver();
	}
	
}
