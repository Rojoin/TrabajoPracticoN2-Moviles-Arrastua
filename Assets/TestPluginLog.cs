using System;
using System.Net.Sockets;
using TMPro;
using UnityEngine;
using UnityEngine.Android;


public class TestPluginLog : MonoBehaviour
{
    private const string packageName = "com.rojoin.UnityLogger";
    private const string className = packageName + ".Logger";
    [SerializeField] private TextMeshProUGUI textBox;

#if UNITY_ANDROID
    private AndroidJavaClass PluginClass;
    private AndroidJavaClass unityPlayer;
    private AndroidJavaObject pluginInstance;
    private AndroidJavaObject unityActivity;

    private const string permission = "android.permission.WRITE_EXTERNAL_STORAGE";

#endif

    private void Start()
    {
        if (Application.platform == RuntimePlatform.Android)
        {
            PluginClass = new AndroidJavaClass(className);
            pluginInstance = new AndroidJavaObject(className);

            unityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
            unityActivity = unityPlayer.GetStatic<AndroidJavaObject>("currentActivity");
            pluginInstance.CallStatic("initialize", unityActivity);
            pluginInstance.Call("SetFileLocation", Application.persistentDataPath + "/" + "Test");

            Application.logMessageReceived += SendLogToAndroid;
            if (Permission.HasUserAuthorizedPermission(permission))
            {
                Debug.Log("Permission is already granted.");
            }
            else
            {
                // Request permission
                Permission.RequestUserPermission(permission);
            }

            pluginInstance.Call("CreateAlert");
        }
    }

    private void SendLogToAndroid(string logString, string stackTrace, LogType type)
    {
        switch (type)
        {
            case LogType.Error:
                pluginInstance.Call("SendLog", logString, 2);
                break;
            case LogType.Assert:
                pluginInstance.Call("SendLog", logString);
                break;
            case LogType.Warning:
                pluginInstance.Call("SendLog", logString, 1);
                break;
            case LogType.Log:
                pluginInstance.Call("SendLog", logString, 0);
                break;
            case LogType.Exception:
                pluginInstance.Call("SendLog", logString, 3);
                break;
        }
    }

    public void DeleteLogs()
    {
        if (Application.platform == RuntimePlatform.Android)
        {
            pluginInstance.Call("ShowAlert");
        }
    }

    private void OnDestroy()
    {
        Application.logMessageReceived -= SendLogToAndroid;
    }

    public void RunPlugin()
    {
        if (Application.platform == RuntimePlatform.Android)
        {
            string text = "Works";
            textBox.text = text;
            Debug.Log(text);
        }
    }



}