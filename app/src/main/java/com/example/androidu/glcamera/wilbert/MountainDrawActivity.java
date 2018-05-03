    package com.example.androidu.glcamera.wilbert;


    import android.hardware.SensorEvent;
    import android.location.Location;
    import android.opengl.GLES20;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.MotionEvent;
    import android.view.View;

    import com.example.androidu.glcamera.R;
    import com.example.androidu.glcamera.ar_framework.graphics3d.camera.Camera;
    import com.example.androidu.glcamera.ar_framework.graphics3d.drawable.Billboard;
    import com.example.androidu.glcamera.ar_framework.graphics3d.drawable.BillboardMaker;
    import com.example.androidu.glcamera.ar_framework.graphics3d.drawable.LitModel;
    import com.example.androidu.glcamera.ar_framework.graphics3d.entity.Entity;
    import com.example.androidu.glcamera.ar_framework.graphics3d.projection.Projection;
    import com.example.androidu.glcamera.ar_framework.graphics3d.scene.Scene;
    import com.example.androidu.glcamera.ar_framework.sensor.ARGps;
    import com.example.androidu.glcamera.ar_framework.sensor.ARSensor;
    import com.example.androidu.glcamera.ar_framework.ui.ARActivity;
    import com.example.androidu.glcamera.ar_framework.util.GeoMath;
    import com.example.androidu.glcamera.ar_framework.util.HeightMapConverter;
    import com.example.androidu.glcamera.ar_framework.util.MeshUtil;
    import com.example.androidu.glcamera.landmark.MountainData;


    public class MountainDrawActivity extends ARActivity {

        private static final String TAG = "waka-mountain";

        private ARGps location;
        private float[] currentLocation = null;
        private ARSensor orientation;
        private float[] currentOrientation = null;

        private Projection projection;
        private Camera camera;

        private LitModel pyramidModel;
        private Billboard mountainBB, riverBB, wellBB;
        private Entity en1, en2, en3, en4, en5;
        private Scene scene;

        float moveFwd, moveRight, moveUp, turnRight, size = 1;


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

            currentLocation = null;
            currentOrientation = null;

            riverBB = BillboardMaker.make(this, R.drawable.river_icon);
            wellBB = BillboardMaker.make(this, R.drawable.well_icon);
            mountainBB = BillboardMaker.make(this, R.drawable.mountain_icon);

            scene = new Scene();

            projection = new Projection();
            camera = new Camera();

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
                camera.setPositionLatLonAlt(currentLocation);
            }


            /* Set up Entities when the conditions are right */

            /* Update Entities/Scenes */



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
                boolean firstTime = false;

                if(currentLocation == null){
                    currentLocation = new float[3];
                    firstTime = true;
                }

                currentLocation[0] = (float)location.getLatitude();
                currentLocation[1] = (float)location.getLongitude();
                currentLocation[2] = (float)location.getAltitude();

                if(firstTime){
                    GeoMath.setReference(currentLocation);
                }
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



        ////////////////////////////////////////////////////////////////////////////////////////////////
        //
        //      Handling Touch Events
        //
        ////////////////////////////////////////////////////////////////////////////////////////////////

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int screenWidth = v.getWidth();
            int screenHeight = v.getHeight();
            int x = (int)event.getX();
            int y = (int)event.getY();

            if(y < screenHeight / 4){
                if(x < screenWidth / 2){
                    moveFwd -= 1;
                }
                else{
                    moveFwd += 1;
                }
            }
            else if(y < screenHeight / 2){
                if(x < screenWidth / 2){
                    moveRight -= 1;
                }
                else{
                    moveRight += 1;
                }
            }
            else if(y < 3 * screenHeight / 4){
                if(x < screenWidth / 2){
                    moveUp -= 1;
                }
                else{
                    moveUp += 1;
                }
            }
            else {
                if(x < screenWidth / 2){
                    turnRight -= 1;
                }
                else{
                    turnRight += 1;
                }
            }

            return true;
        }



    }
