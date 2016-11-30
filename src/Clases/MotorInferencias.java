package Clases;

import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author AlphaGo
 */
public class MotorInferencias {

    ArrayList<BaseConocimientos> base_conocimientos;
    BaseHechos base_hechos;
    ArrayList<BaseConocimientos> listaPosible;

    public MotorInferencias(ArrayList<BaseConocimientos> base_conocimientos, BaseHechos base_hechos) {
        super();
        this.base_conocimientos = base_conocimientos;
        this.base_hechos = base_hechos; //algoritmo 3.2 linea 1

    }

    public String encadenamientoAdelante() {
        ArrayList<Integer> conjuntoConflicto = new ArrayList<>();
        conjuntoConflicto.add(0); //algoritmo 3.2 linea 1
        BaseConocimientos nuevosHechos;
        String hechoUsuario;

        while (!Contenida() && !Vacio(conjuntoConflicto)) {
            conjuntoConflicto = equiparacion();

            if (!Vacio(conjuntoConflicto)) {
                int r = resolucion(conjuntoConflicto);

                nuevosHechos = aplicar(conjuntoConflicto.get(r));
                actualizarBH(nuevosHechos);
                System.out.println(base_hechos.getBase_hechos());
            }
        }

        if (Contenida()) {
            return "exito";
        } else {
            if (listaPosible.size() > 0) {
                hechoUsuario = JOptionPane.showInputDialog("Ingrese la premisa faltante");
                actualizarBH(hechoUsuario);
                return encadenamientoAdelante();
            } else {
                return "fracaso";
            }
        }
    }

    private BaseConocimientos aplicar(int regla) {
        base_conocimientos.get(regla).getAntecedentes().add("*");
        return base_conocimientos.get(regla);
    }

    private void actualizarBH(String p_hecho) {
        base_hechos.getBase_hechos().add(p_hecho);
    }

    private void actualizarBH(BaseConocimientos p_regla) {
        base_hechos.getBase_hechos().add(p_regla.getConsecuente());
    }

    private boolean Contenida() {
        ArrayList<String> tmp = base_hechos.getBase_hechos(); //obtengo bh
        String meta = base_hechos.getMeta(); //obtengo la meta

        return tmp.contains(meta);
    }

    private boolean Vacio(ArrayList<Integer> p_conjuntoConflicto) {
        return p_conjuntoConflicto.isEmpty();
    }

    private ArrayList<Integer> equiparacion() {
        ArrayList<String> tmp_antecedentes;
        ArrayList<String> bh = base_hechos.getBase_hechos();
        ArrayList<Integer> tmpCconflicto = new ArrayList<>();
        int estado = 0;
        int cont;
        String elementoAntecedente;
        String elementoBH;
        listaPosible = new ArrayList<>();
        boolean p;

        //recorrer todas las reglas
        for (int i = 0; i < base_conocimientos.size(); i++) {
            //se obtienen los antecedentes
            p = true;
            tmp_antecedentes = base_conocimientos.get(i).getAntecedentes();

            if (buscaAsterisco(base_conocimientos.get(i))) { //para que no cheque reglas con asterisco
                cont = 0; //para recorrer los antecedentes
                while (cont < tmp_antecedentes.size()) {

                    //para recorrer los hechos de la BH
                    for (int j = 0; j < bh.size(); j++) {
                        elementoBH = bh.get(j); //se obtiene un elemento de la BH
                        elementoAntecedente = tmp_antecedentes.get(cont); //se obtiene un elemento de los antecedentes 

                        //si son iguales...
                        if (elementoBH.equals(elementoAntecedente)) {

                            if (!listaPosible.contains(base_conocimientos.get(i)) && buscaAsterisco(base_conocimientos.get(i))) {
                                listaPosible.add(base_conocimientos.get(i));
                            }
                            estado = 1; //hubo coincidencia, sale del ciclo, no hay necesidad de revisar lo demás
                            p &= true;
                            break;

                        } else {
                            estado = 0; //no hubo coincidencia, continua el ciclo
                        }
                    }
                    ++cont;
                    if (estado == 0) {
                        p &= false;
                    }
                }
                if (p) { //hubo coincidencia
                    tmpCconflicto.add(i); //se agrega regla a conjunto conflicto
                }
            }
        }
        return tmpCconflicto;
    }

    private boolean buscaAsterisco(BaseConocimientos regla) {
        ArrayList<String> list_antecedentes = regla.getAntecedentes();

        for (int i = 0; i < list_antecedentes.size(); i++) {
            if (list_antecedentes.get(i).equals("*")) {
                return false;
            }
        }
        return true; //no encontrò asterisco
    }

    public int resolucion(ArrayList<Integer> p_conjuntoConflicto) {
//        ArrayList< BaseConocimientos> regla = new ArrayList<>(); //lista de lista de antecedentes
//
//        for (int i = 0; i < p_conjuntoConflicto.size(); i++) {
//            int posicion_regla = p_conjuntoConflicto.get(i); //se obtiene la regla del conjunto conflicto
//            regla.add(base_conocimientos.get(posicion_regla));
//        }

        //return menorElemento(regla);
        
        return 0; //elemento con menor número en posición 
    }

    public int menorElemento(ArrayList< BaseConocimientos> lista) {
        int menor = lista.get(0).getAntecedentes().size();
        BaseConocimientos tmp = lista.get(0);
        int posicion = 0;

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getAntecedentes().size() < menor) {
                menor = lista.get(i).getAntecedentes().size();
                tmp = lista.get(i);
                posicion = i;
            }
        }
        return posicion;
    }
}
