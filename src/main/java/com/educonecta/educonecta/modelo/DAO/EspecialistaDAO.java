package com.educonecta.educonecta.modelo.DAO;

import java.util.List;

import com.educonecta.educonecta.modelo.Entidades.Especialista;
import com.educonecta.educonecta.util.JPAUtil;

import jakarta.persistence.EntityManager;

/**
 * «DAO» EspecialistaDAO — CU-02 / CU-04 / CU-05.
 * + existeRuc(ruc:String): boolean
 * + insertar(especialista:Especialista): Especialista
 * + buscarTodos(): List
 * + buscarPorId(idEspecialista:int): Especialista
 */
public class EspecialistaDAO {

    private EntityManager em;

    /** CU-02, paso 12: verificar que el RUC no se encuentre registrado. */
    public boolean existeRuc(String ruc) {
        em = JPAUtil.crearEntityManager();
        try {
            Long total = em
                    .createQuery("SELECT COUNT(e) FROM Especialista e WHERE e.ruc = :ruc", Long.class)
                    .setParameter("ruc", ruc)
                    .getSingleResult();
            return total > 0;
        } finally {
            em.close();
        }
    }

    /** CU-02, paso 15: «persist» em.persist(especialista) */
    public Especialista insertar(Especialista especialista) {
        em = JPAUtil.crearEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(especialista);
            em.getTransaction().commit();
            return especialista;
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    /** CU-04, paso 5: obtener la lista de Especialistas disponibles. */
    public List<Especialista> buscarTodos() {
        em = JPAUtil.crearEntityManager();
        try {
            return em.createQuery(
                    "SELECT e FROM Especialista e ORDER BY e.apellidosCompletos, e.nombresCompletos",
                    Especialista.class).getResultList();
        } finally {
            em.close();
        }
    }

    /** CU-04, paso 8 / CU-05, paso 2: obtener la información del Especialista. */
    public Especialista buscarPorId(int idEspecialista) {
        em = JPAUtil.crearEntityManager();
        try {
            return em.find(Especialista.class, idEspecialista);
        } finally {
            em.close();
        }
    }
}
