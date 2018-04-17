package com.example.androidu.glcamera.ar_framework;


import java.util.ArrayList;
import java.util.Scanner;

public class MountainData {
    private static final String dataString =
            "34.355417203070324 -118.42109841740915 1045.344 " +
            "34.3616524549061 -118.36479348576853 1026.479 " +
            "34.349748389772415 -118.31604165471384 1284.703 " +
            "34.3389765884318 -118.26522988713572 1462.197 " +
            "34.3389765884318 -118.18283242619822 1521.131 " +
            "34.288500785957176 -118.15536660588572 1521.624 " +
            "34.25218482588137 -118.13476724065134 1370.872 " +
            "34.22986801832696 -118.06082612316618 1634.410 " +
            "34.26789598944624 -118.02718049328337 1511.770 " +
            "34.21567407521524 -117.95920258800993 1358.835 " +
            "34.22475847433369 -117.92212373058805 1260.051 " +
            "34.3104442234328 -117.9468429688693 2092.796 " +
            "34.22759714815525 -117.87886506359587 1086.565 " +
            "34.28264848367323 -117.86238557140837 1499.429 " +
            "34.265626145190375 -117.80264741222868 1397.128 " +
            "34.27016577243641 -117.7765548829318 1655.245 " +
            "34.26165377032788 -117.72436982433805 1507.861 " +
            "34.32065259323389 -118.29085236828337 1004.308 ";

    public static ArrayList<float[]> mountainList(){
        ArrayList<float[]> list = new ArrayList<>();
        Scanner scanner = new Scanner(dataString);
        while(scanner.hasNext()){
            float[] latLonAlt = new float[3];
            String temp = scanner.next();
            latLonAlt[0] = Float.parseFloat(temp);
            temp = scanner.next();
            latLonAlt[1] = Float.parseFloat(temp);
            temp = scanner.next();
            latLonAlt[2] = Float.parseFloat(temp);

            list.add(latLonAlt);
        }

        return list;
    }

}
