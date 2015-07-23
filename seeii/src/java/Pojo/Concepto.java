package Pojo;
// Generated 22/07/2015 07:29:31 PM by Hibernate Tools 3.6.0


import java.util.HashSet;
import java.util.Set;

/**
 * Concepto generated by hbm2java
 */
public class Concepto  implements java.io.Serializable {


     private int idConcepto;
     private Tema tema;
     private String nombreConcepto;
     private String traduccion;
     private String descripcion;
     private Set preguntas = new HashSet(0);

    public Concepto() {
    }

	
    public Concepto(int idConcepto, Tema tema, String nombreConcepto, String traduccion, String descripcion) {
        this.idConcepto = idConcepto;
        this.tema = tema;
        this.nombreConcepto = nombreConcepto;
        this.traduccion = traduccion;
        this.descripcion = descripcion;
    }
    public Concepto(int idConcepto, Tema tema, String nombreConcepto, String traduccion, String descripcion, Set preguntas) {
       this.idConcepto = idConcepto;
       this.tema = tema;
       this.nombreConcepto = nombreConcepto;
       this.traduccion = traduccion;
       this.descripcion = descripcion;
       this.preguntas = preguntas;
    }
   
    public int getIdConcepto() {
        return this.idConcepto;
    }
    
    public void setIdConcepto(int idConcepto) {
        this.idConcepto = idConcepto;
    }
    public Tema getTema() {
        return this.tema;
    }
    
    public void setTema(Tema tema) {
        this.tema = tema;
    }
    public String getNombreConcepto() {
        return this.nombreConcepto;
    }
    
    public void setNombreConcepto(String nombreConcepto) {
        this.nombreConcepto = nombreConcepto;
    }
    public String getTraduccion() {
        return this.traduccion;
    }
    
    public void setTraduccion(String traduccion) {
        this.traduccion = traduccion;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Set getPreguntas() {
        return this.preguntas;
    }
    
    public void setPreguntas(Set preguntas) {
        this.preguntas = preguntas;
    }




}


