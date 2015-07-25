/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Dao.DaoItem;
import Dao.DaoPregunta;
import Dao.DaoTest;
import HibernateUtil.HibernateUtil;
import Pojo.Item;
import Pojo.Pregunta;
import Pojo.Test;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;

/**
 *
 * @author KathyR
 */
@ManagedBean
@ViewScoped
public class BeanRPresentarTest {

    private Test test = new Test();
    private List<Test> listaTest;
    private List<Pregunta> listaPreguntas;
    private Session session;
    private Transaction transaction;
    private Pregunta preguntaSeleccionada;
    private List<Item> listaItems;
    private String respuestTemp;
    private boolean correcto;
    

    public BeanRPresentarTest() {

    }
    
    public Pregunta obtenerPregunta(int codigo){
        this.session = null;
        this.transaction = null;
        
        try {
            DaoPregunta daoItem = new DaoPregunta();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            preguntaSeleccionada = daoItem.verPorCodigoPregunta(session, codigo);
            transaction.commit();
            return preguntaSeleccionada;
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR al obtener pregunta por codigo:", "Contacte con el administrador" + ex.getMessage()));
            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }
    
    public void guardarRespuestaTemp(String respuesta){
        this.respuestTemp=respuesta;
        if(this.preguntaSeleccionada.getConcepto().getNombreConcepto().equalsIgnoreCase(respuesta)){
            this.correcto=true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Respuesta correcta:\n\n", respuestTemp));
        }else{
            this.correcto=false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Respuesta incorrecta:\n\n", respuestTemp));
            
        }
//        RequestContext.getCurrentInstance().update("frmRespuesta:panelRespuesta");
//        RequestContext.getCurrentInstance().execute("PF('dialogRespuesta').show()");
    }
    
    public List<Item> itemsPorPregunta(int codigo){
        this.session = null;
        this.transaction = null;
        
        try {
            DaoItem daoItem = new DaoItem();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            List<Item> t = daoItem.verPorPregunta(session, codigo);
            transaction.commit();
            return t;
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR al obtener items por pregunta:", "Contacte con el administrador" + ex.getMessage()));
            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }
    
    public List<Pregunta> cargarListaPreguntas(){
        this.session = null;
        this.transaction = null;
        
        try {
            DaoPregunta daoPregunta = new DaoPregunta();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            listaPreguntas = daoPregunta.verPorTest(session, 1);
            transaction.commit();
            return listaPreguntas;
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR al cargar preguntas del test 1:", "Contacte con el administrador" + ex.getMessage()));
            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }
    
    

    public Test consultarTestPorCodigo(int idTest) {
        this.session = null;
        this.transaction = null;
        try {
            DaoTest daoUnidad = new DaoTest();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            Test t = daoUnidad.verPorCodigoTest(session, idTest);
            transaction.commit();
            return t;
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public List<Test> getAllTest() {
        this.session = null;
        this.transaction = null;
        try {
            DaoTest daoTest = new DaoTest();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.listaTest = daoTest.verTodo(session);
            this.transaction.commit();
            return this.listaTest;

        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR:", "Contacte con el administrador" + ex.getMessage()));
            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }


    public boolean deshabilitarBotonCrearPregunta() {
        if (this.test.getTema() != null) {
            return false;
        }
        return true;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public List<Test> getListaTest() {
        return listaTest;
    }

    public void setListaTest(List<Test> listaTest) {
        this.listaTest = listaTest;
    }

    public List<Pregunta> getListaPreguntas() {
        return listaPreguntas;
    }

    public void setListaPreguntas(List<Pregunta> listaPreguntas) {
        this.listaPreguntas = listaPreguntas;
    }

    public Pregunta getPreguntaSeleccionada() {
        return preguntaSeleccionada;
    }

    public void setPreguntaSeleccionada(Pregunta preguntaSeleccionada) {
        this.preguntaSeleccionada = preguntaSeleccionada;
    }

    public List<Item> getListaItems() {
        return listaItems;
    }

    public void setListaItems(List<Item> listaItems) {
        this.listaItems = listaItems;
    }

    public String getRespuestTemp() {
        return respuestTemp;
    }

    public void setRespuestTemp(String respuestTemp) {
        this.respuestTemp = respuestTemp;
    }

    public boolean isCorrecto() {
        return correcto;
    }

    public void setCorrecto(boolean correcto) {
        this.correcto = correcto;
    }
    
    
 
}