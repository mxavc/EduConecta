package com.educonecta.educonecta.modelo.Servicios;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.educonecta.educonecta.modelo.DAO.UsuarioDAO;
import com.educonecta.educonecta.modelo.Entidades.Usuario;

/**
 * «Service» AutenticacionService — CU-01: Iniciar Sesión.
 * + verificarCamposObligatorios(correo:String, contrasena:String): boolean
 * + buscarCuentaPorCorreo(correo:String): Usuario
 * + comprobarContrasena(usuario:Usuario, contrasena:String): boolean
 * + obtenerDatosSesion(usuario:Usuario): Usuario
 */
public class AutenticacionService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    /** CU-01, paso 5: verificar que los campos obligatorios estén completos. */
    public boolean verificarCamposObligatorios(String correo, String contrasena) {
        return correo != null && !correo.trim().isEmpty()
                && contrasena != null && !contrasena.trim().isEmpty();
    }

    /** CU-01, paso 6: buscar la cuenta asociada con el correo electrónico ingresado. */
    public Usuario buscarCuentaPorCorreo(String correo) {
        return usuarioDAO.buscarPorCorreo(correo.trim());
    }

    /** CU-01, paso 7: comprobar que la contraseña corresponda a la cuenta encontrada. */
    public boolean comprobarContrasena(Usuario usuario, String contrasena) {
        return usuario.getContrasena() != null
                && usuario.getContrasena().equals(calcularHash(contrasena));
    }

    /** CU-01, paso 9: obtener la información del Usuario necesaria para iniciar su sesión. */
    public Usuario obtenerDatosSesion(Usuario usuario) {
        return usuario;
    }

    /** Las contraseñas se almacenan con hash SHA-256 (robustez: nunca en texto plano). */
    static String calcularHash(String contrasena) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(contrasena.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : bytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algoritmo SHA-256 no disponible", e);
        }
    }
}
