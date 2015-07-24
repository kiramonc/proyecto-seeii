package Pojo;
// Generated 23/07/2015 06:24:00 PM by Hibernate Tools 3.6.0


import java.util.HashSet;
import java.util.Set;

/**
 * Tema generated by hbm2java
 */
public class Tema implements java.io.Serializable {

     private int idTema;
     private Unidadensenianza unidadensenianza;
     private String nombre;
     private String vocabulario;
     private String objetivo;
     private String dominio;
     private boolean estado;
     private Set fichas = new HashSet(0);
     private Set tests = new HashSet(0);

    public Tema() {
    }
	
    public Tema(int idTema, Unidadensenianza unidadensenianza, String nombre, String vocabulario, boolean estado) {
        this.idTema = idTema;
        this.unidadensenianza = unidadensenianza;
        this.nombre = nombre;
        this.vocabulario = vocabulario;
        this.estado = estado;
    }
    public Tema(int idTema, Unidadensenianza unidadensenianza, String nombre, String vocabulario, String objetivo, String dominio, boolean estado, Set fichas, Set tests) {
       this.idTema = idTema;
       this.unidadensenianza = unidadensenianza;
       this.nombre = nombre;
       this.vocabulario = vocabulario;
       this.objetivo = objetivo;
       this.dominio = dominio;
       this.estado = estado;
       this.fichas = fichas;
       this.tests = tests;
    }
   
    public int getIdTema() {
        return this.idTema;
    }
    
    public void setIdTema(int idTema) {
        this.idTema = idTema;
    }
    public Unidadensenianza getUnidadensenianza() {
        return this.unidadensenianza;
    }
    
    public void setUnidadensenianza(Unidadensenianza unidadensenianza) {
        this.unidadensenianza = unidadensenianza;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getVocabulario() {
        return this.vocabulario;
    }
    
    public void setVocabulario(String vocabulario) {
        this.vocabulario = vocabulario;
    }
    public String getObjetivo() {
        return this.objetivo;
    }
    
    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }
    public String getDominio() {
        return this.dominio;
    }
    
    public void setDominio(String dominio) {
        this.dominio = dominio;
    }
    public boolean isEstado() {
        return this.estado;
    }
    
    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    public Set getFichas() {
        return this.fichas;
    }
    
    public void setFichas(Set fichas) {
        this.fichas = fichas;
    }
    public Set getTests() {
        return this.tests;
    }
    
    public void setTests(Set tests) {
        this.tests = tests;
    }




}


