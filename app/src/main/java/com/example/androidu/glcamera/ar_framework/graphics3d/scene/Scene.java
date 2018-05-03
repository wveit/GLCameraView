package com.example.androidu.glcamera.ar_framework.graphics3d.scene;

import com.example.androidu.glcamera.ar_framework.graphics3d.drawable.Drawable;
import com.example.androidu.glcamera.ar_framework.graphics3d.entity.Entity;
import com.example.androidu.glcamera.ar_framework.util.GeoMath;

import java.util.ArrayList;

public class Scene {

    private ArrayList<Entity> mEntityList = new ArrayList<>();

    public void add(Entity entity){
        mEntityList.add(entity);
    }

    public void draw(float[] projectionMatrix, float[] viewMatrix){
        for(Entity e : mEntityList){
            e.draw(projectionMatrix, viewMatrix, e.getModelMatrix());
        }
    }

    public boolean isEmpty(){
        return mEntityList.isEmpty();
    }

    public Entity addDrawable(Drawable drawable){
        Entity entity = new Entity();
        entity.setDrawable(drawable);
        add(entity);
        return entity;
    }

}
