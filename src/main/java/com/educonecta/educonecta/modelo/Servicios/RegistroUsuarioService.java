package com.educonecta.educonecta.modelo.Servicios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.educonecta.educonecta.modelo.DAO.CredencialProfesionalDAO;
import com.educonecta.educonecta.modelo.DAO.EspecialistaDAO;
import com.educonecta.educonecta.modelo.DAO.FamiliarDAO;
import com.educonecta.educonecta.modelo.DAO.UsuarioDAO;
import com.educonecta.educonecta.modelo.Entidades.CredencialProfesional;
import com.educonecta.educonecta.modelo.Entidades.Especialista;
import com.educonecta.educonecta.modelo.Entidades.Familiar;
import com.educonecta.educonecta.modelo.Entidades.Usuario;

/**
 * «Service» RegistroUsuarioService — CU-02: Registrar Usuario.
 * + obtenerDatosRequeridos(tipoCuenta:String): List
 * + verificarCamposYFormato(datos:Map): boolean
 * + verificarCorreoNoRegistrado(correo:String): boolean
 * + verificarCedulaNoRegistrada(cedula:String): boolean
 * + verificarRucNoRegistrado(ruc:String): boolean
 * + verificarDocumentosCargados(documentos:List): boolean
 * + crearCuenta(tipoCuenta:String, datos:Map): Usuario
 * + registrarInformacion(familiar:Familiar): void
 * + registrarInformacion(especialista:Especialista, credenciales:List): void
 */
public class RegistroUsuarioService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private FamiliarDAO familiarDAO = new FamiliarDAO();
    private EspecialistaDAO especialistaDAO = new EspecialistaDAO();
    private CredencialProfesionalDAO credencialProfesionalDAO = new CredencialProfesionalDAO();

    /** CU-02, paso 4: obtener los datos requeridos para el tipo de cuenta seleccionado. */
    public List<String> obtenerDatosRequeridos(String tipoCuenta) {
        if ("ESPECIALISTA".equals(tipoCuenta)) {
            return Arrays.asList(
                    "Nombres completos", "Apellidos completos", "Número de cédula", "RUC",
                    "Correo electrónico", "Provincia", "Ciudad",
                    "Dirección del consultorio o lugar de atención",
                    "Título o títulos profesionales", "Contraseña",
                    "Documentos que respalden sus títulos y credenciales profesionales");
        }
        return Arrays.asList(
                "Nombres completos", "Apellidos completos", "Número de cédula",
                "Correo electrónico", "Provincia", "Ciudad", "Contraseña");
    }

    /**
     * CU-02, paso 9: verificar que los campos obligatorios estén completos
     * y que la información tenga un formato válido.
     */
    public boolean verificarCamposYFormato(Map<String, String> datos) {
        String tipoCuenta = datos.get("tipoCuenta");
        List<String> obligatorios = new ArrayList<String>(
                Arrays.asList("nombres", "apellidos", "cedula", "correo", "provincia", "ciudad", "contrasena"));
        if ("ESPECIALISTA".equals(tipoCuenta)) {
            obligatorios.add("ruc");
            obligatorios.add("direccionDeAtencion");
        }
        for (String campo : obligatorios) {
            String valor = datos.get(campo);
            if (valor == null || valor.trim().isEmpty()) {
                return false;
            }
        }
        // Formatos: cédula de 10 dígitos, RUC de 13 dígitos, correo válido, contraseña mínima.
        if (!datos.get("cedula").matches("\\d{10}")) {
            return false;
        }
        if ("ESPECIALISTA".equals(tipoCuenta) && !datos.get("ruc").matches("\\d{13}")) {
            return false;
        }
        if (!datos.get("correo").matches("^[\\w.+-]+@[\\w-]+\\.[\\w.]+$")) {
            return false;
        }
        return datos.get("contrasena").length() >= 6;
    }

    /**
     * CU-02, paso 10: verificar que el correo electrónico no se encuentre registrado.
     * Retorna correoRegistrado (true si pertenece a otra cuenta — flujo 10a).
     */
    public boolean verificarCorreoNoRegistrado(String correo) {
        return usuarioDAO.existeCorreo(correo);
    }

    /**
     * CU-02, paso 11: verificar que el número de cédula no se encuentre registrado.
     * Retorna cedulaRegistrada (true si pertenece a otra cuenta — flujo 11a).
     */
    public boolean verificarCedulaNoRegistrada(String cedula) {
        return usuarioDAO.existeCedula(cedula);
    }

    /**
     * CU-02, paso 12: verificar que el RUC no se encuentre registrado.
     * Retorna rucRegistrado (true si pertenece a otro Especialista — flujo 12a).
     */
    public boolean verificarRucNoRegistrado(String ruc) {
        return especialistaDAO.existeRuc(ruc);
    }

    /** CU-02, paso 13: verificar que los documentos requeridos hayan sido cargados. */
    public boolean verificarDocumentosCargados(List<String[]> documentos) {
        if (documentos == null || documentos.isEmpty()) {
            return false;
        }
        for (String[] credencial : documentos) {
            if (credencial[0] == null || credencial[0].trim().isEmpty()
                    || credencial[1] == null || credencial[1].trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /** CU-02, paso 14: crear la cuenta con el tipo seleccionado. */
    public Usuario crearCuenta(String tipoCuenta, Map<String, String> datos) {
        String contrasenaProtegida = AutenticacionService.calcularHash(datos.get("contrasena"));
        if ("ESPECIALISTA".equals(tipoCuenta)) {
            // «create» Especialista(nombres, apellidos, cedula, ruc, correo, provincia,
            //                       ciudad, direccionDeAtencion, contrasena)
            return new Especialista(
                    datos.get("nombres"), datos.get("apellidos"), datos.get("cedula"),
                    datos.get("ruc"), datos.get("correo"), datos.get("provincia"),
                    datos.get("ciudad"), datos.get("direccionDeAtencion"), contrasenaProtegida);
        }
        // «create» Familiar(nombres, apellidos, cedula, correo, provincia, ciudad, contrasena)
        return new Familiar(
                datos.get("nombres"), datos.get("apellidos"), datos.get("cedula"),
                datos.get("correo"), datos.get("provincia"), datos.get("ciudad"), contrasenaProtegida);
    }

    /** CU-02, paso 15: registrar la información del Familiar. */
    public void registrarInformacion(Familiar familiar) {
        familiarDAO.insertar(familiar);
    }

    /**
     * CU-02, paso 15: registrar la información del Especialista y sus credenciales.
     * Por cada documento: «create» CredencialProfesional(tituloProfesional, documentoDeRespaldo).
     */
    public void registrarInformacion(Especialista especialista, List<String[]> credenciales) {
        especialistaDAO.insertar(especialista);
        for (String[] datosCredencial : credenciales) {
            CredencialProfesional credencial =
                    new CredencialProfesional(datosCredencial[0], datosCredencial[1]);
            credencial.setEspecialista(especialista);
            credencialProfesionalDAO.insertar(credencial);
        }
    }
}
