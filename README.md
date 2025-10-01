# Magic Visor Android App

An Android application for controlling LED-equipped wearable devices via Bluetooth Low Energy (BLE). The app connects to a custom BLE-enabled device (FloraDress) to control LED patterns, colors, brightness, and animations.

This app works with the [Magic Visor Controller](https://github.com/keithfry/visor-controller) firmware.

> [!NOTE]
> The name "FloraDress" was retained from the original Flora-based implementation, though the current device no longer uses Flora hardware.

## Overview

Magic Visor provides a mobile interface for controlling wearable LED devices with various visual effects including:
- Hearts animation
- Rainbow patterns
- Scrolling and pop-up text display
- Pulse effects
- Custom color selection
- Adjustable brightness and animation speeds

## Features

- **Bluetooth LE Device Control**: Connects to FloraDress BLE device
- **LED Pattern Control**: Multiple preset animation patterns (hearts, rainbow, pulse, etc.)
- **Custom Text Display**: Scrolling and pop-up text on LED displays
- **Color Picker**: Custom color selection for LED patterns
- **Brightness Control**: Adjustable LED brightness via slider
- **Animation Speed Control**: Configurable pulse and scroll speeds
- **Real-time Status**: Visual connection status indicators
- **Sound Integration**: Audio playback synchronized with LED effects

## Requirements

### Development Environment
- **Android Studio**: Latest version recommended
- **Java**: JDK 11 or higher
- **Gradle**: 7.4.2 (included via wrapper)

### Android Configuration
- **Min SDK Version**: 25 (Android 7.1)
- **Target SDK Version**: 30 (Android 11)
- **Compile SDK Version**: 30
- **Build Tools**: 31.0.0

### Hardware Requirements
- Android device with Bluetooth Low Energy (BLE) support
- Compatible BLE device:
  - FloraDress (Service UUID: 00001888-xxxx-xxxx-xxxx-xxxxxxxxxxxx)

## Android Permissions

The app requires the following permissions:

- `BLUETOOTH` - BLE communication
- `BLUETOOTH_ADMIN` - BLE device management
- `INTERNET` - Network access
- `ACCESS_FINE_LOCATION` - Required for BLE scanning on Android
- `ACCESS_COARSE_LOCATION` - Required for BLE scanning on Android
- `ACCESS_BACKGROUND_LOCATION` - Background BLE operations
- `ACCESS_NETWORK_STATE` - Network state monitoring

## Installation

### Clone the Repository

```bash
git clone https://github.com/yourusername/MagicVisor.git
cd MagicVisor
```

### Build with Android Studio

1. Open Android Studio
2. Select "Open an Existing Project"
3. Navigate to the MagicVisor directory
4. Wait for Gradle sync to complete
5. Connect an Android device or start an emulator
6. Click "Run" or press Shift+F10

### Build from Command Line

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install on connected device
./gradlew installDebug
```

The debug APK will be generated at:
```
app/build/outputs/apk/debug/app-debug.apk
```

## Usage

### Starting the App

1. Launch the Magic Visor app on your Android device
2. Grant Bluetooth and Location permissions when prompted
3. Press the "Start" button to begin scanning for BLE devices
4. The app will automatically connect to the nearby FloraDress device
5. The status indicator will turn green when connected

### Controlling LED Patterns

**Preset Patterns:**
- Tap any toggle button to select a pattern (Hearts, Rainbow, Dark, Pulse, etc.)
- Only one pattern can be active at a time

**Custom Text Display:**
- Enter text in the text field
- Select "Scroll Text" for scrolling display
- Select "Pop Text" for pop-up style display
- Adjust scroll rate with the slider

**Color Customization:**
- Tap the color button to open the color picker
- Select any color using the picker
- Brightness and alpha controls available
- Tap "Choose" to apply the color

**Brightness & Speed:**
- Use the Brightness slider to adjust LED intensity
- Use the Pulse Speed slider to control pulse animation speed
- Use the Scroll Rate slider to control text scrolling speed

### Stopping the Service

- Press the "Stop" button to disconnect and stop the BLE service

## Project Structure

```
MagicVisor/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/frybynite/magicvisor/
│   │       │   ├── MainActivity.java          # Main UI and controls
│   │       │   ├── BluetoothService.java      # BLE connection management
│   │       │   ├── DressActions.java          # Action constants and receivers
│   │       │   ├── SoundService.java          # Audio playback service
│   │       │   ├── MDApplication.java         # Application state
│   │       │   └── ConnectionStatus.java      # Connection status enum
│   │       ├── res/                           # Android resources
│   │       └── AndroidManifest.xml            # App manifest
│   └── build.gradle                           # App-level build config
├── build.gradle                               # Project-level build config
├── settings.gradle                            # Gradle settings
└── gradlew                                    # Gradle wrapper script
```

## Key Components

### MainActivity.java
Main activity handling UI interactions, status updates, and broadcasting actions to services.

### BluetoothService.java
Background service managing BLE device scanning, connection, and communication with FloraDress device.

### DressActions.java
Defines action constants for LED patterns and provides broadcast receiver registration utilities.

### MDApplication.java
Application class maintaining global state (color, brightness, text, animation speeds).

## Dependencies

```gradle
implementation 'androidx.appcompat:appcompat:1.0.0'
implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
implementation 'com.github.duanhong169:colorpicker:1.1.6'
testImplementation 'junit:junit:4.12'
```

## Troubleshooting

**BLE device not found:**
- Ensure Bluetooth is enabled on your Android device
- Grant Location permissions (required for BLE scanning)
- Check that the BLE device is powered on and in range
- Ensure device is named "FloraDress"

**Connection drops:**
- Keep device within Bluetooth range (typically 10 meters)
- Check battery level on BLE device
- Restart the app and try reconnecting

**Permissions denied:**
- Go to Settings > Apps > Magic Visor > Permissions
- Enable Bluetooth and Location permissions

## Development

### Building for Different SDK Versions

Edit `app/build.gradle` to change SDK versions:

```gradle
android {
    compileSdkVersion 30
    defaultConfig {
        minSdkVersion 25
        targetSdkVersion 30
    }
}
```

### Adding New LED Patterns

1. Add action constant in `DressActions.java`:
   ```java
   public static final String ACTION_NEW_PATTERN = "com.frybynite.magicvisor.action.NEW_PATTERN";
   ```

2. Register action in `DressActions.registerReceiver()` method

3. Add method in `MainActivity.java`:
   ```java
   public void newPattern(View view) {
       broadcast(DressActions.ACTION_NEW_PATTERN);
   }
   ```

4. Handle action in `BluetoothService.java` broadcast receiver

5. Add UI button in layout XML with `android:onClick="newPattern"`

## License

This project is open source. Please check the repository for license details.

## Credits

Developed by FryByNite

Uses the [Android ColorPicker](https://github.com/duanhong169/ColorPicker) library by duanhong169.

## Support

For issues, questions, or contributions, please visit the project repository or contact the development team.
