package com.example.filedownloader;

// Add this service class to Android Manifest

// Service Class to download file from Internet
//The class needs to be modified so that it subclasses the IntentService class.
// When subclassing the IntentService class, there are two rules that must be followed.
// First, a constructor for the class must be implemented which calls the superclass constructor,
// passing through the class name of the service.
// Second, the class must override the onHandleIntent() method.

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class FileDownloadService extends IntentService {

    public static int ServiceRunning = 0;
    private int result = Activity.RESULT_CANCELED;
    public static final String URL = "urlpath";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filepath";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "com.vogella.android.service.receiver";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public FileDownloadService(String name) {
        super(name);
    }

    public FileDownloadService() {
        super("Download Service Started");
    }

    //IntentService is an abstract class. So implement its methods namely  onHandleIntent()

    @Override
    protected void onHandleIntent(Intent intent) {
    // Write code here to implement the functionality for this service

        // @param fileName : Name in which the file has to be stored in Android location
        String urlPath = intent.getStringExtra(URL);
        String fileName = intent.getStringExtra(FILENAME);
        ServiceRunning = 1;

        File output = new File(Environment.getExternalStorageDirectory(), fileName);
        if (output.exists()) {
            output.delete();
        }

        InputStream stream = null;
        FileOutputStream fos = null;


        try {

            /*java.net.URL url = new URL(urlPath);
            HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
            long completeFileSize = httpConnection.getContentLength();

            java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
            java.io.FileOutputStream fos = new FileOutputStream(output.getPath());
            java.io.BufferedOutputStream bout = new BufferedOutputStream(
                    fos, 1024);
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x = -1;
            while ((x = in.read(data, 0, 1024)) < 0) {
                downloadedFileSize += x;

                // calculate progress
                final long currentProgress = (int) ((((double)downloadedFileSize) / ((double)completeFileSize)) * 100000d);
                Log.i("Progress:", "downloadedSize: " + currentProgress + "totalSize:" + completeFileSize + "x value " + x);
                fos.write(data, 0, x);
                //bout.write(data, 0, x);
            }
            //bout.close();
            fos.close();
            in.close();*/
            java.net.URL url = new URL(urlPath);

            long totalSize = url.openConnection().getContentLength();
            Log.i("Info: ","Filling Input Stream");
            stream = url.openConnection().getInputStream();
            InputStreamReader reader = new InputStreamReader(stream);
            fos = new FileOutputStream(output.getPath());

            int next = -1;
            int downloadedSize =0;
            while ((next = reader.read()) != -1) {
                
                downloadedSize = downloadedSize + next;
                Log.i("Progress:", "downloadedSize:" + downloadedSize + "totalSize:" + totalSize);
                fos.write(next);
            }
            // Download and write to file is complete
            result = Activity.RESULT_OK;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ServiceRunning=0;
        }
        publishResults(output.getAbsolutePath(), result);
    }

    private void publishResults(String outputPath, int result) {
        Log.i("PUB","PUB RES");
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(FILEPATH, outputPath);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }

}
