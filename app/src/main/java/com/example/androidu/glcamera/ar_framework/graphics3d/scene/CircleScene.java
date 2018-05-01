package com.example.androidu.glcamera.ar_framework.graphics3d.scene;

import com.example.androidu.glcamera.ar_framework.util.GeoMath;
import com.example.androidu.glcamera.ar_framework.util.VectorMath;

import java.util.ArrayList;

public class CircleScene extends Scene{

//    private float mRadius = 1;
//    private float[] mCenterCoord = {0, 0, 0};
//    private ArrayList<Entity3D_OLD> mCircleTransformList = new ArrayList<>();
//
//
//    public void setRadius(float radius){
//        mRadius = radius;
//    }
//
//    public void setCenterCoordinate(float[] centerCoordinate){
//        VectorMath.copyVec(centerCoordinate, mCenterCoord, 3);
//    }
//
//    @Override
//    public void add(Entity3D_OLD entity){
//        super.add(entity);
//        mCircleTransformList.add(new Entity3D_OLD());
//    }
//
//    public void update(){
//        for(int i = 0; i < mEntityList.size(); i++){
//            /* find bearing from mCenterCoord to e */
//            Entity3D_OLD entity = mEntityList.get(i);
//            float bearing = GeoMath.calcBearing(mCenterCoord, entity.getPosition());
//
//            /* adjust mCircleTransformList[i] so that it places the current entity on the circle
//             * facing the center point, at same altitude as center point, and same bearing as e. */
//            Entity3D_OLD entity2 = mCircleTransformList.get(i);
//            entity2.reset();
//            entity2.translate(0, 0, -mRadius);
//            entity2.rotate(bearing, 0, 1, 0);
//        }
//    }
//
//    @Override
//    public void draw(float[] projectionMatrix, float[] viewMatrix){
//        for(int i = 0; i < mEntityList.size(); i++){
//            Entity3D_OLD entity1 = mEntityList.get(i);
//            Entity3D_OLD entity2 = mCircleTransformList.get(i);
//            entity1.draw(projectionMatrix, viewMatrix, entity2.getModelMatrix());
//        }
//    }

}
