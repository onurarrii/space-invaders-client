package com.group14.termproject.client.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vector2D {
    private double x;
    private double y;

    public Vector2D(){}

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vector2D)) return false;
        Vector2D v = (Vector2D) o;
        return v.x == x && v.y == y;
    }
}
