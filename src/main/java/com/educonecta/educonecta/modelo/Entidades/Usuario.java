package com.educonecta.educonecta.modelo.Entidades;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * «Entity» Usuario — CU-01 / CU-02.
 * Superclase de Familiar y Especialista (Modelo de Dominio).
 *
 * Mapeo ORM (EclipseLink):
 * - Herencia JOINED: tabla USUARIO + tabla por subclase (Padre de Familia /
 *   Especialista heredan de Usuario en el Modelo de Dominio).
 * - Discriminador TIPO_CUENTA: «tipos de cuenta disponibles: Familiar y
 *   Especialista» (CU-02, paso 2).
 * - CORREO_ELECTRONICO único (CU-02, paso 10) y NUMERO_CEDULA único
 *   (CU-02, paso 11).
 */
@Entity
@Table(name = "USUARIO", uniqueConstraints = {
        @UniqueConstraint(name = "UK_USUARIO_CORREO", columnNames = "CORREO_ELECTRONICO"),
        @UniqueConstraint(name = "UK_USUARIO_CEDULA", columnNames = "NUMERO_CEDULA")
})
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TIPO_CUENTA", length = 20)
public abstract class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    /** Nombres completos — dato obligatorio del registro (CU-02). */
    @Column(name = "NOMBRES_COMPLETOS", nullable = false, length = 100)
    private String nombresCompletos;

    /** Apellidos completos — dato obligatorio del registro (CU-02). */
    @Column(name = "APELLIDOS_COMPLETOS", nullable = false, length = 100)
    private String apellidosCompletos;

    /** Número de cédula — obligatorio, 10 dígitos, único (CU-02, paso 11). */
    @Column(name = "NUMERO_CEDULA", nullable = false, length = 10)
    private String numeroCedula;

    /** Correo electrónico — obligatorio, único (CU-02, paso 10; CU-01, paso 6). */
    @Column(name = "CORREO_ELECTRONICO", nullable = false, length = 150)
    private String correoElectronico;

    /** Provincia — dato obligatorio del registro (CU-02). */
    @Column(name = "PROVINCIA", nullable = false, length = 80)
    private String provincia;

    /** Ciudad — dato obligatorio del registro (CU-02). */
    @Column(name = "CIUDAD", nullable = false, length = 80)
    private String ciudad;

    /** Contraseña — obligatoria; se almacena su hash SHA-256 (64 caracteres hex). */
    @Column(name = "CONTRASENA", nullable = false, length = 64)
    private String contrasena;

    /** Rol asociado con la cuenta: FAMILIAR o ESPECIALISTA (CU-01, paso 8). */
    @Column(name = "ROL", nullable = false, length = 20)
    private String rol;

    public Usuario() {
    }

    public Usuario(String nombresCompletos, String apellidosCompletos, String numeroCedula,
                   String correoElectronico, String provincia, String ciudad,
                   String contrasena, String rol) {
        this.nombresCompletos = nombresCompletos;
        this.apellidosCompletos = apellidosCompletos;
        this.numeroCedula = numeroCedula;
        this.correoElectronico = correoElectronico;
        this.provincia = provincia;
        this.ciudad = ciudad;
        this.contrasena = contrasena;
        this.rol = rol;
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

    public String getNumeroCedula() {
        return numeroCedula;
    }

    public void setNumeroCedula(String numeroCedula) {
        this.numeroCedula = numeroCedula;
    }

    /** CU-01, diagrama de clases: + getCorreoElectronico(): String */
    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /** CU-01, diagrama de clases: + getRol(): String — paso 8, identifica el rol asociado. */
    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
