/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openmarkov.core.action.PNEdit;
import org.openmarkov.core.action.PrecisionEdit;
import org.openmarkov.core.exception.DoEditException;
import org.openmarkov.core.exception.IncompatibleEvidenceException;
import org.openmarkov.core.exception.InvalidStateException;
import org.openmarkov.core.exception.NotEvaluableNetworkException;
import org.openmarkov.core.exception.ParserException;
import org.openmarkov.core.exception.ProbNodeNotFoundException;
import org.openmarkov.core.exception.UnexpectedInferenceException;
import org.openmarkov.core.exception.WriterException;
import org.openmarkov.core.gui.action.TablePotentialValueEdit;
import org.openmarkov.core.gui.component.ICIValuesTable;
import org.openmarkov.core.gui.component.LinkRestrictionValuesTable;
import org.openmarkov.core.gui.component.LinkRestrictionValuesTableModel;
import org.openmarkov.core.gui.component.ValuesTable;
import org.openmarkov.core.gui.component.ValuesTableModel;
import org.openmarkov.core.gui.dialog.common.CPTablePanel;
import org.openmarkov.core.gui.dialog.common.ProbabilityTablePanel;
import org.openmarkov.core.gui.graphic.VisualNetwork;
import org.openmarkov.core.model.graph.LabelledLink;
import org.openmarkov.core.model.graph.Link;
import org.openmarkov.core.model.graph.Node;
import org.openmarkov.core.model.network.NodeType;
import org.openmarkov.core.model.network.ProbNet;
import org.openmarkov.core.model.network.ProbNode;
import org.openmarkov.core.model.network.State;
import org.openmarkov.core.model.network.UtilityCombinationFunction;
import org.openmarkov.core.model.network.Variable;
import org.openmarkov.core.model.network.VariableType;
import org.openmarkov.core.model.network.modelUncertainty.ComplementFunction;
import org.openmarkov.core.model.network.modelUncertainty.ExactFunction;
import org.openmarkov.core.model.network.modelUncertainty.ProbDensFunction;
import org.openmarkov.core.model.network.modelUncertainty.UncertainValue;
import org.openmarkov.core.model.network.potential.Potential;
import org.openmarkov.core.model.network.potential.PotentialRole;
import org.openmarkov.core.model.network.potential.ProductPotential;
import org.openmarkov.core.model.network.potential.TablePotential;
import org.openmarkov.core.model.network.potential.canonical.TuningPotential;
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
            // Se define los estados
            State state = new State("sí");
            State state1 = new State("no");
            State[] estados = new State[2];
            estados[0] = state;
            estados[1] = state1;

            // CREACIÓN DE VARIABLES
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

            // TABLA PARA HIJO
            TablePotential tablaPUnidad = unidad.createDeltaTablePotential(0); // Variable hijo
            tablaPUnidad.setUniform();
            Potential potential = tablaPUnidad.addVariable(tema); // se agregan los padres
            tablaPUnidad.values[0] = 1;
            
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

            // TABLA PARA PADRE-HIJO
            TablePotential tablaPTema = tema.createDeltaTablePotential(0); // Variable hijo
//            tablaPTema.setUniform();

            List<Variable> listaVariables = new ArrayList<>();
//            listaVariables.add(tema);
            listaVariables.add(concepto1);
            listaVariables.add(concepto2);

            int[] indiceEstados = new int[2];
//            indiceEstados[0] = concepto1.getStateIndex("sí");
//            tablaPTema.setValue(listaVariables, indiceEstados, 0.7);
            

            indiceEstados[0] = 1;
            indiceEstados[1] = 1;
//            tablaPTema.setValue(listaVariables, indiceEstados, 0.6);
            
//            double[] valores= new double[2];
//            valores[0]=0.2;
//            valores[1]=0.8;
//            tablaPTema.setValues(valores);
//            System.out.println("TAMAÑO TABLE: " +tablaPTema.getTableSize());
//            for (int i = 0; i < tablaPTema.values.length; i++) {
//                System.out.println("Valor en " + tablaPTema.values[i]);
//            }
            
            double[] valores= new double[2];
            valores[0]=0.3;//sí
            valores[1]=0.7;//no
            ProbDensFunction probEx= new ExactFunction(0.3);
//            ProbDensFunction probCom= new ComplementFunction(0.7);
            UncertainValue incert0= new UncertainValue(probEx);
//            UncertainValue incert1= new UncertainValue(probCom);
//            
//            incert0.setName("sí");
////            incert0.getProbDensFunction().setParameters(valores);
////            System.out.println("parámetro en la posición 0: "+incert0.getProbDensFunction().getParameters()[0]);
////            incert1.getProbDensFunction().setParameters(valores);
//            incert1.setName("no");
            UncertainValue[] incertidumbre= new UncertainValue[2];
            incertidumbre[0]= incert0;
//            incertidumbre[1]= incert1;
//            tablaPTema.setUncertaintyTable(incertidumbre);
//            System.out.println("INCERTIDUMBRE 0: "+ incertidumbre[0].getName());
//            System.out.println("INCERTIDUMBRE 0: "+ incertidumbre[0].getProbDensFunction());
            tablaPTema.setValues(valores);
            

            Potential potential1 = tablaPTema.addVariable(concepto1).addVariable(concepto2); // se agregan los padres
            System.out.println("STRING DEL POTENTIAL1: " +potential1.toString());
            HashMap<Variable, Integer> hm= new HashMap<>();
            hm.put(tema, 1);
            hm.put(concepto1, 0);
            hm.put(concepto2, 0);
//            hm.put(tema, 1);
            System.out.println("LA PROBABILIDAD DEL TEMA ES: " +potential1.getProbability(hm));
            
//            Potential potential1 = tablaPTema.addVariable(tema); // se agregan los padres
            
            // Tabla PARA PADRE
            TablePotential tablaPConcepto = concepto1.createDeltaTablePotential(0); // Variable padre
            tablaPConcepto.setUniform();
            tablaPConcepto.values[0] = 0.9; //valor para Sí
            tablaPConcepto.values[1] = 1.0 - tablaPConcepto.values[0];
            Potential potential2 = tablaPConcepto.addVariable(concepto1); // la misma variable

            TablePotential tablaPConcepto2 = concepto2.createDeltaTablePotential(0); // Variable padre
            tablaPConcepto2.setUniform();
            tablaPConcepto2.values[0] = 0.9; //valor para Sí
            tablaPConcepto2.values[1] = 1.0 - tablaPConcepto2.values[0];
            Potential potential3 = tablaPConcepto2.addVariable(concepto2); // la misma variable

            // Creación de nodos
            ProbNode nodo = new ProbNode(probNet1, unidad, NodeType.CHANCE);
            nodo.setPotential(potential);
            probNet1.addProbNode(nodo);

            ProbNode nodo1 = new ProbNode(probNet1, tema, NodeType.CHANCE);
            nodo1.setPotential(potential1);
            Object[][] datos = new Object[2][4];
            datos[0][0] = 1;
            datos[1][0] = 0;
            datos[0][1] = 0.1;
            datos[1][1] = 0.9;
            datos[0][2] = 0.2;
            datos[1][2] = 0.8;
            datos[0][3] = 0.3;
            datos[1][3] = 0.7;

            TablePotentialValueEdit tpve= new TablePotentialValueEdit(nodo1, probNet1, tablaPTema, 0.2, 0, 1, null, datos);
//            tpve.doEdit();
            probNet1.addProbNode(nodo1);
            
            ProbNode nodo2 = new ProbNode(probNet1, concepto1, NodeType.CHANCE);
            nodo2.setPotential(potential2);
            probNet1.addProbNode(nodo2);

            ProbNode nodo3 = new ProbNode(probNet1, concepto2, NodeType.CHANCE);
            nodo3.setPotential(potential3);
            probNet1.addProbNode(nodo3);

            // Escritura de la red
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
//        } catch (DoEditException ex) {
//            Logger.getLogger(CrearBayesNetwork.class.getName()).log(Level.SEVERE, null, ex);
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
