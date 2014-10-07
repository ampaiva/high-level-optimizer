package com.ampaiva.in;

public class MethodWithVariousEH  {

	public void throwSomething() throws TestException { //1
        try { //2
            /* These Five Lines should be Excluded */
            doSomething();
            doSomething();
            doSomething();
            doSomething();
        } catch (Exception e) { //3
            doMore(); //4
        } //5
        finally { //6
            doNothing(); //7
        } //8
	}

    public void doNothing() {
    }
}