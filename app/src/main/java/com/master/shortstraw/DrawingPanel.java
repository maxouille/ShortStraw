package com.master.shortstraw;

import android.content.Context;
import android.gesture.GesturePoint;
import android.gesture.GestureUtils;
import android.gesture.OrientedBoundingBox;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxence Bobin on 05/11/14.
 */
public class DrawingPanel extends View {


    private ArrayList<GesturePoint> pointList = new ArrayList<GesturePoint>();
    private ArrayList<PointF> resamplePointList = new ArrayList<PointF>();

    float distance = 0;

    private int interSpacingConstant = 40;
    private double boundingBoxDiagonal = 0;
    private double interSpacingDistance = 0;

    private long startDrawingTime = 0;

    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = 0xFF660000;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;

    public DrawingPanel(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing() {
        drawPath = new Path();
        drawPaint = new Paint();
        //Set the color of the stroke
        drawPaint.setColor(paintColor);
        //Set the initial path properties
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);

    }

    /** Called when the custom view is assigned a size
     * After the oncreate but before the drawing
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //Instantiate the drawing canvas and bitmap using the width and height values:
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    /** Called when the view is initially drawn and when invalidate() is called */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startDrawingTime = System.currentTimeMillis();
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                long time = System.currentTimeMillis() - startDrawingTime;
                pointList.add(new GesturePoint(touchX, touchY, time));
                drawCanvas.drawPoint(touchX, touchY, drawPaint);
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                //Reset drawingTime
                startDrawingTime = 0;

                //drawCanvas.drawPath(drawPath, drawPaint);

                //Get the top left corner point and the bottom right corner point
                PointF[] points = getPointsBoundingBox();
                PointF topLeftPoint = points[0];
                PointF bottomRightPoint = points[1];

                RectF boundingBox = new RectF(topLeftPoint.x, topLeftPoint.y, bottomRightPoint.x, bottomRightPoint.y);

                //Get the boundingBox diagonal
                boundingBoxDiagonal = getBoundingBoxDiagonal(boundingBox);

                //Compute the insertSpacing distance
                interSpacingDistance = boundingBoxDiagonal / interSpacingConstant;

                Paint p = new Paint();
                p.setColor(0xFF66DD00);
                p.setAntiAlias(true);
                p.setStrokeWidth(20);
                p.setStyle(Paint.Style.STROKE);
                p.setStrokeJoin(Paint.Join.ROUND);
                p.setStrokeCap(Paint.Cap.ROUND);
                drawCanvas.drawRect(boundingBox, p);

                drawPath.reset();
                pointList.clear();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    private double getBoundingBoxDiagonal (RectF box) {
        //Apply Pythagore
        double height = box.height();
        double width = box.width();
        double diag = Math.sqrt(height*height + width*width);
        return diag;
    }

    /**
     *
     * @return the top left corner point and the bottom right corner point of a point list
     */
    private PointF[] getPointsBoundingBox () {
        float minX = pointList.get(0).x;
        float maxX = pointList.get(0).x;
        float minY = pointList.get(0).y;
        float maxY = pointList.get(0).y;
        for (GesturePoint p : pointList) {
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

    public void reset () {
        drawPath.reset();
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        pointList.clear();
        invalidate();
    }
}
