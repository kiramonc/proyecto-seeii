/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Dao.DaoEntrenar;
import Dao.DaoEstudiante;
import Dao.DaoUsuario;
import HibernateUtil.HibernateUtil;
import Pojo.Entrenamiento;
import Pojo.Estudiante;
import Pojo.Usuario;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
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
public class BeansVerResultadoT {

    private Session session;
    private Transaction transaction;

    private LineChartModel lineModel1;

    private List<Entrenamiento> listaEntrenamiento;

    public BeansVerResultadoT() {

    }

    @PostConstruct
    public void init() {
        ObtnerDatosEst();
        createLineModels();
    }

    private void createLineModels() {
        lineModel1 = initCategoryModel();
        lineModel1.setTitle("Resultado Estudiante");
        lineModel1.setLegendPosition("e");
        lineModel1.setShowPointLabels(true);
        lineModel1.getAxes().put(AxisType.X, new CategoryAxis(""));
        Axis yAxis = lineModel1.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(1250);
    }

    private LineChartModel initCategoryModel() {
        LineChartModel model = new LineChartModel();
        ChartSeries valores = new ChartSeries();
        valores.setLabel("Puntajes Entrenamiento ");

        int tamaño = 0;
        int anio;
        int mes;
        String fecha;
        int posicion;
        if (listaEntrenamiento.isEmpty()) {
            valores.set("Sin Fecha de Entrenamiento", 0);
            model.addSeries(valores);
        } else {
            tamaño = listaEntrenamiento.size();
            if (tamaño <= 7) {
                for (int i = 0; i < tamaño; i++) {
                    anio = listaEntrenamiento.get(i).getFecha().getYear() + 1900;
                    mes = listaEntrenamiento.get(i).getFecha().getMonth() + 1;
                    fecha = "[" + i + "] " + anio + "/" + mes + "/" + listaEntrenamiento.get(i).getFecha().getDate();
                    valores.set(fecha, listaEntrenamiento.get(i).getPuntaje());
                }
                model.addSeries(valores);
            } else {
                for (int i = 0; i < 7; i++) {
                    posicion = tamaño - i;
                    anio = listaEntrenamiento.get(tamaño).getFecha().getYear() + 1900;
                    mes = listaEntrenamiento.get(tamaño).getFecha().getMonth() + 1;
                    fecha = "[" + i + "] " + anio + "/" + mes + "/" + listaEntrenamiento.get(tamaño).getFecha().getDate();
                    valores.set(fecha, listaEntrenamiento.get(tamaño).getPuntaje());
                }
                model.addSeries(valores);
            }
        }
        return model;
    }

       //metodo para obtner las de todos los entrenamiento de un estudiante
    public void ObtnerDatosEst() {
        //obtnego el beansSession LOGIN para obtener el nombre del ususario.
        FacesContext context = javax.faces.context.FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        String nameUser = (String) session.getAttribute("usernameLogin");

        int idUsuario = verUsuarioPorUSERNAME(nameUser);
        //metodo para obtner una lista de Entrenamiento de un usuario
        verListaEntrenamientos(idUsuario); //inicializa el atrinuto(listaEntrenamiento)
        System.out.println("el nombre de usuario es :" + nameUser);
        System.out.println("el id del Estudiante " + nameUser + " es :" + idUsuario);
        System.out.println("la tamaño de lista de entrenamientos es: " + listaEntrenamiento.size());
        //metodo para mostrar la linea de entrenamiento
        createLineModels();
    }

    //metodo para obtner un Usuario segun el USERNAME.
    public int verUsuarioPorUSERNAME(String nameUser) {
        this.session = null;
        this.transaction = null;
        int idUsuario = 0;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            //para obtener su id de usuario segun el NameUser
            DaoUsuario daoUsuario = new DaoUsuario();
            Usuario usuario = daoUsuario.verPorUsername(session, nameUser);
            //Obtnemos el id del estudiante segun el [idUsuario]
            DaoEstudiante daoEstudiante = new DaoEstudiante();
            Estudiante estudiante = daoEstudiante.verPorCodigoUsuario(session, usuario.getIdUsuario());//obtuvimos el estudiante  segun el id del usuario

            idUsuario = estudiante.getIdEst();
            this.transaction.commit();

            System.out.println("Correcto: Al Obtner un Usuario con exito");
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL Obtner un Usuario:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
        return idUsuario;
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

    //.................setter y getter.....................
    public LineChartModel getLineModel1() {
        return lineModel1;
    }

    public List<Entrenamiento> getListaEntrenamiento() {
        return listaEntrenamiento;
    }

    public void setListaEntrenamiento(List<Entrenamiento> listaEntrenamiento) {
        this.listaEntrenamiento = listaEntrenamiento;
    }

}
