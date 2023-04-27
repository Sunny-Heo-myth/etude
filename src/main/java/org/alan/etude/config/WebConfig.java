package org.alan.etude.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MessageSource messageSource;
    @Value("${upload.image.location}")
    private String location;

    public WebConfig(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**")
                // access with a designated file system location.
                .addResourceLocations("file:" + location)
                .setCacheControl(CacheControl.maxAge(Duration.ofHours(1L)).cachePublic());

        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:/Users/sunny/IdeaProjects/etude/src/main/resources/static/");
    }

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }
}
