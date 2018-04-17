package com.example.androidu.glcamera.wilbert;


import android.hardware.SensorEvent;
import android.location.Location;
import android.opengl.GLES20;
import android.os.Bundle;

import com.example.androidu.glcamera.R;
import com.example.androidu.glcamera.ar_framework.MeshData;
import com.example.androidu.glcamera.ar_framework.MountainData;
import com.example.androidu.glcamera.ar_framework.graphics3d.Camera3D;
import com.example.androidu.glcamera.ar_framework.graphics3d.Entity3D;
import com.example.androidu.glcamera.ar_framework.graphics3d.Model3D;
import com.example.androidu.glcamera.ar_framework.graphics3d.SensorCamera;
import com.example.androidu.glcamera.ar_framework.graphics3d.billboard.BillboardMaker;
import com.example.androidu.glcamera.ar_framework.graphics3d.billboard.SizedBillboard;
import com.example.androidu.glcamera.ar_framework.sensor.MyGps;
import com.example.androidu.glcamera.ar_framework.sensor.MySensor;
import com.example.androidu.glcamera.ar_framework.ui.GLCameraActivity;
import com.example.androidu.glcamera.ar_framework.util.GeoMath;

import java.util.ArrayList;

public class ShapeDrawActivity extends GLCameraActivity{

    private static final String TAG = "waka-shapes";

    MyGps location;
    MySensor orientation;
    MySensor gravity;

    boolean locationSet = false;

    SensorCamera camera;

    ArrayList<float[]> mountainList;
    boolean mountainListLoaded = false;
    boolean mountainModelsLoaded = false;

    Model3D pyramidModel;
    ArrayList<Entity3D> mountainEntityList;

    SizedBillboard billboard;
    Entity3D billboardEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        location = new MyGps(this);
        location.addListener(locationListener);
        orientation = new MySensor(this, MySensor.ROTATION_VECTOR);
        orientation.addListener(orientationListener);
        gravity = new MySensor(this, MySensor.GRAVITY);
        gravity.addListener(gravityListener);

        mountainList = MountainData.mountainList();
        mountainListLoaded = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        location.stop();
        orientation.stop();
        gravity.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        location.start();
        orientation.start();
        gravity.start();
    }

    @Override
    public void GLInit() {
        super.GLInit();

        GLES20.glClearColor(0, 0, 0, 0);
        GLES20.glLineWidth(2);

        camera = new SensorCamera();

        SizedBillboard.init(this);

        mountainModelsLoaded = false;
    }

    @Override
    public void GLResize(int width, int height) {
        super.GLResize(width, height);
        GLES20.glViewport(0, 0, width, height);
        camera.setPerspective(60, (float)width / height, 0.1f, 10000000f);
    }

    @Override
    public void GLDraw() {
        super.GLDraw();

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // update camera
        camera.updateViewMatrix();

        // update entities


        // draw entities
        if(mountainEntityList != null) {
            for (Entity3D entity : mountainEntityList)
                entity.draw(camera.getProjectionMatrix(), camera.getViewMatrix(), entity.getModelMatrix());
        }

        // create entities as needed
        if(mountainListLoaded && !mountainModelsLoaded && locationSet) {
            loadMountainModels();
            loadBillboard();
        }
    }

    
    private void loadBillboard(){
        billboard = BillboardMaker.make(this, 5, R.drawable.ara_icon, "Billboard", "billboard");
        billboardEntity = new Entity3D();
        billboardEntity.setDrawable(billboard);
        billboardEntity.translate(currentXYZ[0], currentXYZ[1], currentXYZ[2]);
    }
    
    
    private void loadMountainModels(){
        mountainEntityList = new ArrayList<>();
        float[] xyz = new float[3];
        pyramidModel = new Model3D();
        pyramidModel.loadVertices(MeshData.pyramid());
        pyramidModel.setGLDrawingMode(GLES20.GL_TRIANGLES);

        for(float[] latLonAlt : mountainList){
            GeoMath.latLonAltToXYZ(latLonAlt, xyz);
            Entity3D entity = new Entity3D();
            entity.setDrawable(pyramidModel);
            entity.scale(xyz[2]);
            entity.translate(xyz[0], xyz[1], xyz[2]);
            mountainEntityList.add(entity);
        }
        mountainModelsLoaded = true;
    }





    float[] currentLatLonAlt = new float[3];
    float[] currentXYZ = new float[3];

    MyGps.Listener locationListener = new MyGps.Listener(){
        @Override
        public void handleLocation(Location location) {
            currentLatLonAlt = new float[3];
            currentLatLonAlt[0] = (float)location.getLatitude();
            currentLatLonAlt[1] = (float)location.getLongitude();
            currentLatLonAlt[2] = (float)location.getAltitude();
            camera.setLatLonAlt(currentLatLonAlt);

            GeoMath.latLonAltToXYZ(currentLatLonAlt, currentXYZ);

            if(!locationSet){
                GeoMath.setReference(currentLatLonAlt);
                GeoMath.updateMetersPerDegree(currentLatLonAlt[0]);
                locationSet = true;
            }
        }
    };

    MySensor.Listener orientationListener = new MySensor.Listener() {
        @Override
        public void onSensorEvent(SensorEvent event) {
            camera.setByRotationVectorSensor(event.values, SensorCamera.OrientationMode.LANDSCAPE);
        }
    };

    MySensor.Listener gravityListener = new MySensor.Listener() {
        @Override
        public void onSensorEvent(SensorEvent event) {


        }
    };
}
