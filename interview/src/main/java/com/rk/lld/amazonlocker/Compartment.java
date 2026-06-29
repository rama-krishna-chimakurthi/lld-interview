package com.rk.lld.amazonlocker;

import lombok.Getter;

@Getter
public class Compartment {
    private CompartmentSize size;
    private boolean isOccupied;

    Compartment(CompartmentSize size) {
        this.size = size;
        isOccupied = false;
    }

    public void markOccupied() {
        isOccupied = true;
    }

    public void markFree() {
        isOccupied = false;
    }

    public void open() {
        System.out.println("Openinig Compartment");
    }
}
