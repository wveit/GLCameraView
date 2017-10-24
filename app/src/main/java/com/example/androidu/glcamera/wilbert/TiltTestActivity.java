package com.example.androidu.glcamera.wilbert;

import android.hardware.SensorEvent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.androidu.glcamera.R;
import com.example.androidu.glcamera.ar_framework.sensor.MySensor;
import com.example.androidu.glcamera.ar_framework.util.MyMath;

public class TiltTestActivity extends AppCompatActivity {
    private static final String TAG = "waka_TiltTestActivity";

    private float landscapeRoll, landscapePitch, landscapeYaw;
    private float portraitRoll, portraitPitch, portraitYaw;

    private float[] mMagnetVec = new float[]{0,0,0};
    private float[] mGravityVec = new float[]{0,0,0};
    private float[] mPhoneFrontVec = new float[]{0,0,-1};
    private float[] mPhoneUpVec = new float[]{-1,0,0};

    private int mCompassBearing = 0;
    private float[] lookVector = {0,0,0};
    private float[] upVector = {0,0,0};

    private TextView mTiltReadingsTextView;
    private MySensor mGravitySensor;
    private MySensor mMagnetSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTiltReadingsTextView = new TextView(this);
        setContentView(mTiltReadingsTextView);

        mGravitySensor = new MySensor(this, MySensor.GRAVITY);
        mGravitySensor.addListener(new MySensor.Listener(){
            @Override
            public void onSensorEvent(SensorEvent event) {
                onGravity(event);
            }
        });

        mMagnetSensor = new MySensor(this, MySensor.MAGNETIC_FIELD);
        mMagnetSensor.addListener(new MySensor.Listener(){
            @Override
            public void onSensorEvent(SensorEvent event) {
                onMagnet(event);
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        mGravitySensor.stop();
        mMagnetSensor.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGravitySensor.start();
        mMagnetSensor.start();
    }

    private void onGravity(SensorEvent event){
        mGravityVec[0] = - event.values[0];
        mGravityVec[1] = - event.values[1];
        mGravityVec[2] = - event.values[2];
    }

    private void onMagnet(SensorEvent event){
        MyMath.copyVec(event.values, mMagnetVec);
        updateCalculatedValues();
        updateText();
    }

    private void updateCalculatedValues(){
        mCompassBearing = (int)MyMath.compassBearing(mGravityVec, mMagnetVec, mPhoneFrontVec);
        lookVector = MyMath.phoneVecToWorldVec(mGravityVec, mMagnetVec, mPhoneFrontVec);
        upVector = MyMath.phoneVecToWorldVec(mGravityVec, mMagnetVec, mPhoneUpVec);
    }

    private void updateText(){
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("Gravity Sensor: \n% .2f \n% .2f \n% .2f \n\n", mGravityVec[0], mGravityVec[1], mGravityVec[2]));
        sb.append(String.format("Magnet Sensor: \n%7.2f\n%7.2f\n%7.2f\n\n", mMagnetVec[0], mMagnetVec[1], mMagnetVec[2]));
        sb.append(String.format("Compass Bearing: %d\n\n", mCompassBearing));
        sb.append("lookVector: " + MyMath.vecToString(lookVector) + "\n\n");
        sb.append("upVector: " + MyMath.vecToString(upVector) + "\n\n");

        mTiltReadingsTextView.setText(sb);
    }
}
