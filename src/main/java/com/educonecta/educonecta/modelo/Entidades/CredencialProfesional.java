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
import jakarta.persistence.Table;

/**
 * «Entity» CredencialProfesional — CU-02.
 * + CredencialProfesional(tituloProfesional:String, documentoDeRespaldo:String)
 * Relación «acredita» con Especialista (Modelo de Dominio).
 *
 * Mapeo ORM (EclipseLink):
 * - Tabla CREDENCIAL_PROFESIONAL con FK obligatoria al Especialista
 *   («1 acredita 1..*»: toda credencial pertenece a un Especialista).
 */
@Entity
@Table(name = "CREDENCIAL_PROFESIONAL")
public class CredencialProfesional implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    /** Título profesional — dato obligatorio del registro de Especialista (CU-02). */
    @Column(name = "TITULO_PROFESIONAL", nullable = false, length = 150)
    private String tituloProfesional;

    /** Documento que respalda el título — obligatorio (CU-02, paso 13); ruta del archivo cargado. */
    @Column(name = "DOCUMENTO_DE_RESPALDO", nullable = false, length = 255)
    private String documentoDeRespaldo;

    /** Lado propietario de «1 acredita 1..*» (Modelo de Dominio). */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ESPECIALISTA_ID", nullable = false)
    private Especialista especialista;

    public CredencialProfesional() {
    }

    /** Constructor del diagrama de clases CU-02 («create» por cada documento cargado). */
    public CredencialProfesional(String tituloProfesional, String documentoDeRespaldo) {
        this.tituloProfesional = tituloProfesional;
        this.documentoDeRespaldo = documentoDeRespaldo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTituloProfesional() {
        return tituloProfesional;
    }

    public void setTituloProfesional(String tituloProfesional) {
        this.tituloProfesional = tituloProfesional;
    }

    public String getDocumentoDeRespaldo() {
        return documentoDeRespaldo;
    }

    public void setDocumentoDeRespaldo(String documentoDeRespaldo) {
        this.documentoDeRespaldo = documentoDeRespaldo;
    }

    public Especialista getEspecialista() {
        return especialista;
    }

    public void setEspecialista(Especialista especialista) {
        this.especialista = especialista;
    }
}
