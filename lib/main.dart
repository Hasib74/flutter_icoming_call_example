import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/material.dart';
import 'package:flutter_native_channeling/src/Screen/calling_screen.dart';
import 'package:flutter_native_channeling/src/Screen/home_screen.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  Firebase.initializeApp();
  runApp(MaterialApp(
    routes: {
      '/': (BuildContext context) => HomeScreen(),
      '/calling' : (BuildContext context)=>CallingScreen(),
    },
    initialRoute: "/",
  ));
}
