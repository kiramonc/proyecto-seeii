/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Clases.ListFicha;
import Pojo.Ficha;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.ServletContext;

/**
 *
 * @author silvy
 */
@ManagedBean
@RequestScoped
public class BeanRaprendizajeFicha {

    private Ficha ficha;
//    private ListFicha listaF;
//    private List<ListFicha> fichaList;
//    private List<Integer> aleatorios;

    public BeanRaprendizajeFicha() {
        
    }

    
    public List<ListFicha> generar(int valorInicial, int valorFinal, int numAleatorio) {
       ArrayList listaNumero = new ArrayList();
       List<ListFicha> listaN= new ArrayList();
       
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
        System.out.println(".............................................."+listaNumero);
        for (int i = 0; i < listaNumero.size(); i++) {
            ListFicha lista=new ListFicha((Integer)listaNumero.get(i));
            System.out.println(lista);
            listaN.add(lista);
        }
                System.out.println(listaN);

        return listaN;
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
