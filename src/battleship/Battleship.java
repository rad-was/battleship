package battleship;


import java.util.*;

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
    private int row1, row2, column1, column2;
    private boolean assigned = false;
    private int shipsSunken = 0;

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
        System.out.println("The game starts!\n");
        printWithoutShips();
        System.out.println("Take a shot!\n");
        while (shipsSunken != ships.size()) {
            shoot();
        }
        System.out.println("You sank the last ship. You won. Congratulations!");
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
            addShipToGameField();
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
        assignCoordinatesToRowsAndColumns(coordinates);
        if (row1 == row2) {
            return checkCoordinatesOnRowOrColumn(shipName, column1, column2, "rows");
        } else if (column1 == column2) {
            return checkCoordinatesOnRowOrColumn(shipName, row1, row2, "columns");
        } else {
            System.out.println("Error 4! Ships can only be placed in a straight line! Try again:");
            return false;
        }
    }

    //
    //  WORKS BUT FIX DUPLICATE ERROR 6 LATER
    //
    private boolean checkCoordinatesOnRowOrColumn(String shipName, int rowOrColumn1, int rowOrColumn2, String rowsOrColumns) {
        int min = Math.min(rowOrColumn1, rowOrColumn2);
        int max = Math.max(rowOrColumn1, rowOrColumn2);
        if (!(max - min + 1 == ships.get(shipName))) {  // Check if ship takes up correct number of fields
            System.out.println("Error 5! Coordinates don't match the number of fields for the current ship! Try again:");
            return false;
        }
        if (rowsOrColumns.equals("rows")) {
            for (int i = min; i <= max; ++i) {  // Check if ship doesn't touch any other ships
                if (this.gameField[row1][i].contains("O") ||
                        this.gameField[row1 - 1][i].contains("O") ||
                        this.gameField[row1 + 1][i].contains("O") ||
                        this.gameField[row1][i - 1].contains("O") ||
                        this.gameField[row1][i + 1].contains("O")) {
                    System.out.println("Error 6! Ship cannot touch any other ship! Try again:");
                    return false;
                }
            }
        } else if (rowsOrColumns.equals("columns")) {
            for (int i = min; i <= max; ++i) {  // Check if ship doesn't touch any other ships
                if (this.gameField[i][column1].contains("O") ||
                        this.gameField[i - 1][column1].contains("O") ||
                        this.gameField[i + 1][column1].contains("O") ||
                        this.gameField[i][column1 - 1].contains("O") ||
                        this.gameField[i][column1 + 1].contains("O")) {
                    System.out.println("Error 6! Ship cannot touch any other ship! Try again:");
                    return false;
                }
            }
        }
        return true;
    }

    private void assignCoordinatesToRowsAndColumns(String coordinates) {
        Scanner scn = new Scanner(coordinates);
        String next = scn.next();
        row1 = next.charAt(0) - 'A' + 1;
        column1 = Integer.parseInt(next.substring(1));
        next = scn.next();
        row2 = next.charAt(0) - 'A' + 1;
        column2 = Integer.parseInt(next.substring(1));
        assigned = true;
    }

    private void addShipToGameField() {
        if (!assigned) {
            System.exit(1);
        }
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

    private void shoot() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String shotCoordinate = scanner.nextLine();
            // Check if valid coordinate
            if (shotCoordinate.matches("^[A-Z]([1-9]|1\\d|2[0-6])$") &&
                    (shotCoordinate.charAt(0) - 'A' < this.gameFieldSize) &&
                    (Integer.parseInt(shotCoordinate.substring(1)) <= this.gameFieldSize)) {
                // Check if hit or miss
                int row = shotCoordinate.charAt(0) - 'A' + 1;
                int column = Integer.parseInt(shotCoordinate.substring(1));
                if (this.gameField[row][column].equals("O") || this.gameField[row][column].equals("X")) {
                    this.gameField[row][column] = "X";
                    System.out.println();
                    printWithoutShips();
                    if (shipSunken(row, column)) {
                        shipsSunken++;
                        if (shipsSunken != ships.size()) {
                            System.out.println("You sank a ship! Specify a new target:\n");
                        }
                    } else {
                        System.out.println("You hit a ship! Try again:\n");
                    }
                } else if (this.gameField[row][column].equals("S")) {
                    System.out.println();
                    printWithoutShips();
                    System.out.println("You hit a ship! Try again:\n");
                } else {
                    this.gameField[row][column] = "M";
                    System.out.println();
                    printWithoutShips();
                    System.out.println("You missed! Try again:\n");
                }
                break;
            } else {
                System.out.println("\nWrong coordinate. Try again:\n");
            }
        }
    }

    private enum Direction {
        UP(0, 1),
        DOWN(0, -1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        private final int dx, dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    private boolean shipSunken(int row, int column) {
        ArrayList<Direction> directionsOfAdjacentXFields = new ArrayList<>();
        for (int dx = -1; dx <= 1; ++dx) {
            for (int dy = -1; dy <= 1; ++dy) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                String newField = gameField[row + dy][column + dx];
                if (newField.equals("O")) {
                    return false;
                }
                if (newField.equals("X")) {
                    directionsOfAdjacentXFields.add(dy == 0 ?
                            (dx > 0 ? Direction.RIGHT : Direction.LEFT) :
                            (dy > 0 ? Direction.UP : Direction.DOWN));
                }
            }
        }
        for (Direction d : directionsOfAdjacentXFields) {
            int newRow = row;
            int newColumn = column;
            while (true) {
                if (gameField[newRow + d.dy][newColumn + d.dx].equals("O")) {
                    return false;
                } else if (gameField[newRow + d.dy][newColumn + d.dx].equals("X")) {
                    if (d.dx == 0) {
                        newRow = (d.dy == 1 ? newRow + 1 : newRow - 1);
                    } else {
                        newColumn = (d.dx == 1 ? newColumn + 1 : newColumn - 1);
                    }
                } else {
                    break;
                }
            }
            // Mark sunken ship on the game field
            newRow = row;
            newColumn = column;
            while (gameField[newRow][newColumn].equals("X")) {
                gameField[newRow][newColumn] = "S";
                newRow += d.dy;
                newColumn += d.dx;
            }
        }
        return true;
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

    private void printWithoutShips() {
        for (int i = 0; i < this.gameFieldSize + 1; ++i) {
            for (int j = 0; j < this.gameFieldSize + 1; ++j) {
                if (this.gameField[i][j].equals("O")) {
                    System.out.print("~ ");
                } else if (this.gameField[i][j].equals("S")) {
                    System.out.print("X ");
                } else {
                    System.out.printf("%s ", this.gameField[i][j]);
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
