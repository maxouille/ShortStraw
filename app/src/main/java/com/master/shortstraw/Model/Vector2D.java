package com.master.shortstraw.Model;

import android.graphics.PointF;

/**
 * Created by Maxence Bobin on 12/11/14.
 */
public class Vector2D {

    private float x;
    private float y;

    public Vector2D () {
        x = 0;
        y = 0;
    }

    public Vector2D (float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(PointF p1, PointF p2) {
        this.x = p2.x - p1.x;
        this.y = p2.y - p1.y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setX(float x1, float x2) {
        this.x = x2 - x1;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setY (float y1, float y2) {
        this.y = y2 - y1;
    }

    public float length () {
        return (float) Math.sqrt(x * x + y * y);
    }
}
