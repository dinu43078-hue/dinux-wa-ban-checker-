# DINUX WA BAN CHECKER

Native Android app built with Java + XML. This is an Android APK project, not a website or PWA.

## Features
- WhatsApp-inspired dark green UI
- Country code + phone number entry (Sri Lanka +94 default)
- Calls the configured JSON API with `HttpURLConnection`
- Network request runs on a background thread
- Displays actual API status/details/account type
- Local check history stored with `SharedPreferences`
- Copy result, history screen and About screen

## API
`https://wptest.udmodz-2ab.workers.dev/test?number=`

The app joins country code and phone number before calling the endpoint. Example: `94740026280`.

## Build APK on GitHub
1. Create a GitHub repository named `dinux-wa-ban-checker`.
2. Upload all files and folders from this project to the repository root.
3. Open **Actions** and enable workflows if GitHub asks.
4. The workflow runs automatically after a push, or choose **Build DINUX WA BAN CHECKER APK** and click **Run workflow**.
5. Open the completed workflow run, scroll to **Artifacts**, and download `DIN​​UX-WA-BAN-CHECKER-debug`.
6. Extract the artifact ZIP to get `app-debug.apk`.

## Build locally
Requires JDK 17, Android SDK Platform 35 and Gradle 8.7:
`gradle assembleDebug`

## Notes
- `ACTIVE`, `BANNED`, and other values are displayed from the API response; no status is fabricated.
- The endpoint is an unofficial third-party service. This app is not affiliated with WhatsApp and the response is not an official WhatsApp ban appeal or account verification.
- API availability, CORS is irrelevant for native Android, server limits and response format are controlled by the API provider.
- The app does not request contacts, SMS, phone-state or other unnecessary permissions.
