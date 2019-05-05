package com.example.filedownloader;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private TextView textView;
    int temp=0;

    void setTextViewStatus() {

        try {

            textView = findViewById(R.id.status);

            temp = FileDownloadService.ServiceRunning;
            Log.i("try","in try "+temp);
            if(temp==1)
                textView.setText("Download in progress!!!");
            else
                textView.setText("Download not started");
            Log.i("try","out of try");
        } catch (Exception e) {
            Log.i("try","in catch");
            temp = 0;
            textView.setText("Download not started");
            Log.i("try","out of catch");
        }
    }


    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };




    private BroadcastReceiver receiver = new BroadcastReceiver() {

    public void onReceive(Context context, Intent intent){
        Bundle bundle = intent.getExtras();
        int notifyId = 1;
        String CHANNEL_ID = "Progress";
        // Only if intent Extras is not empty
        if(bundle!= null){
            // From Bundle, get the Download Status and Download FilePath
            String filepath = bundle.getString(FileDownloadService.FILEPATH);
            int downloadStatus = bundle.getInt(FileDownloadService.RESULT);
            if (downloadStatus == RESULT_OK) {
                Log.i("Broadcast","in broadcast");
                Toast.makeText(context,
                        "Download complete. Download URI: " + filepath,
                        Toast.LENGTH_LONG).show();

                // Set Notification Title

                NotificationManager notificationManager = (NotificationManager) MainActivity.this
                        .getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = "Name";
                    String description = "Description";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name,
                            importance);
                    channel.setDescription(description);
                    // Register the channel with the system; you can't change the importance
                    // or other notification behaviours after this
                    NotificationManager notificationManager1 =
                            getSystemService(NotificationManager.class);
                    notificationManager1.createNotificationChannel(channel);
                }

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "notify_001")
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("File Downloader")
                        .setContentText("Download is complete !!")
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setAutoCancel(true);

                // Open NotificationView Class on Notification Click
                Intent intentNotification = new Intent(MainActivity.this, MainActivity.class);
                // Send data to NotificationView Class

                // Builder to build the notification

                PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this, 0, intentNotification,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                builder.setContentIntent(pIntent);



// notificationId is a unique int for each notification that you must define

                // Build Notification with Notification Manager
                notificationManager.notify(0, builder.build());

                textView.setText("Download done");
            } else {
                Toast.makeText(MainActivity.this, "Download failed",
                        Toast.LENGTH_LONG).show();

                textView.setText("Download failed");
            }

        }
    }
        };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        textView = findViewById(R.id.status);

        setContentView(R.layout.activity_main);
        setTextViewStatus();
    }

    public static void verifyStoragePermissions(Activity activity) {

        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }


   @Override
    protected void onResume() {
        super.onResume();
        // Receive broadcast using registerReceiver method
        registerReceiver(receiver, new IntentFilter(
                FileDownloadService.NOTIFICATION));
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

   /* private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }*/

    public void onClick(View view) {


        // Dynamic Permission needed from Oreo.
        // Checks of required permissions are present. If not, requests consent from User.
        verifyStoragePermissions(this);

        // Intent is created to start 'download service' from this activity.
        Intent intent = new Intent(this, FileDownloadService.class);
        // Passing the required variables
        intent.putExtra(FileDownloadService.FILENAME, "annual_report_2009.pdf");
        intent.putExtra(FileDownloadService.URL,
                "https://nptel.ac.in/syllabus/syllabus_pdf/106106139.pdf");



        startService(intent);

        textView.setText("Download started");
    }
}
