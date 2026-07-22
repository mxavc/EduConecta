package com.educonecta.educonecta.modelo.Servicios;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.educonecta.educonecta.modelo.DAO.FamiliarDAO;
import com.educonecta.educonecta.modelo.DAO.HistorialAtencionDAO;
import com.educonecta.educonecta.modelo.DAO.PerfilMenorDAO;
import com.educonecta.educonecta.modelo.Entidades.Familiar;
import com.educonecta.educonecta.modelo.Entidades.HistorialAtencion;
import com.educonecta.educonecta.modelo.Entidades.PerfilMenor;

/**
 * «Service» PerfilMenorService — CU-03: Registrar Perfil de Menor.
 * + obtenerFamiliar(idFamiliar:int): Familiar
 * + buscarPerfilesPorFamiliar(idFamiliar:int): List
 * + verificarCamposYValidez(datos:Map): boolean
 * + verificarMenorNoRegistrado(idFamiliar:int, cedula:String): boolean
 * + crearPerfilMenor(datos:Map): PerfilMenor
 * + vincularConFamiliar(perfil:PerfilMenor, familiar:Familiar): void
 */
public class PerfilMenorService {

    private FamiliarDAO familiarDAO = new FamiliarDAO();
    private PerfilMenorDAO perfilMenorDAO = new PerfilMenorDAO();
    private HistorialAtencionDAO historialAtencionDAO = new HistorialAtencionDAO();

    /** CU-03, paso 2: obtener la información del Familiar para su página principal. */
    public Familiar obtenerFamiliar(int idFamiliar) {
        return familiarDAO.buscarPorId(idFamiliar);
    }

    /** CU-03, paso 5: buscar los perfiles de menores vinculados con el Familiar. */
    public List<PerfilMenor> buscarPerfilesPorFamiliar(int idFamiliar) {
        return perfilMenorDAO.buscarPorFamiliar(idFamiliar);
    }

    /** CU-03, paso 11: verificar campos obligatorios completos e información válida. */
    public boolean verificarCamposYValidez(Map<String, String> datos) {
        // Diagnóstico o condición es «si aplica»: no es obligatorio.
        for (String campo : Arrays.asList("nombres", "apellidos", "cedula", "edad",
                "anioEscolar", "provincia", "ciudad")) {
            String valor = datos.get(campo);
            if (valor == null || valor.trim().isEmpty()) {
                return false;
            }
        }
        if (!datos.get("cedula").matches("\\d{10}")) {
            return false;
        }
        try {
            int edad = Integer.parseInt(datos.get("edad").trim());
            return edad >= 1 && edad <= 17;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * CU-03, paso 12: verificar que el menor no se encuentre registrado por el Familiar.
     * Retorna menorRegistrado (true si ya existe — flujo 12a).
     */
    public boolean verificarMenorNoRegistrado(int idFamiliar, String cedula) {
        return perfilMenorDAO.existePorFamiliarYCedula(idFamiliar, cedula);
    }

    /**
     * CU-03, paso 13: crear el Perfil del Menor.
     * «create» PerfilMenor(...) + «create» HistorialAtencion() + setHistorialAtencion(historial)
     * (relación «1 posee 1» del Modelo de Dominio).
     */
    public PerfilMenor crearPerfilMenor(Map<String, String> datos) {
        PerfilMenor perfil = new PerfilMenor(
                datos.get("nombres"), datos.get("apellidos"), datos.get("cedula"),
                Integer.parseInt(datos.get("edad").trim()), datos.get("anioEscolar"),
                datos.get("provincia"), datos.get("ciudad"), datos.get("diagnosticoOCondicion"));
        HistorialAtencion historial = new HistorialAtencion();
        perfil.setHistorialAtencion(historial);
        return perfil;
    }

    /**
     * CU-03, paso 14: vincular el Perfil del Menor con el Familiar y registrarlos:
     * setFamiliar(familiar) → insertar(historial) → insertar(perfil).
     */
    public void vincularConFamiliar(PerfilMenor perfil, Familiar familiar) {
        perfil.setFamiliar(familiar);
        historialAtencionDAO.insertar(perfil.getHistorialAtencion());
        perfilMenorDAO.insertar(perfil);
    }
}
