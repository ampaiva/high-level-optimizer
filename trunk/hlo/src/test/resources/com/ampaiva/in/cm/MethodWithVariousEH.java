package com.ampaiva.in;

public class MethodWithVariousEH  {

	public void throwSomething() throws TestException {
        try { //1
            // These Five Lines should be Excluded
            doSomething();
            doSomething();
            doSomething();
            doSomething();
        } catch (Exception e) { //2
            doMore(); //3
        } //4
        finally { //5
            doNothing(); //6
        } //7
	}

    public void doNothing() {
    }
}