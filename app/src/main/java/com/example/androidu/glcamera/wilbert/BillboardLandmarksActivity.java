package com.example.androidu.glcamera.wilbert;

import android.hardware.SensorEvent;
import android.location.Location;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.widget.Toast;

import com.example.androidu.glcamera.R;
import com.example.androidu.glcamera.ar_framework.graphics3d.camera.Camera3D;
import com.example.androidu.glcamera.ar_framework.graphics3d.drawable.billboard.BillboardMaker;
import com.example.androidu.glcamera.ar_framework.graphics3d.drawable.billboard.SizedBillboard;
import com.example.androidu.glcamera.ar_framework.graphics3d.projection.Projection;
import com.example.androidu.glcamera.ar_framework.sensor.ARGps;
import com.example.androidu.glcamera.ar_framework.sensor.ARSensor;
import com.example.androidu.glcamera.ar_framework.ui.ARActivity;
import com.example.androidu.glcamera.ar_framework.util.MatrixMath;
import com.example.androidu.glcamera.ar_framework.util.VectorMath;
import com.example.androidu.glcamera.landmark.Landmark;
import com.example.androidu.glcamera.landmark.LandmarkTable;

import java.util.ArrayList;


public class BillboardLandmarksActivity extends ARActivity {
    static final String TAG = "waka_BBLandmarks";

    ArrayList<SizedBillboard> billboardList = null;
    LandmarkTable landmarkTable = new LandmarkTable();
    Camera3D mCamera;
    Projection mProjection;

    ARSensor mOrientation;
    ARGps mGps;

    float[] scratchMatrix = new float[16];
    boolean billboardLoadingComplete = false;


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

        if(landmarkTable.isEmpty())
            landmarkTable.loadCalstateLA();

        //billboardList = null;

        mCamera = new Camera3D();
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
        //Log.d(TAG, "num landmarks: " + numLandmarks);
        for(int i = 0; i < numLandmarks; i++){
            Landmark current = landmarkTable.get(i);
            float distance = here.distance(current);
            float angle = here.compassDirection(current);

            SizedBillboard currentBillboard = BillboardMaker.make(this, 5, R.drawable.ara_icon, current.title, current.description);
            float[] matrix = currentBillboard.getMatrix();

            Matrix.setIdentityM(currentBillboard.getMatrix(), 0);
            Matrix.translateM(currentBillboard.getMatrix(), 0, 0, 0, -10 - distance * 0.00001f);
            Matrix.setIdentityM(scratchMatrix, 0);
            Matrix.rotateM(scratchMatrix, 0, -angle, 0, 1, 0);
            Matrix.multiplyMM(currentBillboard.getMatrix(), 0, scratchMatrix, 0, currentBillboard.getMatrix(), 0);

            float[] vec = {0, 0, 0, 1};
            float[] resultVec = new float[4];
            Matrix.multiplyMV(resultVec, 0, currentBillboard.getMatrix(), 0, vec, 0);
            //Log.d(TAG, current.title + "  " + VectorMath.vecToString(resultVec));

            billboardList.add(currentBillboard);
        }

        billboardLoadingComplete = true;
    }

    private void updateBillboards(){
        if(billboardList == null || !billboardLoadingComplete)
            return;

        Landmark here = new Landmark("", "", latLonAlt[0], latLonAlt[1], 100);

        int numLandmarks = landmarkTable.size();
        for(int i = 0; i < numLandmarks; i++){
            Landmark current = landmarkTable.get(i);
            float distance = here.distance(current);
            float angle = here.compassDirection(current);

            SizedBillboard currentBillboard = billboardList.get(i);
            float[] matrix = currentBillboard.getMatrix();

            Matrix.setIdentityM(currentBillboard.getMatrix(), 0);
            Matrix.translateM(currentBillboard.getMatrix(), 0, 0, 0, -10 - distance * 0.00001f);
            Matrix.setIdentityM(scratchMatrix, 0);
            Matrix.rotateM(scratchMatrix, 0, -angle, 0, 1, 0);
            Matrix.multiplyMM(currentBillboard.getMatrix(), 0, scratchMatrix, 0, currentBillboard.getMatrix(), 0);
        }
    }

    private void drawBillboards(){

        int numBillboards = billboardList.size();

        for(int i = 0; i < numBillboards; i++){
            SizedBillboard currentBB = billboardList.get(i);
            MatrixMath.multiplyMatrices(scratchMatrix, mProjection.getProjectionMatrix(), mCamera.getViewMatrix(), currentBB.getMatrix());
            currentBB.draw(scratchMatrix);
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    //      Orientation Listener
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    ARSensor.Listener mOrientationListener = new ARSensor.Listener() {
        @Override
        public void onSensorEvent(SensorEvent event) {

            float[] matrix = new float[16];
            portraitMatrixFromRotation(matrix, event.values);

            if(mCamera != null){
                mCamera.setRotationByMatrix(matrix);
            }

        }


        private void portraitMatrixFromRotation(float[] matrix, float[] rotation){
            float[] rVec = {rotation[0], rotation[1], rotation[2]};
            float magnitude = VectorMath.magnitude(rVec);
            rVec[0] /= magnitude;
            rVec[1] /= magnitude;
            rVec[2] /= magnitude;
            float angle = VectorMath.radToDegrees(2 * (float)Math.asin(magnitude));


            Matrix.setRotateM(matrix, 0, angle, rVec[0], rVec[1], rVec[2]);


            float[] adjustMatrix = new float[16];
            Matrix.setRotateM(adjustMatrix, 0, 90, -1, 0, 0);
            Matrix.multiplyMM(matrix, 0, adjustMatrix, 0, matrix, 0);
        }

        private void landscapeMatrixFromRotation(float[] matrix, float[] rotation){
            float[] rVec = {rotation[0], rotation[1], rotation[2]};
            float magnitude = VectorMath.magnitude(rVec);
            rVec[0] /= magnitude;
            rVec[1] /= magnitude;
            rVec[2] /= magnitude;
            float angle = VectorMath.radToDegrees(2 * (float)Math.asin(magnitude));


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

    ARGps.Listener mGPSListener = new ARGps.Listener() {

        @Override
        public void handleLocation(Location location) {

            if(mOriginalLoc == null){
                mOriginalLoc = new Location(location);
                latLonAlt = new float[3];
            }

            float distance = location.distanceTo(mOriginalLoc);
            float bearing = location.bearingTo(mOriginalLoc);

            mPosition[0] = distance * (float)Math.cos(VectorMath.degreesToRad(bearing));
            mPosition[1] = distance * (float)Math.sin(VectorMath.degreesToRad(bearing));
            mPosition[2] = (float)(location.getAltitude() - mOriginalLoc.getAltitude());

            latLonAlt[0] = (float)location.getLatitude();
            latLonAlt[1] = (float)location.getLongitude();
            latLonAlt[2] = (float)location.getAltitude();

            Toast.makeText(BillboardLandmarksActivity.this, String.format("gps position: [%f, %f, %f]\n", mPosition[0], mPosition[1], mPosition[2]), Toast.LENGTH_LONG).show();
            updateBillboards();
        }


    };

}
