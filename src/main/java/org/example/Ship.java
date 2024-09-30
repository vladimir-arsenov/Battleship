package org.example;

import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

public class Ship {
    int size;
    int damaged;
    @Setter
    Set<int[]> coords = new HashSet<>();

    public Ship(int size) {
        this.size = size;
        damaged = 0;
    }

    /**
     * @return true if ship has sunk
     */
    public boolean takeDamage() {
        damaged++;
        return damaged == size;
    }
}
