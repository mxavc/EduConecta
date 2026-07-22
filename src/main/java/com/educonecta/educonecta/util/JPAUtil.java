package com.educonecta.educonecta.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Utilitario de infraestructura: expone el EntityManagerFactory de la
 * unidad de persistencia "EduConectaJPA" (EclipseLink + MySQL) declarada
 * en META-INF/persistence.xml.
 */
public final class JPAUtil {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("EduConectaJPA");

    private JPAUtil() {
    }

    public static EntityManager crearEntityManager() {
        return emf.createEntityManager();
    }
}
