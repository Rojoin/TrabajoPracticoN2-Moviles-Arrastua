package com.rojoin.UnityLogger;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.Debug;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class Logger {

    private static final String LOGTAG = "RojoinLOG";
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 10;
    public static final int PERMISSION_REQUEST_CODE = 123;
    List<String> logList = new ArrayList<String>();
    private String fileName;
    private static Activity unityActivity;


    public static void initialize(Activity context) {
        unityActivity = context;
    }

    AlertDialog.Builder builder;
    private static Logger _instance = null;

    public void SetFileLocation(String filePath) {
        fileName = filePath;
    }

    public static Logger get_instance() {
        if (_instance == null)
            _instance = new Logger();
        return _instance;
    }

    public String getLOGTAG(String time) {
        return LOGTAG + time;
    }

    public void SendLog(String log) {
        logList.add(log);
        Log.d("Unity Log", log);

        SaveLogsToFile();
    }

    public void SendLog(String log, int logType) {
        String typeOfLog = "Null";
        switch (logType) {
            case 0:
                Log.v("Unity Log", log);
                typeOfLog = "Log:";
                break;
            case 1:
                Log.w("Unity Log", log);
                typeOfLog = "Warning:";
                break;
            case 2:
                Log.e("Unity Log", log);
                typeOfLog = "Error:";
                break;
            case 3:
                Log.d("Unity Log", log);
                typeOfLog = "Exception:";
                break;

        }
        logList.add(typeOfLog+log);
        SaveLogsToFile();

    }

    private static final String PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;


    public void CreateAlert() {
        builder = new AlertDialog.Builder(unityActivity);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Desea borrar todos los logs?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v(LOGTAG, "Apreto Si");

                DeleteLogs();
                logList.clear();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Log.v(LOGTAG, "Apreto No");

            }
        });

    }

    public void ShowAlert() {
        AlertDialog alert = builder.create();
        builder.show();
    }

    private void SaveLogsToFile() {

        Context context = unityActivity.getApplicationContext();
        try {
            DeleteLogs();
            File logFile = new File(context.getExternalFilesDir(null), "Unity_Rojoin_Log" + ".txt");
            FileWriter fileWriter = new FileWriter(logFile, true); // Use 'true' to append to the existing file

            for (String log : logList) {
                fileWriter.append(log).append("\n");
            }
            Log.v("FileWriter", context.getExternalFilesDir(null).toString());
            Toast.makeText(unityActivity.getApplicationContext(), "File created in: " + context.getExternalFilesDir(null).toString(), Toast.LENGTH_SHORT).show();

            fileWriter.close();
        } catch (IOException e) {
            Log.v("FileWriter", "Failed To write");
            Toast.makeText(unityActivity.getApplicationContext(), "Failed To write", Toast.LENGTH_SHORT).show();
        }

    }

    private void DeleteLogs() {

        Context context = unityActivity.getApplicationContext();

        File logFile = new File(context.getExternalFilesDir(null), "Unity_Rojoin_Log.txt");
        if (logFile.exists()) {

            if (logFile.delete()) {
                Log.i("FileDeleted", "File Unity_Rojoin_Log.txt deleted successfully.");
                Toast.makeText(unityActivity.getApplicationContext(), "File Unity_Rojoin_Log.txt deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("FileDeleteError", "Failed to delete file " + fileName + ".txt");
                Toast.makeText(unityActivity.getApplicationContext(), "Failed to delete file File Unity_Rojoin_Log.txt", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private String readFile()
    {
        Context context = unityActivity.getApplicationContext();
        File file = new File(context.getExternalFilesDir(null),"Unity_Rojoin_Log.txt");
        Log.v("FileReader", context.getExternalFilesDir(null).toString());
        byte[] content = new byte[(int)file.length()];
        if (file.exists())
        {
            try
            {
                FileInputStream inputStream =  new FileInputStream(file);
                inputStream.read(content);
                return new String(content);
            }
            catch (IOException e)
            {
                Log.e("login activity", "Can not read file");
                return "Can not read file";
            }
        }
        else
        {
             Log.e("login activity", "File not found ");
                return "File doenst Exist";

        }
    }

}






