package learning.packageTest.somePackage;

import learning.packageTest.SomeClass;

public class AnotherClass extends SomeClass {

    @Override
    public void someMethod() {
        System.out.println("another class some method");
    }

    void anotherMethod() {
        System.out.println("another class another method");
    }

//    @Override
//    public final void someFinalMethod() {
//        System.out.println("some class some final method");
//    }

//    @Override
//    public static void someStaticMethod() {
//        System.out.println("some class some static method");
//    }
}
