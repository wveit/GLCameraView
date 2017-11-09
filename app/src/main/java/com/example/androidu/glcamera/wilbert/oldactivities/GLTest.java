package com.example.androidu.glcamera.wilbert.oldactivities;

import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.Location;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.androidu.glcamera.ar_framework.graphics3d.Camera3D;
import com.example.androidu.glcamera.ar_framework.graphics3d.Model3D;
import com.example.androidu.glcamera.ar_framework.sensor.MyGps;
import com.example.androidu.glcamera.ar_framework.sensor.MySensor;
import com.example.androidu.glcamera.ar_framework.ui.GLCameraActivity;
import com.example.androidu.glcamera.ar_framework.util.MyMath;

public class GLTest extends GLCameraActivity {
    static final String TAG = "waka_GLTest";

    Model3D mModel;
    Camera3D mCamera;

    MySensor mOrientation;
    MyGps mGps;


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

        mModel = new Model3D();
        mModel.loadSquare();

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
        float[] VPMatrix = mCamera.getViewProjectionMatrix();

        drawCompass(VPMatrix);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    //      Drawing Functions
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private void drawCompass(float[] VPMatrix){
        float[] MVPMatrix = new float[16];

        float[] modelMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.scaleM(modelMatrix, 0, modelMatrix, 0, 4, 2, 1);
        Matrix.translateM(modelMatrix, 0, 0, 0, -20);

        float[] rotationMatrix = new float[16];
        Matrix.setRotateM(rotationMatrix, 0, 90, 0, -1, 0);
        float[] color = {1, 0, 0, 1};

        Matrix.multiplyMM(MVPMatrix, 0, VPMatrix, 0, modelMatrix, 0);
        mModel.setColor(color);
        mModel.draw(MVPMatrix);

        Matrix.multiplyMM(modelMatrix, 0, rotationMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(MVPMatrix, 0, VPMatrix, 0, modelMatrix, 0);
        color[0] = 0;
        color[1] = 1;
        mModel.setColor(color);
        mModel.draw(MVPMatrix);

        Matrix.multiplyMM(modelMatrix, 0, rotationMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(MVPMatrix, 0, VPMatrix, 0, modelMatrix, 0);
        color[1] = 0;
        color[2] = 1;
        mModel.setColor(color);
        mModel.draw(MVPMatrix);

        Matrix.multiplyMM(modelMatrix, 0, rotationMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(MVPMatrix, 0, VPMatrix, 0, modelMatrix, 0);
        color[0] = 1;
        mModel.setColor(color);
        mModel.draw(MVPMatrix);


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
//            Log.d(TAG, String.format("[% .2f, % .2f, % .2f]   % .2f\n", rVec[0], rVec[1], rVec[2], angle));


            Matrix.setRotateM(matrix, 0, angle, rVec[0], rVec[1], rVec[2]);
//            Matrix.setRotateM(matrix, 0, angle, rVec[0], -rVec[2], rVec[1]);


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
//            Log.d(TAG, String.format("[% .2f, % .2f, % .2f]   % .2f\n", rVec[0], rVec[1], rVec[2], angle));


            Matrix.setRotateM(matrix, 0, angle, rVec[0], rVec[1], rVec[2]);
//            Matrix.setRotateM(matrix, 0, angle, rVec[0], -rVec[2], rVec[1]);


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

    MyGps.Listener mGPSListener = new MyGps.Listener() {
        @Override
        public void handleLocation(Location location) {

            if(mOriginalLoc == null){
                mOriginalLoc = new Location(location);
            }

            float distance = location.distanceTo(mOriginalLoc);
            float bearing = location.bearingTo(mOriginalLoc);

            mPosition[0] = distance * (float)Math.cos(MyMath.degreesToRad(bearing));
            mPosition[1] = distance * (float)Math.sin(MyMath.degreesToRad(bearing));
            mPosition[2] = (float)(location.getAltitude() - mOriginalLoc.getAltitude());

            Log.d(TAG, String.format("Position: [% .2f, % .2f, % .2f]\n", mPosition[0], mPosition[1], mPosition[2]));
        }
    };
}
