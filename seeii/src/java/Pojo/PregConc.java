package Pojo;
// Generated 19/11/2015 09:00:12 PM by Hibernate Tools 3.6.0



/**
 * PregConc generated by hbm2java
 */
public class PregConc  implements java.io.Serializable {


     private int idPregConc;
     private Pregunta pregunta;
     private Concepto concepto;

    public PregConc() {
    }

    public PregConc(int idPregConc, Pregunta pregunta, Concepto concepto) {
       this.idPregConc = idPregConc;
       this.pregunta = pregunta;
       this.concepto = concepto;
    }
   
    public int getIdPregConc() {
        return this.idPregConc;
    }
    
    public void setIdPregConc(int idPregConc) {
        this.idPregConc = idPregConc;
    }
    public Pregunta getPregunta() {
        return this.pregunta;
    }
    
    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }
    public Concepto getConcepto() {
        return this.concepto;
    }
    
    public void setConcepto(Concepto concepto) {
        this.concepto = concepto;
    }




}


