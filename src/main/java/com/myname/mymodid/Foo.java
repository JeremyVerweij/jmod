package com.myname.mymodid;

public interface Foo {

    default void bar() {
        throw new AssertionError();
    }
}
