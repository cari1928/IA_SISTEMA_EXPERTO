package Clases;

import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author AlphaGo
 */
public class Principal {

    /**
     * Contenido del menú principal
     */
    public static final String MSG1 = "Seleccione una opción:"
            + "\n1) Añadir Meta"
            + "\n2) Modificar Meta"
            + "\n3) Eliminar Meta"
            + "\n4) Encadenamiento hacia Adelante"
            + "\n5) Encadenamiento hacia Atrás"
            + "\n6) Salir";

    /**
     * Contenido del menu Enc. hacia adelante
     */
    public static final String MSG2 = "Seleccione:\n1)Con meta\n2)Sin meta";

    /**
     * Inicio de todo el programa
     *
     * @param args
     */
    public static void main(String[] args) {
        GestionArchivo manager_file = new GestionArchivo(); 
        MotorInferencias mi;
        boolean flag = true;
        Integer opt, opt_menu;

        try {
            //para modificar el archivo maestro con las nuevas reglas
            if (!manager_file.escribir()) {
                JOptionPane.showMessageDialog(null, "ERROR, la sintáxis de las reglas es incorrecta");
            } else {
                while (flag) {
                    System.out.println("-----------------------------------------------------");
                    mostrar_BC();
                    opt_menu = opciones(MSG1, 0, 7);

                    if (opt_menu == null) {
                        flag = false;
                    } else {
                        switch (opt_menu) {
                            case 1:
                                añadir_regla();
                                break;
                            case 2:
                                modificar_regla();
                                break;
                            case 3:
                                eliminar_regla();
                                break;

                            case 4: //enc. hacia adelante
                                opt = opciones(MSG2, 0, 3); //muestra otro menú de opciones y valida la opción seleccionada
                                if (opt == null) { //dio al botón de cancelar ó a la x de salir
                                    break;
                                }

                                mi = new MotorInferencias(manager_file.leerMaestro(), pedirDatos(opt));
                                JOptionPane.showMessageDialog(null, mi.encadenamientoAdelante());
                                break;

                            case 5: //enc. hacia atrás
                                mi = new MotorInferencias(manager_file.leerMaestro(), pedirDatos(1));
                                JOptionPane.showMessageDialog(null, mi.encadenamientoAtras());
                                break;

                            case 6: //salida
                                flag = false;
                                JOptionPane.showMessageDialog(null, "Cerrando el programa...");
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ingreso de los hechos iniciales y el hecho meta
     *
     * @param type int Indica si se requiere hecho meta o no
     * @return BaseHechos Objeto que almacena los hechos iniciales y el hecho
     * meta
     */
    public static BaseHechos pedirDatos(int type) {
        boolean flag = true;
        String meta;
        ArrayList<String> hechos_iniciales = new ArrayList<>(0);

        JOptionPane.showMessageDialog(null, "Se solicitarán los Hechos Iniciales");
        while (flag) {
            String dato = JOptionPane.showInputDialog("Ingresa un hecho o escribe 0 para iniciar el proceso");

            try {
                int opcion = Integer.parseInt(dato);
                if (opcion == 0) {
                    flag = false;
                } else {
                    JOptionPane.showMessageDialog(null, "Valor no admitido");
                }

            } catch (Exception e) {
                    if (!dato.equals("")) {
                    hechos_iniciales.add(dato.toLowerCase());
                }
            }
        }

        meta = "Sin meta"; //para mostrarlo en consola
        if (type == 1) { //con meta
            meta = JOptionPane.showInputDialog("Ingresa el hecho meta").toLowerCase();
        }

        JOptionPane.showMessageDialog(null, "Iniciando Proceso");
        System.out.println("\nBase de hechos: " + hechos_iniciales);
        System.out.println("Hecho meta: " + meta + "\n");

        if (type == 2) { //sin meta
            meta = null;
        }
        return new BaseHechos(hechos_iniciales, meta);
    }

    /**
     * Objetivo: Ahorrar código. \nPermite mostrar los menús de forma más
     * simple. \nLos límites definen cuántas y cuáles opciones serán válidas.
     * Ej: 0-2 opciones válidas: 1
     *
     * @param msg Texto a mostrar dentro del JOPtionPane
     * @param lim_inf Límite inferior para un ciclo
     * @param lim_sup Límite superior para un ciclo
     * @return
     */
    public static Integer opciones(String msg, int lim_inf, int lim_sup) {
        String tipo = "";
        while (true) {
            try {
                tipo = JOptionPane.showInputDialog(msg);

                if (tipo == null) {
                    return null;
                }

                int prueba = Integer.parseInt(tipo);
                if (prueba > lim_inf && prueba < lim_sup) {
                    return prueba;
                } else {
                    JOptionPane.showMessageDialog(null, "ERROR, ingrese datos válidos");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERROR, ingrese datos válidos");
            }
        }
    }

    /**
     * Permite añadir una nueva regla al archivo reglas.txt y archivo maestro
     *
     * @throws IOException En caso de que no se pueda realizar la escritura en
     * el archivo maestro
     */
    public static void añadir_regla() throws IOException {
        ArrayList<BaseConocimientos> tmp_reglas = pedirRegla();

        if (tmp_reglas != null) {
            //escribir nueva regla en el archivo .txt 
            GestionArchivo ga = new GestionArchivo();
            //true para no eliminar los datos ya existentes en el archivo
            ga.escrbirReglas(tmp_reglas, true);
            //escribir nueva regla en archivo maestro
            ga.escribir();
            JOptionPane.showMessageDialog(null, "Regla añadida correctamente");
        }
    }

    /**
     * Elimina una regla indicada por el usuario. Si la regla no existe se sigue
     * pidiendo dicho dato
     */
    public static void eliminar_regla() {
        ArrayList<BaseConocimientos> tmp_reglas;
        GestionArchivo ga = new GestionArchivo();
        boolean flag = true;
        while (flag) {
            try {
                String opt = JOptionPane.showInputDialog("Ingrese el número de la regla a eliminar");

                if (opt != null) {
                    int num_opt = Integer.parseInt(opt) - 1;
                    tmp_reglas = ga.leerReglas(); //no verificamos que sea null porque anteriormente ya se checó la sintaxis                
                    tmp_reglas.remove(num_opt); //elimina regla                
                    ga.escrbirReglas(tmp_reglas, false); //modificar archivo reglas.txt
                    ga.escribir(); //modificar archivo maestro
                    JOptionPane.showMessageDialog(null, "Regla eliminada correctamente");
                }
                flag = false;

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERROR, valor no válido");
            }
        }
    }

    /**
     * Modifica una regla especificada por el usuario. Se solicitan los nuevos
     * antecedentes y consecuente
     */
    public static void modificar_regla() {
        ArrayList<BaseConocimientos> tmp_reglas, tmp_regla;
        GestionArchivo ga = new GestionArchivo();
        boolean flag = true;

        while (flag) {
            try {
                String opt = JOptionPane.showInputDialog("Ingrese el número de la regla a modificar");

                if (opt != null) {
                    int num_opt = Integer.parseInt(opt) - 1;
                    tmp_regla = pedirRegla(); //obtener la lista con la regla modificada
                    tmp_reglas = ga.leerReglas(); //obtener todas las reglas
                    tmp_reglas.set(num_opt, tmp_regla.get(0)); //modifica el número de registro indicado
                    ga.escrbirReglas(tmp_reglas, false); //modificar archivo reglas.txt
                    ga.escribir(); //modificar archivo maestro
                    JOptionPane.showMessageDialog(null, "Regla modificada correctamente");
                }
                flag = false;

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERROR, valor no válido");
            }
        }
    }

    /**
     *
     * @return
     */
    public static ArrayList<BaseConocimientos> pedirRegla() {
        boolean flag = true;
        ArrayList<String> tmp_antecedentes = new ArrayList<>();
        String consecuente = "", antecedente = "";
        ArrayList<BaseConocimientos> tmp_reglas;

        while (flag) {
            try {
                antecedente = JOptionPane.showInputDialog("Ingrese un antecedente, presione 0 para terminar");
                if (antecedente == null) { //presionó Cancelar o la x para salir
                    return null;
                }

                int opt_antecedentes = Integer.parseInt(antecedente);

                if (!tmp_antecedentes.isEmpty()) {
                    consecuente = JOptionPane.showInputDialog("Ingrese el consecuente");
                    flag = false;
                } else {
                    JOptionPane.showMessageDialog(null, "!No ha ingresado antecedentes!");
                }

            } catch (Exception e) {
                tmp_antecedentes.add(antecedente);
            }
        }

        tmp_reglas = new ArrayList<>();
        BaseConocimientos bc = new BaseConocimientos();
        bc.setAntecedentes(tmp_antecedentes);
        bc.setConsecuente(consecuente);
        tmp_reglas.add(bc);
        return tmp_reglas;
    }

    /**
     * Muestra el contenido de la base de conocimientos
     */
    public static void mostrar_BC() {
        GestionArchivo ga = new GestionArchivo();
        ga.leerMaestro();
    }
}
