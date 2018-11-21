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

  private static final int PADDING = 10;
  private static final int TILE_GAP = 2;

  // tetris playground
  private static char[][] backGrid = new char[20][10];

  // size of regular small rectangle
  private static final int size = 25;

  // given tetris, pane and myHeyHandler
  private Tetris tetris;
  private GridPane pane;
  private MyKeyHandler myKeyHandler;

  // active piece
  private Piece theActive;
  // next piece
  private Piece theNext;

  // the titleText, scoreText and gameoverText
  private Text titleText, scoreText, text;

  // the rectangle piece
  private Rectangle[][] rectPiece;

  @Override
  public void start(Stage primaryStage) {
    this.tetris = new Tetris();

    // Comment out if needed
    //startMusic();

    // Given code, create a pnae that we can add elemetns to
    pane = new GridPane();
    pane.setAlignment(Pos.CENTER);
    pane.setPadding(new Insets(PADDING,PADDING,PADDING,PADDING));
    pane.setStyle("-fx-background-color: rgb(255,255,255)");
    pane.setHgap(TILE_GAP); 
    pane.setVgap(TILE_GAP);

    // TODO initialize GUI elements here
    // check if it is already game over
    if(!tetris.isGameover){
      // if it is not game over, set message "Tetris"
      setGameText("Tetris", 0, 0, 8, 2);
    } else {
      // if it is game over, set message "Game Over!"
      setGameText("Game Over!", 0, 0, 8, 2);
    }

    // set lines cleared
    String message = "" + tetris.linesCleared;
    setGameText(message, 8, 0, 2, 2);

    // draw the next Piece
    drawNext();
    // deep copy the grid
    updateGrid();
    // draw the active Piece
    drawActive();
    // draw the backgroud
    drawBackground();

    // draw the hold
    drawHold();


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

  /* draw the hold piece */
  private void drawHold() {
    rectPiece = new Rectangle[4][4];
    for (int i = 0; i < rectPiece.length; i++) {
      for (int j = 0; j < rectPiece[0].length; j++) {

        if (tetris.storedPiece != null) {
          // get the next Piece
          this.theNext = new Piece(tetris.storedPiece);
          // get the next Piece shape
          char nextShape = theNext.shape;
          switch (nextShape) {
            case 'O':
              this.rectPiece[i][j] = new Rectangle(size, size, Color.RED);
              break;
            case 'I':
              this.rectPiece[i][j] = new Rectangle(size, size, Color.YELLOW);
              break;
            case 'S':
              this.rectPiece[i][j] = new Rectangle(size, size, Color.CYAN);
              break;
            case 'Z':
              this.rectPiece[i][j] = new Rectangle(size, size, Color.BLUE);
              break;
            case 'J':
              this.rectPiece[i][j] = new Rectangle(size, size, Color.MAGENTA);
              break;
            case 'L':
              this.rectPiece[i][j] = new Rectangle(size, size, Color.PINK);
              break;
            case 'T':
              this.rectPiece[i][j] = new Rectangle(size, size, Color.ORANGE);
              break;
            default:
              this.rectPiece[i][j] = new Rectangle(size, size, Color.WHITE);
              break;
          }
          pane.add(rectPiece[i][j], j, 2 + i);
        } else {
          this.rectPiece[i][j] = new Rectangle(size, size, Color.WHITE);
          pane.add(rectPiece[i][j], j, 2 + i);
        }
      }
    }
  }

  /**
   * Author: Yiwen Li
   * draw the background pane and contain the newest changes
   */
  private void drawBackground() {
    // initialize 20 rows
    rectPiece = new Rectangle[20][10];
    for(int i = 0; i < backGrid.length; i++) {
      // initialize 10 columns
      for(int j = 0; j < backGrid[0].length; j++) {
        if(this.backGrid[i][j] == 'O') {
          // if it is shape O, draw READ
          this.rectPiece[i][j] = new Rectangle(size, size, Color.RED);
          // add rectangle to the pane
          pane.add(rectPiece[i][j], j, i + 6);
        }
        else if(this.backGrid[i][j] == 'I') {
          // if it is shape I, draw Color.YELLOW
          this.rectPiece[i][j] = new Rectangle(size, size, Color.YELLOW);
          // add rectangle to the pane
          pane.add(rectPiece[i][j], j, i + 6);
        }
        else if(this.backGrid[i][j] == 'S') {
          // if it is shape S, draw Color.CYAN
          this.rectPiece[i][j] = new Rectangle(size, size, Color.CYAN);
          // add rectangle to the pane
          pane.add(rectPiece[i][j], j, i + 6);
        }
        else if(this.backGrid[i][j] == 'Z') {
          // if it is shape Z, draw COLORZ
          this.rectPiece[i][j] = new Rectangle(size, size, Color.BLUE);
          // add rectangle to the pane
          pane.add(rectPiece[i][j], j, i + 6);
        }
        else if(this.backGrid[i][j] == 'J') {
          // if it is shape J, draw Color.MAGENTA
          this.rectPiece[i][j] = new Rectangle(size, size, Color.MAGENTA);
          // add rectangle to the pane
          pane.add(rectPiece[i][j], j, i + 6);
        }
        else if(this.backGrid[i][j] == 'L') {
          // if it is shape L, draw Color.PINK
          this.rectPiece[i][j] = new Rectangle(size, size, Color.PINK);
          // add rectangle to the pane
          pane.add(rectPiece[i][j], j, i + 6);
        }
        else if(this.backGrid[i][j] == 'T') {
          // if it is shape T, draw Color.ORANGE
          this.rectPiece[i][j] = new Rectangle(size, size, Color.ORANGE);
          // add rectangle to the pane
          pane.add(rectPiece[i][j], j, i + 6);
        } else {
          // for the rest of them, draw grey color
          this.rectPiece[i][j] = new Rectangle(size, size, Color.GREY);
          // add rectangle to the pane
          pane.add(rectPiece[i][j], j, i + 6);
        }
      }
    }
  }


  /**
   * Author: Yiwen Li
   * Draw the next Piece
   */
  private void drawNext() {
    // next piece rectangle
    rectPiece = new Rectangle[4][4];
    for(int i = 0; i < rectPiece.length; i++) {
      for(int j = 0; j < rectPiece[0].length; j++) {
        // get the next Piece
        this.theNext = new Piece(tetris.nextPiece);
        // get the next Piece shape
        char nextShape = theNext.shape;

        if (nextShape == 'O') {
          // draw 'O' in the rectangle
          if ((i > 0 && i < 3) && (j > 0 && j < 3)) {
            // draw the color of O
            rectPiece[i][j] = new Rectangle(size, size, Color.RED);
            // add rectangle to the pane
            pane.add(rectPiece[i][j], 6 + j, 2 + i);
          } else {
            // for non piece area, draw white
            rectPiece[i][j] = new Rectangle(size, size, Color.WHITE);
            // add rectangle to the pane
            pane.add(rectPiece[i][j], 6 + j, 2 + i);
          }
        }
        if (nextShape == 'I') {
          // draw 'I' in the rectangle
          if (i == 1) {
            // draw the color of I
            rectPiece[i][j] = new Rectangle(size, size, Color.YELLOW);
            // add rectangle to the pane
            pane.add(rectPiece[i][j], 6 + j, 2 + i);
          } else {
            // for non piece area, draw white
            rectPiece[i][j] = new Rectangle(size, size, Color.WHITE);
            // add rectangle to the pane
            pane.add(rectPiece[i][j], 6 + j, 2 + i);
          }
        }

        if (nextShape == 'S') {
          // draw 'S' in the rectangle
          if ((i == 1 && (j == 1 || j == 2)) || (i == 2 && (j == 1 || j == 0))) {
            // draw the color of S
            rectPiece[i][j] = new Rectangle(size, size, Color.CYAN);
            // add rectangle to the pane
            pane.add(rectPiece[i][j], 6 + j, 2 + i);
          } else {
            // for non piece area, draw white
            rectPiece[i][j] = new Rectangle(size, size, Color.WHITE);
            // add rectangle to the pane
            pane.add(rectPiece[i][j], 6 + j, 2 + i);
          }
        }

        if (nextShape == 'Z') {
          // draw 'Z' in the rectangle
          if ((i == 1 && (j == 1 || j == 0)) || (i == 2 && (j == 1 || j == 2))) {
            // draw the color of Z
            rectPiece[i][j] = new Rectangle(size, size, Color.BLUE);
            // add rectangle to the pane
            pane.add(rectPiece[i][j], 6 + j, 2 + i);
          } else {
            // for non piece area, draw white
            rectPiece[i][j] = new Rectangle(size, size, Color.WHITE);
            // add rectangle to the pane
            pane.add(rectPiece[i][j], 6 + j, 2 + i);
          }
        }

        if (nextShape == 'J') {
          // draw 'J' in the rectangle
          if ((i == 1) || (i == 2 && j == 2)) {
            // draw the color of J
            rectPiece[i][j] = new Rectangle(size, size, Color.MAGENTA);
            // add rectangle to the pane
            pane.add(rectPiece[i][j], 6 + j, 2 + i);
          } else {
            // for non piece area, draw white
            rectPiece[i][j] = new Rectangle(size, size, Color.WHITE);
            // add rectangle to the pane
            pane.add(rectPiece[i][j], 6 + j, 2 + i);
          }
        }

        if (nextShape == 'L') {
          // draw 'L' in the rectangle
          if ((i == 1) || (i == 2 && j == 0)) {
            // draw the color of L
            rectPiece[i][j] = new Rectangle(size, size, Color.PINK);
            // add rectangle to the pane
            pane.add(rectPiece[i][j], 6 + j, 2 + i);
          } else {
            // for non piece area, draw white
            rectPiece[i][j] = new Rectangle(size, size, Color.WHITE);
            // add rectangle to the pane
            pane.add(rectPiece[i][j], 6 + j, 2 + i);
          }
        }

        if (nextShape == 'T') {
          // draw 'T' in the rectangle
          if ((i == 1) || (i == 2 && j == 1)) {
            // draw the color of T
            rectPiece[i][j] = new Rectangle(size, size, Color.ORANGE);
            // add rectangle to the pane
            pane.add(rectPiece[i][j], 6 + j, 2 + i);
          } else {
            // for non piece area, draw white
            rectPiece[i][j] = new Rectangle(size, size, Color.WHITE);
            // add rectangle to the pane
            pane.add(rectPiece[i][j], 6 + j, 2 + i);
          }
        }
      }
    }
  }

  /**
   * Draw the active piece in the playground
   */
  private void drawActive() {
    // get the active piece
    this.theActive = new Piece(tetris.activePiece);
    // get tiles of the Active
    int[][] matrix = this.theActive.tiles;
    // rowoffset of the active
    int rowS = this.theActive.rowOffset;
    // coloffset of the active
    int colS = this.theActive.colOffset;

    // put the active piece into grid
    for(int i = 0; i < matrix.length; i++) {
      for(int j = 0; j < matrix[0].length; j++) {
        if(matrix[i][j] == 1) {
          this.backGrid[i + rowS][j + colS] = theActive.shape;
        }
      }
    }
  }


  /**
   * Setter method that sets the text's message, fontname, fontsize, color info.
   * @param message - String type, message to put in
   * @param colstart
   * @param rowstart
   * @param colspan
   * @param rowspan
   */
  private void setGameText(String message, int colstart, int rowstart, int colspan, int rowspan) {
    Text text = new Text();
    // set the message
    text.setText(message);
    // set the font, font size, color
    text.setFont(Font.font("Consolas", FontWeight.BOLD, 30));
    // set the message into the pane according to the location
    pane.add(text, colstart, rowstart, colspan, rowspan);
    // set it to the center
    pane.setHalignment(text, HPos.CENTER);
  }

  /**
   * Author: Yiwen Li
   * private method helper: update method of grid
   * only update the fourth part of the grid
   */
  private void updateGrid() {
    // Deep copy the grid in tetris
    for(int i = 0; i < this.backGrid.length; i++) {
      for(int j = 0; j < this.backGrid[0].length; j++) {
        // copy each element
        this.backGrid[i][j] = tetris.grid[i][j];
      }
    }
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
        setGameText("Tetris", 0, 0, 8, 2);
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

}

