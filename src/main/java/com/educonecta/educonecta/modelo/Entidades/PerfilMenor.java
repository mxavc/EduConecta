package com.educonecta.educonecta.modelo.Entidades;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * «Entity» PerfilMenor — CU-03 / CU-04 / CU-05.
 * + PerfilMenor(nombres, apellidos, cedula, edad, anioEscolar, provincia, ciudad, diagnosticoOCondicion)
 * + setFamiliar(familiar:Familiar): void
 * + setHistorialAtencion(historial:HistorialAtencion): void
 * Relaciones: «1 registra y representa 0..*» con Familiar y «1 posee 1» con HistorialAtencion.
 *
 * Mapeo ORM (EclipseLink):
 * - Tabla PERFIL_MENOR con FK obligatoria al Familiar y FK única al Historial.
 * - Restricción única (FAMILIAR_ID, CEDULA): «el menor no se encuentre registrado
 *   previamente por el Familiar» (CU-03, paso 12).
 */
@Entity
@Table(name = "PERFIL_MENOR", uniqueConstraints = {
        @UniqueConstraint(name = "UK_MENOR_FAMILIAR_CEDULA", columnNames = {"FAMILIAR_ID", "CEDULA"})
})
public class PerfilMenor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    /** Nombres completos — dato obligatorio (CU-03, paso 9). */
    @Column(name = "NOMBRES_COMPLETOS", nullable = false, length = 100)
    private String nombresCompletos;

    /** Apellidos completos — dato obligatorio (CU-03, paso 9). */
    @Column(name = "APELLIDOS_COMPLETOS", nullable = false, length = 100)
    private String apellidosCompletos;

    /** Cédula del menor — obligatoria, 10 dígitos (CU-03, paso 9). */
    @Column(name = "CEDULA", nullable = false, length = 10)
    private String cedula;

    /** Edad — dato obligatorio (CU-03, paso 9). */
    @Column(name = "EDAD", nullable = false)
    private int edad;

    /** Año escolar de referencia — dato obligatorio (CU-03, paso 9). */
    @Column(name = "ANIO_ESCOLAR", nullable = false, length = 80)
    private String anioEscolar;

    /** Provincia — dato obligatorio (CU-03, paso 9). */
    @Column(name = "PROVINCIA", nullable = false, length = 80)
    private String provincia;

    /** Ciudad — dato obligatorio (CU-03, paso 9). */
    @Column(name = "CIUDAD", nullable = false, length = 80)
    private String ciudad;

    /** Diagnóstico o condición — «si aplica» (CU-03, paso 9): opcional. */
    @Column(name = "DIAGNOSTICO_O_CONDICION", length = 255)
    private String diagnosticoOCondicion;

    /** Lado propietario de «1 registra y representa 0..*» (CU-03, paso 14). */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "FAMILIAR_ID", nullable = false)
    private Familiar familiar;

    /** Lado propietario de «1 posee 1» (CU-03, paso 13): FK única al historial. */
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "HISTORIAL_ATENCION_ID", nullable = false, unique = true)
    private HistorialAtencion historialAtencion;

    /** Constructor del diagrama de clases CU-04: + PerfilMenor() */
    public PerfilMenor() {
    }

    /** Constructor del diagrama de clases CU-03 (paso 13: «create» PerfilMenor). */
    public PerfilMenor(String nombres, String apellidos, String cedula, int edad,
                       String anioEscolar, String provincia, String ciudad,
                       String diagnosticoOCondicion) {
        this.nombresCompletos = nombres;
        this.apellidosCompletos = apellidos;
        this.cedula = cedula;
        this.edad = edad;
        this.anioEscolar = anioEscolar;
        this.provincia = provincia;
        this.ciudad = ciudad;
        this.diagnosticoOCondicion = diagnosticoOCondicion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombresCompletos() {
        return nombresCompletos;
    }

    public void setNombresCompletos(String nombresCompletos) {
        this.nombresCompletos = nombresCompletos;
    }

    public String getApellidosCompletos() {
        return apellidosCompletos;
    }

    public void setApellidosCompletos(String apellidosCompletos) {
        this.apellidosCompletos = apellidosCompletos;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getAnioEscolar() {
        return anioEscolar;
    }

    public void setAnioEscolar(String anioEscolar) {
        this.anioEscolar = anioEscolar;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDiagnosticoOCondicion() {
        return diagnosticoOCondicion;
    }

    public void setDiagnosticoOCondicion(String diagnosticoOCondicion) {
        this.diagnosticoOCondicion = diagnosticoOCondicion;
    }

    public Familiar getFamiliar() {
        return familiar;
    }

    /** CU-03, paso 14: vincular el Perfil del Menor con el Familiar. */
    public void setFamiliar(Familiar familiar) {
        this.familiar = familiar;
    }

    public HistorialAtencion getHistorialAtencion() {
        return historialAtencion;
    }

    /** CU-03, paso 13: el perfil posee su HistorialAtencion («1 posee 1»). */
    public void setHistorialAtencion(HistorialAtencion historialAtencion) {
        this.historialAtencion = historialAtencion;
    }
}
