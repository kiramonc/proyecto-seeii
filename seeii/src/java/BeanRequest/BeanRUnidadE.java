/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Dao.DaoAdministrador;
import Dao.DaoTema;
import Dao.DaoUnidadE;
import Dao.DaoUsuario;
import HibernateUtil.HibernateUtil;
import Pojo.Administrador;
import Pojo.Tema;
import Pojo.Unidadensenianza;
import Pojo.Usuario;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;

/**
 *
 * @author silvy
 */
@ManagedBean
@ViewScoped
public class BeanRUnidadE {

    private Unidadensenianza unidadE;
    private List<Unidadensenianza> listaunidadE;
    private List<Unidadensenianza> listaUnidadEFiltrada;
    private List<Tema> listaTemas;
    private Session session;
    private Transaction transaction;

    public BeanRUnidadE() {
        this.unidadE = new Unidadensenianza();
    }

    //Metodos
    public void registrar() {
        this.session = null;
        this.transaction = null;
        try {
            Dao.DaoUnidadE daoUnidadE = new DaoUnidadE();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            Unidadensenianza u = daoUnidadE.verPorNombreUnidad(session, unidadE.getNombreUnidad());
            if (u != null) {
                System.out.println(u.toString());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "La unidad ya se encuentra registrada"));
                return;
            }
            DaoUsuario daoUsuario = new DaoUsuario();
            HttpSession sesionUsuario = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            Usuario usuario = daoUsuario.verUsuarioLogeado(session, sesionUsuario.getAttribute("usernameLogin").toString());
            DaoAdministrador daoAdmin = new DaoAdministrador();
            Administrador admin = daoAdmin.verPorCodigoUsuario(session, usuario.getIdUsuario());
            this.unidadE.setAdministrador(admin);
            daoUnidadE.registrar(this.session, this.unidadE);

            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue realizado con éxito"));
            this.unidadE = new Unidadensenianza();
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR REGISTRO:", "Contacte con el administrador" + ex.getMessage()));
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
            DaoUnidadE daoUnidadE = new DaoUnidadE();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            System.out.println("EL ID DE LA UNIDAD ANTES ES: " + unidadE.getId());
            daoUnidadE.actualizar(this.session, this.unidadE);
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

    // Recuperar un determinado unida (se utliza en la clase UnidadConverter)
    public Unidadensenianza consultarUnidadPorNombre(String unidad) {
        try {
            DaoUnidadE daoUnidad = new DaoUnidadE();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            Unidadensenianza t = daoUnidad.verPorNombreUnidad(session, unidad);
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
    
    public Unidadensenianza consultarUnidadPorCodigo(int idUnidad) {
        this.session=null;
        this.transaction=null;
        try {
            DaoUnidadE daoUnidad = new DaoUnidadE();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            Unidadensenianza t = daoUnidad.verPorCodigoUnidad(session, idUnidad);
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
    
        public void cargarUnidadEditar(int codigo){
        this.session = null;
        this.transaction = null;
        try {
            DaoUnidadE daoUnidad = new DaoUnidadE();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.unidadE=daoUnidad.verPorCodigoUnidad(session, codigo);
            
            RequestContext.getCurrentInstance().update("frmEditarUnidad:panelEditarUnidad");
            RequestContext.getCurrentInstance().execute("PF('dialogEditarUnidad').show()");            
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios se realizaron con éxito."));
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CARGAR UNIDAD EDITAR:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }


    public List<Unidadensenianza> getAllUnidadE() {
        this.session = null;
        this.transaction = null;
        try {
            DaoUnidadE daoUnidadE = new DaoUnidadE();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.listaunidadE = daoUnidadE.verTodo(session);
            this.transaction.commit();
            return this.listaunidadE;

        } catch (HibernateException ex) {
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

    public String cargarUnidadModificar(int idUnidad) {
        this.session = null;
        this.transaction = null;
        try {
            DaoUnidadE daoUnidadE = new DaoUnidadE();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            System.out.println("CONSULTA UNIDAD CON ID: "+idUnidad);
            this.unidadE = daoUnidadE.verPorCodigoUnidad(session, idUnidad);
            System.out.println("UNIDAD RECUPERADA "+unidadE.getNombreUnidad()+" CON ID: "+unidadE.getId());

            //Para cargar los datos en el panelEditarUnidad del formulario frmEditarUnidad
            RequestContext.getCurrentInstance().update("frmModificarUnidad:panelEditarUnidad");

            //Para mostrar el diálogo que contiene los datos de la unidad con el widgetVar: dialogEditarUnidad
//            RequestContext.getCurrentInstance().execute("PF('frmModificarUnidad').show()");
            this.transaction.commit();
            System.out.println("HOLA SILVIAAAAAAAAAAAAAAAAAAAA");
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios se realizaron con éxito."));
            return "/admin/unidad/modificar_unidad";
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR:", "Contacte con el administrador" + ex.getMessage()));
            System.out.println("Errorrrrrrrr: " + ex);
            return "";
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
        
    }

    public void cargarUnidadEliminar(int idUnidad) {
        this.session = null;
        this.transaction = null;
        try {
            DaoUnidadE daoUnidadE = new DaoUnidadE();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.unidadE = daoUnidadE.verPorCodigoUnidad(session, idUnidad);

            daoUnidadE.eliminar(this.session, this.unidadE);
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Unidad de enseñanza eliminada correctamente."));
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

    public Unidadensenianza getUnidadE() {
        return unidadE;
    }

    public void setUnidadE(Unidadensenianza unidadE) {
        this.unidadE = unidadE;
    }

    public List<Unidadensenianza> getListaunidadE() {
        return listaunidadE;
    }

    public void setListaunidadE(List<Unidadensenianza> listaunidadE) {
        this.listaunidadE = listaunidadE;
    }

    public List<Unidadensenianza> getListaUnidadEFiltrada() {
        return listaUnidadEFiltrada;
    }

    public void setListaUnidadEFiltrada(List<Unidadensenianza> listaUnidadEFiltrada) {
        this.listaUnidadEFiltrada = listaUnidadEFiltrada;
    }

}
