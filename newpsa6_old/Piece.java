/** 
 * Name: Yiwen Li
 * CSE Account: cs8bfcs
 * Date: Oct 21 2018
 *
 * your file header here */

import java.util.*;

/** your class header here */
public class Piece {

  // all possible char representation of a piece
  public static final char[] possibleShapes = 
  {'O', 'I', 'S', 'Z', 'J', 'L', 'T'}; 

  // initial state of all possible shapes, notice that this array's 
  // content shares index with the possibleShapes array 
  public static final int[][][] initialTiles = {
    {{1,1},
      {1,1}}, // O

    {{0,0,0,0},
      {1,1,1,1},
      {0,0,0,0},
      {0,0,0,0}}, // I

    {{0,0,0},
      {0,1,1},
      {1,1,0}}, // S

    {{0,0,0},
      {1,1,0},
      {0,1,1}}, // Z

    {{0,0,0},
      {1,1,1},
      {0,0,1}}, // J

    {{0,0,0},
      {1,1,1},
      {1,0,0}}, // L

    {{0,0,0},
      {1,1,1},
      {0,1,0}} // T
  };

  // random object used to generate a random shape
  public static Random random = new Random();

  // char representation of shape of the piece, I, J, L, O, S, Z, T
  public char shape;

  // the position of the upper-left corner of the tiles array 
  // relative to the Tetris grid
  public int rowOffset;
  public int colOffset;

  // used to determine 2-state-rotations for shapes S, Z, I
  // set to true to indicate the next call to rotate() should
  // rotate clockwise
  public boolean rotateClockwiseNext = false;

  // an array making where the visible tiles are
  // a 1 indicates there is a visible tile in that position
  // a 0 indicates there is no visible tile in that position
  public int[][] tiles;

  /** Deep copy 2-Dimensional int array */
  private static int[][] deepCopy(int[][] source) {
    assert source.length > 0;
    int[][] array = new int[source.length][source[0].length];
    for (int i = 0; i < source.length; i++) {
      array[i] = source[i].clone();
    }
    return array;
  }

  /** Get the index of the value in a char list */
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

  /** Initialize a Piece object with randomly selected shape */
  public Piece(){
    // TODO
    // Random Generator, generator a random index in 0-6
    int index = random.nextInt(7);
    // Deep copy the shape tile from initialTile
    this.tiles = deepCopy(initialTiles[index]);
    // set instance variable shape to the appropriate char
    this.shape = possibleShapes[index];
    // Initialize rowOffset and colOffset
    // Exception considered when it's O-shaped
    boolean isOshape = this.shape == possibleShapes[0];
    this.rowOffset = isOshape ? 0 : -1;
    this.colOffset = isOshape ? 4 : 3;
  }

  /** Initialize an Piece object with fixed shape attribute
   * @param shape -- designated shape of the piece
   * */
  public Piece(char shape) {
    // TODO
    // Define shape
    this.shape = shape;
    // Get the shape/tile index of the piece
    int index = indexOf(possibleShapes, shape);
    // Deep copy the shape tile from initialTile
    this.tiles = deepCopy(initialTiles[index]);
    // Initialize rowOffset and colOffset
    // Exception considered when it's O-shaped
    boolean isOshape = shape == possibleShapes[0];
    this.rowOffset = isOshape ? 0 : -1;
    this.colOffset = isOshape ? 4 : 3;
  }

  /** Create an Piece object with an deep copied piece template
   * @param other -- template piece object to deep copy from
   * */
  public Piece(Piece other){
    // TODO
    // Copy the shape
    this.shape = other.shape;
    // Copy rotateClockWiseNext
    this.rotateClockwiseNext = other.rotateClockwiseNext;
    // Deep copy the tile
    this.tiles = deepCopy(other.tiles);
    // Copy the rowOffset and colOffset
    this.rowOffset = other.rowOffset;
    this.colOffset = other.colOffset;
  }

  // Rotate the tiles array clockwise
  public void rotateClockwise(){
    // TODO
    int rowLength = tiles.length;
    int colLength = tiles[0].length;
    int[][] newArray = new int[colLength][rowLength];
    for(int row = 0; row < rowLength; row++){
      for(int col = 0; col < colLength; col++){
        newArray[col][rowLength - 1 - row] = this.tiles[row][col];
      }
    }
    this.tiles = newArray;
  }

  public void rotateCounterClockwise() {
    // TODO
    int rowLength = tiles.length;
    int colLength = tiles[0].length;
    int[][] newArray = new int[colLength][rowLength];
    for(int row = 0; row < rowLength; row++){
      for(int col = 0; col < colLength; col++){
        newArray[colLength - 1 - col][row] = this.tiles[row][col];
      }
    }
    this.tiles = newArray;
  }

  public void rotate() {
    // TODO
    // FIXME -- Consider situations that when piece's position limits its ability to rotate
    // If the current shape is O, T, L, J, always rotate clockwise.
    if (shape == 'O'|| shape == 'T'|| shape == 'L'|| shape == 'J') {
      rotateClockwise();
      rotateClockwiseNext = false;
    } else {
      // Else, use rotateClockwiseNext as indicator to perform alternate rotate
      if (rotateClockwiseNext) {
        rotateClockwise();
        rotateClockwiseNext = false;
      } else {
        rotateCounterClockwise();
        rotateClockwiseNext = true;
      }
    }
  }

  /** Check if touches the bottom wall/pieces */
  // FIXME -- Cannot detect if collides with other pieces
  private boolean touchBottomEdge() {
    // Assertion for tile dimension
    assert this.tiles.length >= 2;
    assert this.tiles[0].length >= 2;
    // Check Wall -- When at bottom, if the bottom row has any '1's
    int rowLength = this.tiles.length;
    if (this.rowOffset + rowLength == 20) {
      for (int val : this.tiles[rowLength - 1]) {
        if (val == 1) {
          return true;
        }
      }
    }
    return false;
  }

  /** Check if touches the left wall/pieces */
  // FIXME -- Cannot detect if collides with other pieces
  private boolean touchLeftEdge() {
    // Assertion for tile dimension
    assert this.tiles.length >= 2;
    assert this.tiles[0].length >= 2;
    // Check Wall -- When at left-most column,
    // if there's any '1's in the left-most column
    if (this.colOffset == 0) {
      for (int[] row : this.tiles) {
        if (row[0] == 1) {
          return true;
        }
      }
    }
    return false;
  }

  /** Check if touches the left wall/pieces */
  // FIXME -- Cannot detect if collides with other pieces
  private boolean touchRightEdge() {
    // Assertion for tile dimension
    assert this.tiles.length >= 2;
    assert this.tiles[0].length >= 2;
    // Check Wall -- When at right-most column,
    // if there's any '1's in the right-most column
    int colLength = this.tiles[0].length;
    if (this.colOffset == 9) {
      for (int[] row : this.tiles) {
        if (row[colLength - 1] == 1) {
          return true;
        }
      }
    }
    return false;
  }

  public void movePiece(Direction direction) {
    // TODO
    // If move down, check if the piece touches the ground or other pieces,
    // if so, return and do nothing
    if (direction == Direction.DOWN) {
        rowOffset++;
    }
    // If move left, firstly check if the piece touches the ground, if so, return and do nothing,
    // Then, check if the piece touches the left edge already, if so
    else if (direction == Direction.LEFT) {
        colOffset--;
    }
    // If move right, firstly check if the piece touches the ground, if so, return and do nothing,
    // Then, check if the piece touches the right edge already, if so
    else if (direction == Direction.RIGHT) {
        colOffset++;
    }
  }




}
