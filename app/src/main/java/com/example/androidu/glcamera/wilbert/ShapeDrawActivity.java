    package com.example.androidu.glcamera.wilbert;


    import android.hardware.SensorEvent;
    import android.location.Location;
    import android.opengl.GLES20;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.MotionEvent;
    import android.view.View;


    import com.example.androidu.glcamera.ar_framework.MeshData;
    import com.example.androidu.glcamera.ar_framework.graphics3d.camera.Camera;
    import com.example.androidu.glcamera.ar_framework.graphics3d.drawable.LitModel;
    import com.example.androidu.glcamera.ar_framework.graphics3d.entity.Entity;
    import com.example.androidu.glcamera.ar_framework.graphics3d.projection.Projection;
    import com.example.androidu.glcamera.ar_framework.graphics3d.scene.Scene;
    import com.example.androidu.glcamera.ar_framework.sensor.ARGps;
    import com.example.androidu.glcamera.ar_framework.sensor.ARSensor;
    import com.example.androidu.glcamera.ar_framework.ui.ARActivity;
    import com.example.androidu.glcamera.ar_framework.util.MatrixMath;


    public class ShapeDrawActivity extends ARActivity {

        private static final String TAG = "waka-shapes";

        private ARGps location;
        private float[] currentLocation = null;
        private ARSensor orientation;
        private float[] currentOrientation = null;

        private Projection projection;
        private Camera camera;

        private LitModel model;
        private Entity entity1, entity2;
        private float angle1 = 0, angle2 = 0;
        private Scene scene;

        float[] mtWilson = {34.224770353682786f, -118.05668717979921f, 1733.442f};
        float[] sanGabrielPeak = {34.24340686131956f, -118.09707311231966f, 1826.785f};
        float[] mtLukens = {34.26899177233548f, -118.23898315429688f, 1547.361f};
        float[] brownMountain = {34.2366701f, -118.14701609999997f, 1357.138f};
        float[] hoytMountain = {34.27196702744981f, -118.17869480013769f, 1152.849f};


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

            projection = new Projection();
            camera = new Camera();
            camera.setPosition(0, 0, 5);
            camera.setOrientation(0, 1, 0, 0);

            model = new LitModel();
            model.loadVertices(MeshData.pyramid());
            model.loadNormals(MeshData.calculateNormals(MeshData.pyramid()));

            float[] scale = {1, 1, 1};
            scene = new Scene();
            entity1 = scene.addDrawable(model, scale, new float[]{1, 1, -1}, 0);
            scale[1] = 3;
            entity2 = scene.addDrawable(model, scale, new float[]{-1, 1, -1}, 0);



            GLES20.glClearColor(0, 0, 0, 0);
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        }

        @Override
        public void GLResize(int width, int height) {
            super.GLResize(width, height);

            projection.setPerspective(60, (float)width / height, 0.01f, 100000000f);
            GLES20.glViewport(0, 0, width, height);
        }

        @Override
        public void GLDraw() {
            super.GLDraw();

            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

            /* Do camera stuff */
            if(currentOrientation != null && currentLocation != null) {
                camera.setOrientationVector(currentOrientation, 0);
//                camera.setPositionLatLonAlt(currentLocation);
            }


            /* Update Entities/Scenes */
            entity1.setRotation(angle1);
            entity2.setRotation(angle2);
            angle1 += 1;
            angle2 += 3;

            /* Draw */
            scene.draw(projection.getProjectionMatrix(), camera.getViewMatrix());
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
