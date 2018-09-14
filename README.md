# PART 6 - Check if answered and answer
## Steps
1) Add clickListeners for the kinda and no buttons
2) Let's abstract the setValue functions into a method that can be called for each button
3) Compile the app (you'll realise that you can answer a question multiple times. This is rather undesirable...
4) In the abstracted setValue function check if an answer exists in the flat_answers node.
5) If it doesn't exist set the values for both the flat and nested nodes
6) Use a toast to tell the user what they answer and if they've already answered

## noSQL data structures
This is worth repeating from the previous branch:
Firebase Realtime Database is a JSON tree. You could actually think of it as an address system. e.g
we are in at Nona, Woodstock, Cape Town, South Africa. If you had to find us you would start at South Africa and drill down.
This it the same for firebase.

**Tips**

* As flat as possible
* Only nest something if it makes sense
* Form your objects around the data you need for a particular view
* Keys/values in one node inform us how to get to others
* UIDs unlock data the address system

