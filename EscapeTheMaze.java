package PE1;

/* PLEASE DO NOT MODIFY A SINGLE STATEMENT IN THE TEXT BELOW.
READ THE FOLLOWING CAREFULLY AND FILL IN THE GAPS

I hereby declare that all the work that was required to 
solve the following problem including designing the algorithms
and writing the code below, is solely my own and that I received
no help in creating this solution and I have not discussed my solution 
with anybody. I affirm that I have read and understood
the Senate Policy on Academic honesty at 
https://secretariat-policies.info.yorku.ca/policies/academic-honesty-senate-policy-on/
and I am well aware of the seriousness of the matter and the penalties that I will face as a 
result of committing plagiarism in this assignment.

BY FILLING THE GAPS,YOU ARE SIGNING THE ABOVE STATEMENTS.

Full Name: Hiba Halani
Student Number: 218923045
Course Section: EECS 2030 Section E
*/

import java.util.*;

/**
 * This class sets up maze where a path from start to end is found.
 */
public class PE1 {
	Maze dogMaze;

	/**
	 * @author Hiba Halani
	 * @param maze is a maze that is used to construct the dogMaze
	 *             <p>
	 *             This method sets up the maze using the given input argument
	 *             </p>
	 */
	public void setup(String[][] maze) {
		dogMaze = new Maze(maze);
	}

	/**
	 * @author Hiba Halani
	 * @return it returns true, if enough gate exists (at least 2), otherwise false.
	 *         <p>
	 *         This method returns true if the number of gates in dogMaze >= 2.
	 *         </p>
	 */

	public boolean enoughGate() {
		var totalGates = searchRowsForGate(dogMaze.getMaze(), 0);
		return totalGates >= 2;
	}

	/**
	 * @author Hiba Halani
	 * @param mazeArray an array of type String that represents the maze.
	 * @param rowIndex  int that represent the current row
	 * @return it returns number of gates in the maze.
	 *         <p>
	 *         This method uses recursion to check the number of gates in the maze
	 *         by going through the left, right, top and bottom rows
	 *         </p>
	 */
	private int searchRowsForGate(String[][] mazeArray, int rowIndex) {
		int numberOfGates = 0;

		if (rowIndex > mazeArray.length - 1)
			return 0;
		if (rowIndex == 0 || rowIndex == mazeArray.length - 1) { // treat top and bottom rows differently.
			numberOfGates += searchTopAndBottomRowsForGates(mazeArray[rowIndex], rowIndex, 0); // count gates on top row or bottom row.
		} else {
			if (mazeArray[rowIndex][0].charAt(1) == '0') { // counts gates at left hand side without top and bottom rows
				numberOfGates++;
			}
			if (mazeArray[rowIndex][mazeArray[rowIndex].length - 1].charAt(3) == '0') { // count gates at right hand side without top and bottom rows
				numberOfGates++;
			}
		}
		numberOfGates += searchRowsForGate(mazeArray, rowIndex + 1); // recursive call for remaining rows and adds to previous number of gates found.

		return numberOfGates; // return number of gates
	}

	/**
	 * @author Hiba Halani
	 * @param rowToSearch an array of type String that represents row to search for the gate
	 * @param rowIndex int that represents the current row, this is used to identify if the row is first or last
	 * @param columnIndex int that represents the current column
	 * @return it returns number of gates in top and bottom rows of the maze
	 *
	 *         <p>
	 *         This method uses recursion to check the number of gates in the maze
	 *         by going through each element of the top and bottom rows
	 *         </p>
	 */
	private int searchTopAndBottomRowsForGates(String[] rowToSearch, int rowIndex, int columnIndex) {
		int numberOfGates = 0;
		if (columnIndex >= rowToSearch.length) {
			return 0;
		}
		if (rowIndex == 0 && rowToSearch[columnIndex].charAt(0) == '0') { // first character of first row is 0, it means TOP is open
			numberOfGates++;
		}
		if (rowIndex == dogMaze.getMaze().length - 1 && rowToSearch[columnIndex].charAt(2) == '0') { // third character of last row is 0, it means BOTTOM is open
			numberOfGates++;
		}
		numberOfGates = numberOfGates + searchTopAndBottomRowsForGates(rowToSearch, rowIndex, columnIndex + 1); // recursive call to check each column of top and bottom rows
																												
		return numberOfGates;
	}

	/**
	 * @author Hiba Halani
	 * @param row is the index of the row, where the entrance is
	 * @param column is the index of the column, where the entrance is
	 * @return it returns a string that contains the path from the start to the end
	 *         <p>
	 *         This method finds a path from the entrance gate to the exit gate.
	 *         </p>
	 */
	public String findPath(int row, int column) {
		var entryCell = new Cell(row, column); // store entry point, so it can not be confused with exit point
		List<Cell> cellsVisited = new ArrayList<>();
		List<Cell> validPath = new ArrayList<>();

		if (moveToNextCell(row, column, entryCell, cellsVisited, validPath)) { //

			validPath.add(new Cell(row, column)); // add the entry point to the path

			return listToString(validPath);
		} else {
			return "NO valid path found ";
		}
	}

	/**
	 * @author Hiba Halani
	 * @param validPath is the index of the row, where the entrance is
	 * @return it returns a string that contains the path from the start to the end
	 *         <p>
	 *         This method converts the array list into a string
	 *         </p>
	 */
	private String listToString(List<Cell> validPath) {
		String out = "";
		for (Cell cell : validPath) {
			out = "(" + cell.row + "," + cell.column + ")" + out;
		}
		return out;
	}

	/**
	 * @author Hiba Halani
	 * @param row is the row of the cell
	 * @param column is the column of the cell
	 * @param entryCell is entry cell
	 * @param cellsVisited is the list of cells that have been visited
	 * @param validPath is the path that is taken to reach the end
	 * @return it returns true if cell is valid to move to (open side and not outside the maze)
	 *         <p>
	 *         This method is a recursive method that check each cell to find a
	 *         valid path
	 *         </p>
	 *
	 */

	private boolean moveToNextCell(int row, int column, Cell entryCell, List<Cell> cellsVisited, List<Cell> validPath) {
		if (!isValidPath(row, column)) // returns false if the cell is outside the maze.
			return false;
		cellsVisited.add(new Cell(row, column)); // adds cell to cellVisited to stop visiting the same cell again

		if (isExit(row, column, entryCell)) { // if the current cell has the exit gate, then it returns true
			return true;
		}
		var currentCellValue = dogMaze.getMaze()[row][column]; // current CellValue is used to find out the openings of the cell

		// if the right side of the cell is open and is never visited before, visit that cell
		// if recursion is successful, (return true) add that path to validPath and return true
		if (isRight0pen(currentCellValue) && !isCellAlreadyVisited(row, column + 1, cellsVisited)
				&& moveToNextCell(row, column + 1, entryCell, cellsVisited, validPath)) {
			validPath.add(new Cell(row, column + 1));
			return true;
		}

		// if the left side of the cell is open and is never visited before, visit that cell
		// if recursion is successful, (return true) add that path to validPath and return true
		if (isLeftOpen(currentCellValue) && !isCellAlreadyVisited(row, column - 1, cellsVisited)
				&& moveToNextCell(row, column - 1, entryCell, cellsVisited, validPath)) {
			validPath.add(new Cell(row, column - 1));
			return true;
		}

		// if bottom side of the cell is open and is never visited before, visit that cell
		// if recursion is successful, (return true) add that path to validPath and return true
		if (isBottomOpen(currentCellValue) && !isCellAlreadyVisited(row + 1, column, cellsVisited)
				&& moveToNextCell(row + 1, column, entryCell, cellsVisited, validPath)) {
			validPath.add(new Cell(row + 1, column));
			return true;
		}

		// if top side of the cell is open and is never visited before, visit that cell
		// if recursion is successful, (return true) add that path to validPath and return true
		if (isTopOpen(currentCellValue) && !isCellAlreadyVisited(row - 1, column, cellsVisited)
				&& moveToNextCell(row - 1, column, entryCell, cellsVisited, validPath)) {
			validPath.add(new Cell(row - 1, column));
			return true;
		}

		return false; // if no valid top, bottom, left, right movement, return false;
	}

	/**
	 * @author Hiba Halani
	 * @param row is the row of the cell
	 * @param column is the column of the cell
	 * @param cellsVisited is the list of cells that have been visited
	 * @return it returns true if the current cell has been visited, false otherwise
	 *         <p>
	 *         This method checks to see if current cell has already been visited
	 *         </p>
	 */
	private boolean isCellAlreadyVisited(int row, int column, List<Cell> cellsVisited) {

		for (Cell cell : cellsVisited) {
			if (cell.row == row && cell.column == column) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @author Hiba Halani
	 * @param row is the row of the cell
	 * @param column is the column of the cell
	 * @param entryCell is entry cell
	 * @return it returns true if the current cell is the exit cell, false otherwise
	 *         <p>
	 *         This method checks to see if the current cell is the exit cell
	 *         </p>
	 */
	private boolean isExit(int row, int column, Cell entryCell) {
		var cellValue = dogMaze.getMaze()[row][column];
		if (entryCell.row == row && entryCell.column == column)
			return false;

		if (row == 0 && isTopOpen(cellValue)) // top row and first character is 0
			return true;

		if (row == dogMaze.getMaze().length - 1 && isBottomOpen(cellValue)) // bottom row and 3rd character is 0
			return true;

		if (column == 0 && isLeftOpen(cellValue)) // left column and 2nd character is 0
			return true;

		if (column == dogMaze.getMaze()[row].length - 1 && isRight0pen(cellValue)) // right column and 4th character is
																					// 0
			return true;

		// it is not one of the above, means the current cell is not exit cell.
		return false;
	}

	/**
	 * @author Hiba Halani
	 * @param cellValue contains row and column of the cell
	 * @return it returns true if the value of the first character is '0'
	 *         <p>
	 *         This method checks if the top of the cell is open
	 *         </p>
	 */
	private boolean isTopOpen(String cellValue) {
		return cellValue.charAt(0) == '0';
	}

	/**
	 * @author Hiba Halani
	 * @param cellValue contains row and column of the cell
	 * @return it returns true if the value of the second character is '0'
	 *         <p>
	 *         This method checks if the left of the cell is open
	 *         </p>
	 */
	private boolean isLeftOpen(String cellValue) {
		return cellValue.charAt(1) == '0';
	}

	/**
	 * @author Hiba Halani
	 * @param cellValue contains row and column of the cell
	 * @return it returns true if the value of the third character is '0'
	 *         <p>
	 *         This method checks if the bottom of the cell is open
	 *         </p>
	 */
	private boolean isBottomOpen(String cellValue) {
		return cellValue.charAt(2) == '0';
	}

	/**
	 * @author Hiba Halani
	 * @param cellValue contains row and column of the cell
	 * @return it returns true if the value of the fourth character is '0'
	 *         <p>
	 *         This method checks if the right of the cell is open
	 *         </p>
	 */

	private boolean isRight0pen(String cellValue) {
		return cellValue.charAt(3) == '0';
	}

	/**
	 * @author Hiba Halani
	 * @param row is the row of the cell
	 * @param column is the column of the cell
	 * @return it returns true if the coordinate is in the maze, false otherwise
	 *         <p>
	 *         This method checks if the current cell is inside the maze
	 *         </p>
	 */
//check the co-ordinates are not outside of the maze.
	private boolean isValidPath(int row, int column) {
		if (row >= 0 && row < dogMaze.getMaze().length && column >= 0 && column < dogMaze.getMaze()[row].length)
			return true;

		return false;
	}
}

/**
 * This class defines a Cell of the maze to stores it is row and column
 * co-ordinates.
 */
class Cell {
	int row;
	int column;

	/**
	 * @author Hiba Halani
	 * @param row is the row of the cell
	 * @param column is the column of the cell
	 *               <p>
	 *               This constructor makes the cell
	 *               </p>
	 */
	public Cell(int row, int column) {
		this.row = row;
		this.column = column;
	}

	/**
	 * @author Hiba Halani
	 * @return it returns the cell as a String
	 *         <p>
	 *         toString method to create appropriate display for the cell
	 *         </p>
	 */
	@Override
	public String toString() {
		return "Cell{" + "row=" + row + ", column=" + column + '}';
	}
}

/**
 * This class defines a <code> maze </code> using a 2D array.
 */
class Maze {
	private String[][] maze;

	/**
	 * @param maze is a 2D array that contains information on how each cell of the
	 *             array looks like.
	 * @author Hiba Halani
	 *         <p>
	 *         This constructor makes the maze.
	 *         </p>
	 */
	public Maze(String[][] maze) {
		this.maze = clone2DStringArray(maze);
	}

	/**
	 * @author Hiba Halani
	 * @return it returns a deep copy of the array
	 *         <p>
	 *         private method to create clone of 2D array maze
	 *         </p>
	 */
	private String[][] clone2DStringArray(String[][] original) {
		String[][] deepCopy = new String[original.length][];
		for (int i = 0; i < original.length; i++) {
			deepCopy[i] = new String[original[i].length];
			for (int j = 0; j < original[i].length; j++) {
				deepCopy[i][j] = original[i][j];
			}
		}
		return deepCopy;
	}

	/**
	 * @author Hiba Halani
	 * @return it returns a reference to the maze
	 *         <p>
	 *         This accessor (getter) method returns a 2D array that represents the
	 *         maze
	 *         </p>
	 */
	public String[][] getMaze() {
		return clone2DStringArray(this.maze);
	}

	/**
	 * @author Hiba Halani
	 * @return it returns the 2D array as a String
	 *         <p>
	 *         toString method to create appropriate display for the 2D array
	 *         </p>
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (maze != null) {
			for (String[] s : maze) {
				sb.append(Arrays.toString(s));
				sb.append(System.lineSeparator());
			}
		}

		return sb.toString().replace(",", ""); // to remove commas from the output
	}

}// end of class Maze