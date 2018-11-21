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

import java.awt.*;
import java.util.*;
import java.io.*;
import javafx.scene.media.*;

public class GuiTetris extends Application {

  private static final int PADDING = 10;
  private static final int TILE_GAP = 2;

  // given tetris, pane and myHeyHandler
  private Tetris tetris;
  private GridPane pane;
  private MyKeyHandler myKeyHandler;
  // size of regular small rectangle
  private static final int size = 25;

  private Rectangle[][] rectGrid = new Rectangle[20][10];
  private Rectangle[][] rectNext = new Rectangle[4][4];
  private Rectangle[][] rectHold = new Rectangle[4][4];


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

    initRectGrid();


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
    private void initRectGrid() {
      char[][] grid = new char[this.tetris.grid.length][this.tetris.grid[0].length];
      for (int i = 0; i < this.tetris.grid.length; i++) {
          for (int j = 0; j < this.tetris.grid[0].length; j++) {
              grid[i][j] = this.tetris.grid[i][j];
          }
      }

      Piece activePiece = new Piece(tetris.activePiece);
      for (int i = 0; i < activePiece.tiles.length; i++) {
          for (int j = 0; j < activePiece.tiles[0].length; j++) {
              grid[i + activePiece.rowOffset][j + activePiece.colOffset] = activePiece.shape;
          }
      }

      for (int i = 0; i < grid.length; i++) {
          for (int j = 0; j < grid[0].length; j++) {
              char shape = grid[i][j];
              switch (shape) {
                  case 'O':
                      this.rectGrid[i][j] = new Rectangle(size, size, Color.RED);
                      break;
                  case 'I':
                      this.rectGrid[i][j] = new Rectangle(size, size, Color.YELLOW);
                      break;
                  case 'S':
                      this.rectGrid[i][j] = new Rectangle(size, size, Color.CYAN);
                      break;
                  case 'Z':
                      this.rectGrid[i][j] = new Rectangle(size, size, Color.BLUE);
                      break;
                  case 'J':
                      this.rectGrid[i][j] = new Rectangle(size, size, Color.MAGENTA);
                      break;
                  case 'L':
                      this.rectGrid[i][j] = new Rectangle(size, size, Color.PINK);
                      break;
                  case 'T':
                      this.rectGrid[i][j] = new Rectangle(size, size, Color.ORANGE);
                      break;
                  default:
                      this.rectGrid[i][j] = new Rectangle(size, size, Color.GRAY);
                      break;
              }
              this.pane.add(this.rectGrid[i][j], j, i + 6);
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

          // TODO
          System.out.println("Press Down");

        }
        if (e.getCode().equals(KeyCode.LEFT)) {
          tetris.move(Direction.LEFT);

          // TODO
          System.out.println("Press Left");
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
      updateGrid();
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

