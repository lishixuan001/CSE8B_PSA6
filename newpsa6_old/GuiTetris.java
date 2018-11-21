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

  // given tetris, pane and myHeyHandler
  private Tetris tetris;
  private GridPane pane;
  private MyKeyHandler myKeyHandler;
  // size of regular small rectangle
  private static final int size = 25;

  private Rectangle[][] rectGrid = new Rectangle[20][10];
  private Rectangle[][] rectNext = new Rectangle[4][4];
  private Rectangle[][] rectHold = new Rectangle[4][4];

  private Text linesCleared = new Text();
  private Text announcement = new Text();


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

    initHeader();
    initGrid();
    initHold();


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

    private void initHeader() {
        for (int i = 0; i < rectNext.length; i++) {
            for (int j = 0; j < rectNext[0].length; j++) {
                Piece nextPiece = new Piece(tetris.nextPiece);
                char shape = nextPiece.shape;
                switch (shape) {
                    case 'O':
                        if (i >= 1 && i <= 2 && j >= 1 && j <= 2) {
                            this.rectNext[i][j] = new Rectangle(size, size, Color.RED);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        } else {
                            this.rectNext[i][j] = new Rectangle(size, size, Color.WHITE);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        }
                        break;
                    case 'I':
                        if (i == 1 && j <= 3) {
                            this.rectNext[i][j] = new Rectangle(size, size, Color.YELLOW);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        } else {
                            this.rectNext[i][j] = new Rectangle(size, size, Color.WHITE);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        }
                        break;
                    case 'S':
                        if ((i == 1 && (j == 1 || j == 2)) || (i == 2 && (j == 0 || j == 1))) {
                            this.rectNext[i][j] = new Rectangle(size, size, Color.CYAN);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        } else {
                            this.rectNext[i][j] = new Rectangle(size, size, Color.WHITE);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        }
                        break;
                    case 'Z':
                        if ((i == 1 && (j == 1 || j == 2)) || (i == 2 && (j == 2 || j == 3))) {
                            this.rectNext[i][j] = new Rectangle(size, size, Color.BLUE);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        } else {
                            this.rectNext[i][j] = new Rectangle(size, size, Color.WHITE);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        }
                        break;
                    case 'J':
                        if ((i == 1 && j <= 2) || (i == 2 && j == 2)) {
                            this.rectNext[i][j] = new Rectangle(size, size, Color.MAGENTA);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        } else {
                            this.rectNext[i][j] = new Rectangle(size, size, Color.WHITE);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        }
                        break;
                    case 'L':
                        if ((i == 1 && (j >= 1 && j <= 3)) || (i == 2 && j == 1)) {
                            this.rectNext[i][j] = new Rectangle(size, size, Color.PINK);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        } else {
                            this.rectNext[i][j] = new Rectangle(size, size, Color.WHITE);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        }
                        break;
                    case 'T':
                        if ((i == 1 && (j >= 1 && j <= 3)) || (i == 2 && j == 2)) {
                            this.rectNext[i][j] = new Rectangle(size, size, Color.ORANGE);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        } else {
                            this.rectNext[i][j] = new Rectangle(size, size, Color.WHITE);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        }
                        break;
                    default:
                        this.rectNext[i][j] = new Rectangle(size, size, Color.WHITE);
                        this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        break;
                }
            }
        }

        // Set Announcement Text
        String message = tetris.isGameover ? "Game Over!" : "Tetris";
        announcement.setText(message);
        announcement.setFont(new Font(20));
        pane.add(announcement, 0, 0, 8, 2);
        pane.setHalignment(announcement, HPos.CENTER);

        // Set linesCleared Text
        linesCleared.setText(String.valueOf(tetris.linesCleared));
        linesCleared.setFont(new Font(20));
        pane.add(linesCleared, 8, 0, 2, 2);
        pane.setHalignment(linesCleared, HPos.CENTER);

    }

    private void initGrid() {
      char[][] grid = new char[20][10];

      for (int i = 0; i < this.tetris.grid.length; i++) {
          for (int j = 0; j < this.tetris.grid[0].length; j++) {
              grid[i][j] = this.tetris.grid[i][j];
          }
      }

      Piece activePiece = new Piece(tetris.activePiece);
      for (int i = 0; i < activePiece.tiles.length; i++) {
          for (int j = 0; j < activePiece.tiles[0].length; j++) {
              if (activePiece.tiles[i][j] == 1) {
                  grid[i + activePiece.rowOffset][j + activePiece.colOffset] = activePiece.shape;
              }
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

    private void initHold() {
        for (int i = 0; i < rectHold.length; i++) {
            for (int j = 0; j < rectHold[0].length; j++) {
                if (tetris.storedPiece == null) {
                    this.rectHold[i][j] = new Rectangle(size, size, Color.WHITE);
                    this.pane.add(this.rectHold[i][j], j, i + 2);
                } else {
                    Piece holdPiece = new Piece(tetris.storedPiece);
                    char shape = holdPiece.shape;
                    switch (shape) {
                        case 'O':
                            if (i >= 1 && i <= 2 && j >= 1 && j <= 2) {
                                this.rectHold[i][j] = new Rectangle(size, size, Color.RED);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            } else {
                                this.rectNext[i][j] = new Rectangle(size, size, Color.WHITE);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            }
                            break;
                        case 'I':
                            if (i == 1 && j <= 3) {
                                this.rectHold[i][j] = new Rectangle(size, size, Color.YELLOW);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            } else {
                                this.rectHold[i][j] = new Rectangle(size, size, Color.WHITE);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            }
                            break;
                        case 'S':
                            if ((i == 1 && (j == 1 || j == 2)) || (i == 2 && (j == 0 || j == 1))) {
                                this.rectHold[i][j] = new Rectangle(size, size, Color.CYAN);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            } else {
                                this.rectHold[i][j] = new Rectangle(size, size, Color.WHITE);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            }
                            break;
                        case 'Z':
                            if ((i == 1 && (j == 1 || j == 2)) || (i == 2 && (j == 2 || j == 3))) {
                                this.rectHold[i][j] = new Rectangle(size, size, Color.BLUE);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            } else {
                                this.rectHold[i][j] = new Rectangle(size, size, Color.WHITE);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            }
                            break;
                        case 'J':
                            if ((i == 1 && j <= 2) || (i == 2 && j == 2)) {
                                this.rectHold[i][j] = new Rectangle(size, size, Color.MAGENTA);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            } else {
                                this.rectHold[i][j] = new Rectangle(size, size, Color.WHITE);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            }
                            break;
                        case 'L':
                            if ((i == 1 && (j >= 1 && j <= 3)) || (i == 2 && j == 1)) {
                                this.rectHold[i][j] = new Rectangle(size, size, Color.PINK);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            } else {
                                this.rectHold[i][j] = new Rectangle(size, size, Color.WHITE);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            }
                            break;
                        case 'T':
                            if ((i == 1 && (j >= 1 && j <= 3)) || (i == 2 && j == 2)) {
                                this.rectHold[i][j] = new Rectangle(size, size, Color.ORANGE);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            } else {
                                this.rectHold[i][j] = new Rectangle(size, size, Color.WHITE);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            }
                            break;
                        default:
                            this.rectHold[i][j] = new Rectangle(size, size, Color.WHITE);
                            this.pane.add(this.rectHold[i][j], j, i + 2);
                            break;
                    }
                }
            }
        }
    }

    private void updateHeader() {
        for (int i = 0; i < rectNext.length; i++) {
            for (int j = 0; j < rectNext[0].length; j++) {
                Piece nextPiece = new Piece(tetris.nextPiece);
                char shape = nextPiece.shape;
                switch (shape) {
                    case 'O':
                        if (i >= 1 && i <= 2 && j >= 1 && j <= 2) {
                            this.rectNext[i][j].setFill(Color.RED);
                        } else {
                            this.rectNext[i][j].setFill(Color.WHITE);
                        }
                        break;
                    case 'I':
                        if (i == 1 && j <= 3) {
                            this.rectNext[i][j].setFill(Color.YELLOW);
                        } else {
                            this.rectNext[i][j].setFill(Color.WHITE);
                        }
                        break;
                    case 'S':
                        if ((i == 1 && (j == 1 || j == 2)) || (i == 2 && (j == 0 || j == 1))) {
                            this.rectNext[i][j].setFill(Color.CYAN);
                        } else {
                            this.rectNext[i][j].setFill(Color.WHITE);
                        }
                        break;
                    case 'Z':
                        if ((i == 1 && (j == 1 || j == 2)) || (i == 2 && (j == 2 || j == 3))) {
                            this.rectNext[i][j].setFill(Color.BLUE);
                        } else {
                            this.rectNext[i][j].setFill(Color.WHITE);
                        }
                        break;
                    case 'J':
                        if ((i == 1 && j <= 2) || (i == 2 && j == 2)) {
                            this.rectNext[i][j].setFill(Color.MAGENTA);
                        } else {
                            this.rectNext[i][j] .setFill(Color.WHITE);
                        }
                        break;
                    case 'L':
                        if ((i == 1 && (j >= 1 && j <= 3)) || (i == 2 && j == 1)) {
                            this.rectNext[i][j].setFill(Color.PINK);
                        } else {
                            this.rectNext[i][j].setFill(Color.WHITE);
                        }
                        break;
                    case 'T':
                        if ((i == 1 && (j >= 1 && j <= 3)) || (i == 2 && j == 2)) {
                            this.rectNext[i][j].setFill(Color.ORANGE);
                        } else {
                            this.rectNext[i][j].setFill(Color.WHITE);
                        }
                        break;
                    default:
                        this.rectNext[i][j].setFill(Color.WHITE);
                        break;
                }
            }
        }

        // Set Announcement Text
        String message = tetris.isGameover ? "Game Over!" : "Tetris";
        announcement.setText(message);

        // Set linesCleared Text
        linesCleared.setText(String.valueOf(tetris.linesCleared));

    }

    private void updateGrid() {
        char[][] grid = new char[20][10];
        for (int i = 0; i < this.tetris.grid.length; i++) {
            for (int j = 0; j < this.tetris.grid[0].length; j++) {
                grid[i][j] = this.tetris.grid[i][j];
            }
        }

        Piece activePiece = new Piece(tetris.activePiece);
        for (int i = 0; i < activePiece.tiles.length; i++) {
            for (int j = 0; j < activePiece.tiles[0].length; j++) {
                if (activePiece.tiles[i][j] == 1) {
                    grid[i + activePiece.rowOffset][j + activePiece.colOffset] = activePiece.shape;
                }
            }
        }

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                char shape = grid[i][j];
                switch (shape) {
                    case 'O':
                        this.rectGrid[i][j].setFill(Color.RED);
                        break;
                    case 'I':
                        this.rectGrid[i][j].setFill(Color.YELLOW);
                        break;
                    case 'S':
                        this.rectGrid[i][j].setFill(Color.CYAN);
                        break;
                    case 'Z':
                        this.rectGrid[i][j].setFill(Color.BLUE);
                        break;
                    case 'J':
                        this.rectGrid[i][j].setFill(Color.MAGENTA);
                        break;
                    case 'L':
                        this.rectGrid[i][j].setFill(Color.PINK);
                        break;
                    case 'T':
                        this.rectGrid[i][j].setFill(Color.ORANGE);
                        break;
                    default:
                        this.rectGrid[i][j].setFill(Color.GRAY);
                        break;
                }
            }
        }
    }

    private void updateHold() {
        for (int i = 0; i < rectHold.length; i++) {
            for (int j = 0; j < rectHold[0].length; j++) {
                if (tetris.storedPiece == null) {
                    this.rectHold[i][j].setFill(Color.WHITE);
                } else {
                    Piece holdPiece = new Piece(tetris.storedPiece);
                    char shape = holdPiece.shape;
                    switch (shape) {
                        case 'O':
                            if (i >= 1 && i <= 2 && j >= 1 && j <= 2) {
                                this.rectHold[i][j].setFill(Color.RED);
                            } else {
                                this.rectNext[i][j].setFill(Color.WHITE);
                            }
                            break;
                        case 'I':
                            if (i == 1 && j <= 3) {
                                this.rectHold[i][j].setFill(Color.YELLOW);
                            } else {
                                this.rectHold[i][j].setFill(Color.WHITE);
                            }
                            break;
                        case 'S':
                            if ((i == 1 && (j == 1 || j == 2)) || (i == 2 && (j == 0 || j == 1))) {
                                this.rectHold[i][j].setFill(Color.CYAN);
                            } else {
                                this.rectHold[i][j].setFill(Color.WHITE);
                            }
                            break;
                        case 'Z':
                            if ((i == 1 && (j == 1 || j == 2)) || (i == 2 && (j == 2 || j == 3))) {
                                this.rectHold[i][j].setFill(Color.BLUE);
                            } else {
                                this.rectHold[i][j].setFill(Color.WHITE);
                            }
                            break;
                        case 'J':
                            if ((i == 1 && j <= 2) || (i == 2 && j == 2)) {
                                this.rectHold[i][j].setFill(Color.MAGENTA);
                            } else {
                                this.rectHold[i][j].setFill(Color.WHITE);
                            }
                            break;
                        case 'L':
                            if ((i == 1 && (j >= 1 && j <= 3)) || (i == 2 && j == 1)) {
                                this.rectHold[i][j].setFill(Color.PINK);
                            } else {
                                this.rectHold[i][j].setFill(Color.WHITE);
                            }
                            break;
                        case 'T':
                            if ((i == 1 && (j >= 1 && j <= 3)) || (i == 2 && j == 2)) {
                                this.rectHold[i][j].setFill(Color.ORANGE);
                            } else {
                                this.rectHold[i][j].setFill(Color.WHITE);
                            }
                            break;
                        default:
                            this.rectHold[i][j].setFill(Color.WHITE);
                            break;
                    }
                }
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
        // FIXME
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
      updateHeader();
      updateGrid();
      updateHold();
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

