package com.example.androidu.glcamera.ar_framework.graphics3d;


import android.opengl.Matrix;

import com.example.androidu.glcamera.ar_framework.util.MyMath;

public class Entity3D {

    private Drawable mDrawable = null;
    private float[] mModelMatrix = new float[16];

    public Entity3D(){
        Matrix.setIdentityM(mModelMatrix, 0);
    }

    public void setIdentity(){
        Matrix.setIdentityM(mModelMatrix, 0);
    }

    public void scale(float scale){
        Matrix.scaleM(mModelMatrix, 0, scale, scale, scale);
    }

    public void translate(float dx, float dy, float dz){
//        Matrix.translateM(mModelMatrix, 0, dx, dy, dz);
        float[] translateMatrix = new float[16];
        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, dx, dy, dz);
        Matrix.multiplyMM(mModelMatrix, 0, translateMatrix, 0, mModelMatrix, 0);
    }

    public void rotate(float angle, float x, float y, float z){
//        Matrix.rotateM(mModelMatrix, 0, angle, x, y, z);
        float[] rotationMatrix = new float[16];
        Matrix.setIdentityM(rotationMatrix, 0);
        Matrix.rotateM(rotationMatrix, 0, angle, x, y, z);
        Matrix.multiplyMM(mModelMatrix, 0, rotationMatrix, 0, mModelMatrix, 0);
    }

    /* implement these 5 functions eventually */
    public void set(float[] position, float[] frontVec, float[] upVec){}
    public void slide(float[] xyz){}
    public void pitch(float angle){}
    public void roll(float angle){}
    public void yaw(float angle){}


    public float[] getModelMatrix(){return mModelMatrix;}

    public void setDrawable(Drawable drawable){
        mDrawable = drawable;
    }

    public void draw(float[] matrix){
        if(mDrawable == null)
            return;

        mDrawable.draw(matrix);
    }

    public void draw(float[] projectionMatrix, float[] viewMatrix, float[] modelMatrix){
        if(mDrawable == null)
            return;

        mDrawable.draw(projectionMatrix, viewMatrix, getModelMatrix());
    }
}
