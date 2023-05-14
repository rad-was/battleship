package battleship;

import java.util.Scanner;
import java.util.stream.IntStream;

public class Validate {
    static boolean validate(String[][] gameField, String coordinates, int shipSize) {
        if (!isCorrectFormat(coordinates)) {
            System.out.print("Error! Wrong ship location!");
            return false;
        }

        Scanner scn = new Scanner(coordinates);
        String shipHead = scn.next();
        String shipTail = scn.next();

        if (!isWithinBounds(gameField, shipHead) || !isWithinBounds(gameField, shipTail)) {
            System.out.print("Error! Ship doesn't fit on the game field!");
            return false;
        }

        if (!isPlacedOnSingleRowOrColumn(shipHead, shipTail)) {
            System.out.print("Error! Ships can only be placed in a straight line!");
            return false;
        }

        if (!isTheRightLength(shipHead, shipTail, shipSize)) {
            System.out.print("Error! Wrong length of the ship!");
            return false;
        }

        if (!isNotTouchingOtherShips(gameField, shipHead, shipTail)) {
            System.out.print("Error! Ship cannot touch any other ships!");
            return false;
        }

        return true;
    }

    static boolean validateShotCoordinates(String coordinates, int gameFieldSize) {
        return (coordinates.matches("^[A-Z]([1-9]|1\\d|2[0-6])$") &&
                (coordinates.charAt(0) - 'A' < gameFieldSize) &&
                (Integer.parseInt(coordinates.substring(1)) <= gameFieldSize));
    }

    private static boolean isCorrectFormat(String coordinates) {
        return coordinates.matches("^[A-Z]([1-9]|1\\d|2[0-6])\\s[A-Z]([1-9]|1\\d|2[0-6])$");
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean isWithinBounds(String[][] gameField, String coordinates) {
        int row = coordinates.charAt(0) - 'A' + 1;
        int col = Integer.parseInt(coordinates.substring(1));
        return row >= 0 && row < gameField.length && col >= 1 && col < gameField.length;
    }

    private static boolean isPlacedOnSingleRowOrColumn(String shipHead, String shipTail) {
        return shipHead.charAt(0) == shipTail.charAt(0) ||
                shipHead.substring(1).equals(shipTail.substring(1));
    }

    static boolean isOnSameRow(String shipHead, String shipTail) {
        return shipHead.charAt(0) == shipTail.charAt(0);
    }

    static boolean isOnSameColumn(String shipHead, String shipTail) {
        return shipHead.substring(1).equals(shipTail.substring(1));
    }

    private static boolean isTheRightLength(String shipHead, String shipTail, int shipSize) {
        if (isOnSameRow(shipHead, shipTail)) {
            return Math.abs(
                    Integer.parseInt(shipHead.substring(1)) -
                    Integer.parseInt(shipTail.substring(1))) + 1 == shipSize;
        } else if (isOnSameColumn(shipHead, shipTail)) {
            return Math.abs((shipHead.charAt(0) - 'A' + 1) - (shipTail.charAt(0) - 'A' + 1)) + 1 == shipSize;
        }
        return false;
    }

    private static boolean isNotTouchingOtherShips(String[][] gameField, String shipHead, String shipTail) {
        if (isOnSameRow(shipHead, shipTail)) {
            int min = Math.min(
                    Integer.parseInt(shipHead.substring(1)),
                    Integer.parseInt(shipTail.substring(1)));
            int max = Math.max(
                    Integer.parseInt(shipHead.substring(1)),
                    Integer.parseInt(shipTail.substring(1)));
            int row = shipHead.charAt(0) -'A' + 1;

            //noinspection DuplicatedCode
            return IntStream.rangeClosed(min, max).noneMatch(i -> gameField[row][i].contains("O") ||
                    gameField[row - 1][i].contains("O") ||
                    gameField[row + 1][i].contains("O") ||
                    gameField[row][i - 1].contains("O") ||
                    gameField[row][i + 1].contains("O"));
        } else if (isOnSameColumn(shipHead, shipTail)) {
            int min = Math.min(shipHead.charAt(0) - 'A' + 1, shipTail.charAt(0) - 'A' + 1);
            int max = Math.max(shipHead.charAt(0) - 'A' + 1, shipTail.charAt(0) - 'A' + 1);
            int column = Integer.parseInt(shipHead.substring(1));

            //noinspection DuplicatedCode
            return IntStream.rangeClosed(min, max).noneMatch(i -> gameField[i][column].contains("O") ||
                    gameField[i - 1][column].contains("O") ||
                    gameField[i + 1][column].contains("O") ||
                    gameField[i][column - 1].contains("O") ||
                    gameField[i][column + 1].contains("O"));
        }
        return false;
    }
}
