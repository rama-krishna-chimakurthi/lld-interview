package com.rk.lld.elevator;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Request {
    private int floor;
    private RequestType type;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Request))
            return false;
        Request request = (Request) o;
        return floor == request.floor && type == request.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(floor, type);
    }
}
