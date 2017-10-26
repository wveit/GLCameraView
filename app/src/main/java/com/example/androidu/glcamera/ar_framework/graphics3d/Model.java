package com.example.androidu.glcamera.ar_framework.graphics3d;


import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Model {

    FloatBuffer mVertexBuffer = null;
    ShortBuffer mFaceBuffer = null;
    int mShaderProgram = -99;

    public Model(){
        mShaderProgram = loadShaderProgram();
    }

    public void draw(){
        draw(IDENTITY_MATRIX);
    }

    public void draw(float[] MVPMatrix){
//        GLES20.glUseProgram(mShaderProgram);
//
//        int positionAttribute = GLES20.glGetAttribLocation(mShaderProgram, "position");
//        GLES20.glEnableVertexAttribArray(positionAttribute);
//
//        int matrixUniform = GLES20.glGetUniformLocation(mShaderProgram, "matrix");
//        GLES20.glUniformMatrix4v(matrixUniform, 1, false, MVPMatrix, 0);
    }

    public void loadTriangle(){
        loadVerticesAndFaces(TRIANGLE_VERTICES, TRIANGLE_INDICES);
    }

    public void loadSqaure(){
        loadVerticesAndFaces(SQUARE_VERTICES, SQUARE_INDICES);
    }

    public void loadVerticesAndFaces(float[] vertices, short[] faces){
        ByteBuffer b1 = ByteBuffer.allocateDirect(vertices.length * 3 * 4);
        b1.order(ByteOrder.nativeOrder());
        mVertexBuffer = b1.asFloatBuffer();
        mVertexBuffer.put(vertices);

        ByteBuffer b2 = ByteBuffer.allocateDirect(faces.length * 3 * 2);
        b2.order(ByteOrder.nativeOrder());
        mFaceBuffer = b2.asShortBuffer();
        mFaceBuffer.put(faces);
    }

    public void setColor(float red, float green, float blue, float alpha){

    }

    private int loadShaderProgram(){
        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShader, VERTEX_SHADER_CODE);
        GLES20.glCompileShader(vertexShader);

        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(vertexShader, FRAGMENT_SHADER_CODE);
        GLES20.glCompileShader(fragmentShader);

        int program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);

        return program;
    }

    private static final float[] TRIANGLE_VERTICES = {-0.5f, -0.5f, 0f,   0.5f, -0.5f, 0f,   0f, 0.5f, 0f};

    private static final short[] TRIANGLE_INDICES = {0, 1, 2};

    private static final float[] SQUARE_VERTICES = {-0.5f, -0.5f, 0f,   0.5f, -0.5f, 0f,   0.5f, 0.5f, 0f,   0.5f, -0.5f, 0};

    private static final short[] SQUARE_INDICES = {0, 1, 2, 0, 2, 3};

    private static final String VERTEX_SHADER_CODE =
            "attribute vec4 position                \n" +
            "uniform mat4 matrix                    \n" +
            "void main(){                           \n" +
            "    gl_Position = matrix * position;   \n" +
            "}                                      \n" ;

    private static final String FRAGMENT_SHADER_CODE =
            "precision mediump float                \n" +
            "void main(){                           \n" +
            "    gl_FragColor = vec4(1, 0.5, 0, 1); \n" +
            "}                                      \n" ;

    private static final float[] IDENTITY_MATRIX = {1,0,0,0,  0,1,0,0,  0,0,1,0,  0,0,0,1};
}
