package edu.sdccd.cisc191.template;

// import statements
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.io.*;

// The TicTacToe class extends the JavaFX Application
public class TicTacToe extends Application{

    // Making labels to display the score for players X and O
    // and the current player's turn
    private GameBoardLabel Xscore = new GameBoardLabel();
    private GameBoardLabel Oscore = new GameBoardLabel();
    private GameBoardLabel Turn = new GameBoardLabel();

    // Variables to store the number of wins for players X and O
    private int Xwins = 0; // Tracks the number of games X has won
    private int Owins = 0; // Tracks the number of games O has won


    public static Button[][] board = new Button[3][3];; //array representation of the game board
    private boolean x = true; // tracks whether it's X's or O'x turn
    public Button[][] buttons = new Button[3][3];


    boolean gameOver = false;

    /**
     * launches the javaFX applicatgion
     * @param args
     */
    public static void main(String[] args) {
        // launches the application
        launch(args);
    }

    /**
     * Method for updating the header
     */
    public void updateHeader() {
        // update labels
        // changes the text depending how many fishes or guesses are remaining in
        // the game
        Xscore.setText("X: " +  Xwins);
        Oscore.setText("O: " +  Owins);
        Turn.setText("Turn: " + getCurrentTurn());



    }

    /**
     *
     * @param primayStage the primary stage for this application, onto which
     * the application scene can be set. The primary stage will be embedded in
     * the browser if the application was launched as an applet.
     * Applications may create other stages, if needed, but they will not be
     * primary stages and will not be embedded in the browser.
     */
    public void start(Stage primayStage) {

        Button restartButton = new Button("Restart");
        restartButton.setOnAction(event ->{
            restart();
        });
        gameOver = false;

        // SAVE GAME BUTTON
        Button saveButton = new Button("Save Game");
        saveButton.setOnAction(event -> saveGame());

        // LOAD GAME BUTTON
        Button loadButton = new Button("Load Game");
        loadButton.setOnAction(event -> loadGame());

        GridPane grid = new GridPane();
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(grid);
        HBox hbox = new HBox(Xscore, Oscore, Turn, restartButton, saveButton, loadButton);
        borderPane.setTop(hbox);
        x = !x;

        updateHeader();


        // nested for loop to create a 3X3 grid of buttons
        for(int row = 0; row < 3; row++){
            for(int column = 0; column < 3; column++){
                GameBoardButton button = new GameBoardButton(row, column, this);


                // size of each button
                button.setMinSize(160,150);
                // set the font size of the text to 50
                button.setStyle("-fx-font-size: 50;");

                // Create final variables for row and column
                final int r = row;
                final int c = column;

                // Sets the action event for when the button is clicked
                button.setOnAction(event -> {
                    button.handleButtonClick();
                    TicTacToe.board[r][c] = button;
                    // stores the clicked button in the corresponding position
                    //buttons[r][c] = button;
                });
                grid.add(button,column,row);
                buttons[row][column]=button;


            }
        }

        Scene scene = new Scene(borderPane,470, 490);

        // Sets the title of the game window
        primayStage.setTitle("Tic -  Tac - Toe");

        // Set the scene of the primary stage to the one containing the buttons
        primayStage.setScene(scene);

        // Show the game window
        primayStage.show();


    } // end start();

    /** Method for saving the game to a file
     *
     */

    private void saveGame() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("tic_tac_toe_save.dat"))) {
            // Save the board state
            String[][] boardState = new String[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    boardState[i][j] = buttons[i][j].getText();
                }
            }
            out.writeObject(boardState);

            out.writeBoolean(x);  // Save whose turn it is
            out.writeInt(Xwins);      // Save X's score
            out.writeInt(Owins);      // Save O's score
            System.out.println("Game saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving the game: " + e.getMessage());
        }
    }

    /** Method for loading in the saved game
     *
     */

    private void loadGame() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("tic_tac_toe_save.dat"))) {
            // Load the board state
            String[][] boardState = (String[][]) in.readObject();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    buttons[i][j].setText(boardState[i][j]);
                }
            }
            x = in.readBoolean();  // Load whose turn it is
            Xwins = in.readInt();      // Load X's score
            Owins = in.readInt();      // Load O's score
            // Calling the updateHeader method
            updateHeader();
            Check();

            System.out.println("Game loaded successfully!.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading the game: " + e.getMessage());
        }

    }

    /**  Method for restarting the game
     *
     */

    public void restart() {
        // iterating through each row and column
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText("");  // Clear the text on each button
                buttons[row][col].setDisable(false); // Re-enable the button
            }
        }
        x = true;
        // Calling the updateHeader method
        updateHeader();
    }

    /**  Method for knowing whose turn it is
     *
     * @return
     */

    public String getCurrentTurn(){
        String turn;
        if(x){
            turn="X";
        }
        else{
            turn="0";
        }
        return turn;
    }

    /** This method switches the current player's turn.
     *  The variable 'x' represents whether it's X's turn.
     *   If 'x' is true, it's X's turn, and if false, it's O's turn.
     *   The method toggles the value of 'x', so the turn is switched
     *   between X and O after each call.
     */
    public void SwitchTurn(){

        x=!x; // tggle the value of "x"
    }

    /**
     * Method for disabling the buttons on the game board
     */
    public void disableBoard() {

        // Iterate through each row of the 3x3 grid
        for (int row = 0; row < 3; row++) {
            // For each row, iterate through each column
            for (int col = 0; col < 3; col++) {
                // Disable the button at the current row and column which makes it
                // unclickable
            buttons[row][col].setDisable(true);
            }
        }
    }

    /**
     * Method for checking who won
     */
    public void Check(){
        int count = 0;
        // Check rows
        for (int row = 0; row < 3; row++) {
            if (buttons[row][0].getText().equals("")) {
                continue; // Skip if first cell in the row is empty
            }
            if (buttons[row][0].getText().equals(buttons[row][1].getText()) &&
                    buttons[row][0].getText().equals(buttons[row][2].getText())) {

                gameOver = true;
                disableBoard();

                if (getCurrentTurn().equals("X")) {
                    Xwins++;  // Increment X wins
                    Xscore.setText("X: " + Xwins);  // Update X score label
                } else {
                    Owins++;  // Increment O wins
                    Oscore.setText("O: " + Owins);  // Update O score label
                }

                System.out.println(getCurrentTurn() + " wins");
                return;
            }
        }

        // Check columns
        for (int col = 0; col < 3; col++) {
            if (buttons[0][col].getText().equals("")) {
                continue;
            }
            if (buttons[0][col].getText().equals(buttons[1][col].getText()) &&
                    buttons[0][col].getText().equals(buttons[2][col].getText())) {

                gameOver = true;
                disableBoard();

                if (getCurrentTurn().equals("X")) {
                    Xwins++;
                    Xscore.setText("X: " + Xwins);
                } else {
                    Owins++;
                    Oscore.setText("O: " + Owins);
                }

                System.out.println(getCurrentTurn() + " wins");
                return;
            }
        }

        // Check diagonal (top-left to bottom-right)
        if (buttons[0][0].getText().equals(buttons[1][1].getText()) &&
                buttons[0][0].getText().equals(buttons[2][2].getText()) &&
                !buttons[0][0].getText().isEmpty()) {

            gameOver = true;
            disableBoard();

            if (getCurrentTurn().equals("X")) {
                Xwins++;
                Xscore.setText("X: " + Xwins);
            } else {
                Owins++;
                Oscore.setText("O: " + Owins);
            }

            System.out.println(getCurrentTurn() + " wins");
            return;
        }

        // Check diagonal (top-right to bottom-left)
        if (buttons[0][2].getText().equals(buttons[1][1].getText()) &&
                buttons[0][2].getText().equals(buttons[2][0].getText()) &&
                !buttons[0][2].getText().isEmpty()) {

            gameOver = true;
            disableBoard();

            if (getCurrentTurn().equals("X")) {
                Xwins++;
                Xscore.setText("X: " + Xwins);
            } else {
                Owins++;
                Oscore.setText("O: " + Owins);
            }

            System.out.println(getCurrentTurn() + " wins");
            return;
        }

        // Check for tie
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!buttons[i][j].getText().isEmpty()) {
                    count++;
                }
            }
        }

        if (count == 9) {
            System.out.println("It's a tie!");
            disableBoard();
        }
    }




}


