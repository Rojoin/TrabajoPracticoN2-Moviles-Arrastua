package com.rojoin.mylibrary;



import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.checkSelfPermission;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.Manifest;

import androidx.core.content.PermissionChecker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;


public class Logger
{

    private static final String LOGTAG = "RojoinLOG";
    List<String> logList = new ArrayList<String>();

    private static Activity unityActivity;


    public static void initialize(Activity context)
    {
        unityActivity = context;
    }

    private static Logger _instance = null;

    public static Logger get_instance() {
        if (_instance == null)
            _instance = new Logger();
        return _instance;
    }

    public String getLOGTAG(String time) {
        return LOGTAG + time;
    }
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 100;
    public void SendLog(String log)
    {
        logList.add(log);
        Log.d("Unity Log", log);

        SaveLogsToFile();
    }
    public void ClearLogs()
    {
        logList.clear();
    }
    private void SaveLogsToFile() {
        File logFile = new File(Environment.getExternalStorageDirectory(), "unity_logs.txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
            for (String log : logList) {
                writer.write(log);
                writer.newLine();
            }
            writer.close();
            String path = Environment.getExternalStorageDirectory() + "/unity_logs.txt";
            String Msg = "The File has been created in :" + path;
            Log.v("Android", Msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onRequestPermissionResult ( int requestCode, String[] permissions,
        int[] grantResults){
            switch (requestCode) {
                case STORAGE_PERMISSION_REQUEST_CODE: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // Permission was granted, you can proceed with saving logs to storage.
                        SaveLogsToFile();
                    } else {
                        // Permission was denied, handle it as needed (e.g., show a message).
                    }
                    return;
                }
                // Add more cases for other permissions if needed.
            }

        }

    }