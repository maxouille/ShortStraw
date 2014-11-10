package com.master.shortstraw.Model;

import android.graphics.PointF;

/**
 * Created by Maxence Bobin on 10/11/14.
 */
public class Line {

    private PointF p1;
    private PointF p2;

    public Line () {
        p1 = new PointF();
        p2 = new PointF();
    }

    public Line (PointF p1, PointF p2) {
        this.p1 = p1;
        this.p2 = p2;
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
}
