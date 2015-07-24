/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Dao;

import HibernateUtil.HibernateUtil;
import Pojo.Concepto;
import Pojo.Item;
import Pojo.Test;
import Pojo.Unidadensenianza;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author KathyR
 */
public class DaoConcepto implements Interface.InterfaceConcepto{

    @Override
    public boolean registrar(Session session, Concepto concepto) throws Exception {
        session.save(concepto);
        return true;
    }

    @Override
    public List<Concepto> verTodo() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Criteria criteria = session.createCriteria(Concepto.class);
        List<Concepto> listaConceptos = (List<Concepto>) criteria.list();
        session.close();
        return listaConceptos;
    }

    @Override
    public List<Concepto> verTodo(Session session) throws Exception {
        String hql = "from Concepto";
        Query query = session.createQuery(hql);
        List<Concepto> listaConceptos = (List<Concepto>) query.list();
        for (Concepto lista : listaConceptos) {
            Hibernate.initialize(lista.getTema());
//            Hibernate.initialize(lista.getTema().getUnidadensenianza());
        }
        return listaConceptos;
    }

    @Override
    public Concepto verPorCodigoConcepto(Session session, int idConcepto) throws Exception {
        String hql = "from Concepto where idConcepto=:idConcepto";
        Query query = session.createQuery(hql);
        query.setParameter("idConcepto", idConcepto);
        Concepto concepto = (Concepto) query.uniqueResult();
        Hibernate.initialize(concepto.getTema());
//        Hibernate.initialize(concepto.getTema().getUnidadensenianza());
        return concepto;

    }

    @Override
    public boolean actualizar(Session session, Concepto concepto) throws Exception {
        session.update(concepto);
        return true;

    }

    @Override
    public boolean eliminar(Session session, Concepto concepto) throws Exception {
        session.delete(concepto);
        return true;
    }
    
    public Concepto verPorNombreConcepto(Session session, String nombreConcepto) throws Exception {
        String hql="from Concepto where nombreConcepto=:nombreConcepto";
        Query query=session.createQuery(hql);
        query.setParameter("nombreConcepto", nombreConcepto);
        Concepto concepto=(Concepto) query.uniqueResult();
        return concepto;     
    }

    @Override
    public List<Concepto> verPorTema(Session session, int tema) throws Exception {
            String hql = "from Concepto where tema=:tema";
        Query query = session.createQuery(hql);
        query.setInteger("tema", tema);
        List<Concepto> listaItems = (List<Concepto>) query.list();
        for (Concepto lista : listaItems) {
            Hibernate.initialize(lista.getTema());
//            Hibernate.initialize(lista.getTest().getTema());
        }
        return listaItems;
    }
        
}
