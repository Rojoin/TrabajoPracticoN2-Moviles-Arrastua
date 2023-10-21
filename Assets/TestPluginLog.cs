using System;
using System.Net.Sockets;
using TMPro;
using UnityEngine;


public class TestPluginLog : MonoBehaviour
{
    private const string packageName = "com.rojoin.mylibrary";
    private const string className = packageName + ".Logger";
    [SerializeField] private TextMeshProUGUI textBox;
#if UNITY_ANDROID

    private AndroidJavaClass PluginClass;
    private AndroidJavaObject pluginInstance;
#endif

    private void Start()
    {
        if (Application.platform == RuntimePlatform.Android)
        {
            PluginClass = new AndroidJavaClass(className);
            pluginInstance = PluginClass.CallStatic<AndroidJavaObject>("get_instance");
        }
    }

    public void RunPlugin()
    {
        if (Application.platform == RuntimePlatform.Android)
        {
            string text = "Works";
            textBox.text = text;
            Debug.Log(text);
            pluginInstance.Call("SendLog", text);
        }
    }
}