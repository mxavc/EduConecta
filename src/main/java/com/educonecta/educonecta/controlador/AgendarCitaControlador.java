package com.educonecta.educonecta.controlador;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import com.educonecta.educonecta.modelo.Entidades.Calendario;
import com.educonecta.educonecta.modelo.Entidades.Cita;
import com.educonecta.educonecta.modelo.Entidades.Especialista;
import com.educonecta.educonecta.modelo.Entidades.PerfilMenor;
import com.educonecta.educonecta.modelo.Entidades.Usuario;
import com.educonecta.educonecta.modelo.Servicios.CitaService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * «Servlet» AgendarCitaControlador — CU-04: Agendar Cita.
 * Métodos según el diagrama de clases CU-04.
 */
@WebServlet(name = "AgendarCitaControlador", value = "/AgendarCitaControlador")
public class AgendarCitaControlador extends HttpServlet {

    /** Meses hacia adelante navegables en el calendario del Especialista. */
    private static final int MESES_AGENDA = 3;

    private CitaService citaService = new CitaService();

    private HttpServletRequest request;
    private HttpServletResponse response;

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
        if (!sesionDeFamiliarValida()) {
            response.sendRedirect("IniciarSesionControlador?accion=solicitarIngreso");
            return;
        }
        String accion = request.getParameter("accion");
        if ("listarEspecialistas".equals(accion)) {
            listarEspecialistas();
        } else if ("obtenerEspecialista".equals(accion)) {
            obtenerEspecialista(Integer.parseInt(request.getParameter("idEspecialista")));
        } else if ("mostrarFormularioAgendamiento".equals(accion)) {
            mostrarFormularioAgendamiento(Integer.parseInt(request.getParameter("idEspecialista")));
        } else if ("confirmarAgendamiento".equals(accion)) {
            int idPerfilMenor = leerEntero("idPerfilMenor");
            int idEspecialista = leerEntero("idEspecialista");
            Date fecha = leerFecha("fecha");
            Time hora = leerHora("hora");
            registrarCita(idPerfilMenor, idEspecialista, fecha, hora);
        } else if ("cancelarAgendamiento".equals(accion)) {
            cancelarAgendamiento();
        } else if ("consultarCitas".equals(accion)) {
            // Mapa navegacional: consultar_citas / {rol=familiar} → Sección Mis citas.
            List<Cita> citas = citaService.listarCitasFamiliar(idFamiliarEnSesion());
            request.setAttribute("citas", citas);
            request.getRequestDispatcher("/Vistas/SeccionMisCitas.jsp").forward(request, response);
        } else {
            listarEspecialistas();
        }
    }

    /** CU-04, pasos 4-6: consultar los Especialistas disponibles. */
    public void listarEspecialistas() throws ServletException, IOException {
        // Paso 5: obtener la lista de Especialistas disponibles.
        List<Especialista> especialistas = citaService.listarEspecialistas();
        request.setAttribute("especialistas", especialistas);
        // Paso 6: mostrar la sección Especialistas con la lista obtenida.
        request.getRequestDispatcher("/Vistas/ListaDeEspecialistas.jsp").forward(request, response);
    }

    /** CU-04, pasos 7-10: selección de un Especialista, su información y sus horarios. */
    public void obtenerEspecialista(int idEspecialista) throws ServletException, IOException {
        // Paso 8: obtener la información del Especialista seleccionado.
        Especialista especialista = citaService.obtenerEspecialista(idEspecialista);
        // Paso 9: obtener los horarios disponibles del Especialista seleccionado.
        prepararCalendarioMensual(idEspecialista);
        // Paso 10: mostrar la información y los horarios disponibles del Especialista.
        request.setAttribute("especialista", especialista);
        request.getRequestDispatcher("/Vistas/InformacionEspecialista.jsp").forward(request, response);
    }

    /** CU-04, pasos 11-13: solicitar agendamiento y mostrar el formulario. */
    public void mostrarFormularioAgendamiento(int idEspecialista) throws ServletException, IOException {
        // Paso 12: obtener los perfiles de menores vinculados con el Familiar.
        List<PerfilMenor> perfilesMenores = citaService.listarPerfilesMenores(idFamiliarEnSesion());
        // Paso 13: mostrar el formulario con los perfiles de menores y los horarios
        // disponibles (calendario mensual para seleccionar fecha y hora — paso 14).
        prepararCalendarioMensual(idEspecialista);
        request.setAttribute("especialista", citaService.obtenerEspecialista(idEspecialista));
        request.setAttribute("perfilesMenores", perfilesMenores);
        request.getRequestDispatcher("/Vistas/FormularioAgendamientoCita.jsp").forward(request, response);
    }

    /**
     * Prepara el calendario mensual (mes-año-día) del Especialista: el mes mostrado
     * nunca es anterior al mes en curso — al pasar los días el calendario avanza y
     * solo se consultan los horarios desde hoy; las citas de días anteriores se
     * conservan registradas. Deja en el request: horarios, anio, mesNumero,
     * fechaHoy, mesEtiqueta y la navegación mesAnterior / mesSiguiente.
     */
    private void prepararCalendarioMensual(int idEspecialista) {
        YearMonth mesActual = YearMonth.now();
        YearMonth mesMostrado = mesActual;
        String mesParam = request.getParameter("mes");
        if (mesParam != null && !mesParam.isEmpty()) {
            try {
                mesMostrado = YearMonth.parse(mesParam);
            } catch (RuntimeException e) {
                mesMostrado = mesActual;
            }
        }
        if (mesMostrado.isBefore(mesActual)) {
            mesMostrado = mesActual;
        }
        YearMonth limiteAgenda = mesActual.plusMonths(MESES_AGENDA);
        if (mesMostrado.isAfter(limiteAgenda)) {
            mesMostrado = limiteAgenda;
        }
        LocalDate hoy = LocalDate.now();
        LocalDate inicio = mesMostrado.equals(mesActual) ? hoy : mesMostrado.atDay(1);
        LocalDate fin = mesMostrado.atEndOfMonth();

        // CU-04, paso 9: obtener los horarios disponibles del Especialista.
        List<Calendario> horarios = citaService.obtenerHorariosDisponibles(
                idEspecialista, Date.valueOf(inicio), Date.valueOf(fin));

        request.setAttribute("horarios", horarios);
        request.setAttribute("anio", mesMostrado.getYear());
        request.setAttribute("mesNumero", mesMostrado.getMonthValue());
        request.setAttribute("fechaHoy", hoy.toString());
        String etiqueta = mesMostrado.atDay(1)
                .format(DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("es")));
        request.setAttribute("mesEtiqueta",
                etiqueta.substring(0, 1).toUpperCase() + etiqueta.substring(1));
        if (mesMostrado.isAfter(mesActual)) {
            request.setAttribute("mesAnterior", mesMostrado.minusMonths(1).toString());
        }
        if (mesMostrado.isBefore(limiteAgenda)) {
            request.setAttribute("mesSiguiente", mesMostrado.plusMonths(1).toString());
        }
    }

    /** CU-04, pasos 15-17: validaciones previas al registro de la cita. */
    public void registrarCita(int idPerfilMenor, int idEspecialista, Date fecha, Time hora)
            throws ServletException, IOException {
        // Paso 16: verificar campos obligatorios completos.
        boolean existenCamposVacios = citaService.validarCamposVacios(idPerfilMenor, fecha, hora);
        if (existenCamposVacios) {
            // Flujo 16a: datos incompletos.
            mantenerFormulario(idEspecialista,
                    "Complete la información obligatoria o seleccione una fecha permitida");
            return;
        }
        // Paso 16: verificar que la fecha seleccionada sea válida.
        boolean fechaValida = citaService.validarRangoFecha(fecha);
        if (!fechaValida) {
            // Flujo 16a: fecha no válida.
            mantenerFormulario(idEspecialista,
                    "Complete la información obligatoria o seleccione una fecha permitida");
            return;
        }
        // Paso 17: verificar que la fecha y la hora seleccionadas continúen disponibles.
        boolean horarioDisponible = citaService.verificarDisponibilidad(idEspecialista, fecha, hora);
        if (!horarioDisponible) {
            // Flujo 17a: horario no disponible — actualiza los horarios del formulario.
            mantenerFormulario(idEspecialista,
                    "La fecha y hora seleccionadas ya no se encuentran disponibles");
            return;
        }
        confirmarAgendamiento(idPerfilMenor, idEspecialista, fecha, hora);
    }

    /** CU-04, pasos 18-25: registro definitivo de la cita. */
    public void confirmarAgendamiento(int idPerfilMenor, int idEspecialista, Date fecha, Time hora)
            throws ServletException, IOException {
        // Pasos 18-22: crear, vincular y registrar la cita; reservar el horario.
        citaService.agendarCita(idFamiliarEnSesion(), idPerfilMenor, idEspecialista, fecha, hora);
        // Paso 23: obtener la lista actualizada de citas del Familiar.
        List<Cita> citas = citaService.listarCitasFamiliar(idFamiliarEnSesion());
        // Pasos 24-25: mensaje de éxito y sección Mis citas con la cita registrada.
        request.setAttribute("mensajeExito", "Cita agendada con éxito");
        request.setAttribute("citas", citas);
        request.getRequestDispatcher("/Vistas/SeccionMisCitas.jsp").forward(request, response);
    }

    /** CU-04, flujo 15a: cancelar el agendamiento — la cita no se registra. */
    public void cancelarAgendamiento() throws ServletException, IOException {
        // 15a.3-15a.5: obtener nuevamente la información y los horarios del Especialista.
        int idEspecialista = Integer.parseInt(request.getParameter("idEspecialista"));
        obtenerEspecialista(idEspecialista);
    }

    /** Flujos 16a/17a: mantiene el formulario con los horarios actualizados. */
    private void mantenerFormulario(int idEspecialista, String mensaje)
            throws ServletException, IOException {
        request.setAttribute("mensajeError", mensaje);
        mostrarFormularioAgendamiento(idEspecialista);
    }

    private int leerEntero(String nombre) {
        try {
            return Integer.parseInt(request.getParameter(nombre));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private Date leerFecha(String nombre) {
        try {
            return Date.valueOf(request.getParameter(nombre));
        } catch (RuntimeException e) {
            return null;
        }
    }

    private Time leerHora(String nombre) {
        try {
            String hora = request.getParameter(nombre);
            if (hora.length() == 5) {
                hora = hora + ":00";
            }
            return Time.valueOf(hora);
        } catch (RuntimeException e) {
            return null;
        }
    }

    /** Precondición CU-04: el Familiar debe haber iniciado sesión. */
    private boolean sesionDeFamiliarValida() {
        Object usuario = request.getSession().getAttribute("usuarioSesion");
        return usuario instanceof Usuario && "FAMILIAR".equals(((Usuario) usuario).getRol());
    }

    private int idFamiliarEnSesion() {
        return ((Usuario) request.getSession().getAttribute("usuarioSesion")).getId();
    }
}
