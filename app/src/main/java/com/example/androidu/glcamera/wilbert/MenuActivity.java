package com.example.androidu.glcamera.wilbert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.androidu.glcamera.R;

public class MenuActivity extends AppCompatActivity {

    Button compassButton;
    Button landmarksButton;
    Button shapeDrawingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wilbert);

        compassButton = (Button)findViewById(R.id.btn_billboard_compass);
        landmarksButton = (Button)findViewById(R.id.btn_landmarks);
        shapeDrawingButton = (Button)findViewById(R.id.btn_shape_draw);

        compassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, BillboardCompassActivity.class);
                startActivity(intent);
            }
        });

        landmarksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, BillboardLandmarksActivity.class);
                startActivity(intent);
            }
        });

        shapeDrawingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ShapeDrawActivity.class);
                startActivity(intent);
            }
        });


    }
}
