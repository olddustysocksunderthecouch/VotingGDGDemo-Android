# PART 1 - Connecting to Firebase
## What this branch contains
Auth, BaseActivity, FirebaseUtils, Splash, Image resources, Gradle Dependencies

## Steps
    1) Clone the repo
    2) Go to Firebase Console (https://console.firebase.google.com/)
        - Create project and give it a name
        - Open Project Setting
        - Add Firebase to your Android App
        - Package Name
        - SHA1 Key for authentication (see details below **)
    3) Download and Copy google-service.json
    4) Paste (and replace) it into the app folder (Your file structure my be set to Project to do this)
    5) Enable Email Auth in Firebase by clicking on Authentication --> Sign In methods
    6) Compile the app

If you checkout a new branch you’ll have to copy your google-services.json into the project again

** Your SHA1 key can be generated in Android Studio by clicking on Gradle (far right) then --> your project name --> Tasks --> Android --> signingReport
