package com.example.rag.rag_simple.controller;

import com.example.rag.rag_simple.dto.ChatMessage;
import com.example.rag.rag_simple.service.RagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("documentCount", ragService.getDocumentCount());
        return "index";
    }

    @PostMapping("/upload")
    public String subirDocumento(@RequestParam("file") MultipartFile file,
                                 RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Por favor selecciona un archivo");
            return "redirect:/";
        }

        if (!file.getOriginalFilename().endsWith(".txt")) {
            redirectAttributes.addFlashAttribute("error", "Solo se permiten archivos .txt");
            return "redirect:/";
        }

        try {
            String resultado = ragService.cargarDocumento(file);
            redirectAttributes.addFlashAttribute("mensaje", resultado);
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar el archivo: " + e.getMessage());
        }

        return "redirect:/";
    }

    @PostMapping("/preguntar")
    public String preguntar(@RequestParam("pregunta") String pregunta, Model model) {
        String respuesta = ragService.preguntarSobreDocumentos(pregunta);

        ChatMessage message = new ChatMessage(pregunta, respuesta, null);
        model.addAttribute("message", message);
        model.addAttribute("documentCount", ragService.getDocumentCount());

        return "resultado";
    }

    @PostMapping("/limpiar")
    public String limpiarDocumentos(RedirectAttributes redirectAttributes) {
        ragService.limpiarDocumentos();
        redirectAttributes.addFlashAttribute("mensaje", "Todos los documentos han sido eliminados");
        return "redirect:/";
    }
}
