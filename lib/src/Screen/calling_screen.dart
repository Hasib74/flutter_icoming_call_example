import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class CallingScreen extends StatelessWidget {
  const CallingScreen({Key key}) : super(key: key);

  static const methodChannel =
      const MethodChannel('samples.flutter.dev/battery');

  @override
  Widget build(BuildContext context) {
    methodChannel.setMethodCallHandler((call) {
      print("Notification method called : ${call.method}");
    });
    return Scaffold(
      body: Center(
        child: Text("I am calling screen"),
      ),
    );
  }
}
