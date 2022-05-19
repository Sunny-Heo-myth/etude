package learning;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageSourceTest {

    ResourceBundleMessageSource messageSource;
    @BeforeEach
    void beforeEach() {
        messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/test");
    }

    @Test
    void basicExample() {
        // given, when
        String result = messageSource.getMessage("test.message", null, null);
        //String result2 = messageSource.getMessage("test.message", null, new Locale("ko"));

        // then
        assertThat(result).isEqualTo("test default");
        //assertThat(result).isEqualTo("test Korean");
    }
}
