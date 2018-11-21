/**
 * File Header
 * Author: Yiwen Li
 * Date: 11/17/2018
 * CS Account: cs8bfcs
 *
 * This file creates GUI for Tetris game, it allows users
 * to use directional command to manipulate the tetris,
 * including, drop, rotate, output to file, left, right, etc.
 */
import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;

import java.util.*;
import java.io.*;
import javafx.scene.media.*;

public class GuiTetris extends Application {

  @Override
  public void start(Stage primaryStage) {
    this.tetris = new Tetris();

    // Comment out if needed
    // startMusic();

    /* Create base pane */
    pane = new GridPane();
    pane.setAlignment(Pos.CENTER);
    pane.setPadding(new Insets(PADDING,PADDING,PADDING,PADDING));
    pane.setStyle("-fx-background-color: rgb(255,255,255)");
    pane.setHgap(TILE_GAP); 
    pane.setVgap(TILE_GAP);

    /* Set Header Text */
    String headerText = this.tetris.isGameover ? "Game Over!" : "Tetris";
    setGameText(headerText, headerPosition);

    // set lines cleared
    String message = Integer.toString(tetris.linesCleared);
    setGameText(message, headerPosition);

    // draw the default background
    // drawBackground();
    // draw the next Piece
    drawNext();
    // draw the backgroud
    drawGrid();
    // draw the active Piece
    drawActive();


    /////////////////////////////////////////////
    //// DONT CHANGE, GIVEN SCENE AND STAGE ////
    ////////////////////////////////////////////

    // Given code, shows the pane in the window when running the program
    Scene scene = new Scene(pane);
    primaryStage.setTitle("Tetris");
    primaryStage.setScene(scene);
    primaryStage.show();

    myKeyHandler = new MyKeyHandler();
    scene.setOnKeyPressed(myKeyHandler);
    MoveDownWorker worker = new MoveDownWorker();
    worker.start();
  }


  /////////////////////////////////
  ///   Private Helper Method   ///
  /////////////////////////////////

  /**
   * Draw the background of the pane */
//  private void drawBackground() {
//    for (int i = 0; i < windowLength; i++) {
//      for (int j = 0; j < windowWidth; j++) {
//        Rectangle rectangle;
//        if (i < headerLength) {
//          rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorHeaderBackground);
//        } else {
//          rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorGridBackground);
//        }
//        this.pane.add(rectangle, j, i);
//      }
//    }
//  }

  /**
   * draw the background pane and contain the newest changes
   */
  private void drawGrid() {
    char[][] grid = this.tetris.grid;
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        char shape = grid[i][j];
        Rectangle rectangle;
        switch (shape) {
          case 'O':
            rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorTypeO);
          case 'I':
            rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorTypeI);
          case 'S':
            rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorTypeS);
          case 'Z':
            rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorTypeZ);
          case 'J':
            rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorTypeJ);
          case 'L':
            rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorTypeL);
          case 'T':
            rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorTypeT);
          default:
            rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorGridBackground);
        }
        this.pane.add(rectangle, j + paneGridOffset[0], i + paneGridOffset[1]);
      }
    }
  }


  /**
   * Author: Yiwen Li
   * Draw the next Piece
   */
  private void drawNext() {
    char shape = this.tetris.nextPiece.shape;
    int[][] tiles = this.tetris.nextPiece.tiles;
    Rectangle rectangle;
    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles[0].length; j++) {
        switch (shape) {
          case 'O':
            rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorTypeO);
          case 'I':
            rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorTypeI);
          case 'S':
            rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorTypeS);
          case 'Z':
            rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorTypeZ);
          case 'J':
            rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorTypeJ);
          case 'L':
            rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorTypeL);
          case 'T':
            rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorTypeT);
          default:
            rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorHeaderBackground);
        }
        if (tiles[i][j] == 1) {
          pane.add(rectangle, j + paneNextOffset[0], i + paneNextOffset[1]);
        }
      }
    }
  }

  /**
   * Draw the active piece in the playground
   */
  private void drawActive() {
    int[][] tiles = this.tetris.activePiece.tiles;
    char shape = this.tetris.activePiece.shape;
    int rowOffset = this.tetris.activePiece.rowOffset;
    int colOffset = this.tetris.activePiece.colOffset;
    Rectangle rectangle;
    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles[0].length; j++) {
        switch (shape) {
          case 'O':
            rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorTypeO);
          case 'I':
            rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorTypeI);
          case 'S':
            rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorTypeS);
          case 'Z':
            rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorTypeZ);
          case 'J':
            rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorTypeJ);
          case 'L':
            rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorTypeL);
          case 'T':
            rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorTypeT);
          default:
            rectangle = new Rectangle(unitRectSideLength, unitRectSideLength, colorGridBackground);
        }
        this.pane.add(rectangle, j + colOffset + paneGridOffset[0], i + rowOffset + paneGridOffset[1]);
      }
    }
  }


  /**
   * Set Text section to the pane
   * @param message - String type, message to put in
   * @param position - int[] that contains start/end grid position for row/col
   */
  private void setGameText(String message, int[] position) {
    /* Extract position code */
    assert position.length == 4;
    int colIndex = position[0],
            rowIndex = position[1],
            colSpan = position[2],
            rowSpan = position[3];

    Text text = new Text();
    // set the message
    text.setText(message);
    // set the font, font size, color
    text.setFont(Font.font("Consolas", FontWeight.BOLD, 30));
    // set the message into the pane according to the location
    pane.add(text, colIndex, rowIndex, colSpan, rowSpan);
    // set it to the center
    pane.setHalignment(text, HPos.CENTER);
  }

  /////////////////////////////////
  ///     Key Event Handler     ///
  /////////////////////////////////
  private class MyKeyHandler implements EventHandler<KeyEvent> {
    @Override
    public void handle(KeyEvent e) {
      // TODO handle key events here
      if (tetris.isGameover) {
        // check if game over
        // FIX ME
        setGameText("Tetris", headerPosition);
      } else {
        // if game is not over
        if (e.getCode().equals(KeyCode.UP)) {
          tetris.rotate();
        }
        if (e.getCode().equals(KeyCode.DOWN)) {
          tetris.move(Direction.DOWN);
        }
        if (e.getCode().equals(KeyCode.LEFT)) {
          tetris.move(Direction.LEFT);
        }
        if (e.getCode().equals(KeyCode.RIGHT)) {
          tetris.move(Direction.RIGHT);
        }
        if (e.getCode().equals(KeyCode.SPACE)) {
          tetris.drop();
        }
        if (e.getCode().equals(KeyCode.O)) {
          try {
            tetris.outputToFile();
          } catch (IOException err) {
            System.out.println(err.getMessage());
          }
        }
      }
    }

  }


  /* ---------------- DO NOT EDIT BELOW THIS LINE ---------------- */

  /**
   * private class GuiTetris.MoveDownWorker
   * a thread that simulates a downwards keypress every some interval
   * @author Junshen (Kevin) Chen
   */
  private class MoveDownWorker extends Thread{

    private static final int DROP_INTERVAL = 500; // millisecond
    private int move_down_timer = DROP_INTERVAL; 

    /**
     * method run
     * called when the thread begins, decrements the timer every iteration
     * of a loop, reset the timer and sends a keydown when timer hits 0
     */
    @Override
    public void run(){

      // run forever until returned
      while (true) {
        // stop the thread if the game is over
        if (tetris.isGameover) return; 

        // wait 1ms per iteration
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
          break;
        }

        move_down_timer -= 1;
        if (move_down_timer <= 0 ) {

          // simulate one keydown by calling the 
          // handler.handle()
          myKeyHandler.handle(
              new KeyEvent(null, "", "", KeyCode.DOWN, 
                false, false, false, false)
              );

          move_down_timer = DROP_INTERVAL;
        }
      }
    }
  } // end of private class MoveDownWorker

  /**
   * Cheng Shen Nov. 11th 2018
   * This method plays the background music
   */
  private void startMusic(){
    try{
      System.out.println("Playing Music~~~");
      File mp3 = new File("./Tetris.mp3");
      Media bgm = new Media(mp3.toURI().toString());
      MediaPlayer bgmPlayer = new MediaPlayer(bgm);
      bgmPlayer.setCycleCount(bgmPlayer.INDEFINITE);
      bgmPlayer.play();
    }catch (Exception e){
      System.out.println("Exception playing music");
    }
  }

  /* Side length of a unit rectangle */
  private static final int unitRectSideLength = 25;
  /* Game based tetris */
  private Tetris tetris;
  /* GUI pane */
  private GridPane pane;
  /* Key handler */
  private MyKeyHandler myKeyHandler;

  /* Background section barrier edge */
  private static final int windowWidth = 10;
  private static final int windowLength = 26;
  private static final int headerLength = 6;

  /* Positions/Offsets on Pane -- {colOffset, rowOffset}*/
  private static final int[] paneGridOffset = {0, 6};
  private static final int[] paneNextOffset = {6, 2};
  private static final int[] headerPosition = {0, 0, 8, 2};

  /* Background Rectangles */
  private static final Rectangle headerBackgroundRectangle = new Rectangle(unitRectSideLength, unitRectSideLength, Color.WHITE);
  private static final Rectangle gridBackgroundRectangle = new Rectangle(unitRectSideLength, unitRectSideLength, Color.WHITE);

  /* Color of Pieces */
  private static final Color colorTypeO = Color.RED;
  private static final Color colorTypeI = Color.YELLOW;
  private static final Color colorTypeS = Color.CYAN;
  private static final Color colorTypeZ = Color.BLUE;
  private static final Color colorTypeJ = Color.MAGENTA;
  private static final Color colorTypeL = Color.PINK;
  private static final Color colorTypeT = Color.ORANGE;
  private static final Color colorGridBackground = Color.GRAY;
  private static final Color colorHeaderBackground = Color.GRAY;

  /* Pane Attributes */
  private static final int PADDING = 10;
  private static final int TILE_GAP = 2;
}

