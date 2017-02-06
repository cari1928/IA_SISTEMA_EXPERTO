/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Hamburguesas
 */
public class MotorInferencias {

    String procedimiento = "";

    ArrayList<BaseConocimientos> base_conocimientos;
    BaseHechos base_hechos;
    ArrayList<BaseConocimientos> listaPosible;

    public MotorInferencias(ArrayList<BaseConocimientos> base_conocimientos, BaseHechos base_hechos) {
        super();
        this.base_conocimientos = base_conocimientos;
        this.base_hechos = base_hechos; //algoritmo 3.2 linea 1
    }

    // nuevo
    public String encadenamientoAtras() {
        if (verificar(base_hechos.getMeta(), base_hechos)) {
            return "éxito";
        } else {
            return "fracaso";
        }
    }

    //nuevo
    public boolean verificar(String meta, BaseHechos bh) {
        boolean Verificado = false;
        ArrayList<String> nuevasMetas;

        if (buscaHecho(meta)) {
            Verificado = true;

        } else {
            ArrayList<Integer> CC = equiparaConsecuente(meta);
            procedimiento += "\nConjunto conflicto: " + CC;

            while (!CC.isEmpty() && !Verificado) {
                int r = resolver(CC);
                procedimiento += "\nRegla: " + r;

                CC.remove(CC.indexOf(r));
                nuevasMetas = extraeAntecedentes(r);
                Verificado = true;
                while (!nuevasMetas.isEmpty() && Verificado) {
                    String Meta = nuevasMetas.get(0);//secuencia anterior
                    nuevasMetas.remove(0);
                    Verificado = verificar(Meta, base_hechos);
                    if (Verificado) {
                        if (!base_hechos.base_hechos.contains(Meta)) {
                            base_hechos.base_hechos.add(Meta);
                            procedimiento += "\nBH: " + base_hechos.getBase_hechos() + "\n";
                        }
                    }
                }
            }
        }
        return Verificado;
    }

    public String encadenamientoAdelante() {
        ArrayList<Integer> conjuntoConflicto = new ArrayList<>();
        conjuntoConflicto.add(0); //algoritmo 3.2 linea 1
        BaseConocimientos nuevosHechos;
        String hechoUsuario; //hecho solicitado al usuario

        while (!Contenida() && !Vacio(conjuntoConflicto)) {
            conjuntoConflicto = equiparacion();

            System.out.println("Conjunto conflicto: " + conjuntoConflicto);
            procedimiento += "\nConjunto conflicto: " + conjuntoConflicto;
            if (!Vacio(conjuntoConflicto)) {
                int r = resolucion(conjuntoConflicto);

                System.out.println("Regla: " + conjuntoConflicto.get(r));
                procedimiento += "\nRegla: " + conjuntoConflicto.get(r);
                nuevosHechos = aplicar(conjuntoConflicto.get(r));

                if (!base_hechos.getBase_hechos().contains(nuevosHechos.getConsecuente())) {
                    actualizarBH(nuevosHechos);
                }
                System.out.println("BH: " + base_hechos.getBase_hechos() + "\n");
                procedimiento += "\nBH: " + base_hechos.getBase_hechos() + "\n";
            }
        }
        if (Contenida()) {
            return "exito";
        } else {
            if (listaPosible.size() > 0) {
                hechoUsuario = JOptionPane.showInputDialog("Hace falta un hecho para poder continuar, por favor ingréselo."
                        + "\nPresione 0 si no hay más hechos");

                if (hechoUsuario == null) {
                    return null;
                }

                if (hechoUsuario.equals("0")) {
                    procedimiento += "\nBH: " + base_hechos.getBase_hechos() + "\n";
                    return "fracaso";
                }

                if (!buscaHecho(hechoUsuario)) {
                    actualizarBH(hechoUsuario);
                }
                return encadenamientoAdelante();
            } else {
                procedimiento += "\nBH: " + base_hechos.getBase_hechos() + "\n";
                if (base_hechos.getMeta() == null) {
                    if (!VacioBH(base_hechos.getBase_hechos())) {
                        String opt = JOptionPane.showInputDialog("Presione 1 si alguno de estos hechos es su meta\n" + base_hechos.getBase_hechos());
                        if (opt == null) {
                            return null;
                        } else if (opt.equals("1")) { //si
                            return "exito";
                        } else {
                            return "fracaso";
                        }
                    } else {
                        return "fracaso";
                    }
                } else {
                    return "exito";
                }
            }
        }
    }

    private boolean buscaHecho(String hecho) {
        ArrayList<String> tmpBH = base_hechos.getBase_hechos();

        return tmpBH.contains(hecho);
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

        if (meta == null) { //sin meta
            return false;
        } else { //con meta
            return tmp.contains(meta);
        }
    }

    private boolean Vacio(ArrayList<Integer> p_conjuntoConflicto) {
        return p_conjuntoConflicto.isEmpty();
    }

    private boolean VacioBH(ArrayList<String> p_baseHechos) {
        return p_baseHechos.isEmpty();
    }

    //nuevo
    private ArrayList<String> extraeAntecedentes(int p_numeRegla) {
        return base_conocimientos.get(p_numeRegla).antecedentes;
    }

    //nuevo
    private String darConsecuente(int p_numeRegla) {
        return base_conocimientos.get(p_numeRegla).getConsecuente();
    }

    //nuevo 
    private ArrayList<Integer> equiparaConsecuente(String p_meta) {
        String tmp_consecuente;
        ArrayList<String> bh = base_hechos.getBase_hechos();
        ArrayList<Integer> tmpConflicto = new ArrayList<>();
        int estado = 0;
        int cont;
        String elementoAntecedente;
        String elementoBH;
        listaPosible = new ArrayList<>();
        boolean p;

        //Recorrer reglas
        for (int i = 0; i < base_conocimientos.size(); i++) {
            p = true;
            tmp_consecuente = base_conocimientos.get(i).consecuente;

            if (p_meta.equals(tmp_consecuente)) {
                tmpConflicto.add(i);
            }
        }
        return tmpConflicto;
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
    //nuevo

    public int resolver(ArrayList<Integer> p_cc) {
        return p_cc.get(0);
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
