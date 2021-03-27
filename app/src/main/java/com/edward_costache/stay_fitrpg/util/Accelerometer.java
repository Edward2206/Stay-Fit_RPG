package com.edward_costache.stay_fitrpg.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

public class Accelerometer {

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private Context context;

    public interface Listener
    {
        void onTranslation(float tx, float ty, float tz);
    }
    private Listener listener;
    public void setListener(Listener l)
    {
        listener = l;
    }

    public Accelerometer(Context context, int sensorType)
    {
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensorType);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event)
            {
                if(listener != null)
                {
                    /*
                    This Overwritten method returns 3 different set of values stored in an Array called values. The indexing goes from 0 to 2 and it represents
                    x, y, z respectively. Here i am using the interface of the listener initialized above to pass these values onto a method called onTranslate(),
                    these values can then be fetched and used when creating a Accelerometer object in another class.
                     */
                    listener.onTranslation(event.values[0], event.values[1], event.values[2]);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    public void registerListener()
    {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_UI);
        Toast.makeText(context, "Accelerometer Registered", Toast.LENGTH_SHORT).show();
    }

    public void un_registerListener()
    {
        sensorManager.unregisterListener(sensorEventListener);
        Toast.makeText(context, "Accelerometer Unregistered", Toast.LENGTH_SHORT).show();
    }
}