
# react-native-nearby-messaging-library
This library is a wrapper for Google's Nearby Messaging API with notifications already set up. Created using the `react-native-create-library` project.

## Getting started

`$ npm install react-native-nearby-messaging-library --save`

### Mostly automatic installation

`$ react-native link react-native-nearby-messaging-library`

### Manual installation


#### iOS: In progress

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-nearby-messaging-library` and add `RNNearbyMessagingLibrary.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNNearbyMessagingLibrary.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android: Complete

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNNearbyMessagingLibraryPackage;` to the imports at the top of the file
  - Add `new RNNearbyMessagingLibraryPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-nearby-messaging-library'
  	project(':react-native-nearby-messaging-library').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-nearby-messaging-library/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-nearby-messaging-library')
  	```


## Usage
```javascript
import RNNearbyMessagingLibrary from 'react-native-nearby-messaging-library';

// TODO: What to do with the module?
RNNearbyMessagingLibrary;
```
