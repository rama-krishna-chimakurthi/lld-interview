package com.rk.lld.elevator;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class ElevatorController {
    private int elevatorCount;
    private int minFloor;
    private int maxFloor;

    private List<Elevator> elevators;

    ElevatorController(int elevatorCount, int maxFloor, int minFloor) {
        this.maxFloor = maxFloor;
        this.minFloor = minFloor;
        this.elevatorCount = elevatorCount;
        elevators = new ArrayList<>();
        for (int i = 0; i < elevatorCount; i++) {
            elevators.add(new Elevator(minFloor, maxFloor));
        }
    }

    /*
     * Core logic:
     * - find the best elevator to serve
     * - add the request to that elevator
     * Edge Cases:
     * - floor out of bound
     * - if Destion => hall call should be pick up
     */
    public boolean requestElevator(int floor, RequestType requestType) {
        if (requestType == RequestType.DESTINATION)
            return false;
        if (floor > getMaxFloor() || floor < getMinFloor())
            return false;

        Request request = new Request(floor, requestType);
        Elevator best = findBestElevator(request);
        best.addRequests(request);
        return true;
    }

    public void step() {
        for (Elevator e : getElevators()) {
            e.step();
        }
    }

    /*
     * Core Logic:
     * - findMovingTowards in same direction
     * - find nearest idle
     * - find nearest
     */
    private Elevator findBestElevator(Request request) {
        Elevator best = findMovingTowards(request);
        if (best != null) {
            return best;
        }
        best = findNearestIdle(request);
        if (best != null) {
            return best;
        }
        return findNearest(request);
    }

    private Elevator findNearest(Request request) {
        Elevator nearest = null;
        int minDistance = Integer.MAX_VALUE;
        for (Elevator e : elevators) {
            int distance = Math.abs(request.getFloor() - e.getCurrentFloor());
            if (distance > minDistance) {
                continue;
            }
            minDistance = distance;
            nearest = e;
        }
        return nearest;
    }

    private Elevator findNearestIdle(Request request) {
        Elevator nearest = null;
        int minDistance = Integer.MAX_VALUE;
        for (Elevator e : getElevators()) {
            if (e.getDirection() != Direction.IDLE)
                continue;

            int distance = Math.abs(request.getFloor() - e.getCurrentFloor());
            if (distance > minDistance) {
                continue;
            }
            minDistance = distance;
            nearest = e;
        }
        return nearest;
    }

    private Elevator findMovingTowards(Request request) {
        Direction requestDirection = request.getType() == RequestType.PICKUP_DOWN ? Direction.DOWN
                : Direction.UP;

        int minDistance = Integer.MAX_VALUE;
        Elevator best = null;

        for (Elevator e : getElevators()) {
            if (e.getDirection() != requestDirection)
                continue;

            if (requestDirection == Direction.UP && e.getCurrentFloor() > request.getFloor() ||
                    requestDirection == Direction.DOWN && e.getCurrentFloor() < request.getFloor()) {
                continue;
            }

            if (!e.hasRequestsAtOrBeyond(request.getFloor(), requestDirection)) {
                continue;
            }
            int distance = Math.abs(request.getFloor() - e.getCurrentFloor());
            if (distance < minDistance) {
                minDistance = distance;
                best = e;
            }
        }
        return best;
    }

}
