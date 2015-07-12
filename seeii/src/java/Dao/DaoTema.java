/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Interface.InterfaceTema;
import Pojo.Tema;
import Pojo.Usuario;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author silvy
 */
public class DaoTema implements InterfaceTema {
    //creamos un DAO por cada tabla de bd(por cada pojo)
    //Implemetamos la interfaz creada en Interface/InterfaceTema

    //Metodos Abstractos de la implemtacion del InterfaceTema
    @Override
    public boolean registrar(Session session, Tema tema) throws Exception {
        //session es de hibernate para establecer la conexion con la bd
        session.save(tema);
        return true;
    }

    @Override
    public List<Tema> verTodo(Session session) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean actualizar(Session session, Tema tema) throws Exception {
        session.update(tema);
//        Hibernate.initialize(tema.getUnidadensenianza());
        return true;
    }

    @Override
    public Tema verPorCodigoTema(Session session, int idTema) throws Exception {
        String hql="from Tema where idTema=:idTema";
        Query query=session.createQuery(hql);
        query.setParameter("idTema", idTema);
        Tema tema=(Tema) query.uniqueResult();
//        Hibernate.initialize(tema.getUnidadensenianza());
        return tema;
    }

    @Override
    public Tema verPorTemaname(Session session, String nombreTema) throws Exception {
        String hql = "from Tema where nombre=:nombre";
        Query query = session.createQuery(hql);
        query.setParameter("nombre", nombreTema);
        Tema tema = (Tema) query.uniqueResult();
        return tema;
    }
    
    public List<Tema> verPorUnidad(Session session, int idUnidad) throws Exception {
        String hql = "from Tema where unidadensenianza=:unidadensenianza";
        Query query = session.createQuery(hql);
        query.setInteger("unidadensenianza", idUnidad);
//        query.setParameter("unidadensenianza", idUnidad);
        List<Tema> tema = (List<Tema>) query.list();
        if(tema.isEmpty())
            System.out.println("La lista está vacía");
        else{
            for(Tema lista: tema){
                System.out.println("Tema: "+lista.getNombre());
            }
        }
//        for(Tema lista: tema){
//            Hibernate.initialize(lista.getUnidadensenianza());
//        }

        return tema;
    }

}
