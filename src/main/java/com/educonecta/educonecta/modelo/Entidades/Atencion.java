package com.educonecta.educonecta.modelo.Entidades;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * «Entity» Atencion — CU-05.
 * + Atencion(observaciones, informacionRelevante, recomendaciones, diagnostico,
 *            indicacionesDeSeguimiento, notasAdicionales)
 * + setCita(cita:Cita): void · + setEspecialista(especialista:Especialista): void
 * Relaciones: «1 genera 0..1» con Cita y «1 atiende 0..*» con Especialista.
 *
 * Mapeo ORM (EclipseLink):
 * - Tabla ATENCION con FKs obligatorias a la Cita y al Especialista
 *   («vincula la atención con la cita, el Especialista y el Perfil del Menor»
 *   — CU-05, paso 21; el vínculo con el menor queda dado a través de la cita).
 * - Campos obligatorios del CU-05 (paso 17): observaciones, información relevante
 *   y recomendaciones; diagnóstico, indicaciones y notas son «si aplica».
 */
@Entity
@Table(name = "ATENCION")
public class Atencion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    /** Observaciones de la atención — obligatorio (CU-05, paso 17). */
    @Lob
    @Column(name = "OBSERVACIONES", nullable = false)
    private String observaciones;

    /** Información relevante identificada — obligatorio (CU-05, paso 17). */
    @Lob
    @Column(name = "INFORMACION_RELEVANTE", nullable = false)
    private String informacionRelevante;

    /** Recomendaciones para el Familiar y el menor — obligatorio (CU-05, paso 17). */
    @Lob
    @Column(name = "RECOMENDACIONES", nullable = false)
    private String recomendaciones;

    /** Diagnóstico — «si aplica» (CU-05, paso 17): opcional. */
    @Lob
    @Column(name = "DIAGNOSTICO")
    private String diagnostico;

    /** Indicaciones de seguimiento — «si aplica» (CU-05, paso 17): opcional. */
    @Lob
    @Column(name = "INDICACIONES_DE_SEGUIMIENTO")
    private String indicacionesDeSeguimiento;

    /** Notas adicionales — «si aplica» (CU-05, paso 17): opcional. */
    @Lob
    @Column(name = "NOTAS_ADICIONALES")
    private String notasAdicionales;

    /** Vínculo con la cita que genera la atención («1 genera 0..1» — Modelo de Dominio). */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "CITA_ID", nullable = false)
    private Cita cita;

    /** Vínculo con el Especialista que atiende («1 atiende 0..*» — Modelo de Dominio). */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ESPECIALISTA_ID", nullable = false)
    private Especialista especialista;

    public Atencion() {
    }

    /** Constructor del diagrama de clases CU-05 (paso 20: «create» Atencion). */
    public Atencion(String observaciones, String informacionRelevante, String recomendaciones,
                    String diagnostico, String indicacionesDeSeguimiento, String notasAdicionales) {
        this.observaciones = observaciones;
        this.informacionRelevante = informacionRelevante;
        this.recomendaciones = recomendaciones;
        this.diagnostico = diagnostico;
        this.indicacionesDeSeguimiento = indicacionesDeSeguimiento;
        this.notasAdicionales = notasAdicionales;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getInformacionRelevante() {
        return informacionRelevante;
    }

    public void setInformacionRelevante(String informacionRelevante) {
        this.informacionRelevante = informacionRelevante;
    }

    public String getRecomendaciones() {
        return recomendaciones;
    }

    public void setRecomendaciones(String recomendaciones) {
        this.recomendaciones = recomendaciones;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getIndicacionesDeSeguimiento() {
        return indicacionesDeSeguimiento;
    }

    public void setIndicacionesDeSeguimiento(String indicacionesDeSeguimiento) {
        this.indicacionesDeSeguimiento = indicacionesDeSeguimiento;
    }

    public String getNotasAdicionales() {
        return notasAdicionales;
    }

    public void setNotasAdicionales(String notasAdicionales) {
        this.notasAdicionales = notasAdicionales;
    }

    public Cita getCita() {
        return cita;
    }

    /** CU-05, paso 21: vincular la atención con la cita. */
    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public Especialista getEspecialista() {
        return especialista;
    }

    /** CU-05, paso 21: vincular la atención con el Especialista. */
    public void setEspecialista(Especialista especialista) {
        this.especialista = especialista;
    }
}
