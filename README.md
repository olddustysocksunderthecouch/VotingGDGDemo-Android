# PART 5 - setValues in Firebase
## noSQL data structures
Firebase Realtime Database is a JSON tree. You could actually think of it as an address system. e.g
we are in at Nona, Woodstock, Cape Town, South Africa. If you had to find us you would start at South Africa and drill down.
This it the same for firebase.

**Tips**

* As flat as possible
* Only nest something if it makes sense
* Form your objects around the data you need for a particular view
* Keys/values in one node inform us how to get to others
* UIDs unlock data the address system


## Steps
1) Add clickListener for the yes button
2) Define a DatabaseReference for the flat and nest nodes
3) Look compile the app and see the values update
4) Delete the nodes in firebase and see it happen again (you can delete and click yes, to your hearts content).
I donno about you but I find this very satisfying



