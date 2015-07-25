/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Dao.DaoConcepto;
import Dao.DaoItem;
import Dao.DaoPregunta;
import Dao.DaoTema;
import HibernateUtil.HibernateUtil;
import Pojo.Concepto;
import Pojo.Item;
import Pojo.Pregunta;
import Pojo.Tema;
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
public class BeanRConcepto {

    private Concepto concepto = new Concepto();
    private List<Concepto> listaConceptos;
    private List<Concepto> listaConceptoFiltrado;
    private Session session;
    private Transaction transaction;
    private Tema tema;

    public BeanRConcepto() {
        this.tema=null;
    }

    //Metodos
    public void registrar() {
        this.session = null;
        this.transaction = null;
        try {
            DaoConcepto daoConcepto = new DaoConcepto();
            DaoTema daoTema = new DaoTema();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
//            this.tema=daoTema.verPorCodigoTema(session, codigo);
            System.out.println("EL TEMA QUE SE ESTÁ CARGANDO ES: "+this.tema.getNombre());
            this.concepto.setEstado(true);
            this.concepto.setTema(tema);
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
        System.out.println("VAMOS A ELIMINAR EL CONCEPTO: "+this.concepto.getNombreConcepto());
        try {
            DaoConcepto daoConcepto = new DaoConcepto();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            System.out.println("A PUNTO DE ELIMINAR EL CONCEPTO: "+this.concepto.getNombreConcepto());
            daoConcepto.eliminar(this.session, this.concepto);
            System.out.println("SEGÚN ESTO DEBIÓ HABERSE ELIMINADO: "+this.concepto.getNombreConcepto());
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
    
    public void abrirDialogoCrearConcepto(int codigo) {
        this.session=null;
        this.transaction=null;
        try {
            this.concepto= new Concepto();
            DaoTema daoTema = new DaoTema();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.tema=daoTema.verPorCodigoTema(session, codigo);
            System.out.println("EL TEMA QUE SE ESTÁ CARGANDO ES: "+this.tema.getNombre());
            RequestContext.getCurrentInstance().update("frmEditarConcepto:panelEditarConcepto");
            RequestContext.getCurrentInstance().execute("PF('dialogEditarConcepto').show()");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios se realizaron con éxito."));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CARGAR CONCEPTO CREAR:", "Contacte con el administrador" + ex.getMessage()));
        }
    }
    
    public List<Concepto> getConceptosPorTema(Tema tema) {
        if(tema!=null){
        this.session = null;
        this.transaction = null;
        this.tema=tema;
        try {
            DaoConcepto daoConcepto = new DaoConcepto();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            
            List<Concepto> t = daoConcepto.verPorTema(session, this.tema.getIdTema());
            transaction.commit();
            return t;
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CARGAR LISTA DE CONCEPTO POR TEMA:", "Contacte con el administrador" + ex.getMessage()));

            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
        }
        return null;
    }
    
    public boolean deshabilitarBotonCrearTema(){
        
            return false;
        
    }



    public void limpiarFormulario() {
        this.concepto = new Concepto();
        RequestContext.getCurrentInstance().update("frmVerConceptos:panelVerConceptos");
            RequestContext.getCurrentInstance().execute("PF('dialogVerConceptos').show()");
            
    }

//    public boolean deshabilitarBotonCrearPregunta() {
//        if (this.concepto.getTema() != null) {
//            return false;
//        }
//        return true;
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
