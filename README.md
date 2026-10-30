# Lytro Mobile – starter 0.1.0

Android starter project for first-generation Lytro Wi-Fi import.

## Current contents
- Android/Kotlin/Jetpack Compose application shell.
- TCP reachability probe for the camera control port.
- 28-byte Lytro packet header model and bounded read-request builder.
- Initial LFP signature validator.
- Data models for camera information and photo-list entries.

## Known camera network details
For first-generation Lytro firmware with Wi-Fi support, community documentation reports:
- Camera access point SSID: `lytro.camera.###` (### = last three serial digits)
- Camera IP: `10.100.1.1`
- Control TCP: `5678`
- Picture transfer TCP: `5679`
- Event callback TCP: `5677`

Wi-Fi must be enabled on the camera. Its WPA2 key is not assumed or hardcoded.

## Important safety limits
This starter does not issue camera commands yet. It only tests whether TCP port 5678 accepts a connection.
Do not implement delete, firmware update, shell execution, reboot, or undocumented commands in the initial app.
Use bounded download chunks (community documentation warns that oversized buffers can halt the camera).
Do not claim full protocol compatibility until tested against the user's camera and firmware.

## Build
Open this folder in Android Studio (JDK 17, Android SDK 35), allow Gradle sync, then run `app`.

## Next milestones
1. Capture firmware version and validate packet framing against a real camera.
2. Implement camera-info request and response parsing.
3. Implement photo-list request, pagination/download flow, and safe chunked transfer.
4. Download JPEG preview and metadata first; then RAW payload and calibration files.
5. Parse LFP component table and render a basic preview.
6. Add refocus renderer using RAW light-field data and calibration.

## Protocol and format references
- https://optics.miloush.net/lytro/TheProtocols.aspx
- https://optics.miloush.net/lytro/TheProtocols.Commands.aspx
- https://optics.miloush.net/lytro/TheFileFormat.aspx
- https://github.com/ea/lytro_unlock
