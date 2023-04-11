package battleship;


import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Battleship {
    private final int gameFieldSize;  // max size would be 26 because that many letters in the alphabet
    private final String[][] gameField;
    private static final LinkedHashMap<String, Integer> ships = new LinkedHashMap<>();
    static {
        ships.put("Aircraft Carrier", 5);
        ships.put("Battleship", 4);
        ships.put("Submarine", 3);
        ships.put("Cruiser", 3);
        ships.put("Destroyer", 2);
    }

    public Battleship(int size) {
        this.gameFieldSize = size;
        this.gameField = new String[size + 2][size + 2];
        for (int i = 0; i < size + 1; ++i) {
            for (int j = 0; j < size + 1; ++j) {
                if (i == 0) {
                    if (j == 0) this.gameField[i][j] = " ";
                    else this.gameField[i][j] = String.valueOf(j);
                } else {
                    if (j == 0) this.gameField[i][j] = String.valueOf((char) ('A' + i - 1));
                    else this.gameField[i][j] = "~";
                }
            }
        }
        Arrays.fill(this.gameField[size + 1], "=");
        for (int row = 0; row < size + 2; ++row) {
            this.gameField[row][size + 1] = "=";
        }
    }

    public void startGame() {
        addShips();
    }

    // Add ships
    private void addShips() {
        Scanner scanner = new Scanner(System.in);
        for (String key : ships.keySet()) {
            System.out.println("Enter the coordinates of the " + key + " (" + ships.get(key) + " cells):");
            String coordinates;
            do {
                System.out.print("\n> ");
                coordinates = scanner.nextLine();
                System.out.println();
            } while (!checkCoordinates(coordinates, key));
            drawShip(coordinates);
            print();
        }
    }

    private boolean checkCoordinates(String coordinates, String shipName) {
        // Check if coordinates are in correct format
        if (!(coordinates.matches("^[A-Z]([1-9]|1\\d|2[0-6])\\s[A-Z]([1-9]|1\\d|2[0-6])$"))) {
            System.out.println("Error 1! Wrong ship location! Try again:");
            return false;
        }
        // Check if coordinates fit in the game field
        Scanner scn = new Scanner(coordinates);
        while (scn.hasNext()) {
            String next = scn.next();
            if (next.charAt(0) - 'A' >= this.gameFieldSize) {
                System.out.println("Error 2! Wrong ship location! Try again:");
                return false;
            }
            if (Integer.parseInt(next.substring(1)) > this.gameFieldSize) {
                System.out.println("Error 3! Wrong ship location! Try again:");
                return false;
            }
        }
        // Check if ship doesn't touch any other ships
        scn = new Scanner(coordinates);
        int row1, row2, column1, column2;

        String next = scn.next();
        row1 = next.charAt(0) - 'A' + 1;
        column1 = Integer.parseInt(next.substring(1));

        next = scn.next();
        row2 = next.charAt(0) - 'A' + 1;
        column2 = Integer.parseInt(next.substring(1));

        if (row1 == row2) {
            int min = Math.min(column1, column2);
            int max = Math.max(column1, column2);
            if (!(max - min + 1 == ships.get(shipName))) {  // Check if ship takes up correct number of fields
                System.out.println("Error 5! Coordinates don't match the number of fields for the current ship! Try again:");
                return false;
            }
            for (int column = min; column <= max; ++column) {  // Check if ship doesn't touch any other ships
                if (this.gameField[row1][column].contains("O") ||
                        this.gameField[row1 - 1][column].contains("O") ||
                        this.gameField[row1 + 1][column].contains("O") ||
                        this.gameField[row1][column - 1].contains("O") ||
                        this.gameField[row1][column + 1].contains("O")) {
                    System.out.println("Error 6! Ship cannot touch any other ship! Try again:");
                    return false;
                }
            }
        } else if (column1 == column2) {
            int min = Math.min(row1, row2);
            int max = Math.max(row1, row2);
            if (!(max - min + 1 == ships.get(shipName))) {  // Check if ship takes up correct number of fields
                System.out.println("Error 5! Coordinates don't match the number of fields for the current ship! Try again:");
                return false;
            }
            for (int row = min; row <= max; ++row) {  // Check if ship doesn't touch any other ships
                if (this.gameField[row][column1].contains("O") ||
                        this.gameField[row - 1][column1].contains("O") ||
                        this.gameField[row + 1][column1].contains("O") ||
                        this.gameField[row][column1 - 1].contains("O") ||
                        this.gameField[row][column1 + 1].contains("O")) {
                    System.out.println("Error 6! Ship cannot touch any other ship! Try again:");
                    return false;
                }
            }
        } else {
            System.out.println("Error 4! Ships can only be placed in a straight line! Try again:");
            return false;
        }

        return true;
    }

    private void drawShip(String coordinates) {
        Scanner scn = new Scanner(coordinates);
        int row1, row2, column1, column2;

        String next = scn.next();
        row1 = next.charAt(0) - 'A' + 1;
        column1 = Integer.parseInt(next.substring(1));

        next = scn.next();
        row2 = next.charAt(0) - 'A' + 1;
        column2 = Integer.parseInt(next.substring(1));

        if (row1 == row2) {
            int min = Math.min(column1, column2);
            int max = Math.max(column1, column2);
            for (int column = min; column <= max; ++column) {
                gameField[row1][column] = "O";
            }
        } else if (column1 == column2) {
            int min = Math.min(row1, row2);
            int max = Math.max(row1, row2);
            for (int row = min; row <= max; ++row) {
                gameField[row][column1] = "O";
            }
        }
    }

    private void print() {
        for (int i = 0; i < this.gameFieldSize + 1; ++i) {
            for (int j = 0; j < this.gameFieldSize + 1; ++j) {
                System.out.printf("%s ", this.gameField[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}