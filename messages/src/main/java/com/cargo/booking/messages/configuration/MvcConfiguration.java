package com.cargo.booking.messages.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;


@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    @Value("${com.cargo.booking.language-parameter}")
    private String languageParameter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName(languageParameter);
        registry.addInterceptor(localeChangeInterceptor);
    }
}
