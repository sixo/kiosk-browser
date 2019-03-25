# Kiosk Browser for Android
This app works as "Kiosk" browser. It loads a specific url inside of WebView and locks the screen, so that the user is not able to exit.

The app is related to my blogpost that shows how to use Android's device owner mode to turn your Android device into a dedicated (COSU) kiosk
[www.sisik.eu/blog/android/dev-admin/kiosk-browser](https://sisik.eu/blog/android/dev-admin/kiosk-browser)

Before you can use the kiosk browser, you need to configure your target device via ADB
```
adb shell dpm set-device-owner eu.sisik.kioskbrowser/.DevAdminReceiver
```

To load your own url, change the `MY_URL` variable inside of `MainActivity`.