/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Dao.DaoEstudiante;
import HibernateUtil.HibernateUtil;
import Pojo.Estudiante;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author USUARIO
 */
@ManagedBean
@ViewScoped
public class BeanREstudiante {

    private Session session;
    private Transaction transaction;

    public BeanREstudiante() {
    }

    // Recuperar un determinado Estudiante (se utliza en la clase EstudianteConverter)
    public Estudiante consultarUnidadPorNombre(String unidad) {
        try {
            DaoEstudiante daoEstudiante = new DaoEstudiante();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
//            Estudiante t = daoEstudiante.verPorNombreUnidad(session, unidad);
            transaction.commit();
//            return t;
            return null;

        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }

    }
}
