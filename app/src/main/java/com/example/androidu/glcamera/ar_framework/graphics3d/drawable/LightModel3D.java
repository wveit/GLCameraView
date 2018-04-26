package com.example.androidu.glcamera.ar_framework.graphics3d.drawable;



import android.opengl.GLES20;

import com.example.androidu.glcamera.ar_framework.graphics3d.helper.BufferHelper;
import com.example.androidu.glcamera.ar_framework.graphics3d.helper.ShaderHelper;
import com.example.androidu.glcamera.ar_framework.util.MatrixMath;

import java.nio.FloatBuffer;


public class LightModel3D extends Drawable {

    private FloatBuffer mVertexBuffer = null;
    private FloatBuffer mNormalBuffer = null;
    private float[] mColor = {0.0f, 0.8f, 0.0f, 1f};
    private float[] mLightVec = {0.0f, 0.0f, -1.0f, 0.0f};

    private int mShaderProgram;
    private int mNumVertices = 0;
    private int mDrawingMode = GLES20.GL_TRIANGLES;

    private static final int FLOATS_PER_VERTEX = 3;


    private static final String vertexShaderCode =
            "attribute vec4 vPosition;                                              " +
            "attribute vec4 vNormal;                                                " +
            "uniform mat4 uMVPMatrix;                                               " +
            "uniform vec4 uLightVec;                                                " +
            "uniform vec4 uColor;                                                   " +
            "varying vec4 vColor;                                                   " +
            "                                                                       " +
            "void main() {                                                          " +
            "  vec4 nor = vec4(vNormal.xyz, 0.0);                                   " +
            "  vec4 light = vec4(uLightVec.xyz, 0.0);                               " +
            "  gl_Position = uMVPMatrix * vPosition;                                " +
            "  nor = normalize(uMVPMatrix * nor);                                   " +
            "  light = normalize(uMVPMatrix * light);                               " +
            "  float dotProduct = -dot(light, nor);                                 " +
            "  if(dotProduct < 0.0)                                                 " +
            "     dotProduct = 0.0;                                                 " +
            "  vColor = vec4((0.3 + 0.7 * dotProduct) * uColor.xyz, 1.0);           " +
            "}                                                                      ";

    private static final String fragmentShaderCode =
            "precision mediump float;           " +
            "varying vec4 vColor;               " +
            "                                   " +
            "void main() {                      " +
            "  gl_FragColor = vColor;           " +
            "}                                  ";




    public LightModel3D(){
        mShaderProgram = ShaderHelper.buildShaderProgram(vertexShaderCode, fragmentShaderCode);
    }


    public void setGLDrawingMode(int glDrawingMode){
        mDrawingMode = glDrawingMode;
    }


    public void draw(){
        draw(MatrixMath.IDENTITY_MATRIX);
    }


    @Override
    public void draw(float[] MVPMatrix){
        if(mVertexBuffer == null)
            return;

        GLES20.glUseProgram(mShaderProgram);

        int positionAttrib = GLES20.glGetAttribLocation(mShaderProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(positionAttrib);
        GLES20.glVertexAttribPointer(positionAttrib, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer);

        int normalAttrib = GLES20.glGetAttribLocation(mShaderProgram, "vNormal");
        GLES20.glEnableVertexAttribArray(normalAttrib);
        GLES20.glVertexAttribPointer(normalAttrib, 3, GLES20.GL_FLOAT, false, 3 * 4, mNormalBuffer);

        int matrixUniform = GLES20.glGetUniformLocation(mShaderProgram, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(matrixUniform, 1, false, MVPMatrix, 0);

        int colorUniform = GLES20.glGetUniformLocation(mShaderProgram, "uColor");
        GLES20.glUniform4fv(colorUniform, 1, mColor, 0);

        int lightVecUniform = GLES20.glGetUniformLocation(mShaderProgram, "uLightVec");
        GLES20.glUniform4fv(lightVecUniform, 1, mLightVec, 0);

        GLES20.glDrawArrays(mDrawingMode, 0, mNumVertices);

        GLES20.glDisableVertexAttribArray(positionAttrib);
        GLES20.glDisableVertexAttribArray(normalAttrib);
    }


    @Override
    public void draw(float[] projectionMatrix, float[] viewMatrix, float[] modelMatrix){
        float[] tempMatrix = new float[16];
        MatrixMath.multiplyMatrices(tempMatrix, projectionMatrix, viewMatrix, modelMatrix);
        draw(tempMatrix);
    }


    public void setColor(float[] rgbaVec){
        if(rgbaVec != null || rgbaVec.length == 4)
            mColor = rgbaVec;
    }


    public void loadVertices(float[] vertexList){
        mNumVertices = vertexList.length / FLOATS_PER_VERTEX;
        mVertexBuffer = BufferHelper.arrayToBuffer(vertexList);
    }

    public void loadNormals(float[] normalList){
        mNormalBuffer = BufferHelper.arrayToBuffer(normalList);
    }


}
