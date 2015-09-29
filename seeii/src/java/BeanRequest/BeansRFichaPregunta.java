/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Dao.DaoEntrenar;
import Dao.DaoFicha;
import Dao.DaoFichaPregunta;
import Dao.DaoPreguntaEntrenar;
import HibernateUtil.HibernateUtil;
import Pojo.Entrenamiento;
import Pojo.Ficha;
import Pojo.Fichaspregunta;
import Pojo.Preguntaentrenar;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author silvy
 */
@ManagedBean
@ViewScoped//creo que es de cambia el beans tipo view
public class BeansRFichaPregunta {

    private Fichaspregunta fichaPregunta;
    private List<Fichaspregunta> listFichasPregunta;
    private List<Ficha> listFichas;

    private Session session;
    private Transaction transaction;

    public BeansRFichaPregunta() {
        fichaPregunta = new Fichaspregunta();
    }

    public List<Fichaspregunta> crearFichasPreguntas(int idEntrenamiento, int idPrenguntaEnt) {
        
        int sizeListaFicha = obtnerListaFichas(idEntrenamiento);
        ArrayList listaAleatorio;
        //si sizeListaFicha(tamaño de la lista de fichas) es diferente de cero(exista fichas)
        System.out.println("tamaño de la lista de fichas ..."+sizeListaFicha+"...............");
        if (sizeListaFicha >= 3) {
            //obtiene n(num) numeros aleatorios
            listaAleatorio = generarAleatoreo(1, sizeListaFicha, 3);
            for (int i = 0; i < 3; i++) {
                //aqui  registrar(FichaPregunta) la fija con (idFicha, idPreguntaEntrenar)
                registrarFichaPregunta((int) listaAleatorio.get(i), idPrenguntaEnt);
            }

            //metodo para obtnere la lista de fichasPreguntaCreadas anteriormente
            listaFichasPregunta(idPrenguntaEnt); //se inicializa---listFichasPregunta---para retornar
        } else {
            System.out.println("(NO HAY SUFUCIENTE O NO EXITE )lista de fichas, para este tema");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:", "NO HAY SUFUCIENTE O NO EXITE )lista de fichas, para este tema"));
            //se debe volver a la pagina inicioAprendizaje.xhtml
        }
        return listFichasPregunta; //se retorna ya que se inicializa en el MÉTODO listaFichasPregunta
    }

    public int obtnerListaFichas(int idEntrenamiento) {
        int sizeListaFichaPregunta = 0;
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            //obtener el entrenamiento mediante el id, para obtner el TEMA
            DaoEntrenar daoEntrenar = new DaoEntrenar();
            Entrenamiento entrenar = daoEntrenar.verPorCodigoEntrenamiento(session, idEntrenamiento);

            //obtener lista de fichas por le Tema segun el entrenamiento
            DaoFicha daoficha = new DaoFicha();
            listFichas = daoficha.verListfichasPorTema(session, entrenar.getTema().getIdTema());
            this.transaction.commit();

            //fijar el numero de fijas que se obtiene de la listFichas
            sizeListaFichaPregunta = listFichas.size();
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL OBTENER LISTA DE FICHAS:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
        return sizeListaFichaPregunta;
    }

    public ArrayList generarAleatoreo(int valorInicial, int valorFinal, int numAleatorio) {
        ArrayList listaNumero = new ArrayList();

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
        System.out.println(".........................." + listaNumero + ".........................");

        return listaNumero;
    }

    public void registrarFichaPregunta(int idFicha, int idPreguntaEntrena) {
        ///.......................................PROBAR S CREA
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            //obtener una ficha por su id y fijar en el Fichaspregunta.
            DaoFicha daoficha = new DaoFicha();
            Ficha ficha = daoficha.verPorCodigoFicha(session, idFicha);
            this.fichaPregunta.setFicha(ficha);

            //obtener una pregunta por su id y fijar en el Fichaspregunta.
            DaoPreguntaEntrenar daoPreguntaEntrenar = new DaoPreguntaEntrenar();
            Preguntaentrenar preguntaEntrenar = daoPreguntaEntrenar.verPorCodigoPreguntaEntrenar(session, idPreguntaEntrena);
            this.fichaPregunta.setPreguntaentrenar(preguntaEntrenar);

            //para crear Fichaspregunta (lista de fichas en base a un preguntadeENTRENAMIENTO)
            DaoFichaPregunta daofichaPregunta = new DaoFichaPregunta();
            daofichaPregunta.registrarFichaPregunta(session, this.fichaPregunta);//Devuelve TRUE al crear una fichaPregunta
            this.transaction.commit();

            System.out.println("Correcto: El registro de las fichasPregunta se ha realizado con exito");
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL REGISTRA FICHA_PREGUNTA:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }

    }

    public void listaFichasPregunta(int idPrenguntaEnt) {
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            //para crear Fichaspregunta (lista de fichas en base a un preguntadeENTRENAMIENTO)
            DaoFichaPregunta daofichaPregunta = new DaoFichaPregunta();
            listFichasPregunta = daofichaPregunta.verPreguntaEntrenamiento(session, idPrenguntaEnt);
            this.transaction.commit();

            System.out.println("Correcto: El registro de las fichasPregunta se ha realizado con exito");
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL REGISTRA FICHA_PREGUNTA:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public Fichaspregunta getFichaPregunta() {
        return fichaPregunta;
    }

    public void setFichaPregunta(Fichaspregunta fichaPregunta) {
        this.fichaPregunta = fichaPregunta;
    }

    public List<Ficha> getListFichas() {
        return listFichas;
    }

    public void setListFichas(List<Ficha> listFichas) {
        this.listFichas = listFichas;
    }

    public List<Fichaspregunta> getListFichasPregunta() {
        return listFichasPregunta;
    }

    public void setListFichasPregunta(List<Fichaspregunta> listFichasPregunta) {
        this.listFichasPregunta = listFichasPregunta;
    }

}