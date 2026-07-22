package com.educonecta.educonecta.controlador;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.educonecta.educonecta.modelo.Entidades.Especialista;
import com.educonecta.educonecta.modelo.Entidades.Familiar;
import com.educonecta.educonecta.modelo.Entidades.Usuario;
import com.educonecta.educonecta.modelo.Servicios.RegistroUsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

/**
 * «Servlet» RegistrarUsuarioControlador — CU-02: Registrar Usuario.
 * Métodos según el diagrama de clases CU-02.
 */
@WebServlet(name = "RegistrarUsuarioControlador", value = "/RegistrarUsuarioControlador")
@MultipartConfig(maxFileSize = 10 * 1024 * 1024, maxRequestSize = 60 * 1024 * 1024)
public class RegistrarUsuarioControlador extends HttpServlet {

    private RegistroUsuarioService registroUsuarioService = new RegistroUsuarioService();

    private HttpServletRequest request;
    private HttpServletResponse response;
    private Map<String, String> datosIngresados;
    private List<String[]> documentosCargados;

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
        if ("solicitarRegistro".equals(accion)) {
            solicitarRegistro();
        } else if ("seleccionarTipoCuenta".equals(accion)) {
            seleccionarTipoCuenta(request.getParameter("tipoCuenta"));
        } else if ("confirmarRegistro".equals(accion)) {
            completarInformacion(leerDatosDelFormulario());
            if ("ESPECIALISTA".equals(datosIngresados.get("tipoCuenta"))) {
                cargarDocumentos(leerDocumentosDelFormulario());
            }
            confirmarRegistro();
        } else if ("cancelarRegistro".equals(accion)) {
            cancelarRegistro();
        } else {
            solicitarRegistro();
        }
    }

    /** CU-02, pasos 1-2: solicitar registro; se muestran los tipos de cuenta disponibles. */
    public void solicitarRegistro() throws ServletException, IOException {
        request.getRequestDispatcher("/Vistas/SeleccionTipoCuenta.jsp").forward(request, response);
    }

    /** CU-02, pasos 3-5: selección del tipo de cuenta y formulario correspondiente. */
    public void seleccionarTipoCuenta(String tipoCuenta) throws ServletException, IOException {
        // Paso 4: obtener los datos requeridos para el tipo de cuenta seleccionado.
        List<String> camposRequeridos = registroUsuarioService.obtenerDatosRequeridos(tipoCuenta);
        request.setAttribute("camposRequeridos", camposRequeridos);
        // Paso 5: mostrar el formulario de registro correspondiente.
        if ("ESPECIALISTA".equals(tipoCuenta)) {
            request.getRequestDispatcher("/Vistas/FormularioRegistroEspecialista.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/Vistas/FormularioRegistroFamiliar.jsp").forward(request, response);
        }
    }

    /** CU-02, paso 6: el Usuario completa la información solicitada. */
    public void completarInformacion(Map<String, String> datos) {
        this.datosIngresados = datos;
        this.documentosCargados = new ArrayList<String[]>();
    }

    /** CU-02, paso 7: el Especialista carga los documentos requeridos. */
    public void cargarDocumentos(List<String[]> documentos) {
        this.documentosCargados = documentos;
    }

    /** CU-02, pasos 8-17: confirmación del registro con sus verificaciones. */
    public void confirmarRegistro() throws ServletException, IOException {
        Map<String, String> datos = this.datosIngresados;
        List<String[]> documentos = this.documentosCargados;
        String tipoCuenta = datos.get("tipoCuenta");

        // Paso 9: verificar campos obligatorios completos y formato válido.
        boolean datosValidos = registroUsuarioService.verificarCamposYFormato(datos);
        if (!datosValidos) {
            // Flujo 9a: información incompleta o no válida.
            mantenerFormulario(tipoCuenta,
                    "Complete los campos obligatorios y verifique el formato de la información");
            return;
        }

        // Paso 10: verificar que el correo electrónico no se encuentre registrado.
        boolean correoRegistrado = registroUsuarioService.verificarCorreoNoRegistrado(datos.get("correo"));
        if (correoRegistrado) {
            // Flujo 10a: correo electrónico registrado.
            mantenerFormulario(tipoCuenta, "El correo electrónico ingresado ya se encuentra registrado");
            return;
        }

        // Paso 11: verificar que el número de cédula no se encuentre registrado.
        boolean cedulaRegistrada = registroUsuarioService.verificarCedulaNoRegistrada(datos.get("cedula"));
        if (cedulaRegistrada) {
            // Flujo 11a: número de cédula registrado.
            mantenerFormulario(tipoCuenta, "El número de cédula ingresado ya se encuentra registrado");
            return;
        }

        if ("ESPECIALISTA".equals(tipoCuenta)) {
            // Paso 12: verificar que el RUC no se encuentre registrado.
            boolean rucRegistrado = registroUsuarioService.verificarRucNoRegistrado(datos.get("ruc"));
            if (rucRegistrado) {
                // Flujo 12a: RUC registrado.
                mantenerFormulario(tipoCuenta, "El RUC ingresado ya se encuentra registrado");
                return;
            }
            // Paso 13: verificar que los documentos requeridos hayan sido cargados.
            boolean documentosOk = registroUsuarioService.verificarDocumentosCargados(documentos);
            if (!documentosOk) {
                // Flujo 13a: documentos no cargados.
                mantenerFormulario(tipoCuenta, "Revise y cargue correctamente los documentos solicitados");
                return;
            }
        }

        // Paso 14: crear la cuenta con el tipo seleccionado.
        Usuario cuenta = registroUsuarioService.crearCuenta(tipoCuenta, datos);

        // Paso 15: registrar la información proporcionada por el Usuario.
        if (cuenta instanceof Especialista) {
            registroUsuarioService.registrarInformacion((Especialista) cuenta, documentos);
        } else {
            registroUsuarioService.registrarInformacion((Familiar) cuenta);
        }

        // Pasos 16-17: mensaje de éxito y formulario de inicio de sesión.
        request.setAttribute("mensajeExito", "Registro realizado correctamente");
        request.getRequestDispatcher("/Vistas/FormularioInicioSesion.jsp").forward(request, response);
    }

    /** CU-02, flujo 8a: cancelar el registro. */
    public void cancelarRegistro() throws ServletException, IOException {
        descartarDatosNoConfirmados();
        // 8a.3: mostrar nuevamente el formulario de inicio de sesión.
        response.sendRedirect("IniciarSesionControlador?accion=solicitarIngreso");
    }

    /** CU-02, paso 8a.2: descartar la información no confirmada. */
    private void descartarDatosNoConfirmados() {
        this.datosIngresados = null;
        this.documentosCargados = null;
    }

    /** Flujos 9a-13a: mantiene visible el formulario con la información previamente ingresada. */
    private void mantenerFormulario(String tipoCuenta, String mensaje)
            throws ServletException, IOException {
        request.setAttribute("mensajeError", mensaje);
        request.setAttribute("camposRequeridos", registroUsuarioService.obtenerDatosRequeridos(tipoCuenta));
        if ("ESPECIALISTA".equals(tipoCuenta)) {
            request.getRequestDispatcher("/Vistas/FormularioRegistroEspecialista.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/Vistas/FormularioRegistroFamiliar.jsp").forward(request, response);
        }
    }

    /** Lee los datos del formulario de registro (Familiar o Especialista). */
    private Map<String, String> leerDatosDelFormulario() {
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("tipoCuenta", request.getParameter("tipoCuenta"));
        datos.put("nombres", request.getParameter("nombres"));
        datos.put("apellidos", request.getParameter("apellidos"));
        datos.put("cedula", request.getParameter("cedula"));
        datos.put("ruc", request.getParameter("ruc"));
        datos.put("correo", request.getParameter("correo"));
        datos.put("provincia", request.getParameter("provincia"));
        datos.put("ciudad", request.getParameter("ciudad"));
        datos.put("direccionDeAtencion", request.getParameter("direccionDeAtencion"));
        datos.put("contrasena", request.getParameter("contrasena"));
        return datos;
    }

    /**
     * Lee las credenciales del formulario del Especialista: cada fila es
     * {tituloProfesional, documentoDeRespaldo}. Los archivos se guardan en /uploads.
     */
    private List<String[]> leerDocumentosDelFormulario() throws ServletException, IOException {
        List<String[]> credenciales = new ArrayList<String[]>();
        String[] titulos = request.getParameterValues("titulos");
        List<Part> archivos = new ArrayList<Part>();
        for (Part part : request.getParts()) {
            if ("documentos".equals(part.getName()) && part.getSize() > 0) {
                archivos.add(part);
            }
        }
        String rutaCarga = getServletContext().getRealPath("/uploads");
        File carpeta = new File(rutaCarga);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        for (int i = 0; i < archivos.size(); i++) {
            Part archivo = archivos.get(i);
            String titulo = (titulos != null && i < titulos.length) ? titulos[i] : null;
            String nombreArchivo = System.currentTimeMillis() + "_" + i + "_"
                    + archivo.getSubmittedFileName().replaceAll("[^A-Za-z0-9._-]", "_");
            archivo.write(rutaCarga + File.separator + nombreArchivo);
            credenciales.add(new String[]{titulo, "uploads/" + nombreArchivo});
        }
        return credenciales;
    }
}
