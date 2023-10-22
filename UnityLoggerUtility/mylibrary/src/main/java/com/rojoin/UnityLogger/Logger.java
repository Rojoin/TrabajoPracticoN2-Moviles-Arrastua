package com.rojoin.UnityLogger;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class Logger
{

    private static final String LOGTAG = "RojoinLOG";
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 10;
    private static final int PERMISSION_REQUEST_CODE = 123;
    List<String> logList = new ArrayList<String>();

    private static Activity unityActivity;


    public static void initialize(Activity context)
    {
        unityActivity = context;
    }
    AlertDialog.Builder builder;
    private static Logger _instance = null;

    public static Logger get_instance() {
        if (_instance == null)
            _instance = new Logger();
        return _instance;
    }

    public String getLOGTAG(String time) {
        return LOGTAG + time;
    }

    public void SendLog(String log)
    {
        logList.add(log);
        Log.d("Unity Log", log);

        SaveLogsToFile();
    }
    public void SendLog(String log,int logType)
    {
        switch(logType)
        {
            case 0:
                Log.v("Unity Log", log);
                break;
            case 1:
                Log.w("Unity Log", log);
                break;
            case 2:
                Log.e("Unity Log", log);
                break;
            case 3:
                Log.d("Unity Log", log);
                break;

        }
        logList.add(log);
        if (ContextCompat.checkSelfPermission(unityActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(unityActivity, new String[]{PERMISSION}, PERMISSION_REQUEST_CODE);
            SaveLogsToFile();
        } else {
            SaveLogsToFile();

        }

    }
    private static final String PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;


    public void CreateAlert()
    {
        builder = new AlertDialog.Builder(unityActivity);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Desea borrar todos los logs?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v(LOGTAG,"Apreto Si");

                logList.clear();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Log.v(LOGTAG,"Apreto No");

            }
        });

    }
    public void ShowAlert()
    {
        AlertDialog  alert = builder.create();
        builder.show();
    }
    private void SaveLogsToFile() {
        Log.v("Android","Llegue aca");
        File logFile = new File(Environment.getExternalStorageDirectory(), "unity_logs.txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
            for (String log : logList) {
                writer.write(log);
                writer.newLine();
            }
            writer.close();
            String Msg = "The File has been created";
            Log.v("Android", Msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted; you can now save logs
                SaveLogsToFile();
            } else {
                // Permission is denied; handle it as needed (e.g., show a message)
            }
        }
    }

}