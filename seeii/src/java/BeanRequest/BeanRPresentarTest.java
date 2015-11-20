/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Clases.RedBayesiana.BayesNetworkIngles;
import Dao.DaoConcepto;
import Dao.DaoItem;
import Dao.DaoPregunta;
import Dao.DaoTema;
import HibernateUtil.HibernateUtil;
import Pojo.Concepto;
import Pojo.Item;
import Pojo.Pregunta;
import Pojo.Tema;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class BeanRPresentarTest {

    private Tema test = new Tema();
    private List<Tema> listaTest;
    private List<Pregunta> listaPreguntas;
    private Session session;
    private Transaction transaction;
    private Pregunta preguntaSeleccionada;
    private List<Item> listaItems;
    private String respuestTemp;
    private String resultado;
    private String imgItemSeleccionado;
    private boolean correcto;

    public BeanRPresentarTest() {

    }

    public Pregunta obtenerPregunta(int codigo) {
        this.session = null;
        this.transaction = null;

        try {
            DaoPregunta daoPregunta = new DaoPregunta();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            List<Pregunta> listaPregunta= new ArrayList<>();
            daoPregunta.verPorTest(session, codigo);

            // Crear objeto del análisis de la red bayesiana
            BayesNetworkIngles bn = new BayesNetworkIngles();
            //obtención de una pregunta según el análisis
            preguntaSeleccionada = listaPregunta.get(bn.generarCodigoPregunta(listaPregunta));
            System.out.println("La pregunta mostrada en el enunciado es:" + preguntaSeleccionada.getEnunciado());
//            preguntaSeleccionada = daoPregunta.verPorCodigoPregunta(session, codigo);
            transaction.commit();
            return preguntaSeleccionada;
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR al obtener pregunta por codigo:", "Contacte con el administrador" + ex.getMessage()));
            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public void guardarRespuestaTemp(String respuesta) {
        this.respuestTemp = respuesta;
        if (preguntaSeleccionada.getDificultad() == 2.0 || preguntaSeleccionada.getDificultad() == 4.0) {
            /*if (this.preguntaSeleccionada.getDificultad().getNombreConcepto().equalsIgnoreCase(respuestTemp)) {
                this.correcto = true;
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Respuesta correcta:\n\n", respuestTemp));
            } else {
                this.correcto = false;
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Respuesta incorrecta:\n\n", respuestTemp));
            }
            if (correcto) {
                if (preguntaSeleccionada.getPeso() == 2.0) {
                    this.resultado = "¡CONGRATULATIONS!";
                } else {
                    this.resultado = respuestTemp;
                }
            } else {
                if (preguntaSeleccionada.getPeso() == 2.0) {
                    this.resultado = "Try again";
                } else {
                    this.resultado = "Try again.";
                }
            }

        } else {
            if (preguntaSeleccionada.getPeso() == 1.0 || preguntaSeleccionada.getPeso() == 3.0) {
                this.session = null;
                this.transaction = null;

                try {
                    DaoConcepto daoConcepto = new DaoConcepto();
                    DaoTema daoTema = new DaoTema();
                    DaoTest daoTest = new DaoTest();
                    this.session = HibernateUtil.getSessionFactory().openSession();
                    this.transaction = session.beginTransaction();
                    Test test = daoTest.verPorCodigoTest(session, preguntaSeleccionada.getTest().getIdTest());
                    Tema tema = daoTema.verPorCodigoTema(session, test.getTema().getIdTema());
                    List<Concepto> listaConceptos = daoConcepto.verPorTema(session, tema.getIdTema());
                    transaction.commit();
                    this.correcto = false;
                    if (preguntaSeleccionada.getPeso() == 1.0) {
                        for (int i = 0; i < listaConceptos.size(); i++) {
                            if (listaConceptos.get(i).getNombreConcepto().equalsIgnoreCase(respuestTemp)) {
                                this.correcto = true;
                                break;
                            }
                        }
                    } else {
                        DaoItem daoItem = new DaoItem();
                        Item itemSelect = null;
                        this.transaction = session.beginTransaction();
                        for (int i = 0; i < listaConceptos.size(); i++) {
                            if (listaConceptos.get(i).getNombreConcepto().equalsIgnoreCase(respuestTemp)) {
                                System.out.println("el valor ahora es: "+this.correcto);
                                this.correcto = true;
                                itemSelect = daoItem.verPorNombreItem(session, respuestTemp);
                                imgItemSeleccionado = itemSelect.getImgItem();
                                break;
                            }
                        }
                        System.out.println("el valor final: "+this.correcto);
                        transaction.commit();
                    }
                    
                    if (correcto) {
                        if (preguntaSeleccionada.getPeso() == 1.0) {
                            this.resultado = respuestTemp;
                        } else {
                            this.resultado = "This is " + respuestTemp + ". Now repeat.";
                        }
//                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Respuesta correcta:\n\n", respuestTemp));
                    } else {
                        if (preguntaSeleccionada.getPeso() == 1.0) {
                            this.resultado = "Try again";
                        } else {
                            this.resultado = "Try again. "+respuestTemp;
                        }
//                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Respuesta incorrecta:\n\n", respuestTemp));
                    }

                } catch (Exception ex) {
                    if (this.transaction != null) {
                        this.transaction.rollback();
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CONSULTAR CONCEPTO POR TEST - TEMA:", "Contacte con el administrador" + ex.getMessage()));
                } finally {
                    if (this.session != null) {
                        this.session.close();
                    }
                }
            }

        }
        if(preguntaSeleccionada.getPeso()==3.0){
            RequestContext.getCurrentInstance().update("frmResultadoSpeak:panelResultadoSpeak");
        RequestContext.getCurrentInstance().execute("PF('dialogResultadoSpeak').show()");
        }else{
        RequestContext.getCurrentInstance().update("frmResultado:panelResultado");
        RequestContext.getCurrentInstance().execute("PF('dialogResultado').show()");
            */
        }

    }

    public String actualizarPagina() {
        return "test";
    }

    public void compRespuestaListening1(String respuesta) {
        this.respuestTemp = respuesta;
        this.session = null;
        this.transaction = null;

        try {
            DaoConcepto daoConcepto = new DaoConcepto();
            DaoTema daoTema = new DaoTema();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            Tema tema = daoTema.verPorCodigoTema(session, test.getIdTema());
            List<Concepto> listaConceptos = daoConcepto.verPorTema(session, tema.getIdTema());
            transaction.commit();
            this.correcto = false;
            for (int i = 0; i < listaConceptos.size(); i++) {
                if (listaConceptos.get(i).getNombreConcepto().equalsIgnoreCase(respuestTemp)) {
                    this.correcto = true;
                    break;
                }
            }
            if (correcto) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Respuesta correcta:\n\n", respuestTemp));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Respuesta incorrecta:\n\n", respuestTemp));
            }

        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CONSULTAR CONCEPTO POR TEST - TEMA:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public List<Item> itemsPorPregunta() {
        this.session = null;
        this.transaction = null;

        try {
            DaoItem daoItem = new DaoItem();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            List<Item> t = daoItem.verPorPregunta(session, preguntaSeleccionada.getIdPregunta());
            System.out.println("LOS ITEMS MOSTRADOS SON PARA LA PREGUNTA:" + preguntaSeleccionada.getDificultad());
            transaction.commit();
            return t;
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR al obtener items por pregunta:", "Contacte con el administrador" + ex.getMessage()));
            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public List<Pregunta> cargarListaPreguntas() {
        this.session = null;
        this.transaction = null;

        try {
            DaoPregunta daoPregunta = new DaoPregunta();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            daoPregunta.verPorTest(session, 1);
            transaction.commit();
            return listaPreguntas;
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR al cargar preguntas del test 1:", "Contacte con el administrador" + ex.getMessage()));
            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public Tema consultarTestPorCodigo(int idTest) {
        this.session = null;
        this.transaction = null;
        try {
            DaoTema daoUnidad = new DaoTema();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            Tema t = daoUnidad.verPorCodigoTema(session, idTest);
            transaction.commit();
            this.test = t;
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

    public List<Tema> getAllTest() {
        this.session = null;
        this.transaction = null;
        try {
            DaoTema daoTest = new DaoTema();
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

    public boolean deshabilitarBotonCrearPregunta() {
        if (this.test != null) {
            return false;
        }
        return true;
    }

    public Tema getTest() {
        return test;
    }

    public void setTest(Tema test) {
        this.test = test;
    }

    public List<Tema> getListaTest() {
        return listaTest;
    }

    public void setListaTest(List<Tema> listaTest) {
        this.listaTest = listaTest;
    }

    public List<Pregunta> getListaPreguntas() {
        return listaPreguntas;
    }

    public void setListaPreguntas(List<Pregunta> listaPreguntas) {
        this.listaPreguntas = listaPreguntas;
    }

    public Pregunta getPreguntaSeleccionada() {
        return preguntaSeleccionada;
    }

    public void setPreguntaSeleccionada(Pregunta preguntaSeleccionada) {
        this.preguntaSeleccionada = preguntaSeleccionada;
    }

    public List<Item> getListaItems() {
        return listaItems;
    }

    public void setListaItems(List<Item> listaItems) {
        this.listaItems = listaItems;
    }

    public String getRespuestTemp() {
        return respuestTemp;
    }

    public void setRespuestTemp(String respuestTemp) {
        this.respuestTemp = respuestTemp;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getImgItemSeleccionado() {
        return imgItemSeleccionado;
    }

    public void setImgItemSeleccionado(String imgItemSeleccionado) {
        this.imgItemSeleccionado = imgItemSeleccionado;
    }

    public boolean isCorrecto() {
        return correcto;
    }

    public void setCorrecto(boolean correcto) {
        this.correcto = correcto;
    }
    
}
