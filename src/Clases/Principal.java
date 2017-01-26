package Clases;

import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author AlphaGo
 */
public class Principal {

    /**
     * Inicio de todo el programa
     *
     * @param args
     */
    public static void main(String[] args) {
        GestionArchivo manager_file = new GestionArchivo();
        boolean flag = true;
        Integer opt;

        try {
            //para modificar el archivo maestro con las nuevas reglas
            if (!manager_file.escribir()) {
                JOptionPane.showMessageDialog(null, "ERROR, la sintáxis de las reglas es incorrecta");
            } else {
                while (flag) {
                    switch (opciones()) {
                        case 1: //enc. hacia adelante
                            MotorInferencias mi = new MotorInferencias(manager_file.leerMaestro(), pedirDatos());
                            JOptionPane.showMessageDialog(null, mi.encadenamientoAdelante());
                            break;
                        case 2:
                            break;
                        case 3:
                            flag = false;
                            JOptionPane.showMessageDialog(null, "Cerrando el programa...");
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
     * @return
     */
    public static BaseHechos pedirDatos() {
        boolean flag = true;
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

        String meta = JOptionPane.showInputDialog("Ingresa el hecho meta").toLowerCase();
        JOptionPane.showMessageDialog(null, "Iniciando Proceso");

        System.out.println("\nBase de hechos: " + hechos_iniciales);
        System.out.println("Hecho meta: " + meta + "\n");
        System.out.println("Encadenamiento Hacia Adelante:");

        return new BaseHechos(hechos_iniciales, meta);
    }

    public static Integer opciones() {
        while (true) {
            try {
                String tipo = JOptionPane.showInputDialog("Seleccione un tipo de Encadenamiento:\n1)Adelante\n2)Atrás\n3)Salir");
                int prueba = Integer.parseInt(tipo);
                if (prueba > 0 && prueba < 4) {
                    return prueba;
                }
            } catch (Exception e) {
            }
        }
    }
}
