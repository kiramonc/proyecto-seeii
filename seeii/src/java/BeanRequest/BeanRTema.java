/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Dao.DaoTema;
import Dao.DaoUnidadE;
import Dao.DaoUsuario;
import HibernateUtil.HibernateUtil;
import Pojo.Tema;
import Pojo.Unidadensenianza;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;

/**
 *
 * @author silvy
 */
@ManagedBean
@ViewScoped
public class BeanRTema {
    
    private Tema tema;
    private Unidadensenianza unidadensenianza;
     private List<Unidadensenianza> listaUnidadensenianza;
    private List<Tema> listaTemas;
    //Atributos de sesion y transaccion.
    private Session session;
    private Transaction transaction;
 
   
   //constructor
    public BeanRTema() {
        this.tema= new Tema();
    }
    //    metodos para crear,actualizar y ver temas
    
    public void registrar() {     
        this.session = null;
        this.transaction = null;
        try {
            
            Dao.DaoTema daoTema = new DaoTema();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            System.out.println("abre la sesion y transaccion correctamente");
            Tema t= daoTema.verPorTemaname(session, tema.getNombre()) ;
            System.out.println("consulta de existencia de tema realizada con éxito");
            if (t != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El nombre de Tema ya se encuentra registrado"));
                return;
            }
            System.out.println("el tema a ingresar es: "+this.tema.getNombre()+"-"+this.tema.getVocabulario()+"-"+this.tema.getObjetivo()+"-"+this.tema.getDominio());
            
            
            daoTema.registrar(this.session, this.tema);
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue realizado con éxito"));

            this.tema = new Tema();
            
            
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }
    
    public List<Tema> getTemasPorUnidad(int codigo) {
        this.session=null;
        this.transaction=null;
        try {
            DaoTema daoTema= new DaoTema();
//            DaoUnidadE daoUnidad = new DaoUnidadE();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
//            HttpSession sesionUnidad = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
//            Unidadensenianza u = daoUnidad.verPorNombreUnidad(session, sesionUnidad.getAttribute("unidadSeleccionada").toString());
            
            List<Tema> t=daoTema.verPorUnidad(session, codigo);
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
    
    public void cargarTemaEditar(int codigo){
        this.session = null;
        this.transaction = null;
        try {
            DaoTema daoTema = new DaoTema();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.tema=daoTema.verPorCodigoTema(session, codigo);
            
            RequestContext.getCurrentInstance().update("frmEditarTema:panelEditarTema");
            RequestContext.getCurrentInstance().execute("PF('dialogEditarTema').show()");            
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios se realizaron con éxito."));
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CARGAR TEMA EDITAR:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }
    
    public void actualizar() {
        this.session = null;
        this.transaction = null;
        try {
            DaoTema daoTema = new DaoTema();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            daoTema.actualizar(this.session, this.tema);
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

    
    //    setter and getter de atributos
    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }

    public Unidadensenianza getUnidadensenianza() {
        return unidadensenianza;
    }

    public void setUnidadensenianza(Unidadensenianza unidadensenianza) {
        this.unidadensenianza = unidadensenianza;
    }

    public List<Tema> getListaTemas() {
        return listaTemas;
    }

    public void setListaTemas(List<Tema> listaTemas) {
        this.listaTemas = listaTemas;
    }
    
      //Recupera la lista de unidades de enseñanza de la BD
    public List<Unidadensenianza> getListaUnidadensenianza() {
         DaoUnidadE daoUnidad = new DaoUnidadE();
        List<Unidadensenianza> unidades=daoUnidad.verTodo();
        this.listaUnidadensenianza= unidades;
        return listaUnidadensenianza;
    }

    public void setListaUnidadensenianza(List<Unidadensenianza> listaUnidadensenianza) {
        this.listaUnidadensenianza = listaUnidadensenianza;
    }
    
    
  
  
    
}
