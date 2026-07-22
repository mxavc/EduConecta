package com.educonecta.educonecta.modelo.DAO;

import java.util.List;

import com.educonecta.educonecta.modelo.Entidades.HistorialAtencion;
import com.educonecta.educonecta.util.JPAUtil;

import jakarta.persistence.EntityManager;

/**
 * «DAO» HistorialAtencionDAO — CU-03 / CU-05.
 * + insertar(historial:HistorialAtencion): HistorialAtencion
 * + buscarPorPerfilMenor(idPerfilMenor:int): HistorialAtencion
 * + actualizar(historial:HistorialAtencion): HistorialAtencion
 */
public class HistorialAtencionDAO {

    private EntityManager em;

    /** CU-03, paso 14: «persist» em.persist(historial) */
    public HistorialAtencion insertar(HistorialAtencion historial) {
        em = JPAUtil.crearEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(historial);
            em.getTransaction().commit();
            return historial;
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    /** CU-05, paso 12: «query» SELECT h FROM HistorialAtencion h WHERE h.perfilMenor.id = :idPerfilMenor */
    public HistorialAtencion buscarPorPerfilMenor(int idPerfilMenor) {
        em = JPAUtil.crearEntityManager();
        try {
            List<HistorialAtencion> resultado = em.createQuery(
                    "SELECT h FROM HistorialAtencion h WHERE h.perfilMenor.id = :idPerfilMenor",
                    HistorialAtencion.class)
                    .setParameter("idPerfilMenor", idPerfilMenor)
                    .getResultList();
            return resultado.isEmpty() ? null : resultado.get(0);
        } finally {
            em.close();
        }
    }

    /** CU-05, paso 24: registrar los cambios del historial — «merge» em.merge(historial) */
    public HistorialAtencion actualizar(HistorialAtencion historial) {
        em = JPAUtil.crearEntityManager();
        try {
            em.getTransaction().begin();
            HistorialAtencion actualizado = em.merge(historial);
            em.getTransaction().commit();
            return actualizado;
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }
}
