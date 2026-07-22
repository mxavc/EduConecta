package com.educonecta.educonecta.controlador;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.educonecta.educonecta.modelo.Entidades.Atencion;
import com.educonecta.educonecta.modelo.Entidades.Cita;
import com.educonecta.educonecta.modelo.Entidades.Especialista;
import com.educonecta.educonecta.modelo.Entidades.HistorialAtencion;
import com.educonecta.educonecta.modelo.Entidades.PerfilMenor;
import com.educonecta.educonecta.modelo.Entidades.Usuario;
import com.educonecta.educonecta.modelo.Servicios.AtencionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * «Servlet» AtenderCitaControlador — CU-05: Atender Cita.
 * Métodos según el diagrama de clases CU-05.
 */
@WebServlet(name = "AtenderCitaControlador", value = "/AtenderCitaControlador")
public class AtenderCitaControlador extends HttpServlet {

    private AtencionService atencionService = new AtencionService();

    private HttpServletRequest request;
    private HttpServletResponse response;
    private Map<String, String> datosIngresados;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ruteador(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ruteador(request, response);
    }

    /** Enruta la petición hacia la acción del caso de uso correspondiente. */
    private void ruteador(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        this.request = request;
        this.response = response;
        if (!sesionDeEspecialistaValida()) {
            response.sendRedirect("IniciarSesionControlador?accion=solicitarIngreso");
            return;
        }
        String accion = request.getParameter("accion");
        if (accion == null || accion.isEmpty() || "solicitarIngreso".equals(accion)) {
            solicitarIngreso();
        } else if ("consultarCitasAsignadas".equals(accion)) {
            consultarCitasAsignadas();
        } else if ("seleccionarCita".equals(accion)) {
            seleccionarCita(Integer.parseInt(request.getParameter("idCita")));
        } else if ("consultarHistorialMenor".equals(accion)) {
            consultarHistorialMenor(Integer.parseInt(request.getParameter("idPerfilMenor")));
        } else if ("solicitarRegistroAtencion".equals(accion)) {
            solicitarRegistroAtencion(Integer.parseInt(request.getParameter("idCita")));
        } else if ("confirmarRegistro".equals(accion)) {
            ingresarInformacionAtencion(leerDatosDeAtencion());
            confirmarRegistro();
        } else if ("solicitarRegistroInasistencia".equals(accion)) {
            solicitarRegistroInasistencia(Integer.parseInt(request.getParameter("idCita")));
        } else if ("ingresarObservacionYConfirmar".equals(accion)) {
            ingresarObservacionYConfirmar(Integer.parseInt(request.getParameter("idCita")),
                    request.getParameter("observacion"));
        } else if ("cancelarRegistroAtencion".equals(accion)) {
            cancelarRegistroAtencion();
        } else {
            solicitarIngreso();
        }
    }

    /** CU-05, pasos 1-3: ingresar a la Página principal del Especialista. */
    public void solicitarIngreso() throws ServletException, IOException {
        // Paso 2: obtener la información del Especialista.
        Especialista especialista = atencionService.obtenerEspecialista(idEspecialistaEnSesion());
        request.setAttribute("especialista", especialista);
        // Paso 3: mostrar la Página principal del Especialista.
        request.getRequestDispatcher("/Vistas/PaginaPrincipalEspecialista.jsp").forward(request, response);
    }

    /** CU-05, pasos 4-6: consultar las citas asignadas al Especialista. */
    public void consultarCitasAsignadas() throws ServletException, IOException {
        // Paso 5: obtener las citas vinculadas con el Especialista.
        List<Cita> citas = atencionService.listarCitasEspecialista(idEspecialistaEnSesion());
        request.setAttribute("citas", citas);
        // Paso 6: mostrar la sección Citas asignadas con la lista obtenida.
        request.getRequestDispatcher("/Vistas/SeccionCitasAsignadas.jsp").forward(request, response);
    }

    /** CU-05, pasos 7-10: selección de una cita y presentación de sus detalles. */
    public void seleccionarCita(int idCita) throws ServletException, IOException {
        // Paso 8: obtener los detalles de la cita seleccionada.
        Cita cita = atencionService.obtenerCita(idCita);
        // Paso 9: obtener la información del Perfil del Menor vinculado con la cita.
        PerfilMenor perfilMenor = atencionService.obtenerPerfilMenor(idCita);
        // Paso 10: mostrar los detalles de la cita y la información disponible del menor.
        request.setAttribute("cita", cita);
        request.setAttribute("perfilMenor", perfilMenor);
        request.getRequestDispatcher("/Vistas/DetalleCita.jsp").forward(request, response);
    }

    /** CU-05, pasos 11-14: consultar el historial del menor. */
    public void consultarHistorialMenor(int idPerfilMenor) throws ServletException, IOException {
        // Paso 12: obtener el historial vinculado con el Perfil del Menor.
        HistorialAtencion historial = atencionService.obtenerHistorial(idPerfilMenor);
        // Paso 13: obtener los registros de atenciones anteriores contenidos en el historial.
        List<Atencion> atenciones = atencionService.obtenerAtenciones(historial);
        // Paso 14: mostrar los antecedentes y los registros de atenciones anteriores del menor.
        request.setAttribute("historial", historial);
        request.setAttribute("atenciones", atenciones);
        request.setAttribute("idCita", request.getParameter("idCita"));
        request.getRequestDispatcher("/Vistas/HistorialAtencionMenor.jsp").forward(request, response);
    }

    /** CU-05, pasos 15-16: solicitar el registro de la atención. */
    public void solicitarRegistroAtencion(int idCita) throws ServletException, IOException {
        Cita cita = atencionService.obtenerCita(idCita);
        request.setAttribute("cita", cita);
        request.getRequestDispatcher("/Vistas/FormularioRegistroAtencion.jsp").forward(request, response);
    }

    /** CU-05, paso 17: el Especialista ingresa la información de la atención. */
    public void ingresarInformacionAtencion(Map<String, String> datos) {
        this.datosIngresados = datos;
    }

    /** CU-05, pasos 18-27: confirmación del registro de la atención. */
    public void confirmarRegistro() throws ServletException, IOException {
        Map<String, String> datos = this.datosIngresados;
        int idCita = Integer.parseInt(datos.get("idCita"));

        // Paso 19: verificar que los campos obligatorios estén completos y sean válidos.
        boolean datosValidos = atencionService.verificarCamposAtencion(datos);
        if (!datosValidos) {
            // Flujo 19a: datos incompletos — mantiene el formulario con la información ingresada.
            request.setAttribute("mensajeError", "Complete los campos obligatorios");
            request.setAttribute("cita", atencionService.obtenerCita(idCita));
            request.getRequestDispatcher("/Vistas/FormularioRegistroAtencion.jsp").forward(request, response);
            return;
        }

        Cita cita = atencionService.obtenerCita(idCita);
        PerfilMenor perfilMenor = atencionService.obtenerPerfilMenor(idCita);
        HistorialAtencion historial = atencionService.obtenerHistorial(perfilMenor.getId());

        // Paso 20: crear el registro de la atención.
        Atencion atencion = atencionService.crearAtencion(datos);
        // Paso 21: vincular la atención con la cita, el Especialista y el Perfil del Menor.
        atencionService.vincularAtencion(atencion, cita, idEspecialistaEnSesion(), perfilMenor);
        // Paso 22: incorporar la atención al historial del menor.
        atencionService.incorporarAtencionAlHistorial(historial, atencion);
        // Paso 23: actualizar el estado de la cita a ATENDIDA.
        atencionService.actualizarEstadoCita(cita, "ATENDIDA");
        // Paso 24: registrar los cambios realizados.
        atencionService.registrarCambios(atencion, cita, historial);

        // Paso 25: obtener la lista actualizada de citas asignadas al Especialista.
        List<Cita> citas = atencionService.listarCitasEspecialista(idEspecialistaEnSesion());
        // Pasos 26-27: mensaje de éxito y sección Citas asignadas actualizada.
        request.setAttribute("mensajeExito", "La atención ha sido registrada correctamente");
        request.setAttribute("citas", citas);
        request.getRequestDispatcher("/Vistas/SeccionCitasAsignadas.jsp").forward(request, response);
    }

    /** CU-05, pasos 7a.5-7a.6: solicitar el registro de la inasistencia. */
    public void solicitarRegistroInasistencia(int idCita) throws ServletException, IOException {
        Cita cita = atencionService.obtenerCita(idCita);
        request.setAttribute("cita", cita);
        request.getRequestDispatcher("/Vistas/FormularioRegistroInasistencia.jsp").forward(request, response);
    }

    /** CU-05, pasos 7a.7-7a.16: registro de la inasistencia. */
    public void ingresarObservacionYConfirmar(int idCita, String observacion)
            throws ServletException, IOException {
        // Paso 7a.8: verificar que la información obligatoria esté completa.
        boolean observacionValida = atencionService.verificarObservacionCompleta(observacion);
        if (!observacionValida) {
            request.setAttribute("mensajeError", "Complete los campos obligatorios");
            request.setAttribute("cita", atencionService.obtenerCita(idCita));
            request.getRequestDispatcher("/Vistas/FormularioRegistroInasistencia.jsp").forward(request, response);
            return;
        }

        Cita cita = atencionService.obtenerCita(idCita);
        PerfilMenor perfilMenor = atencionService.obtenerPerfilMenor(idCita);
        HistorialAtencion historial = atencionService.obtenerHistorial(perfilMenor.getId());

        // Paso 7a.9: crear el registro de inasistencia.
        cita = atencionService.crearRegistroInasistencia(cita, observacion);
        // Paso 7a.10: vincular la inasistencia con la cita y el Perfil del Menor.
        atencionService.vincularInasistencia(cita, perfilMenor);
        // Paso 7a.11: incorporar la inasistencia al historial del menor.
        atencionService.incorporarInasistenciaAlHistorial(historial, cita);
        // Paso 7a.12: actualizar el estado de la cita a NO_ASISTIDA.
        atencionService.actualizarEstadoCita(cita, "NO_ASISTIDA");
        // Paso 7a.13: registrar los cambios realizados.
        atencionService.registrarCambios(cita);

        // Paso 7a.14: obtener la lista actualizada de citas asignadas al Especialista.
        List<Cita> citas = atencionService.listarCitasEspecialista(idEspecialistaEnSesion());
        // Pasos 7a.15-7a.16: mensaje de éxito y sección Citas asignadas actualizada.
        request.setAttribute("mensajeExito", "La inasistencia ha sido registrada correctamente");
        request.setAttribute("citas", citas);
        request.getRequestDispatcher("/Vistas/SeccionCitasAsignadas.jsp").forward(request, response);
    }

    /** CU-05, flujo 18a: cancelar el registro de la atención. */
    public void cancelarRegistroAtencion() throws ServletException, IOException {
        descartarDatosNoConfirmados();
        // 18a.3-18a.5: obtener nuevamente los detalles de la cita y mostrarlos.
        int idCita = Integer.parseInt(request.getParameter("idCita"));
        seleccionarCita(idCita);
    }

    /** CU-05, paso 18a.2: descartar la información no confirmada. */
    private void descartarDatosNoConfirmados() {
        this.datosIngresados = null;
    }

    /** Lee los datos del formulario de registro de atención. */
    private Map<String, String> leerDatosDeAtencion() {
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("idCita", request.getParameter("idCita"));
        datos.put("observaciones", request.getParameter("observaciones"));
        datos.put("informacionRelevante", request.getParameter("informacionRelevante"));
        datos.put("recomendaciones", request.getParameter("recomendaciones"));
        datos.put("diagnostico", request.getParameter("diagnostico"));
        datos.put("indicacionesDeSeguimiento", request.getParameter("indicacionesDeSeguimiento"));
        datos.put("notasAdicionales", request.getParameter("notasAdicionales"));
        return datos;
    }

    /** Precondición CU-05: el Especialista debe haber iniciado sesión. */
    private boolean sesionDeEspecialistaValida() {
        Object usuario = request.getSession().getAttribute("usuarioSesion");
        return usuario instanceof Usuario && "ESPECIALISTA".equals(((Usuario) usuario).getRol());
    }

    private int idEspecialistaEnSesion() {
        return ((Usuario) request.getSession().getAttribute("usuarioSesion")).getId();
    }
}
