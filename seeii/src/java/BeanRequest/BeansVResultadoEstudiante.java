/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Dao.DaoEntrenar;
import Dao.DaoEstudiante;
import Dao.DaoUnidadE;
import HibernateUtil.HibernateUtil;
import Pojo.Entrenamiento;
import Pojo.Estudiante;
import Pojo.Unidadensenianza;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author USUARIO
 */
@ManagedBean
@ViewScoped
public class BeansVResultadoEstudiante {

    private Session session;
    private Transaction transaction;

    //Para obtner estudiantes respecto a esa unidad
    private Unidadensenianza unidadE;
    private List<Unidadensenianza> listaUnidades; //inicalizado en método(ObtnerListaUnidades)

    //obtener estudiante de la unidad selecionada
    private Estudiante estudianteSeleccionado;
    private List<Estudiante> listaEstudiantesPorUnidad;
    private List<Estudiante> listaEstudiantesFiltrada;

    //mostrara grafico del entrenamiento
    private CartesianChartModel modelGraphFichas;

    //lista de entrenamiento de un estudiante X
    private List<Entrenamiento> listaEntrenamiento;

    public BeansVResultadoEstudiante() {
    }

    @PostConstruct
    public void init() {
        modelGraphFichas = new CartesianChartModel();
        ChartSeries temaSeries = new ChartSeries();
        temaSeries.setLabel("Entrenamiento");
        modelGraphFichas.addSeries(temaSeries);
    }

    //Metodo para obtner la lista de Unidades
    public List<Unidadensenianza> obtnerListaUnidades() {
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            DaoUnidadE daoUnidades = new DaoUnidadE();
            //obtiene la lista de unidades del dao

            this.listaUnidades = daoUnidades.verTodo(session);
            this.transaction.commit();
        } catch (Exception ex) {
            if (this.transaction != null) {
                if (transaction.isInitiator()) {
                    this.transaction.rollback();
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR al obtener lista de estudiantes por unidad:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                if (session.isOpen()) {
                    this.session.close();
                }
            }
        }
        return listaUnidades;
    }

    //obtniene la lista de estudiantes con respecto a la unidad elegida en XHTML
    public List<Estudiante> getListaEstudiantesPorUnidad() {
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            DaoEstudiante daoEstudiante = new DaoEstudiante();
            this.listaEstudiantesPorUnidad = daoEstudiante.verEstudiantePorIdUnidad(session, this.unidadE.getId());
            this.transaction.commit();
        } catch (Exception ex) {
            if (this.transaction != null) {
                if (transaction.isInitiator()) {
                    this.transaction.rollback();
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR al obtener lista de estudiantes por unidad:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                if (session.isOpen()) {
                    this.session.close();
                }
            }
        }
        return listaEstudiantesPorUnidad;
    }

    public void incializarGraficoResultado(Estudiante estudiant) {
        //inicializo el atributo (listaEntrenamiento)
        verListaEntrenamientos(estudiant.getIdEst());

        modelGraphFichas = new CartesianChartModel();
        ChartSeries temaSeries = new ChartSeries();
        temaSeries.setLabel("Puntajes del Entrenamiento");
        int tamaño = 0;
        int tamaño1 = 0;
        int anio;
        int mes;
        String fecha;
        int posicion;
        System.out.println(" el tamano de la lista es_" + listaEntrenamiento.size());
        if (listaEntrenamiento.isEmpty()) {
            temaSeries.set("Sin Fecha de Entrenamiento", 0);
            modelGraphFichas.addSeries(temaSeries);
        } else {
            tamaño = listaEntrenamiento.size();
            if (tamaño <= 7) {
                for (int i = 0; i < tamaño; i++) {
                    anio = listaEntrenamiento.get(i).getFecha().getYear() + 1900;
                    mes = listaEntrenamiento.get(i).getFecha().getMonth() + 1;
                    fecha = "[" + i + "] " + anio + "/" + mes + "/" + listaEntrenamiento.get(i).getFecha().getDate();
                    temaSeries.set(fecha, listaEntrenamiento.get(i).getPuntaje());
                }
                modelGraphFichas.addSeries(temaSeries);
            } else {
                tamaño = listaEntrenamiento.size();
                tamaño1 = listaEntrenamiento.size() - 7;
                int con = 0;
                for (int i = tamaño1; i < tamaño; i++) {
                    con = con + 1;
                    System.out.println("i:" + i);
                    anio = listaEntrenamiento.get(i).getFecha().getYear() + 1900;
                    mes = listaEntrenamiento.get(i).getFecha().getMonth() + 1;
                    fecha = "[" + con + "] " + anio + "/" + mes + "/" + listaEntrenamiento.get(i).getFecha().getDate();
                    temaSeries.set(fecha, listaEntrenamiento.get(i).getPuntaje());
                }
                modelGraphFichas.addSeries(temaSeries);
            }
        }
    }

    //metodo para obtner una lista de Entrenamiento de un usuario
    public void verListaEntrenamientos(int idEstudiante) {
        this.session = null;
        this.transaction = null;

        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            //para obtener un entrenamiento por su id
            DaoEntrenar daoEntrenar = new DaoEntrenar();
            listaEntrenamiento = daoEntrenar.listEntrenamientoPorIdEstudiante(session, idEstudiante);
            this.transaction.commit();

            System.out.println("Correcto: Al Obtner la lsiata de Entrenamientos con exito");
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL Obtner la lista de Entrenamientos:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

//....................................................................
    public Unidadensenianza getUnidadE() {
        return unidadE;
    }

    public void setUnidadE(Unidadensenianza unidadE) {
        this.unidadE = unidadE;
    }

    public Estudiante getEstudianteSeleccionado() {
        return estudianteSeleccionado;
    }

    public void setEstudianteSeleccionado(Estudiante estudianteSeleccionado) {
        this.estudianteSeleccionado = estudianteSeleccionado;
    }

    public List<Estudiante> getListaEstudiantesFiltrada() {
        return listaEstudiantesFiltrada;
    }

    public void setListaEstudiantesFiltrada(List<Estudiante> listaEstudiantesFiltrada) {
        this.listaEstudiantesFiltrada = listaEstudiantesFiltrada;
    }

    public CartesianChartModel getModelGraphFichas() {
        return modelGraphFichas;
    }

    public void setModelGraphFichas(CartesianChartModel modelGraphFichas) {
        this.modelGraphFichas = modelGraphFichas;
    }

 }
