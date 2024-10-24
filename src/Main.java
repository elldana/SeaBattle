import java.util.Random;
import java.util.Scanner;

public class Main {
    private static int size = 7;
    private static char emptyField = '.';
    private static char ship = '❌';
    private static char[][] field = new char[size][size];

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Hi!" +
                "\n" + "It is a Modifies Sea Battle game." +
                "\n" + "Enter your nickname: ");

        String nickname = sc.nextLine();
        System.out.println("Welcome, " + nickname + "!");

        System.out.println("Enter coordinates of the ship: ");

    }

    static void cleanAll() {
        System.out.println("\033[H\033[J");
    }

    static int printField(int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                field[i][j] = emptyField;
            }
        }
    return 0;}

    static int placeShip(int size) {
        Random rand = new Random();
        boolean place = false;

        while (!place) {
            boolean horizontal = rand.nextBoolean();
            int row = rand.nextInt(size);
            int col = rand.nextInt(size);

                for (int i = 0; i < row; i++) {
                    if (horizontal) {
                        field[row][col + i] = ship;
                    } else {
                        field[row + i][col] = ship;
                    }
                }
                place = true;
            }
        return size;}
    }