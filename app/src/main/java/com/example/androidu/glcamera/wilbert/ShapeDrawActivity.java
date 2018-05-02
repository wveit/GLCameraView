    package com.example.androidu.glcamera.wilbert;


    import android.hardware.SensorEvent;
    import android.location.Location;
    import android.opengl.GLES20;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.MotionEvent;
    import android.view.View;


    import com.example.androidu.glcamera.ar_framework.util.MeshUtil;
    import com.example.androidu.glcamera.ar_framework.graphics3d.camera.Camera;
    import com.example.androidu.glcamera.ar_framework.graphics3d.drawable.Drawable;
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

        private LitModel model;
        private Entity entity1, entity2;
        private Scene scene;

        float moveFwd;
        float moveRight;
        float moveUp;
        float turnRight;


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

            projection = new Projection();
            camera = new Camera();
            camera.setPosition(0, 0, 0);

            model = new LitModel();
            model.loadVertices(MeshUtil.pyramid());
            model.loadNormals(MeshUtil.calculateNormals(MeshUtil.pyramid()));



            scene = new Scene();
            entity1 = scene.addDrawable(model, new float[]{1, 1, 1}, new float[]{0, 1, 5}, 0);
            entity1.setColor(new float[]{1, 0, 0, 1});
            entity2 = scene.addDrawable(model, new float[]{1, 3, 1}, new float[]{0, 1, 5}, 0);
            entity2.setColor(new float[]{0, 0, 1, 1});



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
//                camera.setPositionLatLonAlt(currentLocation);
            }

            if(entity1 instanceof Drawable)
                Log.d(TAG, "drawable");
            else
                Log.d(TAG, "not drawable");

            /* Update Entities/Scenes */
            //entity1.yaw(1);

            float speed = 0.1f;
            float angleSpeed = 1;
            float scaleSpeed = 0.1f;
            entity2.slide(moveRight * speed, moveUp * speed, -moveFwd * speed);
            entity2.yaw(turnRight * angleSpeed);
            moveRight = moveUp = moveFwd = turnRight = 0;

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
