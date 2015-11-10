/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Clases.RedBayesiana;

import Clases.Conocimiento;
import Pojo.Pregunta;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.openmarkov.core.exception.IncompatibleEvidenceException;
import org.openmarkov.core.exception.InvalidStateException;
import org.openmarkov.core.exception.NotEvaluableNetworkException;
import org.openmarkov.core.exception.ParserException;
import org.openmarkov.core.exception.ProbNodeNotFoundException;
import org.openmarkov.core.exception.UnexpectedInferenceException;
import org.openmarkov.core.inference.InferenceAlgorithm;
import org.openmarkov.core.model.network.EvidenceCase;
import org.openmarkov.core.model.network.Finding;
import org.openmarkov.core.model.network.ProbNet;
import org.openmarkov.core.model.network.ProbNode;
import org.openmarkov.core.model.network.State;
import org.openmarkov.core.model.network.Util;
import org.openmarkov.core.model.network.Variable;
import org.openmarkov.core.model.network.potential.TablePotential;
import org.openmarkov.inference.variableElimination.VariableElimination;
import org.openmarkov.io.probmodel.PGMXReader;

/**
 *
 * @author KathyR
 */
public class BayesNetworkIngles {
    private String nodosRed;
    public static List<String> s = new ArrayList<String>();
    final private String bayesNetworkName = "Conocimiento.pgmx";

    // método para seleccionar una pregunta y devolver su código
    public int generarCodigoPregunta(List<Pregunta> listPreguntas){
        
        int codigoPregunta =(int)(Math.random()*listPreguntas.size());
        return codigoPregunta;
    }
    
    public String leerArchivo(Conocimiento a) throws FileNotFoundException, ParserException, ProbNodeNotFoundException, InvalidStateException, IncompatibleEvidenceException, NotEvaluableNetworkException, UnexpectedInferenceException {
        
        String r="";
        //Abrir archivo
        InputStream file = new FileInputStream(new File("C:\\Users\\KathyR\\Dropbox\\Décimo\\Trabajo de titulación\\Red bayesiana\\PlanEstudio5prob.pgmx"));

        //cargar la red bayesiana
        PGMXReader pgmxReader = new PGMXReader();

        //probabilidades de la red bayesiana
        ProbNet probNet = pgmxReader.loadProbNet(file, bayesNetworkName).getProbNet();

        List<ProbNode> lista = probNet.getProbNodes();
        
        for (int i = 0; i < lista.size(); i++) {
            ProbNode probNode = lista.get(i);
//            System.out.println(probNode.getName());
//            System.out.println("---- " + probNode.getProbNet());
//            System.out.println("---- " + probNode.getRelevance());
//            System.out.println("---- " + probNode.getNodeType().toString());
            r=r+(probNode.getNodeType().toString());
        }
        System.out.println();
//        nodosRed= r;
        
        //EvidenceCase
        EvidenceCase evidence = new EvidenceCase();
        evidence.addFinding(probNet, "Hermana", a.getHermana());        
        evidence.addFinding(probNet, "Hermano", a.getHermano());        
        evidence.addFinding(probNet, "Padre", a.getPadre());        
        evidence.addFinding(probNet, "Amarillo", a.getAmarillo());        
        evidence.addFinding(probNet, "Azul", a.getAzul());        
        evidence.addFinding(probNet, "Morado", a.getMorado());        
        evidence.addFinding(probNet, "Naranja", a.getNaranja());        
        evidence.addFinding(probNet, "Rojo", a.getRojo());        
        evidence.addFinding(probNet, "Verde", a.getVerde());        
        evidence.addFinding(probNet, "Brazo", a.getBrazo());        
        evidence.addFinding(probNet, "Cabeza", a.getCabeza());        
        evidence.addFinding(probNet, "Hombro", a.getHombro());        
        evidence.addFinding(probNet, "Mano", a.getMano());        
        evidence.addFinding(probNet, "Pie", a.getPie());        
        evidence.addFinding(probNet, "Pierna", a.getPierna());        
        evidence.addFinding(probNet, "Cinco", a.getCinco());        
        evidence.addFinding(probNet, "Cuatro", a.getCuatro());        
        evidence.addFinding(probNet, "Dos", a.getDos());        
        evidence.addFinding(probNet, "Tres", a.getTres());        
        evidence.addFinding(probNet, "Uno", a.getUno());        
        evidence.addFinding(probNet, "Clase", a.getClase());        
        evidence.addFinding(probNet, "Mesa", a.getMesa());        
        evidence.addFinding(probNet, "Pizarron", a.getPizarron());        
        evidence.addFinding(probNet, "Profesor", a.getProfesor());        
        evidence.addFinding(probNet, "Silla", a.getSilla());        
        

        InferenceAlgorithm variableElimination = new VariableElimination(probNet);
        variableElimination.setPreResolutionEvidence(evidence);

        Variable estado = probNet.getVariable("Estudio5");
        Variable estado1 = probNet.getVariable("Familia");
        Variable estado2 = probNet.getVariable("PartesCuerpo");
        Variable estado3 = probNet.getVariable("Numeros");
        Variable estado4 = probNet.getVariable("Colores");
        Variable estado5 = probNet.getVariable("ObjetosEscuela");
        
        ArrayList<Variable> variablesOfInterest = new ArrayList<Variable>();
        variablesOfInterest.add(estado);
        variablesOfInterest.add(estado1);
        variablesOfInterest.add(estado2);
        variablesOfInterest.add(estado3);
        variablesOfInterest.add(estado4);
        variablesOfInterest.add(estado5);

        
        HashMap<Variable, TablePotential> posteriorProbabilities= variableElimination.getProbsAndUtilities();
//String resultado="Prueba Print";
        String resultado= printResults(evidence, variablesOfInterest, posteriorProbabilities);
        return resultado;

    }

    private String printResults(EvidenceCase evidence, ArrayList<Variable> variablesOfInterest, HashMap<Variable, TablePotential> posteriorProbabilities) {
        List probabilidadFinal=new ArrayList();
        nodosRed="";
        String r="";
        System.out.println("----------------------------------------------");
        System.out.println("Evidencia:");
        r="Evidencia";
        for (Finding finding : evidence.getFindings()) {
//            System.out.print(finding.getVariable() + ": ");
            r=r+" "+finding.getVariable() + ": ";
            s.add(String.valueOf(finding.getVariable()));
            s.add(finding.getState());
            System.out.println(finding.getVariable() + ": "+finding.getState());
            r=r+finding.getState();
        }
        //nodosRed=r;
        System.out.println();
        
        // Imprimir la probabilidad posterior del estado "pasa" de cada variable de interés
        System.out.println("Probabilidade posteriores: ");
        double value;
        TablePotential posteriorProbabilitiesPotential;
        for (Variable variable : variablesOfInterest) {
            
            posteriorProbabilitiesPotential = posteriorProbabilities.get(variable);
            System.out.print(variable + ": ");
            probabilidadFinal.add(variable.getName()+": ");
            
            int stateIndex = -1;
            try {                
                stateIndex = variable.getStateIndex("presente");
                value = posteriorProbabilitiesPotential.values[stateIndex];
                
                s.add(String.valueOf(Util.roundedString(value, "0.16001")));
                
                System.out.println(Util.roundedString(value, "0.16001"));
                probabilidadFinal.add(Util.roundedString(value, "0.16001")+"  ");
                
            } catch (InvalidStateException e) {
                try {
                    stateIndex = variable.getStateIndex("sí");
                    value = posteriorProbabilitiesPotential.values[stateIndex];
                    s.add(String.valueOf(Util.roundedString(value, "0.16001")));
                    
                    System.out.println(Util.roundedString(value, "0.16001"));
                    probabilidadFinal.add(Util.roundedString(value, "0.16001")+"  ");
                    
                } catch (InvalidStateException ex) {
                                 System.err.println("State \"sí\" not found for variable \""
                        + variable.getName() + "\".");
                e.printStackTrace();
                }
                
            }    
        }
        
        for (int i = 0; i < probabilidadFinal.size(); i++) {
            nodosRed=nodosRed+" "+probabilidadFinal.get(i);
        }
        
        
        return nodosRed;
    }
    
    

    public String getNodosRed() {
        return nodosRed;
    }

    public void setNodosRed(String nodosRed) {
        this.nodosRed = nodosRed;
    }
    
}
