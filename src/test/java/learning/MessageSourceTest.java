package learning;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageSourceTest {

    MessageSource messageSource;

    @Test
    void basicExample() {
        String result = messageSource.getMessage("test", null, null);
        assertThat(result).isEqualTo("hi");
    }
}
