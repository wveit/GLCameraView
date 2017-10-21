

package com.example.androidu.glcamera.ar_framework.ui;


import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;


public class CameraView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "waka_CameraView";

    private Camera mCamera;
    private Context mContext;

    public CameraView(Context context) {
        super(context);
        mContext = context;
        getHolder().addCallback(this);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //      SurfaceHolder Callback Functions
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();

        fixRotation();
        fixSize();

        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }


    private void fixRotation(){
        // Reference: https://www.captechconsulting.com/blogs/android-camera-orientation-made-simple

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(0, info);
        int cameraOrientation = info.orientation;
        boolean isFrontFacing = info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT;
//        Log.d(TAG, "Camera Orientation: " + cameraOrientation);


        int deviceRotationCode = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay()
                .getRotation();


        int deviceRotationAngle = 0;
        switch(deviceRotationCode){
            case Surface.ROTATION_0:    deviceRotationAngle = 0;    break;
            case Surface.ROTATION_90:   deviceRotationAngle = 90;   break;
            case Surface.ROTATION_180:  deviceRotationAngle = 180;  break;
            case Surface.ROTATION_270:  deviceRotationAngle = 270;  break;
        }
//        Log.d(TAG, "Device Rotation Angle: " + deviceRotationAngle);


        int newRotation;
        if(isFrontFacing){
            newRotation = (cameraOrientation + deviceRotationAngle) % 360;
            newRotation = (360 - newRotation) % 360;
        }
        else{
            newRotation = (cameraOrientation - deviceRotationAngle + 360) % 360;
        }
//        Log.d(TAG, "New Rotation: " + newRotation);


        mCamera.setDisplayOrientation(newRotation);
        Camera.Parameters params = mCamera.getParameters();
        params.setRotation(newRotation);
        mCamera.setParameters(params);
    }

    private void fixSize(){

    }


}



//  Issues:
//      + Need to add ability for this view to scale to different sizes (the fixSize() function)
//      + Need to address using various cameras (not just Camera[0])
//      + May need to make onPause and onResume methods to allow open() and release() of camera
//          when the parent Activity pauses and resumes
//      + Need to test if the camera has correct orientation on all devices (especially Nexus5x and
//          tablets.
//      + Should create updated version of this class that uses Camera2 instead of this deprecated
//          Camera class
//      + Think about how this class addresses permissions (right now it just assumes that permission
//          has been obtained already).

