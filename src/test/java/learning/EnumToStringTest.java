package learning;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EnumToStringTest {

    public enum TestEnum {
        TEST1, TEST2
    }

    @Test
    void enumToStringTest() {
        assertThat(TestEnum.TEST1.toString()).isEqualTo("TEST1");
        assertThat(TestEnum.TEST2.toString()).isEqualTo("TEST2");
    }

}
