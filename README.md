# TwitterLogin123
Twitter Login 

#Registering Twitter App

1. Go to https://dev.twitter.com/apps/new and register new application. Fill application name, description and website.
2. Give some dummy url in the callback url field to make the app as browser app(If you leave it as blank it will act as Desktop app which won’t work in mobile device).
3. Copy Consumer Key & Consumer Secret key

#App Gradle  
1. Add following implementations :  
implementation 'com.twitter.sdk.android:twitter-core:3.1.1'  
implementation 'com.twitter.sdk.android:tweet-ui:3.1.1'  
2. Add Permissions in your Manifest file  
```
<uses-permission android:name="android.permission.INTERNET" />  
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />  
```
