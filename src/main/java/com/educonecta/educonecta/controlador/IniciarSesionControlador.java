package com.educonecta.educonecta.controlador;

import java.io.IOException;

import com.educonecta.educonecta.modelo.Entidades.Usuario;
import com.educonecta.educonecta.modelo.Servicios.AutenticacionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * «Servlet» IniciarSesionControlador — CU-01: Iniciar Sesión.
 * Métodos según el diagrama de clases CU-01.
 */
@WebServlet(name = "IniciarSesionControlador", value = "/IniciarSesionControlador")
public class IniciarSesionControlador extends HttpServlet {

    private AutenticacionService autenticacionService = new AutenticacionService();

    private HttpServletRequest request;
    private HttpServletResponse response;
    private String correoIngresado;
    private String contrasenaIngresada;

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
        String accion = request.getParameter("accion");
        if (accion == null || accion.isEmpty() || "solicitarIngreso".equals(accion)) {
            solicitarIngreso();
        } else if ("confirmarIngreso".equals(accion)) {
            ingresarCredenciales(request.getParameter("correo"), request.getParameter("contrasena"));
            confirmarIngreso();
        } else if ("cerrarSesion".equals(accion)) {
            // Mapa navegacional: cerrar_sesión → Formulario de inicio de sesión.
            request.getSession().invalidate();
            response.sendRedirect("IniciarSesionControlador?accion=solicitarIngreso");
        } else {
            solicitarIngreso();
        }
    }

    /** CU-01, pasos 1-2: el Usuario solicita ingresar; se muestra el formulario de inicio de sesión. */
    public void solicitarIngreso() throws ServletException, IOException {
        request.getRequestDispatcher("/Vistas/FormularioInicioSesion.jsp").forward(request, response);
    }

    /** CU-01, paso 3: el Usuario ingresa su correo electrónico y contraseña. */
    public void ingresarCredenciales(String correo, String contrasena) {
        this.correoIngresado = correo;
        this.contrasenaIngresada = contrasena;
    }

    /** CU-01, pasos 4-10: confirmación del ingreso y verificación de credenciales. */
    public void confirmarIngreso() throws ServletException, IOException {
        String correo = this.correoIngresado;
        String contrasena = this.contrasenaIngresada;

        // Paso 5: verificar que los campos obligatorios estén completos.
        boolean camposCompletos = autenticacionService.verificarCamposObligatorios(correo, contrasena);
        if (!camposCompletos) {
            // Flujo 5a: datos incompletos — mantiene el formulario con la información ingresada.
            request.setAttribute("mensajeError", "Complete los campos obligatorios");
            request.getRequestDispatcher("/Vistas/FormularioInicioSesion.jsp").forward(request, response);
            return;
        }

        // Paso 6: buscar la cuenta asociada con el correo electrónico ingresado.
        Usuario usuario = autenticacionService.buscarCuentaPorCorreo(correo);
        if (usuario == null) {
            // Flujo 6a: cuenta no registrada.
            request.setAttribute("mensajeError", "El correo electrónico o la contraseña son incorrectos");
            request.getRequestDispatcher("/Vistas/FormularioInicioSesion.jsp").forward(request, response);
            return;
        }

        // Paso 7: comprobar que la contraseña corresponda a la cuenta encontrada.
        boolean contrasenaValida = autenticacionService.comprobarContrasena(usuario, contrasena);
        if (!contrasenaValida) {
            // Flujo 7a: contraseña incorrecta.
            request.setAttribute("mensajeError", "El correo electrónico o la contraseña son incorrectos");
            request.getRequestDispatcher("/Vistas/FormularioInicioSesion.jsp").forward(request, response);
            return;
        }

        // Paso 8: identificar el rol asociado con la cuenta.
        String rol = usuario.getRol();

        // Paso 9: obtener la información del Usuario necesaria para iniciar su sesión.
        Usuario usuarioSesion = autenticacionService.obtenerDatosSesion(usuario);
        crearSesion(usuarioSesion);

        // Paso 10: mostrar la página principal correspondiente al rol.
        if ("FAMILIAR".equals(rol)) {
            request.getRequestDispatcher("/Vistas/PaginaPrincipalFamiliar.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/Vistas/PaginaPrincipalEspecialista.jsp").forward(request, response);
        }
    }

    /** Crea la sesión HTTP del Usuario autenticado. */
    private void crearSesion(Usuario usuarioSesion) {
        request.getSession(true).setAttribute("usuarioSesion", usuarioSesion);
    }
}
