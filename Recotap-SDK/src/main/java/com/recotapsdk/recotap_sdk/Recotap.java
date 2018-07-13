package com.recotapsdk.recotap_sdk;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Recotap{

    private static final int MY_PERMISSIONS_REQUEST = 124;
    private static final String API_URL = "https://testapi.recotap.com/v1/api/events";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();

    private String getToken;
    private JSONObject token;

    public Recotap(String key) {
        Log.i("RecotapDetails " + 1, key);

        try {
            JSONObject tokenDetails = new JSONObject();

            tokenDetails.put("client_id", "01U11SeHvN1JuqI");
            tokenDetails.put("client_secret", "WZdoawHBBjjtWQ6mIiYpYLk2C");

            String sendToken = tokenDetails.toString();

            getToken = post("https://testapi.recotap.com/v1/oauth/token?key=CC7LyIcNj5VYpTjW8n85fuFFkv7Ygg",
                sendToken);

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

    }

    public void getInstance(Activity activity) {

        if ((ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {

            // Permission is not granted. No explanation needed; request the permission
            ActivityCompat.requestPermissions(activity,
                    new String[]{
                        Manifest.permission.INTERNET,
                            Manifest.permission.CAMERA,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, MY_PERMISSIONS_REQUEST);

            // MY_PERMISSIONS_REQUEST is an app-defined int constant.

        } else {
            // Permission has already been granted
            Log.i("RecotapDetails " + 2, "Permission has been granted.");
        }

    }

    public void login(HashMap loginDetails) {

        try {

            JSONObject jsonObject = new JSONObject(loginDetails);
            JSONArray jsonArray = new JSONArray();

            if (getToken != null) {
                token = new JSONObject(getToken);
                jsonArray.put(jsonObject);

                String json = jsonArray.toString();

                Integer test = post(API_URL, json, "Bearer " + token.getString("token"));
                Log.i("RecotapDetails " + 3.5, String.valueOf(test));
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

    }

    public void emit(String eventName, HashMap eventData) {
        JSONObject jsonObject = new JSONObject(eventData);
        Log.i("RecotapDetails " + 4, eventName + " " + String.valueOf(jsonObject));
    }

    public void logout(HashMap user) {

        try {
            JSONObject jsonObject = new JSONObject(user);
            JSONArray jsonArray = new JSONArray();

            token = new JSONObject(getToken);
            jsonArray.put(jsonObject);

            String json = jsonArray.toString();


            Integer response = post(API_URL, json , "Bearer " + token.getString("token"));

            Log.i("RecotapDetails " + 5, String.valueOf(response));

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


    }


    @SuppressWarnings("SameParameterValue")
    private String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }

    private Integer post(String url, String json, String token) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.code();
        }
    }

}
