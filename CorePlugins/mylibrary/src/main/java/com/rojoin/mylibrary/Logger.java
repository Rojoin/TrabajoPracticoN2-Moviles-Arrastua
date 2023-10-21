package com.rojoin.mylibrary;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
}
