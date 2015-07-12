/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Clases.Encrypt;
import Dao.DaoAdministrador;
import Dao.DaoEstudiante;
import Dao.DaoRol;
import Dao.DaoUnidadE;
import Dao.DaoUsuario;
import HibernateUtil.HibernateUtil;
import Pojo.Administrador;
import Pojo.Estudiante;
import Pojo.Rol;
import Pojo.Unidadensenianza;
import Pojo.Usuario;
import java.util.List;
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
 * @author KathyR
 */
@ManagedBean
@ViewScoped
public class BeanRUsuario {

    /**
     * Creates a new instance of BeanRUsuario
     */
    private Usuario usuario;
    private Rol rol;
    private List<Usuario> listaUsuarios;
    private List<Usuario> listaUsuarioFiltrado;
    private List<Rol> listaRoles;
    private String txtPasswordRepita;
    private Session session;
    private Transaction transaction;

    public BeanRUsuario() {
        this.usuario = new Usuario();
        this.usuario.setEstado(true);
        this.usuario.setGenero(true);
    }

    //Ingresa un nuevo Usuario a la BD
    public void registrar() {
        this.session = null;
        this.transaction = null;

        try {
            if (!this.usuario.getPassword().equals(this.txtPasswordRepita)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "Las contraseñas no coinciden"));
                return;
            }
            Dao.DaoUsuario daoUsuario = new DaoUsuario();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            Usuario u= daoUsuario.verPorUsername(session, usuario.getUsername()) ;
            if (u != null) {
                System.out.println(u.toString());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El nombre de usuario ya se encuentra registrado"));
                return;
            }
            
            this.usuario.setPassword(Encrypt.sha512(this.usuario.getPassword()));
            daoUsuario.registrar(this.session, this.usuario);
            
            if(this.usuario.getRol().getTipo().equals("Administrador")){
                Administrador admin= new Administrador();
                admin.setUsuario(daoUsuario.verPorUsername(session, usuario.getUsername()));
                DaoAdministrador daoAdmin=new DaoAdministrador();
                daoAdmin.registrar(session, admin);
            }else{
                if(this.usuario.getRol().getTipo().equals("Estudiante")){
                    Estudiante est= new Estudiante();
                    est.setUsuario(daoUsuario.verPorUsername(session, usuario.getUsername()));
                    DaoUnidadE daoUnidad=new DaoUnidadE();
                    Unidadensenianza unidadE=daoUnidad.verPorNombreUnidad(session, "UnidadBasica");
                    est.setUnidadensenianza(unidadE);
                    DaoEstudiante daoEst= new DaoEstudiante();
                    daoEst.registrar(session, est);
                }
            }

            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue realizado con éxito"));

            this.usuario = new Usuario();
            this.usuario.setEstado(true);
            this.usuario.setGenero(true);
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
            Dao.DaoUsuario daoUsuario = new DaoUsuario();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
//            if(daoUsuario.verPorUsername(session, usuario.getUsername())!=null){
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error:", "El nombre de usuario ya se encuentra registrado"));
//                return;
//            }
            daoUsuario.actualizar(this.session, this.usuario);
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

    //Recupera toda la lista de usuarios de la BD
    public List<Usuario> getAllUsuario() {
        this.session = null;
        this.transaction = null;

        try {
            DaoUsuario daoUsuario = new DaoUsuario();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.listaUsuarios = daoUsuario.verTodo(this.session);
            this.transaction.commit();
            return this.listaUsuarios;

        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR RECUPERAR USUARIOS:", "Contacte con el administrador" + ex.getMessage()));
            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    //Recuperar el usuario que ha iniciado sesión a través del BeanSLogin
    public Usuario getPorUsername() {
        this.session = null;
        this.transaction = null;

        try {
            DaoUsuario daoUsuario = new DaoUsuario();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            HttpSession sesionUsuario = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            this.usuario = daoUsuario.verUsuarioLogeado(session, sesionUsuario.getAttribute("usernameLogin").toString());
            this.transaction.commit();
            return this.usuario;

        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR OBTENER POR USERNAME:", "Contacte con el administrador" + ex.getMessage()));
            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public void cargarUsuarioEditar(int codigo){
        this.session = null;
        this.transaction = null;
        try {
            Dao.DaoUsuario daoUsuario = new DaoUsuario();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.usuario=daoUsuario.verPorCodigoUsuario(session, codigo);
            
            //Para cargar los datos en el panelEditarUsuario del formulario frmEditarUsuario
            RequestContext.getCurrentInstance().update("frmEditarUsuario:panelEditarUsuario");
            
            //Para mostrar el diálogo que contiene los datos del usuario con el widgetVar: dialogEditarUsuario
            RequestContext.getCurrentInstance().execute("PF('dialogEditarUsuario').show()");            
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios se realizaron con éxito."));
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CARGAR USUARIO EDITAR:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }
    public void cargarUsuarioDesactivar(int codigo){
        this.usuario=new Usuario();
        this.usuario.setEstado(true);
        this.session = null;
        this.transaction = null;
        try {
            Dao.DaoUsuario daoUsuario = new DaoUsuario();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.usuario=daoUsuario.verPorCodigoUsuario(session, codigo);
            System.out.println(this.usuario.toString());
            if(this.usuario.isEstado()){
                this.usuario.setEstado(false);
            }else{
                this.usuario.setEstado(true);
            }
            
            daoUsuario.actualizar(this.session, this.usuario);

            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Estado modificado con éxito."));
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR DESACTIVAR USUARIO:", "Contacte con el administrador, " + ex));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
        
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public String getTxtPasswordRepita() {
        return txtPasswordRepita;
    }

    public void setTxtPasswordRepita(String txtPasswordRepita) {
        this.txtPasswordRepita = txtPasswordRepita;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    //Recupera la lista de Roles de la BD
    public List<Rol> getListaRoles() {
        DaoRol daoRol = new DaoRol();
        List<Rol> roles = daoRol.verTodo();
        listaRoles = roles;
        return listaRoles;
    }

    public void setListaRoles(List<Rol> listaRoles) {
        this.listaRoles = listaRoles;
    }

    public List<Usuario> getListaUsuarioFiltrado() {
        return listaUsuarioFiltrado;
    }

    public void setListaUsuarioFiltrado(List<Usuario> listaUsuarioFiltrado) {
        this.listaUsuarioFiltrado = listaUsuarioFiltrado;
    }
    
    

}
