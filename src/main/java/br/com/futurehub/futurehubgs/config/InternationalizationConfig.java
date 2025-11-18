package br.com.futurehub.futurehubgs.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class InternationalizationConfig implements WebMvcConfigurer {

    /**
     * Define o LocaleResolver que o Spring MVC vai usar.
     * Aqui usamos sessão, com default em pt-BR.
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(new Locale("pt", "BR"));
        return resolver;
    }

    /**
     * Fonte de mensagens para i18n.
     * Vai ler:
     * - messages.properties
     * - messages_pt_BR.properties
     * - messages_es_ES.properties
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.setBasename("classpath:messages");
        ms.setDefaultEncoding("UTF-8");
        ms.setUseCodeAsDefaultMessage(false);
        return ms;
    }

    /**
     * Permite trocar idioma via parâmetro ?lang=pt_BR|en|es
     * Ex.: /api/ideias?lang=es_ES
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor i = new LocaleChangeInterceptor();
        i.setParamName("lang");
        registry.addInterceptor(i);
    }
}





