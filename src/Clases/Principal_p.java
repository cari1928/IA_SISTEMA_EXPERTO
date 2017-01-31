package Clases;

import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author AlphaGo
 */
public class Principal_p {

    public static final String MSG1 = "Seleccione un tipo de Encadenamiento:\n1)Adelante\n2)Salir";
    public static final String MSG2 = "Seleccione:\n1)Con meta\n2)Sin meta";
    public static final String MSG3 = "Seleccione:\n1)Añadir Meta\n2)Modificar Meta\n3)Eliminar Meta";

    /**
     * Inicio de todo el programa
     *
     * @param args
     */
    public static void main(String[] args) {
        GestionArchivo manager_file = new GestionArchivo(); //última versión funcional        
        boolean flag = true;
        int opt;

        try {
            //para modificar el archivo maestro con las nuevas reglas
            if (!manager_file.escribir(null)) {
                JOptionPane.showMessageDialog(null, "ERROR, la sintáxis de las reglas es incorrecta");
            } else {
                while (flag) {
                    switch (opciones(MSG3, 0, 4)) {
                        case 1: //añadir meta
                            add_rule();
                            break;
                        case 2: //modificar meta

                            break;

                        case 3: //eliminar meta

                            break;
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
        System.out.println("Encadenamiento Hacia Adelante:");

        if (type == 2) { //sin meta
            meta = null;
        }

        return new BaseHechos(hechos_iniciales, meta);
    }

    public static Integer opciones(String msg, int lim_inf, int lim_sup) {
        while (true) {
            try {
                String tipo = JOptionPane.showInputDialog(msg);
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

    public static void add_rule() throws IOException {
        ArrayList<String> tmp_antecedentes = new ArrayList<>();
        ArrayList<BaseConocimientos> tmp_reglas;
        String consecuente = "", antecedente = "";
        boolean flag = true;

        while (flag) {
            try {
                antecedente = JOptionPane.showInputDialog("Ingrese un antecedente, presione 0 para terminar");
                int opt = Integer.parseInt(antecedente);

                if (tmp_antecedentes.size() != 0) {
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

        //escribir nueva regla en el archivo .txt 
        GestionArchivo ga = new GestionArchivo();
        ga.escrbirRegla(tmp_reglas);

        //escribir nueva regla en archivo maestro
        ga.escribir(tmp_reglas);
    }
}
