package Clases;

import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author AlphaGo
 */
public class Principal {

    public static void main(String[] args) {
        GestionArchivo manager_file = new GestionArchivo();

        try {
            manager_file.escribir(); //para modificar el archivo maestro con las nuevas reglas

            MotorInferencias mi = new MotorInferencias(manager_file.leerMaestro(), pedirDatos());
            JOptionPane.showMessageDialog(null, mi.encadenamientoAdelante());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BaseHechos pedirDatos() {
        boolean flag = true;
        ArrayList<String> hechos_iniciales = new ArrayList<>(0);

        JOptionPane.showMessageDialog(null, "Se solicitarán los Hechos Iniciales");
        while (flag) {
            String dato = JOptionPane.showInputDialog("Ingresa un hecho o escribe 0 para iniciar el proceso");

            try {
                int opcion = Integer.parseInt(dato);
                flag = false;
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
}
