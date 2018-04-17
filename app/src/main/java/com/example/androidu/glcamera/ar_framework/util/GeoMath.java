package com.example.androidu.glcamera.ar_framework.util;


//    NONE OF THIS IS IMPLEMENTED YET!!!!

public class GeoMath {

    private static float metersPerDegreeLat = 111111;
    private static float metersPerDegreeLon = 111111;
    private static float[] referenceLLA = {34, -117, 0};

    public static void setReference(float[] refLLA){
        referenceLLA = refLLA;
    }

    public static void updateMetersPerDegree(float lat){
        metersPerDegreeLon = (float)(111111 * Math.cos(lat * 2 * Math.PI / 360));
    }

    public static void latLonAltToXYZ(float[] latLonAlt, float[] xyz){
        xyz[0] = latLonAlt[0] - referenceLLA[0] * metersPerDegreeLat;
        xyz[1] = latLonAlt[1] - referenceLLA[1] * metersPerDegreeLon;
        xyz[2] = latLonAlt[2] - referenceLLA[2];
    }

    public static void xyzToLatLonAlt(float[] xyz, float[] latLonAlt){

    }
}
