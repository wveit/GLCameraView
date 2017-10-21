package com.example.androidu.glcamera.sample_app;

import android.opengl.GLES20;
import android.os.Bundle;

import com.example.androidu.glcamera.ar_framework.graphics3d.Camera3D;
import com.example.androidu.glcamera.ar_framework.ui.GLCameraActivity;
import com.example.androidu.glcamera.ar_framework.graphics3d.Model3D;


public class MainActivity extends GLCameraActivity {

    float z = 0;
    private Model3D mTriangle;
    private Camera3D mCamera3D;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void GLInit() {
        mTriangle = new Model3D();
        mTriangle.loadTriangle();
        mCamera3D = new Camera3D();
        GLES20.glClearColor(0, 0, 0, 0);
    }

    @Override
    public void GLResize(int width, int height) {
        mCamera3D.viewport(width, height);
    }

    @Override
    public void GLDraw() {
        z += 0.01;
        mCamera3D.setTranslate(0, 0, z);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        float[] matrix = mCamera3D.getVPMatrix();
        mTriangle.draw(matrix);
    }
}
