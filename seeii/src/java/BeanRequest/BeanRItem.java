/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Dao.DaoItem;
import Dao.DaoPregunta;
import HibernateUtil.HibernateUtil;
import Pojo.Item;
import Pojo.Pregunta;
import Pojo.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author KathyR
 */
@ManagedBean
@ViewScoped
public class BeanRItem {

    private Item item;
    private Pregunta pregunta;
    private List<Item> listaItem;
    private Session session;
    private Transaction transaction;
    private String nombreImagen;
    private byte[] contenidoImg;
    private UploadedFile imagen;

    //constructor
    public BeanRItem() {
        this.item = new Item();
    }

    public void registrar(Pregunta pregunta) {
        this.session = null;
        this.transaction = null;
        try {
            actualizarImg();
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR DE LECTURA ESCRITURA:", "Contacte con el administrador" + ex.getMessage()));
        }
        try {

            DaoItem daoItem = new DaoItem();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.item.setPregunta(this.pregunta);
            this.item.setImgItem(nombreImagen);
            this.item.setEstado(true);
            daoItem.registrar(session, item);
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue realizado con éxito"));
            this.item = new Item();
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR:", "Contacte con el administrador" + ex.getMessage()));
            System.out.println("ERRORRRRRRRRRRRRRRRRRRR: "+ex.getMessage());
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public List<Item> getItemsPorPregunta(Pregunta pregunta) {
        this.session = null;
        this.transaction = null;
        this.pregunta=pregunta;
        try {
            DaoItem daoItem = new DaoItem();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            List<Item> t = daoItem.verPorPregunta(session, this.pregunta.getIdPregunta());
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

    public void abrirDialogoCrearItem(int codigo) {
        this.session=null;
        this.transaction=null;
        try {
            this.item = new Item();
            DaoPregunta daoItem = new DaoPregunta();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.pregunta=daoItem.verPorCodigoPregunta(session, codigo);
            System.out.println("LA PREGUNTA QUE SE ESTÁ CARGANDO ES: "+this.pregunta.getDescripcion());
            RequestContext.getCurrentInstance().update("frmCrearItems:panelCrearItems");
            RequestContext.getCurrentInstance().execute("PF('dialogCrearItems').show()");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios se realizaron con éxito."));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CARGAR ITEM CREAR:", "Contacte con el administrador" + ex.getMessage()));
        }
    }

    public void cargarPreguntaEditar(int codigo) {
        this.session = null;
        this.transaction = null;
        try {
            DaoItem daoItem = new DaoItem();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.item = daoItem.verPorCodigoItem(session, codigo);

            RequestContext.getCurrentInstance().update("frmEditarItem:panelEditarItem");
            RequestContext.getCurrentInstance().execute("PF('dialogEditarItem').show()");
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios se realizaron con éxito."));
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CARGAR ITEM EDITAR:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }
    
    public void cargarItemsPregunta(int codigo) {
        this.session = null;
        this.transaction = null;
        try {
            DaoPregunta daoItem = new DaoPregunta();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.pregunta=daoItem.verPorCodigoPregunta(session, codigo);
            RequestContext.getCurrentInstance().update("frmVerItems:panelVerItems");
            RequestContext.getCurrentInstance().execute("PF('dialogVerItems').show()");
            this.transaction.commit();
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios se realizaron con éxito."));
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CARGAR ITEM EDITAR:", "Contacte con el administrador" + ex.getMessage()));
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
            actualizarImg();
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR DE LECTURA ESCRITURA:", "Contacte con el administrador" + ex.getMessage()));
        }
        
        try {
            DaoItem daoItem = new DaoItem();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.item.setImgItem(nombreImagen);
            daoItem.actualizar(this.session, this.item);
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
            DaoItem daoItem = new DaoItem();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            daoItem.eliminar(this.session, this.item);
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Item eliminado correctamente."));
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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public List<Item> getListaItem() {
        return listaItem;
    }

    public void setListaItem(List<Item> listaItem) {
        this.listaItem = listaItem;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    public byte[] getContenidoImg() {
        return contenidoImg;
    }

    public void setContenidoImg(byte[] contenidoImg) {
        this.contenidoImg = contenidoImg;
    }

    public UploadedFile getImagen() {
        return imagen;
    }

    public void setImagen(UploadedFile imagen) {
        this.imagen = imagen;
    }
   
    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        this.contenidoImg= file.getContents();
        this.nombreImagen=file.getFileName();
//        item.setImgItem(byte[]);
    }
    
    public void actualizarImg() throws IOException{
        InputStream inputS = null;
        OutputStream outputS= null;
        try{
            if(imagen.getSize()<=0){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR:", "Debe seleccionar una imagen"));
                return;
            }
//            else{
//                if(imagen.getSize()<=65535){
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR:", "El archivo no puede ser mayor a 64kb"));
//                return;
//            }
//            }
            
            ServletContext servletContex= (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String imgItems= (String) servletContex.getRealPath("/imgItems");
            outputS= new FileOutputStream(new File(imgItems+"/"+imagen.getFileName()));
            inputS= this.imagen.getInputstream();
            
            int read=0;
            byte[] bytes= new byte[1024];
            while((read=inputS.read(bytes))!=-1){
                outputS.write(bytes,0,read);
            }
            this.nombreImagen=imagen.getFileName();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Imagen de Item actualizado correctamente."));
        }catch(Exception ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL GUARDAR IMAGEN:", "Contacte con el administrador, " + ex));
        }finally{
            if(inputS!=null){
                inputS.close();
            }
            if(outputS!=null){
                outputS.close();
            }
        }
    }

//    public boolean guardarImagen(String ruta, String nombre) {
//        String insert = "insert into Imagenes(imagen,nombre) values(?,?)";
//        FileInputStream fis = null;
//        this.session = null;
//        this.transaction = null;
//        try {
//            DaoItem daoItem = new DaoItem();
//            this.session = HibernateUtil.getSessionFactory().openSession();
//            this.transaction = session.beginTransaction();
//        
//            File file = new File(ruta);
//            fis = new FileInputStream(file);
//            daoItem.saveImg(this.session, fis, file);
//            transaction.commit();
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "La imagen se guardó con éxito."));
//            return true;
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
//        return false;
//    }

}