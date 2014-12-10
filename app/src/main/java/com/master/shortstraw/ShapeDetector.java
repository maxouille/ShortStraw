package com.master.shortstraw;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.Log;

import com.master.shortstraw.Model.Line;
import com.master.shortstraw.Model.Losange;
import com.master.shortstraw.Model.MathTools;
import com.master.shortstraw.Model.PolyLine;
import com.master.shortstraw.Model.Rectangle;
import com.master.shortstraw.Model.Square;
import com.master.shortstraw.Model.Triangle;
import com.master.shortstraw.Model.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Maxence Bobin on 10/11/14.
 */
public class ShapeDetector {

    private ArrayList<PointF> corners;
    private DrawingPanel drawingPanel;

    private int CLOSE_RANGE_LINE = 50;
    private int CLOSE_RANGE_TRIANGLE = 50;
    private int CLOSE_RANGE_SQUARE = 50;
    private int LENGTH_ERROR = 200;

    public ShapeDetector (ArrayList<PointF> corners, DrawingPanel drawingPanel) {
        this.corners = corners;
        this.drawingPanel = drawingPanel;
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
                if(MathTools.distance(p1, p2) < CLOSE_RANGE_LINE) {
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

                if (MathTools.distance(p1, p4) < CLOSE_RANGE_TRIANGLE) {
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
                //PointF middle;
                Vector2D AB = new Vector2D();
                Vector2D DC = new Vector2D();
                Vector2D AD = new Vector2D();
                Vector2D BC = new Vector2D();

                ArrayList<PointF> sortedPoints = new ArrayList<PointF>();
                sortedPoints.add(p1);
                sortedPoints.add(p2);
                sortedPoints.add(p3);
                sortedPoints.add(p4);
                //It is a quadrangle
                if (MathTools.distance(p1, p5) < CLOSE_RANGE_SQUARE) {

                    //Sort points to be A top left, B top right, C bottom right and D bottom left
                    /*ArrayList<PointF> sortedPoints = sortPoints (p1, p2, p3, p4);

                    Log.d("test", "sortedpoints : "+sortedPoints);
                    Log.d("test", "p2 "+p2.toString());
                    Log.d("test", "p3 " + p3.toString());
                    Log.d("test", "p4 " + p4.toString());
                    Log.d("test", "sortedPoints lenght : " + sortedPoints.size());

                    Log.d("test", "sort 0 " + sortedPoints.get(0).toString());
                    Log.d("test", "sort 1 "+sortedPoints.get(1).toString());
                    Log.d("test", "sort 2 "+sortedPoints.get(2).toString());
                    Log.d("test", "sort 3 " + sortedPoints.get(3).toString());*/
                    /*PointF A = sortedPoints.get(0);
                    PointF B = sortedPoints.get(1);
                    PointF C = sortedPoints.get(2);
                    PointF D = sortedPoints.get(3);*/

                    PointF A = p1;
                    PointF B = p2;
                    PointF C = p3;
                    PointF D = p4;

                    //drawingPanel.addPoint(A);

                    //Set coordinates for the 4 vectors (AB/DC and AD/BC)
                    AB.setX(A.x, B.x);
                    AB.setY(A.y, B.y);

                    DC.setX(D.x, C.x);
                    DC.setY(D.y, C.y);

                    AD.setX(A.x, D.x);
                    AD.setY(A.y, D.y);

                    BC.setX(B.x, C.x);
                    BC.setY(B.y, C.y);

                    Vector2D AC = new Vector2D(p1, p3);
                    Vector2D BD = new Vector2D(p2, p4);
                    PointF baryCenter = MathTools.getBarycenter(sortedPoints);

                    //If all edge have quite the same length
                    //Square or Losange
                    if (Math.abs(AB.length()-BC.length()) < LENGTH_ERROR &&
                            Math.abs(BC.length()-DC.length()) < LENGTH_ERROR &&
                            Math.abs(DC.length()-AD.length()) < LENGTH_ERROR) {

                        //If the diagonals length are quite the same
                        //Square
                        if (Math.abs(AC.length()-BD.length()) < LENGTH_ERROR) {
                            Log.d("test", "in square");
                            ArrayList<Float> l2 = new ArrayList<Float>();
                            l2.add(MathTools.distance(p1, p2));
                            l2.add(MathTools.distance(p2, p3));
                            l2.add(MathTools.distance(p3, p4));
                            float edgeLength = MathTools.mean(l2);
                            float angle = MathTools.getAngle(AB);
                            drawingPanel.addSquare(new Square(baryCenter, edgeLength, angle));
                        }
                        //Else Losange
                        else {
                          Log.d("test", "in losange");
                            //Get the rotation angle of the diagonal
                            float angle = MathTools.getAngle(BD);
                            drawingPanel.addLosange(new Losange(baryCenter, angle, AC.length(), BC.length()));
                        }
                    }
                    else {
                        //Rectangle or parallélogramme or trapèze
                        //If c1 and c2 perpendicular -> Rectangle
                        Log.d("test", "dot : "+Math.abs(MathTools.dot(AB, BC)));
                        if (Math.abs(MathTools.dot(AB, BC)) < 1000*LENGTH_ERROR) {
                            Log.d("test", "in rect");
                            ArrayList<Float> l1 = new ArrayList<Float>();
                            ArrayList<Float> l2 = new ArrayList<Float>();
                            l1.add(MathTools.distance(p1, p2));
                            l1.add(MathTools.distance(p3, p4));
                            l2.add(MathTools.distance(p2, p3));
                            l2.add(MathTools.distance(p4, p1));
                            float c1 = MathTools.mean(l1);
                            float c2 = MathTools.mean(l2);
                            float angle = MathTools.getAngle(AB);
                            drawingPanel.addRectangle(new Rectangle(baryCenter, c1, c2, angle));
                        }
                        //Other
                        else {
                            Log.d("test", "in other");
                        }
                    }

                }
                //It is a polyLine
                else {
                    drawingPanel.addPolyLine(new PolyLine(corners));
                }

                break;
            default:
                break;
        }
    }

   /* private ArrayList<PointF> sortPoints (PointF p1, PointF p2, PointF p3, PointF p4) {
        PointF[] res = new PointF[4];
        ArrayList<PointF> l = new ArrayList<PointF>();
        l.add(p1);
        l.add(p2);
        l.add(p3);
        l.add(p4);
        Log.d("test", "sortPoits function : "+l);
        float a1 = (p3.y - p1.y)/(p3.x - p1.x);
        float b1 = p3.y - a1*p3.x;
        float a2 = (p4.y - p2.y)/(p4.x - p2.x);
        float b2 = p4.y - a2*p4.x;

        PointF bar = new PointF(((b2-b1)/(a1-a2)), ((a1*b2 - a2*b1)/(a1 - a2)));
        Log.d("test", "bar : "+bar);
        float middleX = bar.x;
        float middleY = bar.y;
        PointF p;
        for (int i = 0; i < 4; i++) {
            p = l.get(i);
            if (p.x <= middleX && p.y <= middleY) {
                Log.d("test", "point 0 : "+p);
                res[0] = p;
            }
            if (p.x > middleX && p.y < middleY) {
                Log.d("test", "point 1 : "+p);
                res[1] = p;
            }
            if (p.x > middleX && p.y >= middleY) {
                Log.d("test", "point 2 : "+p);
                res[2] = p;
            }
            if (p.x <= middleX && p.y > middleY) {
                Log.d("test", "point 3 : "+p);
                res[3] = p;
            }
        }
        return new ArrayList<PointF>(Arrays.asList(res));
    }*/

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
