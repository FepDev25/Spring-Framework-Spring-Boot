package com.example.asistente.asistente_clasificacion.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

import com.example.asistente.asistente_clasificacion.dto.TicketCategory;
import com.example.asistente.asistente_clasificacion.entity.Ticket;
import com.example.asistente.asistente_clasificacion.repository.TicketRepository;

@Service
public class TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    private final ChatClient chatClient;
    private final TicketRepository ticketRepository;
    private final BeanOutputConverter<TicketCategory> outputConverter;

    public TicketService(ChatClient.Builder chatClientBuilder, TicketRepository ticketRepository) {
        this.chatClient = chatClientBuilder.build();
        this.ticketRepository = ticketRepository;

        // explicar a la clase BeanOutputConverter el tipo de dato esperado, en este caso TicketCategory
        this.outputConverter = new BeanOutputConverter<>(TicketCategory.class);
    }

    public Ticket clasificarYGuardar(String descripcion) {
        // obtiene la clasificación del ticket
        TicketCategory category = clasificarTicket(descripcion);

        // crea y guarda el ticket en la base de datos
        Ticket ticket = new Ticket(
                descripcion,
                category.getCategoria(),
                category.getPrioridad(),
                category.getDepartamento(),
                category.getResumen()
        );

        return ticketRepository.save(ticket);
    }

    public TicketCategory clasificarTicket(String descripcion) {
        // obtener el formato esperado por el BeanOutputConverter
        String format = outputConverter.getFormat();

        log.info("Formato esperado por BeanOutputConverter: {}", format);

        String promptTemplate = """
                Eres un asistente experto en clasificación de tickets de soporte técnico.

                Analiza el siguiente problema reportado por un usuario y clasifícalo:

                Problema: {descripcion}

                Debes clasificar el ticket en:
                - categoria: RED, SOFTWARE, HARDWARE, ACCESO, CORREO, OTRO
                - prioridad: CRITICA, ALTA, MEDIA, BAJA
                - departamento: SISTEMAS, SOPORTE, INFRAESTRUCTURA, SEGURIDAD
                - resumen: Un resumen breve del problema en una línea

                {format}
                """;

        PromptTemplate template = new PromptTemplate(promptTemplate);
        var prompt = template.create(Map.of(
                "descripcion", descripcion,
                "format", format
        ));

        // recibe el json puro como String
        String response = chatClient.prompt(prompt)
                .call()
                .content();

        log.info("Respuesta de Ollama: {}", response);

        // convertir la respuesta en un objeto TicketCategory
        TicketCategory category = outputConverter.convert(response);

        log.info("Ticket clasificado: categoria={}, prioridad={}, departamento={}, resumen={}",
                 category.getCategoria(), category.getPrioridad(),
                 category.getDepartamento(), category.getResumen());

        return category;
    }

    public Ticket generarYGuardarRespuesta(Long ticketId) {
        Ticket ticket = obtenerPorId(ticketId);

        if (ticket == null) {
            throw new IllegalArgumentException("Ticket con ID " + ticketId + " no encontrado.");
        }

        // Evitar regenerar si ya existe
        if (ticket.getRespuestaSugerida() != null && !ticket.getRespuestaSugerida().isEmpty()) {
            log.info("Ticket {} ya tiene respuesta sugerida, retornando existente", ticketId);
            return ticket;
        }

        String promptTemplate = """
            Actúa como un agente de soporte técnico del departamento de {departamento}.
            Escribe una respuesta corta y profesional para el siguiente ticket:
            
            Problema: {descripcion}
            Prioridad: {prioridad}
            Categoría: {categoria}
            
            Instrucciones de tono:
            - Si la prioridad es CRITICA, sé muy empático y asegura atención inmediata.
            - Si la categoría es ACCESO, sugiere verificar mayúsculas o reiniciar el router.
            - Firma como "Tu Asistente de IA".
            """;

        PromptTemplate template = new PromptTemplate(promptTemplate);
        var prompt = template.create(Map.of(
                "departamento", ticket.getDepartamento(),
                "descripcion", ticket.getDescripcion(),
                "prioridad", ticket.getPrioridad(),
                "categoria", ticket.getCategoria()
        ));

        String respuesta = chatClient.prompt(prompt)
                .call()
                .content();

        ticket.setRespuestaSugerida(respuesta);
        log.info("Respuesta generada y guardada para ticket {}", ticketId);
        
        return ticketRepository.save(ticket);
    }

    public List<Ticket> obtenerTodos() {
        return ticketRepository.findAllByOrderByFechaCreacionDesc();
    }

    public List<Ticket> obtenerPorEstado(String estado) {
        return ticketRepository.findByEstadoOrderByFechaCreacionDesc(estado);
    }

    public List<Ticket> obtenerPorPrioridad(String prioridad) {
        return ticketRepository.findByPrioridadOrderByFechaCreacionDesc(prioridad);
    }

    public Ticket obtenerPorId(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }

    public void actualizarEstado(Long id, String nuevoEstado) {
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        if (ticket != null) {
            ticket.setEstado(nuevoEstado);
            ticketRepository.save(ticket);
        }
    }
}
