/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Dao.DaoFicha;
import Dao.DaoTema;
import HibernateUtil.HibernateUtil;
import Pojo.Ficha;
import Pojo.Tema;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author silvy
 */
@ManagedBean
@ViewScoped
public class BeanRFicha {

    private Session session;
    private Transaction transaction;

    //ATRIBUTO PARA CREAR UNA FICHA    
    private Ficha ficha;

    private String nombreImagen;
    private UploadedFile imagen;

    public BeanRFicha() {
    }

//    método para abrir el dialogo para crear una ficha.
    public void abrirDialogoCrearFicha(int idTema) {
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            DaoTema daoTema = new DaoTema();
            Tema tema = daoTema.verPorCodigoTema(session, idTema);

            this.ficha = new Ficha();
            this.ficha.setTema(tema);
            this.transaction.commit();

            RequestContext.getCurrentInstance().update("frmCrearFicha:panelCrearFicha");
            RequestContext.getCurrentInstance().execute("PF('dialogCrearFicha').show()");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Obtner el tema para fijar en la ficha."));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR OBTENER TEMA DE LA FICHA:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    //metodo para crear una ficha
    public void registrar() {
        this.session = null;
        this.transaction = null;
        int idficha = 0;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            //verifica si existe un ficha con ese nombre y en el mismo tema y su estado=true
            DaoFicha daoFicha = new DaoFicha();
            List<Ficha> lista = daoFicha.obtenerFichaRepetidos(session, this.ficha.getNombreFicha(), this.ficha.getTema().getIdTema(), true);
            //si no existe una ficha <=> crea la ficha
            if (lista.isEmpty()) {
                this.ficha.setEstadoAprendizaje("no aprendido");
                this.ficha.setEstado(true);
                boolean stateF = daoFicha.registrar(session, ficha);
                this.transaction.commit();

                //verifica si se crea la ficha para crear una imagen.
                if (stateF) {
                    try {
                        idficha = obtenerIDfichaCreada();//obtener el id ficha  para fijar al nombre de la imagen
                        actualizarImg(idficha);
                    } catch (IOException ex) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR DE LECTURA ESCRITURA:", "Contacte con el administrador" + ex.getMessage()));
                    }
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro de la ficha fue realizado con éxito"));
            } else { //caso contrario muestra msj que ya existe una ficha
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR:", "El (Nombre de la Ficha) ya se encuentra registrado"));
            }
//            this.ficha = new Ficha();
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR:", "Contacte con el administrador" + ex.getMessage()));
            System.out.println("ERROR:................................ " + ex.getMessage());
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }

    }
//metodo para obtener una ficha creada anteriormente

    public int obtenerIDfichaCreada() {
        this.session = null;
        this.transaction = null;
        int id = 0;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            //verifica si existe un ficha con ese nombre y en el mismo tema y su estado=true
            DaoFicha daoFicha = new DaoFicha();
            Ficha lista = daoFicha.verFichaPorAtributos(session, this.ficha.getNombreFicha(), this.ficha.getDescripcion(), this.ficha.getTema().getIdTema());
            this.transaction.commit();
            id = lista.getIdFicha();

        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR:", "Contacte con el administrador" + ex.getMessage()));
            System.out.println("ERROR:................................ " + ex.getMessage());
        }
        return id;
    }

    //metod para crear la imagen
    public void actualizarImg(int idficha) throws IOException {
        InputStream inputS = null;
        OutputStream outputS = null;
        try {
            if (imagen.getSize() <= 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR:", "Debe seleccionar una imagen"));
                return;
            }

            inputS = this.imagen.getInputstream();
            ServletContext servletContex = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String imgItems = (String) servletContex.getRealPath("/resources/imagen/imgFichas") + "/" + idficha + ".jpg";

            File f = new File(imgItems);
            outputS = new FileOutputStream(f);

            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputS.read(bytes)) != -1) {
                outputS.write(bytes, 0, read);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Imagen de Item actualizado correctamente."));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL GUARDAR IMAGEN:", "Contacte con el administrador, " + ex));
        } finally {
            if (inputS != null) {
                inputS.close();
            }
            if (outputS != null) {
                outputS.close();
            }
        }
    }

    public List<Ficha> getFichasPorTema(Tema tema) {
        this.session = null;
        this.transaction = null;
        try {
            DaoFicha daoFicha = new DaoFicha();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            List<Ficha> listaFichas = daoFicha.verListfichasActivasPorTema(session, tema.getIdTema());
            transaction.commit();
            return listaFichas;
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

    //metodo para ver los detalles de la ficha.
    public void abrirDialogoVerFicha(int codigo) {
        this.session = null;
        this.transaction = null;
        try {
            DaoFicha daoItem = new DaoFicha();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            this.ficha = daoItem.verPorCodigoFicha(session, codigo);
//                        this.imagen = f;

            RequestContext.getCurrentInstance().update("frmVerFicha:panelVerFicha");
            RequestContext.getCurrentInstance().execute("PF('dialogVerFicha').show()");
            this.transaction.commit();
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CARGAR FICHA EDITAR:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    //metodo para actualizar la ficha
    public void actualizar() {
        this.session = null;
        this.transaction = null;

        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            //obtener la lista de fichas con (nombres) repetidos
            DaoFicha daoFicha = new DaoFicha();
            List<Ficha> lista = daoFicha.obtenerFichaRepetidos(session, this.ficha.getNombreFicha(), this.ficha.getTema().getIdTema(), true);

            if (lista.isEmpty()) { //no hay ninguna ficha con ese [nombre ficha]
                daoFicha.actualizar(this.session, this.ficha);//actualiza la ficha
                this.transaction.commit();
                //verifica si existe el nombre del imagen no sea ""(cadena vacia)
                if (!this.imagen.getFileName().equals("")) {
                    try {
                        //si existe una imagen la modifica 
                        actualizarImg(this.ficha.getIdFicha());
                    } catch (IOException ex) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR DE LECTURA ESCRITURA:", "Contacte con el administrador" + ex.getMessage()));
                    }
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Se modificó la ficha se realizaron con éxito."));
            } else {
                //se realiza un for comprar si existe la ficha(con el mismo nombre) para eliminarla de la lista
                for (int i = 0; i < lista.size(); i++) {
                    if (lista.get(i).getIdFicha() == this.ficha.getIdFicha()) {
                        lista.remove(i);
                    }
                }
                //si la lista es vacia, quiere decir que no hay Nombre de fichas repetidas
                if (lista.isEmpty()) {
                    //actualiza la ficha con los nuevos datos
                    daoFicha = new DaoFicha();
                    this.session = HibernateUtil.getSessionFactory().openSession();
                    this.transaction = session.beginTransaction();
                    daoFicha.actualizar(this.session, this.ficha);
                    this.transaction.commit();
                    //verifica si existe el nombre del imagen no sea ""(cadena vacia)
                    if (!this.imagen.getFileName().equals("")) {
                        try {
                            //si existe una imagen la modifica 
                            actualizarImg(this.ficha.getIdFicha());
                        } catch (IOException ex) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR DE LECTURA ESCRITURA:", "Contacte con el administrador" + ex.getMessage()));
                        }
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Se modificó la ficha se realizaron con éxito."));
                } else {
                    //caso contrario la lista contiene (nombres)de fichas repetidas
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR:", "El (Nombre de la Ficha) ya se encuentra registrado"));
                }
            }

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
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            DaoFicha daoFicha = new DaoFicha();
            this.ficha.setEstado(false);
            daoFicha.actualizar(this.session, this.ficha);//actualiza la ficha (ELININACION LOGICO)
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Se Eliminó la ficha correctamente."));

        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR ElIMINAR FICHA:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public Ficha getFicha() {
        return ficha;
    }

    public void setFicha(Ficha ficha) {
        this.ficha = ficha;
    }

    public UploadedFile getImagen() {
        return imagen;
    }

    public void setImagen(UploadedFile imagen) {
        this.imagen = imagen;
    }
}
