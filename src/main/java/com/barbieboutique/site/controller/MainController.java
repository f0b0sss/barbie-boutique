package com.barbieboutique.site.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class MainController {

    @RequestMapping({"", "/"})
    public String index(Model model, HttpSession httpSession, HttpServletRequest request) {

//        Locale currentLocale = request.getLocale();
//        String countryCode = currentLocale.getCountry();
//        String countryName = currentLocale.getDisplayCountry();
//
//        String langCode = currentLocale.getLanguage();
//        String langName = currentLocale.getDisplayLanguage();
//        System.out.println(countryCode + ": " + countryName);
//        System.out.println(langCode + ": " + langName);
//
//        Locale ua = Locale.forLanguageTag("uk-UA");
//        langCode = ua.getLanguage();
//        langName = ua.getDisplayLanguage();
//        System.out.println("langCode: " + langCode);
//        System.out.println("langName: " + langName);


//        String[] languages = Locale.getISOLanguages();
//        for (String language : languages) {
//            Locale locale = new Locale(language);
//            System.out.println(language + ": " + locale.getDisplayLanguage());
//        }

        return "index";
    }

}
