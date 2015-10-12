/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Dao.DaoEntrenar;
import Dao.DaoFichaPregunta;
import Dao.DaoPreguntaEntrenar;
import HibernateUtil.HibernateUtil;
import Pojo.Entrenamiento;
import Pojo.Fichaspregunta;
import Pojo.Preguntaentrenar;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
    int numero;
    //atributo para utilizar con el metodo (obtenerlistaFichasPregunta)
    private List<Fichaspregunta> listFichasPregunta;
    //atributo para obtner una listas de fichas para mostrar en test, se utliza en el metodo (listaFichas)
    ArrayList listaNumero = new ArrayList();

    public BeansRPreguntaEntrena() {
        model = new DefaultDashboardModel();
        DashboardColumn column1 = new DefaultDashboardColumn();
        DashboardColumn column2 = new DefaultDashboardColumn();
        //llamamos al metodo para elegir un modelo del dashboard
        elegirModelDashboard(column1, column2);
    }

    public void elegirModelDashboard(DashboardColumn column1, DashboardColumn column2) {
        numero = (int) (Math.random() * (6) + 1); //numero aleatorio entre 1-6 para obtner un modelo del dashbord
        switch (numero) {
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

    //------------------------------------------------------------------------------------------------

    public void inicializarListaFichaPrengunta(int idPrenguntaEnt) {
        //metodo para obtner la lista de fichas a presentar inicializa el atributo (listaNumero)
        listaFichas(0, 3, 3);//obtner 3 numeros del 0 al 3 
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
    public String obtnerficha1() {
        //método (inicializarListaFichaPrengunta) inicializa las variables (listFichasPregunta y listaNumero )
        int idFicha = this.listFichasPregunta.get((int) listaNumero.get(0)).getFicha().getIdFicha();
        String dirFicha = idFicha + ".jpg";
        return dirFicha;
    }

    public String obtnerficha2() {
        //método (inicializarListaFichaPrengunta) inicializa las variables (listFichasPregunta y listaNumero )
        int idFicha = this.listFichasPregunta.get((int) listaNumero.get(1)).getFicha().getIdFicha();
        String dirFicha = idFicha + ".jpg";
        return dirFicha;
    }

    public String obtnerficha3() {
        //método (inicializarListaFichaPrengunta) inicializa las variables (listFichasPregunta y listaNumero )
        int idFicha = this.listFichasPregunta.get((int) listaNumero.get(2)).getFicha().getIdFicha();
        String dirFicha = idFicha + ".jpg";
        return dirFicha;
    }

    //........................................................................................
    
    public String finaizarTestPreguntaEntrenar(){
        System.out.println(model.getColumn(0).getWidget(0)+"id de widgwts................");
        System.out.println(model.getColumn(0).getWidgetCount()+"colunmnas................");
        
    return "aprendizajeFichas1";
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

}

//public void crearPreguntaEntrena(int idEntrenar) {
//        boolean estado = false;
//        this.session = null;
//        this.transaction = null;
//        try {
//            this.session = HibernateUtil.getSessionFactory().openSession();
//            this.transaction = session.beginTransaction();
//            //obtener el entrenamiento mediante el id para fijar a las preguntas.
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
