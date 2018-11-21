*Author: Yiwen Li*
*CSE Account: cs8bfcs*
*Date: 11/17/2018*

# Program Description
This program designs the Graphic User Interface for Tetris game.
It is based on PSA3, the back-end function program of Tetris game.
This program allows users to use directional input command to manipulate
the tetris, including drop, rotate, move down, move left, move right, and
output to file. This program is very useful because it visualize the tetris
game and update the status of the game for the user.

# Short Response
## Unix/Linux Questions:
1. mkdir -p fooDir/barDir, This can work because 
the -p (i.e., parents) option creates the specified intermediate directories 
for a new directory if they do not already exist. For example, it can be used 
to create the following directory structure.

2. ls *.html *.txt, the example command would tell the ls command 
(which is used to list files) to provide the names of all files in the 
current directory that have an .html or a .txt extension

3. ls -LR is to display all files,directories and sub directories in
home directory 

## JavaFX Questions:
1. Yes, Because GridPane is a subclass of Node.
To do so you must add the GridPane instance to a Scene object,
or add the GridPane to a layout component which is added to a Scene object.
It is said that GridPane.add() method is able to compartmentalize different
types of objects, so GridPane sub-object is also an object, which can be
added to GridPane.

2. Because MoveDownWorker is a private class, if we define it outside the GuiTetris class,
we won't be able to access it. To call a private method, it has to be in the
same class. Also, we need it to directly change the initialized game object, it is easier
to keep it consistent by putting it in the same class

3. The alternative way to define the event handle is to use
setOnKeyPressed(EventHandler<? super KeyEvent> value)
For example,
scene.setOnKeyPressed(new EventHandler (final KeyEvent keyEvent) -> {
public void handle(....){} }

4. The JavaFX Scene class is the container for all content in a scene graph.
Scene is used for layouting and handling events, so with Scene, we can properly
shows the pane in the window when running the program. It allows listeners that
can detext the Keyevent by KeyCode to control GUI implementations.
