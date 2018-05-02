package com.example.androidu.glcamera.ar_framework.graphics3d.scene;

import com.example.androidu.glcamera.ar_framework.graphics3d.drawable.Drawable;
import com.example.androidu.glcamera.ar_framework.graphics3d.entity.Entity;
import com.example.androidu.glcamera.ar_framework.util.GeoMath;

import java.util.ArrayList;

public class Scene {

    protected ArrayList<Entity> mEntityList = new ArrayList<>();

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


    public Entity addDrawable(Drawable drawable, float[] scale, float[] xyz, float angle){
        Entity entity = new Entity();
        entity.setDrawable(drawable);
        entity.setScale(scale[0], scale[1], scale[2]);
        entity.setPosition(xyz[0], xyz[1], xyz[2]);
        entity.setYaw(angle);
        add(entity);
        return entity;
    }

    public void addMountain(Drawable drawable, float[] latLonAlt){
        Entity entity = new Entity();
        entity.setDrawable(drawable);
        float[] xyz = new float[3];
        GeoMath.latLonAltToXYZ(latLonAlt, xyz);
        entity.setScale(xyz[1] / 2, xyz[1], xyz[1] / 2);
        entity.setPosition(xyz[0], 0, xyz[2]);
        add(entity);
    }

}
