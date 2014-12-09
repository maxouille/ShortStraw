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
    private float edgeLength;
    private PointF baryCenter;
    private float angle;
    private float littleDiag;
    private float bigDiag;

    public Losange() {
        p1 = new PointF();
        p2 = new PointF();
        p3 = new PointF();
        p4 = new PointF();
        baryCenter = new PointF();
        edgeLength = 0;
        angle = 0;
        littleDiag = 0;
        bigDiag = 0;
    }

    public Losange(PointF baryCenter, float edgeLength, float angle, float diag1, float diag2) {
        this.baryCenter = baryCenter;
        this.edgeLength = edgeLength;
        this.angle = angle;
        if (diag1 < diag2) {
            littleDiag = diag1;
            bigDiag = diag2;
        }
        else {
            littleDiag = diag2;
            bigDiag = diag1;
        }
        p1 = new PointF(baryCenter.x, baryCenter.y-littleDiag/2);
        p2 = new PointF(baryCenter.x-bigDiag/2, baryCenter.y);
        p3 = new PointF(baryCenter.x, baryCenter.y+littleDiag/2);
        p4 = new PointF(baryCenter.x+littleDiag/2, baryCenter.y);
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

    public float getEdgeLength() {
        return edgeLength;
    }

    public void setEdgeLength(float edgeLength) {
        this.edgeLength = edgeLength;
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

    public float getLittleDiag() {
        return littleDiag;
    }

    public void setLittleDiag(float littleDiag) {
        this.littleDiag = littleDiag;
    }

    public float getBigDiag() {
        return bigDiag;
    }

    public void setBigDiag(float bigDiag) {
        this.bigDiag = bigDiag;
    }
}
