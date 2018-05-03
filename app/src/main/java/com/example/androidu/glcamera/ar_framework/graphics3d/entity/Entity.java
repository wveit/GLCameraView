package com.example.androidu.glcamera.ar_framework.graphics3d.entity;

import android.nfc.Tag;
import android.opengl.Matrix;
import android.util.Log;

import com.example.androidu.glcamera.ar_framework.graphics3d.drawable.Drawable;
import com.example.androidu.glcamera.ar_framework.util.GeoMath;
import com.example.androidu.glcamera.ar_framework.util.MatrixMath;
import com.example.androidu.glcamera.ar_framework.util.VectorMath;


public class Entity extends Drawable{
    private static final String TAG = "waka-Entity";

    private float scaleX = 1, scaleY = 1, scaleZ = 1;
    private float posX, posY, posZ;
    private float yawAngle;
    private float[] modelMatrix = new float[16];
    private Drawable drawable = null;

    private float[] mColor = null;

    private boolean matrixIsClean = false;

    public void reset(){
        scaleX = 1; scaleY = 1; scaleZ = 1;
        posX = 0; posY = 0; posZ = 0;
        yawAngle = 0;
        matrixIsClean = false;
    }

    public void setScale(float scaleX, float scaleY, float scaleZ){
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
        matrixIsClean = false;
    }

    public void setPosition(float x, float y, float z){
        posX = x; posY = y; posZ = z;
        matrixIsClean = false;
    }

    public void move(float dx, float dy, float dz){
        posX += dx; posY += dy; posZ += dz;
        matrixIsClean = false;
    }

    public void slide(float dx, float dy, float dz){
        posX += dx * Math.cos(VectorMath.degreesToRad(yawAngle)) + dz * Math.sin(VectorMath.degreesToRad(yawAngle));
        posY += dy;
        posZ += dz * Math.cos(VectorMath.degreesToRad(yawAngle)) - dx * Math.sin(VectorMath.degreesToRad(yawAngle));
    }

    public void setPositionLatLonAlt(float[] latLonAlt){
        float[] xyz = new float[3];
        GeoMath.latLonAltToXYZ(latLonAlt, xyz);
        setPosition(xyz[0], xyz[1], xyz[2]);
        matrixIsClean = false;
    }


    public void setYaw(float angle){
        yawAngle = angle;
        matrixIsClean = false;
    }

    public void yaw(float dAngle){
        yawAngle += dAngle;
        matrixIsClean = false;
    }

    private void updateModelMatrix(){
        Matrix.translateM(modelMatrix, 0, MatrixMath.IDENTITY_MATRIX, 0, posX, posY, posZ);
        Matrix.rotateM(modelMatrix, 0, yawAngle, 0, 1, 0);
        Matrix.scaleM(modelMatrix, 0, scaleX, scaleY, scaleZ);

        matrixIsClean = true;
    }

    // TODO Entity: add setLookAt(...) method
//    public void setLookAt(float[] eye, float[] look, float[] up){
//        Matrix.setLookAtM(modelMatrix, 0, eye[0], eye[1], eye[2], look[0], look[1], look[2], up[0], up[1], up[2]);
//        Matrix.
//        matrixIsClean = true;
//    }

    public float[] getModelMatrix(){
        if(!matrixIsClean)
            updateModelMatrix();

        return modelMatrix;
    }

    public void setDrawable(Drawable d){
        drawable = d;
    }

    @Override
    public void draw(float[] projection, float[] view, float[] model){
        if(drawable == null)
            return;

        if(mColor != null){
            drawable.drawColor(projection, view, model, mColor);
        }

        drawable.draw(projection, view, model);
    }

    @Override
    public void draw(float[] matrix){
        if(drawable == null)
            return;

        if(mColor != null)
            drawable.drawColor(matrix, mColor);

        drawable.draw(matrix);
    }

    @Override
    public void setColor(float[] color){
        if(color == null || color.length != 4){
            mColor = null;
            return;
        }

        if(mColor == null)
            mColor = new float[4];

        VectorMath.copyVec(color, mColor, 4);
    }
}
