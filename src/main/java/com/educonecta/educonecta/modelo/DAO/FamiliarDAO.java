package com.educonecta.educonecta.modelo.DAO;

import com.educonecta.educonecta.modelo.Entidades.Familiar;
import com.educonecta.educonecta.util.JPAUtil;

import jakarta.persistence.EntityManager;

/**
 * «DAO» FamiliarDAO — CU-02 / CU-03.
 * + insertar(familiar:Familiar): Familiar
 * + buscarPorId(idFamiliar:int): Familiar
 */
public class FamiliarDAO {

    private EntityManager em;

    /** CU-02, paso 15: «persist» em.persist(familiar) */
    public Familiar insertar(Familiar familiar) {
        em = JPAUtil.crearEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(familiar);
            em.getTransaction().commit();
            return familiar;
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    /** CU-03, paso 2: obtener la información del Familiar. */
    public Familiar buscarPorId(int idFamiliar) {
        em = JPAUtil.crearEntityManager();
        try {
            return em.find(Familiar.class, idFamiliar);
        } finally {
            em.close();
        }
    }
}
