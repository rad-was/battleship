package battleship;

import java.util.ArrayList;
import java.util.Scanner;

public class Battleship {
    private final ArrayList<Player> players = new ArrayList<>();

    public Battleship(int gameFieldSize) {
        Player player1 = new Player(gameFieldSize);
        Player player2 = new Player(gameFieldSize);
        players.add(player1);
        players.add(player2);
    }

    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        for (Player player : players) {
            System.out.println("Player " + (players.indexOf(player) + 1) + ", place your ships on the game field\n");
            player.addShips();
            System.out.println("Press Enter and pass the move to another player");
            scanner.nextLine();
            System.out.print("\033\143");
        }

        //noinspection InfiniteLoopStatement
        while (true) {
            for (Player player : players) {
                Player opponent = players.indexOf(player) == 0 ? players.get(1) : players.get(0);
                opponent.printWithoutShips();
                System.out.println("---------------------");
                player.print();
                System.out.println();
                System.out.println("Player " + (players.indexOf(player) + 1) + ", it's your turn:\n");
                String coordinates = scanner.nextLine();
                while (!Validate.validateShotCoordinates(coordinates, player.getGameFieldSize())) {
                    System.out.println("Error! Wrong coordinates! Try again:");
                    coordinates = scanner.nextLine();
                }
                player.shoot(opponent, coordinates);

                if (player.getShipsRemaining() == 0) {
                    System.out.println("You sank the last ship. You won. Congratulations!");
                    break;
                }
                System.out.println("Press Enter and pass the move to another player");
                scanner.nextLine();
                System.out.print("\033\143");
            }
        }
    }
}
