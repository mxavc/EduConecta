package com.educonecta.educonecta.modelo.DAO;

import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.educonecta.educonecta.modelo.Entidades.Calendario;
import com.educonecta.educonecta.modelo.Entidades.Especialista;
import com.educonecta.educonecta.util.JPAUtil;

import jakarta.persistence.EntityManager;

/**
 * «DAO» CalendarioDAO — CU-04.
 * + buscarDisponibilidad(idEspecialista:int, fechaInicio:Date, fechaFin:Date): List
 * + existeDisponibilidad(idEspecialista:int, fecha:Date, hora:Time): boolean
 * + bloquearHorario(idEspecialista:int, fecha:Date, hora:Time): void
 */
public class CalendarioDAO {

    /** Jornada de atención base del calendario: 09h00 a 16h00, lunes a viernes. */
    private static final int HORA_INICIO_JORNADA = 9;
    private static final int HORA_FIN_JORNADA = 16;

    private EntityManager em;

    /**
     * CU-04, paso 9: obtener los horarios disponibles del Especialista.
     * El diagrama de secuencia CU-04 muestra «create» Calendario() dentro de esta
     * operación: si el Especialista aún no tiene agenda en el rango consultado,
     * se genera su calendario base (garantiza la precondición del CU-04:
     * «Debe existir al menos un Especialista con horarios disponibles»).
     */
    public List<Calendario> buscarDisponibilidad(int idEspecialista, Date fechaInicio, Date fechaFin) {
        generarAgendaBaseSiNoExiste(idEspecialista, fechaInicio, fechaFin);
        em = JPAUtil.crearEntityManager();
        try {
            // Devuelve la Disponibilidad (fecha, hora, estado) del rango consultado;
            // el estado de cada horario se distingue con isDisponible():
            // disponible = true → libre · disponible = false → RESERVADO (CU-04, paso 20).
            return em.createQuery(
                    "SELECT c FROM Calendario c WHERE c.especialista.id = :idEspecialista "
                            + "AND c.fecha BETWEEN :fechaInicio AND :fechaFin "
                            + "ORDER BY c.fecha, c.hora",
                    Calendario.class)
                    .setParameter("idEspecialista", idEspecialista)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /** CU-04, paso 17: verificar que la fecha y la hora seleccionadas continúen disponibles. */
    public boolean existeDisponibilidad(int idEspecialista, Date fecha, Time hora) {
        em = JPAUtil.crearEntityManager();
        try {
            Long total = em.createQuery(
                    "SELECT COUNT(c) FROM Calendario c WHERE c.especialista.id = :idEspecialista "
                            + "AND c.fecha = :fecha AND c.hora = :hora AND c.disponible = true",
                    Long.class)
                    .setParameter("idEspecialista", idEspecialista)
                    .setParameter("fecha", fecha)
                    .setParameter("hora", hora)
                    .getSingleResult();
            return total > 0;
        } finally {
            em.close();
        }
    }

    /** CU-04, paso 20-22: actualizar el estado del horario seleccionado a RESERVADO. */
    public void bloquearHorario(int idEspecialista, Date fecha, Time hora) {
        em = JPAUtil.crearEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery(
                    "UPDATE Calendario c SET c.disponible = false WHERE c.especialista.id = :idEspecialista "
                            + "AND c.fecha = :fecha AND c.hora = :hora")
                    .setParameter("idEspecialista", idEspecialista)
                    .setParameter("fecha", fecha)
                    .setParameter("hora", hora)
                    .executeUpdate();
            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    /**
     * Genera la agenda base del Especialista para los días del rango que aún no
     * existen (Modelo de Dominio: el Especialista «establece» Disponibilidad).
     * Al pasar los días, el calendario avanza: los días nuevos se generan y los
     * días anteriores (con sus citas registradas) se conservan intactos.
     */
    private void generarAgendaBaseSiNoExiste(int idEspecialista, Date fechaInicio, Date fechaFin) {
        EntityManager emAgenda = JPAUtil.crearEntityManager();
        try {
            Especialista especialista = emAgenda.find(Especialista.class, idEspecialista);
            if (especialista == null) {
                return;
            }
            // Fechas del rango que ya tienen agenda establecida.
            List<Date> registradas = emAgenda.createQuery(
                    "SELECT DISTINCT c.fecha FROM Calendario c WHERE c.especialista.id = :idEspecialista "
                            + "AND c.fecha BETWEEN :fechaInicio AND :fechaFin",
                    Date.class)
                    .setParameter("idEspecialista", idEspecialista)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getResultList();
            Set<LocalDate> fechasRegistradas = new HashSet<LocalDate>();
            for (Date fecha : registradas) {
                fechasRegistradas.add(fecha.toLocalDate());
            }
            emAgenda.getTransaction().begin();
            LocalDate dia = fechaInicio.toLocalDate();
            LocalDate fin = fechaFin.toLocalDate();
            while (!dia.isAfter(fin)) {
                boolean diaLaborable = dia.getDayOfWeek() != DayOfWeek.SATURDAY
                        && dia.getDayOfWeek() != DayOfWeek.SUNDAY;
                if (diaLaborable && !fechasRegistradas.contains(dia)) {
                    for (int horaJornada = HORA_INICIO_JORNADA; horaJornada <= HORA_FIN_JORNADA; horaJornada++) {
                        Calendario calendario = new Calendario();
                        calendario.setEspecialista(especialista);
                        calendario.setFecha(Date.valueOf(dia));
                        calendario.setHora(Time.valueOf(LocalTime.of(horaJornada, 0)));
                        calendario.setDisponible(true);
                        emAgenda.persist(calendario);
                    }
                }
                dia = dia.plusDays(1);
            }
            emAgenda.getTransaction().commit();
        } finally {
            if (emAgenda.getTransaction().isActive()) {
                emAgenda.getTransaction().rollback();
            }
            emAgenda.close();
        }
    }
}
