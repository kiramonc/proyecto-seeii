/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Dao.DaoTest;
import HibernateUtil.HibernateUtil;
import Pojo.Pregunta;
import Pojo.Test;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author KathyR
 */
@ManagedBean
@ViewScoped
public class BeanRTest {

    private Test test = new Test();
    private List<Test> listaTest;
    private List<Test> listaTestFiltrada;
    private List<Pregunta> listaPreguntas;
    private Session session;
    private Transaction transaction;

    public BeanRTest() {

    }

    //Metodos
//    public void registrar() {
//        this.session = null;
//        this.transaction = null;
//        try {
//            DaoTest daoTest = new DaoTest();
//            this.session = HibernateUtil.getSessionFactory().openSession();
//            this.transaction = session.beginTransaction();
//            daoTest.registrar(this.session, this.test);
//
//            this.transaction.commit();
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue realizado con éxito"));
//        } catch (Exception ex) {
//            if (this.transaction != null) {
//                this.transaction.rollback();
//            }
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR REGISTRO:", "Contacte con el administrador" + ex.getMessage()));
//        } finally {
//            if (this.session != null) {
//                this.session.close();
//            }
//            this.test = new Test();
//        }
//    }

    public void actualizar() {
        this.session = null;
        this.transaction = null;
        try {
            DaoTest daoTest = new DaoTest();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            daoTest.actualizar(this.session, this.test);
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios se realizaron con éxito."));
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR ACTUALIZAR:", "Contacte con el administrador" + ex.getMessage()));
            System.out.println("ERRRRORRRR: " + ex);
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public void eliminar() {
        this.session = null;
        this.transaction = null;
        try {
            DaoTest daoTest = new DaoTest();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            daoTest.eliminar(this.session, this.test);
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Test eliminado correctamente."));
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL ELIMINAR:", "Contacte con el administrador, " + ex));
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

    public void limpiarFormulario() {
        this.test = new Test();
    }

    public boolean deshabilitarBotonCrearPregunta() {
        if (this.test.getTema() != null) {
            return false;
        }
        return true;
    }

//    public void cargarTestEditar(int codigo){
//        this.session = null;
//        this.transaction = null;
//        try {
//            DaoTest daoUnidad = new DaoTest();
//            this.session = HibernateUtil.getSessionFactory().openSession();
//            this.transaction = session.beginTransaction();
//            this.test=daoUnidad.verPorCodigoTest(session, codigo);
//            
//            RequestContext.getCurrentInstance().update("frmEditarTest:panelEditarTest");
//            RequestContext.getCurrentInstance().execute("PF('dialogEditarTest').show()");            
//            this.transaction.commit();
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios se realizaron con éxito."));
//        } catch (Exception ex) {
//            if (this.transaction != null) {
//                this.transaction.rollback();
//            }
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CARGAR TEST EDITAR:", "Contacte con el administrador" + ex.getMessage()));
//        } finally {
//            if (this.session != null) {
//                this.session.close();
//            }
//        }
//    }
//
//    public void cargarTestEliminar(int idTest) {
//        this.session = null;
//        this.transaction = null;
//        try {
//            DaoTest daoTest = new DaoTest();
//            this.session = HibernateUtil.getSessionFactory().openSession();
//            this.transaction = session.beginTransaction();
//            this.test = daoTest.verPorCodigoTest(session, idTest);
//            daoTest.eliminar(this.session, this.test);
//            this.transaction.commit();
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Test eliminado correctamente."));
//        } catch (Exception ex) {
//            if (this.transaction != null) {
//                this.transaction.rollback();
//            }
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL ELIMINAR:", "Contacte con el administrador, " + ex));
//        } finally {
//            if (this.session != null) {
//                this.session.close();
//            }
//        }
//    }
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

    public List<Test> getListaTestFiltrada() {
        return listaTestFiltrada;
    }

    public void setListaTestFiltrada(List<Test> listaTestFiltrada) {
        this.listaTestFiltrada = listaTestFiltrada;
    }

    public List<Pregunta> getListaPreguntas() {
        return listaPreguntas;
    }

    public void setListaPreguntas(List<Pregunta> listaPreguntas) {
        this.listaPreguntas = listaPreguntas;
    }

}
