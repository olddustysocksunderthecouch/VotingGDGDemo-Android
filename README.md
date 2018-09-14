# PART 3 - First valueEventListener

## Steps
1) Add question node to your Firebase Realtime Database
2) Get a reference to the database
3) Create a DatabaseReference for Question
4) AddValueEventListener
5) Add and if statement to make sure that dataSanapShot.exists() or you might get a null pointer exception
6) Get the value of the dataSnapshot in the form of a String **
6) findviewbyid - questionTextView
7) set the text of the questionTextView with questionTextView.setText()


** You can also get it in the form of a custom model that you create. This is useful if you have multiple named fields in the dataShapshot. We won't use this in the workshop but you will most likely use in other apps that you build.
Example:

person1:
    - name
    - age
    - height

In this case you'd create a Person plain old java object (POJO) aka model that takes in name, age, height
