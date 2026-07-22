package com.educonecta.educonecta.modelo.Servicios;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

import com.educonecta.educonecta.modelo.DAO.CalendarioDAO;
import com.educonecta.educonecta.modelo.DAO.CitaDAO;
import com.educonecta.educonecta.modelo.DAO.EspecialistaDAO;
import com.educonecta.educonecta.modelo.DAO.PerfilMenorDAO;
import com.educonecta.educonecta.modelo.Entidades.Calendario;
import com.educonecta.educonecta.modelo.Entidades.Cita;
import com.educonecta.educonecta.modelo.Entidades.Especialista;
import com.educonecta.educonecta.modelo.Entidades.PerfilMenor;

/**
 * «Service» CitaService — CU-04: Agendar Cita.
 * + listarEspecialistas(): List
 * + obtenerEspecialista(idEspecialista:int): Especialista
 * + listarPerfilesMenores(idFamiliar:int): List
 * + obtenerHorariosDisponibles(idEspecialista:int, fechaInicio:Date, fechaFin:Date): List
 * + validarCamposVacios(idPerfilMenor:int, fecha:Date, hora:Time): boolean
 * + validarRangoFecha(fecha:Date): boolean
 * + verificarDisponibilidad(idEspecialista:int, fecha:Date, hora:Time): boolean
 * + agendarCita(idFamiliar:int, idPerfilMenor:int, idEspecialista:int, fecha:Date, hora:Time): Cita
 * + listarCitasFamiliar(idFamiliar:int): List
 */
public class CitaService {

    private EspecialistaDAO especialistaDAO = new EspecialistaDAO();
    private PerfilMenorDAO perfilMenorDAO = new PerfilMenorDAO();
    private CalendarioDAO calendarioDAO = new CalendarioDAO();
    private CitaDAO citaDAO = new CitaDAO();

    /** CU-04, paso 5: obtener la lista de Especialistas disponibles. */
    public List<Especialista> listarEspecialistas() {
        return especialistaDAO.buscarTodos();
    }

    /** CU-04, paso 8: obtener la información del Especialista seleccionado. */
    public Especialista obtenerEspecialista(int idEspecialista) {
        return especialistaDAO.buscarPorId(idEspecialista);
    }

    /** CU-04, paso 12: obtener los perfiles de menores vinculados con el Familiar. */
    public List<PerfilMenor> listarPerfilesMenores(int idFamiliar) {
        return perfilMenorDAO.buscarPorFamiliar(idFamiliar);
    }

    /** CU-04, paso 9: obtener los horarios disponibles del Especialista seleccionado. */
    public List<Calendario> obtenerHorariosDisponibles(int idEspecialista, Date fechaInicio, Date fechaFin) {
        return calendarioDAO.buscarDisponibilidad(idEspecialista, fechaInicio, fechaFin);
    }

    /** CU-04, paso 16: verificar que los campos obligatorios estén completos. */
    public boolean validarCamposVacios(int idPerfilMenor, Date fecha, Time hora) {
        // Retorna existenCamposVacios (true ⇒ flujo 16a).
        return idPerfilMenor <= 0 || fecha == null || hora == null;
    }

    /** CU-04, paso 16: verificar que la fecha seleccionada sea válida (no anterior a hoy). */
    public boolean validarRangoFecha(Date fecha) {
        return !fecha.toLocalDate().isBefore(LocalDate.now());
    }

    /** CU-04, paso 17: verificar que la fecha y la hora seleccionadas continúen disponibles. */
    public boolean verificarDisponibilidad(int idEspecialista, Date fecha, Time hora) {
        return calendarioDAO.existeDisponibilidad(idEspecialista, fecha, hora);
    }

    /**
     * CU-04, pasos 18-22:
     * 18. crear la cita con estado AGENDADA — «create» Cita(fecha, hora);
     * 19. vincular la cita con el Familiar, el Perfil del Menor, el Especialista y el horario;
     * 21. registrar la cita — insertar(cita);
     * 20/22. actualizar el horario seleccionado a RESERVADO — bloquearHorario(...).
     */
    public Cita agendarCita(int idFamiliar, int idPerfilMenor, int idEspecialista, Date fecha, Time hora) {
        Cita cita = new Cita(fecha, hora);
        cita.setEstado("AGENDADA");

        PerfilMenor perfilSeleccionado = null;
        for (PerfilMenor perfil : perfilMenorDAO.buscarPorFamiliar(idFamiliar)) {
            if (perfil.getId() == idPerfilMenor) {
                perfilSeleccionado = perfil;
                break;
            }
        }
        if (perfilSeleccionado == null) {
            return null;
        }
        cita.setPerfilMenor(perfilSeleccionado);
        cita.setFamiliar(perfilSeleccionado.getFamiliar());
        cita.setEspecialista(especialistaDAO.buscarPorId(idEspecialista));

        citaDAO.insertar(cita);
        calendarioDAO.bloquearHorario(idEspecialista, fecha, hora);
        return cita;
    }

    /** CU-04, paso 23: obtener la lista actualizada de citas del Familiar. */
    public List<Cita> listarCitasFamiliar(int idFamiliar) {
        return citaDAO.buscarPorFamiliar(idFamiliar);
    }
}
