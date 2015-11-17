/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Clases.RedBayesiana;

import Pojo.Concepto;
import Pojo.Estudiante;

/**
 *
 * @author KathyR
 */
public class Resultado1 {
    private int idResultado;
    private Estudiante estudiante;
    private Concepto concepto;
    private double valor;

    public Resultado1() {
    }

    public Resultado1(int idResultado, Estudiante estudiante, Concepto concepto, double valor) {
        this.idResultado = idResultado;
        this.estudiante = estudiante;
        this.concepto = concepto;
        this.valor = valor;
    }

    public int getIdResultado() {
        return idResultado;
    }

    public void setIdResultado(int idResultado) {
        this.idResultado = idResultado;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Concepto getConcepto() {
        return concepto;
    }

    public void setConcepto(Concepto concepto) {
        this.concepto = concepto;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
    
    
    
    
}
