import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final int boardSize = 7;
    private static final char emptyField = '⬜';
    private static final char missed = '⬛';
    private static final char hit = '⛵';
    private static final char ship = '⭕';
    private static final char sunkShip = '❌';
    private static final char[][] field = new char[boardSize][boardSize];
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        inputNickname();

        boolean continueGame = true;
        while (continueGame) {
            game();
            continueGame = continueGamePrompt(); // Запрашиваем продолжение
        }
        System.out.println("Thank you for playing! Goodbye.");
    }
    static void game() {
        boolean playAgain = true;

        while (playAgain) {
            cleanAll();
            createField();
            hideAllShips();
            outputHiddenField();

            int attempts = 30;
            boolean gameWon = startGame(attempts);

            if (gameWon) {
                System.out.println("CONGRATULATIONS! \nYou won the game!");
                System.out.println(" ");
            }
            else {
                System.out.println("You LOST!\nYou wasted all attempts.");
            }

            playAgain = continueGamePrompt();  // Спрашиваем, хочет ли игрок сыграть снова
        }

        System.out.println("Thank you for playing! Goodbye.");
    }
    static void inputNickname() {
        System.out.println("Hi!" +
                "\n" + "It is a Sea Battle game." +
                "\n" + "Enter your nickname: ");

        String nickname = sc.nextLine();
        System.out.println("Welcome, " + nickname + "! Game is started!");
    }

    static void cleanAll() {
        System.out.println("\033[H\033[J");
    }

    static int createField() {
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
                    }
                    else {
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
                field[row][column] = hit;  // Помечаем как попадание

                if (isShipSunk(row, column)) {
                    System.out.println("You've sunk a ship! ⛵");
                    markSunkShip(row, column);
                }
                outputHiddenField();

                if (checkAllShipsSunk()) {
                    return true;
                }
            }
            else if (field[row][column] == emptyField) {
                System.out.println("Missed!");
                field[row][column] = missed; // Помечаем как пропуск
                outputHiddenField();
            }
            else {
                System.out.println("You already tried this coordinates. Try again.");
                outputHiddenField();
            }
            remainingAttempts--;
        }
        return false;
    }

    public static boolean isShipSunk(int row, int col) {
        boolean horizontal = (col > 0 && field[row][col] == ship) || (col < boardSize - 1 && field[row][col + 1] == ship);
        if (horizontal) {

            // Проверка всей горизонтальной линии корабля
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
            // Проверка всей вертикальной линии корабля
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

    public static void markSunkShip(int row, int col) {
        boolean horizontal = (col > 0 && field[row][col - 1] == hit) ||
                (col < boardSize - 1 && field[row][col + 1] == hit);

        if (horizontal) {
            // Проверка и пометка всей горизонтальной линии потопленного корабля
            int startCol = col;
            // Идем влево
            while (startCol >= 0 && (field[row][startCol] == hit || field[row][startCol] == ship)) {
                field[row][startCol] = sunkShip;
                startCol--;
            }
            // Идем вправо
            startCol = col + 1;
            while (startCol < boardSize && (field[row][startCol] == hit || field[row][startCol] == ship)) {
                field[row][startCol] = sunkShip;
                startCol++;
            }
        } else {
            // Проверка и пометка всей вертикальной линии потопленного корабля
            int startRow = row;
            // Идем вверх
            while (startRow >= 0 && (field[startRow][col] == hit || field[startRow][col] == ship)) {
                field[startRow][col] = sunkShip;
                startRow--;
            }
            // Идем вниз
            startRow = row + 1;
            while (startRow < boardSize && (field[startRow][col] == hit || field[startRow][col] == ship)) {
                field[startRow][col] = sunkShip;
                startRow++;
            }
        }
    }

    public static boolean checkAllShipsSunk() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (field[i][j] == ship) {
                    return false;
                }
            }
        }
        return true;
    }

    static boolean continueGamePrompt() {
        sc.nextLine(); // Очищаем буфер после nextInt
        System.out.println("Would you like to play again? (yes/no)");

        String answer = sc.nextLine().trim().toLowerCase();
        return answer.equals("yes");
    }

}