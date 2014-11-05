package com.master.shortstraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxence Bobin on 05/11/14.
 */
public class DrawingPanel extends View {


    private ArrayList<PointF> pointList = new ArrayList<PointF>();
    private ArrayList<PointF> resamplePointList = new ArrayList<PointF>();

    float distance = 0;

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
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                pointList.add(new PointF(touchX, touchY));
                //drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                //drawCanvas.drawPath(drawPath, drawPaint);
                //drawPath.reset();
                for (PointF p : pointList) {
                    drawCanvas.drawPoint(p.x, p.y, drawPaint);
                }
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
        invalidate();
    }
}
