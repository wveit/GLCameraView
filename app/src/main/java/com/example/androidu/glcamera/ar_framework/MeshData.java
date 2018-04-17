package com.example.androidu.glcamera.ar_framework;

public class MeshData {

    public static float[] pyramid(){
        return new float[]{
                0.5f, 0, 0.5f,
                0.5f, 0, -0.5f,
                -0.5f, 0, -0.5f,

                -0.5f, 0, -0.5f,
                -0.5f, 0, 0.5f,
                0.5f, 0, 0.5f,

                0.5f, 0, 0.5f,
                0, 1f, 0,
                -0.5f, 0, 0.5f,

                0.5f, 0, -0.5f,
                0, 1f, 0,
                0.5f, 0, 0.5f,

                -0.5f, 0, -0.5f,
                0, 1f, 0,
                0.5f, 0, -0.5f,

                -0.5f, 0, 0.5f,
                0, 1f, 0,
                -0.5f, 0, -0.5f,
        };
    }

    public static float[] triangle(){
        float[] vertices = {
                -0.1f, -0.8f, 0f,
                0.1f, -0.8f, 0f,
                0f, 0.5f, 0f
        };
        return vertices;
    }

    public static float[] square(){
        float[] vertices = {
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f,

                -0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f,
                -0.5f, 0.5f, 0f
        };

        return vertices;
    }
}
