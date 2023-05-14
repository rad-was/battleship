package battleship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Player {
    private final int gameFieldSize;
    private final String[][] gameField;
    private final ArrayList<Ship> ships;
    private int shipsRemaining;

    public Player(int gameFieldSize) {
        ships = new ArrayList<>();
        this.gameFieldSize = gameFieldSize;
        gameField = new String[gameFieldSize + 2][gameFieldSize + 2]; // to accommodate the edges
        for (String[] field : gameField) {
            Arrays.fill(field, "~");
        }
        gameField[0][0] = " ";
        for (int i = 0; i < gameField.length - 1; ++i) {
            gameField[0][i + 1] = String.valueOf(i + 1);
            gameField[i + 1][0] = String.valueOf((char) ('A' + i));
        }
        // Fill the edge
        for (int i = 0; i < gameField.length; ++i) {
            gameField[gameField.length - 1][i] = "=";
            gameField[i][gameField.length - 1] = "=";
        }
    }

    public int getGameFieldSize() {
        return gameFieldSize;
    }

    public int getShipsRemaining() {
        return shipsRemaining;
    }

    @SuppressWarnings("unused")
    void print() {
        for (int row = 0; row <= gameFieldSize; ++row) {
            for (int col = 0; col <= gameFieldSize; ++col) {
                System.out.printf("%s ", gameField[row][col]);
            }
            System.out.println();
        }
    }

    void printWithoutShips() {
        for (int row = 0; row <= gameFieldSize; ++row) {
            for (int col = 0; col <= gameFieldSize; ++col) {
                if (gameField[row][col].equals("O")) {
                    System.out.print("~ ");
                } else {
                    System.out.printf("%s ", gameField[row][col]);
                }
            }
            System.out.println();
        }
    }

    void addShips() {
        print();
        System.out.println();
        ships.add(new Ship("Aircraft Carrier", 5));
        ships.add(new Ship("Battleship", 4));
        ships.add(new Ship("Submarine", 3));
        ships.add(new Ship("Cruiser", 3));
        ships.add(new Ship("Destroyer", 2));
        shipsRemaining = 5;
        for (Ship ship : ships) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the coordinates of the " + ship + " (" + ship.getSize() + " cells):\n");
            while (true) {
                String coordinates = scanner.nextLine();
                System.out.println();
                if (Validate.validate(gameField, coordinates, ship.getSize())) {
                    drawShipOnGameField(ship, coordinates);
                    print();
                    System.out.println();
                    break;
                } else {
                    System.out.println(" Try again:\n");
                }
            }
        }
    }

    private void drawShipOnGameField(Ship ship, String coordinates) {
        Scanner scn = new Scanner(coordinates);
        String shipHead = scn.next();
        String shipTail = scn.next();
        if (Validate.isOnSameRow(shipHead, shipTail)) {
            int row = shipHead.charAt(0) - 'A' + 1;
            int colMin = Math.min(
                    Integer.parseInt(shipHead.substring(1)),
                    Integer.parseInt(shipTail.substring(1)));
            int colMax = Math.max(
                    Integer.parseInt(shipHead.substring(1)),
                    Integer.parseInt(shipTail.substring(1)));
            for (int col = colMin; col <= colMax; ++col) {
                gameField[row][col] = "O";
                String field = coordinates.charAt(0) + String.valueOf(col);
                ship.addCoordinates(field);
            }
        } else if (Validate.isOnSameColumn(shipHead, shipTail)) {
            int col = Integer.parseInt(shipHead.substring(1));
            int rowMin = Math.min(shipHead.charAt(0) - 'A' + 1, shipTail.charAt(0) - 'A' + 1);
            int rowMax = Math.max(shipHead.charAt(0) - 'A' + 1, shipTail.charAt(0) - 'A' + 1);
            for (int row = rowMin; row <= rowMax; ++row) {
                gameField[row][col] = "O";
                String field = (char) (row + 'A' - 1) + String.valueOf(col);
                ship.addCoordinates(field);
            }
        }
    }

    void shoot(Player opponent, String coordinates) {
        System.out.println();
        int row = coordinates.charAt(0) - 'A' + 1;
        int col = Integer.parseInt(coordinates.substring(1));
        Ship hit = opponent.ships.stream()
                .filter(s -> s.getFieldsOccupied().contains(coordinates))
                .findFirst()
                .orElse(null);
        if (hit != null) {
            if (hit.getFieldsRemainingUntilSunken().contains(coordinates)) {
                hit.removeCoordinates(coordinates);
                if (hit.getFieldsRemainingUntilSunken().isEmpty()) {
                    System.out.println("You sank a ship!");
                    shipsRemaining--;
                    opponent.gameField[row][col] = "X";
                    return;
                }
            }
            System.out.println("You hit a ship!");
            opponent.gameField[row][col] = "X";
        } else {
            System.out.println("You missed!");
            opponent.gameField[row][col] = "M";
        }
    }
}
