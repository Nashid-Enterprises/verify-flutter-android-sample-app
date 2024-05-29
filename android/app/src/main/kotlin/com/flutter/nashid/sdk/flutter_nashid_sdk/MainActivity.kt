package com.flutter.nashid.sdk.flutter_nashid_sdk

import android.util.Log
import com.kyc.nashidmrz.NashidSDK
import com.kyc.nashidmrz.SDKIntListener
import com.kyc.nashidmrz.mrtd2.resultcallback.ResultListener
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject

class MainActivity : FlutterActivity() {
    private val CHANNEL = "samples.flutter.dev/mychannel"
    private val ID_METHOD = "idcard"
    private val PASSPORT_METHOD = "passport"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL
        ).setMethodCallHandler { call, result ->
            Log.d("TAG", "configureFlutterEngine: " + call.method)
            if (call.method == ID_METHOD) {
                callSDK(result,"idcard")

            } else if (call.method == PASSPORT_METHOD) {
                callSDK(result,"passport")
            } else {
                result.notImplemented()
            }
        }

    }

    fun callSDK(result: MethodChannel.Result, s: String) {
//        nashidSDK.documentScan(activity)
//    val intent = Intent(this, SelectDocumentActivity::class.java)
//    startActivity(intent)
        val sdkInstance = NashidSDK.getInstance()
        val sdkIntListener = object : SDKIntListener {

            override fun onInitFail() {
                Log.d("MainActivity", "onInitFail: ")
            }

            override fun onInitSuccess() {
                Log.d("MainActivity", "onInitSuccess: ")
//            You can call this method anywhere you like. For instance,
//            if you wish to invoke it on a button click,
//            you need to include this line within the button's click listener.
//            However, it's crucial to ensure that the NashidSDK has been successfully initialized before calling this method.
//            Otherwise, the method won't function as intended.
                if (s.equals("passport")){
                    NashidSDK.getInstance().passportScan(this@MainActivity)
                }else {
                    NashidSDK.getInstance().documentScan(this@MainActivity)
                }
            }

        }
        sdkInstance.init(
            "MIO7NIJsfkJsE8HJJOB1Ff3xpysU7k1HE2NhslTCKA2qaIfIS9",
            "MD7ECJ0K48AJP6S",
            "https://dashboard.test.projectnid.com/api/",
            "sotopo5208@ikumaru.com",
            sdkIntListener,
        )

        val resultListener = object : ResultListener {

            override fun onResultData(jsonObject: JSONObject?, scannedDocType: String?) {
                Log.d("MainActivity", "onResultData: " + jsonObject)
                result.success(jsonObject.toString())

            }

            override fun onFailure() {
                Log.d("MainActivity", "onFailure: ")

            }
        }

//// Check if resultListener is not null
        sdkInstance.setResultListener(resultListener)

    }
}

