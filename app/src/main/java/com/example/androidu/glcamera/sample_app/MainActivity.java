package com.example.androidu.glcamera.sample_app;

import android.opengl.GLES20;
import android.os.Bundle;

import com.example.androidu.glcamera.ar_framework.ui.GLCameraActivity;
import com.example.androidu.glcamera.ar_framework.graphic3d.Model;


public class MainActivity extends GLCameraActivity {

    private Model mTriangle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void GLInit() {
        mTriangle = new Model();
        mTriangle.loadTriangle();
        GLES20.glClearColor(0, 0, 0, 0);
    }

    @Override
    public void GLResize(int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void GLDraw() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        mTriangle.draw();
    }
}
