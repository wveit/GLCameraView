package com.example.androidu.glcamera.wilbert;

import android.hardware.SensorEvent;
import android.location.Location;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;

import com.example.androidu.glcamera.R;
import com.example.androidu.glcamera.ar_framework.graphics3d.Camera3D;
import com.example.androidu.glcamera.ar_framework.graphics3d.billboard.BillboardMaker;
import com.example.androidu.glcamera.ar_framework.graphics3d.billboard.SizedBillboard;
import com.example.androidu.glcamera.ar_framework.sensor.MyGps;
import com.example.androidu.glcamera.ar_framework.sensor.MySensor;
import com.example.androidu.glcamera.ar_framework.ui.GLCameraActivity;
import com.example.androidu.glcamera.ar_framework.util.MyMath;
import com.example.androidu.glcamera.landmark.Landmark;
import com.example.androidu.glcamera.landmark.LandmarkTable;

import java.util.ArrayList;


public class BillboardLandmarksActivity extends GLCameraActivity {
    static final String TAG = "waka_BBLandmarks";

    ArrayList<SizedBillboard> billboardList = null;
    LandmarkTable landmarkTable = new LandmarkTable();
    Camera3D mCamera;

    MySensor mOrientation;
    MyGps mGps;

    float[] scratchMatrix = new float[16];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mOrientation = new MySensor(this, MySensor.ROTATION_VECTOR);
        mOrientation.addListener(mOrientationListener);

        mGps = new MyGps(this);
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

        if(landmarkTable.isEmpty())
            landmarkTable.loadCities();

        mCamera = new Camera3D();
    }

    @Override
    public void GLResize(int width, int height) {
        super.GLResize(width, height);

        GLES20.glViewport(0, 0, width, height);
        mCamera.setPerspective(60, (float)width / height, 0.1f, 1000f);
    }

    @Override
    public void GLDraw() {
        super.GLDraw();

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        mCamera.updateViewMatrix();

        if(latLonAlt == null)
            return;
        else if(billboardList == null)
            setupBillboards();
        else
            drawBillboards();
    }


    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    //      Drawing Functions
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private void setupBillboards(){
        if(billboardList != null)
            return;

        SizedBillboard.init(this);
        billboardList = new ArrayList<>();
        Landmark here = new Landmark("", "", latLonAlt[0], latLonAlt[1], 100);

        int numLandmarks = landmarkTable.size();
        for(int i = 0; i < numLandmarks; i++){
            Landmark current = landmarkTable.get(i);
            float distance = here.distance(current);
            float angle = here.compassDirection(current);
//            Log.d(TAG, current.title + "  distance: " + distance + "  angle: " + angle);

            SizedBillboard currentBillboard = BillboardMaker.make(this, 5, R.drawable.ara_icon, current.title, current.description);
            float[] matrix = currentBillboard.getMatrix();

//            Matrix.setIdentityM(matrix, 0);
//            Matrix.translateM(matrix, 0, 0, 0, -10 - distance * 0.00001f);
//            Matrix.rotateM(matrix, 0, -angle, 0, 1, 0);

            Matrix.setIdentityM(currentBillboard.getMatrix(), 0);
            Matrix.translateM(currentBillboard.getMatrix(), 0, 0, 0, -10 - distance * 0.00001f);
            Matrix.setIdentityM(scratchMatrix, 0);
            Matrix.rotateM(scratchMatrix, 0, -angle, 0, 1, 0);
            Matrix.multiplyMM(currentBillboard.getMatrix(), 0, scratchMatrix, 0, currentBillboard.getMatrix(), 0);

            float[] vec = {0, 0, 0, 1};
            float[] resultVec = new float[4];
            Matrix.multiplyMV(resultVec, 0, currentBillboard.getMatrix(), 0, vec, 0);
            Log.d(TAG, current.title + "  " + MyMath.vecToString(resultVec));

            billboardList.add(currentBillboard);
        }
    }

    private void drawBillboards(){

        int numBillboards = billboardList.size();

//        Log.d(TAG, "drawing billboards: " + numBillboards);

        for(int i = 0; i < numBillboards; i++){
            SizedBillboard currentBB = billboardList.get(i);
            Matrix.multiplyMM(scratchMatrix, 0, mCamera.getViewProjectionMatrix(), 0, currentBB.getMatrix(), 0);
            currentBB.draw(scratchMatrix);
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    //      Orientation Listener
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    MySensor.Listener mOrientationListener = new MySensor.Listener() {
        @Override
        public void onSensorEvent(SensorEvent event) {

            float[] matrix = new float[16];
            portraitMatrixFromRotation(matrix, event.values);

            if(mCamera != null){
                mCamera.setByMatrix(matrix);
            }

        }


        private void portraitMatrixFromRotation(float[] matrix, float[] rotation){
            float[] rVec = {rotation[0], rotation[1], rotation[2]};
            float magnitude = MyMath.magnitude(rVec);
            rVec[0] /= magnitude;
            rVec[1] /= magnitude;
            rVec[2] /= magnitude;
            float angle = MyMath.radToDegrees(2 * (float)Math.asin(magnitude));


            Matrix.setRotateM(matrix, 0, angle, rVec[0], rVec[1], rVec[2]);


            float[] adjustMatrix = new float[16];
            Matrix.setRotateM(adjustMatrix, 0, 90, -1, 0, 0);
            Matrix.multiplyMM(matrix, 0, adjustMatrix, 0, matrix, 0);
        }

        private void landscapeMatrixFromRotation(float[] matrix, float[] rotation){
            float[] rVec = {rotation[0], rotation[1], rotation[2]};
            float magnitude = MyMath.magnitude(rVec);
            rVec[0] /= magnitude;
            rVec[1] /= magnitude;
            rVec[2] /= magnitude;
            float angle = MyMath.radToDegrees(2 * (float)Math.asin(magnitude));


            Matrix.setRotateM(matrix, 0, angle, rVec[0], rVec[1], rVec[2]);


            float[] adjustMatrix = new float[16];
            Matrix.setRotateM(adjustMatrix, 0, 90, 0, 0, 1);
            Matrix.rotateM(adjustMatrix, 0, 90, -1, 0, 0);
            Matrix.multiplyMM(matrix, 0, adjustMatrix, 0, matrix, 0);
        }


    };


    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    //      GPS Listener
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    Location mOriginalLoc = null;
    float[] mPosition = new float[3];
    float[] latLonAlt = null;

    MyGps.Listener mGPSListener = new MyGps.Listener() {

        @Override
        public void handleLocation(Location location) {

            if(mOriginalLoc == null){
                mOriginalLoc = new Location(location);
                latLonAlt = new float[3];
            }


            float distance = location.distanceTo(mOriginalLoc);
            float bearing = location.bearingTo(mOriginalLoc);

            mPosition[0] = distance * (float)Math.cos(MyMath.degreesToRad(bearing));
            mPosition[1] = distance * (float)Math.sin(MyMath.degreesToRad(bearing));
            mPosition[2] = (float)(location.getAltitude() - mOriginalLoc.getAltitude());

            latLonAlt[0] = (float)location.getLatitude();
            latLonAlt[1] = (float)location.getLongitude();
            latLonAlt[2] = (float)location.getAltitude();

            Log.d(TAG, String.format("gps: [%f, %f, %f]\n", latLonAlt[0], latLonAlt[1], latLonAlt[2]));
        }


    };

}
