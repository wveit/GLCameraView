package com.example.androidu.glcamera.ar_framework.graphics3d.scene;


import com.example.androidu.glcamera.ar_framework.graphics3d.entity.Entity;
import com.example.androidu.glcamera.ar_framework.util.GeoMath;
import com.example.androidu.glcamera.ar_framework.util.VectorMath;

public class CircleScene extends Scene{
    // TODO - create CircleScene code

    private float mRadius = 10;
    private float[] mCenter = new float[3];

    public void setRadius(float radius){
        mRadius = radius;
    }

    public void setCenterLatLonAlt(float[] latLonAlt){
        GeoMath.latLonAltToXYZ(latLonAlt, mCenter);
    }

    @Override
    public void draw(float[] projectionMatrix, float[] viewMatrix){

        Entity entity = new Entity();
        float[] north = {0, 0, -1};

        for(Entity e : mEntityList){
            // Copy the entity's matrix and place in new entity
            entity.setModelMatrix(e.getModelMatrix());

            // Draw entity with model matrix from new entity
            float[] position = e.getPosition();
            float[] diffVector = new float[]{position[0] - mCenter[0], 0, position[2] - mCenter[2]};
            VectorMath.normalizeInPlace(diffVector); diffVector[0] *= mRadius; diffVector[2] *= mRadius;
            entity.setPosition(diffVector[0], diffVector[1], diffVector[2]);

            e.draw(projectionMatrix, viewMatrix, entity.getModelMatrix());
        }
    }

}
