package com.master.shortstraw.Model;

import android.graphics.PointF;

/**
 * Created by Maxence Bobin on 10/11/14.
 */
public class Triangle {

    private PointF p1;
    private PointF p2;
    private PointF p3;

    public Triangle () {
        p1 = new PointF();
        p2 = new PointF();
        p3 = new PointF();
    }

    public Triangle (PointF p1, PointF p2, PointF p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public PointF getP1() {
        return p1;
    }

    public void setP1(PointF p1) {
        this.p1 = p1;
    }

    public PointF getP2() {
        return p2;
    }

    public void setP2(PointF p2) {
        this.p2 = p2;
    }

    public PointF getP3() {
        return p3;
    }

    public void setP3(PointF p3) {
        this.p3 = p3;
    }
}
