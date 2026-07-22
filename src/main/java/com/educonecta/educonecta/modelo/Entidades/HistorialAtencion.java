package com.educonecta.educonecta.modelo.Entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * «Entity» HistorialAtencion — CU-03 / CU-05.
 * + HistorialAtencion()
 * + agregarAtencion(atencion:Atencion): void
 * + registrarInasistencia(cita:Cita): void
 * Relaciones del Modelo de Dominio: «1 contiene 0..*» Atencion y «1 registra 0..*» Cita.
 *
 * Mapeo ORM (EclipseLink):
 * - Tabla HISTORIAL_ATENCION.
 * - perfilMenor: lado inverso de «1 posee 1» (la FK la posee PERFIL_MENOR — CU-03).
 * - atenciones: FK HISTORIAL_ID en la tabla ATENCION («1 contiene 0..*»).
 * - inasistencias: FK HISTORIAL_INASISTENCIA_ID en la tabla CITA («1 registra 0..*»).
 */
@Entity
@Table(name = "HISTORIAL_ATENCION")
public class HistorialAtencion implements Serializable {

    /**
     * Estrategia TABLE (no IDENTITY): la tabla HISTORIAL_ATENCION solo posee la
     * columna ID (el diagrama no define más atributos propios) y EclipseLink no
     * admite un INSERT sin campos; con TABLE el id se asigna antes del insert y
     * la columna ID se incluye en la sentencia.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "ID")
    private int id;

    /** Lado inverso de «1 posee 1»: la FK la posee PerfilMenor (CU-03). */
    @OneToOne(mappedBy = "historialAtencion", fetch = FetchType.EAGER)
    private PerfilMenor perfilMenor;

    /** «1 contiene 0..*» — registros de atenciones del historial (CU-05, paso 13). */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "HISTORIAL_ID")
    private List<Atencion> atenciones = new ArrayList<Atencion>();

    /** «1 registra 0..*» — citas con inasistencia registradas en el historial (CU-05, 7a). */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "HISTORIAL_INASISTENCIA_ID")
    private List<Cita> inasistencias = new ArrayList<Cita>();

    /** Constructor del diagrama de clases CU-03 («create» HistorialAtencion). */
    public HistorialAtencion() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PerfilMenor getPerfilMenor() {
        return perfilMenor;
    }

    public void setPerfilMenor(PerfilMenor perfilMenor) {
        this.perfilMenor = perfilMenor;
    }

    public List<Atencion> getAtenciones() {
        return atenciones;
    }

    public void setAtenciones(List<Atencion> atenciones) {
        this.atenciones = atenciones;
    }

    public List<Cita> getInasistencias() {
        return inasistencias;
    }

    public void setInasistencias(List<Cita> inasistencias) {
        this.inasistencias = inasistencias;
    }

    /** CU-05, paso 22: incorporar la atención al historial del menor. */
    public void agregarAtencion(Atencion atencion) {
        this.atenciones.add(atencion);
    }

    /** CU-05, paso 7a.11: incorporar la inasistencia al historial del menor. */
    public void registrarInasistencia(Cita cita) {
        this.inasistencias.add(cita);
    }
}
