


/*
 *  Attention!! Must make Manifest Entries when if you use this class!!
 *
 *  Additional Setup Required: In order to request a particular permission, a <uses-permission>
 *  tag must be added to the Manifest. Here are the particular tags used for the supported
 *  permissions:
 *
 *      <uses-permission android:name="android.permission.CAMERA"/>
 *      <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 *
 *  In addition, you may want to add a <uses-feature> tag to the manifest so that the play-store
 *  can filter; only allowing apps with the particular capability to install your app. i.e. only
 *  devices with a camera can install app if the following tag is in the manifest:
 *
 *      <uses-feature android:name="android.hardware.camera" />
 */

package com.example.androidu.glcamera.ar_framework.util;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;



public class MyPermission {

    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 4324;
    public static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 40280;

    public static boolean havePermission(Context context, String permissionType){
        return ContextCompat.checkSelfPermission(context, permissionType) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(Activity activity, String permissionType){
        int requestCode = 0;
        if(permissionType.equals(PERMISSION_CAMERA))
            requestCode = CAMERA_PERMISSION_REQUEST_CODE;
        else if(permissionType.equals(PERMISSION_WRITE_EXTERNAL_STORAGE))
            requestCode = WRITE_EXTERNAL_STORAGE_REQUEST_CODE;

        ActivityCompat.requestPermissions(activity, new String[] {permissionType}, requestCode);
    }
}
