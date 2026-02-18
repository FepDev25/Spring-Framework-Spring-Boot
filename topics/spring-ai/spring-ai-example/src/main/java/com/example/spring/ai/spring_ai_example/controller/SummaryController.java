package com.example.spring.ai.spring_ai_example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.spring.ai.spring_ai_example.service.SummaryService;

@Controller
public class SummaryController {

    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/summarize")
    public String summarize(@RequestParam("text") String text, 
                            @RequestParam("action") String action, Model model) {
                                
        String result;

        if (null == action) {
            result = summaryService.analyzeSentiment(text);
            model.addAttribute("actionType", "Análisis de Sentimiento");
        } else switch (action) {
            case "summary" -> {
                result = summaryService.generateSummary(text);
                model.addAttribute("actionType", "Resumen");
            }
            case "frenchTranslate" -> {
                result = summaryService.translateToFrench(text);
                model.addAttribute("actionType", "Traducción al Francés");
            }
            default -> {
                result = summaryService.analyzeSentiment(text);
                model.addAttribute("actionType", "Análisis de Sentimiento");
            }
        }

        model.addAttribute("originalText", text);
        model.addAttribute("result", result);

        return "result";
    }
}
