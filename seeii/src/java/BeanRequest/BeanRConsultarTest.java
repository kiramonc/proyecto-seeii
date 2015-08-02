/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Dao.DaoTema;
import Dao.DaoTest;
import HibernateUtil.HibernateUtil;
import Pojo.Pregunta;
import Pojo.Tema;
import Pojo.Test;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author KathyR
 */
@ManagedBean
@ViewScoped
public class BeanRConsultarTest {

    private Test test = new Test();
    private List<Test> listaTest;
    private List<Test> listaTestFiltrada;
    private List<Pregunta> listaPreguntas;
    private Session session;
    private Transaction transaction;
    private MenuModel model;

    public BeanRConsultarTest() {
        model = new DefaultMenuModel();

    }

    public Test consultarTestPorCodigo(int idTest) {
        this.session = null;
        this.transaction = null;
        try {
            DaoTest daoUnidad = new DaoTest();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            Test t = daoUnidad.verPorCodigoTest(session, idTest);
            this.test=t;
            transaction.commit();
            return t;
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public List<Test> getAllTest() {
        this.session = null;
        this.transaction = null;
        try {
            DaoTest daoTest = new DaoTest();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.listaTest = daoTest.verTodo(session);
            this.transaction.commit();
            return this.listaTest;

        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR:", "Contacte con el administrador" + ex.getMessage()));
            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public List<Test> getListaTest() {
        return listaTest;
    }

    public void setListaTest(List<Test> listaTest) {
        this.listaTest = listaTest;
    }

    public List<Test> getListaTestFiltrada() {
        return listaTestFiltrada;
    }

    public void setListaTestFiltrada(List<Test> listaTestFiltrada) {
        this.listaTestFiltrada = listaTestFiltrada;
    }

    public List<Pregunta> getListaPreguntas() {
        return listaPreguntas;
    }

    public void setListaPreguntas(List<Pregunta> listaPreguntas) {
        this.listaPreguntas = listaPreguntas;
    }

    public List<Test> getItemsMenu() {
        this.session = null;
        this.transaction = null;
        try {
            DaoTest daoTest = new DaoTest();
            DaoTema daoTema = new DaoTema();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            List<Tema> temas = daoTema.verPorUnidad(session, 1);

            Test t = null;
            List<Test> listaTests = new ArrayList<>();
            for (int i = 0; i < temas.size(); i++) {
                t = daoTest.verPorTema(session, temas.get(i).getIdTema());
                listaTests.add(t);
            }
            this.listaTest = listaTests;
            this.transaction.commit();
            return this.listaTest;
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CREAR MENU DINÁMICO:", "Contacte con el administrador" + ex.getMessage()));
            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }
        
//    public MenuModel getModel() {
//        this.session = null;
//        this.transaction = null;
//        try {
//            DaoTest daoTest = new DaoTest();
//            DaoTema daoTema = new DaoTema();
//            this.session = HibernateUtil.getSessionFactory().openSession();
//            this.transaction = session.beginTransaction();
//            List<Tema> temas = daoTema.verPorUnidad(session, 1);
//
//            Test t = null;
//            List<Test> listaTests = new ArrayList<>();
//            for (int i = 0; i < temas.size(); i++) {
//                t = daoTest.verPorTema(session, temas.get(i).getIdTema());
//                listaTests.add(t);
//            }
//            this.listaTest = listaTests;
//
//            this.transaction.commit();
//            System.out.println("Lista Test tiene un total de: " + listaTest.size() + " elementos");
//            MenuModel model = new DefaultMenuModel();
//            DefaultMenuItem item = null;
//            
//            for (int i = 0; i < listaTest.size(); i++) {
//                item = new DefaultMenuItem(listaTest.get(i).getTema().getNombre());
//                item.setIcon("/resources/iconos/Tutorial.ico");
////                item.setCommand("#{beanSTest.iniciarTest('"+Integer.toString(listaTests.get(i).getIdTest())+"')}");
//                if(i==0){
////                item.setOutcome("/estudiante/estudianteK/test");
//                item.setCommand("#{beanSTest.iniciarTest(\"1\")}");
//                }else{
//                    if(i==1){
////                    item.setOutcome("/estudiante/estudianteK/testListening1");
//                    }else{
////                        item.setOutcome("/estudiante/estudianteK/testSpeaking3");
//                    }
//                }
//                model.addElement(item);
//            }
//            this.model = model;
//            System.out.println("El modelo tiene " + this.model.getElements().size() + " elementos");
//
//            
//            return this.model;
//
//        } catch (Exception ex) {
//            if (this.transaction != null) {
//                this.transaction.rollback();
//            }
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CREAR MENU DINÁMICO:", "Contacte con el administrador" + ex.getMessage()));
//            return null;
//        } finally {
//            if (this.session != null) {
//                this.session.close();
//            }
//        }
//    }

}
