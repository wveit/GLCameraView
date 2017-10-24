package com.example.androidu.glcamera.ar_framework.util;

import android.util.Log;

public class MyMath {
    private static final String TAG = "wakaMyMath";

    public static float[] crossProduct(float[] a, float[] b){
        float[] c = new float[a.length];

        c[0] = a[1] * b[2] - a[2] * b[1];
        c[1] = a[2] * b[0] - a[0] * b[2];
        c[2] = a[0] * b[1] - a[1] * b[0];

        if(c.length == 4)
            c[3] = a[3];

        return c;
    }

    public static float dotProduct(float[] a, float[] b){
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
    }

    public static float magnitude(float[] vec){
        return (float) Math.sqrt(vec[0] * vec[0] + vec[1] * vec[1] + vec[2] * vec[2]);
    }

    public static float angle(float[] a, float[] b){
        float unitDotProduct = dotProduct(a, b) / magnitude(a) / magnitude(b);
        float angle = (float)Math.acos(unitDotProduct);

        return angle;
    }

    public static float[] normalize(float[] vec){
        float[] temp = new float[3];
        float mag = magnitude(vec);

        temp[0] = vec[0] / mag;
        temp[1] = vec[1] / mag;
        temp[2] = vec[2] / mag;

        return temp;
    }

    public static float radToDegrees(float radians){
        return (float)(radians / 2 / Math.PI * 360);
    }

    public static float degreesToRad(float degrees){
        return (float) (degrees / 360 * 2 * Math.PI);
    }

    public static String vecToString(float[] vec){
        StringBuilder sb = new StringBuilder();
        sb.append("Vec: (");
        sb.append(String.format("% .2f", vec[0]));
        for(int i = 1; i < vec.length; i++){
            sb.append(", ");
            sb.append(String.format("% .2f", vec[i]));
        }
        sb.append(")");
        return sb.toString();

    }

    public static void copyVec(float[] src, float[] dest, int n){
        for(int i = 0; i < n; i++)
            dest[i] = src[i];
    }

    /****************************************************/

    public static float compassBearing(float[] gravityVec, float[] magnetVec, float[] cameraVec){
        // Information we have:
        //  + gravityVec: A vector representing the direction and magnitude of gravity in camera coordinate system
        //  + magnetVec: A vector representing the direction and magnitude of earth's magnetic field from camera coordinate system
        //  + cameraVec: A vector representing the direction of the camera in phone coordinate system

        // Algorithm for finding compass bearing:
        //  * Project magnetVec onto earth's xz plane -> xzMagnet
        //  * Project cameraVec onto earth's xz plane -> xzCamera
        //  * Use dot product to find angle between xzMagnet and xzCamera

        float[] xzTemp = MyMath.crossProduct(magnetVec, gravityVec);
        float[] xzMagnet = MyMath.crossProduct(gravityVec, xzTemp);

        xzTemp = MyMath.crossProduct(cameraVec, gravityVec);
        float[] xzCamera = MyMath.crossProduct(gravityVec, xzTemp);

        float angle = MyMath.angle(xzCamera, xzMagnet);
        angle = MyMath.radToDegrees(angle);

        float[] xproduct = MyMath.crossProduct(xzMagnet, xzCamera);
        float direction = MyMath.dotProduct(xproduct, gravityVec);

        if(direction >=0)
            return angle;
        else
            return 360 - angle;
    }

    public static float rollAngle(float[] gravityVec, float[] phoneUpVec){
        float[] xyGravityVec = {gravityVec[0], gravityVec[1], 0};
        float[] phoneFrontVec = {0, 0, -1};
        float unitDotProduct = dotProduct(phoneUpVec, xyGravityVec) / magnitude(xyGravityVec) / magnitude(phoneUpVec);
        float angle = (float)Math.acos(unitDotProduct);
        angle = radToDegrees(angle);
        float direction = dotProduct(crossProduct(phoneUpVec, xyGravityVec), phoneFrontVec);

        if(direction >= 0){
            return angle;
        }
        else
            return 360 - angle;
    }

    public static float elevationAngle(float[] gravityVec, float[] vector){
        float unitDotProduct = dotProduct(vector, gravityVec) / magnitude(gravityVec) / magnitude(vector);
        float angle = (float)Math.acos(unitDotProduct);
        angle = radToDegrees(angle);
        return angle - 90;
    }

    public static float[] directionVector(float azimuth, float elevation){
        azimuth = MyMath.degreesToRad(azimuth);
        elevation = MyMath.degreesToRad(elevation);

        float y = (float)Math.sin(elevation);
        float groundMagnitude = (float)Math.cos(elevation);
        float x = groundMagnitude * (float)Math.sin(azimuth);
        float z = - groundMagnitude * (float)Math.cos(azimuth);

        float[] directionVector = {x, y, z};

        return directionVector;
    }

    public static float[] phoneVecToWorldVec(float[] gravityVec, float[] magnetVec, float[] phoneVec){
        float azimuth = compassBearing(gravityVec, magnetVec, phoneVec);
        float elevation = elevationAngle(gravityVec, phoneVec);
        float[] directionVector = directionVector(azimuth, elevation);
        return directionVector;
    }
}
