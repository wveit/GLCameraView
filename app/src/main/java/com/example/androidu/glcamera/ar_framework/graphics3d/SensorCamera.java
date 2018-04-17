package com.example.androidu.glcamera.ar_framework.graphics3d;


import android.opengl.Matrix;

import com.example.androidu.glcamera.ar_framework.util.GeoMath;
import com.example.androidu.glcamera.ar_framework.util.MyMath;

public class SensorCamera extends Camera3D {

    public enum OrientationMode{PORTRAIT, LANDSCAPE}

    public void setByRotationVectorSensor(float[] rotation, OrientationMode orientationMode){

        float[] matrix = new float[16];
        if(orientationMode == OrientationMode.PORTRAIT){
            portraitMatrixFromRotation(matrix, rotation);
        }
        else{
            landscapeMatrixFromRotation(matrix, rotation);
        }

        setRotationByMatrix(matrix);

    }

    public void setLatLonAlt(float[] latLonAlt){
        float[] xyz = new float[3];
        GeoMath.latLonAltToXYZ(latLonAlt, xyz);
        setPosition(xyz[0], xyz[1], xyz[2]);
    }

    public float[] getLatLonAlt(){return null;}




    private void portraitMatrixFromRotation(float[] matrix, float[] rotation){
        // 'rotation' represents the last 3 terms of a rotation quaternion.
        // Extract angle and axis of rotation from the quaternion.
        float[] rVec = {rotation[0], rotation[1], rotation[2]};
        float magnitude = MyMath.magnitude(rVec);
        rVec[0] /= magnitude;
        rVec[1] /= magnitude;
        rVec[2] /= magnitude;
        float angle = MyMath.radToDegrees(2 * (float)Math.asin(magnitude));


        // Create a rotation matrix from the angle and axis of rotation
        Matrix.setRotateM(matrix, 0, angle, rVec[0], rVec[1], rVec[2]);


        // Correct for the fact that the rotation vector sensor axes are different from phone
        // axes.
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
}
