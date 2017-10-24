package com.example.androidu.glcamera.ar_framework.graphics3d;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.example.androidu.glcamera.ar_framework.util.MyMath;

public class Camera3D {
    private static final String TAG = "waka_Camera3D";
    
    private float[] mPosition = new float[]{0, 0, 2, 1};
    private float[] mFrontVec = new float[]{0, 0, -1, 0};
    private float[] mUpVec = new float[]{0, 1, 0, 0};
    private float[] mRightVec = new float[]{1, 0, 0, 0};

    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mViewProjectionMatrix = new float[16];

    private static final float[] tempMatrix = new float[16]; // used for calculations



    public void set(float[] position, float[] frontVec, float[] upVec){

        mPosition = position;
        mFrontVec = frontVec;
        mUpVec = upVec;
        mRightVec = MyMath.crossProduct(mFrontVec, mUpVec);
    }


    public void updateViewMatrix(){

        Matrix.setLookAtM(mViewMatrix, 0,
                mPosition[0], mPosition[1], mPosition[2],
                mPosition[0] + mFrontVec[0], mPosition[1] + mFrontVec[1], mPosition[2] + mFrontVec[2],
                mUpVec[0], mUpVec[1], mUpVec[2]);

        Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
    }


    public void setPerspective(float viewAngle, float aspectRatio, float nearD, float farD){
        Matrix.perspectiveM(mProjectionMatrix, 0, viewAngle, aspectRatio, nearD, farD);
        Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
    }


    public void pitch(float angle){
        Matrix.setRotateM(tempMatrix, 0, angle, mRightVec[0], mRightVec[1], mRightVec[2]);
        Matrix.multiplyMV(mFrontVec, 0, tempMatrix, 0, mFrontVec, 0);
        Matrix.multiplyMV(mUpVec, 0, tempMatrix, 0, mUpVec, 0);
    }


    public void roll(float angle){
        Matrix.setRotateM(tempMatrix, 0, angle, mFrontVec[0], mFrontVec[1], mFrontVec[2]);
        Matrix.multiplyMV(mRightVec, 0, tempMatrix, 0, mRightVec, 0);
        Matrix.multiplyMV(mUpVec, 0, tempMatrix, 0, mUpVec, 0);
    }


    public void yaw(float angle){
        Matrix.setRotateM(tempMatrix, 0, angle, mUpVec[0], mUpVec[1], mUpVec[2]);
        Matrix.multiplyMV(mFrontVec, 0, tempMatrix, 0, mFrontVec, 0);
        Matrix.multiplyMV(mRightVec, 0, tempMatrix, 0, mRightVec, 0);
    }


    public void slide(float dRight, float dUp, float dFront){
        mPosition[0] += dRight * mRightVec[0] + dUp * mUpVec[0] + dFront * mFrontVec[0];
        mPosition[1] += dRight * mRightVec[1] + dUp * mUpVec[1] + dFront * mFrontVec[1];
        mPosition[2] += dRight * mRightVec[2] + dUp * mUpVec[2] + dFront * mFrontVec[2];
//        Log.d(TAG, "position: " + MyMath.vecToString(mFrontVec));
//        Log.d(TAG, "dRight: " + dRight + "   dUp: " + dUp + "   dFront: " + dFront);
    }


    public void move(float dX, float dY, float dZ){
        mPosition[0] += dX;
        mPosition[1] += dY;
        mPosition[2] += dZ;
    }


    public void setPosition(float x, float y, float z){
        mPosition[0] = x;
        mPosition[1] = y;
        mPosition[2] = z;
    }


    public float[] getViewMatrix(){
        return mViewMatrix;
    }


    public float[] getProjectionMatrix(){
        return mProjectionMatrix;
    }


    public float[] getViewProjectionMatrix(){
        return mViewProjectionMatrix;
    }


}
