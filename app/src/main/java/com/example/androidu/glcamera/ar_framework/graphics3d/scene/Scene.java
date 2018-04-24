package com.example.androidu.glcamera.ar_framework.graphics3d.scene;

import com.example.androidu.glcamera.ar_framework.graphics3d.entity.Entity3D;

import java.util.ArrayList;

public class Scene {

    protected ArrayList<Entity3D> mEntityList = new ArrayList<>();

    public void add(Entity3D entity){
        mEntityList.add(entity);
    }

    public void draw(float[] projectionMatrix, float[] viewMatrix){
        for(Entity3D e : mEntityList){
            e.draw(projectionMatrix, viewMatrix, e.getModelMatrix());
        }
    }

    public boolean isEmpty(){
        return mEntityList.isEmpty();
    }

}
