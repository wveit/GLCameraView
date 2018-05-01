package com.example.androidu.glcamera.ar_framework.util;

public class HeightMapConverter {
    private float unitWidth = 1, unitHeight = 1;
    private int width, height;
    private float[] mesh = new float[0];
    private float[][] heightMap = null;
    private int verticesIndex = 0;

    public void setUnitWidthAndHeight(float w, float h){
        unitWidth = w;
        unitHeight = h;
    }

    public void createMesh(float[][] heightMap){
        this.heightMap = heightMap;

        width = heightMap.length - 1;
        height = heightMap[0].length - 1;

        int numSquares = width * height;
        int numTriangles = numSquares * 2;
        int numVertices = numTriangles * 3;
        mesh = new float[numVertices * 3];

        verticesIndex = 0;
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                addVerticesForSquare(i, j);
            }
        }
    }

    private void addVerticesForSquare(int x, int y){
        mesh[verticesIndex] = x * unitWidth; verticesIndex++;
        mesh[verticesIndex] = heightMap[x][y]; verticesIndex++;
        mesh[verticesIndex] = y * unitHeight; verticesIndex++;

        mesh[verticesIndex] = (x + 1) * unitWidth; verticesIndex++;
        mesh[verticesIndex] = heightMap[x + 1][y]; verticesIndex++;
        mesh[verticesIndex] = y * unitHeight; verticesIndex++;

        mesh[verticesIndex] = x * unitWidth; verticesIndex++;
        mesh[verticesIndex] = heightMap[x][y + 1]; verticesIndex++;
        mesh[verticesIndex] = (y + 1) * unitHeight; verticesIndex++;


        mesh[verticesIndex] = (x + 1) * unitWidth; verticesIndex++;
        mesh[verticesIndex] = heightMap[x + 1][y]; verticesIndex++;
        mesh[verticesIndex] = y * unitHeight; verticesIndex++;

        mesh[verticesIndex] = (x + 1) * unitWidth; verticesIndex++;
        mesh[verticesIndex] = heightMap[x + 1][y + 1]; verticesIndex++;
        mesh[verticesIndex] = (y + 1) * unitHeight; verticesIndex++;

        mesh[verticesIndex] = x * unitWidth; verticesIndex++;
        mesh[verticesIndex] = heightMap[x][y + 1]; verticesIndex++;
        mesh[verticesIndex] = (y + 1) * unitHeight; verticesIndex++;
    }

    public float[] getMesh(){
        return mesh;
    }


    public static void main(String[] args){
        float[][] heightMap = new float[3][3];
        heightMap[0][0] = 3;
        heightMap[0][1] = 4;
        heightMap[0][2] = 3;
        heightMap[1][0] = 5;
        heightMap[1][1] = 2;
        heightMap[1][2] = 1;
        heightMap[2][0] = 4;
        heightMap[2][1] = 1;
        heightMap[2][2] = 4;

        HeightMapConverter converter = new HeightMapConverter();
        converter.setUnitWidthAndHeight(2, 2);
        converter.createMesh(heightMap);
        float[] mesh = converter.getMesh();
        for(int i = 0; i < mesh.length / 3; i++){
            System.out.println("(" + mesh[i * 3 + 0] + ", " + mesh[i * 3 + 1] + ", " + mesh[i * 3 + 2] + ")");
        }
    }

}
