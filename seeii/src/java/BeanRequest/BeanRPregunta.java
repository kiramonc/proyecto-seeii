/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Dao.DaoConcepto;
import Dao.DaoPregConc;
import Dao.DaoPregunta;
import Dao.DaoTema;
import HibernateUtil.HibernateUtil;
import Pojo.Concepto;
import Pojo.PregConc;
import Pojo.Pregunta;
import Pojo.Tema;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

/**
 *
 * @author KathyR
 */
@ManagedBean
@ViewScoped
public class BeanRPregunta {

    private Pregunta pregunta;
    private Tema tema = new Tema();
    private List<Pregunta> listaPreguntas;
    private Session session;
    private Transaction transaction;
    private HashMap<Integer, List<Concepto>> pregConc;
    private String tipoPreg;
    private Concepto concepto;
    private List<Concepto> target = new ArrayList<>();

    private DualListModel<Concepto> modelConc = new DualListModel<Concepto>();

    //constructor
    public BeanRPregunta() {
        this.pregunta = new Pregunta();
    }

    public void registrar(Tema tema) {
        this.session = null;
        this.transaction = null;
        try {
            DaoPregunta daoPregunta = new DaoPregunta();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.pregunta.setEstado(true);
            this.pregunta.setNombrePreg("NombrePreg");
            daoPregunta.registrar(this.session, this.pregunta);
            Pregunta preg = daoPregunta.verPorCodigoPregunta(session, daoPregunta.verUltimoRegistro(session));
            this.transaction.commit();
            this.session.close();

            DaoPregConc daoPregConc = new DaoPregConc();
            List<Concepto> listaC = modelConc.getTarget();
            PregConc pregConc = new PregConc();

            for (int i = 0; i < listaC.size(); i++) {
                this.session = HibernateUtil.getSessionFactory().openSession();
                this.transaction = session.beginTransaction();
                pregConc.setPregunta(preg);
                pregConc.setConcepto(listaC.get(i));
                daoPregConc.registrar(session, pregConc);
                this.transaction.commit();
                this.session.close();
                pregConc = new PregConc();
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue realizado con éxito"));
            this.pregunta = new Pregunta();
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                if (this.session.isOpen()) {
                    this.session.close();
                }
            }
        }
    }

    public List<Pregunta> getPreguntasPorTest(Tema tema) {
        this.tema = tema;
        this.session = null;
        this.transaction = null;
        try {
            DaoPregunta daoPregunta = new DaoPregunta();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            List<PregConc> t = daoPregunta.verPorTest(session, tema.getIdTema());

            this.listaPreguntas = new ArrayList<>(); // Lista de preguntas
            this.pregConc = new HashMap<>(); // Pregunta con la lista de conceptos
            List<Concepto> lstConceptos = new ArrayList<>();
            for (int i = 0; i < t.size(); i++) {
                if (!listaPreguntas.isEmpty()) {
                    if (!listaPreguntas.contains(t.get(i).getPregunta())) {
                        listaPreguntas.add(t.get(i).getPregunta());
                    }
                } else {
                    listaPreguntas.add(t.get(i).getPregunta());
                }
                if (!pregConc.isEmpty()) {
                    if (!pregConc.containsKey(t.get(i).getPregunta().getIdPregunta())) {
                        lstConceptos.add(t.get(i).getConcepto());
                        pregConc.put(t.get(i).getPregunta().getIdPregunta(), lstConceptos);
                        lstConceptos.clear();
                    } else {
                        lstConceptos = pregConc.get(t.get(i).getPregunta().getIdPregunta());
                        lstConceptos.add(t.get(i).getConcepto());
                        pregConc.put(t.get(i).getPregunta().getIdPregunta(), lstConceptos);
                        lstConceptos.clear();
                    }
                } else {
                    lstConceptos.add(t.get(i).getConcepto());
                    pregConc.put(t.get(i).getPregunta().getIdPregunta(), lstConceptos);
                    lstConceptos.clear();
                }
            }
            transaction.commit();
            return listaPreguntas;
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

    public void abrirDialogoCrearPregunta(int codigo) {
        this.session = null;
        this.transaction = null;
        try {
            DaoTema daoTema = new DaoTema();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.tema = daoTema.verPorCodigoTema(session, codigo);
            this.pregunta = new Pregunta();

            BeanRConcepto brC = new BeanRConcepto();
            List<Concepto> source = brC.getConceptosPorTema(tema);
            List<Concepto> target = new ArrayList<>();
            if (source.isEmpty()) {
                RequestContext.getCurrentInstance().update("frmCrearPregunta");
                RequestContext.getCurrentInstance().execute("PF('dialogSinConcepto').show()");
                return;
            }

            this.modelConc = new DualListModel<>(source, target);

            RequestContext.getCurrentInstance().update("frmCrearPregunta:panelCrearPregunta");
            RequestContext.getCurrentInstance().execute("PF('dialogCrearPregunta').show()");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios se realizaron con éxito."));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CARGAR CREACIÓN DE PREGUNTA:", "Contacte con el administrador" + ex.getMessage()));
        }
    }

    public void abrirDialogoVerItems(int codigo) {
        this.session = null;
        this.transaction = null;
        try {
            DaoPregunta daoItem = new DaoPregunta();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.pregunta = daoItem.verPorCodigoPregunta(session, codigo);
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

    public void cargarPreguntaEditar(int codigo) {
        this.session = null;
        this.transaction = null;
        try {
            DaoPregunta daoPregunta = new DaoPregunta();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.pregunta = daoPregunta.verPorCodigoPregunta(session, codigo);

            RequestContext.getCurrentInstance().update("frmEditarPregunta:panelEditarPregunta");
            RequestContext.getCurrentInstance().execute("PF('dialogEditarPregunta').show()");
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios se realizaron con éxito."));
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CARGAR PREGUNTA EDITAR:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public void actualizar() {
        DaoPregConc daoPregConc = new DaoPregConc();
        DaoConcepto daoConcepto = new DaoConcepto();
        List<Concepto> listaC = modelConc.getTarget();
        if (listaC.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR ACTUALIZAR:", "La pregunta tiene que hacer referencia almenos a un concepto"));
            return;
        }

        this.session = null;
        this.transaction = null;
        try {
            DaoPregunta daoPregunta = new DaoPregunta();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            daoPregunta.actualizar(this.session, this.pregunta); // Actualizar datos de la pregunta
            Pregunta preg = daoPregunta.verPorCodigoPregunta(session, this.pregunta.getIdPregunta());
            this.transaction.commit();
            this.session.close();
            // Actualizar registros en tabla PregConc

            PregConc pregConc = new PregConc();
            boolean banderaExiste = false; // false= el concepto no existe en la bd
            boolean banderaMantiene = false; // false= el concepto fue eliminado
            // Recorro lista conceptos nueva para ver si algún concepto fue agregado
            for (int i = 0; i < listaC.size(); i++) {
                for (int j = 0; j < target.size(); j++) {
                    if (listaC.get(i).getNombreConcepto().equals(target.get(j).getNombreConcepto())) {
                        banderaExiste = true; // existe el concepto en la bd
                    }
                }
                if (!banderaExiste) { // el concepto i es nuevo
                    this.session = HibernateUtil.getSessionFactory().openSession();
                    this.transaction = session.beginTransaction();
                    pregConc.setPregunta(preg);
                    pregConc.setConcepto(daoConcepto.verPorNombreConcepto(session, listaC.get(i).getNombreConcepto()));
                    daoPregConc.registrar(session, pregConc);
                    this.transaction.commit();
                    this.session.close();
                    pregConc = new PregConc();
                }
                banderaExiste = false;
            }

            for (int i = 0; i < target.size(); i++) { // recorro lista de conceptos anterior para ver si un concepto fue eliminiado
                for (int j = 0; j < listaC.size(); j++) {
                    if (target.get(i).getNombreConcepto().equals(listaC.get(j).getNombreConcepto())) {
                        banderaMantiene = true;
                    }
                }
                if (!banderaMantiene) { // el concepto i fue eliminado
                    this.session = HibernateUtil.getSessionFactory().openSession();
                    this.transaction = session.beginTransaction();
                    daoPregConc.eliminar(session, preg, daoConcepto.verPorNombreConcepto(session, target.get(i).getNombreConcepto()));
                    this.transaction.commit();
                    this.session.close();
                }
                banderaMantiene = false;
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios se realizaron con éxito."));
            this.pregunta = new Pregunta();
        } catch (Exception ex) {
            if (this.transaction != null) {
                if (this.transaction.isInitiator()) {
                    this.transaction.rollback();
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR ACTUALIZAR:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                if (this.session.isOpen()) {
                    this.session.close();
                }
            }
        }
    }

    public void eliminar() {
        this.session = null;
        this.transaction = null;
        try {
            DaoPregunta daoPregunta = new DaoPregunta();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            daoPregunta.eliminar(this.session, this.pregunta);
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Pregunta eliminada correctamente."));
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

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public List<Pregunta> getListaPreguntas() {
        return listaPreguntas;
    }

    public void setListaPreguntas(List<Pregunta> listaPreguntas) {
        this.listaPreguntas = listaPreguntas;
    }

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }

    public HashMap<Integer, List<Concepto>> getPregConc() {
        return pregConc;
    }

    public void setPregConc(HashMap<Integer, List<Concepto>> pregConc) {
        this.pregConc = pregConc;
    }

    public DualListModel<Concepto> getModelConc() {
        if (pregunta != null && target.isEmpty()) {
            this.session = null;
            this.transaction = null;
            try {
                DaoPregConc daoPregConc = new DaoPregConc();
                this.session = HibernateUtil.getSessionFactory().openSession();
                this.transaction = session.beginTransaction();

                List<PregConc> t = daoPregConc.verPorPregunta(session, pregunta.getIdPregunta());
                List<Concepto> lstConceptos = new ArrayList<>();
                for (int i = 0; i < t.size(); i++) {
                    lstConceptos.add(t.get(i).getConcepto());
                }
                this.target = lstConceptos;
                BeanRConcepto brC = new BeanRConcepto();
                List<Concepto> source = brC.getConceptosPorTema(tema);
                for (int i = 0; i < target.size(); i++) {
                    for (int j = 0; j < source.size(); j++) {
                        if (target.get(i).getNombreConcepto().equals(source.get(j).getNombreConcepto())) {
                            source.remove(j);
                        }
                    }
                }
                this.modelConc = new DualListModel<>(source, target);

            } catch (Exception ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CARGAR CREACIÓN DE PREGUNTA:", "Contacte con el administrador" + ex.getMessage()));
            }
        }
        return modelConc;
    }

    // listener que inicializa el target (lista de conceptos) cuando se cambia de pregunta
    public void cargarPregunta() {
        this.target = new ArrayList<>();
    }

    public void setModelConc(DualListModel<Concepto> modelConc) {
        this.modelConc = modelConc;
    }

    public Concepto getConcepto() {
        return concepto;
    }

    public void setConcepto(Concepto concepto) {
        this.concepto = concepto;
    }

    public String outputLabel() {
        String tipoPregunta = getTipoPreg();
        if (null != tipoPregunta) {
            switch (tipoPregunta) {
                case "listenF":
                    return "Escuchar (Fácil)";
                case "listenM":
                    return "Escuchar (Medio)";
                case "speakF":
                    return "Hablar (Fácil)";
                case "speakM":
                    return "Hablar (Medio)";
                case "speakD":
                    return "Escuchar y Hablar (Difícil)";
            }
        }
        return "Error";
    }

    public String outputLabel(Pregunta pregunta) {
        String tipoPregunta = getTipoPreg(pregunta);
        if (null != tipoPregunta) {
            switch (tipoPregunta) {
                case "listenF":
                    return "Escuchar (Fácil)";
                case "listenM":
                    return "Escuchar (Medio)";
                case "speakF":
                    return "Hablar (Fácil)";
                case "speakM":
                    return "Hablar (Medio)";
                case "speakD":
                    return "Escuchar y Hablar (Difícil)";
            }
        }
        return "Error";
    }

    public String getTipoPreg() {
        if (pregunta != null) {
            if (this.pregunta.getDificultad() == 2.0 && this.pregunta.getFdescuido() == 0.2 && this.pregunta.getIndiceDis() == 0.2) {
                tipoPreg = "listenF";
            } else {
                if (this.pregunta.getDificultad() == 3.0 && this.pregunta.getFdescuido() == 0.01 && this.pregunta.getIndiceDis() == 1.2) {
                    tipoPreg = "listenM";
                } else {
                    if (this.pregunta.getDificultad() == 2.0 && this.pregunta.getFdescuido() == 0.001 && this.pregunta.getIndiceDis() == 0.2) {
                        tipoPreg = "speakF";
                    } else {
                        if (this.pregunta.getDificultad() == 3.0 && this.pregunta.getFdescuido() == 0.001 && this.pregunta.getIndiceDis() == 1.2) {
                            tipoPreg = "speakM";
                        } else {
                            if (this.pregunta.getDificultad() == 4.0 && this.pregunta.getFdescuido() == 0.001 && this.pregunta.getIndiceDis() == 2.0) {
                                tipoPreg = "speakD";
                            }
                        }
                    }
                }
            }
        }
        return tipoPreg;
    }

    public String getTipoPreg(Pregunta pregunta) {
        if (pregunta != null) {
            if (pregunta.getDificultad() == 2.0 && pregunta.getFdescuido() == 0.2 && pregunta.getIndiceDis() == 0.2) {
                tipoPreg = "listenF";
            } else {
                if (pregunta.getDificultad() == 3.0 && pregunta.getFdescuido() == 0.01 && pregunta.getIndiceDis() == 1.2) {
                    tipoPreg = "listenM";
                } else {
                    if (pregunta.getDificultad() == 2.0 && pregunta.getFdescuido() == 0.001 && pregunta.getIndiceDis() == 0.2) {
                        tipoPreg = "speakF";
                    } else {
                        if (pregunta.getDificultad() == 3.0 && pregunta.getFdescuido() == 0.001 && pregunta.getIndiceDis() == 1.2) {
                            tipoPreg = "speakM";
                        } else {
                            if (pregunta.getDificultad() == 4.0 && pregunta.getFdescuido() == 0.001 && pregunta.getIndiceDis() == 2.0) {
                                tipoPreg = "speakD";
                            }
                        }
                    }
                }
            }
        }
        return tipoPreg;
    }

    public void setTipoPreg(String tipoPreg) {
        if (tipoPreg.equalsIgnoreCase("listenF")) {
            this.pregunta.setDificultad(2.0);
            this.pregunta.setFdescuido(0.2);
            this.pregunta.setIndiceDis(0.2);
        } else if (tipoPreg.equalsIgnoreCase("listenM")) {
            this.pregunta.setDificultad(3.0);
            this.pregunta.setFdescuido(0.01);
            this.pregunta.setIndiceDis(1.2);
        } else if (tipoPreg.equalsIgnoreCase("speakF")) {
            this.pregunta.setDificultad(2.0);
            this.pregunta.setFdescuido(0.001);
            this.pregunta.setIndiceDis(0.2);
        } else if (tipoPreg.equalsIgnoreCase("speakM")) {
            this.pregunta.setDificultad(3.0);
            this.pregunta.setFdescuido(0.001);
            this.pregunta.setIndiceDis(1.2);
        } else if (tipoPreg.equalsIgnoreCase("speakD")) {
            this.pregunta.setDificultad(4.0);
            this.pregunta.setFdescuido(0.001);
            this.pregunta.setIndiceDis(2.0);
        }
        this.tipoPreg = tipoPreg;
    }

}
