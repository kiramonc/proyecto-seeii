/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Pojo.Pregunta;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author KathyR
 */
public class DaoPregunta implements Interface.InterfacePregunta {

    @Override
    public boolean registrar(Session session, Pregunta pregunta) throws Exception {
        session.save(pregunta);
        return true;
    }

    @Override
    public List<Pregunta> verTodo(Session session) throws Exception {
        String hql = "from Pregunta";
        Query query = session.createQuery(hql);
        List<Pregunta> listaPreguntas = (List<Pregunta>) query.list();
        for (Pregunta lista : listaPreguntas) {
            Hibernate.initialize(lista.getConcepto());
//            Hibernate.initialize(lista.getTest().getTema());
        }
        return listaPreguntas;
    }

    @Override
    public List<Pregunta> verPorTest(Session session, int test) throws Exception {
        String hql = "from Pregunta where test=:test";
        Query query = session.createQuery(hql);
        query.setInteger("test", test);
        List<Pregunta> listaPreguntas = (List<Pregunta>) query.list();
        for (Pregunta lista : listaPreguntas) {
            Hibernate.initialize(lista.getConcepto());
//            Hibernate.initialize(lista.getTest().getTema());
        }
        return listaPreguntas;
    }

    @Override
    public List<Pregunta> verPorConcepto(Session session, int concepto) throws Exception {
        String hql = "from Pregunta where concepto=:concepto";
        Query query = session.createQuery(hql);
        query.setInteger("concepto", concepto);
        List<Pregunta> listaPreguntas = (List<Pregunta>) query.list();
        for (Pregunta lista : listaPreguntas) {
            Hibernate.initialize(lista.getConcepto());
//            Hibernate.initialize(lista.getTest().getTema());
        }
        return listaPreguntas;
    }

    @Override
    public Pregunta verPorCodigoPregunta(Session session, int idPregunta) throws Exception {
        String hql = "from Pregunta where idPregunta=:idPregunta";
        Query query = session.createQuery(hql);
        query.setParameter("idPregunta", idPregunta);
        Pregunta pregunta = (Pregunta) query.uniqueResult();
        Hibernate.initialize(pregunta.getConcepto());
//        Hibernate.initialize(pregunta.getTest().getTema());
        return pregunta;
    }

    @Override
    public boolean actualizar(Session session, Pregunta pregunta) throws Exception {
        session.update(pregunta);
        return true;
    }

    @Override
    public boolean eliminar(Session session, Pregunta pregunta) throws Exception {
        session.delete(pregunta);
        return true;
    }

}
