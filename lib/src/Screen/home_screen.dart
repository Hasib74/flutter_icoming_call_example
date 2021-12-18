import 'dart:async';
import 'dart:math';

import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'dart:convert' show utf8;

String _TAG = "Method_channeling";

class HomeScreen extends StatelessWidget {
  HomeScreen({Key key}) : super(key: key);

  var platform = MethodChannel('samples.flutter.dev/battery');

  var channel = "getBatteryLevel";

  //var channel_background = "getBackgroundMessage";

  var method_background = "background_method";

  @override
  Widget build(BuildContext context) {
    _setUpToken();
    _getBackgroundMessage();
    _getBataryLevel();

    return Scaffold(
      body: Center(child: Text("Home"),),
    );
  }

  void _getBataryLevel() async {
    String batteryLevel;

    try {
      try {
        final int result = await platform.invokeMethod('getBatteryLevel');
        batteryLevel = 'Battery level at $result % .';
        print("Batary level is :: ${batteryLevel}");
      } on PlatformException catch (e) {
        batteryLevel = "Failed to get battery level: '${e.message}'.";
      }
    } catch (err) {
      print("$_TAG  error ${err.toString()}");
    }
  }

  _text() {
    print("Test");

    return "Text";
  }

  void _getBackgroundMessage() async {
    EventChannel _event_channel = new EventChannel(method_background);

    _event_channel.receiveBroadcastStream().listen((event) {
      print("Data is :: ${event.toString()}");
    });
  }

  void _setUpToken() {
    FirebaseMessaging _firebaseMessage = new FirebaseMessaging();
    _firebaseMessage.getToken().then((token) {
      print("Firebase token is ::: ${token}");

      FirebaseFirestore.instance
          .collection("token")
          .doc("hasib_gmail_com")
          .set({"token": token}).catchError((err) {
        print("Fcm The error : ${err.toString()}");
      });
    }).catchError((err) {
      print("Fcm error :: ${err.toString()}");
    });
  }
}
