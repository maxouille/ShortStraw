package com.master.shortstraw;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.master.shortstraw.Model.Line;
import com.master.shortstraw.Model.PolyLine;
import com.master.shortstraw.Model.Square;
import com.master.shortstraw.Model.Triangle;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Maxence Bobin on 05/11/14.
 */
public class DrawingPanel extends View {


    private ArrayList<PointF> pointList = new ArrayList<PointF>();

    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaintLine, drawPaintPoints, drawPaintSampledPoints, drawPaintCorners, drawPaintBox, drawPaintShape, canvasPaint;
    //initial color
    private int paintColor = 0xFF660000;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;

    private boolean drawLine = true;
    private boolean drawPoints = false;
    private boolean drawSampledPoints = false;
    private boolean drawBox = false;
    private boolean drawCorners = true;

    private ArrayList<PointF> cornerPoints = new ArrayList<PointF>();
    private ShortStraw ss;
    private ShapeDetector sd;

    private ArrayList<PointF> pList = new ArrayList<PointF>();
    private ArrayList<Line> lineList = new ArrayList<Line>();
    private ArrayList<Triangle> triangleList = new ArrayList<Triangle>();
    private ArrayList<Square> squareList = new ArrayList<Square>();
    private ArrayList<PolyLine> polyLineList = new ArrayList<PolyLine>();

    public DrawingPanel(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing() {
        ss = new ShortStraw(this);

        drawPath = new Path();

        //Set the drawPaintLine
        drawPaintLine = new Paint();
        //Set the color of the stroke
        drawPaintLine.setColor(paintColor);
        //Set the initial path properties
        drawPaintLine.setAntiAlias(true);
        drawPaintLine.setStrokeWidth(20);
        drawPaintLine.setStyle(Paint.Style.STROKE);
        drawPaintLine.setStrokeJoin(Paint.Join.ROUND);
        drawPaintLine.setStrokeCap(Paint.Cap.ROUND);

        //Set the drawPaintPoints
        drawPaintPoints = new Paint();
        drawPaintPoints.setColor(0xFF4466DD);
        drawPaintPoints.setAntiAlias(true);
        drawPaintPoints.setStrokeWidth(20);
        drawPaintPoints.setStyle(Paint.Style.STROKE);
        drawPaintPoints.setStrokeJoin(Paint.Join.ROUND);
        drawPaintPoints.setStrokeCap(Paint.Cap.ROUND);

        //Set the drawPaintSampledPoints
        drawPaintSampledPoints = new Paint();
        drawPaintSampledPoints.setColor(0xFFDD2200);
        drawPaintSampledPoints.setAntiAlias(true);
        drawPaintSampledPoints.setStrokeWidth(20);
        drawPaintSampledPoints.setStyle(Paint.Style.STROKE);
        drawPaintSampledPoints.setStrokeJoin(Paint.Join.ROUND);
        drawPaintSampledPoints.setStrokeCap(Paint.Cap.ROUND);

        //Set the drawPaintCorner
        drawPaintCorners = new Paint();
        drawPaintCorners.setColor(0xFF66DD00);
        drawPaintCorners.setAntiAlias(true);
        drawPaintCorners.setStrokeWidth(20);
        drawPaintCorners.setStyle(Paint.Style.STROKE);
        drawPaintCorners.setStrokeJoin(Paint.Join.ROUND);
        drawPaintCorners.setStrokeCap(Paint.Cap.ROUND);

        //Set the drawPaintBox
        drawPaintBox = new Paint();
        drawPaintBox.setColor(0xFF662200);
        drawPaintBox.setAntiAlias(true);
        drawPaintBox.setStrokeWidth(20);
        drawPaintBox.setStyle(Paint.Style.STROKE);
        drawPaintBox.setStrokeJoin(Paint.Join.ROUND);
        drawPaintBox.setStrokeCap(Paint.Cap.ROUND);

        //Set the drawPaintBox
        drawPaintShape = new Paint();
        drawPaintShape.setColor(0xFF000000);
        drawPaintShape.setAntiAlias(true);
        drawPaintShape.setStrokeWidth(20);
        drawPaintShape.setStyle(Paint.Style.STROKE);
        drawPaintShape.setStrokeJoin(Paint.Join.ROUND);
        drawPaintShape.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);

        sd = new ShapeDetector(cornerPoints, this);
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

        //Draw the boundingBox
        if (drawBox) {
            drawCanvas.drawRect(ss.getBoundingBox(), drawPaintBox);
        }

        if (drawLine) {
            //Draw the stroke
            drawCanvas.drawPath(drawPath, drawPaintLine);
        }

        if(drawPoints) {
            //Draw the points not sampled
            for (PointF po : pointList) {
                drawCanvas.drawPoint(po.x, po.y, drawPaintPoints);
            }
        }

        //Draw sampled points
        if (drawSampledPoints) {
            for (PointF p : ss.getResampledPoints()) {
                drawCanvas.drawPoint(p.x, p.y, drawPaintSampledPoints);
            }
        }

        //Check if there is no error or if the list is not empty
        if (cornerPoints != null && cornerPoints.size() != 0) {
            if (drawCorners) {
                //Draw corners
                for (PointF po : cornerPoints) {
                    drawCanvas.drawPoint(po.x, po.y, drawPaintCorners);
                }
            }
        }

        //draw shapes
        for (PointF p : pList) {
            drawCanvas.drawPoint(p.x, p.y, drawPaintShape);
        }

        for (Line l : lineList) {
            drawCanvas.drawLine(l.getP1().x, l.getP1().y, l.getP2().x, l.getP2().y, drawPaintShape);
        }

        for (Triangle t : triangleList) {
            Path triangle = new Path();
            triangle.moveTo(t.getP1().x, t.getP1().y);
            triangle.lineTo(t.getP2().x, t.getP2().y);
            triangle.lineTo(t.getP3().x, t.getP3().y);
            triangle.close();
            drawCanvas.drawPath(triangle, drawPaintShape);
        }

        for (Square s : squareList) {
            drawCanvas.save();
            PointF bar = s.getBaryCenter();
            float length = s.getEdgeLength()/2;
            canvas.rotate(s.getAngle(), s.getBaryCenter().x, s.getBaryCenter().y);
            RectF square = new RectF(bar.x-length, bar.y-length, bar.x+length, bar.y+length);
            drawCanvas.drawRect(square, drawPaintShape);
            drawCanvas.restore();
        }


        for (PolyLine pl : polyLineList) {
            ArrayList<PointF> pList = pl.getPointList();
            Path plPath = new Path();
            plPath.moveTo(pList.get(0).x, pList.get(0).y);
            for (int i = 1; i < pList.size(); i++) {
                plPath.lineTo(pList.get(i).x, pList.get(i).y);
            }
            drawCanvas.drawPath(plPath, drawPaintShape);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                restorDefaultBooleans();
                //Reset drawPath and pointList
                drawPath.reset();
                pointList.clear();
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                pointList.add(new PointF(touchX, touchY));
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                drawLine = false;
                ss = new ShortStraw(this);
                cornerPoints = ss.getCornerPoints(pointList);

                //Set the cornerPoints into the shapeDetector
                sd.setCorners(cornerPoints);

                sd.detection();

                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    /**
     * GETTERS AND SETTERS
     */
    public boolean isDrawLine() {
        return drawLine;
    }

    public void setDrawLine(boolean drawLine) {
        this.drawLine = drawLine;
    }

    public boolean isDrawPoints() {
        return drawPoints;
    }

    public void setDrawPoints(boolean drawPoints) {
        this.drawPoints = drawPoints;
    }

    public boolean isDrawSampledPoints() {
        return drawSampledPoints;
    }

    public void setDrawSampledPoints(boolean drawSampledPoints) {
        this.drawSampledPoints = drawSampledPoints;
    }

    public boolean isDrawBox() {
        return drawBox;
    }

    public void setDrawBox(boolean drawBox) {
        this.drawBox = drawBox;
    }

    public boolean isDrawCorners() {
        return drawCorners;
    }

    public void setDrawCorners(boolean drawCorners) {
        this.drawCorners = drawCorners;
    }

    public Paint getDrawPaintBox() {
        return drawPaintBox;
    }

    public void setDrawPaintBox(Paint drawPaintBox) {
        this.drawPaintBox = drawPaintBox;
    }

    public Paint getDrawPaintShape() {
        return drawPaintShape;
    }

    public void setDrawPaintShape(Paint drawPaintShape) {
        this.drawPaintShape = drawPaintShape;
    }

    public Paint getDrawPaintSampledPoints() {
        return drawPaintSampledPoints;
    }

    public void setDrawPaintSampledPoints(Paint drawPaintSampledPoints) {
        this.drawPaintSampledPoints = drawPaintSampledPoints;
    }

    public Canvas getDrawCanvas() {
        return drawCanvas;
    }

    public void setDrawCanvas(Canvas drawCanvas) {
        this.drawCanvas = drawCanvas;
    }

    /**
     * Save a point into the list
     * @param p
     */
    public void addPoint (PointF p) {
        pList.add(p);
    }

    /**
     * Save a line into the list
     * @param l
     */
    public void addLine (Line l) {
        lineList.add(l);
    }

    /**
     * Save a triangle into the list
     * @param t
     */
    public void addTriangle (Triangle t) {
        triangleList.add(t);
    }

    /**
     * Save a square into the list
     * @param s
     */
    public void addSquare (Square s) {
        squareList.add(s);
    }

    /**
     * Save a polyLine into the list
     * @param pl
     */
    public void  addPolyLine (PolyLine pl) {
        polyLineList.add(pl);
    }

    /**
     * Restore the default boolean for a new Path
     */
    public void restorDefaultBooleans() {
        drawLine = true;
        drawPoints = false;
        drawSampledPoints = false;
        drawBox = false;
        drawCorners = true;
    }

    /**
     * Reset the canvas
     */
    public void reset () {
        drawPath.reset();
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        cornerPoints.clear();
        pointList.clear();
        restorDefaultBooleans();
        pList.clear();
        lineList.clear();
        triangleList.clear();
        squareList.clear();
        polyLineList.clear();
        invalidate();
    }
}
