package com.educonecta.educonecta.modelo.DAO;

import java.util.List;

import com.educonecta.educonecta.modelo.Entidades.Familiar;
import com.educonecta.educonecta.modelo.Entidades.HistorialAtencion;
import com.educonecta.educonecta.modelo.Entidades.PerfilMenor;
import com.educonecta.educonecta.util.JPAUtil;

import jakarta.persistence.EntityManager;

/**
 * «DAO» PerfilMenorDAO — CU-03 / CU-04 / CU-05.
 * + buscarPorFamiliar(idFamiliar:int): List
 * + existePorFamiliarYCedula(idFamiliar:int, cedula:String): boolean
 * + insertar(perfil:PerfilMenor): PerfilMenor
 * + buscarPorCita(idCita:int): PerfilMenor
 */
public class PerfilMenorDAO {

    private EntityManager em;

    /** CU-03, paso 5: «query» SELECT p FROM PerfilMenor p WHERE p.familiar.id = :idFamiliar */
    public List<PerfilMenor> buscarPorFamiliar(int idFamiliar) {
        em = JPAUtil.crearEntityManager();
        try {
            return em.createQuery(
                    "SELECT p FROM PerfilMenor p WHERE p.familiar.id = :idFamiliar "
                            + "ORDER BY p.apellidosCompletos, p.nombresCompletos",
                    PerfilMenor.class)
                    .setParameter("idFamiliar", idFamiliar)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /** CU-03, paso 12: verificar que el menor no se encuentre registrado por el Familiar. */
    public boolean existePorFamiliarYCedula(int idFamiliar, String cedula) {
        em = JPAUtil.crearEntityManager();
        try {
            Long total = em.createQuery(
                    "SELECT COUNT(p) FROM PerfilMenor p WHERE p.familiar.id = :idFamiliar AND p.cedula = :cedula",
                    Long.class)
                    .setParameter("idFamiliar", idFamiliar)
                    .setParameter("cedula", cedula)
                    .getSingleResult();
            return total > 0;
        } finally {
            em.close();
        }
    }

    /** CU-03, paso 14: «persist» em.persist(perfil) */
    public PerfilMenor insertar(PerfilMenor perfil) {
        em = JPAUtil.crearEntityManager();
        try {
            em.getTransaction().begin();
            // Reasociar las referencias del perfil dentro de este contexto de persistencia.
            if (perfil.getFamiliar() != null) {
                perfil.setFamiliar(em.getReference(Familiar.class, perfil.getFamiliar().getId()));
            }
            if (perfil.getHistorialAtencion() != null) {
                perfil.setHistorialAtencion(
                        em.getReference(HistorialAtencion.class, perfil.getHistorialAtencion().getId()));
            }
            em.persist(perfil);
            em.getTransaction().commit();
            return perfil;
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    /** CU-05, paso 9: obtener el Perfil del Menor vinculado con la cita. */
    public PerfilMenor buscarPorCita(int idCita) {
        em = JPAUtil.crearEntityManager();
        try {
            List<PerfilMenor> resultado = em.createQuery(
                    "SELECT c.perfilMenor FROM Cita c WHERE c.id = :idCita", PerfilMenor.class)
                    .setParameter("idCita", idCita)
                    .getResultList();
            return resultado.isEmpty() ? null : resultado.get(0);
        } finally {
            em.close();
        }
    }
}
