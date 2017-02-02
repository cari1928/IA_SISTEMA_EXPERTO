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

    /**
     * Constructor, inicializa BC y BH
     *
     * @param base_conocimientos
     * @param base_hechos
     */
    public MotorInferencias(ArrayList<BaseConocimientos> base_conocimientos, BaseHechos base_hechos) {
        super();
        this.base_conocimientos = base_conocimientos;
        this.base_hechos = base_hechos; //algoritmo 3.2 linea 1 || algoritmo 3.3 línea 1
    }

    /**
     * Desarrolla el algoritmo de enc. hacia adelante con o sin meta
     *
     * @return String éxito o fracaso
     */
    public String encadenamientoAdelante() {
        ArrayList<Integer> conjuntoConflicto = new ArrayList<>();
        conjuntoConflicto.add(0); //algoritmo 3.2 linea 1
        BaseConocimientos nuevosHechos;
        String hechoUsuario; //hecho solicitado al usuario

        System.out.println("Encadenamiento Hacia Adelante:");

        while (!Contenida() && !Vacio(conjuntoConflicto)) {
            conjuntoConflicto = equiparacion(null);

            System.out.println("Conjunto conflicto: " + conjuntoConflicto);

            if (!Vacio(conjuntoConflicto)) {
                int r = resolucion(conjuntoConflicto);

                System.out.println("Regla: " + conjuntoConflicto.get(r));

                nuevosHechos = aplicar(conjuntoConflicto.get(r));

                if (!base_hechos.getBase_hechos().contains(nuevosHechos.getConsecuente())) {
                    actualizarBH(nuevosHechos);
                }
                System.out.println("BH: " + base_hechos.getBase_hechos() + "\n");
            }
        }

        if (Contenida()) {
            return "exito";
        } else {
            if (listaPosible.size() > 0) {
                hechoUsuario = JOptionPane.showInputDialog("Hace falta un antecedente, por favor ingréselo");

                if (!buscaHecho(hechoUsuario)) {
                    actualizarBH(hechoUsuario);
                }
                return encadenamientoAdelante();
            } else {
                if (base_hechos.getMeta() == null) {

                    if (!Vacio(base_hechos.getBase_hechos())) {
                        String opt = JOptionPane.showInputDialog("¿Alguno es el hecho meta?\n" + base_hechos.getBase_hechos() + "\n1)Si\n2)No");
                        if (opt.equals("1")) { //si
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

    public String encadenamientoAtras() {
        String meta = base_hechos.getMeta(); //obtengo la meta
        System.out.println("Encadenamiento Hacia Atrás:");
        if (verificar(meta)) {
            base_hechos.getBase_hechos().add(meta);
            System.out.println("BH: " + base_hechos.getBase_hechos() + "\n");
            return "éxito";
        } else {
            return "fracaso";
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

    private ArrayList<Integer> eliminar(int elemento, ArrayList<Integer> arreglo) {
        base_conocimientos.get(elemento).getAntecedentes().add("*");
        arreglo.remove(0);
        return arreglo;
    }

    private ArrayList<String> eliminar(String elemento, ArrayList<String> arreglo) {
        arreglo.remove(elemento);
        return arreglo;
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

    private boolean Vacio(ArrayList p_conjuntoConflicto) {
        return p_conjuntoConflicto.isEmpty();
    }

    private ArrayList<Integer> equiparacion(String meta) {

        ArrayList<String> tmp_ante_conse; //lista de antecedentes o consecuentes
        ArrayList<String> bh = base_hechos.getBase_hechos(); //toda la bh
        ArrayList<Integer> tmpCconflicto = new ArrayList<>(); //inicializa conjunto conflicto
        int estado = 0, cont, limite;
        String elemento_ante_conse;
        String elementoBH;
        listaPosible = new ArrayList<>();
        boolean p;

        //recorrer todas las reglas
        for (int i = 0; i < base_conocimientos.size(); i++) {
            p = true;

            if (meta == null) {
                tmp_ante_conse = base_conocimientos.get(i).getAntecedentes();
            } else {
                tmp_ante_conse = new ArrayList<>();
                tmp_ante_conse.add(base_conocimientos.get(i).getConsecuente());
            }

            if (buscaAsterisco(base_conocimientos.get(i))) { //para que no cheque reglas con asterisco
                cont = 0; //para recorrer los antecedentes|consecuentes

                while (cont < tmp_ante_conse.size()) {

                    if (meta == null) {
                        limite = bh.size();
                    } else {
                        limite = 1;
                    }

                    //para recorrer los hechos de la BH
                    for (int j = 0; j < limite; j++) {
                        if (meta == null) {
                            elementoBH = bh.get(j); //se obtiene un elemento de la BH
                        } else {
                            elementoBH = meta; //se obtiene la meta
                        }

                        //se obtiene un elemento de los antecedentes|consecuentes
                        elemento_ante_conse = tmp_ante_conse.get(cont);

                        //si son iguales...
                        if (elementoBH.equals(elemento_ante_conse)) {

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

    /**
     *
     * @param p_conjuntoConflicto
     * @return
     */
    private int resolucion(ArrayList<Integer> p_conjuntoConflicto) {
        return 0; //elemento con menor número en posición 
    }

    private String seleccionaMeta(ArrayList<String> p_nuevasMetas) {
        return p_nuevasMetas.get(0);
    }

    /**
     *
     * @param lista
     * @return
     */
    private int menorElemento(ArrayList< BaseConocimientos> lista) {
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

    private boolean verificar(String meta) {
        ArrayList<Integer> conjuntoConflicto;
        ArrayList<String> nuevasMetas;
        boolean v = false, tmp_flag;
        int r;

        //se hace un movimiento en las metas para que se pueda ejecutar el método Contenida()
        //Contenida trabaja con base_hechos.meta
        //y si lo pongo como null choca con la programacion de enc. hacia adelante sin meta
        String tmp_meta = base_hechos.getMeta();
        base_hechos.setMeta(meta);
        tmp_flag = Contenida();
        base_hechos.setMeta(tmp_meta);

        if (tmp_flag) {
            return true;
        } else {
            conjuntoConflicto = equiparacion(meta);
            System.out.println("Conjunto conflicto: " + conjuntoConflicto);

            while (!Vacio(conjuntoConflicto) && !v) {
                r = resolucion(conjuntoConflicto);
                r = conjuntoConflicto.get(r);
                System.out.println("Regla: " + r);
                nuevasMetas = extraerAntecedentes(r); //para que no tome el *, por eso se pone antes de eliminar()
                conjuntoConflicto = eliminar(r, conjuntoConflicto);
                v = true;

                while (!Vacio(nuevasMetas) && v) {
                    meta = seleccionaMeta(nuevasMetas);
                    if (!meta.equals("*")) {
                        nuevasMetas = eliminar(meta, nuevasMetas);
                        v = verificar(meta);

                        if (v) {
                            if (!base_hechos.getBase_hechos().contains(meta)) {
                                base_hechos.getBase_hechos().add(meta);
                                System.out.println("BH: " + base_hechos.getBase_hechos() + "\n");
                            }
                        }
                    } else {
                        eliminar("*", nuevasMetas);
                    }
                }
            }
            return v;
        }
    }

    private ArrayList<String> extraerAntecedentes(int index) {
        return base_conocimientos.get(index).getAntecedentes();
    }
}
