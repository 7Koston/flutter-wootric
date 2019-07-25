package com.claimsforce.wootric.flutter_wootric;
import android.app.Activity;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import com.wootric.androidsdk.Wootric;

import java.util.HashMap;

/** FlutterWootricPlugin */
public class FlutterWootricPlugin implements MethodCallHandler {
  Activity context;
  MethodChannel methodChannel;

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_wootric");
    channel.setMethodCallHandler(new FlutterWootricPlugin(registrar.activity(), channel));
  }

  public FlutterWootricPlugin(Activity activity, MethodChannel methodChannel) {
    this.context = activity;
    this.methodChannel = methodChannel;
    this.methodChannel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if(call.method.equals("showWootricSurvey")) {
      String clientId = call.argument("clientId");
      String accountToken = call.argument("accountToken");
      String email = call.argument("email");
      String userId = call.argument("userId");
      long ut2 = System.currentTimeMillis() / 1000L;
      
      Wootric wootric = Wootric.init((Activity) context, clientId, accountToken);
      wootric.setEndUserEmail(email);
      wootric.setEndUserCreatedAt(1234567890);
      wootric.setSurveyImmediately(true);
      if(userId.length() > 0) {
        wootric.setEndUserExternalId(userId);
      } 
      wootric.setProperties((HashMap<String,String>) call.argument("properties"));
      wootric.survey();
    }
  }

}