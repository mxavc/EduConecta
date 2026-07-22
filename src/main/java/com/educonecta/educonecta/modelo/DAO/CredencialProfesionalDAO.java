package com.educonecta.educonecta.modelo.DAO;

import com.educonecta.educonecta.modelo.Entidades.CredencialProfesional;
import com.educonecta.educonecta.modelo.Entidades.Especialista;
import com.educonecta.educonecta.util.JPAUtil;

import jakarta.persistence.EntityManager;

/**
 * «DAO» CredencialProfesionalDAO — CU-02.
 * + insertar(credencial:CredencialProfesional): CredencialProfesional
 */
public class CredencialProfesionalDAO {

    private EntityManager em;

    /** CU-02, paso 15: «persist» em.persist(credencial) — por cada documento. */
    public CredencialProfesional insertar(CredencialProfesional credencial) {
        em = JPAUtil.crearEntityManager();
        try {
            em.getTransaction().begin();
            // Reasociar la referencia al Especialista dentro de este contexto de persistencia.
            if (credencial.getEspecialista() != null) {
                credencial.setEspecialista(
                        em.getReference(Especialista.class, credencial.getEspecialista().getId()));
            }
            em.persist(credencial);
            em.getTransaction().commit();
            return credencial;
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }
}
