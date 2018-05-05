    package com.example.androidu.glcamera.wilbert;


    import android.hardware.SensorEvent;
    import android.location.Location;
    import android.opengl.GLES20;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.MotionEvent;
    import android.view.View;


    import com.example.androidu.glcamera.R;
    import com.example.androidu.glcamera.ar_framework.graphics3d.drawable.Billboard;
    import com.example.androidu.glcamera.ar_framework.graphics3d.drawable.BillboardMaker;
    import com.example.androidu.glcamera.ar_framework.graphics3d.drawable.Model;
    import com.example.androidu.glcamera.ar_framework.util.MeshUtil;
    import com.example.androidu.glcamera.ar_framework.graphics3d.camera.Camera;
    import com.example.androidu.glcamera.ar_framework.graphics3d.drawable.LitModel;
    import com.example.androidu.glcamera.ar_framework.graphics3d.entity.Entity;
    import com.example.androidu.glcamera.ar_framework.graphics3d.projection.Projection;
    import com.example.androidu.glcamera.ar_framework.graphics3d.scene.Scene;
    import com.example.androidu.glcamera.ar_framework.sensor.ARGps;
    import com.example.androidu.glcamera.ar_framework.sensor.ARSensor;
    import com.example.androidu.glcamera.ar_framework.ui.ARActivity;


    public class ShapeDrawActivity extends ARActivity {

        private static final String TAG = "waka-shapes";

        private ARGps location;
        private float[] currentLocation = null;
        private ARSensor orientation;
        private float[] currentOrientation = null;

        private Projection projection;
        private Camera camera;

        private LitModel cubeModel, pyramidModel;
        private Entity entity1, entity2, entity3, entity4, entity5, entity6, entity7, entity8;
        private Scene scene;



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

            Log.d(TAG, "....... init .........");
            cubeModel = pyramidModel = null;
            entity1 = entity2 = entity3 = entity4 = entity5 = entity6 = entity7 = entity8 = null;
            scene = null;

            projection = new Projection();
            camera = new Camera();
            camera.setPosition(0, 0, 0);

            cubeModel = new LitModel();
            cubeModel.loadVertices(MeshUtil.cube());
            cubeModel.loadNormals(MeshUtil.calculateNormals(MeshUtil.cube()));

            pyramidModel = new LitModel();
            pyramidModel.loadVertices(MeshUtil.pyramid());
            pyramidModel.loadNormals(MeshUtil.calculateNormals(MeshUtil.pyramid()));

            Billboard wellBB = BillboardMaker.make(this, R.drawable.well_icon);
            Billboard mountainBB = BillboardMaker.make(this, R.drawable.ara_icon);
            Billboard signBB = BillboardMaker.make(this, R.drawable.well_icon, "Sample Title", "This is Sample Text");

            Model pyramidWireframe = new Model();
            pyramidWireframe.loadVertices(MeshUtil.pyramid());
            pyramidWireframe.setGLDrawingMode(GLES20.GL_LINE_STRIP);


            scene = new Scene();

            entity1 = scene.addDrawable(cubeModel);
            entity1.setPosition(-2, 3, -10);
            entity1.setColor(new float[]{1, 0, 0, 1});

            entity2 = scene.addDrawable(cubeModel);
            entity2.setPosition(0, 0, -10);
            entity2.setScale(1, 3, 1);
            entity2.setColor(new float[]{0, 0, 1, 1});

            entity3 = scene.addDrawable(cubeModel);
            entity3.setPosition(2, -3, -10);
            entity3.setScale(1, 3, 1);
            entity3.setColor(new float[]{0, 1, 0, 1});

            entity4 = scene.addDrawable(pyramidModel);
            entity4.setPosition(-4, 0, -10);
            entity3.setColor(new float[]{0.7f, 0.3f, 0f});

            entity5 = scene.addDrawable(wellBB); entity5.setPosition(-5, -1, -10);
            entity6 = scene.addDrawable(mountainBB); entity6.setPosition(-5, 3, -10);
            entity7 = scene.addDrawable(signBB); entity7.setPosition(-9, 0, -10); entity7.setScale(4, 2, 2);


            entity8 = scene.addDrawable(pyramidWireframe);
            entity8.setPosition(2, 0, -5);

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

//            Log.d(TAG, "..... draw .....");
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

            /* Do camera stuff */
            if(currentOrientation != null && currentLocation != null) {
                camera.setOrientationVector(currentOrientation, 0);
//                camera.setLatLonAlt(currentLocation);
            }


            /* Update Entities/Scenes */
            entity1.yaw(1);
            entity2.yaw(2);
            entity3.yaw(3);
            entity4.yaw(-1);
            entity5.yaw(-1);
            entity7.yaw(1);
            entity8.yaw(-2);

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
