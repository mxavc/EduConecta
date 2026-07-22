package com.educonecta.educonecta.modelo.Entidades;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;

/**
 * «Entity» Especialista — CU-02 / CU-04 / CU-05.
 * Atributos propios: ruc, direccionDeAtencion (Modelo de Dominio).
 * Relación: 1 acredita 1..* CredencialProfesional (CU-02).
 *
 * Mapeo ORM (EclipseLink):
 * - Subclase de Usuario (herencia JOINED): tabla ESPECIALISTA unida a USUARIO por ID.
 * - Discriminador TIPO_CUENTA = ESPECIALISTA (CU-02, paso 2).
 * - RUC único (CU-02, paso 12: «verifica que el RUC no se encuentre registrado»).
 */
@Entity
@Table(name = "ESPECIALISTA", uniqueConstraints = {
        @UniqueConstraint(name = "UK_ESPECIALISTA_RUC", columnNames = "RUC")
})
@DiscriminatorValue("ESPECIALISTA")
@PrimaryKeyJoinColumn(name = "ID")
public class Especialista extends Usuario {

    /** RUC — obligatorio para el Especialista, 13 dígitos, único (CU-02, paso 12). */
    @Column(name = "RUC", nullable = false, length = 13)
    private String ruc;

    /** Dirección del consultorio o lugar de atención — dato obligatorio (CU-02). */
    @Column(name = "DIRECCION_DE_ATENCION", nullable = false, length = 200)
    private String direccionDeAtencion;

    /** Relación «1 acredita 1..*» con CredencialProfesional (CU-02 / Modelo de Dominio). */
    @OneToMany(mappedBy = "especialista", fetch = FetchType.EAGER)
    private List<CredencialProfesional> credenciales = new ArrayList<CredencialProfesional>();

    /** Constructor del diagrama de clases CU-04: + Especialista() */
    public Especialista() {
        super();
        setRol("ESPECIALISTA");
    }

    /**
     * Constructor del diagrama de clases CU-02 (paso 14: crear la cuenta tipo ESPECIALISTA):
     * + Especialista(nombres, apellidos, cedula, ruc, correo, provincia, ciudad, direccionDeAtencion, contrasena)
     */
    public Especialista(String nombres, String apellidos, String cedula, String ruc,
                        String correo, String provincia, String ciudad,
                        String direccionDeAtencion, String contrasena) {
        super(nombres, apellidos, cedula, correo, provincia, ciudad, contrasena, "ESPECIALISTA");
        this.ruc = ruc;
        this.direccionDeAtencion = direccionDeAtencion;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getDireccionDeAtencion() {
        return direccionDeAtencion;
    }

    public void setDireccionDeAtencion(String direccionDeAtencion) {
        this.direccionDeAtencion = direccionDeAtencion;
    }

    public List<CredencialProfesional> getCredenciales() {
        return credenciales;
    }

    public void setCredenciales(List<CredencialProfesional> credenciales) {
        this.credenciales = credenciales;
    }

    /**
     * Atributo derivado «titulosProfesionales» del diagrama de clases CU-04:
     * se obtiene de las credenciales profesionales que acreditan al Especialista.
     */
    @Transient
    public String getTitulosProfesionales() {
        StringBuilder titulos = new StringBuilder();
        for (CredencialProfesional credencial : credenciales) {
            if (titulos.length() > 0) {
                titulos.append(", ");
            }
            titulos.append(credencial.getTituloProfesional());
        }
        return titulos.toString();
    }
}
