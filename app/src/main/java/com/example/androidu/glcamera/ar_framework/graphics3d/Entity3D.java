package com.example.androidu.glcamera.ar_framework.graphics3d;


import android.opengl.Matrix;

import com.example.androidu.glcamera.ar_framework.util.MyMath;

public class Entity3D {
    private Model3D mModel = null;
    private float[] mModelMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private float[] mColor = {0, 1, 0, 1};

    public Entity3D(){
        Matrix.setIdentityM(mModelMatrix, 0);
    }

    public void setModel(Model3D model){
        mModel = model;
    }

    public void setModelMatrix(float[] matrix){
        MyMath.copyVec(matrix, mModelMatrix, 16);
    }

    public float[] getModelMatrix(){
        return mModelMatrix;
    }

    public void setColor(float[] color){
        mColor = color;
    }

    public void draw(float[] VPMatrix){
        Matrix.multiplyMM(mMVPMatrix, 0, VPMatrix, 0, mModelMatrix, 0);
        mModel.setColor(mColor);
        mModel.draw(mMVPMatrix);
    }
}
