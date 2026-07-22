package com.educonecta.educonecta.modelo.Entidades;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

/**
 * «Entity» Familiar — CU-02.
 * + Familiar(nombres, apellidos, cedula, correo, provincia, ciudad, contrasena)
 *
 * Mapeo ORM (EclipseLink):
 * - Subclase de Usuario (herencia JOINED): tabla FAMILIAR unida a USUARIO por ID.
 * - Discriminador TIPO_CUENTA = FAMILIAR (CU-02, paso 2).
 */
@Entity
@Table(name = "FAMILIAR")
@DiscriminatorValue("FAMILIAR")
@PrimaryKeyJoinColumn(name = "ID")
public class Familiar extends Usuario {

    public Familiar() {
        super();
        setRol("FAMILIAR");
    }

    /** Constructor del diagrama de clases CU-02 (paso 14: crear la cuenta tipo FAMILIAR). */
    public Familiar(String nombres, String apellidos, String cedula, String correo,
                    String provincia, String ciudad, String contrasena) {
        super(nombres, apellidos, cedula, correo, provincia, ciudad, contrasena, "FAMILIAR");
    }
}
