import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final int size = 7;
    private static final char emptyField = '⬜';
    private static final char ship = '❌';
    private static final char[][] field = new char[size][size];

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        inputNickname();
        cleanAll();

        field(size);
        hideAllShips();
        printField();
    }

    static void inputNickname() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Hi!" +
                "\n" + "It is a Modifies Sea Battle game." +
                "\n" + "Enter your nickname: ");

        String nickname = sc.nextLine();
        System.out.println("Welcome, " + nickname +"! Game is started!");
    }
    static void cleanAll() {
        System.out.println("\033[H\033[J");
    }

    static int field(int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                field[i][j] = emptyField;
            }
        }
    return 0;}

    static int hideAllShips() {

        placeShip(3);

        placeShip(2);
        placeShip(2);

        placeShip(1);
        placeShip(1);
        placeShip(1);
        placeShip(1);

    return 0; }

    static int placeShip (int size) {
        Random rand = new Random();
        boolean place = false;

        while (!place) {
            boolean horizontal = rand.nextBoolean();

            int row = rand.nextInt(size);
            int col = rand.nextInt(size);

            if (checkPlace(row, col, horizontal)) {
                for (int i = 0; i < row; i++) {
                    if (horizontal) {
                        field[row][col + i] = ship;
                    } else {
                        field[row + i][col] = ship;
                    }
                }
            }
                place = true;
            }
        return size; }

    static boolean checkPlace (int row, int col, boolean place) {
        if (place) {
            if (col + size > size)
                return false;
            for (int i = 0; i < row; i++) {
                if (!checkIsEmpty(row, col + i))
                    return false;
            }
        }
            else
                if (row + size > size)
                    return false;
                    for (int i = 0; i < row; i++) {
                    if (!checkIsEmpty(row + i, col))
                        return false;
                }
        return true; }

    static boolean checkIsEmpty (int row, int col) {
        for (int i = row - 1; i < row + 1; i++) {
            for (int j = col - 1; j < col + 1; j++) {
                if (i >= 0 && i < size && j >= 0 && j < size) {
                    if (field[i][j] == ship)
                        return false;
                }
            }
        }
    return true; }
    static void printField() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.println();
        }
    }

    }