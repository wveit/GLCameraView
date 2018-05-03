package com.example.androidu.glcamera.wilbert;

import android.hardware.SensorEvent;
import android.location.Location;
import android.opengl.GLES20;
import android.os.Bundle;

import com.example.androidu.glcamera.R;
import com.example.androidu.glcamera.ar_framework.graphics3d.camera.Camera;
import com.example.androidu.glcamera.ar_framework.graphics3d.drawable.Billboard;
import com.example.androidu.glcamera.ar_framework.graphics3d.drawable.BillboardMaker;
import com.example.androidu.glcamera.ar_framework.graphics3d.entity.Entity;
import com.example.androidu.glcamera.ar_framework.graphics3d.projection.Projection;
import com.example.androidu.glcamera.ar_framework.graphics3d.scene.CircleScene;
import com.example.androidu.glcamera.ar_framework.sensor.ARGps;
import com.example.androidu.glcamera.ar_framework.sensor.ARSensor;
import com.example.androidu.glcamera.ar_framework.ui.ARActivity;
import com.example.androidu.glcamera.landmark.Landmark;
import com.example.androidu.glcamera.landmark.LandmarkTable;


public class BillboardLandmarksActivity extends ARActivity {
    static final String TAG = "waka_BBLandmarks";

    LandmarkTable landmarkTable = new LandmarkTable();

    CircleScene mScene;
    Camera mCamera;
    Projection mProjection;

    ARSensor mOrientationSensor;
    ARGps mGps;


    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    //      Activity Callbacks
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mOrientationSensor = new ARSensor(this, ARSensor.ROTATION_VECTOR);
        mOrientationSensor.addListener(mOrientationListener);

        mGps = new ARGps(this);
        mGps.addListener(mGPSListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mOrientationSensor.stop();
        mGps.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mOrientationSensor.start();
        mGps.start();
    }


    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    //      OpenGL Callbacks
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void GLInit() {
        super.GLInit();

        GLES20.glClearColor(0, 0, 0, 0);


        mScene = new CircleScene();
        mCamera = new Camera();
        mProjection = new Projection();


        if(landmarkTable.isEmpty())
            landmarkTable.loadCalstateLA();

        setupBillboards();
    }

    @Override
    public void GLResize(int width, int height) {
        super.GLResize(width, height);

        GLES20.glViewport(0, 0, width, height);
        mProjection.setPerspective(60, (float)width / height, 0.1f, 1000f);
    }

    @Override
    public void GLDraw() {
        super.GLDraw();

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        mScene.draw(mProjection.getProjectionMatrix(), mCamera.getViewMatrix());
    }


    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    //      Drawing Helper Functions
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    private void setupBillboards(){
        mScene.setRadius(5);
        for(Landmark l : landmarkTable){
            Billboard bb = BillboardMaker.make(this, R.drawable.ara_icon, l.title, l.description);
            Entity entity = mScene.addDrawable(bb);
            entity.setPositionLatLonAlt(new float[]{l.latitude, l.longitude, l.altitude});
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    //      Sensor Callbacks
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    private float[] orientation = null;
    ARSensor.Listener mOrientationListener = new ARSensor.Listener() {
        @Override
        public void onSensorEvent(SensorEvent event) {

            if(orientation == null)
                orientation = new float[3];

            orientation[0] = event.values[0];
            orientation[1] = event.values[1];
            orientation[2] = event.values[2];
        }
    };


    private float[] latLonAlt = null;
    ARGps.Listener mGPSListener = new ARGps.Listener() {

        @Override
        public void handleLocation(Location location) {

            if(latLonAlt == null)
                latLonAlt = new float[3];

            latLonAlt[0] = (float)location.getLatitude();
            latLonAlt[1] = (float)location.getLongitude();
            latLonAlt[2] = (float)location.getAltitude();
        }
    };

}
