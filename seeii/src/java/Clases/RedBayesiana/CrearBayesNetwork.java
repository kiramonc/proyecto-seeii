/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases.RedBayesiana;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openmarkov.core.exception.IncompatibleEvidenceException;
import org.openmarkov.core.exception.InvalidStateException;
import org.openmarkov.core.exception.NotEvaluableNetworkException;
import org.openmarkov.core.exception.ParserException;
import org.openmarkov.core.exception.ProbNodeNotFoundException;
import org.openmarkov.core.exception.UnexpectedInferenceException;
import org.openmarkov.core.exception.WriterException;
import org.openmarkov.core.gui.action.TablePotentialValueEdit;
import org.openmarkov.core.model.network.NodeType;
import org.openmarkov.core.model.network.ProbNet;
import org.openmarkov.core.model.network.ProbNode;
import org.openmarkov.core.model.network.State;
import org.openmarkov.core.model.network.Variable;
import org.openmarkov.core.model.network.VariableType;
import org.openmarkov.core.model.network.modelUncertainty.ExactFunction;
import org.openmarkov.core.model.network.modelUncertainty.ProbDensFunction;
import org.openmarkov.core.model.network.modelUncertainty.UncertainValue;
import org.openmarkov.core.model.network.potential.GTablePotential;
import org.openmarkov.core.model.network.potential.Potential;
import org.openmarkov.core.model.network.potential.PotentialRole;
import org.openmarkov.core.model.network.potential.TablePotential;
import org.openmarkov.io.probmodel.PGMXReader;
import org.openmarkov.io.probmodel.PGMXWriter;

/**
 *
 * @author KathyR
 */
public class CrearBayesNetwork {

    private String nodosRed;
    public static List<String> s = new ArrayList<String>();
    final private String bayesNetworkName = "BayesNetworkIngles.pgmx";

    public void ejemplo() {
        InputStream file = null;
        try {
            file = new FileInputStream(new File("C:\\Users\\KathyR\\Dropbox\\Décimo\\Trabajo de titulación\\Red bayesiana\\PlanEstudio5prob.pgmx"));
            //cargar la red bayesiana
            PGMXReader pgmxReader = new PGMXReader();
            //probabilidades de la red bayesiana
            ProbNet probNet = pgmxReader.loadProbNet(file, bayesNetworkName).getProbNet();
            List<ProbNode> lista = probNet.getProbNodes();
            for (int i = 0; i < lista.size(); i++) {
                ProbNode probNode = lista.get(i);
                System.out.println(probNode.getName());
                System.out.println("Nombre: " + probNode.getName());
                System.out.println("---- ProbNet: " + probNode.getProbNet());
                System.out.println("---- Relevance: " + probNode.getRelevance());
                System.out.println("---- NodeTye: " + probNode.getNodeType().toString());
            }
            System.out.println();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CrearBayesNetwork.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserException ex) {
            Logger.getLogger(CrearBayesNetwork.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                file.close();
            } catch (IOException ex) {
                Logger.getLogger(CrearBayesNetwork.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void leerArchivo() throws FileNotFoundException, ParserException, ProbNodeNotFoundException, InvalidStateException, IncompatibleEvidenceException, NotEvaluableNetworkException, UnexpectedInferenceException {

        try {
            // Ruta donde se guarda
            String file = "C:\\Users\\KathyR\\Dropbox\\Décimo\\Trabajo de titulación\\Red bayesiana\\BayesNetworkIngles1.pgmx";
            // Creación de red tipo Red Bayesiana
            File archivoPgmx = new File(file);
            ProbNet probNet1;
//            if (archivoPgmx.exists()) {
//                InputStream inputStream = new FileInputStream(archivoPgmx);
//                PGMXReader pgmxReader = new PGMXReader();
//                probNet1 = pgmxReader.loadProbNet(inputStream, bayesNetworkName).getProbNet();
//            } else {
                probNet1 = new ProbNet(org.openmarkov.core.model.network.type.BayesianNetworkType.getUniqueInstance());
//            }
                
            /**
             * Se define los estados
             */
            State state = new State("sí");
            State state1 = new State("no");
            State[] estados = new State[2];
            estados[0] = state;
            estados[1] = state1;

            /** 
             * CREACIÓN DE VARIABLES
             */
            Variable unidad = new Variable("Unidad");
            unidad.setVariableType(VariableType.FINITE_STATES);
            unidad.setStates(estados);
            Variable tema = new Variable("Tema1");
            tema.setVariableType(VariableType.FINITE_STATES);
            tema.setStates(estados);

            Variable concepto1 = new Variable("Concepto1");
            concepto1.setVariableType(VariableType.FINITE_STATES);
            concepto1.setStates(estados);

            Variable concepto2 = new Variable("Concepto2");
            concepto2.setVariableType(VariableType.FINITE_STATES);
            concepto2.setStates(estados);

            /**
             * TABLA DE POTENCIALES PARA NODO HOJA
             */
            TablePotential tablaPUnidad = unidad.createDeltaTablePotential(0); // Variable hijo
            tablaPUnidad.setPotentialRole(PotentialRole.CONDITIONAL_PROBABILITY); // Rol
            List<Variable> listaVUnidad= new ArrayList<>();
            listaVUnidad.add(unidad); // Variable Condicionada
            listaVUnidad.add(tema); // Variable Condicionante TEMAS
            tablaPUnidad.setVariables(listaVUnidad); // Establecer Variables
            double[] valoresUnidad= new double[4];
            valoresUnidad[0]=0.9; // valor 1 1
            valoresUnidad[1]=1-valoresUnidad[0]; // valor 0 1 
            valoresUnidad[2]=0.7; // valor 1 0
            valoresUnidad[3]=1-valoresUnidad[2]; // valor 0 0
            tablaPUnidad.setValues(valoresUnidad); // Establecer valores
            
//            Variable unidad1 = probNet1.getVariable("Unidad");
//            TablePotential tablaPUnidad = unidad1.createDeltaTablePotential(0); // Variable hijo
//            tablaPUnidad.setUniform();
//            List<Variable> listaVariablesUnidad = tablaPUnidad.getVariables();
//            Potential potential = null;
//            for (int i = 0; i < listaVariablesUnidad.size(); i++) {
//                if (i == 0) {
//                    potential = tablaPUnidad.addVariable(listaVariablesUnidad.get(i)); // se agregan los padres   
//                } else {
//                    potential = potential.addVariable(listaVariablesUnidad.get(i)); // se agregan los padres
//                }
//            }
//            potential = potential.addVariable(tema);
//            tablaPUnidad.values[0] = 1;

            /**
             * TABLA PARA NODO INTERMEDIO --> [TIENE PADRE(s) E HIJO(s)]
             */
            TablePotential tablaPTema = tema.createDeltaTablePotential(0); // Variable hijo
            tablaPTema.setPotentialRole(PotentialRole.CONDITIONAL_PROBABILITY); // Rol
            List<Variable> listaV= new ArrayList<>();
            listaV.add(tema); // Variable Condicionada
            listaV.add(concepto2); // Variable Condicionante2 CONCEPTOS
            listaV.add(concepto1); // Variable Condicionante1 CONCEPTOS
            tablaPTema.setVariables(listaV); // Establecer Variables
            double[] valores= new double[8]; // Regla 2^NumVariables
            valores[0]=0.9; // valor 1 1 1
            valores[1]=1-valores[0]; // valor 0 1 1
            valores[2]=0.7; // valor 1 1 0
            valores[3]=1-valores[2]; // valor 0 1 0
            valores[4]=0.6; // valor 1 0 1
            valores[5]=1-valores[4]; // valor 0 0 1
            valores[6]=0.2; // valor 1 0 0
            valores[7]=1-valores[6]; // valor 0 0 0
            tablaPTema.setValues(valores); // Establecer valores
            System.out.println("Número variables tema: "+tablaPTema.getVariables().size());
            /**
             * Tabla PARA NODOS PADRE --> [SIN PADRES]
             */
            TablePotential tablaPConcepto = concepto1.createDeltaTablePotential(0); // Variable padre
            List<Variable> listaVConcepto= new ArrayList<>();
            listaVConcepto.add(concepto1); // Variable Condicionada
            tablaPConcepto.setVariables(listaVConcepto);
            double[] valoresConcepto= new double[2];
            valoresConcepto[0]=0.9; // valor 1
            valoresConcepto[1]=1-valoresConcepto[0]; // valor 0
            tablaPConcepto.setValues(valoresConcepto);

            TablePotential tablaPConcepto2 = concepto2.createDeltaTablePotential(0); // Variable padre
            List<Variable> listaVConcepto1= new ArrayList<>();
            listaVConcepto1.add(concepto2); // Variable Condicionada
            tablaPConcepto2.setVariables(listaVConcepto1);
            double[] valoresConcepto2= new double[2];
            valoresConcepto2[0]=0.9; // valor 1
            valoresConcepto2[1]=1-valoresConcepto2[0]; // valor 0
            tablaPConcepto2.setValues(valoresConcepto2);

            /** 
             * Creación de nodos
             */
            ProbNode nodo = new ProbNode(probNet1, unidad, NodeType.CHANCE);
            nodo.setPotential(tablaPUnidad);
            probNet1.addProbNode(nodo);

            ProbNode nodo1 = new ProbNode(probNet1, tema, NodeType.CHANCE);
            nodo1.setPotential(tablaPTema);
            probNet1.addProbNode(nodo1);
            
            ProbNode nodo2 = new ProbNode(probNet1, concepto1, NodeType.CHANCE);
            nodo2.setPotential(tablaPConcepto);
            probNet1.addProbNode(nodo2);

            ProbNode nodo3 = new ProbNode(probNet1, concepto2, NodeType.CHANCE);
            nodo3.setPotential(tablaPConcepto2);
            probNet1.addProbNode(nodo3);

            /**
             * Escritura de la red
             */
            PGMXWriter pgmxWriter = new PGMXWriter();
            pgmxWriter.writeProbNet(file, probNet1);

            // Resultados de la red escrita
            InputStream file1 = new FileInputStream(new File(file));
            PGMXReader pgmxReader = new PGMXReader();
            ProbNet probNet = pgmxReader.loadProbNet(file1, bayesNetworkName).getProbNet();
            List<ProbNode> lista = probNet.getProbNodes();
            for (int i = 0; i < lista.size(); i++) {
                ProbNode probNode = lista.get(i);
                System.out.println("Nombre: " + probNode.getName());
                System.out.println("---- ProbNet: " + probNode.getProbNet());
                System.out.println("---- Relevance: " + probNode.getRelevance());
                System.out.println("---- NodeTye: " + probNode.getNodeType().toString());
            }
            System.out.println();
        } catch (WriterException ex) {
            Logger.getLogger(CrearBayesNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getNodosRed() {
        return nodosRed;
    }

    public void setNodosRed(String nodosRed) {
        this.nodosRed = nodosRed;
    }

    public static void main(String[] args) {
        CrearBayesNetwork c = new CrearBayesNetwork();
        try {
//            c.ejemplo();
            c.leerArchivo();
            System.out.println("Termino de ejecutar");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CrearBayesNetwork.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserException ex) {
            Logger.getLogger(CrearBayesNetwork.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProbNodeNotFoundException ex) {
            Logger.getLogger(CrearBayesNetwork.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidStateException ex) {
            Logger.getLogger(CrearBayesNetwork.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IncompatibleEvidenceException ex) {
            Logger.getLogger(CrearBayesNetwork.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotEvaluableNetworkException ex) {
            Logger.getLogger(CrearBayesNetwork.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnexpectedInferenceException ex) {
            Logger.getLogger(CrearBayesNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
