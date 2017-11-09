package com.example.androidu.glcamera.wilbert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.androidu.glcamera.R;

public class WilbertActivity extends AppCompatActivity {

    Button compassButton;
    Button landmarksButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wilbert);

        compassButton = (Button)findViewById(R.id.btn_billboard_compass);
        landmarksButton = (Button)findViewById(R.id.btn_landmarks);

        compassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WilbertActivity.this, BillboardCompassActivity.class);
                startActivity(intent);
            }
        });

        landmarksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WilbertActivity.this, BillboardLandmarksActivity.class);
                startActivity(intent);
            }
        });


    }
}
