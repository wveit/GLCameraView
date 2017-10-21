package com.example.androidu.glcamera.ar_framework.graphics3d;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class Camera3D {

    float[] mViewMatrix = new float[16];
    float[] mProjectionMatrix = new float[16];
    float[] mVPMatrix = new float[16];
    float[] mTranslation = new float[3];
    float[] mRotation = new float[3];
    float mViewAngleDegrees = 45;

    public Camera3D(){
        setTranslation(0, 0, 0);
        setRotation(0, 0, 0);
    }

    public void viewport(int width, int height){
        GLES20.glViewport(0, 0, width, height);
        float aspectRatio = (float)width / height;
        Matrix.perspectiveM(mProjectionMatrix, 0, mViewAngleDegrees, aspectRatio, 0.01f, 20f);
    }

    public void setTranslation(float x, float y, float z){
        mTranslation[0] = x;
        mTranslation[1] = y;
        mTranslation[2] = z;
    }

    public void translate(float dx, float dy, float dz){
        mTranslation[0] += dx;
        mTranslation[1] += dy;
        mTranslation[2] += dz;
    }

    public void slide(float dx, float dy, float dz){

    }

    public void setRotation(float pitch, float roll, float yaw){
        mRotation[0] = pitch;
        mRotation[1] = roll;
        mRotation[2] = roll;
    }

    public void rotate(float dPitch, float dRoll, float dYaw){
        mRotation[0] += dPitch;
        mRotation[1] += dRoll;
        mRotation[2] += dYaw;
    }

    public float[] getViewMatrix(){
        Matrix.setLookAtM(mViewMatrix, 0,
                mTranslation[0], mTranslation[1], mTranslation[2],
                (float)Math.sin(degreeToRad(mRotation[1])), 0, (float)Math.cos(degreeToRad(mRotation[1])),
                0, 1, 0);

        return mViewMatrix;
    }

    public float degreeToRad(float degree){
        return (float)(degree * 2 * Math.PI / 360);
    }

    public float[] getProjectionMatrix(){
        return mProjectionMatrix;
    }

    public float[] getVPMatrix(){
        float[] viewMatrix = getViewMatrix();
        Matrix.multiplyMM(mVPMatrix, 0, mProjectionMatrix, 0, viewMatrix, 0);
        return mVPMatrix;
    }
}
