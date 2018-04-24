package com.example.androidu.glcamera.ar_framework.graphics3d.projection;

import android.opengl.Matrix;

import com.example.androidu.glcamera.ar_framework.util.MatrixMath;
import com.example.androidu.glcamera.ar_framework.util.VectorMath;

public class Projection {

    private final float[] projectionMatrix = new float[16];

    public Projection(){
        VectorMath.copyVec(MatrixMath.IDENTITY_MATRIX, projectionMatrix, 16);
    }

    public void setPerspective(float viewAngle, float aspectRatio, float nearDistance, float farDistance){
        Matrix.perspectiveM(projectionMatrix, 0, viewAngle, aspectRatio, nearDistance, farDistance);
    }

    public float[] getProjectionMatrix(){
        return projectionMatrix;
    }
}
