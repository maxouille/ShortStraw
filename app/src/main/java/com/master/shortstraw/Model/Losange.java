package com.master.shortstraw.Model;

import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Created by Maxence Bobin on 10/11/14.
 */
public class Losange {

    private PointF baryCenter;
    private float angle;
    private float diag1;
    private float diag2;

    public Losange(PointF baryCenter, float angle, float diag1, float diag2) {
        this.baryCenter = baryCenter;
        this.angle = angle;
        this.diag1 = diag1;
        this.diag2 = diag2;
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
