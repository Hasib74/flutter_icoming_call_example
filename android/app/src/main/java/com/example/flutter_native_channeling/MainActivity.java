package com.example.flutter_native_channeling;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.view.FlutterNativeView;

public class MainActivity extends FlutterActivity {

    private static final String CHANNEL = "samples.flutter.dev/battery";

    String method_background = "background_method";

    MethodChannel _methodChannel;

    EventChannel _eventChannel;


    private Map<Object, Runnable> listeners = new HashMap<>();


    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);

        _eventChannel = new EventChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), method_background);
        _methodChannel = new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL);

        _methodChannel.setMethodCallHandler(
                (call, result) -> {
                    // Note: this method is invoked on the main thread.
                    // TODO
                    if (call.method.equals("getBatteryLevel")) {

                        int batteryLevel = getBatteryLevel();
                        System.out.print("_batteryLevel  :: " + batteryLevel);
                        if (batteryLevel != -1) {
                            result.success(batteryLevel);
                        } else {
                            result.error("UNAVAILABLE", "Battery level not available.", null);
                        }
                    } else if (call.method.equals(method_background)) {
                        result.success("Welcome to background");
                    } else {

                        result.error("500", "Error", null);

                    }

                }
        );
        _methodChannel.invokeMethod("test", null, new MethodChannel.Result() {
            @Override
            public void success(@Nullable Object result) {


                System.out.println("The result is  " + result.toString());
                _methodChannel.invokeMethod("getBatteryLevel", "I am ok");


            }

            @Override
            public void error(String errorCode, @Nullable String errorMessage, @Nullable Object errorDetails) {

            }

            @Override
            public void notImplemented() {

            }
        });


        _backgroundStream();

    }

    private void _backgroundStream() {

        _eventChannel.setStreamHandler(new EventChannel.StreamHandler() {
            @Override
            public void onListen(Object arguments, EventChannel.EventSink events) {
                try {
                    Handler handler = new Handler();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Context context = getApplicationContext();
                            CharSequence text = "Hello toast!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast.makeText(context, text, duration);
                            events.success("I am stream");
                            //your code
                            handler.postDelayed(this, 1000);
                        }
                    }, 1000);

                } catch (Exception exception) {

                    events.error("505", "Failed to stream", null);

                }


            }

            @Override
            public void onCancel(Object arguments) {
                cancelListening(arguments);
            }
        });
    }


    void cancelListening(Object listener) {
        // Remove callback

        System.out.println("Argument :: " + listener);


        listeners.remove(listener);
    }

    private Object _backgroundMessage() {


        return "Welcome to background";
    }


    private int getBatteryLevel() {
        int batteryLevel = -1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
            batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        } else {
            Intent intent = new ContextWrapper(getApplicationContext()).
                    registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            batteryLevel = (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) /
                    intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        }

        return batteryLevel;
    }
}
