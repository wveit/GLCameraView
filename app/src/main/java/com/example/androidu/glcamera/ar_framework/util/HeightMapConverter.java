package com.example.androidu.glcamera.ar_framework.util;

import android.util.Log;

public class HeightMapConverter {
    private static final String TAG = "waka-heightConv";

    public static float[] createMesh(float[][][] coordMap){
        Log.d(TAG, "..... createMesh() .....");
//        for(int i = 0; i < coordMap.length; i++){
//            for(int j = 0; j < coordMap[0].length; j++) {
//                Log.d(TAG, String.format("(%f, %f, %f)\n", coordMap[i][j][0], coordMap[i][j][1], coordMap[i][j][2]));
//            }
//            Log.d(TAG, "  ");
//        }
//        Log.d(TAG, "===============================================================================");
        
        int width = coordMap.length - 1;
        int height = coordMap[0].length - 1;


        float[] tempCoord = new float[3];
        for(int i = 0; i < coordMap.length; i++){
            float[][] coordLine = coordMap[i];
            for(int j = 0; j < coordMap[0].length; j++){
                float[] coord = coordLine[j];
                GeoMath.latLonAltToXYZ(coord, tempCoord);
                VectorMath.copyVec(tempCoord, coord, 3);
            }
        }

        int numSquares = width * height;
        int numTriangles = numSquares * 2;
        int numVertices = numTriangles * 3;
        float[] mesh = new float[numVertices * 3];

        int verticesIndex = 0;
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                mesh[verticesIndex] = coordMap[i][j][0]; verticesIndex++;
                mesh[verticesIndex] = coordMap[i][j][1]; verticesIndex++;
                mesh[verticesIndex] = coordMap[i][j][2]; verticesIndex++;

                mesh[verticesIndex] = coordMap[i+1][j][0]; verticesIndex++;
                mesh[verticesIndex] = coordMap[i+1][j][1]; verticesIndex++;
                mesh[verticesIndex] = coordMap[i+1][j][2]; verticesIndex++;

                mesh[verticesIndex] = coordMap[i+1][j+1][0]; verticesIndex++;
                mesh[verticesIndex] = coordMap[i+1][j+1][1]; verticesIndex++;
                mesh[verticesIndex] = coordMap[i+1][j+1][2]; verticesIndex++;

                mesh[verticesIndex] = coordMap[i][j][0]; verticesIndex++;
                mesh[verticesIndex] = coordMap[i][j][1]; verticesIndex++;
                mesh[verticesIndex] = coordMap[i][j][2]; verticesIndex++;

                mesh[verticesIndex] = coordMap[i+1][j+1][0]; verticesIndex++;
                mesh[verticesIndex] = coordMap[i+1][j+1][1]; verticesIndex++;
                mesh[verticesIndex] = coordMap[i+1][j+1][2]; verticesIndex++;

                mesh[verticesIndex] = coordMap[i][j+1][0]; verticesIndex++;
                mesh[verticesIndex] = coordMap[i][j+1][1]; verticesIndex++;
                mesh[verticesIndex] = coordMap[i][j+1][2]; verticesIndex++;
            }
        }

//        for(int i = 0; i < mesh.length; i+= 9){
//            Log.d(TAG, String.format("(%f, %f, %f)\n", mesh[i+0], mesh[i+1], mesh[i+2]));
//            Log.d(TAG, String.format("(%f, %f, %f)\n", mesh[i+3], mesh[i+4], mesh[i+5]));
//            Log.d(TAG, String.format("(%f, %f, %f)\n", mesh[i+6], mesh[i+7], mesh[i+8]));
//            Log.d(TAG, String.format("  "));
//        }
//        Log.d(TAG, "||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");

        return mesh;
    }



}
