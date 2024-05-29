import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  static const MethodChannel methodChannel =
  MethodChannel('samples.flutter.dev/mychannel');
  static const String methodName = "idcard";
  static const String methodName1 = "passport";

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      // Add MaterialApp to provide text directionality
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  // Replace these with your actual method channel and method names
  static const MethodChannel methodChannel =
  MethodChannel('samples.flutter.dev/mychannel');
  static const String methodName = "idcard";
  static const String methodName1 = "passport";

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  String _data = '';

  Future<void> _callSDK(String docType) async {
    try {
      final String data;
      if (docType == "passport") {
        data = await MyHomePage.methodChannel.invokeMethod(MyHomePage.methodName1);
      } else {
        data = await MyHomePage.methodChannel.invokeMethod(MyHomePage.methodName);
      }
      print("hssn -------> " + data);
      setState(() {
        _data = data.isNotEmpty ? data : 'Data is Empty.';
      });
    } on PlatformException catch (e) {
      debugPrint("Failed to Invoke: '${e.message}'.");
      setState(() {
        _data = 'Failed to get data: ${e.message}';
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Welcome"),
      ),
      body: Center(
        child: Container(
          color: Colors.white,
          child: SingleChildScrollView( // Added SingleChildScrollView here
            child: Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Directionality(
                    textDirection: TextDirection.ltr,
                    child: TextButton(
                      child: Text(
                        "Scan Id card",
                        textDirection: TextDirection.ltr,
                      ),
                      onPressed: () {
                        _callSDK("idcard");
                      },
                    ),
                  ),
                  Directionality(
                    textDirection: TextDirection.ltr,
                    child: TextButton(
                      child: Text(
                        "Scan Passport",
                        textDirection: TextDirection.ltr,
                      ),
                      onPressed: () {
                        _callSDK("passport");
                      },
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.all(16.0),
                    child: Text(_data, textAlign: TextAlign.center),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
