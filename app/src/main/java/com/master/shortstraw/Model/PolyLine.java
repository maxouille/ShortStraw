package com.master.shortstraw.Model;

import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Created by Maxence Bobin on 10/11/14.
 */
public class PolyLine {

    private ArrayList<PointF> pointList;

    public PolyLine () {
        pointList = new ArrayList<PointF>();
    }

    public PolyLine (ArrayList<PointF> pointList) {
        this.pointList = pointList;
    }

    public ArrayList<PointF> getPointList() {
        return pointList;
    }

    public void setPointList(ArrayList<PointF> pointList) {
        this.pointList = pointList;
    }
}
