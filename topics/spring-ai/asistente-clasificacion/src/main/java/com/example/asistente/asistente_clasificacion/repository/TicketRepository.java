package com.example.asistente.asistente_clasificacion.repository;

import com.example.asistente.asistente_clasificacion.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByEstadoOrderByFechaCreacionDesc(String estado);

    List<Ticket> findAllByOrderByFechaCreacionDesc();

    List<Ticket> findByPrioridadOrderByFechaCreacionDesc(String prioridad);

    List<Ticket> findByCategoriaOrderByFechaCreacionDesc(String categoria);
}
