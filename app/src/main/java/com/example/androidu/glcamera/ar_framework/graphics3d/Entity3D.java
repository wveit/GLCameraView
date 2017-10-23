package com.example.androidu.glcamera.ar_framework.graphics3d;


import android.opengl.Matrix;

public class Entity3D {
    private Model3D mModel = null;
    private float[] mModelMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    public Entity3D(){
        Matrix.setIdentityM(mModelMatrix, 0);
    }

    public void setModel(Model3D model){
        mModel = model;
    }

    public void setModelMatrix(float[] matrix){
        mModelMatrix = matrix;
    }

    public void draw(float[] VPMatrix){
        Matrix.multiplyMM(mMVPMatrix, 0, mMVPMatrix, 0, mModelMatrix, 0);
        mModel.draw(mMVPMatrix);
    }
}
