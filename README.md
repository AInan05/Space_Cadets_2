# Space_Cadets_2

This is my attempt at Space Cadets Challange 2, SCChallengeBareBones.

This program is currently incomplete but functional. I will add error handling in the next commit.

The program uses a stack to keep track of while loops. When the program encounters an "end" statement, it goes to the line on top of the stack.
I tries a bunch of approaches and ended up using this one to ensure that the nested while loops functions properly.
I will fix the issue that happens when the condition of the while loops is false when first entering the loop.

The program uses a dictionary to handle the variables.

I used subroutines for each keyword so the program would be extendable.
