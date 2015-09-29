/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanSession;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author silvy
 */
@Named(value = "beanSEntrenar")
@SessionScoped
public class BeanSEntrenar implements Serializable {

    private Session session;
    private Transaction transaction;

    private int idEntrenamiento;
    private int idPrenguntaEnt;

    public BeanSEntrenar() {
    }

    public void iniciarEntrenamiento(int codigoEntrena) {
        this.idEntrenamiento = codigoEntrena;
        this.session = null;
        this.transaction = null;
        try {
            HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            httpSession.setAttribute("idEntrenamiento", this.idEntrenamiento);

        } catch (Exception ex) {
            System.out.println(".........Error al iniciar entrenamiento.........");
        }
    }

    public void finalizar() {
        this.idEntrenamiento = -1;
        this.idPrenguntaEnt=-1;
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        httpSession.invalidate();
    }

    public int getIdEntrenamiento() {
        return idEntrenamiento;
    }

    public void setIdEntrenamiento(int idEntrenamiento) {
        this.idEntrenamiento = idEntrenamiento;
    }

    public int getIdPrenguntaEnt() {
        return idPrenguntaEnt;
    }

    public void setIdPrenguntaEnt(int idPrenguntaEnt) {
        this.idPrenguntaEnt = idPrenguntaEnt;
    }

}
