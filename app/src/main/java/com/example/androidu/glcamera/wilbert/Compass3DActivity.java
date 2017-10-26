package com.example.androidu.glcamera.wilbert;

import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.Location;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;

import com.example.androidu.glcamera.ar_framework.graphics3d.Camera3D;
import com.example.androidu.glcamera.ar_framework.graphics3d.Entity3D;
import com.example.androidu.glcamera.ar_framework.graphics3d.Model3D;
import com.example.androidu.glcamera.ar_framework.sensor.MyGps;
import com.example.androidu.glcamera.ar_framework.sensor.MySensor;
import com.example.androidu.glcamera.ar_framework.ui.GLActivity;
import com.example.androidu.glcamera.ar_framework.ui.GLCameraActivity;
import com.example.androidu.glcamera.ar_framework.util.MyMath;
import com.example.androidu.glcamera.ar_framework.util.MyPermission;

public class Compass3DActivity extends GLCameraActivity {
    static final String TAG = "waka_compass3DActivity";

    MySensor mGravitySensor;
    MySensor mMagnetSensor;
    MySensor mOrientationSensor;
    MyGps mGps;

    float[] mStartLocation = null;
    float[] mLocation = {0,0,0};

    Camera3D mCamera3D;
    Model3D mSquareModel;
    Entity3D mNorth;
    Entity3D mSouth;
    Entity3D mEast;
    Entity3D mWest;

    float[] cameraMatrix = new float[16];


    float[] mMagnetVec = {0,1,0};
    float[] mGravityVec = {0,1,0};
    float[] mOrientation = {0,0,0,0};

    float[] mPhoneFrontVec = {0,0,-1};
    float[] mPhoneUpVec = {1,0,0};

    float[] mPosition = {0,0,0};
    float[] mLookVec = {0,0,-1};
    float[] mUpVec = {0,1,0};

    float[] mModelMatrix = new float[16];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraMatrix[0] = 1;
        cameraMatrix[4] = 1;
        cameraMatrix[8] = 1;
        cameraMatrix[12] = 1;

        if(!MyPermission.havePermission(this, MyPermission.PERMISSION_ACCESS_FINE_LOCATION)) {
            MyPermission.requestPermission(this, MyPermission.PERMISSION_ACCESS_FINE_LOCATION);
            Log.d(TAG, "no gps permission");
        }
        else{
            Log.d(TAG, "have gps permission");
        }

        mGravitySensor = new MySensor(this, MySensor.GRAVITY);
        mGravitySensor.addListener(mGravityListener);

        mMagnetSensor = new MySensor(this, MySensor.MAGNETIC_FIELD);
        mMagnetSensor.addListener(mMagnetListener);

        mGps = new MyGps(this);
        mGps.addListener(mGpsListener);

        mOrientationSensor = new MySensor(this, MySensor.ROTATION_VECTOR);
        mOrientationSensor.addListener(mOrientationListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mGravitySensor.stop();
        mMagnetSensor.stop();
        mOrientationSensor.stop();
        mGps.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGravitySensor.start();
        mMagnetSensor.start();
        mOrientationSensor.start();
        mGps.start();
    }

    @Override
    public void GLInit() {
        super.GLInit();

        GLES20.glClearColor(0,0,0,0);

        mCamera3D = new Camera3D();

        mSquareModel = new Model3D();
        mSquareModel.loadSquare();

        mNorth = new Entity3D(); mNorth.setModel(mSquareModel);
        mSouth = new Entity3D(); mSouth.setModel(mSquareModel);
        mEast = new Entity3D(); mEast.setModel(mSquareModel);
        mWest = new Entity3D(); mWest.setModel(mSquareModel);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.scaleM(mModelMatrix, 0, 80, 40, 1);
        Matrix.translateM(mModelMatrix, 0, 0, 0, -2000);

        float[] rotateMatrix = new float[16];
        Matrix.setRotateM(rotateMatrix, 0, 90, 0, -1, 0);
        mNorth.setModelMatrix(mModelMatrix);

        Matrix.multiplyMM(mModelMatrix, 0, rotateMatrix, 0, mModelMatrix, 0);
        mEast.setModelMatrix(mModelMatrix);
        mEast.setColor(new float[]{0, 0, 1, 1});

        Matrix.multiplyMM(mModelMatrix, 0, rotateMatrix, 0, mModelMatrix, 0);
        mSouth.setModelMatrix(mModelMatrix);
        mSouth.setColor(new float[]{0.5f, 0.2f, 0.2f, 1});

        Matrix.multiplyMM(mModelMatrix, 0, rotateMatrix, 0, mModelMatrix, 0);
        mWest.setModelMatrix(mModelMatrix);
        mWest.setColor(new float[]{1, 0, 0, 1});
    }

    @Override
    public void GLResize(int width, int height) {
        super.GLResize(width, height);
        GLES20.glViewport(0, 0, width, height);
        mCamera3D.setPerspective(60, (float)width / height, 0.1f, 10000);
    }

    @Override
    public void GLDraw() {
        super.GLDraw();

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

//        mCamera3D.set(mPosition, mLookVec, mUpVec);
        mCamera3D.setByMatrix(cameraMatrix);
        mCamera3D.updateViewMatrix();

        float[] matrix = mCamera3D.getViewProjectionMatrix();
//        Matrix.multiplyMM(matrix, 0, matrix, 0, mModelMatrix, 0);

        mNorth.draw(matrix);
        mEast.draw(matrix);
        mSouth.draw(matrix);
        mWest.draw(matrix);
//        mSquareModel.draw(matrix);

        Log.d(TAG, String.format("Orientation: [%f, %f, %f, %f]\n", mOrientation[0], mOrientation[1], mOrientation[2], mOrientation[3]));
    }






    MySensor.Listener mMagnetListener = new MySensor.Listener(){
        @Override
        public void onSensorEvent(SensorEvent event) {
            MyMath.copyVec(event.values, mMagnetVec, 3);
        }
    };

    MySensor.Listener mGravityListener = new MySensor.Listener(){
        @Override
        public void onSensorEvent(SensorEvent event) {
            mGravityVec[0] = - event.values[0];
            mGravityVec[1] = - event.values[1];
            mGravityVec[2] = - event.values[2];

            mLookVec = MyMath.phoneVecToWorldVec(mGravityVec, mMagnetVec, mPhoneFrontVec);
            mUpVec = MyMath.phoneVecToWorldVec(mGravityVec, mMagnetVec, mPhoneUpVec);

        }
    };

    MyGps.Listener mGpsListener = new MyGps.Listener(){

        @Override
        public void handleLocation(Location location) {
            if(mStartLocation == null) {
                mStartLocation = new float[3];
                mStartLocation[0] = (float) location.getLatitude();
                mStartLocation[1] = (float) location.getLongitude();
                mStartLocation[2] = (float) location.getAltitude();
            }

            mLocation[0] = (float) location.getLatitude();
            mLocation[1] = (float) location.getLongitude();
            mLocation[2] = (float) location.getAltitude();

            mPosition[0] = (mLocation[1] - mStartLocation[1]) * 100000;
            mPosition[2] = (mStartLocation[0] - mLocation[0]) * 100000;

        }
    };

    MySensor.Listener mOrientationListener = new MySensor.Listener(){

        @Override
        public void onSensorEvent(SensorEvent event) {
            MyMath.copyVec(event.values, mOrientation, 4);
            SensorManager.getRotationMatrixFromVector(cameraMatrix, event.values);
        }
    };

}
