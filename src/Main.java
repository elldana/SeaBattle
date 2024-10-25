import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final int size = 7;
    private static final char emptyField = '⬜';
    private static final char missed = '⬛';
    private static final char hit = '⛵';
    private static final char ship = '⭕';
    private static final char sunkShip = '❌';
    private static final char[][] field = new char[size][size];

    public static void main(String[] args) {

        inputNickname();

        cleanAll();

        field();
        hideAllShips();
        outputHiddenField();

        int count = 0;
        int attempts = 30;
        while (count < attempts) {
            startGame();
            count++;
        }
    }

    static void inputNickname() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Hi!" +
                "\n" + "It is a Sea Battle game." +
                "\n" + "Enter your nickname: ");

        String nickname = sc.nextLine();
        System.out.println("Welcome, " + nickname +"! Game is started!");
    }

    static void cleanAll() {
        System.out.println("\033[H\033[J");
    }

    static int field () {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                field[i][j] = emptyField;
            }
        }
    return 0;}

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

    return 0; }

    static int placeShip (int shipSize) {
        Random rand = new Random();
        boolean place = false;

        while (!place) {
            boolean horizontal = rand.nextBoolean();

            int row = rand.nextInt(size);
            int col = rand.nextInt(size);

            if (checkPlace(row, col, shipSize, horizontal)) {
                for (int i = 0; i < shipSize; i++) {
                    if (horizontal) {
                        field[row][col + i] = ship;
                    }
                    else {
                        field[row + i][col] = ship;
                    }
                }
            }
                place = true;
            }
        return shipSize; }

    static boolean checkPlace (int row, int col, int shipSize, boolean horizontal) {
        if (horizontal) {
            if (col + shipSize > size) return false;
            for (int i = 0; i < shipSize; i++) {
                if (!checkIsEmpty(row, col + i))
                    return false;
            }
        } else {
            if (row + shipSize > size) return false;
            for (int i = 0; i < shipSize; i++) {
                if (!checkIsEmpty(row + i, col))
                    return false;
            }
        }
        return true;
    }

    static boolean checkIsEmpty (int row, int col) {

        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < size && j >= 0 && j < size) {
                    if (field[i][j] == ship) return false;
                }
            }
        }
        return true;
    }

    static void outputHiddenField() {
        System.out.print("  ");
        for (int i = 1; i <= size; i++) {
            System.out.print(i + "  ");
        }
        System.out.println();

        for (int i = 0; i < size; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < size; j++) {
                if (field[i][j] == ship) {
                    System.out.print(emptyField+ " ");
                }
                else {
                    System.out.print(field[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    static void startGame() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter coordinates of the ship:" +
                "\n (first vertical, second horizontal) ");
        int row = sc.nextInt() - 1;
        int column = sc.nextInt() - 1;

        if (row < 0 || column < 0 || row > size || column > size) {
            System.out.println("Coordinates out of bounds. Try again.");
            return;
        }
        if (field[row][column] == ship) {
                System.out.println("Hit!");
                field[row][column] = hit;
                if (checkIsSunk(row, column)) {
                    markSunkShip(row, column);
                    System.out.println("Congratulations! Ship is sunk.");
                }
                outputHiddenField();
            }
            else if (field[row][column] == emptyField) {
            System.out.println("Missed!");
            field[row][column] = missed;
            outputHiddenField();
        }
        else {
            System.out.println("You already tried this coordinates. Try again.");
            outputHiddenField();
        }
    }
    static boolean checkIsSunk(int row, int col) {
        boolean horizontal = false;

        int startCol = col;
        int startRow = row;

        while (startCol > 0 && field[row][startCol - 1] == hit)
            startCol--;
        for (int i = startCol; i < size && field[row][i] != emptyField; i++) {
            if (field[row][i] == ship)
                return false;
            if (field[row][i] == emptyField)
                break;
                horizontal = true;
        }
        if (!horizontal) {
            while (startRow > 0 && field[startRow - 1][col] == hit)
                startRow--;
            for (int i = startRow; i < size && field[i][col] != emptyField; i++) {
                if (field[i][col] == ship)
                    return false;
            }
        }
        return true;
    }

    static void markSunkShip(int row, int col) {
        boolean horizontal = false;
        int startCol = col;
        int startRow = row;

        while (startCol > 0 && field[row][startCol - 1] == hit)
            startCol--;
        for (int i = startCol; i < size && field[row][i] == hit; i++) {
            field[row][i] = sunkShip;
            horizontal = true;
        }

        if (!horizontal) {
            while (startRow > 0 && field[startRow - 1][col] == hit)
                startRow--;
            for (int i = startRow; i < size && field[i][col] == hit; i++) {
                field[i][col] = sunkShip;
            }
        }
    }
}