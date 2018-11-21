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

    // global variable storage for game grid
  private Rectangle[][] rectGrid = new Rectangle[20][10];
    // global variable storage for next grid
  private Rectangle[][] rectNext = new Rectangle[4][4];
    // global variable storage for hold grid
  private Rectangle[][] rectHold = new Rectangle[4][4];
    // global variable storage for linesCleared text
  private Text linesCleared = new Text();
    // global variable storage for title(header) text
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

    // Init header (next, linesCleared)
    initHeader();
    // Init game grid
    initGrid();
    // Init hold section
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

    /**
     * Init Header Section (Next, LinesCleared)
     * Create (Add) value to the global storage variables
     * */
    private void initHeader() {
        // Iterate through the Next Section's global storage variable
        // make sure every grid is filled
        for (int i = 0; i < rectNext.length; i++) {
            for (int j = 0; j < rectNext[0].length; j++) {
                // Copy the next piece
                Piece nextPiece = new Piece(tetris.nextPiece);
                // Get the shape of the next piece
                char shape = nextPiece.shape;
                // Use switch statement to decide which color.etc attributes to
                // assign to the rectangle
                switch (shape) {
                    case 'O':
                        // Check the status of the piece
                        if (i >= 1 && i <= 2 && j >= 1 && j <= 2) {
                            this.rectNext[i][j] = new Rectangle(size, size, Color.RED);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        } else {
                            // If status is none, fill the grid with White rectangle
                            this.rectNext[i][j] = new Rectangle(size, size, Color.WHITE);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        }
                        break;
                    case 'I':
                        // Check the status of the piece
                        if (i == 1 && j <= 3) {
                            this.rectNext[i][j] = new Rectangle(size, size, Color.YELLOW);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        } else {
                            // If status is none, fill the grid with White rectangle
                            this.rectNext[i][j] = new Rectangle(size, size, Color.WHITE);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        }
                        break;
                    case 'S':
                        // Check the status of the piece
                        if ((i == 1 && (j == 1 || j == 2)) || (i == 2 && (j == 0 || j == 1))) {
                            this.rectNext[i][j] = new Rectangle(size, size, Color.CYAN);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        } else {
                            // If status is none, fill the grid with White rectangle
                            this.rectNext[i][j] = new Rectangle(size, size, Color.WHITE);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        }
                        break;
                    case 'Z':
                        // Check the status of the piece
                        if ((i == 1 && (j == 1 || j == 2)) || (i == 2 && (j == 2 || j == 3))) {
                            this.rectNext[i][j] = new Rectangle(size, size, Color.BLUE);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        } else {
                            // If status is none, fill the grid with White rectangle
                            this.rectNext[i][j] = new Rectangle(size, size, Color.WHITE);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        }
                        break;
                    case 'J':
                        // Check the status of the piece
                        if ((i == 1 && j <= 2) || (i == 2 && j == 2)) {
                            this.rectNext[i][j] = new Rectangle(size, size, Color.MAGENTA);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        } else {
                            // If status is none, fill the grid with White rectangle
                            this.rectNext[i][j] = new Rectangle(size, size, Color.WHITE);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        }
                        break;
                    case 'L':
                        // Check the status of the piece
                        if ((i == 1 && (j >= 1 && j <= 3)) || (i == 2 && j == 1)) {
                            this.rectNext[i][j] = new Rectangle(size, size, Color.PINK);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        } else {
                            // If status is none, fill the grid with White rectangle
                            this.rectNext[i][j] = new Rectangle(size, size, Color.WHITE);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        }
                        break;
                    case 'T':
                        // Check the status of the piece
                        if ((i == 1 && (j >= 1 && j <= 3)) || (i == 2 && j == 2)) {
                            this.rectNext[i][j] = new Rectangle(size, size, Color.ORANGE);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        } else {
                            // If status is none, fill the grid with White rectangle
                            this.rectNext[i][j] = new Rectangle(size, size, Color.WHITE);
                            this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        }
                        break;
                    default:
                        // This should not happen, but if the piece is weird shape anyways,
                        // fill the grid with White rectangle
                        this.rectNext[i][j] = new Rectangle(size, size, Color.WHITE);
                        this.pane.add(this.rectNext[i][j], j + 6, i + 2);
                        break;
                }
            }
        }

        // Set Announcement Text -- Message content
        String message = tetris.isGameover ? "Game Over!" : "Tetris";
        // Assign message content
        announcement.setText(message);
        // Assign attributes (font, position, alignment)
        announcement.setFont(new Font(20));
        pane.add(announcement, 0, 0, 8, 2);
        pane.setHalignment(announcement, HPos.CENTER);

        // Set linesCleared Text
        // Assign message content
        linesCleared.setText(String.valueOf(tetris.linesCleared));
        // Assign attributes (font, position, alignment)
        linesCleared.setFont(new Font(20));
        pane.add(linesCleared, 8, 0, 2, 2);
        pane.setHalignment(linesCleared, HPos.CENTER);

    }

    /** Init the game grid. Create and add values for the global
     * storage variable for the game grid. */
    private void initGrid() {
        // Create a temp grid to store the appearance of the game grid
      char[][] grid = new char[20][10];

      // Deep copy thte tetris background grid
      for (int i = 0; i < this.tetris.grid.length; i++) {
          for (int j = 0; j < this.tetris.grid[0].length; j++) {
              grid[i][j] = this.tetris.grid[i][j];
          }
      }

      // Get the active piece
      Piece activePiece = new Piece(tetris.activePiece);
      // Cover the background grid with the active piece to get
      // the appearance
      for (int i = 0; i < activePiece.tiles.length; i++) {
          for (int j = 0; j < activePiece.tiles[0].length; j++) {
              if (activePiece.tiles[i][j] == 1) {
                  grid[i + activePiece.rowOffset][j + activePiece.colOffset] = activePiece.shape;
              }
          }
      }

      // Loop through the grid and add rectangles based on the shape
      for (int i = 0; i < grid.length; i++) {
          for (int j = 0; j < grid[0].length; j++) {
              char shape = grid[i][j];
              switch (shape) {
                  case 'O':
                      // Create rectangle with corresponding color
                      this.rectGrid[i][j] = new Rectangle(size, size, Color.RED);
                      break;
                  case 'I':
                      // Create rectangle with corresponding color
                      this.rectGrid[i][j] = new Rectangle(size, size, Color.YELLOW);
                      break;
                  case 'S':
                      // Create rectangle with corresponding color
                      this.rectGrid[i][j] = new Rectangle(size, size, Color.CYAN);
                      break;
                  case 'Z':
                      this.rectGrid[i][j] = new Rectangle(size, size, Color.BLUE);
                      break;
                  case 'J':
                      // Create rectangle with corresponding color
                      this.rectGrid[i][j] = new Rectangle(size, size, Color.MAGENTA);
                      break;
                  case 'L':
                      // Create rectangle with corresponding color
                      this.rectGrid[i][j] = new Rectangle(size, size, Color.PINK);
                      break;
                  case 'T':
                      // Create rectangle with corresponding color
                      this.rectGrid[i][j] = new Rectangle(size, size, Color.ORANGE);
                      break;
                  default:
                      // Create rectangle with corresponding color --
                      // This should not be reached though
                      this.rectGrid[i][j] = new Rectangle(size, size, Color.GRAY);
                      break;
              }
              // Add the rectangle map to the pane
              this.pane.add(this.rectGrid[i][j], j, i + 6);
          }
      }
    }

    /** Init the hold section grid. If there's no stored piece,
     * leave it blank; otherwise, show that on the left side of the header */
    private void initHold() {
        // Loop through the hold global variable
        for (int i = 0; i < rectHold.length; i++) {
            for (int j = 0; j < rectHold[0].length; j++) {
                // If there's no stored piece on hold, fill the map with
                // white rectangle
                if (tetris.storedPiece == null) {
                    this.rectHold[i][j] = new Rectangle(size, size, Color.WHITE);
                    this.pane.add(this.rectHold[i][j], j, i + 2);
                } else {
                    // Otherwise, get the hold piece
                    Piece holdPiece = new Piece(tetris.storedPiece);
                    char shape = holdPiece.shape;
                    switch (shape) {
                        case 'O':
                            // Check the shape and status of the piece
                            if (i >= 1 && i <= 2 && j >= 1 && j <= 2) {
                                this.rectHold[i][j] = new Rectangle(size, size, Color.RED);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            } else {
                                // If status not satisfied, fill if with White Rectangle
                                this.rectNext[i][j] = new Rectangle(size, size, Color.WHITE);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            }
                            break;
                        case 'I':
                            // Check the shape and status of the piece
                            if (i == 1 && j <= 3) {
                                this.rectHold[i][j] = new Rectangle(size, size, Color.YELLOW);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            } else {
                                // If status not satisfied, fill if with White Rectangle
                                this.rectHold[i][j] = new Rectangle(size, size, Color.WHITE);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            }
                            break;
                        case 'S':
                            // Check the shape and status of the piece
                            if ((i == 1 && (j == 1 || j == 2)) || (i == 2 && (j == 0 || j == 1))) {
                                this.rectHold[i][j] = new Rectangle(size, size, Color.CYAN);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            } else {
                                // If status not satisfied, fill if with White Rectangle
                                this.rectHold[i][j] = new Rectangle(size, size, Color.WHITE);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            }
                            break;
                        case 'Z':
                            // Check the shape and status of the piece
                            if ((i == 1 && (j == 1 || j == 2)) || (i == 2 && (j == 2 || j == 3))) {
                                this.rectHold[i][j] = new Rectangle(size, size, Color.BLUE);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            } else {
                                // If status not satisfied, fill if with White Rectangle
                                this.rectHold[i][j] = new Rectangle(size, size, Color.WHITE);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            }
                            break;
                        case 'J':
                            // Check the shape and status of the piece
                            if ((i == 1 && j <= 2) || (i == 2 && j == 2)) {
                                this.rectHold[i][j] = new Rectangle(size, size, Color.MAGENTA);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            } else {
                                // If status not satisfied, fill if with White Rectangle
                                this.rectHold[i][j] = new Rectangle(size, size, Color.WHITE);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            }
                            break;
                        case 'L':
                            // Check the shape and status of the piece
                            if ((i == 1 && (j >= 1 && j <= 3)) || (i == 2 && j == 1)) {
                                this.rectHold[i][j] = new Rectangle(size, size, Color.PINK);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            } else {
                                // If status not satisfied, fill if with White Rectangle
                                this.rectHold[i][j] = new Rectangle(size, size, Color.WHITE);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            }
                            break;
                        case 'T':
                            // Check the shape and status of the piece
                            if ((i == 1 && (j >= 1 && j <= 3)) || (i == 2 && j == 2)) {
                                this.rectHold[i][j] = new Rectangle(size, size, Color.ORANGE);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            } else {
                                // If status not satisfied, fill if with White Rectangle
                                this.rectHold[i][j] = new Rectangle(size, size, Color.WHITE);
                                this.pane.add(this.rectHold[i][j], j, i + 2);
                            }
                            break;
                        default:
                            // If status not satisfied, fill if with White Rectangle
                            // Here should not be reached though
                            this.rectHold[i][j] = new Rectangle(size, size, Color.WHITE);
                            this.pane.add(this.rectHold[i][j], j, i + 2);
                            break;
                    }
                }
            }
        }
    }

    /**
     * Update the header section by updating colors of all rectangles
     * */
    private void updateHeader() {
        // Iterate through the Next Section's global storage variable
        // make sure every grid is filled
        for (int i = 0; i < rectNext.length; i++) {
            for (int j = 0; j < rectNext[0].length; j++) {
                // Copy the next piece
                Piece nextPiece = new Piece(tetris.nextPiece);
                // Get the shape of the next piece
                char shape = nextPiece.shape;
                // Use switch statement to decide which color.etc attributes to
                // assign to the rectangle
                switch (shape) {
                    case 'O':
                        // Check the status of the piece to update the color
                        // of each rectangle
                        if (i >= 1 && i <= 2 && j >= 1 && j <= 2) {
                            this.rectNext[i][j].setFill(Color.RED);
                        } else {
                            // If status not satisfied, fill the rectangleWhite Color
                            this.rectNext[i][j].setFill(Color.WHITE);
                        }
                        break;
                    case 'I':
                        // Check the status of the piece to update the color
                        // of each rectangle
                        if (i == 1 && j <= 3) {
                            this.rectNext[i][j].setFill(Color.YELLOW);
                        } else {
                            // If status not satisfied, fill the rectangleWhite Color
                            this.rectNext[i][j].setFill(Color.WHITE);
                        }
                        break;
                    case 'S':
                        // Check the status of the piece to update the color
                        // of each rectangle
                        if ((i == 1 && (j == 1 || j == 2)) || (i == 2 && (j == 0 || j == 1))) {
                            this.rectNext[i][j].setFill(Color.CYAN);
                        } else {
                            // If status not satisfied, fill the rectangleWhite Color
                            this.rectNext[i][j].setFill(Color.WHITE);
                        }
                        break;
                    case 'Z':
                        // Check the status of the piece to update the color
                        // of each rectangle
                        if ((i == 1 && (j == 1 || j == 2)) || (i == 2 && (j == 2 || j == 3))) {
                            this.rectNext[i][j].setFill(Color.BLUE);
                        } else {
                            // If status not satisfied, fill the rectangleWhite Color
                            this.rectNext[i][j].setFill(Color.WHITE);
                        }
                        break;
                    case 'J':
                        // Check the status of the piece to update the color
                        // of each rectangle
                        if ((i == 1 && j <= 2) || (i == 2 && j == 2)) {
                            this.rectNext[i][j].setFill(Color.MAGENTA);
                        } else {
                            // If status not satisfied, fill the rectangleWhite Color
                            this.rectNext[i][j] .setFill(Color.WHITE);
                        }
                        break;
                    case 'L':
                        // Check the status of the piece to update the color
                        // of each rectangle
                        if ((i == 1 && (j >= 1 && j <= 3)) || (i == 2 && j == 1)) {
                            this.rectNext[i][j].setFill(Color.PINK);
                        } else {
                            // If status not satisfied, fill the rectangleWhite Color
                            this.rectNext[i][j].setFill(Color.WHITE);
                        }
                        break;
                    case 'T':
                        // Check the status of the piece to update the color
                        // of each rectangle
                        if ((i == 1 && (j >= 1 && j <= 3)) || (i == 2 && j == 2)) {
                            this.rectNext[i][j].setFill(Color.ORANGE);
                        } else {
                            // If status not satisfied, fill the rectangleWhite Color
                            this.rectNext[i][j].setFill(Color.WHITE);
                        }
                        break;
                    default:
                        // If status not satisfied, fill the rectangleWhite Color
                        // Here should not be reached though
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

    /**
     * Update the grid content by updating the colors of each rectangle
     * */
    private void updateGrid() {
        // Create a temp grid to store the appearance of the game grid
        char[][] grid = new char[20][10];
        // Deep copy thte tetris background grid
        for (int i = 0; i < this.tetris.grid.length; i++) {
            for (int j = 0; j < this.tetris.grid[0].length; j++) {
                grid[i][j] = this.tetris.grid[i][j];
            }
        }
        // Get the active piece
        Piece activePiece = new Piece(tetris.activePiece);
        // Cover the background grid with the active piece to get
        // the appearance
        for (int i = 0; i < activePiece.tiles.length; i++) {
            for (int j = 0; j < activePiece.tiles[0].length; j++) {
                if (activePiece.tiles[i][j] == 1) {
                    grid[i + activePiece.rowOffset][j + activePiece.colOffset] = activePiece.shape;
                }
            }
        }
        // Loop through the grid and add rectangles based on the shape
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                char shape = grid[i][j];
                switch (shape) {
                    case 'O':
                        // Update the color of the corresponding rectangle
                        // to the grid by the shape
                        this.rectGrid[i][j].setFill(Color.RED);
                        break;
                    case 'I':
                        // Update the color of the corresponding rectangle
                        // to the grid by the shape
                        this.rectGrid[i][j].setFill(Color.YELLOW);
                        break;
                    case 'S':
                        // Update the color of the corresponding rectangle
                        // to the grid by the shape
                        this.rectGrid[i][j].setFill(Color.CYAN);
                        break;
                    case 'Z':
                        // Update the color of the corresponding rectangle
                        // to the grid by the shape
                        this.rectGrid[i][j].setFill(Color.BLUE);
                        break;
                    case 'J':
                        // Update the color of the corresponding rectangle
                        // to the grid by the shape
                        this.rectGrid[i][j].setFill(Color.MAGENTA);
                        break;
                    case 'L':
                        // Update the color of the corresponding rectangle
                        // to the grid by the shape
                        this.rectGrid[i][j].setFill(Color.PINK);
                        break;
                    case 'T':
                        // Update the color of the corresponding rectangle
                        // to the grid by the shape
                        this.rectGrid[i][j].setFill(Color.ORANGE);
                        break;
                    default:
                        this.rectGrid[i][j].setFill(Color.GRAY);
                        break;
                }
            }
        }
    }

    /**
     * Update the color section of the hold section.
     * */
    private void updateHold() {
        // Loop through the hold global variable
        for (int i = 0; i < rectHold.length; i++) {
            for (int j = 0; j < rectHold[0].length; j++) {
                // If there's no stored piece on hold, fill the map with
                // white rectangle
                if (tetris.storedPiece == null) {
                    this.rectHold[i][j].setFill(Color.WHITE);
                } else {
                    // Otherwise, get the hold piece
                    Piece holdPiece = new Piece(tetris.storedPiece);
                    char shape = holdPiece.shape;
                    switch (shape) {
                        case 'O':
                            // Update the color of the grid by the shape
                            if (i >= 1 && i <= 2 && j >= 1 && j <= 2) {
                                this.rectHold[i][j].setFill(Color.RED);
                            } else {
                                // If condition not satisfied, fill the color
                                // with white
                                this.rectNext[i][j].setFill(Color.WHITE);
                            }
                            break;
                        case 'I':
                            // Update the color of the grid by the shape
                            if (i == 1 && j <= 3) {
                                this.rectHold[i][j].setFill(Color.YELLOW);
                            } else {
                                // If condition not satisfied, fill the color
                                // with white
                                this.rectHold[i][j].setFill(Color.WHITE);
                            }
                            break;
                        case 'S':
                            // Update the color of the grid by the shape
                            if ((i == 1 && (j == 1 || j == 2)) || (i == 2 && (j == 0 || j == 1))) {
                                this.rectHold[i][j].setFill(Color.CYAN);
                            } else {
                                // If condition not satisfied, fill the color
                                // with white
                                this.rectHold[i][j].setFill(Color.WHITE);
                            }
                            break;
                        case 'Z':
                            // Update the color of the grid by the shape
                            if ((i == 1 && (j == 1 || j == 2)) || (i == 2 && (j == 2 || j == 3))) {
                                this.rectHold[i][j].setFill(Color.BLUE);
                            } else {
                                // If condition not satisfied, fill the color
                                // with white
                                this.rectHold[i][j].setFill(Color.WHITE);
                            }
                            break;
                        case 'J':
                            // Update the color of the grid by the shape
                            if ((i == 1 && j <= 2) || (i == 2 && j == 2)) {
                                this.rectHold[i][j].setFill(Color.MAGENTA);
                            } else {
                                // If condition not satisfied, fill the color
                                // with white
                                this.rectHold[i][j].setFill(Color.WHITE);
                            }
                            break;
                        case 'L':
                            // Update the color of the grid by the shape
                            if ((i == 1 && (j >= 1 && j <= 3)) || (i == 2 && j == 1)) {
                                this.rectHold[i][j].setFill(Color.PINK);
                            } else {
                                // If condition not satisfied, fill the color
                                // with white
                                this.rectHold[i][j].setFill(Color.WHITE);
                            }
                            break;
                        case 'T':
                            // Update the color of the grid by the shape
                            if ((i == 1 && (j >= 1 && j <= 3)) || (i == 2 && j == 2)) {
                                this.rectHold[i][j].setFill(Color.ORANGE);
                            } else {
                                // If condition not satisfied, fill the color
                                // with white
                                this.rectHold[i][j].setFill(Color.WHITE);
                            }
                            break;
                        default:
                            // If condition not satisfied, fill the color
                            // with white
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
          // Set Announcement Text
          String message = tetris.isGameover ? "Game Over!" : "Tetris";
          announcement.setText(message);

          // Set linesCleared Text
          linesCleared.setText(String.valueOf(tetris.linesCleared));
      } else {
        // if game is not over
          // Respond to press up
        if (e.getCode().equals(KeyCode.UP)) {
          tetris.rotate();
        }
          // Respond to press down
        if (e.getCode().equals(KeyCode.DOWN)) {
          tetris.move(Direction.DOWN);
        }
          // Respond to press left
        if (e.getCode().equals(KeyCode.LEFT)) {
          tetris.move(Direction.LEFT);
        }
          // Respond to press right
        if (e.getCode().equals(KeyCode.RIGHT)) {
          tetris.move(Direction.RIGHT);
        }
          // Respond to press space
        if (e.getCode().equals(KeyCode.SPACE)) {
          tetris.drop();
        }
          // Respond to press hold
        if (e.getCode().equals(KeyCode.Z)) {
            tetris.hold();
        }
          // Respond to press output
        if (e.getCode().equals(KeyCode.O)) {
          try {
            tetris.outputToFile();
          } catch (IOException err) {
            System.out.println(err.getMessage());
          }
        }
      }
      // Update the header
      updateHeader();
      // Update the grid
      updateGrid();
      // Update the hole
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

