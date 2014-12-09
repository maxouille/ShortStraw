package com.master.shortstraw;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

import com.master.shortstraw.Model.MathTools;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Maxence Bobin on 10/11/14.
 */
public class ShortStraw {

    private int INTERSPACING_CONSTANT = 40;
    private int STRAW_WINDOW = 3;
    private double MEDIAN_TRESHOLD = 0.95;
    private double LINE_TGRESHOLD = 0.95;

    private DrawingPanel drawingPanel;
    private RectF boundingBox = new RectF();
    private ArrayList<PointF> resampledPoints = new ArrayList<PointF>();

    public ShortStraw (DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }

    /**
     * @param pList
     * @return the corners points according to the pList
     */
    public ArrayList<PointF> getCornerPoints(ArrayList<PointF> pList) {
        if (pList.isEmpty() || pList.size() < 2) {
            return new ArrayList<PointF>();
        }
        else {
            float interSpacingDistance = (float) determineResamplingSpacing(pList);
            resampledPoints = resampling(pList, interSpacingDistance);
            ArrayList<Integer> indicesPoints = getCorners(resampledPoints);
            ArrayList<PointF> cornerPoints = new ArrayList<PointF>();
            for (int i = 0; i < indicesPoints.size(); i++) {
                PointF p = resampledPoints.get(indicesPoints.get(i));
                cornerPoints.add(new PointF(p.x, p.y));
            }
            return cornerPoints;
        }
    }

    /**
     * @param pList
     * @return an ArraList of the corners indices
     */
    public ArrayList<Integer> getCornersIndices (ArrayList<PointF> pList) {
        if (pList.isEmpty() || pList.size() < 2) return new ArrayList<Integer>();
        float interSpacingDistance = (float) determineResamplingSpacing(pList);
        ArrayList<PointF> resampledPoints = resampling(pList, interSpacingDistance);
        return getCorners(resampledPoints);
    }

    /**
     * @param pList
     * @return the resampling spacing according to the points List.
     */
    private double determineResamplingSpacing (ArrayList<PointF> pList) {
        //Get the top left corner point and the bottom right corner point
        PointF[] points = getPointsBoundingBox(pList);
        PointF topLeftPoint = points[0];
        PointF bottomRightPoint = points[1];

        boundingBox = new RectF(topLeftPoint.x, topLeftPoint.y, bottomRightPoint.x, bottomRightPoint.y);

        //Get the boundingBox diagonal
        double boundingBoxDiagonal = MathTools.distance(topLeftPoint, bottomRightPoint);

        //Compute the insertSpacing distance
        return (boundingBoxDiagonal / INTERSPACING_CONSTANT);
    }

    /**
     * @param pointList : The list of points to be resampled
     * @return a new ArrayList with the resampled points
     */
    private ArrayList<PointF> resampling (ArrayList<PointF> pointList, float interSpacingDistance) {
        float distance = 0;
        //Create an empty ArrayList of points
        ArrayList<PointF> resamplePointList = new ArrayList<PointF>();
        //Add the first point of the stroke on the resampled points
        resamplePointList.add(pointList.get(0));
        for (int i = 1; i < pointList.size(); i++) {
            PointF point1 = pointList.get(i - 1);
            PointF point2 = pointList.get(i);
            float d = MathTools.distance(point1, point2);

            if ((distance+d) >= interSpacingDistance) {
                PointF newPoint = new PointF();
                newPoint.x = point1.x + ((interSpacingDistance - distance) / d) * (point2.x - point1.x);
                newPoint.y = point1.y + ((interSpacingDistance - distance) / d) * (point2.y - point1.y);
                resamplePointList.add(newPoint);
                pointList.add(i, newPoint);
                distance = 0;
            }
            else {
                distance += d;
            }
        }
        return resamplePointList;
    }

    /**
     * @param resamplePointList
     * @return the corners indexes within the stroke
     */
    private ArrayList<Integer> getCorners (ArrayList<PointF> resamplePointList) {

        //Create a new ArrayList for the corners indexes
        ArrayList<Integer> corners = new ArrayList<Integer>();
        corners.add(0);
        //Create a new ArrayList for the straws
        ArrayList<Float> straws = new ArrayList<Float>();

        if (resamplePointList.size() >= STRAW_WINDOW) {
            //Add the distance between the i - STRAW_WINDOW and i + STRAW_WINDOW points
            for (int i = STRAW_WINDOW; i < resamplePointList.size() - STRAW_WINDOW; i++) {
                PointF point1 = resamplePointList.get(i - STRAW_WINDOW);
                PointF point2 = resamplePointList.get(i + STRAW_WINDOW);
                straws.add(MathTools.distance(point1, point2));
            }

            //Calculate the median of the straws
            float t = (float) (median(straws) * MEDIAN_TRESHOLD);

            for (int i = STRAW_WINDOW; i < resamplePointList.size() - STRAW_WINDOW; i++) {
                float s = straws.get(i - STRAW_WINDOW);
                //If straw < median
                if (s < t) {
                    double localMin = Double.POSITIVE_INFINITY;
                    int localMinIndex = i;
                    //Retrieve the local min
                    while (i < straws.size() && s < t) {
                        if (s < localMin) {
                            localMin = s;
                            localMinIndex = i;
                        }
                        i++;
                       // s = straws.get(i - STRAW_WINDOW);
                    }
                    //Add the local min index to the array list
                    corners.add(localMinIndex);
                }
            }
            //Add the last index
            corners.add(resamplePointList.size() - 1);
            corners = postProcessCorners(resamplePointList, corners, straws);
        }
        return corners;
    }

    /**
     * @param values
     * @return the median of an array List of float
     */
    private float median (ArrayList<Float> values) {
        //Sort the array List into ascending order
        Collections.sort(values);
        int m;
        if (values.size() / 2 == 0) {
            m = values.size() / 2;
            return (values.get(m - 1) + values.get(m))/2;
        }
        else {
            m = (values.size() + 1) / 2;
            return values.get(m - 1);
        }
    }

    /**
     * @param pList
     * @param corners
     * @param straws
     * @return the arrayList of corners indexes  after checking if any corners
     * can be removed or added based on high level polyline rules
     */
    private ArrayList<Integer> postProcessCorners (ArrayList<PointF> pList, ArrayList<Integer> corners, ArrayList<Float> straws) {
        boolean go = false;
        int c1, c2, i;
        while (!go) {
            go = true;
            for (i = 1; i < corners.size(); i++) {
                c1 = corners.get(i - 1);
                c2 = corners.get(i);
                if (!isLine(pList, c1, c2)) {
                    int newCorner = halfWayCorner(straws, c1, c2);
                    // This checking was not in the paper,
                    // but prevents adding undefined points
                    if (newCorner > c1 && newCorner < c2) {
                        corners.add(i, newCorner);
                        go = false;
                    }
                }
            }
        }
        for (i = 1; i < corners.size() - 1; i++) {
            c1 = corners.get(i - 1);
            c2 = corners.get(i + 1);
            if (isLine(pList, c1, c2)) {
                corners.remove(i);
                i--;
            }
        }
        return corners;
    }

    /**
     * Determines if the stroke segment between the points
     * indices c1 and c2 form a line
     * @param pList
     * @param c1
     * @param c2
     * @return a boolean
     */
    private boolean isLine (ArrayList<PointF> pList, int c1, int c2) {
        float distance = MathTools.distance(pList.get(c1), pList.get(c2));
        float pathDistance = pathDistance(pList, c1, c2);
        return (distance / pathDistance) > LINE_TGRESHOLD;
    }

    /**
     * Computes the euclidian path distance between the points at indices c1 and c2
     * @param pList
     * @param c1
     * @param c2
     * @return
     */
    private float pathDistance (ArrayList<PointF> pList, int c1, int c2) {
        float d = 0;
        for (int i = c1; i < c2; i++) {
            d += MathTools.distance(pList.get(i), pList.get(i+1));
        }
        return d;
    }

    /**
     * Finds a corner roughly halfway between point indices c1 and c2
     * @param straws
     * @param c1
     * @param c2
     * @return
     */
    private int halfWayCorner (ArrayList<Float> straws, int c1, int c2) {
        int quarter = (c2 - c1) / 4;
        double minValue = Double.POSITIVE_INFINITY;
        int minIndex = 0;
        for (int i = c1 + quarter; i < (c2 - quarter); i++) {
                float s = straws.get(i - STRAW_WINDOW-1);
                if (s < minValue) {
                    minValue = s;
                    minIndex = i;
                }
            }
        return minIndex;
    }

    /**
     * @return the top left corner point and the bottom right corner point of a point list
     */
    private PointF[] getPointsBoundingBox (ArrayList<PointF> pointList) {
        float minX = pointList.get(0).x;
        float maxX = pointList.get(0).x;
        float minY = pointList.get(0).y;
        float maxY = pointList.get(0).y;
        for (PointF p : pointList) {
            if (p.x < minX) {
                minX = p.x;
            }
            if (p.x > maxX) {
                maxX = p.x;
            }
            if (p.y < minY) {
                minY = p.y;
            }
            if (p.y > maxY) {
                maxY = p.y;
            }
        }
        return new PointF[]{new PointF(minX, minY), new PointF(maxX, maxY)};
    }

    public RectF getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(RectF boundingBox) {
        this.boundingBox = boundingBox;
    }

    public ArrayList<PointF> getResampledPoints() {
        return resampledPoints;
    }

    public void setResampledPoints(ArrayList<PointF> resampledPoints) {
        this.resampledPoints = resampledPoints;
    }
}
