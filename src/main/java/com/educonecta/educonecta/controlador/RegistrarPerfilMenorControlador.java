package com.educonecta.educonecta.controlador;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.educonecta.educonecta.modelo.Entidades.Familiar;
import com.educonecta.educonecta.modelo.Entidades.PerfilMenor;
import com.educonecta.educonecta.modelo.Entidades.Usuario;
import com.educonecta.educonecta.modelo.Servicios.PerfilMenorService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * «Servlet» RegistrarPerfilMenorControlador — CU-03: Registrar Perfil de Menor.
 * Métodos según el diagrama de clases CU-03.
 */
@WebServlet(name = "RegistrarPerfilMenorControlador", value = "/RegistrarPerfilMenorControlador")
public class RegistrarPerfilMenorControlador extends HttpServlet {

    private PerfilMenorService perfilMenorService = new PerfilMenorService();

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
        if (!sesionDeFamiliarValida()) {
            response.sendRedirect("IniciarSesionControlador?accion=solicitarIngreso");
            return;
        }
        String accion = request.getParameter("accion");
        if (accion == null || accion.isEmpty() || "solicitarIngreso".equals(accion)) {
            solicitarIngreso();
        } else if ("consultarPerfilesMenores".equals(accion)) {
            consultarPerfilesMenores();
        } else if ("solicitarRegistroPerfilMenor".equals(accion)) {
            solicitarRegistroPerfilMenor();
        } else if ("confirmarRegistro".equals(accion)) {
            ingresarInformacionMenor(leerDatosDelFormulario());
            confirmarRegistro();
        } else if ("cancelarRegistro".equals(accion)) {
            cancelarRegistro();
        } else {
            solicitarIngreso();
        }
    }

    /** CU-03, pasos 1-3: ingresar a la Página principal del Familiar. */
    public void solicitarIngreso() throws ServletException, IOException {
        // Paso 2: obtener la información del Familiar.
        Familiar familiar = perfilMenorService.obtenerFamiliar(idFamiliarEnSesion());
        request.setAttribute("familiar", familiar);
        // Paso 3: mostrar la Página principal del Familiar.
        request.getRequestDispatcher("/Vistas/PaginaPrincipalFamiliar.jsp").forward(request, response);
    }

    /** CU-03, pasos 4-6: consultar los perfiles de menores vinculados con el Familiar. */
    public void consultarPerfilesMenores() throws ServletException, IOException {
        // Paso 5: buscar los perfiles de menores vinculados con el Familiar.
        List<PerfilMenor> perfiles = perfilMenorService.buscarPerfilesPorFamiliar(idFamiliarEnSesion());
        request.setAttribute("perfiles", perfiles);
        // Paso 6: mostrar la sección Perfiles de menores con la lista obtenida.
        request.getRequestDispatcher("/Vistas/SeccionPerfilesMenores.jsp").forward(request, response);
    }

    /** CU-03, pasos 7-8: solicitar el registro de un nuevo Perfil de Menor. */
    public void solicitarRegistroPerfilMenor() throws ServletException, IOException {
        request.getRequestDispatcher("/Vistas/FormularioRegistroMenor.jsp").forward(request, response);
    }

    /** CU-03, paso 9: el Familiar ingresa la información del menor. */
    public void ingresarInformacionMenor(Map<String, String> datos) {
        this.datosIngresados = datos;
    }

    /** CU-03, pasos 10-17: confirmación del registro con sus verificaciones. */
    public void confirmarRegistro() throws ServletException, IOException {
        Map<String, String> datos = this.datosIngresados;
        int idFamiliar = idFamiliarEnSesion();

        // Paso 11: verificar campos obligatorios completos e información válida.
        boolean datosValidos = perfilMenorService.verificarCamposYValidez(datos);
        if (!datosValidos) {
            // Flujo 11a: datos incompletos o no válidos.
            request.setAttribute("mensajeError",
                    "Complete los campos obligatorios y verifique que la información sea válida");
            request.getRequestDispatcher("/Vistas/FormularioRegistroMenor.jsp").forward(request, response);
            return;
        }

        // Paso 12: verificar que el menor no se encuentre registrado previamente.
        boolean menorRegistrado =
                perfilMenorService.verificarMenorNoRegistrado(idFamiliar, datos.get("cedula"));
        if (menorRegistrado) {
            // Flujo 12a: menor registrado previamente — no se crea un nuevo perfil.
            request.setAttribute("mensajeError", "El menor ya se encuentra registrado");
            request.getRequestDispatcher("/Vistas/FormularioRegistroMenor.jsp").forward(request, response);
            return;
        }

        // Paso 13: crear el Perfil del Menor (incluye su HistorialAtencion — «1 posee 1»).
        PerfilMenor perfil = perfilMenorService.crearPerfilMenor(datos);

        // Paso 14: vincular el Perfil del Menor con el Familiar y registrarlo.
        Familiar familiar = perfilMenorService.obtenerFamiliar(idFamiliar);
        perfilMenorService.vincularConFamiliar(perfil, familiar);

        // Paso 15: obtener la lista actualizada de perfiles de menores.
        List<PerfilMenor> perfiles = perfilMenorService.buscarPerfilesPorFamiliar(idFamiliar);

        // Pasos 16-17: mensaje de éxito y sección Perfiles de menores actualizada.
        request.setAttribute("mensajeExito", "Perfil de menor registrado con éxito");
        request.setAttribute("perfiles", perfiles);
        request.getRequestDispatcher("/Vistas/SeccionPerfilesMenores.jsp").forward(request, response);
    }

    /** CU-03, flujo 10a: cancelar el registro. */
    public void cancelarRegistro() throws ServletException, IOException {
        descartarDatosNoConfirmados();
        // 10a.3: obtener la lista de perfiles de menores vinculados con el Familiar.
        List<PerfilMenor> perfiles = perfilMenorService.buscarPerfilesPorFamiliar(idFamiliarEnSesion());
        request.setAttribute("perfiles", perfiles);
        // 10a.4: mostrar nuevamente la sección Perfiles de menores.
        request.getRequestDispatcher("/Vistas/SeccionPerfilesMenores.jsp").forward(request, response);
    }

    /** CU-03, paso 10a.2: descartar la información no confirmada. */
    private void descartarDatosNoConfirmados() {
        this.datosIngresados = null;
    }

    /** Lee los datos del formulario de registro de menor. */
    private Map<String, String> leerDatosDelFormulario() {
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("nombres", request.getParameter("nombres"));
        datos.put("apellidos", request.getParameter("apellidos"));
        datos.put("cedula", request.getParameter("cedula"));
        datos.put("edad", request.getParameter("edad"));
        datos.put("anioEscolar", request.getParameter("anioEscolar"));
        datos.put("provincia", request.getParameter("provincia"));
        datos.put("ciudad", request.getParameter("ciudad"));
        datos.put("diagnosticoOCondicion", request.getParameter("diagnosticoOCondicion"));
        return datos;
    }

    /** Precondición CU-03: el Familiar debe haber iniciado sesión. */
    private boolean sesionDeFamiliarValida() {
        Object usuario = request.getSession().getAttribute("usuarioSesion");
        return usuario instanceof Usuario && "FAMILIAR".equals(((Usuario) usuario).getRol());
    }

    private int idFamiliarEnSesion() {
        return ((Usuario) request.getSession().getAttribute("usuarioSesion")).getId();
    }
}
