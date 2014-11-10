package com.master.shortstraw;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawing_view);

        ImageButton resetButton = (ImageButton) findViewById(R.id.resetButton);
        ImageButton lineButton = (ImageButton) findViewById(R.id.strokeButton);
        ImageButton pointButton = (ImageButton) findViewById(R.id.pointsButton);
        ImageButton sampleButton = (ImageButton) findViewById(R.id.sampleButton);
        ImageButton rectButton = (ImageButton) findViewById(R.id.boxButton);
        ImageButton cornerButton = (ImageButton) findViewById(R.id.cornerButton);

        final DrawingPanel drawingPanel = (DrawingPanel) findViewById(R.id.drawingPanel);
        resetButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                drawingPanel.reset();
           }
        });

        lineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingPanel.setDrawLine(!drawingPanel.isDrawLine());
                drawingPanel.getDrawCanvas().drawColor(0, PorterDuff.Mode.CLEAR);
                drawingPanel.invalidate();
            }
        });

        pointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingPanel.setDrawPoints(!drawingPanel.isDrawPoints());
                drawingPanel.getDrawCanvas().drawColor(0, PorterDuff.Mode.CLEAR);
                drawingPanel.invalidate();
            }
        });

        sampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingPanel.setDrawSampledPoints(!drawingPanel.isDrawSampledPoints());
                drawingPanel.getDrawCanvas().drawColor(0, PorterDuff.Mode.CLEAR);
                drawingPanel.invalidate();
            }
        });

        rectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingPanel.setDrawBox(!drawingPanel.isDrawBox());
                drawingPanel.getDrawCanvas().drawColor(0, PorterDuff.Mode.CLEAR);
                drawingPanel.invalidate();
            }
        });

        cornerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingPanel.setDrawCorners(!drawingPanel.isDrawCorners());
                drawingPanel.getDrawCanvas().drawColor(0, PorterDuff.Mode.CLEAR);
                drawingPanel.invalidate();
            }
        });
    }
}