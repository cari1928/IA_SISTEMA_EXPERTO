/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Decker
 */
public class BaseConocimientos implements Serializable {

    ArrayList<String> antecedentes;
    String consecuente;

    public BaseConocimientos() {
    }

    public BaseConocimientos(ArrayList<String> antecedentes, String consecuente) {
        this.antecedentes = antecedentes;
        this.consecuente = consecuente;
    }

    public ArrayList<String> getAntecedentes() {
        return antecedentes;
    }

    public void setAntecedentes(ArrayList<String> antecedentes) {
        this.antecedentes = antecedentes;
    }

    public String getConsecuente() {
        return consecuente;
    }

    public void setConsecuente(String consecuente) {
        this.consecuente = consecuente;
    }

}
