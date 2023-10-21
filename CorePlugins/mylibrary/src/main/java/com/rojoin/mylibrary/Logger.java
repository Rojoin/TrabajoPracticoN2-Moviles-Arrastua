package com.rojoin.mylibrary;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
public class Logger {

    private static final String LOGTAG = "RojoinLOG";
    List<String> logList = new ArrayList<String>();

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
