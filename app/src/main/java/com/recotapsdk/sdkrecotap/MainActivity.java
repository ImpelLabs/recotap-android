package com.recotapsdk.sdkrecotap;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.recotapsdk.recotap_sdk.Recotap;

import java.util.HashMap;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private static final int RC_VIDEO_APP_PERM = 124;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button button = (Button)findViewById(R.id.click);
        final TextView response = (TextView) findViewById(R.id.response);

        final Recotap recotap = new Recotap("nGaMdfY59CRfMy0n8YBAmDdUGpV8Tu");

        recotap.getInstance(this, getApplicationContext());

        try {
            ApplicationInfo ai = getApplicationContext().getPackageManager().getApplicationInfo(getApplicationContext().getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            user_id = bundle.getString("RECOTAP_ACCOUNT_ID");

            Log.e("RecotapDetails", user_id);
        } catch (Exception e) {
            Log.e("RecotapDetails", "CONFIGURE NEEDED!");
        }

        final HashMap<String, String> loginDetails = new HashMap<>();
        loginDetails.put("user_id", user_id);
        loginDetails.put("username", "Souptik");
        loginDetails.put("email", "souptik@impel.io");
        loginDetails.put("age", "49");
        loginDetails.put("gender", "M");

        HashMap<String, String> eventDetails = new HashMap<>();
        eventDetails.put("video_id", "12345");
        eventDetails.put("session_id", "lsdfjlaskdjf");

        recotap.emit("Video Played", eventDetails);
        recotap.emit("Video Paused", eventDetails);
        recotap.emit("Video Stopped", eventDetails);

        final HashMap<String, String> logoutDetails = new HashMap<>();
        logoutDetails.put("12345", "Logged Out");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer loginTest, logoutTest;
                loginTest = recotap.login(loginDetails);
                logoutTest = recotap.logout(logoutDetails);

                response.setText(String.valueOf(loginTest) +" "+ String.valueOf(logoutTest));
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = {
                Manifest.permission.INTERNET,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        if (EasyPermissions.hasPermissions(getApplicationContext(), perms)) {

            Toast.makeText(this, "All Permission Granted!", Toast.LENGTH_LONG).show();


        } else {
            EasyPermissions.requestPermissions(this,
                    "This app needs access to your Camera and Location to make Video Calls",
                    RC_VIDEO_APP_PERM, perms);
        }
    }
}
