package com.master.shortstraw.Model;

import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Created by Maxence Bobin on 10/11/14.
 */
public class Losange {

    private PointF p1;
    private PointF p2;
    private PointF p3;
    private PointF p4;
    private PointF baryCenter;
    private float angle;
    private float diag1;
    private float diag2;

    public Losange() {
        p1 = new PointF();
        p2 = new PointF();
        p3 = new PointF();
        p4 = new PointF();
        baryCenter = new PointF();
        angle = 0;
        diag1 = 0;
        diag2 = 0;
    }

    public Losange(PointF baryCenter, float angle, float diag1, float diag2) {
        this.baryCenter = baryCenter;
        this.angle = angle;
        this.diag1 = diag1;
        this.diag2 = diag2;
        p1 = new PointF();
        p2 = new PointF();
        p3 = new PointF();
        p4 = new PointF();
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

    public PointF getP4() {
        return p4;
    }

    public void setP4(PointF p4) {
        this.p4 = p4;
    }

    public PointF getBaryCenter() {
        return baryCenter;
    }

    public void setBaryCenter(PointF baryCenter) {
        this.baryCenter = baryCenter;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getDiag1() {
        return diag1;
    }

    public void setDiag1(float diag1) {
        this.diag1 = diag1;
    }

    public float getDiag2() {
        return diag2;
    }

    public void setDiag2(float diag2) {
        this.diag2 = diag2;
    }
}
