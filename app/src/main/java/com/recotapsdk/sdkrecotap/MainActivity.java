package com.recotapsdk.sdkrecotap;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.recotapsdk.recotap_sdk.Recotap;

import java.util.HashMap;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private static final int RC_VIDEO_APP_PERM = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Recotap recotap = new Recotap("nGaMdfY59CRfMy0n8YBAmDdUGpV8Tu");

        recotap.getInstance(this, getApplicationContext());

        HashMap<String, String> loginDetails = new HashMap<>();
        loginDetails.put("user_id", "12345");
        loginDetails.put("username", "Souptik");
        loginDetails.put("email", "souptik@impel.io");
        loginDetails.put("age", "49");
        loginDetails.put("gender", "M");

        recotap.login(loginDetails);


        HashMap<String, String> eventDetails = new HashMap<>();
        eventDetails.put("video_id", "12345");
        eventDetails.put("session_id", "lsdfjlaskdjf");

        recotap.emit("Video Played", eventDetails);
        recotap.emit("Video Paused", eventDetails);
        recotap.emit("Video Stopped", eventDetails);

        recotap.logout();

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
