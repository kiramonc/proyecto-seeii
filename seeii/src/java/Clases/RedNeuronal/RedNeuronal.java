/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases.RedNeuronal;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 *
 * @author silvy
 */
public class RedNeuronal {

    

    public void redNeuronal() throws Exception {
        //si puntaje >= 200 entonces aprendido
        //si tiempo <= 240 (4 minutos) entonces aprendido
        //si errores <= 3 entonces aprendido
        String[] dato = {puntaje(400), tiempo(120), errores(2)};

//        ConverterUtils.DataSource con = new ConverterUtils.DataSource("E:\\Unl\\9no Modulo\\Sistemas Inteligentes\\Programacion\\2.Weka\\redAnd.arff");
        ConverterUtils.DataSource con = new ConverterUtils.DataSource("E:\\Unl\\10 Modulo\\2.ANTEPROYECTOS DE TESIS\\Proyecto\\Aplicacion\\redeAprendizaje.arff");

        Instances instances = con.getDataSet();
        System.out.println(instances);
        instances.setClassIndex(instances.numAttributes() - 1);

        MultilayerPerceptron mp = new MultilayerPerceptron();
        mp.buildClassifier(instances);

        Evaluation evalucion = new Evaluation(instances);
        evalucion.evaluateModel(mp, instances);
        System.out.println(evalucion.toSummaryString());
        System.out.println(evalucion.toMatrixString());

        String datosEntrada = null;
        String datosSalida = "no se puede predecir";
        for (int i = 0; i < instances.numInstances(); i++) {
            double predecido = mp.classifyInstance(instances.instance(i));
            datosEntrada = dato[0] + " " + dato[1] + " " + dato[2];
            if ((int) instances.instance(i).value(0) == Integer.parseInt(dato[0])
                    && (int) instances.instance(i).value(1) == Integer.parseInt(dato[1])
                    && (int) instances.instance(i).value(2) == Integer.parseInt(dato[2])) {
                datosSalida = instances.classAttribute().value((int) predecido);
            }
        }
        System.out.println("DATOS DE ENTRADA: " + datosEntrada);
        System.out.println("SALIDA PREDECIDA: " + datosSalida);

        switch (datosSalida) {
            case "0":
                System.out.println("Excelente ha aprendido");
                break;
            case "1":
                System.out.println("Disminuir Errores");
                break;
            case "2":
                System.out.println("Disminuir Tiempo");
                break;
            case "3":
                System.out.println("Disminuir Errores y tiempo");
                break;
            case "4":
                System.out.println("Subir Puntaje");
                break;
            case "5":
                System.out.println("Subir Puntaje y disminuir Errores");
                break;
            case "6":
                System.out.println("Subir Puntaje y disminuir Tiempo");
                break;
            case "7":
                System.out.println("Ponle mas Empeño");
                break;
            default:
                System.out.println("Verifique entradas, no se puede predecir");
                break;
        }
    }
    public String puntaje(int punto) {
        int valorPuntos;
        if (punto >= 200) {
            valorPuntos = 1;
        } else {
            valorPuntos = 0;
        }
        String puntaje=""+valorPuntos;
        return puntaje;
    }
    public String tiempo(int tiempo) {
        int valorTiempo;
        if (tiempo >= 240) { //mayor a 120(4 minutos)
            valorTiempo = 0;
        } else {
            valorTiempo = 1;
        }
        String tiemposEntrenar=""+valorTiempo;
        return tiemposEntrenar;
    }
    public String errores(int errors) {
        int valorError;
        if (errors > 3) {
            valorError = 0;
        } else {
            valorError = 1;
        }
        String erroresEntrenar=""+valorError;
        return erroresEntrenar;
    }
}
