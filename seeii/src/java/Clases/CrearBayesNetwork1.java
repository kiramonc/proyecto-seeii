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
import org.openmarkov.core.model.network.NodeType;
import org.openmarkov.core.model.network.ProbNet;
import org.openmarkov.core.model.network.ProbNode;
import org.openmarkov.core.model.network.State;
import org.openmarkov.core.model.network.Variable;
import org.openmarkov.core.model.network.VariableType;
import org.openmarkov.core.model.network.potential.Potential;
import org.openmarkov.core.model.network.potential.TablePotential;
import org.openmarkov.io.probmodel.PGMXReader;
import org.openmarkov.io.probmodel.PGMXWriter;

/**
 *
 * @author Usuario
 */
public class CrearBayesNetwork1 {

    final private String bayesNetworkName = "BayesNetwork.pgmx";

    public void crearBD() throws FileNotFoundException, ParserException, ProbNodeNotFoundException, InvalidStateException, IncompatibleEvidenceException, NotEvaluableNetworkException, UnexpectedInferenceException, IOException {
        try {
            // Ruta donde se guarda
            String path = new File(".").getCanonicalPath()+"/"+bayesNetworkName;

            // Creación de red tipo Red Bayesiana
            ProbNet probNet1 = new ProbNet(org.openmarkov.core.model.network.type.BayesianNetworkType.getUniqueInstance());

            // Se define los estados
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
             
            TablePotential tablaPUnidad = unidad.createDeltaTablePotential(0); // Variable
            tablaPUnidad.values[0] = 0.3;
            tablaPUnidad.values[1] = 0.7;
            Potential potential = tablaPUnidad.addVariable(tema); // se agregan los padres
            
            /**
             * TABLA PARA NODO INTERMEDIO --> [TIENE PADRE(s) E HIJO(s)]
             */
            
            TablePotential tablaPTema = tema.createDeltaTablePotential(0); // Variable
            // Asignación de probabilidades para el nodo
            double[] valores = new double[2];
            valores[0] = 0.98;     // valor para estado sí
            valores[1] = 1 - valores[0];     // valor para estado no
            tablaPTema.setValues(valores);
            Potential potential1 = tablaPTema.addVariable(concepto1).addVariable(concepto2); // se agregan los padres
            
            /**
             * Tabla PARA NODOS PADRE --> [SIN PADRES]
             */
            
            TablePotential tablaPConcepto = concepto1.createDeltaTablePotential(0); // Variable concepto1
            // Asignación de probabilidades para el nodo
            tablaPConcepto.values[0] = 0.9; //valor para Sí
            tablaPConcepto.values[1] = 1 - tablaPConcepto.values[0];
            Potential potential2 = tablaPConcepto.addVariable(concepto1);
            
            TablePotential tablaPConcepto2 = concepto2.createDeltaTablePotential(0); // Variable concepto2
            // Asignación de probabilidades para el nodo
            tablaPConcepto2.values[0] = 0.9; // valor para Sí
            tablaPConcepto2.values[1] = 1 - tablaPConcepto2.values[0];
            Potential potential3 = tablaPConcepto2.addVariable(concepto2);

            /**
             * Creación de nodos
             */
            
            ProbNode nodo = new ProbNode(probNet1, unidad, NodeType.CHANCE);
            nodo.setPotential(potential);
            probNet1.addProbNode(nodo);

            ProbNode nodo1 = new ProbNode(probNet1, tema, NodeType.CHANCE);
            nodo1.setPotential(potential1);
            probNet1.addProbNode(nodo1);

            ProbNode nodo2 = new ProbNode(probNet1, concepto1, NodeType.CHANCE);
            nodo2.setPotential(potential2);
            probNet1.addProbNode(nodo2);

            ProbNode nodo3 = new ProbNode(probNet1, concepto2, NodeType.CHANCE);
            nodo3.setPotential(potential3);
            probNet1.addProbNode(nodo3);

            /**
             * Escritura de la red
             */
            
            PGMXWriter pgmxWriter = new PGMXWriter();
            pgmxWriter.writeProbNet(path, probNet1);

            // Resultados de la red escrita
            InputStream file1 = new FileInputStream(new File(path));
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
            Logger.getLogger(CrearBayesNetwork1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        CrearBayesNetwork1 c = new CrearBayesNetwork1();
        try {
            c.crearBD();
        } catch (FileNotFoundException | ParserException | ProbNodeNotFoundException | InvalidStateException | IncompatibleEvidenceException | NotEvaluableNetworkException | UnexpectedInferenceException ex) {
            Logger.getLogger(CrearBayesNetwork1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CrearBayesNetwork1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}