package com.educonecta.educonecta.modelo.DAO;

import java.util.List;

import com.educonecta.educonecta.modelo.Entidades.Atencion;
import com.educonecta.educonecta.modelo.Entidades.Cita;
import com.educonecta.educonecta.modelo.Entidades.Especialista;
import com.educonecta.educonecta.util.JPAUtil;

import jakarta.persistence.EntityManager;

/**
 * «DAO» AtencionDAO — CU-05.
 * + buscarPorHistorial(idHistorial:int): List
 * + insertar(atencion:Atencion): Atencion
 */
public class AtencionDAO {

    private EntityManager em;

    /** CU-05, paso 13: obtener los registros de atenciones anteriores contenidos en el historial. */
    public List<Atencion> buscarPorHistorial(int idHistorial) {
        em = JPAUtil.crearEntityManager();
        try {
            return em.createQuery(
                    "SELECT a FROM HistorialAtencion h JOIN h.atenciones a WHERE h.id = :idHistorial "
                            + "ORDER BY a.id DESC",
                    Atencion.class)
                    .setParameter("idHistorial", idHistorial)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /** CU-05, paso 24: «persist» em.persist(atencion) */
    public Atencion insertar(Atencion atencion) {
        em = JPAUtil.crearEntityManager();
        try {
            em.getTransaction().begin();
            // Reasociar los vínculos de la atención dentro de este contexto de persistencia.
            if (atencion.getCita() != null) {
                atencion.setCita(em.getReference(Cita.class, atencion.getCita().getId()));
            }
            if (atencion.getEspecialista() != null) {
                atencion.setEspecialista(
                        em.getReference(Especialista.class, atencion.getEspecialista().getId()));
            }
            em.persist(atencion);
            em.getTransaction().commit();
            return atencion;
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }
}
