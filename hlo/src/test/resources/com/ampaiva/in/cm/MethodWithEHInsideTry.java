package com.ampaiva.in;

public class MethodWithEHInsideTry  {

	public void throwSomething() {
        try { //1
            // Four Lines should be Excluded
            doSomething();
            try{ //2
                doSomething();
            } //3
            catch (Exception e) { //4
                doMore(); //5
            } //6
            doSomething();
            doSomething();
        } catch (Exception e) { //7
            doMore(); //8
        } //9
        finally { //10
            doNothing(); //11
        } //12
	}

    public void doNothing() {
    }
}