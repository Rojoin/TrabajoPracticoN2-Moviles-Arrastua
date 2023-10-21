using System;
using System.Net.Sockets;
using TMPro;
using UnityEngine;
using UnityEngine.Android;


public class TestPluginLog : MonoBehaviour
{
    private const string packageName = "com.rojoin.mylibrary";
    private const string className = packageName + ".Logger";
    [SerializeField] private TextMeshProUGUI textBox;
    
#if UNITY_ANDROID
    private AndroidJavaClass PluginClass;
    private AndroidJavaClass unityPlayer;
    private AndroidJavaObject pluginInstance;
    private AndroidJavaObject unityActivity;

    private const string permission = "android.permission.WRITE_EXTERNAL_STORAGE";
    private const int permissionRequestCode = 1;
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

            if (Permission.HasUserAuthorizedPermission(permission))
            {
                Debug.Log("Permission is already granted.");
            }
            else
            {
                // Request permission
                Permission.RequestUserPermission(permission);
            }
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

    void OnPermissionResult(int requestCode, string[] permissions, int[] grantResults)
    {
        if (requestCode == permissionRequestCode)
        {
            if (grantResults.Length > 0 && grantResults[0] == (int)1)
            {
                Debug.Log("Permission granted.");
                // You can now perform actions that require this permission.
            }
            else
            {
                Debug.Log("Permission denied.");
                // Handle denied permission (e.g., show a message to the user).
            }
        }
    }
}