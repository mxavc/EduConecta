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
import jakarta.persistence.UniqueConstraint;

/**
 * «Entity» Calendario — CU-04.
 * + Calendario() · + isDisponible(): boolean
 * Representa la disponibilidad (fecha, hora, estado) que el Especialista establece
 * (Modelo de Dominio: Calendario «establece» Disponibilidad).
 * disponible = false ⇒ horario RESERVADO (CU-04, paso 20).
 *
 * Mapeo ORM (EclipseLink):
 * - Tabla CALENDARIO con FK obligatoria al Especialista («usa» — Modelo de Dominio).
 * - Restricción única (ESPECIALISTA_ID, FECHA, HORA): cada Disponibilidad es un
 *   horario único del Especialista.
 */
@Entity
@Table(name = "CALENDARIO", uniqueConstraints = {
        @UniqueConstraint(name = "UK_CALENDARIO_HORARIO",
                columnNames = {"ESPECIALISTA_ID", "FECHA", "HORA"})
})
public class Calendario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    /** Fecha de la disponibilidad (Modelo de Dominio: Disponibilidad.fecha). */
    @Column(name = "FECHA", nullable = false)
    private Date fecha;

    /** Hora de la disponibilidad (Modelo de Dominio: Disponibilidad.hora). */
    @Column(name = "HORA", nullable = false)
    private Time hora;

    /** Estado de la disponibilidad: true=disponible · false=RESERVADO (CU-04, paso 20). */
    @Column(name = "DISPONIBLE", nullable = false)
    private boolean disponible;

    /** Especialista que establece la disponibilidad (Modelo de Dominio: «establece»). */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ESPECIALISTA_ID", nullable = false)
    private Especialista especialista;

    /** Constructor del diagrama de clases CU-04: + Calendario() */
    public Calendario() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    /** CU-04, diagrama de clases: + isDisponible(): boolean */
    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public Especialista getEspecialista() {
        return especialista;
    }

    public void setEspecialista(Especialista especialista) {
        this.especialista = especialista;
    }
}
