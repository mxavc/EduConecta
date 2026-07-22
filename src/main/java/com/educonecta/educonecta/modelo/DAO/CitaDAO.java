package com.educonecta.educonecta.modelo.DAO;

import java.util.List;

import com.educonecta.educonecta.modelo.Entidades.Cita;
import com.educonecta.educonecta.modelo.Entidades.Especialista;
import com.educonecta.educonecta.modelo.Entidades.Familiar;
import com.educonecta.educonecta.modelo.Entidades.PerfilMenor;
import com.educonecta.educonecta.util.JPAUtil;

import jakarta.persistence.EntityManager;

/**
 * «DAO» CitaDAO — CU-04 / CU-05.
 * + insertar(cita:Cita): Cita
 * + buscarPorFamiliar(idFamiliar:int): List
 * + buscarPorEspecialista(idEspecialista:int): List
 * + buscarPorId(idCita:int): Cita
 * + actualizar(cita:Cita): Cita
 */
public class CitaDAO {

    private EntityManager em;

    /** CU-04, paso 21: registrar la cita — «persist» em.persist(cita) */
    public Cita insertar(Cita cita) {
        em = JPAUtil.crearEntityManager();
        try {
            em.getTransaction().begin();
            // Reasociar los vínculos de la cita dentro de este contexto de persistencia.
            if (cita.getFamiliar() != null) {
                cita.setFamiliar(em.getReference(Familiar.class, cita.getFamiliar().getId()));
            }
            if (cita.getPerfilMenor() != null) {
                cita.setPerfilMenor(em.getReference(PerfilMenor.class, cita.getPerfilMenor().getId()));
            }
            if (cita.getEspecialista() != null) {
                cita.setEspecialista(em.getReference(Especialista.class, cita.getEspecialista().getId()));
            }
            em.persist(cita);
            em.getTransaction().commit();
            return cita;
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    /** CU-04, paso 23: obtener la lista actualizada de citas del Familiar. */
    public List<Cita> buscarPorFamiliar(int idFamiliar) {
        em = JPAUtil.crearEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Cita c WHERE c.familiar.id = :idFamiliar ORDER BY c.fecha, c.hora",
                    Cita.class)
                    .setParameter("idFamiliar", idFamiliar)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /** CU-05, paso 5: «query» SELECT c FROM Cita c WHERE c.especialista.id = :idEspecialista */
    public List<Cita> buscarPorEspecialista(int idEspecialista) {
        em = JPAUtil.crearEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Cita c WHERE c.especialista.id = :idEspecialista ORDER BY c.fecha, c.hora",
                    Cita.class)
                    .setParameter("idEspecialista", idEspecialista)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /** CU-05, paso 8: obtener los detalles de la cita seleccionada. */
    public Cita buscarPorId(int idCita) {
        em = JPAUtil.crearEntityManager();
        try {
            return em.find(Cita.class, idCita);
        } finally {
            em.close();
        }
    }

    /** CU-05, pasos 24 / 7a.13: registrar los cambios — «merge» em.merge(cita) */
    public Cita actualizar(Cita cita) {
        em = JPAUtil.crearEntityManager();
        try {
            em.getTransaction().begin();
            Cita actualizada = em.merge(cita);
            em.getTransaction().commit();
            return actualizada;
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }
}
