package Clases;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author AlphaGo
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
