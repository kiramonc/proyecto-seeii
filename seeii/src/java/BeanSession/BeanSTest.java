/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanSession;

import Clases.Encrypt;
import Dao.DaoTest;
import Dao.DaoUsuario;
import HibernateUtil.HibernateUtil;
import Pojo.Test;
import Pojo.Usuario;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author KathyR
 */
@ManagedBean
@SessionScoped
public class BeanSTest {

    private Session session;
    private Transaction transaction;

    private int codigoTest;

    /**
     * Creates a new instance of BeanSTest
     */
    public BeanSTest() {

    }

    public String iniciarTest(String codigo) {
        this.codigoTest = Integer.parseInt(codigo);
        this.session = null;
        this.transaction = null;

        try {
            HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            httpSession.setAttribute("codigoTest", this.codigoTest);
            return "test";

        } catch (Exception ex) {
            return null;
        }
    }

    public void finalizar() {
        this.codigoTest = -1;
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        httpSession.invalidate();
    }

    public int getCodigoTest() {
        return codigoTest;
    }

    public void setCodigoTest(int codigoTest) {
        this.codigoTest = codigoTest;
    }

}
