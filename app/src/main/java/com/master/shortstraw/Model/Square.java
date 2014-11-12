package com.master.shortstraw.Model;

import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Created by Maxence Bobin on 10/11/14.
 */
public class Square {

    private PointF p1;
    private PointF p2;
    private PointF p3;
    private PointF p4;
    private float edgeLength;
    private PointF baryCenter;
    private float angle;

    public Square() {
        p1 = new PointF();
        p2 = new PointF();
        p3 = new PointF();
        p4 = new PointF();
        baryCenter = new PointF();
        edgeLength = 0;
        angle = 0;
    }

    public Square(PointF baryCenter, float edgeLength, float angle) {
        this.baryCenter = baryCenter;
        this.edgeLength = edgeLength;
        this.angle = angle;
        /*float length = edgeLength/2;
        this.p1 = new PointF();
        p1.x = baryCenter.x;
        p1.y = baryCenter.y;
        p1.x -= length;
        p1.y += length;
        this.p2 = new PointF();
        p2.x = baryCenter.x;
        p2.y = baryCenter.y;
        p2.x += length;
        p2.y += length;
        this.p3 = new PointF();
        p3.x = baryCenter.x;
        p3.y = baryCenter.y;
        p3.x += length;
        p3.y -= length;
        this.p4 = new PointF();
        p4.x = baryCenter.x;
        p4.x -= length;
        p4.y -= length;*/
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
}
