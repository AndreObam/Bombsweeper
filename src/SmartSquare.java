import java.util.*;

/**
 * The SmartSquare class is a sub-class derived from the GameSquare class, this means that it inherits the attributes and
 * methods from the GameSquare class, in addition to extra functionality being added to the class due to the extensibility
 * provided from inheritance; this includes adding more methods and variables, implementing abstracted methods and etc.
 */
public class SmartSquare extends GameSquare {
	private boolean thisSquareHasBomb = false;		//Refers to whether or not the square contains a bomb.
	public static final int MINE_PROBABILITY = 10;	//Constant value used as the limit when randomly determining whether square has bomb.
	private int mineCounter = 0;					//Refers to the amount of mine surrounding a single square.
	private boolean revealed = false;				//Refers to whether or not the square is visible to the user, used to control access.

	/**
	 * The constructor is used to to set the properties (xLocation, yLocation and GameBoard)of the Smart Square Object, this
	 * is a super constructor, thus it calls the constructor of it's parent class (GameSquare)
	 * @param x refers to the SmartSquare's location on the x-axis of the board.
	 * @param y refers to the SmartSquare's location on the y-axis of the board.
	 * @param board refers to the GameBoard that the square is positioned in.
	 */
	public SmartSquare(int x, int y, GameBoard board) {
		super(x, y, "src/images/blank.png", board);
		Random r = new Random();
		thisSquareHasBomb = (r.nextInt(MINE_PROBABILITY) == 0);
	}

	/**
	 * When invoked, this method scans all surrounding SmartSquares and checks if they contain bombs in order to count
	 * the amount of bombs surrounding a SmartSquare.It constantly checks whether each square location is null in order to
	 * prevent a null pointer exception(accessing an array index which doesn't exist) from occurring whilst attempting to retrieve a board location.
	 * After calculating the amount of mines in the surrounding square, it checks whether the user has clicked on a blank
	 * square (mineCounter value of 0), if the user has clicked on a blank square then it calls the checkBlanks method.
	 * @return mineCounter
	 */
	private int surroundingMines() {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (board.getSquareAt(xLocation + i, yLocation + j) != null) {
					SmartSquare surroundingSquare = (SmartSquare) board.getSquareAt(xLocation + i, yLocation + j);
					if (surroundingSquare.thisSquareHasBomb) {
						mineCounter++;
					}
				}
			}
		}
		if(mineCounter == 0){
			checkBlanks();
		}
		setImage("src/images/" + mineCounter + ".png");
		return mineCounter;
	}

	/**
	 * When invoked, this method scans all surrounding SmartSquares and checks if they contain bombs or have been revealed,
	 * if the surrounding square hasn't been revealed and it doesn't have a bomb then it reveals the square and calls the
	 * surroundingMine method in order to count the amount of surrounding bombs and set the image as appropriate.
	 * If any of the squares surrounding the adjacent squares have a mine counter value of 0 then the method will be called again,
	 * thus this method effectively recursively reveals adjacent squares that have zero bombs in their surrounding squares.
	 */
	private void checkBlanks() {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (board.getSquareAt(xLocation + i, yLocation + j) != null) {
					SmartSquare surroundingSquare = (SmartSquare) board.getSquareAt(xLocation + i, yLocation + j);
					if (!surroundingSquare.revealed && !surroundingSquare.thisSquareHasBomb) {
						surroundingSquare.revealed = true;
						surroundingSquare.surroundingMines();
					}
				}
			}
		}
	}

	/**
	 * When invoked, it checks whether the clicked SmartSquare has already been revealed (value visible to user), if it
	 * hasn't been revealed then it proceeds to check whether the square has a bomb, if the square has a bomb then it
	 * sets the image to that of a bomb, otherwise it calls the 'surroundingMines' method in order to check how many bombs
	 * are surrounding the SmartSquare. Lastly, it sets revealed to 'false' so that the user can't click on the square again.
	 */
	public void clicked() {
		if (revealed == false) {
			if (thisSquareHasBomb) {
				setImage("src/images/bomb.png");
			} else {
				surroundingMines();
			}
			revealed = true;
		}
	}
}