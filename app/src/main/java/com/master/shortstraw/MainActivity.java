package com.master.shortstraw;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawing_view);

        ImageButton resetButton = (ImageButton) findViewById(R.id.resetButton);

        final DrawingPanel drawingPanel = (DrawingPanel) findViewById(R.id.drawingPanel);
        resetButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                drawingPanel.reset();
           }
        });

    }
}