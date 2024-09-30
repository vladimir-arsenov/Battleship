package org.example;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Cell {
    @Setter
    private Ship ship;
    private boolean isHit;
    private boolean isNearShip;

    public Cell(Ship ship) {
        this.ship = ship;
        isHit = false;
        isNearShip = true;
    }

    public boolean hasShip() {
        return ship != null;
    }

    public void setNearShip() {
        isNearShip = false;
    }

    public void hit() {
        isHit = true;
    }
}

