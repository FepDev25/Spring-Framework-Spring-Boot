package com.example.asistente.asistente_clasificacion.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.asistente.asistente_clasificacion.entity.Ticket;
import com.example.asistente.asistente_clasificacion.service.TicketService;


@Controller
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Ticket> tickets = ticketService.obtenerTodos();
        model.addAttribute("tickets", tickets);
        return "index";
    }

    @PostMapping("/clasificar")
    public String clasificarTicket(@RequestParam String descripcion, Model model) {
        Ticket ticket = ticketService.clasificarYGuardar(descripcion);
        model.addAttribute("ticket", ticket);
        model.addAttribute("mensaje", "Ticket clasificado y guardado exitosamente");
        return "resultado";
    }

    @PostMapping("/tickets/{id}/respuesta")
    public String generarRespuesta(@PathVariable Long id, Model model) {
        Ticket ticket = ticketService.generarYGuardarRespuesta(id);
        model.addAttribute("ticket", ticket);
        model.addAttribute("mensaje", "Respuesta sugerida generada exitosamente");
        return "resultado";
    }
    
    @GetMapping("/tickets/{id}")
    public String verTicket(@PathVariable Long id, Model model) {
        Ticket ticket = ticketService.obtenerPorId(id);
        if (ticket == null) {
            return "redirect:/tickets";
        }
        model.addAttribute("ticket", ticket);
        return "resultado";
    }
    

    @GetMapping("/tickets")
    public String listarTickets(@RequestParam(required = false) String filtro,
                                @RequestParam(required = false) String valor,
                                Model model) {
        List<Ticket> tickets;

        if (filtro != null && valor != null) {
            tickets = switch (filtro) {
                case "prioridad" -> ticketService.obtenerPorPrioridad(valor);
                case "estado" -> ticketService.obtenerPorEstado(valor);
                default -> ticketService.obtenerTodos();
            };
        } else {
            tickets = ticketService.obtenerTodos();
        }

        model.addAttribute("tickets", tickets);
        return "lista";
    }

    @PostMapping("/tickets/{id}/estado")
    public String cambiarEstado(@PathVariable Long id, @RequestParam String estado) {
        ticketService.actualizarEstado(id, estado);
        return "redirect:/tickets";
    }
}
