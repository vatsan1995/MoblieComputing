package com.example.sensorapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // log activity
    private static final String TAG = "MainActivity";

    public static int timerControl = 0;

    // Accelerometer X, Y, and Z values
    public static TextView accelXValue, accelYValue, accelZValue;
    // Gyroscope X, Y, and Z values
    public static TextView gyroXValue, gyroYValue, gyroZValue;
    // Light value
    public static TextView lightValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SetupUIViews();
        Button startBackService = (Button) findViewById(R.id.btn_strt);
        startBackService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerControl=1;
                Log.i(TAG, "Start button is clicked");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (timerControl==1) {
                            Intent startServiceIntent = new Intent(MainActivity.this, SensorService.class);
                            startService(startServiceIntent);
                            new Handler().postDelayed(this, 1000);
                            // Stop android service.
                            Log.i(TAG, "Destroying service");
                            Intent stopServiceIntent = new Intent(MainActivity.this, SensorService.class);
                            stopService(stopServiceIntent);
                        }
                    }
                }, 1000);

            }
        });

        Button stopBackService = (Button) findViewById(R.id.btn_stp);
        stopBackService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                timerControl=0;
                // Stop android service.
                Log.i(TAG, "Stopping service");
                Intent stopServiceIntent = new Intent(MainActivity.this, SensorService.class);
                stopService(stopServiceIntent);
            }

        });
    }

    private void SetupUIViews() {
        // Capture accelerometer related view elements
        accelXValue = (TextView) findViewById(R.id.accel_x_value);
        accelYValue = (TextView) findViewById(R.id.accel_y_value);
        accelZValue = (TextView) findViewById(R.id.accel_z_value);

        // Capture gyroscope related view elements
        gyroXValue = (TextView) findViewById(R.id.gyro_x_value);
        gyroYValue = (TextView) findViewById(R.id.gyro_y_value);
        gyroZValue = (TextView) findViewById(R.id.gyro_z_value);

        // Capture light related view elements
        lightValue = (TextView) findViewById(R.id.light_value);
    }
}
