# Recotap Android SDK

This guide will show you how to install the Recotap SDK, track your first user event, and see this information within the Recotap dashboard.

# Step 1: Install SDK
You can install it automatically using Gradle in Android Studio. Just add these lines to you build.gradle root and build.gradle app.

Add it in your root build.gradle at the end of repositories:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

And add the dependency
**NOTE: Please check for the latest version in the releases**
```
dependencies {
    ...
    implementation 'com.github.ImpelLabs:recotap-android:0.2.1'
}
```
Sync the gradle file and now you can build the SDK.

# Step 2: Enable Tracking by Adding Permissions
Now add these permissions in the AndroidManifest.xml file
```
<!-- Required to allow the app to send events and user profile information -->
<uses-permission android:name="android.permission.INTERNET"/>
<!-- Recommended so that Recotap knows when to attempt a network call -->
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```



# Step 4: Initialize the CleverTap SDK

```
// Initilize the SDK
Recotap recotap = new Recotap("Put_key_here");

// Get Instance for permissions by passing the activity instance
recotap.getInstance(this)

```

# Step 5: Send Information to server
You can send login data to server using HashMap with the properties. You have to call the SDK's recotap.login method and pass the HashMap you created as a parameter.

```
HashMap<String, String> loginDetails = new HashMap<>();
loginDetails.put("user_id", "12345");
loginDetails.put("username", "Name");
loginDetails.put("email", "example@impel.io");
loginDetails.put("age", "30");
loginDetails.put("gender", "M");

recotap.login(loginDetails);
```

To know if the user logged out or not use **recotap.logout** method
```
HashMap<String, String> logoutDetails = new HashMap<>();
logoutDetails.put(user_id, "Logged Out");
        
recotap.logout(logoutDetails);
```

To send event data you have many options.
But first you need to gather event information in **HashMap**

```
HashMap<String, String> events = new HashMap<>();
events.put("video_id", "12345");
events.put("session_id", "lsdfjlaskdjf");
```
Then you need to combine the eventDetails in another **HashMap** with your **Event Name**

```
HashMap<String, HashMap> eventDetails = new HashMap<>();
// Use when Video is playing
eventDetails.put("Video Played", events);

// Use when Video is Paused
eventDetails.put("Video Paused", events);

// Use when Video is Stopped
eventDetails.put("Video Stopped", events);
```
Use **recotap.emit** method to push the data to server
```
// Then call the methods like below
recotap.emit(eventDetails);
```

