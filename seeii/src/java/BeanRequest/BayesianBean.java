/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Clases.BayesNetworkIngles;
import Clases.Conocimiento;
import java.io.FileNotFoundException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.openmarkov.core.exception.IncompatibleEvidenceException;
import org.openmarkov.core.exception.InvalidStateException;
import org.openmarkov.core.exception.NotEvaluableNetworkException;
import org.openmarkov.core.exception.ParserException;
import org.openmarkov.core.exception.ProbNodeNotFoundException;
import org.openmarkov.core.exception.UnexpectedInferenceException;

/**
 *
 * @author KathyR
 */
@ManagedBean
@ViewScoped
public class BayesianBean {
    private Conocimiento conocimiento;
    private String red="Probabilidad";
    private BayesNetworkIngles bayes;

    public BayesianBean() {
    }

    //Metodos
    public void obtenerBayesianNet() throws FileNotFoundException, ParserException, ProbNodeNotFoundException, InvalidStateException, IncompatibleEvidenceException, NotEvaluableNetworkException, UnexpectedInferenceException{
        conocimiento=new Conocimiento("sí",//escuela
                "no",
                "no",
                "no",
                "no",
                "sí",//colores
                "sí",
                "sí",
                "sí",
                "sí",
                "sí",
                "sí",//números
                "sí",
                "sí",
                "sí",
                "sí",
                "sí",//familia
                "sí",
                "sí",
                "sí",//cuerpo
                "sí",
                "sí",
                "sí",
                "sí",
                "sí"
                );
        bayes= new BayesNetworkIngles();
        red= bayes.leerArchivo(conocimiento);
//        System.out.println("EL RESULTADO RECIBIDO ES "+red);
//        return "bayesian.xhtml?faces-redirect=true";
    }

    public Conocimiento getConocimiento() {
        return conocimiento;
    }

    public void setConocimiento(Conocimiento conocimiento) {
        this.conocimiento = conocimiento;
    }

    public String getRed() {
        return red;
    }

    public void setRed(String red) {
        this.red = red;
    }

    public BayesNetworkIngles getBayes() {
        return bayes;
    }

    public void setBayes(BayesNetworkIngles bayes) {
        this.bayes = bayes;
    }
    
}
