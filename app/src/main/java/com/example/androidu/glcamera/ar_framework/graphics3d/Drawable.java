package com.example.androidu.glcamera.ar_framework.graphics3d;

import com.example.androidu.glcamera.ar_framework.util.MatrixMath;

public class Drawable {

    public void draw(float[] projectionMatrix, float[] viewMatrix, float[] modelMatrix){
        float[] matrix = new float[16];
        MatrixMath.multiplyMatrices(matrix, projectionMatrix, viewMatrix, modelMatrix);
        draw(matrix);
    }

    public void draw(float[] matrix){

    }
}
