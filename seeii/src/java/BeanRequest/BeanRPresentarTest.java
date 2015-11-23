/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Clases.RedBayesiana.CrearBayesDynamic;
import Dao.DaoConcepto;
import Dao.DaoEstudiante;
import Dao.DaoItem;
import Dao.DaoPregConc;
import Dao.DaoPregunta;
import Dao.DaoResultado;
import Dao.DaoTema;
import HibernateUtil.HibernateUtil;
import Pojo.Concepto;
import Pojo.Item;
import Pojo.PregConc;
import Pojo.Pregunta;
import Pojo.Resultado;
import Pojo.Tema;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class BeanRPresentarTest {

    private Tema test = new Tema();
    private int codigoTest;
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
    private String usuarioLogeado;
    private int codigoEstudiante;
    private HashMap<String, String> valorPrioriConocimiento;

    public BeanRPresentarTest() {
        HttpSession sesionUsuario = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        usuarioLogeado = sesionUsuario.getAttribute("usernameLogin").toString();
        valorPrioriConocimiento = new HashMap<>();

    }

    public Pregunta obtenerPregunta(int codigo) {
        this.session = null;
        this.transaction = null;
        this.codigoTest = codigo;
        try {
            DaoPregunta daoPregunta = new DaoPregunta();
            DaoItem daoItem = new DaoItem();
            DaoTema daoTema = new DaoTema();
            DaoConcepto daoConcepto = new DaoConcepto();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            List<Concepto> lstConceptos = daoConcepto.verPorTema(session, codigoTest); // Conceptos del tema
            System.out.println("LA LISTA DE CONCEPTOS POR EL TEMA ESTÁ VACÍA? " + lstConceptos.size());
            this.codigoEstudiante = new DaoEstudiante().verPorUsername(session, usuarioLogeado).getIdEst(); // id del estudiante
            System.out.println("EL CÓDIGO DEL ESTUDIANTE ES " + codigoEstudiante);
            this.transaction.commit();
            this.session.close();
            //1. Obtener un concepto aleatorio de la lista de conceptos para el tema
            Concepto conc3 = getConceptoAleatorio(lstConceptos);
            System.out.println("***********************************************");
            System.out.println("***********************************************");

            if (conc3 != null) {
                System.out.println("EL CONCEPTO OBTENIDO ALEATORIAMENTE ES: " + conc3.getNombreConcepto());
                this.session = HibernateUtil.getSessionFactory().openSession();
                this.transaction = session.beginTransaction();
                // 2. Consultar a qué preguntas corresponde el concepto
                List<Pregunta> listaPregunta = daoPregunta.verPorConcepto(session, conc3.getIdConcepto());
                List<Pregunta> pregForConcepto = new ArrayList<>(); // lista de preguntas válidas
                for (int i = 0; i < listaPregunta.size(); i++) {
                    if (daoItem.verNumItemsPorPregunta(session, listaPregunta.get(i).getIdPregunta()) != 0) {
                        pregForConcepto.add(listaPregunta.get(i)); // Pregunta válida si tiene items
                    }
                }
                System.out.println("HAY UN TOTAL DE " + pregForConcepto.size() + " DE PREGUNTAS VÁLIDAS");
                // Crear objeto del análisis de la red bayesiana
                CrearBayesDynamic cbdynamic = new CrearBayesDynamic();
                this.preguntaSeleccionada = cbdynamic.getPreguntaOptima(daoTema.verPorCodigoTema(session, codigo).getNombre(), usuarioLogeado, pregForConcepto);
                System.out.println("La pregunta mostrada en el enunciado es:" + preguntaSeleccionada.getEnunciado());
                transaction.commit();
            } else { // si el concepto es null todos los conceptos han sido aprendidos
                System.out.println(" TODOS LOS CONCEPTOS HAN SIDO APRENDIDOS");
                RequestContext.getCurrentInstance().update("frmTerminarTest:panelTerminarTest");
                RequestContext.getCurrentInstance().execute("PF('dialogTerminarTest').show()");
            }
            return preguntaSeleccionada;
        } catch (Exception ex) {
            if (this.transaction != null) {
                if (transaction.isInitiator()) {
                    this.transaction.rollback();
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR al obtener pregunta por codigo:", "Contacte con el administrador" + ex.getMessage()));
            return null;
        } finally {
            if (this.session != null) {
                if (session.isOpen()) {
                    this.session.close();
                }
            }
        }
    }

    private Concepto getConceptoAleatorio(List<Concepto> lstConceptos) {
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            DaoPregunta daoPregunta = new DaoPregunta();
            int codigoConcepto;
            boolean bandera = true;
            Concepto c = null;
            Concepto cElegido = null;
            do {
                c = new Concepto();
                if (!lstConceptos.isEmpty()) {
                    codigoConcepto = (int) (Math.random() * lstConceptos.size());
                    c = lstConceptos.get(codigoConcepto);
                    DaoResultado daoResultado = new DaoResultado();
                    if (this.valorPrioriConocimiento.isEmpty()) { // se consulta en la bd la primera vez
                        double resultado= daoResultado.concAprendidoPorEst(session, this.codigoEstudiante, c.getIdConcepto());
                        if (resultado>0.95) {
                            lstConceptos.remove(codigoConcepto);
                            bandera = true;
                        } else {
                            if (!daoPregunta.verPorConcepto(session, c.getIdConcepto()).isEmpty()) { // Comprobar que existen preguntas para el concepto
                                cElegido = c;
                                bandera = false;
                            } else {
                                lstConceptos.remove(codigoConcepto);
                                bandera = true;
                            }
                        }
                    } else { // se consulta en los valores de la inferencia
                        double valor= Double.parseDouble(this.valorPrioriConocimiento.get(c.getNombreConcepto()));
                        if (valor>0.95) { // concepto aprendido
                            lstConceptos.remove(codigoConcepto);
                            bandera = true;
                        } else {
                            if (!daoPregunta.verPorConcepto(session, c.getIdConcepto()).isEmpty()) { // Comprobar que existen preguntas para el concepto
                                cElegido = c;
                                bandera = false;
                            } else {
                                lstConceptos.remove(codigoConcepto);
                                bandera = true;
                            }
                        }
                    }
                } else {
                    bandera = false;
                }
            } while (bandera == true);

            this.transaction.commit();

            return cElegido;
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
        return null;
    }

    private void inferenciaRed() {
        // LA INFERENCIA TIENE QUE IR LUEGO QUE SE HA DADO UNA RESPUESTA
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            if (valorPrioriConocimiento.isEmpty()) {
                // consultar valores de la BD durante la primera pregunta
                DaoResultado daoResultado = new DaoResultado();
                List<Resultado> lstResultados = daoResultado.verPorEstudianteTema(session, codigoTest, codigoEstudiante);
                for (int i = 0; i < lstResultados.size(); i++) {
                    this.valorPrioriConocimiento.put(lstResultados.get(i).getConcepto().getNombreConcepto(), lstResultados.get(i).getValor() + "");
                }
            }
            // valorPOsteriori calculado            
            DaoTema daoTema = new DaoTema();
            CrearBayesDynamic cbdynamic = new CrearBayesDynamic();
            // asignación de los nuevos valores del aprendizaje (valores posteriori pasan a ser priori para la siguiente pregunta)
            valorPrioriConocimiento = cbdynamic.inferencia(usuarioLogeado, daoTema.verPorCodigoTema(session, this.codigoTest).getNombre(), preguntaSeleccionada, correcto, valorPrioriConocimiento);
        } catch (Exception ex) {
            if (this.transaction != null) {
                if (transaction.isInitiator()) {
                    this.transaction.rollback();
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR al calcular el aprendizaje:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                if (session.isOpen()) {
                    this.session.close();
                }
            }
        }
    }

    // Una sola respuesta correcta (1 concepto)
    private void preguntaListenM() {
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            //Consultar el concepto relacionado con la pregunta
            DaoPregConc daoPregConc = new DaoPregConc();
            List<PregConc> lstPregConc = daoPregConc.verPorPregunta(session, preguntaSeleccionada.getIdPregunta());
            this.transaction.commit();
            // Si el nombre del concepto es igual al del item la respuesta es correcta= true
            this.correcto = lstPregConc.get(0).getConcepto().getNombreConcepto().equalsIgnoreCase(respuestTemp);
            if (correcto) { // Cadena que se va a presentar
                this.resultado = "¡CONGRATULATIONS!";
            } else {
                this.resultado = "Try again";
            }
            inferenciaRed();
            RequestContext.getCurrentInstance().update("frmResultado:panelResultado");
            RequestContext.getCurrentInstance().execute("PF('dialogResultado').show()");
        } catch (Exception ex) {
            if (this.transaction != null) {
                if (this.transaction.isInitiator()) {
                    this.transaction.rollback();
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CONSULTAR CONCEPTO POR TEST - TEMA:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                if (this.session.isOpen()) {
                    this.session.close();
                }
            }
        }
    }

    // Una sola respuesta correcta (1 concepto)
    private void preguntaSpeakM() {
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            //Consultar el concepto relacionado con la pregunta
            DaoPregConc daoPregConc = new DaoPregConc();
            List<PregConc> lstPregConc = daoPregConc.verPorPregunta(session, preguntaSeleccionada.getIdPregunta());
            this.transaction.commit();
            // Si el nombre del concepto es igual al del item la respuesta es correcta= true
            this.correcto = lstPregConc.get(0).getConcepto().getNombreConcepto().equalsIgnoreCase(respuestTemp);
            if (correcto) { // Cadena que se va a presentar
                this.resultado = respuestTemp;
            } else {
                this.resultado = "Try again";
            }
            inferenciaRed();
            RequestContext.getCurrentInstance().update("frmResultado:panelResultado");
            RequestContext.getCurrentInstance().execute("PF('dialogResultado').show()");
        } catch (Exception ex) {
            if (this.transaction != null) {
                if (this.transaction.isInitiator()) {
                    this.transaction.rollback();
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CONSULTAR CONCEPTO POR TEST - TEMA:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                if (this.session.isOpen()) {
                    this.session.close();
                }
            }
        }
    }

    // Varios conceptos (Varias respuestas)
    private void preguntaListenF() {
        this.session = null;
        this.transaction = null;
        try {
            DaoConcepto daoConcepto = new DaoConcepto();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            // Consultar todos los conceptos relacionados con ese tema
            List<Concepto> listaConceptos = daoConcepto.verPorTema(session, codigoTest);
            System.out.println("****************************************************+");
            System.out.println("NÚMERO DE CONCEPTOS EN EL TEST " + "está vacía? " + listaConceptos.isEmpty());
            transaction.commit();
            this.correcto = false;
            for (int i = 0; i < listaConceptos.size(); i++) {
                if (listaConceptos.get(i).getNombreConcepto().equalsIgnoreCase(respuestTemp)) {
                    this.correcto = true; // la respuesta pertenece a la lista de respuestas correctas
                    break;
                }
            }
            if (correcto) { // Cadena que se va a presentar
                this.resultado = respuestTemp;
            } else {
                this.resultado = "Try again";
            }
            inferenciaRed();
            RequestContext.getCurrentInstance().update("frmResultado:panelResultado");
            RequestContext.getCurrentInstance().execute("PF('dialogResultado').show()");
        } catch (Exception ex) {
            if (this.transaction != null) {
                if (this.transaction.isInitiator()) {
                    this.transaction.rollback();
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CONSULTAR CONCEPTO POR TEST - TEMA:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                if (session.isOpen()) {
                    this.session.close();
                }
            }
        }
    }

    // Varios conceptos (Varias respuestas)
    private void preguntaSpeakF() {
        this.session = null;
        this.transaction = null;
        try {
            DaoConcepto daoConcepto = new DaoConcepto();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            // Consultar todos los conceptos relacionados con ese tema
            List<Concepto> listaConceptos = daoConcepto.verPorTema(session, codigoTest);
            this.correcto = false;
            DaoItem daoItem = new DaoItem();
            Item itemSelect = null;
            for (int i = 0; i < listaConceptos.size(); i++) {
                if (listaConceptos.get(i).getNombreConcepto().equalsIgnoreCase(respuestTemp)) {
                    this.correcto = true;
                    itemSelect = daoItem.verPorPREGNombreItem(session, respuestTemp, this.preguntaSeleccionada.getIdPregunta());
                    imgItemSeleccionado = itemSelect.getImgItem();
                    System.out.println("el item seleccionado es: " + itemSelect.getNombreItem());
                    break;
                }
            }
            System.out.println("el valor final: " + this.correcto);

            if (correcto) { // Cadena que se va a presentar
                this.resultado = "This is " + respuestTemp + ". Now repeat.";
            } else {
                this.resultado = "Try again" + respuestTemp;
            }
            transaction.commit();
            inferenciaRed();
            RequestContext.getCurrentInstance().update("frmResultadoSpeak:panelResultadoSpeak");
            RequestContext.getCurrentInstance().execute("PF('dialogResultadoSpeak').show()");
        } catch (Exception ex) {
            if (this.transaction != null) {
                if (transaction.isInitiator()) {
                    this.transaction.rollback();
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CONSULTAR ITEM HAY MÁS DE UN ITEM CON LA MISMO IDENTIFICADOR EN LA PREGUNTA:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                if (session.isOpen()) {
                    this.session.close();
                }
            }
        }
    }

    public void guardarRespuestaTemp(String respuesta) {
        this.respuestTemp = respuesta;
        String tipoPregunta = getTipoPreg(preguntaSeleccionada);
        if (null != tipoPregunta) {
            switch (tipoPregunta) {
                case "listenF":
                    preguntaListenF();
                    break;
                case "listenM":
                    preguntaListenM();
                    break;
                case "speakF":
                    preguntaSpeakF();
                    break;
                case "speakM":
                    preguntaSpeakM();
                    break;
                case "speakD":
                    preguntaSpeakM();
                    break;
            }
        }

    }

    private String getTipoPreg(Pregunta pregunta) {
        if (pregunta.getDificultad() == 2.0 && pregunta.getFdescuido() == 0.2 && pregunta.getIndiceDis() == 0.2) {
            return "listenF";
        } else {
            if (pregunta.getDificultad() == 3.0 && pregunta.getFdescuido() == 0.01 && pregunta.getIndiceDis() == 1.2) {
                return "listenM";
            } else {
                if (pregunta.getDificultad() == 2.0 && pregunta.getFdescuido() == 0.001 && pregunta.getIndiceDis() == 0.2) {
                    return "speakF";
                } else {
                    if (pregunta.getDificultad() == 3.0 && pregunta.getFdescuido() == 0.001 && pregunta.getIndiceDis() == 1.2) {
                        return "speakM";
                    } else {
                        if (pregunta.getDificultad() == 4.0 && pregunta.getFdescuido() == 0.001 && pregunta.getIndiceDis() == 2.0) {
                            return "speakD";
                        }
                        return null;
                    }
                }
            }
        }
    }

    public String actualizarPagina() {
        return "test";
    }

    public String terminarTest() {
        // Guardar valores del conocimineto en la BD
        if (!valorPrioriConocimiento.isEmpty()) {
            this.session = null;
            this.transaction = null;
            try {
                this.session = HibernateUtil.getSessionFactory().openSession();
                this.transaction = session.beginTransaction();
                // actualizar los valores del conocimiento en la BD
                DaoResultado daoResultado = new DaoResultado();
                DaoConcepto daoConcepto = new DaoConcepto();
                List<Resultado> lstResultados = new ArrayList<>();
                Resultado rBD;
                System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
                for (Map.Entry<String, String> entry : valorPrioriConocimiento.entrySet()) {
                    // obtengo el resultado anterior
                    rBD = daoResultado.verPorEstudianteConcepto(session, codigoEstudiante, daoConcepto.verPorNombreConcepto(session, entry.getKey()).getIdConcepto());
                    rBD.setValor(Double.parseDouble(entry.getValue())); // actualizo valor
                    lstResultados.add(rBD); // agrego el resultado a la lista de resultados nuevos
                    System.out.println("resultado " + rBD.getIdResultado() + " Estudiante: " + rBD.getEstudiante().getIdEst() + " Concepto: " + rBD.getConcepto().getIdConcepto() + " Valor: " + rBD.getValor());
                    rBD = new Resultado();
                }
                System.out.println("lista de resultados SIZE: " + lstResultados.size());
                daoResultado.actualizarVarios(session, lstResultados); // actualizar resultados en la base de datos
                DaoTema daoTema = new DaoTema();
                CrearBayesDynamic cbdynamic = new CrearBayesDynamic();
                cbdynamic.terminarInferencia(daoTema.verPorCodigoTema(session, this.codigoTest).getNombre(), usuarioLogeado);
                this.transaction.commit();
            } catch (Exception ex) {
                if (this.transaction != null) {
                    if (transaction.isInitiator()) {
                        this.transaction.rollback();
                    }
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR al guardar valores conocimiento en la Base de datos:", "Contacte con el administrador" + ex.getMessage()));
            } finally {
                if (this.session != null) {
                    if (session.isOpen()) {
                        this.session.close();
                    }
                }
            }
        }
        return "listaTest";
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
            Tema tema = daoTema.verPorCodigoTema(session, codigoTest);
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
