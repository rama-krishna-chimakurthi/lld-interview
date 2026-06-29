package com.rk.lld.elevator;

import java.util.HashSet;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import lombok.Getter;

public class Elevator {
    private BlockingQueue<Request> requestQueue = new LinkedBlockingQueue<>();

    private Set<Request> requests = new HashSet<>();
    @Getter
    private int currentFloor;
    @Getter
    private Direction direction;

    private int maxFloor;
    private int minFloor;

    Elevator(int minFloor, int maxFloor) {
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
    }

    public void addRequests(Request request) {
        requestQueue.offer(request);
    }

    public boolean hasRequestsAtOrBeyond(int floor, Direction dir) {
        for (Request request : requests) {
            if (dir == Direction.UP && request.getFloor() >= floor) {
                if (request.getType() == RequestType.PICKUP_UP
                        || request.getType() == RequestType.DESTINATION) {
                    return true;
                }
            }
            if (dir == Direction.DOWN && request.getFloor() <= floor) {
                if (request.getType() == RequestType.PICKUP_DOWN
                        || request.getType() == RequestType.DESTINATION) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * Core logic:
     * - Add the new requests in queue to set
     * - If no requests for the elevator, make it idle
     * - if the elevator is idle, move to nearest floor in requests
     * - If current floor is in requests as destination(or pick floor with same
     * direction), remove from the queue and open door
     * - if there are no requests ahead, reverse Direction
     * - move to next floor
     */
    public void step() {
        while (!requestQueue.isEmpty()) {
            requests.add(requestQueue.poll());
        }

        if (requests.size() == 0) {
            direction = Direction.IDLE;
            return;
        }

        if (direction == Direction.IDLE) {
            direction = getDirectionOfNearestRequest();
        }

        RequestType type = direction == Direction.UP ? RequestType.PICKUP_UP : RequestType.PICKUP_DOWN;
        Request pickupRequest = new Request(currentFloor, type);
        Request destinationRequest = new Request(currentFloor, RequestType.DESTINATION);
        if (requests.contains(destinationRequest) || requests.contains(pickupRequest)) {
            requests.remove(destinationRequest);
            requests.remove(pickupRequest);
            openDoors();
            return;
        }

        if (!requestsAhead()) {
            direction = direction == Direction.UP ? Direction.DOWN : Direction.UP;
        }

        if (direction == Direction.UP) {
            moveUp();
        }
        if (direction == Direction.DOWN) {
            moveDown();
        }
    }

    private void moveDown() {
        if (currentFloor == minFloor)
            return;
        currentFloor--;
    }

    private void moveUp() {
        if (currentFloor == maxFloor)
            return;
        currentFloor++;
    }

    private boolean requestsAhead() {
        for (Request req : requests) {
            if (direction == Direction.UP) {
                if (req.getFloor() > getCurrentFloor())
                    return true;
            }
            if (direction == Direction.DOWN) {
                if (req.getFloor() < getCurrentFloor())
                    return true;
            }
        }
        return false;
    }

    private void openDoors() {
        System.out.println("Open doors");
    }

    private Direction getDirectionOfNearestRequest() {
        int minDistance = Integer.MAX_VALUE;
        Request nearest = null;
        for (Request req : requests) {
            int distance = Math.abs(req.getFloor() - getCurrentFloor());
            if (distance < minDistance) {
                minDistance = distance;
                nearest = req;
            }
        }
        return nearest.getFloor() > getCurrentFloor() ? Direction.UP : Direction.DOWN;
    }
}
