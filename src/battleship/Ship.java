package battleship;

import java.util.HashSet;

public class Ship {
    private final String name;
    private final int size;
    private final HashSet<String> fieldsOccupied;
    private final HashSet<String> fieldsRemainingUntilSunken;

    public Ship(String name, int size) {
        this.name = name;
        this.size = size;
        fieldsOccupied = new HashSet<>(size);
        fieldsRemainingUntilSunken = new HashSet<>(size);
    }

    int getSize() {
        return this.size;
    }

    void addCoordinates(String coordinates) {
        fieldsOccupied.add(coordinates);
        fieldsRemainingUntilSunken.add(coordinates);
    }

    void removeCoordinates(String coordinates) {
        fieldsRemainingUntilSunken.remove(coordinates);
    }

    HashSet<String> getFieldsOccupied() {
        return fieldsOccupied;
    }

    HashSet<String> getFieldsRemainingUntilSunken() {
        return fieldsRemainingUntilSunken;
    }

    @Override
    public String toString() {
        return name;
    }
}
