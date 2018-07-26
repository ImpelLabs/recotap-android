package com.recotapsdk.recotap_sdk;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
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

public class Recotap extends Activity{

    private static final int MY_PERMISSIONS_REQUEST = 124;
    private static String API_URL, CLIENT_ID, CLIENT_SECRET, TOKEN_URL, ACCOUNT_ID;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();

    private String getToken;
    private JSONObject token;



    public Recotap(String key) {
        Log.i("RecotapDetails " + 1, key);

    }

    public void getInstance(Activity activity, Context context) {

        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;

            ACCOUNT_ID = bundle.getString("RECOTAP_ACCOUNT_ID");
            CLIENT_ID = bundle.getString("RECOTAP_CLIENT_ID");
            CLIENT_SECRET = bundle.getString("RECOTAP_CLIENT_SECRET");
            TOKEN_URL = bundle.getString("RECOTAP_TOKEN_URL");
            API_URL = bundle.getString("RECOTAP_ENDPOINT_URL");

            Log.e("RecotapDetails " + 2, API_URL + "\n" + CLIENT_ID + "\n" + CLIENT_SECRET + "\n");

            // get token
            JSONObject tokenDetails = new JSONObject();

            tokenDetails.put("client_id", CLIENT_ID);
            tokenDetails.put("client_secret", CLIENT_SECRET);

            String sendToken = tokenDetails.toString();

            getToken = post(TOKEN_URL, sendToken);


        } catch (Exception e) {
            Log.e("RecotapDetails " + 2, "Don't forget to configure <meta-data android:name=\"my_test_metagadata\" android:value=\"testValue\"/> in your AndroidManifest.xml file.");
        }

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

    public Integer login(HashMap loginDetails) {

        Integer test = null;

        try {
            JSONObject jsonObject = new JSONObject(loginDetails);
            JSONArray jsonArray = new JSONArray();
            if (getToken != null) {
                token = new JSONObject(getToken);
                jsonArray.put(jsonObject);
                String json = jsonArray.toString();
                test= post(API_URL, json, "Bearer " + token.getString("token"));
                Log.i("RecotapDetails " + 3.5, String.valueOf(test));
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return test;
    }

    public Integer emit(HashMap eventData) {

        Integer response = null;
        try {
            JSONObject jsonObject = new JSONObject(eventData);
            JSONArray jsonArray = new JSONArray();
            token = new JSONObject(getToken);
            jsonArray.put(jsonObject);

            String json = jsonArray.toString();
            response = post(API_URL, json , "Bearer " + token.getString("token"));
            Log.i("RecotapDetails " + 4, String.valueOf(response));

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return response;

    }

    public Integer logout(HashMap user) {

        Integer response = null;
        try {
            JSONObject jsonObject = new JSONObject(user);
            JSONArray jsonArray = new JSONArray();
            token = new JSONObject(getToken);
            jsonArray.put(jsonObject);

            String json = jsonArray.toString();
            response = post(API_URL, json , "Bearer " + token.getString("token"));
            Log.i("RecotapDetails " + 5, String.valueOf(response));

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return response;
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
