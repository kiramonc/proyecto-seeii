/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Pojo.Item;
import Pojo.Pregunta;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author KathyR
 */
public class DaoItem implements Interface.InterfaceItems {

    @Override
    public boolean registrar(Session session, Item item) throws Exception {
        session.save(item);
        return true;
    }
    
//    public boolean registrar(Session session, int pregunta, String nombreItem, byte[] imgItem, int traduccion) throws Exception {
//        String insert = "insert into Item(pregunta,nombreItem, imgItem, traduccion) values(?,?,?,?)";
//        Query query = session.createQuery(insert);
//        query.setInteger(1, pregunta);
//        query.setString(2, nombreItem);
//        query.setBinary(3, imgItem);
//        query.setInteger(4, traduccion);
//        query.executeUpdate();
//        
//        return true;
//    }
    
    

    @Override
    public List<Item> verTodo(Session session) throws Exception {
        String hql = "from Item";
        Query query = session.createQuery(hql);
        List<Item> listaItems = (List<Item>) query.list();
        for (Item lista : listaItems) {
            Hibernate.initialize(lista.getPregunta());
//            Hibernate.initialize(lista.getTest().getTema());
        }
        return listaItems;
    }

    @Override
    public List<Item> verPorPregunta(Session session, int pregunta) throws Exception {
        String hql = "from Item where pregunta=:pregunta";
        Query query = session.createQuery(hql);
        query.setInteger("pregunta", pregunta);
        List<Item> listaItems = (List<Item>) query.list();
        for (Item lista : listaItems) {
            Hibernate.initialize(lista.getPregunta());
//            Hibernate.initialize(lista.getTest().getTema());
        }
        return listaItems;
    }

    @Override
    public Item verPorNombreItem(Session session, String nombreItem) throws Exception {
        String hql = "from Item where nombreItem=:nombreItem";
        Query query = session.createQuery(hql);
        query.setParameter("nombreItem", nombreItem);
        Item item = (Item) query.uniqueResult();
        Hibernate.initialize(item.getPregunta());
//            Hibernate.initialize(lista.getTest().getTema());
        return item;
    }

    @Override
    public Item verPorCodigoItem(Session session, int idItem) throws Exception {
        String hql = "from Item where idItem=:idItem";
        Query query = session.createQuery(hql);
        query.setParameter("idItem", idItem);
        Item pregunta = (Item) query.uniqueResult();
        Hibernate.initialize(pregunta.getPregunta());
//        Hibernate.initialize(pregunta.getTest().getTema());
        return pregunta;
    }

    @Override
    public boolean actualizar(Session session, Item item) throws Exception {
        session.update(item);
        return true;
    }

    @Override
    public boolean eliminar(Session session, Item item) throws Exception {
        session.delete(item);
        return true;
    }
    
//    public void saveImg(Session session, FileInputStream fis, File file) throws Exception{
//        String hql = "from Item where idItem=:idItem";
//        Query query = session.createQuery(hql);
//                    ps.setBinaryStream(1, fis, (int) file.length());
//
//    }

}