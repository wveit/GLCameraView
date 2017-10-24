//  Problems that need to be resolved
//      + How to display a sign with text and an icon on it
//      + How to get the camera class working
//      + How to hook up sensors to the camera class so that the camera moves when the device moves
//      + How to make sure that a graphic will stay in the correct place on the screen

package com.example.androidu.glcamera.wilbert;

import android.opengl.GLES20;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.example.androidu.glcamera.ar_framework.graphics3d.Camera3D;
import com.example.androidu.glcamera.ar_framework.ui.GLCameraActivity;
import com.example.androidu.glcamera.ar_framework.graphics3d.Model3D;


public class Camera3DTestActivity extends GLCameraActivity implements View.OnTouchListener{
    private static final String TAG = "waka_MainActivity";


    private int slideFront = 0, slideBack = 0, slideRight = 0, slideLeft = 0, slideUp = 0, slideDown = 0;
    private int rollRight = 0, rollLeft = 0, yawRight = 0, yawLeft = 0, pitchUp = 0, pitchDown = 0;
    
    
    private Model3D mTriangle;
    private Camera3D mCamera3D;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout layout = getTopFrameLayout();
        layout.setOnTouchListener(this);
    }


    @Override
    public void GLInit() {
        mTriangle = new Model3D();
        mTriangle.loadTriangle();

        mCamera3D = new Camera3D();
        mCamera3D.set(new float[]{0,0,2, 1}, new float[]{0,0,-1, 0}, new float[]{0,1,0, 0});

        GLES20.glClearColor(0, 0, 0, 0);
    }


    @Override
    public void GLResize(int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        mCamera3D.updateViewMatrix();
        mCamera3D.setPerspective(60, (float)width / height, 0.1f, 1000);
    }


    @Override
    public void GLDraw() {

        float angleChange = 1;
        float change = 0.2f;

        float frontChange = (slideFront - slideBack) * change;
        float upChange = (slideUp - slideDown) * change;
        float rightChange = (slideRight - slideLeft) * change;
        float rollChange = (rollRight - rollLeft) * angleChange;
        float yawChange = (yawRight - yawLeft) * angleChange;
        float pitchChange = (pitchUp - pitchDown) * angleChange;

        mCamera3D.slide(rightChange, upChange, frontChange);
        mCamera3D.yaw(yawChange);
        mCamera3D.pitch(pitchChange);
        mCamera3D.roll(rollChange);
        mCamera3D.updateViewMatrix();
        float[] matrix = mCamera3D.getViewProjectionMatrix();

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        mTriangle.draw(matrix);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float screenWidth = v.getWidth();
        float screenHeight = v.getHeight();
        float x = event.getX();
        float y = event.getY();
        
        int trueValue = 1;
        int falseValue = 0;
        
        if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_POINTER_DOWN){
            
            if(x < screenWidth / 6) {
                if(y < screenHeight / 2)
                    slideFront = trueValue;
                else
                    slideBack = trueValue;
            }
            else if(x < screenWidth * 2 / 6) {
                if(y < screenHeight / 2)
                    slideRight = trueValue;
                else
                    slideLeft = trueValue;
            }
            else if(x < screenWidth * 3 / 6) {
                if(y < screenHeight / 2)
                    slideUp = trueValue;
                else
                    slideDown = trueValue;
            }
            else if(x < screenWidth * 4 / 6) {
                if(y < screenHeight / 2)
                    rollRight = trueValue;
                else
                    rollLeft = trueValue;
            }
            else if(x < screenWidth * 5 / 6) {
                if(y < screenHeight / 2)
                    yawRight = trueValue;
                else
                    yawLeft = trueValue;
            }
            else {
                if(y < screenHeight / 2)
                    pitchUp = trueValue;
                else
                    pitchDown = trueValue;
            }
            
            return true;
        }
        else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_POINTER_UP){
            if(x < screenWidth / 6) {
                if(y < screenHeight / 2)
                    slideFront = falseValue;
                else
                    slideBack = falseValue;
            }
            else if(x < screenWidth * 2 / 6) {
                if(y < screenHeight / 2)
                    slideRight = falseValue;
                else
                    slideLeft = falseValue;
            }
            else if(x < screenWidth * 3 / 6) {
                if(y < screenHeight / 2)
                    slideUp = falseValue;
                else
                    slideDown = falseValue;
            }
            else if(x < screenWidth * 4 / 6) {
                if(y < screenHeight / 2)
                    rollRight = falseValue;
                else
                    rollLeft = falseValue;
            }
            else if(x < screenWidth * 5 / 6) {
                if(y < screenHeight / 2)
                    yawRight = falseValue;
                else
                    yawLeft = falseValue;
            }
            else {
                if(y < screenHeight / 2)
                    pitchUp = falseValue;
                else
                    pitchDown = falseValue;
            }
            
            return true;
        }

        return false;
    }
}
