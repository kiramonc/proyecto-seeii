/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Interface.InterfacePreguntaEntrenar;
import Pojo.Preguntaentrenar;
import java.sql.Timestamp;
import java.util.Date;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author silvy
 */
public class DaoPreguntaEntrenar  implements InterfacePreguntaEntrenar{

    @Override
    public boolean registrarPreguntaEnt(Session session, Preguntaentrenar preguntaEntrena) throws Exception {
        session.save(preguntaEntrena);
        return true;
    }

    @Override
    public Preguntaentrenar verPreguntaEntrenamiento(Session session, int idEntrenar,Timestamp fecha) throws Exception {
        String hql = "from Preguntaentrenar where fecha=:fecha and identrenar=:identrenar";
        Query query = session.createQuery(hql);
        query.setParameter("fecha", fecha);
        query.setInteger("identrenar", idEntrenar);
        Preguntaentrenar preguntaE = (Preguntaentrenar) query.uniqueResult();
        return preguntaE; }

    @Override
    public Preguntaentrenar verPorCodigoPreguntaEntrenar(Session session, int idPreguntaE) throws Exception {
        String hql="from Preguntaentrenar where idInt=:idInt";
        Query query=session.createQuery(hql);
        query.setParameter("idInt", idPreguntaE);
        Preguntaentrenar preguntaEntren=(Preguntaentrenar) query.uniqueResult();
        return preguntaEntren;
    }
    
    @Override
    public boolean actualizar(Session session, Preguntaentrenar preguntaEntrena) throws Exception {
        session.update(preguntaEntrena);
        return true;
    }
    
}
