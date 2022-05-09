package learning.packageTest.somePackage.anotherPackage;

import learning.packageTest.somePackage.AnotherClass;

public class AnotherTwoClass extends AnotherClass {

    @Override
    public void someMethod() {
        super.someMethod();
    }

//    @Override   // compile error
    void anotherMethod() {

    }
}
