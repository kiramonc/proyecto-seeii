/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Dao;

import HibernateUtil.HibernateUtil;
import Pojo.Resultado;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author KathyR
 */
public class DaoResultado implements Interface.InterfaceResultado{

    @Override
    public boolean registrar(Session session, Resultado resultado) throws Exception {
        session.save(resultado);
        return true;
    }
    
    public boolean registrarVarios(Session session, List<Resultado> resultados) throws Exception {
        for (Resultado r : resultados) {
            session.save(r);
            session.flush();
            session.clear();
        }
        return true;
    }

    @Override
    public List<Resultado> verTodo() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Criteria criteria = session.createCriteria(Resultado.class);
        List<Resultado> listaResultados = (List<Resultado>) criteria.list();
        session.close();
        return listaResultados;
    }

    @Override
    public List<Resultado> verTodo(Session session) throws Exception {
        String hql = "from Resultado";
        Query query = session.createQuery(hql);
        List<Resultado> listaResultados = (List<Resultado>) query.list();
        for (Resultado lista : listaResultados) {
            Hibernate.initialize(lista.getEstudiante());
            Hibernate.initialize(lista.getConcepto());
        }
        return listaResultados;
    }

    @Override
    public Resultado verPorCodigoResultado(Session session, int idResultado) throws Exception {
        String hql = "from Resultado where idResultado=:idResultado";
        Query query = session.createQuery(hql);
        query.setParameter("idResultado", idResultado);
        Resultado resultado = (Resultado) query.uniqueResult();
        Hibernate.initialize(resultado.getEstudiante());
        Hibernate.initialize(resultado.getConcepto());
        return resultado;

    }

    @Override
    public boolean actualizar(Session session, Resultado resultado) throws Exception {
        session.update(resultado);
        return true;

    }

    @Override
    public boolean eliminar(Session session, Resultado resultado) throws Exception {
        session.delete(resultado);
        return true;
    }
    

    @Override
    public List<Resultado> verPorEstudiante(Session session, int estudiante) throws Exception {
            String hql = "from Resultado where estudiante=:estudiante";
        Query query = session.createQuery(hql);
        query.setInteger("estudiante", estudiante);
        List<Resultado> listaResultados = (List<Resultado>) query.list();
        if(listaResultados!=null){
        for (Resultado lista : listaResultados) {
            Hibernate.initialize(lista.getEstudiante());
            Hibernate.initialize(lista.getConcepto());
        }
        }
        return listaResultados;
    }
    
    @Override
    public List<Resultado> verPorConcepto(Session session, int concepto) throws Exception {
            String hql = "from Resultado where concepto=:concepto";
        Query query = session.createQuery(hql);
        query.setInteger("concepto", concepto);
        List<Resultado> listaResultados = (List<Resultado>) query.list();
        if(listaResultados!=null){
        for (Resultado lista : listaResultados) {
            Hibernate.initialize(lista.getEstudiante());
            Hibernate.initialize(lista.getConcepto());
        }
        }
        return listaResultados;
    }    
    
    
}
