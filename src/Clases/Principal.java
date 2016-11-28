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
        MotorInferencias mi = new MotorInferencias(manager_file.leerMaestro(), pedirDatos());
        JOptionPane.showMessageDialog(null, mi.encadenamientoAdelante());
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
                hechos_iniciales.add(dato);
            }
        }

        String meta = JOptionPane.showInputDialog("Ingresa el hecho meta");
        JOptionPane.showMessageDialog(null, "Iniciando Proceso");
        return new BaseHechos(hechos_iniciales, meta);
    }
}
