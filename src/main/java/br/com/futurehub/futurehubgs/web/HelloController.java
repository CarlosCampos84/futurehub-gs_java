package br.com.futurehub.futurehubgs.web;

import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
public class HelloController {

    private final MessageSource messageSource;

    public HelloController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping(path = "/hello", produces = MediaType.TEXT_PLAIN_VALUE)
    public String hello(Locale locale) {
        return messageSource.getMessage("app.title", null, locale);
    }
}




