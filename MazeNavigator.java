import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * HW - 10 Challenge -- Maze Navigator
 *
 * Navigates through a maze with the help of concurrency.
 *
 * @author Advit Bhullar, L-24
 *
 * @version October 30, 2021
 *
 */

public class MazeNavigator extends Thread implements Runnable  {
    private static int currentRow = 4;
    private static int currentColumn = 4;
    private static int moveNumber;
    private static boolean started = false;
    private static char[][] maze;
    private int playerNumber;
    private String fileName;
    public static Object gateKeeper = new Object();

    public MazeNavigator(int playerNumber, String fileName) {
        this.playerNumber = playerNumber;
        this.fileName = fileName;
    }

    public void run() {
        if (!started) {
            synchronized (gateKeeper) {
                started = true;
                System.out.println("Welcome! Initial Maze:");
                maze = new char[10][10];
            }
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    synchronized (gateKeeper) {
                        maze[i][j] = ' ';
                    }
                }
            }
            synchronized (gateKeeper) {
                maze[4][4] = 'X';
                for (int i = 0; i < 10; i++) {
                    System.out.println(Arrays.toString(maze[i]));
                }
            }
        }
        String moveString = "";

        try (BufferedReader bfr = new BufferedReader(new FileReader(fileName))) {

            String line = bfr.readLine();

            while (line != null) {
                int move = Integer.parseInt(line);
                if (move >= 1 && move <= 4) {
                    synchronized (gateKeeper) {
                        maze[currentRow][currentColumn] = ' ';
                        if (move == 1) {
                            currentColumn--;
                            if (currentColumn == -1)
                                currentColumn = 9;
                            else if (currentColumn == 10)
                                currentColumn = 0;

                            maze[currentRow][currentColumn] = 'X';
                            moveString = "Left";
                        } else if (move == 2) {
                            currentColumn++;
                            if (currentColumn == -1)
                                currentColumn = 9;
                            else if (currentColumn == 10)
                                currentColumn = 0;

                            maze[currentRow][currentColumn] = 'X';

                            moveString = "Right";
                        } else if (move == 3) {

                            currentRow--;
                            if (currentRow == -1)
                                currentRow = 9;
                            else if (currentRow == 10)
                                currentRow = 0;
                            maze[currentRow][currentColumn] = 'X';

                            moveString = "Up";
                        } else if (move == 4) {
                            currentRow++;
                            if (currentRow == -1)
                                currentRow = 9;
                            else if (currentRow == 10)
                                currentRow = 0;
                            maze[currentRow][currentColumn] = 'X';
                            moveString = "Down";
                        }


                        moveNumber++;
                        System.out.println("Move number: " + moveNumber);
                        System.out.println("Player: " + playerNumber);
                        System.out.println("Move: " + moveString);
                        for (int i = 0; i < 10; i++) {
                            System.out.println(Arrays.toString(maze[i]));
                        }
                    }
                } else {
                    synchronized (gateKeeper) {
                        System.out.println("Error, invalid input!");
                    }
                }
                line = bfr.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            MazeNavigator[] mazeNavigators = {new MazeNavigator(1, "PlayerOneMoves.txt"),
                    new MazeNavigator(2, "PlayerTwoMoves.txt")};

            for (int i = 0; i < mazeNavigators.length; i++) {
                mazeNavigators[i].start();
            }
            for (int i = 0; i < mazeNavigators.length; i++) {
                mazeNavigators[i].join();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }
}
