/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Dao;

import HibernateUtil.HibernateUtil;
import Pojo.Administrador;
import Pojo.Estudiante;
import Pojo.Usuario;
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
    public Estudiante verPorCodigoEstudiante(Session session, int idEst) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Estudiante verPorCodigoUsuario(Session session, int idUsuario) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean actualizar(Session session, Estudiante est) throws Exception {
        session.update(est);
        return true;
    }
    
}
