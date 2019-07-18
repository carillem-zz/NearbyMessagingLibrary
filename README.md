
# react-native-nearby-messaging-library
This library is a wrapper for Google's Nearby Messaging API with notifications already set up. Created using the `react-native-create-library` project.

## Getting started

### 1. Download library
#### NPM

`$ yarn add react-native-nearby-messaging-library-with-notifications` or `$ npm install -i react-native-nearby-messaging-library-with-notifications`

#### Github

`$ yarn add react-native-nearby-messaging-library` or `$ npm install react-native-nearby-messaging-library --save`

### 2. Link library to project
#### Mostly automatic installation

`$ react-native link react-native-nearby-messaging-library`

#### Manual installation

##### Android: Complete

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import com.reactlibrary.RNNearbyMessagingLibraryPackage;` to the imports at the top of the file
  - Add `new RNNearbyMessagingLibraryPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-nearby-messaging-library'
  	project(':react-native-nearby-messaging-library').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-nearby-messaging-library/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      implementation project(':react-native-nearby-messaging-library')
  	```

##### iOS: In progress

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-nearby-messaging-library` and add `RNNearbyMessagingLibrary.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNNearbyMessagingLibrary.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)

## Usage

### Update app AndroidManifest

1. Update permission requests
```
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
<uses-permission android:name="android.permission.WAKE_LOCK" />
```

2. Add API key
```
<meta-data
          android:name="com.google.android.nearby.messages.API_KEY"
          android:value="YOURAPIKEYHERE"
          />
```

### Call library

```javascript
import RNNearbyMessagingLibrary from 'react-native-nearby-messaging-library';

// TODO: What to do with the module?
RNNearbyMessagingLibrary.checkLibraryConnection();
```

## Library Methods
 All functions are `@ReactMethod` and by default returns void. If you would like for it to return something to the react native layer, feel free to edit the `../android/src/main/java/com/cmendoza/RNNearbyMessagingLibraryModule.java` and use RCTDeviceEventEmitter.

### foregroundSubscribe()
  Starts subscribing in the foreground and uses Bluetooth, Location, and Wifi technologies.

### backgroundSubscribe()
 Starts subscribing in the background and uses BLE. It is considered best practice to have a notification running if Nearby is listening in the background.

### publish(String msgToPublish)
 Publishes a message using Google's Nearby Messaging API to all subscribers.

### checkLibraryConnection()
 Creates a toast message to confirm that the library was connected properly.

### backgroundUnsubscribe()
 Stops background subscription.

### foregroundUnsubscribe()
 Stops foreground subscription.
