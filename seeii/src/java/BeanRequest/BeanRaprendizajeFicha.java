/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Clases.ListAleatoreos;
import Pojo.Ficha;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.ServletContext;
import org.primefaces.component.dashboard.Dashboard;
import org.primefaces.component.graphicimage.GraphicImage;
import org.primefaces.component.panel.Panel;
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
public class BeanRaprendizajeFicha implements Serializable {

    private Ficha ficha;
//    private ListAleatoreos listaF;
//    private List<ListFicha> fichaList;
//    private List<Integer> aleatorios;
    private Dashboard dashboar;
    private DashboardModel model;

    public BeanRaprendizajeFicha() {
//        crearDashboardDinamico();
    }

    @PostConstruct
    public void init() {
        model = new DefaultDashboardModel();
        DashboardColumn column1 = new DefaultDashboardColumn();
        DashboardColumn column2 = new DefaultDashboardColumn();

        column1.addWidget("sports");
        column1.addWidget("finance");

        column1.addWidget("lifestyle");
        column2.addWidget("weather");

        column2.addWidget("politics");

        model.addColumn(column1);
        model.addColumn(column2);
    }
    public List<ListAleatoreos> generar(int valorInicial, int valorFinal, int numAleatorio) {
        ArrayList listaNumero = new ArrayList();
        List<ListAleatoreos> listaN = new ArrayList();

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
        System.out.println(".............................................." + listaNumero);
        for (int i = 0; i < listaNumero.size(); i++) {
            ListAleatoreos lista = new ListAleatoreos((Integer) listaNumero.get(i));
            System.out.println(lista);
            listaN.add(lista);
        }
        System.out.println(listaN);

        return listaN;
    }

//    public void crearDashboardDinamico() {
//        FacesContext fc = FacesContext.getCurrentInstance();
//        Application app = fc.getApplication();
//
//        dashboar = (Dashboard) app.createComponent(fc, "org.primefaces.component.Dashboard", "org.primefaces.component.DashboardRenderer");
//        model = new DefaultDashboardModel();
//
//        DashboardColumn column1 = new DefaultDashboardColumn();
//        DashboardColumn column2 = new DefaultDashboardColumn();
//        model.addColumn(column1);
//        model.addColumn(column2);
//        dashboar.setId("idDahboard");
//        dashboar.setModel(model);
//
//        for (int i = 0; i < 3; i++) {
//            GraphicImage gImage = (GraphicImage) app.createComponent(fc, "org.primefaces.component.GraphicImage", "org.primefaces.component.GraphicImageRenderer");
//            gImage.setUrl("1.jpg");
//            gImage.setId("idImage" + i);
//            gImage.setHeight("30%");
//            gImage.setWidth("30%");
//            Panel panel=(Panel)app.createComponent(fc, "org.primefaces.component.Panel", "org.primefaces.component.PanelRenderer");
//            panel.setWidgetVar(gImage.getId());
//            panel.setId("idIPanel" + i);
//            getDashboar().getChildren().add(panel);
//            DashboardColumn cl = model.getColumn(0);
//            cl.addWidget(panel.getId());
//
//        }
//    }

    
    public Dashboard getDashboar() {
        return dashboar;
    }

    public DashboardModel getModel() {
        return model;
    }

    public void setModel(DashboardModel model) {
        this.model = model;
    }

    public void setDashboar(Dashboard dashboar) {
        this.dashboar = dashboar;
    }

    public Ficha getFicha() {
        return ficha;
    }

    public void setFicha(Ficha ficha) {
        this.ficha = ficha;

    }

}
//private List<String> urls;
////    //atributos para tener numero aleatorios
//    private final ArrayList listaNumero;
//
//    public BeanRaprendizajeFicha() {
//        listaNumero = new ArrayList();
//        urls = new ArrayList();
//    }
//
//    public List<String> visualizarFicha() {
//        generar(1, 10, 5);//metodo generar 5 numeros aleatorios entre 1 y 10 
//        System.out.println(listaNumero);
//        generarUrls(listaNumero);//metodo para generar urls para obtener fichas
//        System.out.println(urls);
//        return urls;
//    }
//
//    //método para generar una lista de numero aleatorios sin repetirse
//    public void generarUrls(ArrayList lista) {
//        for (int x = 0; x < lista.size(); x++) {
////            String url = FacesContext.getCurrentInstance() + "/imgFicha/" + listaNumero.get(x) + ".png";
//            ServletContext servlet=(ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
//            String url = (String)servlet.getRealPath("/imgFicha/")+lista.get(x)+".jpg";
//            urls.add(url);
//        }
//    }
//    
//    public void generar(int valorInicial, int valorFinal, int numAleatorio) {
//        for (int i = 0; i < numAleatorio;) {
//            int numero = (int) (Math.random() * (valorFinal - valorInicial + 1) + valorInicial);//genero un numero
//            if (listaNumero.isEmpty()) {//si la lista esta vacia
//                listaNumero.add(numero);
//                i++;
//            } else {//si no esta vacia
//                if (listaNumero.contains(numero)) {//Si el numero que generé esta contenido en la lista
//                } else {//Si no esta contenido en la lista
//                    listaNumero.add(numero);
//                    i++;
//                }
//            }
//        }
//    }
//
//    public Ficha getFicha() {
//        return ficha;
//    }
//
//    public void setFicha(Ficha ficha) {
//        this.ficha = ficha;
//    }
//
//    public List<String> getUrls() {
//        return urls;
//    }
//
//    public void setUrls(List<String> urls) {
//        this.urls = urls;
//    }
