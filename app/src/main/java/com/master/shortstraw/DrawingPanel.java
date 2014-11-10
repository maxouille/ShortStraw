package com.master.shortstraw;

import android.content.Context;
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
    private Paint drawPaintLine, drawPaintPoints, drawPaintCorners, canvasPaint;
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


        //Set the drawPaintCorner
        drawPaintCorners = new Paint();
        drawPaintCorners.setColor(0xFF66DD00);
        drawPaintCorners.setAntiAlias(true);
        drawPaintCorners.setStrokeWidth(20);
        drawPaintCorners.setStyle(Paint.Style.STROKE);
        drawPaintCorners.setStrokeJoin(Paint.Join.ROUND);
        drawPaintCorners.setStrokeCap(Paint.Cap.ROUND);

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
        canvas.drawPath(drawPath, drawPaintLine);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                pointList.add(new PointF(touchX, touchY));
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                //Draw the stroke
                drawCanvas.drawPath(drawPath, drawPaintLine);

                //Draw the points not sampled
                for (PointF po : pointList) {
                    drawCanvas.drawPoint(po.x, po.y, drawPaintPoints);
                }

                ShortStraw ss = new ShortStraw();
                ArrayList<PointF> cornerPoints = ss.getCornerPoints(pointList);

                //Check if there is no error or if the list is not empty
                if (cornerPoints != null && cornerPoints.size() != 0) {
                    //Draw corners
                    for (PointF po : cornerPoints) {
                        drawCanvas.drawPoint(po.x, po.y, drawPaintCorners);
                    }
                }

                drawPath.reset();
                pointList.clear();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void reset () {
        drawPath.reset();
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        pointList.clear();
        invalidate();
    }
}
