/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Interface.InterfaceEntrenamiento;
import Pojo.Entrenamiento;
import java.sql.Date;
import java.sql.Timestamp;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author silvy
 */
public class DaoEntrenar implements InterfaceEntrenamiento {

    @Override
    public boolean registrar(Session session, Entrenamiento entrenar) throws Exception {
        session.save(entrenar);
        return true;
    }

    @Override
    public Entrenamiento verEntrenamiento(Session session, int estudiate, int tema, int tiempo, Timestamp fecha) throws Exception {
        String hql = "from Entrenamiento where fecha=:fecha and "
                + "idestudiante=:idestudiante and idtema=:idtema and tiempo=:tiempo";
        Query query = session.createQuery(hql);
        query.setParameter("fecha", fecha);
        query.setInteger("idestudiante", estudiate);
        query.setInteger("idtema", tema);
        query.setInteger("tiempo", tiempo);
        Entrenamiento entrenamiento = (Entrenamiento) query.uniqueResult();
        return entrenamiento;
    }
    @Override
    public Entrenamiento verPorCodigoEntrenamiento(Session session, int idEntrenar) throws Exception {
        String hql = "from Entrenamiento where idEntrena=:idEntrena";
        Query query = session.createQuery(hql);
        query.setInteger("idEntrena", idEntrenar);
        Entrenamiento entrenamiento = (Entrenamiento) query.uniqueResult();
        return entrenamiento;
    }

    @Override
    public boolean actualizar(Session session, Entrenamiento entrenar) throws Exception {
       session.update(entrenar);
        return true;
    }

}
