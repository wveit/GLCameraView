package com.example.androidu.glcamera.wilbert;


import android.hardware.SensorEvent;
import android.location.Location;
import android.opengl.GLES20;
import android.os.Bundle;


import com.example.androidu.glcamera.ar_framework.MeshData;
import com.example.androidu.glcamera.ar_framework.graphics3d.camera.SensorCamera3D;
import com.example.androidu.glcamera.ar_framework.graphics3d.entity.Entity3D;
import com.example.androidu.glcamera.ar_framework.graphics3d.drawable.Model3D;
import com.example.androidu.glcamera.ar_framework.graphics3d.projection.Projection;
import com.example.androidu.glcamera.ar_framework.sensor.ARGps;
import com.example.androidu.glcamera.ar_framework.sensor.ARSensor;
import com.example.androidu.glcamera.ar_framework.ui.ARActivity;


public class ShapeDrawActivity extends ARActivity {

    private static final String TAG = "waka-shapes";

    private ARGps location;
    private float[] currentLocation = null;
    private ARSensor orientation;
    private float[] currentOrientation = null;

    private SensorCamera3D camera;
    private Projection projection;
    private Entity3D pyramidEntity;
    private Model3D pyramidModel;
    private float rotationAngle = 0;
    private float distance = 0;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //      Activity Callbacks
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        location = new ARGps(this);
        location.addListener(locationListener);
        orientation = new ARSensor(this, ARSensor.ROTATION_VECTOR);
        orientation.addListener(orientationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        location.stop();
        orientation.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        location.start();
        orientation.start();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //      OpenGL Callbacks
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void GLInit() {
        super.GLInit();

        camera = new SensorCamera3D();
        projection = new Projection();

        pyramidModel = new Model3D();
        pyramidModel.loadVertices(MeshData.pyramid());
        //pyramidModel.setGLDrawingMode(GLES20.GL_LINE_STRIP);

        pyramidEntity = new Entity3D();
        pyramidEntity.setDrawable(pyramidModel);

        GLES20.glClearColor(0, 0, 0, 0);
    }

    @Override
    public void GLResize(int width, int height) {
        super.GLResize(width, height);

        GLES20.glViewport(0, 0, width, height);
        projection.setPerspective(60, (float)width/height, 0.01f, 1000f);
    }

    @Override
    public void GLDraw() {
        super.GLDraw();

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        /* move camera as appropriate and update camera */
        if(currentOrientation != null)

            camera.setByRotationVectorSensor(currentOrientation, SensorCamera3D.OrientationMode.LANDSCAPE);
        camera.updateViewMatrix();

        /* move entity as appropriate */


        /* draw the entity */
        pyramidEntity.draw(
                projection.getProjectionMatrix(),
                camera.getViewMatrix(),
                pyramidEntity.getModelMatrix()
        );
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //      Sensor Callbacks
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private ARGps.Listener locationListener = new ARGps.Listener(){
        @Override
        public void handleLocation(Location location){
            if(currentLocation == null){
                currentLocation = new float[3];
            }

            currentLocation[0] = (float)location.getLatitude();
            currentLocation[1] = (float)location.getLongitude();
            currentLocation[2] = (float)location.getAltitude();
        }
    };

    private ARSensor.Listener orientationListener = new ARSensor.Listener(){
        @Override
        public void onSensorEvent(SensorEvent event){
            if(currentOrientation == null){
                currentOrientation = new float[3];
            }

            currentOrientation[0] = event.values[0];
            currentOrientation[1] = event.values[1];
            currentOrientation[2] = event.values[2];
        }
    };
}
