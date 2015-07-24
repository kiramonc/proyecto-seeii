/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import HibernateUtil.HibernateUtil;
import Pojo.Test;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author KathyR
 */
public class DaoTest implements Interface.InterfaceTest {

    @Override
    public boolean registrar(Session session, Test test) throws Exception {
        session.save(test);
        return true;
    }

    @Override
    public List<Test> verTodo() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Criteria criteria = session.createCriteria(Test.class);
        List<Test> listaTest = (List<Test>) criteria.list();
        session.close();
        return listaTest;
    }

    @Override
    public List<Test> verTodo(Session session) throws Exception {
        String hql = "from Test";
        Query query = session.createQuery(hql);
        List<Test> listaTest = (List<Test>) query.list();
        for (Test lista : listaTest) {
            Hibernate.initialize(lista.getTema());
            Hibernate.initialize(lista.getTema().getUnidadensenianza());
        }
        return listaTest;
    }

    @Override
    public Test verPorTema(Session session, int tema) throws Exception {
        String hql = "from Test where tema=:tema";
        Query query = session.createQuery(hql);
        query.setInteger("tema", tema);
        Test test = (Test) query.uniqueResult();
        return test;
    }

    @Override
    public Test verPorCodigoTest(Session session, int idTest) throws Exception {
        String hql = "from Test where idTest=:idTest";
        Query query = session.createQuery(hql);
        query.setParameter("idTest", idTest);
        Test test = (Test) query.uniqueResult();
        Hibernate.initialize(test.getTema());
        Hibernate.initialize(test.getTema().getUnidadensenianza());
        return test;
    }

    @Override
    public boolean actualizar(Session session, Test test) throws Exception {
        session.update(test);
        return true;
    }

    @Override
    public boolean eliminar(Session session, Test test) throws Exception {
        session.delete(test);
        return true;
    }

}
