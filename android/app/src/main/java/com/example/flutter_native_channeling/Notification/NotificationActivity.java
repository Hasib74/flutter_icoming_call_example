package com.example.flutter_native_channeling.Notification;

import static androidx.core.content.ContextCompat.startActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.flutter_native_channeling.R;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.android.FlutterActivity;

import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.view.FlutterView;


import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.plugin.common.MethodChannel;


public class NotificationActivity extends FlutterActivity {

    String CHANNEL = "samples.flutter.dev/battery";


    private FlutterView flutterView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //flutterView = getFlutterView();
        System.out.println("Notification notification screen ");
        setContentView(R.layout.activity_notification);

        navigateToFlutter();
        TextView textView = (TextView) findViewById(R.id.android_text);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("Notification  on clicked");
                navigateToFlutter();
            }
        });


    }

    private void navigateToFlutter() {





       /* startActivity(
                FlutterActivity.createDefaultIntent(this)
        );*/

        startActivity(
                FlutterActivity
                        .withNewEngine()
                        .initialRoute("/calling")
                        .build(this)
        );

        this.finish();





  /*      callFlutter();
        FlutterEngine flutterEngine = new FlutterEngine(this);
        // Configure an initial route.
        flutterEngine.getNavigationChannel().setInitialRoute("/");
        // Start executing Dart code to pre-warm the FlutterEngine.
        flutterEngine.getDartExecutor().executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
        );
        // Cache the FlutterEngine to be used by FlutterActivity or FlutterFragment.
        FlutterEngineCache
                .getInstance()
                .put("my_engine_id", flutterEngine);*/


    }


    void callFlutter() {
        MethodChannel methodChannel = new MethodChannel(flutterView, CHANNEL);
        methodChannel.invokeMethod("ringing", "now_ringing");
    }
}
