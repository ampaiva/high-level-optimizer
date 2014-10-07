package com.ampaiva.in;

public class MethodWithEHCatchAfterBrace  {

	public void throwSomething() {
        try { //1
            // These Five Lines should be Excluded
            doSomething();
            doSomething();
            doSomething();
            doSomething();
        } //2
        catch (Exception e) { //3
            doMore(); //4
        } //5
        finally { //6
            doNothing(); //7
        } //8
	}

    public void doNothing() {
    }
}