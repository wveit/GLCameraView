package com.example.androidu.glcamera.wilbert;

import android.hardware.SensorEvent;
import android.location.Location;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;

import com.example.androidu.glcamera.R;
import com.example.androidu.glcamera.ar_framework.graphics3d.camera.Camera;
import com.example.androidu.glcamera.ar_framework.graphics3d.drawable.billboard.BillboardMaker;
import com.example.androidu.glcamera.ar_framework.graphics3d.drawable.billboard.SizedBillboard;
import com.example.androidu.glcamera.ar_framework.graphics3d.projection.Projection;
import com.example.androidu.glcamera.ar_framework.sensor.ARGps;
import com.example.androidu.glcamera.ar_framework.sensor.ARSensor;
import com.example.androidu.glcamera.ar_framework.ui.ARActivity;
import com.example.androidu.glcamera.ar_framework.util.MatrixMath;
import com.example.androidu.glcamera.ar_framework.util.VectorMath;


public class BillboardCompassActivity extends ARActivity {
    static final String TAG = "waka_BBCompass";

    SizedBillboard mNorthBB, mEastBB, mSouthBB, mWestBB;
    Camera mCamera;
    Projection mProjection;

    ARSensor mOrientation;
    ARGps mGps;

    float[] scratchMatrix = new float[16];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mOrientation = new ARSensor(this, ARSensor.ROTATION_VECTOR);
        mOrientation.addListener(mOrientationListener);

        mGps = new ARGps(this);
        mGps.addListener(mGPSListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mOrientation.stop();
        mGps.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mOrientation.start();
        mGps.start();
    }

    @Override
    public void GLInit() {
        super.GLInit();

        GLES20.glClearColor(0, 0, 0, 0);

        SizedBillboard.init(this);
        mNorthBB = BillboardMaker.make(this, 5, R.drawable.ara_icon, "North", "A compass direction");
        mEastBB = BillboardMaker.make(this, 5, R.drawable.ara_icon, "East", "A compass direction");
        mSouthBB = BillboardMaker.make(this, 5, R.drawable.ara_icon, "South", "A compass direction");
        mWestBB = BillboardMaker.make(this, 5, R.drawable.ara_icon, "West", "A compass direction");
        positionCompass();

        mCamera = new Camera();
        mProjection = new Projection();
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


        drawCompass();
    }


    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    //      Drawing Functions
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private void positionCompass(){
        Matrix.setIdentityM(mNorthBB.getMatrix(), 0);
        Matrix.translateM(mNorthBB.getMatrix(), 0, 0, 0, -10);
        Matrix.setRotateM(scratchMatrix, 0, -90, 0, 1, 0);

        Matrix.multiplyMM(mEastBB.getMatrix(), 0, scratchMatrix, 0, mNorthBB.getMatrix(), 0);
        Matrix.multiplyMM(mSouthBB.getMatrix(), 0, scratchMatrix, 0, mEastBB.getMatrix(), 0);
        Matrix.multiplyMM(mWestBB.getMatrix(), 0, scratchMatrix, 0, mSouthBB.getMatrix(), 0);
    }

    private void drawCompass(){

        MatrixMath.multiplyMatrices(scratchMatrix, mProjection.getProjectionMatrix(), mCamera.getViewMatrix(), mNorthBB.getMatrix());
        mNorthBB.draw(scratchMatrix);

        MatrixMath.multiplyMatrices(scratchMatrix, mProjection.getProjectionMatrix(), mCamera.getViewMatrix(), mEastBB.getMatrix());
        mEastBB.draw(scratchMatrix);

        MatrixMath.multiplyMatrices(scratchMatrix, mProjection.getProjectionMatrix(), mCamera.getViewMatrix(), mSouthBB.getMatrix());
        mSouthBB.draw(scratchMatrix);

        MatrixMath.multiplyMatrices(scratchMatrix, mProjection.getProjectionMatrix(), mCamera.getViewMatrix(), mWestBB.getMatrix());
        mWestBB.draw(scratchMatrix);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    //      Orientation Listener
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    ARSensor.Listener mOrientationListener = new ARSensor.Listener() {
        @Override
        public void onSensorEvent(SensorEvent event) {

            if (mCamera != null) {
                mCamera.setOrientationVector(event.values, 0);
            }

        }
    };

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    //      GPS Listener
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    Location mOriginalLoc = null;
    float[] mPosition = new float[3];

    ARGps.Listener mGPSListener = new ARGps.Listener() {
        @Override
        public void handleLocation(Location location) {

            if(mOriginalLoc == null){
                mOriginalLoc = new Location(location);
            }

            float distance = location.distanceTo(mOriginalLoc);
            float bearing = location.bearingTo(mOriginalLoc);

            mPosition[0] = distance * (float)Math.cos(VectorMath.degreesToRad(bearing));
            mPosition[1] = distance * (float)Math.sin(VectorMath.degreesToRad(bearing));
            mPosition[2] = (float)(location.getAltitude() - mOriginalLoc.getAltitude());
        }
    };
}
