# JPL-AR-Framework
This project is an Augmented Reality (AR) Framework, designed to display data from JPL's Watertrek system.
However, the functions are general purpose and can be used for multiple applications. This framework is written
in Java for the Android Platform.

## This project's approach to AR
There are different ways to approach AR. 

One approach is to detect visual features using a device's camera output and computer vision techniques. The AR
system will then keep rendered images lined up with the real world components seen in the camera output.
This is the approach of frameworks such as Vuforia, ARToolkit, ARKit and ARCore. 

Another approach is to assign gps coordinates to the graphical entities, and determine when to move them on the 
screen based on the device's sensors (such as gps, accelerometer, and gyroscope). This is the approach used in our framework.

The sensor approach has the advantages of being able to detect the correct position of an item, even if it is very far away,
or if the view is obstructed. On the other hand, it suffers from innacuracies of the sensors. Since GPS on most devices 
does not have pinpoint accuracy, and there is an error associated with basing position information on accelerometer alone,
it is difficult to get positioning exactly right. 

We found that the approach we chose was better suited to our intended application, since it can be used for placing items
over a very large area (Geological features such as mountains, rivers, wells, etc). Eventually, we would like to employ 
filtering and computer vision techniques in order to improve the accuracy of object placement.

## Key Features
The features supplied by this framework are as follows:
* Provide a Window/Drawing surface that displays camera preview, with rendered objects on top.
* Provide easy access to gps, accelerometer, gyroscope and other sensors.
* Provide classes that simplify rendering:
  - Camera
  - Projection
  - Entity
  - Simple Model loader / displayer
  - Scene 
* Provide math functions that allow:
  - Converting between Latitude/Longitude/Altitude and XYZ coordinates
  - Calculate distance, compass bearing and elevation between two locations
  - Vector and matrix math
  - Calculate pitch, roll, yaw of device
  - Convert Quaternion to rotation matrix

## Big Picture Components
In general, I would split this project into the following components
* Rendering
* Sensor
* Math
* Utility

## Future Features
<description will be added shortly>

## Current Status of Project
<description will be added shortly>

## Quick description of the classes
<description will be added shortly>

## Sample code
<description will be added shortly>

## Downloading, Building, and Running
This project is written in Java for Android devices (particularly phones) using Android Studio. In order to download, build
and run this project, do the following:
1) Download and install Android studio (if not already installed)
2) Download this project (either clone or download zip file and extract)
3) In Android Studio, go to File -> Open. In the file chooser, navigate to and select the top level directory of the 
  downloaded project. This will cause the project to open in Android Studio. 
4) Click the "run app" button in Android Studio. This is the green triangle at the top. This compiles and runs the project. 
   Since there are some sample Android Activities included in the project that use the framework, this will cause the app to
   be launched on the attached device/emulator.
   
