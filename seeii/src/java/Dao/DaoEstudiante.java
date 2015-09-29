/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Dao;

import HibernateUtil.HibernateUtil;
import Pojo.Administrador;
import Pojo.Estudiante;
import Pojo.Tema;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author KathyR
 */
public class DaoEstudiante implements Interface.InterfaceEstudiante{

    @Override
    public boolean registrar(Session session, Estudiante est) throws Exception {
        session.save(est);
        return true;
    }

    @Override
    public List<Estudiante> verTodo(Session session) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Estudiante verPorCodigoEstudiante(Session session, int idEstud) throws Exception {
        String hql="from Estudiante where idEst=:idEst";
        Query query=session.createQuery(hql);
        query.setParameter("idEst", idEstud);
        Estudiante estudiante=(Estudiante) query.uniqueResult();
//        Hibernate.initialize(tema.getUnidadensenianza());
        return estudiante;
    }

    @Override
    public Estudiante verPorCodigoUsuario(Session session, int idUsuario) throws Exception {
        
     String hql="from Estudiante where usuarioEst=:usuarioEst";
        Query query=session.createQuery(hql);
        query.setParameter("usuarioEst", idUsuario);
        Estudiante estudiante=(Estudiante) query.uniqueResult();
//        Hibernate.initialize(tema.getUnidadensenianza());
        return estudiante;
    }

    @Override
    public boolean actualizar(Session session, Estudiante est) throws Exception {
        session.update(est);
        return true;
    }
    
}
