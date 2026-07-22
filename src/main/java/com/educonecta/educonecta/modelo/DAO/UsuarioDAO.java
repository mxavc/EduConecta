package com.educonecta.educonecta.modelo.DAO;

import java.util.List;

import com.educonecta.educonecta.modelo.Entidades.Usuario;
import com.educonecta.educonecta.util.JPAUtil;

import jakarta.persistence.EntityManager;

/**
 * «DAO» UsuarioDAO — CU-01 / CU-02.
 * + buscarPorCorreo(correo:String): Usuario
 * + existeCorreo(correo:String): boolean
 * + existeCedula(cedula:String): boolean
 */
public class UsuarioDAO {

    private EntityManager em;

    /** CU-01, paso 6: «query» SELECT u FROM Usuario u WHERE u.correoElectronico = :correo */
    public Usuario buscarPorCorreo(String correo) {
        em = JPAUtil.crearEntityManager();
        try {
            List<Usuario> resultado = em
                    .createQuery("SELECT u FROM Usuario u WHERE u.correoElectronico = :correo", Usuario.class)
                    .setParameter("correo", correo)
                    .getResultList();
            return resultado.isEmpty() ? null : resultado.get(0);
        } finally {
            em.close();
        }
    }

    /** CU-02, paso 10: verificar que el correo electrónico no se encuentre registrado. */
    public boolean existeCorreo(String correo) {
        em = JPAUtil.crearEntityManager();
        try {
            Long total = em
                    .createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.correoElectronico = :correo", Long.class)
                    .setParameter("correo", correo)
                    .getSingleResult();
            return total > 0;
        } finally {
            em.close();
        }
    }

    /** CU-02, paso 11: verificar que el número de cédula no se encuentre registrado. */
    public boolean existeCedula(String cedula) {
        em = JPAUtil.crearEntityManager();
        try {
            Long total = em
                    .createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.numeroCedula = :cedula", Long.class)
                    .setParameter("cedula", cedula)
                    .getSingleResult();
            return total > 0;
        } finally {
            em.close();
        }
    }
}
