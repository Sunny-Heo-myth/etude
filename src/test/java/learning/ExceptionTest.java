package learning;

import org.junit.jupiter.api.Test;

public class ExceptionTest {

    @Test
    void exceptionTest() {
        try {
            test1();
        }
        catch (RuntimeException e) {
            throw new RuntimeException("main" + e);
        }

    }

    static private void test1() {
        System.out.println(1);
        test2();
        System.out.println(2);
    }

    static private void test2() {
        System.out.println(3);
        test3();
        System.out.println(4);
    }

    static private void test3() {
        System.out.println(5);
        throw new RuntimeException("here!");
    }

}
