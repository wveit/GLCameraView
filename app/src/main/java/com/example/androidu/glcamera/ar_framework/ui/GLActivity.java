package com.example.androidu.glcamera.ar_framework.ui;


import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class GLActivity extends AppCompatActivity{

    GLSurfaceView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        view = new GLSurfaceView(this);
        setContentView(view);
        view.setEGLContextClientVersion(2);
        view.setRenderer(renderer);
    }

    @Override
    protected void onPause() {
        super.onPause();
        view.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.onResume();
    }

    public void GLInit(){}
    public void GLResize(int width, int height){}
    public void GLDraw(){};

    GLSurfaceView.Renderer renderer = new GLSurfaceView.Renderer(){

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLInit();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLResize(width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLDraw();
        }
    };

    SurfaceHolder.Callback surfaceHolderCallback = new SurfaceHolder.Callback(){

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };


}
