package learning;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class PredicateTest {

    @Test
    void testTest() {
        // given
        Predicate<Integer> predicate = num -> num > 10;

        // when
        boolean result = predicate.test(100);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void andTest() {
        // given
        Predicate<Long> predicate1 = num -> num > 10;
        Predicate<Long> predicate2 = num -> num < 20;

        // when
        boolean result = predicate1.and(predicate2).test(15L);

        // then
        assertThat(result).isTrue();
    }
}
