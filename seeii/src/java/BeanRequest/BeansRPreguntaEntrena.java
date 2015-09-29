/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Dao.DaoEntrenar;
import Dao.DaoPreguntaEntrenar;
import HibernateUtil.HibernateUtil;
import Pojo.Entrenamiento;
import Pojo.Preguntaentrenar;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author silvy
 */
@ManagedBean
@RequestScoped
public class BeansRPreguntaEntrena{

    private Session session;
    private Transaction transaction;
    private Preguntaentrenar preguntaEnt;
    java.util.Date fecha = new Date();

    public BeansRPreguntaEntrena() {
        this.preguntaEnt = new Preguntaentrenar();
        this.preguntaEnt.setIncorrecto(0);
        this.preguntaEnt.setValor(0);
//        this.preguntaEnt.setFecha(fecha);
    }

    public void crearPreguntaEntrena(int idEntrenar) {
        boolean estado = false;
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            //obtener el entrenamiento mediante el id para fijar a las preguntas.
            DaoEntrenar daoEntrenar = new DaoEntrenar();
            Entrenamiento entrenar = daoEntrenar.verPorCodigoEntrenamiento(session, idEntrenar);
            this.preguntaEnt.setEntrenamiento(entrenar);
            
            DaoPreguntaEntrenar daoPregunta = new DaoPreguntaEntrenar();
            daoPregunta.registrarPreguntaEnt(this.session, this.preguntaEnt);
            this.transaction.commit();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue realizado con éxito"));

        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL REGISTRA  PREGUNTAS DE entrenamiento:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public Preguntaentrenar getPreguntaEnt() {
        return preguntaEnt;
    }

    public void setPreguntaEnt(Preguntaentrenar preguntaEnt) {
        this.preguntaEnt = preguntaEnt;
    }

}
