/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import BeanSession.BeanSEntrenar;
import Dao.DaoEntrenar;
import Dao.DaoPreguntaEntrenar;
import HibernateUtil.HibernateUtil;
import Pojo.Entrenamiento;
import Pojo.Preguntaentrenar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

/**
 *
 * @author USUARIO
 */
@ManagedBean
@RequestScoped
public class beansTotalResultado {

    private Session session;
    private Transaction transaction;

    private LineChartModel lineModel1;
    private LineChartModel lineModel2;

    //llamar al bean de sesion 
    @ManagedProperty("#{beanSEntrenar}")
    private BeanSEntrenar beanSEntrena;

    private Entrenamiento entrenamiento;
    private List<Preguntaentrenar> ListpreguntaEntrenar;

    public beansTotalResultado() {

    }

    @PostConstruct
    public void init() {
        createLineModels();
    }

    private void createLineModels() {
        lineModel1 = initCategoryModel();
        lineModel1.setTitle("Resultado Entrenamiento");
        lineModel1.setLegendPosition("e");
        lineModel1.setShowPointLabels(true);
        lineModel1.getAxes().put(AxisType.X, new CategoryAxis(""));
        Axis yAxis = lineModel1.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(1250);

        lineModel2 = initCategoryModelG2();
        lineModel2.setTitle("Preguntas de Entrenamiento");
        lineModel2.setLegendPosition("e");
        lineModel2.setShowPointLabels(true);
        lineModel2.getAxes().put(AxisType.X, new CategoryAxis(""));
        Axis yAxis2 = lineModel2.getAxis(AxisType.Y);
        yAxis2.setMin(0);
        yAxis2.setMax(600);

    }

    private LineChartModel initCategoryModel() {
        entrenamiento = obtnerEntrenamiento();
        LineChartModel model = new LineChartModel();

        ChartSeries valores = new ChartSeries();
        valores.setLabel("Valores ");
        valores.set("error", entrenamiento.getError());
        valores.set("tiempo", entrenamiento.getTiempo());
        valores.set("puntaje", entrenamiento.getPuntaje());
        model.addSeries(valores);

        return model;
    }

    private LineChartModel initCategoryModelG2() {
        //metodo que inicializa el atributo (ListpreguntaEntrenar)
        obtnerListPreguntaEntrenar();
        LineChartModel model = new LineChartModel();

        ChartSeries valores = new ChartSeries();
        valores.setLabel("Pregunta 1 ");
        valores.set("incorrecto", ListpreguntaEntrenar.get(0).getIncorrecto());
        valores.set("valor", ListpreguntaEntrenar.get(0).getValor());

        model.addSeries(valores);

        ChartSeries valores2 = new ChartSeries();
        valores2.setLabel("Pregunta 2 ");
        valores2.set("incorrecto", ListpreguntaEntrenar.get(1).getIncorrecto());
        valores2.set("valor", ListpreguntaEntrenar.get(1).getValor());

        model.addSeries(valores2);

        ChartSeries valores3 = new ChartSeries();
        valores3.setLabel("Pregunta 3 ");
        valores3.set("incorrecto", ListpreguntaEntrenar.get(2).getIncorrecto());
        valores3.set("valor", ListpreguntaEntrenar.get(2).getValor());

        model.addSeries(valores3);

        return model;
    }

    public Entrenamiento obtnerEntrenamiento() {
        this.session = null;
        this.transaction = null;
        Entrenamiento entrenamiento = new Entrenamiento();

        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            //para obtener un entrenamiento por su id
            DaoEntrenar daoEntrenar = new DaoEntrenar();//verPorCodigoEntrenamiento
            entrenamiento = daoEntrenar.verPorCodigoEntrenamiento(session, this.beanSEntrena.getIdEntrenamiento());
            this.transaction.commit();

            System.out.println("Correcto: Al Obtner el Entrenamiento con exito");
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL Obtner el Entrenamiento:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
        return entrenamiento;
    }

    public void obtnerListPreguntaEntrenar() {
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            //obtener una preguntaentrenar por su id 
            DaoPreguntaEntrenar daoPreguntaEntrenar = new DaoPreguntaEntrenar();
            ListpreguntaEntrenar = daoPreguntaEntrenar.verListPreguntaEntrenarPorIdEntrena(session, this.beanSEntrena.getIdEntrenamiento());
            this.transaction.commit();

            System.out.println("Correcto: La obtener preguntaEntrenar  se ha realizado con exito");
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL obtner preguntaEntrenar:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }

    }

    //.................setter y getter.....................
    public LineChartModel getLineModel1() {
        return lineModel1;
    }

    public LineChartModel getLineModel2() {
        return lineModel2;
    }

    public Entrenamiento getEntrenamiento() {
        return entrenamiento;
    }

    public BeanSEntrenar getBeanSEntrena() {
        return beanSEntrena;
    }

    public void setBeanSEntrena(BeanSEntrenar beanSEntrena) {
        this.beanSEntrena = beanSEntrena;
    }

}
