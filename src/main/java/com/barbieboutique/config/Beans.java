package com.barbieboutique.config;


import com.barbieboutique.language.service.LanguageService;
import com.barbieboutique.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@AllArgsConstructor
public class Beans {
    private final LanguageService languageService;

    @Bean
    public Utils utils(){
        return new Utils(languageService);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
