# PART 7 - Listen for answer totals
So let's listen for how many times a certain answer has been given for a particular question (yes, kinda, maybe).

## Steps
1) Create a method that is called in the question index ValueEventListener for each type of answer
2) In the method, set the relevant TextView to the number of children for the specific answer that the method is called with.
The number of children can be gotten with the dataSnapshot.getChildrenCount() call.

