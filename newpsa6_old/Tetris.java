/**
 * Name: Yiwen Li
 * CSE Account: cs8bfcs
 * Date: 10/27/2018
 *
 * This is the main file of grid manipulation, it considers out of
 * bound situations and directions
 * Note that this serves for second submission of PSA3 after checkpoint
 *
 **/

import java.util.*;
import java.io.*;

/** This class names Tetris has many instance variables,
 * such as linesCleared, isGameover, activePiece, nextPiece,
 * usedHold, storedPiece, grid, it also has two constructors
 * and various methods
 * The main picture of this class is to keep track of the
 * status of Tetris game and operate user's command.
 * */
public class Tetris {

  public int linesCleared; // how many lines cleared so far

  public boolean isGameover;  // true if the game is over
  // false if the game is not over

  public Piece activePiece;   // holds a Piece object that can be moved
  // or rotated by the player

  public Piece nextPiece; // holds a Piece object that will become the new 
  // activePiece when the activePiece consolidates

  // The following 2 variables are used for the extra credit 
  public boolean usedHold = false;    // set to true if the player already 
  // used hold once since last piece 
  // consolidated

  public Piece storedPiece = null;    // holds the stored piece 

  public char[][] grid;   // contains all consolidated pieces, each tile  
  // represented by a char of the piece's shape
  // a position stores a space char if it is empty


  /** This is no-argument constructor of Tetris
   * Initialize the instance variable grid
   * Initialize all the array's content the new container that 
   * fulfills method requirement */
  public Tetris(){
    // TODO
    this.isGameover = false;
    // Initialize grid to be 2D char array with 20 rows and 10 cols
    this.grid = new char[20][10];
    // Initialize all the array's content with space characters
    for(int i = 0; i < grid.length; i++) {
      for(int j = 0; j < grid[0].length; j++) {
        this.grid[i][j] = ' ';
      }
    }
    // Generate random pieces for activePiece and nextPiece
    this.activePiece = new Piece();
    this.nextPiece = new Piece();
  }

  /** Constructor Tetris will read file's content and initial appropriate
   * instance variables, also check if the game is already over
   * @param filename -- file content
   */
  public Tetris (String filename) throws IOException {
    // TODO;
    this.grid = new char[20][10];
    // Read input filename
    BufferedReader bufferedReader = new BufferedReader(
        new FileReader(new File(filename)));
    this.linesCleared = Integer.parseInt(bufferedReader.readLine());
    this.activePiece = new Piece(bufferedReader.readLine().charAt(0));
    this.nextPiece = new Piece(bufferedReader.readLine().charAt(0));
    // Assign grid values by rows
    for (int i = 0; i < this.grid.length; i++) {
      String row = bufferedReader.readLine();
      // Make space compensation for a length-10 string
      row = String.format("%-10s", row);
      // Convert string into char array
      char[] char_array = row.toCharArray();
      this.grid[i] = char_array;
    }
    // Check GameOver
    this.isGameover = this.hasConflict(this.activePiece);
    if (this.isGameover) {
      System.out.println(this.toString());
      System.out.println("Game over! Total lines cleared: "
          + this.linesCleared);
    }
  }

  /** Check if the piece has conflict,
   * for example, if it has a visible tile that is outside of the grid
   * or has a visible tile that sits on a non-empty spot on the grid
   * @param piece -- visible piece to check
   * @return boolean
   * */
  public boolean hasConflict(Piece piece) {
    // TODO
    // Identify the row/col offset
    int rowStart = piece.rowOffset;
    int colStart = piece.colOffset;

    // Iterate through the tiles
    for (int i = 0; i < piece.tiles.length; i++) {
      for (int j = 0; j < piece.tiles[0].length; j++) {
        // For every valid (1) position in tiles, check the
        // global grid to see if out of bound or conflict
        if (piece.tiles[i][j] == 1) {
          if (i + rowStart >= this.grid.length || i + rowStart < 0
              || j + colStart >= this.grid[0].length || j + colStart < 0) {
            // check if the row is out of bound
            // check if the column is out of bound
            return true;
          } else {
            if (this.grid[rowStart + i][colStart + j] != ' '){
              // check if it sits on an non-empty space
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  /** for each of the visible tiles of the current active piece,
   * stores the active piece's character representation into its
   * corresponding location
   * @return void
   * */
  public void consolidate() {
    // TODO
    int rowStart = this.activePiece.rowOffset;
    int colStart = this.activePiece.colOffset;
    for (int i = 0; i < this.activePiece.tiles.length; i++) {
      for (int j = 0; j < this.activePiece.tiles[0].length; j++) {
        // save activePiece to grid by shape letter
        if (this.activePiece.tiles[i][j] == 1) {
          this.grid[i + rowStart][j + colStart] = activePiece.shape;
        }
      }
    }
    this.usedHold = false;
  }


  /** check each row in the grid and clears the row if it's fully
   * occupied and moves everything above the row down by one.
   * */
  public void clearLines() {
    // TODO
    // loop through the grid,
    // find out which row is empty
    for (int i = 0; i < grid.length; i++) {
      int count = 0;
      int clearedRow = 0;
      for (int j = 0; j < grid[0].length; j++) {
        if (grid[i][j] != ' ') {
          count++;
        }
      }
      // check if the row is empty, and
      // save the row should be cleared
      if (count == 10) {
        this.linesCleared++;
        clearedRow = i;
      }
      // clear the last row of grid
      for (int x = clearedRow; x > 0; x--) {
        for (int y = 0; y < this.grid[0].length; y++) {
          this.grid[x][y] = this.grid[x - 1][y];
        }
      }
      // make the first row with empty space because everything shift down
      for (int k = 0; k < this.grid[0].length; k++) {
        this.grid[0][k] = ' ';
      }
    }
  }

  /** this method attempts to move the active piece,
   * and check if the piece is movable
   * @param direction -- direction to move, LEFT, RIGHT, DOWN
   * @return boolean -- if active piece is moved successfully
   * */
  public boolean move(Direction direction) {
    // TODO
    Piece copy = new Piece(this.activePiece);
    copy.movePiece(direction);
    if (!hasConflict(copy)) {
      this.activePiece.movePiece(direction);
      return true;
    } else {
      if (direction == Direction.DOWN) {
        this.consolidate();
        this.clearLines();
        this.activePiece = this.nextPiece;
        this.nextPiece = new Piece();
        if (hasConflict(this.activePiece)) {
          this.isGameover = true;
        }
      }
      return false;
    }
  }

  /** This method will cause the current active piece to continue
   * moving down until it fails
   * */
  public void drop() {
    // TODO
    while (this.move(Direction.DOWN)) {
    }
  }

  /** rotate the active piece if it does not cause a conflict * */
  public void rotate() {
    // TODO
    Piece copyRotate = new Piece(activePiece);
    copyRotate.rotate();
    if (!hasConflict(copyRotate)) {
      this.activePiece.rotate();
    }
  }

  /** save the current status to file output.txt
   * note that here will write into file output.txt
   */
  public void outputToFile() throws IOException {
    // TODO
    BufferedWriter bufferedWriter = new BufferedWriter(
        new FileWriter("output.txt"));
    // Write attribute
    bufferedWriter.write(String.valueOf(this.linesCleared));
    bufferedWriter.newLine();
    bufferedWriter.write(String.valueOf(this.activePiece.shape));
    bufferedWriter.newLine();
    bufferedWriter.write(String.valueOf(this.nextPiece.shape));
    bufferedWriter.newLine();
    // Write grid
    for (int i = 0; i < this.grid.length; i++) {
      String line = String.valueOf(this.grid[i]);
      bufferedWriter.write(line);
      bufferedWriter.newLine();
    }
    // Close file
    bufferedWriter.close();
    System.out.println("Saved to output.txt");
  }


  /** This method handles the interactive part of the program
   * This is a way to debug the other methods
   * */
  public void play () {
    // TODO

    // If already game over, return
    if (this.isGameover) {
      return;
    }

    Scanner scanner = new Scanner(System.in);

    while (!this.isGameover) {
      // Check if input has multiple characters (invalid)
      System.out.println(this.toString());
      System.out.print("> ");
      String command = scanner.nextLine();
      // Check if the char c is valid user input
      if (command.equals("z")) {
        hold();
      }
      if (command.equals("a")) {
        move(Direction.LEFT);
      }
      if (command.equals("d")) {
        move(Direction.RIGHT);
      }
      if (command.equals("s")) {
        move(Direction.DOWN);
      }
      if (command.equals("w")) {
        rotate();
      }
      if (command.equals(" ")) {
        drop();
      }
      if (command.equals("o")) {
        try {
          outputToFile();
        } catch (IOException e) {
          // This prints out an error message!
          System.err.println("SOS pleases someone help me");
        }
      }
      if (command.equals("q")) {
        this.isGameover = true;
        break;
      }
    }
    System.out.println(this.toString());
    System.out.println("Game over! Total lines cleared: " + this.linesCleared);
  }

  /**
   * returns the string representation of the Tetris game state in the 
   * following format:
   *  Lines cleared: [number]
   *  Next piece: [char]  (Stored piece: [char])
   *  char[20][10]
   * @return string representation of the Tetris game
   */
  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();

    str.append("\nLines cleared: " + this.linesCleared + '\n');

    str.append("Next piece: " + this.nextPiece.shape);
    if (this.storedPiece == null) str.append("\n");
    else str.append("  Stored piece: " + this.storedPiece.shape + '\n');

    str.append("| - - - - - - - - - - |\n");

    /*deep copy the grid*/
    char[][] temp_grid = new char[this.grid.length][this.grid[0].length];
    for (int row=0; row<this.grid.length; row++)
      for (int col=0; col<this.grid[0].length; col++)
        temp_grid[row][col] = this.grid[row][col];

    /*put the active piece in the temp grid*/
    for (int row=0; row<this.activePiece.tiles.length; row++)
      for (int col=0; col<this.activePiece.tiles[0].length; col++)
        if (activePiece.tiles[row][col] == 1)
          temp_grid[row+activePiece.rowOffset]
            [col+activePiece.colOffset] = 
            activePiece.shape;

    /*print the temp grid*/
    for (int row=0; row<temp_grid.length; row++){
      str.append('|');
      for (int col=0; col<temp_grid[0].length; col++){
        str.append(' ');
        str.append(temp_grid[row][col]);
      }
      str.append(" |\n");
    }

    str.append("| - - - - - - - - - - |\n");
    return str.toString();        
  }

  /** Get the index of the value in a char list
   * @param array -- array of tiles(possibleshapes)
   * @param val -- the character command
   * */
  private static int indexOf(char[] array, char val) {
    assert array.length > 0;
    int index = -1;
    for (int i = 0; i < array.length; i++) {
      if (array[i] == val) {
        index = i;
        break;
      }
    }
    return index;
  }

  /** extra credit method, hold or swap the activePiece */
  public void hold() {
    // TODO extra credit
    // hold and swap-out functionality can only be triggered once
    if (!this.usedHold) {
      // reset the active piece to the top of the grid
      // in its initial rotation state as newly created
      this.activePiece = new Piece(this.activePiece.shape);

      // if storedPiece = null, store activePiece into storedPiece
      // then use nextPiece to be the new activePiece, generate new nextPiece
      if (this.storedPiece == null) {
        this.storedPiece = this.activePiece;
        this.activePiece = this.nextPiece;
        this.nextPiece = new Piece();
      } else {
        // if storedPiece != null, swap storedPiece and activePiece,
        // do not modify nextPiece
        Piece temp = this.storedPiece;
        this.storedPiece = this.activePiece;
        this.activePiece = temp;
      }
      this.usedHold = true;

      // it cannot be triggered again until something has consolidated
    }
  }

  /**
   * first method called during program execution
   * @param args: an array of String when running the program from the 
   * command line, either empty, or contains a valid filename to load
   * the Tetris game from
   */
  public static void main(String[] args) {
    if (args.length != 0 && args.length != 1) {
      System.err.println("Usage: java Tetris / java Tetris <filename>");
      return;
    }
    try {
      Tetris tetris;
      if (args.length == 0) tetris = new Tetris();
      else tetris = new Tetris(args[0]);
      tetris.play();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
