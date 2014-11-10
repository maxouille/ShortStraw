package com.master.shortstraw;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.Log;

import com.master.shortstraw.Model.Line;
import com.master.shortstraw.Model.PolyLine;
import com.master.shortstraw.Model.Triangle;

import java.util.ArrayList;

/**
 * Created by Maxence Bobin on 10/11/14.
 */
public class ShapeDetector {

    private ArrayList<PointF> corners;
    private DrawingPanel drawingPanel;
    private Paint drawPaintShape;

    private int CLOSE_RANGE_LINE = 50;
    private int CLOSE_RANGE_TRIANGLE = 50;
    private int CLOSE_RANGE_SQUARE = 50;

    public ShapeDetector (ArrayList<PointF> corners, DrawingPanel drawingPanel) {
        this.corners = corners;
        this.drawingPanel = drawingPanel;
        drawPaintShape = drawingPanel.getDrawPaintShape();
    }

    public void detection () {
        int nbCorners = corners.size();
        PointF p1;
        PointF p2;
        PointF p3;
        PointF p4;
        switch (nbCorners) {
            case 0:
                break;
            case 1:
                break;
            //It is a point or a line
            case 2:
                p1 = corners.get(0);
                p2 = corners.get(1);
                if(distance(p1, p2) < CLOSE_RANGE_LINE) {
                    PointF p = getMiddlePoint(p1, p2);
                    drawingPanel.addPoint(p);
                }
                //Draw a line
                else {
                    drawingPanel.addLine(new Line(p1, p2));
                }
                break;
            //It is a curve or a polyLine
            case 3:
               drawingPanel.addPolyLine(new PolyLine(corners));
               break;
            //It is a triangle or just a line
            case 4:
                p1 = corners.get(0);
                p2 = corners.get(1);
                p3 = corners.get(2);
                p4 = corners.get(3);

                if (distance(p1, p4) < CLOSE_RANGE_TRIANGLE) {
                    PointF middle = getMiddlePoint(p1, p4);
                    drawingPanel.addTriangle(new Triangle(middle, p2, p3));
                }
                else {
                    drawingPanel.addPolyLine(new PolyLine(corners));
                }
                break;
            //Square or polyLine
            case 5:
                p1 = corners.get(0);
                p2 = corners.get(1);
                p3 = corners.get(2);
                p4 = corners.get(3);
                PointF p5 = corners.get(4);
                //It is a quadrangle
                if (distance(p1, p5) < CLOSE_RANGE_SQUARE) {
                    PointF middle = getMiddlePoint(p1, p5);
                    //TODO : check rect/square/trapeze
                    ArrayList<PointF> pl = new ArrayList<PointF>();
                    pl.add(middle);
                    pl.add(p2);
                    pl.add(p3);
                    pl.add(p4);
                    pl.add(middle);
                    drawingPanel.addPolyLine(new PolyLine(pl));
                }
                //It is a polyLine
                else {
                    drawingPanel.addPolyLine(new PolyLine(corners));
                }

                break;
            default:
        }
    }

    /**
     *
     * @param p1 : the first 2D point
     * @param p2 : the second 2D point
     * @return the euclidian distance between the 2D points p1 and p2.
     */
    private double distance(PointF p1, PointF p2) {
        double d = (p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y);
        return Math.sqrt(d);
    }

    private PointF getMiddlePoint (PointF p1, PointF p2) {
        return new PointF((p1.x + p2.x)/2, (p1.y + p2.y)/2);
    }
    public ArrayList<PointF> getCorners() {
        return corners;
    }

    public void setCorners(ArrayList<PointF> corners) {
        this.corners = corners;
    }
}
