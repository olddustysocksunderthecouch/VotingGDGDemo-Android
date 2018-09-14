# PART 2 - Auth, SplashScreen Intents & Signing Out

## Steps
We won't be covering how the AuthenticationActivity works but you can take a look if you'd like. Perhaps look after the workshop - it might be a bit too scary for right now.

**SplashScreenActivity**
1) Get an instance of Firebase Auth
2) Get the Firebase User object
3) If loop check if user object is null
4) Log outcome of the if loop using Log.d("foo", "bar")
5) Intents based on null or not
6) finish() the activity after the intent to prevent back navigation

**MainActivity**
1) Get the Firebase user object as in SplashScreenActivity
2) findViewById(R.id.signout_button)
3) setOnClickListener on signOutButton
4) Add a method for signOut
    - Intents
    - mAuth.signOut()
    - finish()


NB: If you checkout a new branch you’ll have to copy your google-services.json into the project app folder again