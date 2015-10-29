/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Dao.DaoEntrenar;
import Dao.DaoFicha;
import Dao.DaoFichaPregunta;
import Dao.DaoPreguntaEntrenar;
import HibernateUtil.HibernateUtil;
import Pojo.Entrenamiento;
import Pojo.Ficha;
import Pojo.Fichaspregunta;
import Pojo.Preguntaentrenar;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

/**
 *
 * @author silvy
 */
@ManagedBean
@RequestScoped
public class BeansRPreguntaEntrena implements Serializable {

    private Session session;
    private Transaction transaction;
    private Preguntaentrenar preguntaEnt;
//    java.util.Date fecha = new Date();

    private DashboardModel model;
    int numero = numeroAleator(1, 6);
    //atributo para obtner una listas de fichas para mostrar en test, se utliza en el metodo (listaFichas)
    ArrayList listaNumero = new ArrayList();
    //atributo para utilizar con el metodo (obtenerlistaFichasPregunta)
    private List<Fichaspregunta> listFichasPregunta;
    //atributo para utiliza en el metodo (obtnerSonidoficha)
//    private Ficha ficha;
    //atributo para obtner el nombre de las fichas --> para la Pronunciacion.
    private String nameficha1; //usado en metodo (obtnerSonidoficha1)
    private String nameficha2; //usado en metodo (obtnerSonidoficha2)
    private String nameficha3; //usado en metodo (obtnerSonidoficha3)
    //atributo para la columna y asignar cuando de mueven
    List<String> columna1 ; //metodo handleReorder
    List<String> columna2; //metodo handleReorder

    public BeansRPreguntaEntrena() {
        model = new DefaultDashboardModel();

    }

    @PostConstruct
    public void init() {

        DashboardColumn columnDashboard1 = new DefaultDashboardColumn();
        DashboardColumn columnDashboard2 = new DefaultDashboardColumn();
        //llamamos al metodo para elegir un modelo del dashboard
        elegirModelDashboard(columnDashboard1, columnDashboard2);
    }

    public void elegirModelDashboard(DashboardColumn column1, DashboardColumn column2) {
        int num = numero;
        switch (num) {
            case 1:
                System.out.println("model(Dashboard) numero 1");
                modeloDEFAULT1(column1, column2);
                break;
            case 2:
                System.out.println("model(Dashboard) numero 2");
                modeloDEFAULT2(column1, column2);
                break;
            case 3:
                System.out.println("model(Dashboard) numero 3");
                modeloDEFAULT3(column1, column2);
                break;
            case 4:
                System.out.println("model(Dashboard) numero 4");
                modeloDEFAULT4(column1, column2);
                break;
            case 5:
                System.out.println("model(Dashboard) numero 5");
                modeloDEFAULT5(column1, column2);
                break;
            case 6:
                System.out.println("model(Dashboard) numero 6");
                modeloDEFAULT6(column1, column2);
                break;
            default:
                System.out.println("model(Dashboard) numero 5 por defecto.");
                modeloDEFAULT5(column1, column2);
                break;
        }
    }

    public void modeloDEFAULT1(DashboardColumn column1, DashboardColumn column2) {
        column1.addWidget("imagen1");
        column1.addWidget("imagen2");
        column1.addWidget("imagen3");

        column2.addWidget("sonido2");
        column2.addWidget("sonido3");
        column2.addWidget("sonido1");

        model.addColumn(column1);
        model.addColumn(column2);
    }

    public void modeloDEFAULT2(DashboardColumn column1, DashboardColumn column2) {
        column1.addWidget("imagen1");
        column1.addWidget("imagen2");
        column1.addWidget("imagen3");

        column2.addWidget("sonido3");
        column2.addWidget("sonido1");
        column2.addWidget("sonido2");

        model.addColumn(column1);
        model.addColumn(column2);
    }

    public void modeloDEFAULT3(DashboardColumn column1, DashboardColumn column2) {
        column1.addWidget("imagen2");
        column1.addWidget("imagen3");
        column1.addWidget("imagen1");

        column2.addWidget("sonido1");
        column2.addWidget("sonido2");
        column2.addWidget("sonido3");

        model.addColumn(column1);
        model.addColumn(column2);
    }

    public void modeloDEFAULT4(DashboardColumn column1, DashboardColumn column2) {
        column1.addWidget("imagen2");
        column1.addWidget("imagen3");
        column1.addWidget("imagen1");

        column2.addWidget("sonido3");
        column2.addWidget("sonido1");
        column2.addWidget("sonido2");

        model.addColumn(column1);
        model.addColumn(column2);
    }

    public void modeloDEFAULT5(DashboardColumn column1, DashboardColumn column2) {
        column1.addWidget("imagen3");
        column1.addWidget("imagen1");
        column1.addWidget("imagen2");

        column2.addWidget("sonido1");
        column2.addWidget("sonido2");
        column2.addWidget("sonido3");

        model.addColumn(column1);
        model.addColumn(column2);
    }

    public void modeloDEFAULT6(DashboardColumn column1, DashboardColumn column2) {
        column1.addWidget("imagen3");
        column1.addWidget("imagen1");
        column1.addWidget("imagen2");

        column2.addWidget("sonido2");
        column2.addWidget("sonido3");
        column2.addWidget("sonido1");

        model.addColumn(column1);
        model.addColumn(column2);
    }

    public void fijarColumna() {
        int num=numero;
        switch (num) {
            case 1:
                columna1.add("imagen1");
                columna1.add("imagen2");
                columna1.add("imagen3");
                System.out.println(columna1);

                columna2.add("sonido2");
                columna2.add("sonido3");
                columna2.add("sonido1");
                System.out.println(columna2);
                break;
            case 2:
                columna1.add("imagen1");
                columna1.add("imagen2");
                columna1.add("imagen3");
                System.out.println(columna1);

                columna2.add("sonido3");
                columna2.add("sonido1");
                columna2.add("sonido2");
                System.out.println(columna2);
                break;
            case 3:
                columna1.add("imagen2");
                columna1.add("imagen3");
                columna1.add("imagen1");
                System.out.println(columna1);

                columna2.add("sonido1");
                columna2.add("sonido2");
                columna2.add("sonido3");
                System.out.println(columna2);
                break;
            case 4:
                columna1.add("imagen2");
                columna1.add("imagen3");
                columna1.add("imagen1");
                System.out.println(columna1);

                columna2.add("sonido3");
                columna2.add("sonido1");
                columna2.add("sonido2");
                System.out.println(columna2);
                break;
            case 5:
                columna1.add("imagen3");
                columna1.add("imagen1");
                columna1.add("imagen2");
                System.out.println(columna1);

                columna2.add("sonido1");
                columna2.add("sonido2");
                columna2.add("sonido3");
                System.out.println(columna2);
                break;
            case 6:
                columna1.add("imagen3");
                columna1.add("imagen1");
                columna1.add("imagen2");
                System.out.println(columna1);

                columna2.add("sonido2");
                columna2.add("sonido3");
                columna2.add("sonido1");
                System.out.println(columna2);
                break;
            default:
                System.out.println("model(Dashboard) numero 5 por defecto.");
                columna1.add("imagen3");
                columna1.add("imagen1");
                columna1.add("imagen2");
                System.out.println(columna1);

                columna2.add("sonido1");
                columna2.add("sonido2");
                columna2.add("sonido3");
                System.out.println(columna2);
        }
    }

    public void handleReorder(DashboardReorderEvent event) {
        String widgetId = event.getWidgetId();
        int widgetIndex = event.getItemIndex();
        int columnIndex = event.getColumnIndex();
        int senderColumnIndex = event.getSenderColumnIndex();
        System.out.println("............................" + numero);

        //Add facesmessage
    }

    //------------------------------------------------------------------------------------------------
    public void inicializarListaFichaPrengunta(int idPrenguntaEnt) {
        //metodo para obtner la lista de fichas a presentar inicializa el atributo (listaNumero)
        listaFichas(0, 3, 3);//obtner 3 numeros del 0 al 3 
//        //metodo para inicializar columna (fijarColumna)
//        fijarColumna();
        //hacer un  if si  idPrenguntaEnt!=0....................................
        //metodo para obtner la lista de fichasPreguntar inicializa el atributo (listFichasPregunta)
        obtenerlistaFichasPregunta(idPrenguntaEnt);
    }

    //metodo para obtner una lista de numero aleatorios (para mostrar fichas.)
    public void listaFichas(int valorInicial, int valorFinal, int numAleatorio) {
        for (int i = 0; i < numAleatorio;) {
            int numero = (int) (Math.random() * (valorFinal - valorInicial + 1) + valorInicial);//genero un numero
            if (listaNumero.isEmpty()) {//si la lista esta vacia
                listaNumero.add(numero);
                i++;
            } else {//si no esta vacia
                if (listaNumero.contains(numero)) {//Si el numero que generé esta contenido en la lista
                } else {//Si no esta contenido en la lista
                    listaNumero.add(numero);
                    i++;
                }
            }
        }
        System.out.println(" lista de numero aleatorios " + listaNumero + " para realizar el test emparejamiento........");
    }

    public int numeroAleator(int valorInicial, int valorFinal) {
        ArrayList listaNumero = new ArrayList();
        int numero = (int) (Math.random() * (valorFinal - valorInicial + 1) + valorInicial);//genero un numero
        listaNumero.add(numero);
        return (int) listaNumero.get(0);
    }

    //metodo para obtner la lista FichasPreguntas para mostrara en el test
    public void obtenerlistaFichasPregunta(int idPrenguntaEnt) {
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            //para crear Fichaspregunta (lista de fichas en base a un preguntadeENTRENAMIENTO)
            DaoFichaPregunta daofichaPregunta = new DaoFichaPregunta();
            listFichasPregunta = daofichaPregunta.verPreguntaEntrenamiento(session, idPrenguntaEnt);
            this.transaction.commit();

            System.out.println("Correcto: al obtner la lista de fichasPregunta se ha realizado con exito");
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL obtner FICHA_PREGUNTA en el test:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    //.........................................................................................
    public String verfichaPorId(int idFich) {
        String nombreFicha = "empty";
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            //para crear Fichaspregunta (lista de fichas en base columna1 un preguntadeENTRENAMIENTO)
            DaoFicha daofichaPregunta = new DaoFicha();
            Ficha ficha = daofichaPregunta.verPorCodigoFicha(session, idFich);
            this.transaction.commit();
            nombreFicha = ficha.getNombreFicha();

            System.out.println("Correcto: al obtner ficha (para obtner el nombre)se ha realizado con exito");
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL obtner FICHA en el Test Emparejamiento:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
        return nombreFicha;
    }

    public String obtnerficha1() {
        //método (inicializarListaFichaPrengunta) inicializa las variables (listFichasPregunta y listaNumero )
        int idficha1 = this.listFichasPregunta.get((int) listaNumero.get(0)).getFicha().getIdFicha();
        String dirFicha = idficha1 + "";
        //metodo para sonido de las fichas (verfichaPorId)
        nameficha1 = verfichaPorId(idficha1);
        return dirFicha;
    }

    public String obtnerficha2() {
        //método (inicializarListaFichaPrengunta) inicializa las variables (listFichasPregunta y listaNumero )
        int idficha2 = this.listFichasPregunta.get((int) listaNumero.get(1)).getFicha().getIdFicha();
        String dirFicha = idficha2 + "";
        //metodo para sonido de las fichas (verfichaPorId)
        nameficha2 = verfichaPorId(idficha2);
        return dirFicha;
    }

    public String obtnerficha3() {
        //método (inicializarListaFichaPrengunta) inicializa las variables (listFichasPregunta y listaNumero )
        int idficha3 = this.listFichasPregunta.get((int) listaNumero.get(2)).getFicha().getIdFicha();
        String dirFicha = idficha3 + "";
        //metodo para sonido de las fichas (verfichaPorId)
        nameficha3 = verfichaPorId(idficha3);
        return dirFicha;
    }
    //.........................................................................................

    public String obtnerSonidoficha1() {
        return nameficha1;
    }

    public String obtnerSonidoficha2() {
        return nameficha2;
    }

    public String obtnerSonidoficha3() {
        return nameficha3;
    }

    //........................................................................................
    public String finaizarTestPreguntaEntrenar() {
        System.out.println(model.getColumn(0).getWidget(0) + " id de widgwts................");
        System.out.println(model.getColumn(0).getWidgetCount() + "colunmnas 1................");
        System.out.println(model.getColumn(1).getWidgets() + "colunmnas 2................");
        System.out.println(model.getColumnCount() + "colunmnas del modelo................");
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println(numero);
        System.out.println("-----------------------------------------------------------------------------");

        return "aprendizajeFichas";

    }

    //.............................SETTER AND GETTER...........................................
    public Preguntaentrenar getPreguntaEnt() {
        return preguntaEnt;
    }

    public void setPreguntaEnt(Preguntaentrenar preguntaEnt) {
        this.preguntaEnt = preguntaEnt;
    }

    public DashboardModel getModel() {
        return model;
    }

    public void setModel(DashboardModel model) {
        this.model = model;
    }

    public List<Fichaspregunta> getListFichasPregunta() {
        return listFichasPregunta;
    }

    public void setListFichasPregunta(List<Fichaspregunta> listFichasPregunta) {
        this.listFichasPregunta = listFichasPregunta;
    }

//    public Ficha getFicha() {
//        return ficha;
//    }
//
//    public void setFicha(Ficha ficha) {
//        this.ficha = ficha;
//    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}
//public void crearPreguntaEntrena(int idEntrenar) {
//        boolean estado = false;
//        this.session = null;
//        this.transaction = null;
//        try {
//            this.session = HibernateUtil.getSessionFactory().openSession();
//            this.transaction = session.beginTransaction();
//            //obtener el entrenamiento mediante el id para fijar columna1 las preguntas.
//            DaoEntrenar daoEntrenar = new DaoEntrenar();
//            Entrenamiento entrenar = daoEntrenar.verPorCodigoEntrenamiento(session, idEntrenar);
//            this.preguntaEnt.setEntrenamiento(entrenar);
//            
//            DaoPreguntaEntrenar daoPregunta = new DaoPreguntaEntrenar();
//            daoPregunta.registrarPreguntaEnt(this.session, this.preguntaEnt);
//            this.transaction.commit();
//
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue realizado con éxito"));
//
//        } catch (Exception ex) {
//            if (this.transaction != null) {
//                this.transaction.rollback();
//            }
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL REGISTRA  PREGUNTAS DE entrenamiento:", "Contacte con el administrador" + ex.getMessage()));
//        } finally {
//            if (this.session != null) {
//                this.session.close();
//            }
//        }
//    }
