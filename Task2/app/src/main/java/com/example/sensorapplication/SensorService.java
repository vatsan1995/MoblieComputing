package com.example.sensorapplication;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.os.Process;
import android.widget.Toast;

import static android.hardware.SensorManager.SENSOR_DELAY_FASTEST;

public class SensorService extends Service implements SensorEventListener {

    private static final String TAG = SensorService.class.getCanonicalName();

    private SensorManager sensorManager = null;

    private Thread backgroundServiceThread;

    Sensor accelerometerSensor;
    Sensor gyroscopeSensor;
    Sensor lightSensor;

    private static float[] accelValues = {};
    private static float[] gyroValues = {};
    private static float[] lightValue = {};

    private boolean accelSensorListenerRegistered = false;
    private boolean gyroSensorListenerRegistered = false;
    private boolean lightSensorListenerRegistered = false;

    private boolean sensorValueChanged = false;

    public SensorService() {
    }

    private void Sensor_initialize () {

        Log.d(TAG, "Initializing Sensor Services");

        // Get a reference to a SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Accelerometer sensor
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // Gyroscope sensor
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        // Light sensor
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String message = bundle.getString("SensorValue");
            MainActivity.accelXValue.setText("X: " + Float.toString(accelValues[0]));
            MainActivity.accelYValue.setText("Y: " + Float.toString(accelValues[1]));
            MainActivity.accelZValue.setText("Z: " + Float.toString(accelValues[2]));
            MainActivity.gyroXValue.setText("X: " + Float.toString(gyroValues[0]));
            MainActivity.gyroYValue.setText("Y: " + Float.toString(gyroValues[1]));
            MainActivity.gyroZValue.setText("Z: " + Float.toString(gyroValues[2]));
            MainActivity.lightValue.setText("Lux: " + Float.toString(lightValue[0]));
        }
    };

    private void RegisterSensorListeners () {
        // Register accelerometer sensor listener
        if (sensorManager.registerListener(this, accelerometerSensor, SENSOR_DELAY_FASTEST)) {
            Log.d(TAG, "Registered accelerometer sensor listener");
            accelSensorListenerRegistered = true;
        }

        // Register gyroscope sensor listener
        if (sensorManager.registerListener(this, gyroscopeSensor, SENSOR_DELAY_FASTEST)) {
            Log.d(TAG, "Registered gyroscope sensor listener");
            gyroSensorListenerRegistered = true;
        }

        // Register light sensor listener
        if (sensorManager.registerListener(this, lightSensor, SENSOR_DELAY_FASTEST)) {
            Log.d(TAG, "Registered light sensor listener");
            lightSensorListenerRegistered = true;
        }
    }

    private void UnregisterSensorListeners () {
        // Unregister accelerometer sensor listener
        if (accelSensorListenerRegistered) {
            Log.d(TAG, "Unregistered accelerometer sensor listener");
            sensorManager.unregisterListener(this);
        }
        // Unregister gyroscope sensor listener
        if (gyroSensorListenerRegistered) {
            Log.d(TAG, "Unregistered gyroscope sensor listener");
            sensorManager.unregisterListener(this);
        }
        // Unregister light sensor listener
        if (lightSensorListenerRegistered) {
            Log.d(TAG, "Unregistered light sensor listener");
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged (SensorEvent event){
        Sensor sensor = event.sensor;
        sensorValueChanged = true;

        switch (sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                accelValues = event.values;
                break;

            case Sensor.TYPE_GYROSCOPE:
                gyroValues = event.values;
                break;

            case Sensor.TYPE_LIGHT:
                lightValue = event.values;
                break;

            default:
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not in use
    }

    private void startBackgroundThread(Intent intent, final int startId) {
        // Start a new thread for background operations
        backgroundServiceThread = new Thread(new Runnable() {
            @Override
            public void run() {
                /*thread will run until Runtime exits,
                it completes its job, or throws an exception*/
                if (sensorValueChanged) {
                    //  new sensor changed
                    sensorValueChanged = false;
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("SensorValue", "Sensor updated: " + startId);
                    msg.setData(bundle);
                    Log.i(TAG, "Sending message to handler...");
                    handler.sendMessage(msg);
                }
            }
        });

        backgroundServiceThread.setPriority(
                Process.THREAD_PRIORITY_BACKGROUND);
        Log.i(TAG, "Starting thread...");
        backgroundServiceThread.start();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Sensor_initialize ();
        // Register all available sensor listeners
        RegisterSensorListeners();
        startBackgroundThread(intent, startId);
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Register all available sensor listeners
        UnregisterSensorListeners();
      /*  if (backgroundServiceThread != null) {
            Log.i(TAG, "Destroying Thread...");
            Thread dummy = backgroundServiceThread;
            backgroundServiceThread = null;
            dummy.interrupt();
        } */
    }
}
