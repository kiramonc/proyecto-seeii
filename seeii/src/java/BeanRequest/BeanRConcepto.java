/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Dao.DaoConcepto;
import Dao.DaoTest;
import Dao.DaoUnidadE;
import HibernateUtil.HibernateUtil;
import Pojo.Concepto;
import Pojo.Pregunta;
import Pojo.Test;
import Pojo.Unidadensenianza;
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
public class BeanRConcepto {

    private Concepto concepto = new Concepto();
    private List<Concepto> listaConceptos;
    private List<Concepto> listaConceptoFiltrado;
    private Session session;
    private Transaction transaction;

    public BeanRConcepto() {

    }

    //Metodos
    public void registrar() {
        this.session = null;
        this.transaction = null;
        try {
            DaoConcepto daoConcepto = new DaoConcepto();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.concepto.setEstado(true);
            daoConcepto.registrar(this.session, this.concepto);

            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue realizado con éxito"));
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR REGISTRO:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
            this.concepto = new Concepto();
        }
    }

    public void actualizar() {
        this.session = null;
        this.transaction = null;
        try {
            DaoConcepto daoConcepto = new DaoConcepto();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            daoConcepto.actualizar(this.session, this.concepto);
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios se realizaron con éxito."));
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR ACTUALIZAR:", "Contacte con el administrador" + ex.getMessage()));
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
            DaoConcepto daoConcepto = new DaoConcepto();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            daoConcepto.eliminar(this.session, this.concepto);
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Concepto eliminado correctamente."));
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

    public Concepto consultarConceptoPorCodigo(int idConcepto) {
        this.session = null;
        this.transaction = null;
        try {
            DaoConcepto daoConcepto = new DaoConcepto();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            Concepto c = daoConcepto.verPorCodigoConcepto(session, idConcepto);
            transaction.commit();
            return c;
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
    // para el converter
    public Concepto consultarConceptoPorNombre(String nombreConcepto) {
        try {
            DaoConcepto daoConcepto = new DaoConcepto();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            Concepto c = daoConcepto.verPorNombreConcepto(session, nombreConcepto);
            transaction.commit();
            return c;

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

    public List<Concepto> getAllConcepto() {
        this.session = null;
        this.transaction = null;
        try {
            DaoConcepto daoConcepto = new DaoConcepto();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.listaConceptos = daoConcepto.verTodo(session);
            this.transaction.commit();
            return this.listaConceptos;

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
        this.concepto = new Concepto();
    }

//    public boolean deshabilitarBotonCrearPregunta() {
//        if (this.concepto.getTema() != null) {
//            return false;
//        }
//        return true;
//    }
//    public void cargarTestEditar(int codigo){
//        this.session = null;
//        this.transaction = null;
//        try {
//            DaoTest daoConcepto = new DaoTest();
//            this.session = HibernateUtil.getSessionFactory().openSession();
//            this.transaction = session.beginTransaction();
//            this.concepto=daoConcepto.verPorCodigoTest(session, codigo);
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
//            DaoTest daoConcepto = new DaoTest();
//            this.session = HibernateUtil.getSessionFactory().openSession();
//            this.transaction = session.beginTransaction();
//            this.concepto = daoConcepto.verPorCodigoTest(session, idTest);
//            daoConcepto.eliminar(this.session, this.concepto);
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
    public Concepto getConcepto() {
        return concepto;
    }

    public void setConcepto(Concepto concepto) {
        this.concepto = concepto;
    }

    public List<Concepto> getListaConceptos() {
        return listaConceptos;
    }

    public void setListaConceptos(List<Concepto> listaConceptos) {
        this.listaConceptos = listaConceptos;
    }

    public List<Concepto> getListaConceptoFiltrado() {
        return listaConceptoFiltrado;
    }

    public void setListaConceptoFiltrado(List<Concepto> listaConceptoFiltrado) {
        this.listaConceptoFiltrado = listaConceptoFiltrado;
    }
    
}
