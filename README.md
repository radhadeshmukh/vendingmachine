# VendingMachine

-----------------------------
Question - PRACTICAL EXERCISE
-----------------------------
Write a program to design Vending Machine using your 'favourite language' with all possible tests
1. Accepts coins of 1,5,10,25 Cents i.e. penny, nickel, dime, and quarter.
2. Allow user to select products Coke(25), Pepsi(35), Soda(45)
3. Allow user to take refund by cancelling the request.
4. Return selected product and remaining change if any.
5. Allow reset operation for vending machine supplier.
*****************************

Answer
-----------------------------

How to test/build/run
---------------------
This is a java maven project. I have used Eclipse to build, run the tests and program.
I have built a command line vending machine. To run the program, run the
com.test.vending.Main class. To run the test from commandline, run:

```
mvn test
```


A little bit about the program
------------------------------
The VendingMachine interface specifies methods for the main requirements in the Question.
SimpleVendingMachine is the command line implementation of the VendingMachine.
In addition to printing commandline output, the SimpleVendingMachine also returns
the objects back to the calling API. The internal state of VendingMachine is managed via
VendingMachineStateManager.

Tests
-----
There are tests for the `SimpleVendingMachine` to test the behaviour. But in addition
there are tests for `VendingMachineStateManager` because it is responsible for ensuring
the `SimpleVendingMachine` is in the right state for the given set of inputs. There is
also a test for Coin's `compareTo` method - this is to ensure the coins get sorted correctly
for dispensing the remaining change back to user.

----*----*----*----*----*----*----*----*----*----*----*----*----*----*----*----*----*----*----
