package com.recotapsdk.recotap_sdk;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Recotap {

    private static final int MY_PERMISSIONS_REQUEST = 124;
    private static final String URL = "http://testapi.recotap.com/v1/api/events";

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();
    private String json;

    public Recotap(String key) {
        Log.i("RecotapDetails " + 1, key);
    }

    public void getInstance(Activity activity, Context context) {

        Log.i("RecotapDetails " + 2, "Got the activity " + String.valueOf(activity));

        // Here, thisActivity is the current activity
        if ((ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {

            Log.i("RecotapDetails " + 2, "Permission is not granted ");
            // Permission is not granted
            // Should we show an explanation?
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(activity,
                    new String[]{
                        Manifest.permission.INTERNET,
                            Manifest.permission.CAMERA,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, MY_PERMISSIONS_REQUEST);

            // MY_PERMISSIONS_REQUEST is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        } else {
            // Permission has already been granted
            Log.i("RecotapDetails " + 2, "Permission has been granted.");
        }

    }

    public void login(HashMap loginDetails) {
        JSONObject jsonObject = new JSONObject(loginDetails);
        Log.i("RecotapDetails " + 3, String.valueOf(jsonObject));


        /*json = jsonObject.toString();

        try {
            String test = post(URL, json);
            Log.i("RecotapDetails " + 3, test);

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void emit(String eventName, HashMap eventData) {
        JSONObject jsonObject = new JSONObject(eventData);
        Log.i("RecotapDetails " + 4, eventName + " " + String.valueOf(jsonObject));
    }

    public void logout() {
        Log.i("RecotapDetails " + 5, "Logged Out!");
    }


    private String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
