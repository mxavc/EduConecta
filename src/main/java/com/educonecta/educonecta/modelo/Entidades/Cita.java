package com.educonecta.educonecta.modelo.Entidades;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

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
 * «Entity» Cita — CU-04 / CU-05.
 * + Cita(fecha:Date, hora:Time)
 * + getFecha(): Date · + getHora(): Time
 * + setEstado(estado:String): void · + setObservacionInasistencia(observacion:String): void
 * Estados: AGENDADA · ATENDIDA · NO_ASISTIDA.
 *
 * Mapeo ORM (EclipseLink):
 * - Tabla CITA con FKs obligatorias al Familiar, al PerfilMenor y al Especialista:
 *   «vincula la cita con el Familiar, el Perfil del Menor, el Especialista y el
 *   horario seleccionado» (CU-04, paso 19).
 * - OBSERVACION_INASISTENCIA opcional: solo existe en el flujo 7a del CU-05.
 */
@Entity
@Table(name = "CITA")
public class Cita implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    /** Fecha seleccionada del agendamiento — obligatoria (CU-04, paso 14). */
    @Column(name = "FECHA", nullable = false)
    private Date fecha;

    /** Hora seleccionada del agendamiento — obligatoria (CU-04, paso 14). */
    @Column(name = "HORA", nullable = false)
    private Time hora;

    /** Estado de la cita: AGENDADA (CU-04, paso 18) · ATENDIDA · NO_ASISTIDA (CU-05). */
    @Column(name = "ESTADO", nullable = false, length = 20)
    private String estado;

    /** Observación de la inasistencia — solo en el flujo 7a del CU-05: opcional. */
    @Column(name = "OBSERVACION_INASISTENCIA", length = 500)
    private String observacionInasistencia;

    /** Vínculo con el Familiar que agenda («agendar» — Modelo de Dominio). */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "FAMILIAR_ID", nullable = false)
    private Familiar familiar;

    /** Vínculo con el Perfil del Menor que recibe la cita («recibe» — Modelo de Dominio). */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "PERFIL_MENOR_ID", nullable = false)
    private PerfilMenor perfilMenor;

    /** Vínculo con el Especialista que atiende («atiende» — Modelo de Dominio). */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ESPECIALISTA_ID", nullable = false)
    private Especialista especialista;

    public Cita() {
    }

    /** Constructor del diagrama de clases CU-04 («create» Cita(fecha, hora)). */
    public Cita(Date fecha, Time hora) {
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /** CU-04, diagrama de clases: + getFecha(): Date */
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /** CU-04, diagrama de clases: + getHora(): Time */
    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    /** CU-05: actualizar el estado de la cita (ATENDIDA — paso 23 · NO_ASISTIDA — 7a.12). */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacionInasistencia() {
        return observacionInasistencia;
    }

    /** CU-05, paso 7a.9: crear el registro de inasistencia sobre la cita. */
    public void setObservacionInasistencia(String observacionInasistencia) {
        this.observacionInasistencia = observacionInasistencia;
    }

    public Familiar getFamiliar() {
        return familiar;
    }

    public void setFamiliar(Familiar familiar) {
        this.familiar = familiar;
    }

    public PerfilMenor getPerfilMenor() {
        return perfilMenor;
    }

    public void setPerfilMenor(PerfilMenor perfilMenor) {
        this.perfilMenor = perfilMenor;
    }

    public Especialista getEspecialista() {
        return especialista;
    }

    public void setEspecialista(Especialista especialista) {
        this.especialista = especialista;
    }
}
