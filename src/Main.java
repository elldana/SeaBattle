import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    private static final int boardSize = 7;
    private static final char emptyField = '⬜';
    private static final char missed = '⬛';
    private static final char hit = '⛵';
    private static final char ship = '⭕';
    private static final char sunkShip = '❌';
    private static final char[][] field = new char[boardSize][boardSize];
    private static final Scanner sc = new Scanner(System.in);

    private static ArrayList<Player> players = new ArrayList<>();
    private static String nickname;

    public static void main(String[] args) {
        boolean continueGame = true;
        while (continueGame) {
            game();
            continueGame = playAgain();
        }
        displayAllStatistics();
        System.out.println("Thank you for playing! Goodbye.");
    }

    static void game() {
        inputNickname();

        cleanAll();
        initializedField();
        hideAllShips();
        outputHiddenField();

        int attempts = 30;
        boolean gameWon = startGame(attempts);

        if (gameWon) {
            System.out.println("CONGRATULATIONS! \nYou won the game!");
            updateStatistics(nickname, true);
        } else {
            System.out.println("You LOST!\nYou wasted all attempts.");
            updateStatistics(nickname, false);
        }
        displayStatisticsOfShots();
    }

    static void cleanAll() {
        System.out.println("\033[H\033[J");
    }

    static int initializedField() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                field[i][j] = emptyField;
            }
        }
        return 0;
    }

    static int hideAllShips() {
        int shipByThree = 3;
        placeShip(shipByThree);

        int shipByTwo = 2;
        placeShip(shipByTwo);
        placeShip(shipByTwo);

        int shipByOne = 1;
        placeShip(shipByOne);
        placeShip(shipByOne);
        placeShip(shipByOne);
        placeShip(shipByOne);

        return 0;
    }

    static int placeShip(int shipSize) {
        Random rand = new Random();
        boolean place = false;

        while (!place) {
            boolean horizontal = rand.nextBoolean();
            int row = rand.nextInt(boardSize);
            int col = rand.nextInt(boardSize);

            if (checkPlace(row, col, shipSize, horizontal)) {
                for (int i = 0; i < shipSize; i++) {
                    if (horizontal) {
                        field[row][col + i] = ship;
                    } else {
                        field[row + i][col] = ship;
                    }
                }
                place = true;
            }
        }
        return shipSize;
    }

    static boolean checkPlace(int row, int col, int shipSize, boolean horizontal) {
        if (horizontal) {
            if (col + shipSize > boardSize)
                return false;
            for (int i = 0; i < shipSize; i++) {
                if (!checkIsEmpty(row, col + i))
                    return false;
            }
        } else {
            if (row + shipSize > boardSize)
                return false;
            for (int i = 0; i < shipSize; i++) {
                if (!checkIsEmpty(row + i, col))
                    return false;
            }
        }
        return true;
    }

    static boolean checkIsEmpty(int row, int col) {

        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < boardSize && j >= 0 && j < boardSize) {
                    if (field[i][j] == ship) return false;
                }
            }
        }
        return true;
    }

    static void outputHiddenField() {
        cleanAll();
        System.out.print("  ");
        for (int i = 1; i <= boardSize; i++) {
            System.out.print(i + "  ");
        }
        System.out.println();

        for (int i = 0; i < boardSize; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < boardSize; j++) {
                if (field[i][j] == ship) {
                    System.out.print(emptyField + " ");
                } else {
                    System.out.print(field[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    static boolean startGame(int attempts) {
        int remainingAttempts = attempts;

        while (remainingAttempts > 0) {
            System.out.println("Attempts left: " + remainingAttempts);
            System.out.println("Enter coordinates of the ship:" +
                    "\n (first vertical, second horizontal) ");

            int row = sc.nextInt() - 1;
            int column = sc.nextInt() - 1;

            if (row < 0 || column < 0 || row >= boardSize || column >= boardSize) {
                System.out.println("Coordinates out of bounds. Try again.");
                continue;
            }

            if (field[row][column] == ship) {
                System.out.println("Hit!");
                field[row][column] = hit;
                updateShotStatistics();

                if (isShipSunk(row, column)) {
                    System.out.println("You've sunk a ship! ⛵");
                    markSunkShip(row, column);
                }
                outputHiddenField();

                if (checkAllShipsSunk()) {
                    return true;
                }
            } else if (field[row][column] == emptyField) {
                System.out.println("Missed!");
                field[row][column] = missed;
                outputHiddenField();
            } else {
                System.out.println("You already tried this coordinates. Try again.");
                outputHiddenField();
            }
            remainingAttempts--;
        }
        return false;
    }

    static void updateShotStatistics() {
        Player currentPlayer = getPlayerByName(nickname);
        if (currentPlayer != null) {
            currentPlayer.incrementHits();
        }
    }

    static boolean isShipSunk(int row, int col) {
        boolean horizontal = (col > 0 && field[row][col] == ship) || (col < boardSize - 1 && field[row][col + 1] == ship);
        if (horizontal) {


            int startCol = col;
            while (startCol >= 0 && (field[row][startCol] == hit || field[row][startCol] == ship)) {
                if (field[row][startCol] == ship) {
                    return false;
                }
                startCol--;
            }
            startCol = col + 1;
            while (startCol < boardSize && (field[row][startCol] == hit || field[row][startCol] == ship)) {
                if (field[row][startCol] == ship) {
                    return false;
                }
                startCol++;
            }
        }
        else {
            int startRow = row;
            while (startRow >= 0 && (field[startRow][col] == hit || field[startRow][col] == ship)) {
                if (field[startRow][col] == ship) {
                    return false;
                }
                startRow--;
            }
            startRow = row + 1;
            while (startRow < boardSize && (field[startRow][col] == hit || field[startRow][col] == ship)) {
                if (field[startRow][col] == ship) {
                    return false;
                }
                startRow++;
            }
        }
        return true;
    }

    static void markSunkShip(int row, int col) {
        boolean horizontal = (col > 0 && field[row][col - 1] == hit) ||
                (col < boardSize - 1 && field[row][col + 1] == hit);

        if (horizontal) {

            int startCol = col;

            while (startCol >= 0 && (field[row][startCol] == hit || field[row][startCol] == ship)) {
                field[row][startCol] = sunkShip;
                startCol--;
            }

            startCol = col + 1;
            while (startCol < boardSize && (field[row][startCol] == hit || field[row][startCol] == ship)) {
                field[row][startCol] = sunkShip;
                startCol++;
            }
        } else {

            int startRow = row;

            while (startRow >= 0 && (field[startRow][col] == hit || field[startRow][col] == ship)) {
                field[startRow][col] = sunkShip;
                startRow--;
            }

            startRow = row + 1;
            while (startRow < boardSize && (field[startRow][col] == hit || field[startRow][col] == ship)) {
                field[startRow][col] = sunkShip;
                startRow++;
            }
        }
    }

    static boolean checkAllShipsSunk() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (field[i][j] == ship) {
                    return false;
                }
            }
        }
        return true;
    }

    static boolean playAgain() {
        sc.nextLine();
        System.out.println("Would you like to play again? (yes/no)");

        String answer = sc.nextLine().trim().toLowerCase();
        return answer.equals("yes");
    }

    public static class Player {
            private String name;
            private int wins;
            private int losses;
            private int hits;

            public Player(String name) {
                this.name = name;
                this.wins = 0;
                this.losses = 0;
                this.hits = 0;
            }
            public String getName() {
                return name;
            }

            public int getWins() {
                return wins;
            }

            public int getLosses() {
                return losses;
            }

            public int getHits() {
                return hits;
            }

            public void incrementWins() {
                wins++;
            }

            public void incrementLosses() {
                losses++;
            }

            public void incrementHits() {
                hits++;
            }
        }
    static void inputNickname() {
        System.out.println("Hi!" +
                "\n" + "It is a Sea Battle game." +
                "\n" + "Enter your nickname: ");

        nickname = sc.nextLine();
        System.out.println("Welcome, " + nickname + "! Let's start the game!");

        if (getPlayerByName(nickname) == null) {
            players.add(new Player(nickname));
        }
    }

    static void updateStatistics(String player, boolean won) {
        Player currentPlayer = getPlayerByName(player);
        if (currentPlayer != null) {
            if (won) {
                currentPlayer.incrementWins();
            } else {
                currentPlayer.incrementLosses();
            }
        }
    }
    static void displayStatisticsOfShots() {
        Player currentPlayer = getPlayerByName(nickname);
        if (currentPlayer != null) {
            System.out.println(currentPlayer.getName() + " - Hits: " + currentPlayer.getHits());
        }
        System.out.println();
    }
    static void displayAllStatistics() {
        System.out.println("\nAll Players Statistics:");
        if (players.isEmpty()) {
            System.out.println("No players have played yet.");
            return;
        }
        for (Player player : players) {
            System.out.println(player.getName() + " - Wins: " + player.getWins() + ", Losses: " + player.getLosses());
        }
        System.out.println();
    }

    static Player getPlayerByName(String name) {
        for (Player player : players) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

}