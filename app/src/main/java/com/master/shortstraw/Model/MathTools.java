package com.master.shortstraw.Model;

import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Created by Maxence Bobin on 12/11/14.
 */
public class MathTools {

    /**
     * @param l : the list of float.
     * @return the mean of an ArrayList of Float
     */
    public static float mean (ArrayList<Float> l) {
        float res = 0;
        for (Float f : l) {
            res += f;
        }
        return res/l.size();
    }

    /**
     * @param l : the list of PointF
     * @return the barycenter of a list of PointF
     */
    public static PointF getBarycenter (ArrayList<PointF> l) {
        PointF res = new PointF();
        for (PointF p : l) {
            res.x += p.x;
            res.y += p.y;
        }
        res.x /= l.size();
        res.y /= l.size();
        return res;
    }

    /**
     * @param v : The vector 2D
     * @return the angle between v and the horizontal
     */
    public static float getAngle (Vector2D v) {
        Vector2D horizontal = new Vector2D(1,0);
        double res = Math.acos( dot(v, horizontal) / (norm(v) * norm(horizontal)));
        double res2 = res*180/Math.PI;
        return (float) res2;
    }

    /**
     * @param v1 : the first vector2D
     * @param v2 : the second vector2D
     * @return the dot product between two vectors
     */
    public static float dot (Vector2D v1, Vector2D v2) {
        return v1.getX()*v2.getX() + v1.getY()*v2.getY();
    }

    public static float norm (Vector2D v) {
        return (float) Math.sqrt(v.getX()*v.getX() + v.getY()*v.getY());
    }

    /**
     * @param p1 : the first 2D point
     * @param p2 : the second 2D point
     * @return the euclidian distance between the 2D points p1 and p2.
     */
    public static float distance(PointF p1, PointF p2) {
        double d = (p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y);
        return (float) Math.sqrt(d);
    }

}
