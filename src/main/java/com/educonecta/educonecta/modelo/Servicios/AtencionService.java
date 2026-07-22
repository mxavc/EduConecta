package com.educonecta.educonecta.modelo.Servicios;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.educonecta.educonecta.modelo.DAO.AtencionDAO;
import com.educonecta.educonecta.modelo.DAO.CitaDAO;
import com.educonecta.educonecta.modelo.DAO.EspecialistaDAO;
import com.educonecta.educonecta.modelo.DAO.HistorialAtencionDAO;
import com.educonecta.educonecta.modelo.DAO.PerfilMenorDAO;
import com.educonecta.educonecta.modelo.Entidades.Atencion;
import com.educonecta.educonecta.modelo.Entidades.Cita;
import com.educonecta.educonecta.modelo.Entidades.Especialista;
import com.educonecta.educonecta.modelo.Entidades.HistorialAtencion;
import com.educonecta.educonecta.modelo.Entidades.PerfilMenor;

/**
 * «Service» AtencionService — CU-05: Atender Cita.
 * Métodos según el diagrama de clases CU-05.
 */
public class AtencionService {

    private EspecialistaDAO especialistaDAO = new EspecialistaDAO();
    private CitaDAO citaDAO = new CitaDAO();
    private PerfilMenorDAO perfilMenorDAO = new PerfilMenorDAO();
    private HistorialAtencionDAO historialAtencionDAO = new HistorialAtencionDAO();
    private AtencionDAO atencionDAO = new AtencionDAO();

    /** CU-05, paso 2: obtener la información del Especialista para su página principal. */
    public Especialista obtenerEspecialista(int idEspecialista) {
        return especialistaDAO.buscarPorId(idEspecialista);
    }

    /** CU-05, paso 5: obtener las citas vinculadas con el Especialista. */
    public List<Cita> listarCitasEspecialista(int idEspecialista) {
        return citaDAO.buscarPorEspecialista(idEspecialista);
    }

    /** CU-05, paso 8: obtener los detalles de la cita seleccionada. */
    public Cita obtenerCita(int idCita) {
        return citaDAO.buscarPorId(idCita);
    }

    /** CU-05, paso 9: obtener el Perfil del Menor vinculado con la cita. */
    public PerfilMenor obtenerPerfilMenor(int idCita) {
        return perfilMenorDAO.buscarPorCita(idCita);
    }

    /** CU-05, paso 12: obtener el historial vinculado con el Perfil del Menor. */
    public HistorialAtencion obtenerHistorial(int idPerfilMenor) {
        return historialAtencionDAO.buscarPorPerfilMenor(idPerfilMenor);
    }

    /** CU-05, paso 13: obtener los registros de atenciones anteriores del historial. */
    public List<Atencion> obtenerAtenciones(HistorialAtencion historial) {
        return atencionDAO.buscarPorHistorial(historial.getId());
    }

    /** CU-05, paso 19: verificar que los campos obligatorios estén completos y sean válidos. */
    public boolean verificarCamposAtencion(Map<String, String> datos) {
        // Diagnóstico, indicaciones de seguimiento y notas adicionales son «si aplica».
        for (String campo : Arrays.asList("observaciones", "informacionRelevante", "recomendaciones")) {
            String valor = datos.get(campo);
            if (valor == null || valor.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /** CU-05, paso 7a.8: verificar que la observación de la inasistencia esté completa. */
    public boolean verificarObservacionCompleta(String observacion) {
        return observacion != null && !observacion.trim().isEmpty();
    }

    /** CU-05, paso 20: crear el registro de la atención — «create» Atencion(...). */
    public Atencion crearAtencion(Map<String, String> datos) {
        return new Atencion(
                datos.get("observaciones"), datos.get("informacionRelevante"),
                datos.get("recomendaciones"), datos.get("diagnostico"),
                datos.get("indicacionesDeSeguimiento"), datos.get("notasAdicionales"));
    }

    /**
     * CU-05, paso 21: vincular la atención con la cita, el Especialista y el
     * Perfil del Menor (el vínculo con el menor queda dado a través de la cita).
     */
    public void vincularAtencion(Atencion atencion, Cita cita, int idEspecialista, PerfilMenor perfilMenor) {
        atencion.setCita(cita);
        atencion.setEspecialista(especialistaDAO.buscarPorId(idEspecialista));
    }

    /** CU-05, paso 22: incorporar la atención al historial del menor. */
    public void incorporarAtencionAlHistorial(HistorialAtencion historial, Atencion atencion) {
        historial.agregarAtencion(atencion);
    }

    /** CU-05, paso 7a.9: crear el registro de inasistencia sobre la cita. */
    public Cita crearRegistroInasistencia(Cita cita, String observacion) {
        cita.setObservacionInasistencia(observacion);
        return cita;
    }

    /**
     * CU-05, paso 7a.10: vincular la inasistencia con la cita y el Perfil del Menor
     * (el vínculo queda dado por la relación cita—perfilMenor ya existente).
     */
    public void vincularInasistencia(Cita cita, PerfilMenor perfilMenor) {
        cita.setPerfilMenor(perfilMenor);
    }

    /** CU-05, paso 7a.11: incorporar la inasistencia al historial del menor. */
    public void incorporarInasistenciaAlHistorial(HistorialAtencion historial, Cita cita) {
        historial.registrarInasistencia(cita);
        historialAtencionDAO.actualizar(historial);
    }

    /** CU-05, pasos 23 / 7a.12: actualizar el estado de la cita (ATENDIDA / NO_ASISTIDA). */
    public void actualizarEstadoCita(Cita cita, String estado) {
        cita.setEstado(estado);
    }

    /**
     * CU-05, paso 24: registrar los cambios realizados —
     * insertar(atencion) → actualizar(historial) → actualizar(cita).
     */
    public void registrarCambios(Atencion atencion, Cita cita, HistorialAtencion historial) {
        atencionDAO.insertar(atencion);
        historialAtencionDAO.actualizar(historial);
        citaDAO.actualizar(cita);
    }

    /** CU-05, paso 7a.13: registrar los cambios de la cita — actualizar(cita). */
    public void registrarCambios(Cita cita) {
        citaDAO.actualizar(cita);
    }
}
