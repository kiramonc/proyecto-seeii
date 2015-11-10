/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Clases.RedBayesiana.CrearBayesNetwork1;
import Dao.DaoConcepto;
import Dao.DaoTema;
import Dao.DaoTest;
import Dao.DaoUnidadE;
import HibernateUtil.HibernateUtil;
import Pojo.Concepto;
import Pojo.Tema;
import Pojo.Test;
import Pojo.Unidadensenianza;
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
        this.tema = new Tema();
    }
    //    metodos para crear,actualizar y ver temas

    public void registrar(Unidadensenianza unidadE) {
        this.session = null;
        this.transaction = null;
        try {

            Dao.DaoTema daoTema = new DaoTema();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            System.out.println("abre la sesion y transaccion correctamente");
            Tema t = daoTema.verPorTemaname(session, tema.getNombre());
            System.out.println("consulta de existencia de tema realizada con éxito");
            if (t != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El nombre de Tema ya se encuentra registrado"));
                return;
            }
            this.tema.setUnidadensenianza(unidadE);
            this.tema.setEstado(true);
            daoTema.registrar(this.session, this.tema);

            // Crear nodo Tema en la red bayesiana
            CrearBayesNetwork1 redBayesiana = new CrearBayesNetwork1();
            redBayesiana.crearTema(unidadE.getNombreUnidad(), tema.getNombre());

            Test test = new Test();
            test.setTema(daoTema.verPorTemaname(session, tema.getNombre()));
            DaoTest daoTest = new DaoTest();
            daoTest.registrar(session, test);

            /*REGISTRO DE CONCEPTO GENERAL PARA PREGUNTA LISTENING(1)*/
            DaoConcepto daoConcepto = new DaoConcepto();
            Concepto c = new Concepto();
            c.setEstado(true);
            c.setDescripcion("VOCABULARY");
            c.setNombreConcepto(tema.getNombre() + " Vocabulary");
            c.setTraduccion("TODO");
            c.setTema(daoTema.verPorTemaname(session, tema.getNombre()));
            daoConcepto.registrar(session, c);

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
        this.session = null;
        this.transaction = null;
        try {
            DaoTema daoTema = new DaoTema();
//            DaoUnidadE daoUnidad = new DaoUnidadE();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
//            HttpSession sesionUnidad = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
//            Unidadensenianza u = daoUnidad.verPorNombreUnidad(session, sesionUnidad.getAttribute("unidadSeleccionada").toString());

            List<Tema> t = daoTema.verPorUnidad(session, codigo);
            transaction.commit();
            System.out.println("NO EXISTE ERROR EN LOS TEMAS");
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

    public void abrirDialogoCrearTema() {
        try {
            this.tema = new Tema();
            RequestContext.getCurrentInstance().update("frmCrearTema:panelCrearTema");
            RequestContext.getCurrentInstance().execute("PF('dialogCrearTema').show()");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios se realizaron con éxito."));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CARGAR TEMA CREAR:", "Contacte con el administrador" + ex.getMessage()));
        }
    }

    public void cargarTemaEditar(int codigo) {
        this.session = null;
        this.transaction = null;
        try {
            DaoTema daoTema = new DaoTema();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.tema = daoTema.verPorCodigoTema(session, codigo);

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
            Tema temaAnterior = daoTema.verPorCodigoTema(session, tema.getIdTema());
            String nombreUnidad = temaAnterior.getUnidadensenianza().getNombreUnidad();
            String nombre = temaAnterior.getNombre();
            this.transaction.commit();
            this.session.close();
            temaAnterior = null;

            // Crear nodo Tema en la red bayesiana
            if (!nombre.equals(tema.getNombre())) {
                this.session = HibernateUtil.getSessionFactory().openSession();
                this.transaction = session.beginTransaction();
                daoTema.actualizar(this.session, this.tema);
                DaoConcepto daoConcepto = new DaoConcepto();
                Concepto conceptoGeneral= daoConcepto.verConceptoGeneral(session, this.tema.getIdTema(), nombre);
                conceptoGeneral.setNombreConcepto(tema.getNombre() + " Vocabulary");
                daoConcepto.actualizar(session, conceptoGeneral);
                CrearBayesNetwork1 redBayesiana = new CrearBayesNetwork1();
                redBayesiana.editarTema(nombreUnidad, nombre, tema.getNombre());
                this.transaction.commit();
            }

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
            this.tema = new Tema();
        }
    }

    public void eliminar() {
        this.session = null;
        this.transaction = null;
        try {
            DaoTema daoTema = new DaoTema();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            Tema temaAnterior = daoTema.verPorCodigoTema(session, tema.getIdTema());
            String nombreUnidad = temaAnterior.getUnidadensenianza().getNombreUnidad();

            this.transaction.commit();
            this.session.close();
            temaAnterior = null;

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            daoTema.eliminar(this.session, this.tema);
            // Eliminar nodo Tema en la red bayesiana
            CrearBayesNetwork1 redBayesiana = new CrearBayesNetwork1();
            redBayesiana.eliminarTema(nombreUnidad, tema.getNombre());
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Tema eliminado correctamente."));
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL ELIMINAR:", "Contacte con el administrador, " + ex));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
            this.tema = new Tema();
        }
    }

    public void abrirDialogoVerConceptos(int codigo) {
        this.session = null;
        this.transaction = null;
        try {
            DaoTema daoTema = new DaoTema();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.tema = daoTema.verPorCodigoTema(session, codigo);
            RequestContext.getCurrentInstance().update("frmVerConceptos:panelVerConceptos");
            RequestContext.getCurrentInstance().execute("PF('dialogVerConceptos').show()");
            this.transaction.commit();
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios se realizaron con éxito."));
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CARGAR CONCEPTO:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public Tema consultarTemaPorNombre(String tema) {
        try {
            DaoTema daotema = new DaoTema();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            Tema t = daotema.verPorTemaname(session, tema);
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
        List<Unidadensenianza> unidades = daoUnidad.verTodo();
        this.listaUnidadensenianza = unidades;
        return listaUnidadensenianza;
    }

    public void setListaUnidadensenianza(List<Unidadensenianza> listaUnidadensenianza) {
        this.listaUnidadensenianza = listaUnidadensenianza;
    }

}
