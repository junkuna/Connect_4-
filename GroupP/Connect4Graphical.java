
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;


import javax.sound.sampled.AudioSystem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.applet.AudioClip;

public class Connect4Graphical extends JPanel {
    // declare variables

    private char[][] board;
    private char currentPlayer;
    private boolean gameEnded;

    // Make own board variables
    private int boardWidth;
    private int boardHeight;

    // declare start menu
    private static final String[] MENU_OPTIONS = {"Start Game", "Exit"};

    //set the AI
    private static boolean playAgainstAI;


    // show the menu
    private void showMenu() {

        //Load the logo image

        ImageIcon logoIcon = new ImageIcon(getClass().getResource("logo.png"));

        int choice = JOptionPane.showOptionDialog(
                null,  //edit null because we have logo sign
                null,  //edit null because we have logo sign
                null,  //edit null because we have logo sign
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                logoIcon, //Set the logo image as the message
                MENU_OPTIONS,
                MENU_OPTIONS[0]
        );

        // Start Game
        if (choice == 0) {
            // Prompt for board size
            String widthInput = JOptionPane.showInputDialog(null, "Enter the board width:");
            String heightInput = JOptionPane.showInputDialog(null, "Enter the board height:");
            try {
                int width = Integer.parseInt(widthInput);
                int height = Integer.parseInt(heightInput);
                initializeBoard(height, width);

                currentPlayer = 'X';
                gameEnded = false;
                calculatePanelSize();
                setPreferredSize(new Dimension(panelWidth, panelHeight));
                repaint();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid board size input. Please try again!");


                showMenu();  //Show the menu bar
            }

            // Exit
        } else {
            System.exit(0);
        }
    }

    // Constants for drawing panel
    private static final int CELL_SIZE = 50;
    private int panelWidth;
    private int panelHeight;

    // create a board with custom size
    public Connect4Graphical() {
        showMenu();                    //show the start menu
        playAgainstAI = false;
        int difficulty = aiChoice();    // Choose if you want AI or not and how hard it is


        // catch the mouse click event

        addMouseListener(new MouseAdapter() {


            @Override

            public void mouseClicked(MouseEvent e) {
                if (!gameEnded) {
                    int column = e.getX() / CELL_SIZE;

                    if (isValidColumn(column)) {
                        playMove(column);
                        repaint();

                        if (checkWin()) {
                            String playerWin;

                            if (currentPlayer == 'X') {
                                playerWin = "RED-Team";
                            } else {
                                playerWin = "BLACK-Team";
                            }

                            JOptionPane.showMessageDialog(null, "Congratulation! " + playerWin + " is the winner!");
                            gameEnded = true;

                        } else if (isBoardFull()) {
                            JOptionPane.showMessageDialog(null, "The round is draw!");
                            gameEnded = true;
                        } else {
                            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                        }

                        // Ai play move
                        if(playAgainstAI && !gameEnded){
                            aiPlayMove(difficulty);
                            repaint();

                            if (checkWin()) {
                                String playerWin = (currentPlayer == 'X') ? "RED-Team" : "BLACK-Team";
                                JOptionPane.showMessageDialog(null, "Congratulations! " + playerWin + " is the winner!");
                                gameEnded = true;
                            } else if (isBoardFull()) {
                                JOptionPane.showMessageDialog(null, "The round is a draw!");
                                gameEnded = true;
                            } else {
                                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                            }
                        }

                    } else if (column < 0 || column >= boardWidth) {
                        JOptionPane.showMessageDialog(null, "Invalid column, please try again!");
                    } else {
                        JOptionPane.showMessageDialog(null, "The column is full!");
                    }
                }
            }
        });
    }

    // Sets the Ai move
    // Bases the way the AI moves depending on the difficulty chosen
    private void aiPlayMove(int aiDiff) {
        int column;
        if (aiDiff == 1) {
            column = getAIMove("easy");
            playMove(column);
        } else if (aiDiff == 2){
            column = getAIMove("medium");
            playMove(column);
        } else {
            column = getAIMove("hard");
            playMove(column);
        }
    }
    private int getAIMove(String difficulty) {
        // Unfinished due to being unable to figure out how to make a heatmap
        int column;
        int heatMap = 0;
        if (difficulty == "easy") {
            Random r = new Random();
            column = r.nextInt(8) - 1; // Generates a random number between -1 and 7
            while (!isValidColumn(column)) {
                column = r.nextInt(8) - 1;

            }
            return column;
        } else if (difficulty == "medium") {
            // Using a "heat map" to base its moves, the AI will randomly choose between the "hottest" columns to place the token.
            Random s = new Random();
            column = s.nextInt(8) - 1; // Generates a random number between -1 and 7
            while (!isValidColumn(column)) {
                column = s.nextInt(8) - 1;
            }
        } else {
            // Will brute force & scan each column to find the most in a row and act upon it.
            Random s = new Random();
            column = s.nextInt(8) - 1; // Generates a random number between -1 and 7
            while (!isValidColumn(column)) {
                column = s.nextInt(8) - 1;
            }
        }
        return column;
    }
    private int aiChoice(){
        // Make a choice for AI Play
        // ... and choose how hard the AI will be
        String playAgainstAIChoice = JOptionPane.showInputDialog(null, "Do you want to play against an AI? (yes/no)");
        if (playAgainstAIChoice != null && playAgainstAIChoice.equalsIgnoreCase("yes")) {

            playAgainstAI = true;
            String aiDifficulty = JOptionPane.showInputDialog(null, "What level of AI would you like? (1-3, easy to hard)");
            // Will set difficulties to easy, medium, or hard respectively.
            while (0 != 1) {
                if (aiDifficulty.equalsIgnoreCase("1")) {
                    return 1;
                } else if (aiDifficulty.equalsIgnoreCase("2")) {
                    return 2;
                } else if (aiDifficulty.equalsIgnoreCase("3")) {
                    return 3;
                } else {
                    aiDifficulty = JOptionPane.showInputDialog(null, "Invalid choice! Try again. (1-3, easy to hard)");
                }
            }
        }
        return 0;
    }


    // initialize board
    private void initializeBoard(int width, int height) {
        boardWidth = width;
        boardHeight = height;
        board = new char[boardWidth][boardHeight];
        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardHeight; j++) {
                board[i][j] = '-';
            }
        }
    }

    // calculate the panel size based on the board size
    private void calculatePanelSize() {
        panelWidth = CELL_SIZE * boardHeight;
        panelHeight = CELL_SIZE * boardWidth;
    }



    // check if the column is valid and not full
    private boolean isValidColumn(int column)
    {
        return column >= 0 && column < boardHeight && board[0][column] == '-';
    }

    // play a move
    private void playMove(int column)
    {
        for (int i = boardWidth - 1; i >= 0; i--)
        {
            if (board[i][column] == '-')
            {
                board[i][column] = currentPlayer;
                break;
            }
        }
    }

    // check for a win
    private boolean checkWin() {

        // check win in vertical line
        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardHeight - 3; j++) {
                if (board[i][j] == currentPlayer &&
                        board[i][j + 1] == currentPlayer &&
                        board[i][j + 2] == currentPlayer &&
                        board[i][j + 3] == currentPlayer) {
                    return true;
                }
            }
        }

        // check win in horizontal line
        for (int i = 0; i < boardWidth - 3; i++) {
            for (int j = 0; j < boardHeight; j++) {
                if (board[i][j] == currentPlayer &&
                        board[i + 1][j] == currentPlayer &&
                        board[i + 2][j] == currentPlayer &&
                        board[i + 3][j] == currentPlayer) {
                    return true;
                }
            }
        }

        // check win in main diagonal line
        for (int i = 0; i < boardWidth - 3; i++) {
            for (int j = 0; j < boardHeight - 3; j++) {
                if (board[i][j] == currentPlayer &&
                        board[i + 1][j + 1] == currentPlayer &&
                        board[i + 2][j + 2] == currentPlayer &&
                        board[i + 3][j + 3] == currentPlayer) {
                    return true;
                }
            }
        }

        // check win in sub-diagonal line
        for (int i = 0; i < boardWidth - 3; i++) {
            for (int j = 3; j < boardHeight; j++) {
                if (board[i][j] == currentPlayer &&
                        board[i + 1][j - 1] == currentPlayer &&
                        board[i + 2][j - 2] == currentPlayer &&
                        board[i + 3][j - 3] == currentPlayer) {
                    return true;
                }
            }
        }

        return false;
    }


    // check if the board is full
    private boolean isBoardFull()
    {
        for (int j = 1; j < boardHeight; j++)
        {
            if (board[0][j] == '-')
            {
                return false;
            }
        }

        return true;
    }

    // override the paint component method to draw the game board
    @Override
    protected void paintComponent(Graphics g){

        super.paintComponent(g);

        for (int i = 0; i< boardWidth; i++)
        {
            for (int j = 0; j < boardHeight; j++)
            {
                int x = j * CELL_SIZE;
                int y = i * CELL_SIZE;
                drawCell(g, x, y, board[i][j]);
            }
        }


    }

    // this method uses to draw a single cell
    private void drawCell(Graphics g, int x, int y, char player)
    {
        if (player == 'X')
        {
            g.setColor(Color.RED);
        } else if (player == 'O') {
            g.setColor(Color.BLACK);
        }else
        {
            g.setColor(Color.WHITE);
        }

        g.fillOval(x, y, CELL_SIZE, CELL_SIZE);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> {

                    JFrame frame = new JFrame("Digital Connect 4 Game");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setResizable(false);

                    Connect4Graphical connect4 = new Connect4Graphical();

                    frame.add(connect4);
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);


                }
        );
    }
}